/**
 * Copyright 2012 Jason Sorensen (sorensenj@smert.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package net.smert.frameworkgl.opengl.mesh;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.smert.frameworkgl.math.AABB;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.TextureType;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderable.RenderableConfiguration;
import net.smert.frameworkgl.utils.HashMapIntGeneric;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Mesh {

    private boolean hasColors;
    private boolean hasIndexes;
    private boolean hasNormals;
    private boolean hasTexCoords;
    private boolean hasVertices;
    private int renderableConfigID;
    private int totalVertices;
    private int[] indexes;
    private final AABB aabb;
    private final List<Segment> segments;
    private MeshMaterial material;

    public Mesh() {
        aabb = new AABB();
        segments = new ArrayList<>();
        material = GL.meshFactory.createMeshMaterial();
        reset();
    }

    public void addSegment(Segment segment) {

        // Check arguments
        if (segments.contains(segment)) {
            throw new IllegalArgumentException("The segment already exists");
        }

        float[] colors = segment.getData(SegmentDataType.COLOR);
        float[] normals = segment.getData(SegmentDataType.NORMAL);
        float[] texCoords = segment.getData(SegmentDataType.TEX_COORD0);
        float[] vertices = segment.getData(SegmentDataType.VERTEX);
        int elementCount = segment.getElementCount();
        RenderableConfiguration config = Renderable.configPool.get(renderableConfigID);

        if ((elementCount == 0) && !segment.hasDrawCommands()) {
            throw new IllegalArgumentException("The segment must contain at least 1 vertex or have draw commands");
        }
        if ((colors != null) && (colors.length != (elementCount * config.getColorSize()))) {
            throw new IllegalArgumentException(
                    "There needs to be one color for every element in the segment. Colors: "
                    + colors.length + " Expected: " + (elementCount * config.getColorSize()));
        }
        if ((normals != null) && (normals.length != (elementCount * config.getNormalSize()))) {
            throw new IllegalArgumentException(
                    "There needs to be one normal for every element in the segment. Normals: "
                    + normals.length + " Expected: " + (elementCount * config.getNormalSize()));
        }
        if ((texCoords != null) && (texCoords.length != (elementCount * config.getTexCoordSize()))) {
            throw new IllegalArgumentException(
                    "There needs to be one texture coord for every element in the segment. Texture Coords: "
                    + texCoords.length + " Expected: " + (elementCount * config.getTexCoordSize()));
        }
        if ((vertices != null) && (vertices.length != (elementCount * config.getVertexSize()))) {
            throw new IllegalArgumentException(
                    "There needs to be one vertex for every element in the segment. Vertices: "
                    + vertices.length + " Expected: " + (elementCount * config.getVertexSize()));
        }

        // Add segment and update totals
        segments.add(segment);
        totalVertices += segment.getElementCount();

        // Update booleans
        updateHasBooleansFromSegment();
    }

    public Mesh cloneWithSharedSegmentData() {
        Mesh newMesh = GL.meshFactory.createMesh();
        Mesh oldMesh = this;

        // Copy mesh data
        newMesh.setRenderableConfigID(oldMesh.getRenderableConfigID());
        newMesh.setIndexes(oldMesh.getIndexes());
        newMesh.getAabb().set(oldMesh.getAabb());
        newMesh.getMaterial().setShaderName(oldMesh.getMaterial().getShaderName());

        for (int i = 0; i < oldMesh.getTotalSegments(); i++) {
            Segment newSegment = GL.meshFactory.createSegment();
            Segment oldSegment = oldMesh.getSegment(i);

            // Copy segment data
            newSegment.setElementCount(oldSegment.getElementCount());
            newSegment.setMaxIndex(oldSegment.getMaxIndex());
            newSegment.setMinIndex(oldSegment.getMinIndex());
            newSegment.setPrimitiveMode(oldSegment.getPrimitiveMode());
            if (oldSegment.hasDrawCommands()) {
                newSegment.setDrawCommands(oldSegment.getDrawCommands());
            }
            newSegment.setName(oldSegment.getName());

            // Copy segment data
            if (oldSegment.getData().size() > 0) {
                Iterator<HashMapIntGeneric.Entry<float[]>> iterator = oldSegment.getData().entrySet().iterator();

                while (iterator.hasNext()) {
                    HashMapIntGeneric.Entry<float[]> entry = iterator.next();
                    int segmentDataType = entry.getKey();
                    float[] data = entry.getValue();
                    newSegment.setData(segmentDataType, data);
                }
            }

            // Copy segment material
            if (oldSegment.getMaterial() != null) {
                SegmentMaterial newMaterial = GL.meshFactory.createSegmentMaterial();
                SegmentMaterial oldMaterial = oldSegment.getMaterial();

                newMaterial.getColor().set(oldMaterial.getColor());
                newMaterial.setMaterialLightName(oldMaterial.getMaterialLightName());

                // Copy segment material textures
                if (oldMaterial.getTextures().size() > 0) {
                    Iterator<Map.Entry<TextureType, String>> iterator = oldMaterial.getTextures().entrySet().iterator();

                    while (iterator.hasNext()) {
                        Map.Entry<TextureType, String> entry = iterator.next();
                        TextureType textureType = entry.getKey();
                        String filename = entry.getValue();
                        newMaterial.setTexture(textureType, filename);
                    }
                }

                // Add segment material to segment
                newSegment.setMaterial(newMaterial);
            }

            // Add segment to mesh
            newMesh.addSegment(newSegment);
        }

        return newMesh;
    }

    public int getRenderableConfigID() {
        return renderableConfigID;
    }

    public void setRenderableConfigID(int renderableConfigID) {
        this.renderableConfigID = renderableConfigID;
    }

    public int getTotalSegments() {
        return segments.size();
    }

    public int getTotalVerticies() {
        return totalVertices;
    }

    public int[] getIndexes() {
        return indexes;
    }

    public void setIndexes(int[] indexes) {
        hasIndexes = (indexes != null);
        this.indexes = indexes;
    }

    public AABB getAabb() {
        return aabb;
    }

    public MeshMaterial getMaterial() {
        return material;
    }

    public void setMaterial(MeshMaterial material) {
        this.material = material;
    }

    /**
     * Get the unique list of textures found in all segments.
     *
     * @return
     */
    public List<String> getTextures() {
        List<String> newTextures = new ArrayList<>();
        for (Segment segment : segments) {
            SegmentMaterial segmentMaterial = segment.getMaterial();

            if (segmentMaterial == null) {
                continue;
            }

            Map<TextureType, String> textureTypeToFilename = segmentMaterial.getTextures();
            Iterator<String> iterator = textureTypeToFilename.values().iterator();
            while (iterator.hasNext()) {
                String texture = iterator.next();
                if (newTextures.contains(texture)) {
                    continue;
                }
                newTextures.add(texture);
            }
        }
        return newTextures;
    }

    public Segment getSegment(int index) {
        return segments.get(index);
    }

    public Segment getSegmentByName(String name) {
        for (Segment segment : segments) {
            if (segment.getName().equals(name)) {
                return segment;
            }
        }
        return null;
    }

    public boolean hasColors() {
        return hasColors;
    }

    public boolean hasIndexes() {
        return hasIndexes;
    }

    public boolean hasNormals() {
        return hasNormals;
    }

    public boolean hasTexCoords() {
        return hasTexCoords;
    }

    public boolean hasVertices() {
        return hasVertices;
    }

    public void removeSegment(Segment segment) {

        // Check arguments
        if (!segments.remove(segment)) {
            throw new IllegalArgumentException("The segment was not found");
        }

        // Update totals
        totalVertices -= segment.getElementCount();
    }

    public final void reset() {
        hasColors = false;
        hasIndexes = false;
        hasNormals = false;
        hasTexCoords = false;
        hasVertices = false;
        renderableConfigID = -1;
        totalVertices = 0;
        indexes = null;
        segments.clear();
    }

    public void setAllColors(float r, float g, float b, float a) {
        for (int i = 0; i < segments.size(); i++) {
            setAllColors(i, r, g, b, a);
        }
    }

    public void setAllColors(int segmentIndex, float r, float g, float b, float a) {

        // Check arguments
        if ((segmentIndex < 0) || (segmentIndex >= segments.size())) {
            throw new IllegalArgumentException("Invalid segment index: " + segmentIndex);
        }

        // Get configuration from the pool
        RenderableConfiguration config = Renderable.configPool.get(renderableConfigID);
        int colorSize = config.getColorSize();

        // Change colors for the segment
        Segment segment = segments.get(segmentIndex);
        int elementCount = segment.getElementCount();
        int totalColorSize = elementCount * colorSize;
        float[] colors = new float[totalColorSize];
        for (int i = 0; i < elementCount; i++) {
            int offset = i * colorSize;
            colors[offset + 0] = r;
            colors[offset + 1] = g;
            colors[offset + 2] = b;
            if (colorSize == 4) {
                colors[offset + 3] = a;
            }
        }

        // Set color data
        segment.setData(SegmentDataType.COLOR, colors);
    }

    public void setAllMaterialColors(float r, float g, float b, float a) {
        for (int i = 0; i < segments.size(); i++) {
            setAllMaterialColors(i, r, g, b, a);
        }
    }

    public void setAllMaterialColors(int segmentIndex, float r, float g, float b, float a) {

        // Check arguments
        if ((segmentIndex < 0) || (segmentIndex >= segments.size())) {
            throw new IllegalArgumentException("Invalid segment index: " + segmentIndex);
        }

        // Change colors for the segment material
        Segment segment = segments.get(segmentIndex);
        SegmentMaterial segmentMaterial = segment.getMaterial();

        if (segmentMaterial != null) {
            segmentMaterial.getColor().set(r, g, b, a);
        }
    }

    public void updateHasBooleansFromSegment() {
        hasColors = false;
        hasIndexes = false;
        hasNormals = false;
        hasTexCoords = false;
        hasVertices = false;

        // Update booleans
        for (Segment segment : segments) {
            float[] colors = segment.getData(SegmentDataType.COLOR);
            float[] normals = segment.getData(SegmentDataType.NORMAL);
            float[] texCoords = segment.getData(SegmentDataType.TEX_COORD0);
            float[] vertices = segment.getData(SegmentDataType.VERTEX);
            hasColors |= (colors != null);
            hasNormals |= (normals != null);
            hasTexCoords |= (texCoords != null);
            hasVertices |= (vertices != null);
            hasIndexes |= ((getIndexes() != null) && (getIndexes().length > 0));
        }
    }

}
