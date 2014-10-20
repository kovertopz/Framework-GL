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
import net.smert.frameworkgl.opengl.TextureType;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderable.RenderableConfiguration;

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
    private final List<Segment> segments;
    private final AABB aabb;

    public Mesh() {
        segments = new ArrayList<>();
        aabb = new AABB();
        reset();
    }

    public void addSegment(Segment segment) {

        // Check arguments
        if (segments.contains(segment)) {
            throw new IllegalArgumentException("The segment already exists");
        }
        int elementCount = segment.getElementCount();
        RenderableConfiguration config = Renderable.configPool.get(renderableConfigID);
        if ((elementCount == 0) && !segment.hasDrawCommands()) {
            throw new IllegalArgumentException("The segment must contain at least 1 vertex or have draw commands");
        }
        if ((segment.getColors().length != 0)
                && (segment.getColors().length != elementCount * config.getColorSize())) {
            throw new IllegalArgumentException(
                    "There needs to be one color for every element in the segment. Colors: "
                    + segment.getColors().length + " Expected: " + elementCount * config.getColorSize());
        }
        if ((segment.getNormals().length != 0)
                && (segment.getNormals().length != elementCount * config.getNormalSize())) {
            throw new IllegalArgumentException(
                    "There needs to be one normal for every element in the segment. Normals: "
                    + segment.getNormals().length + " Expected: " + elementCount * config.getNormalSize());
        }
        if ((segment.getTexCoords().length != 0)
                && (segment.getTexCoords().length != elementCount * config.getTexCoordSize())) {
            throw new IllegalArgumentException(
                    "There needs to be one texture coord for every element in the segment. Texture Coords: "
                    + segment.getTexCoords().length + " Expected: " + elementCount * config.getTexCoordSize());
        }
        if ((segment.getVertices().length != 0)
                && (segment.getVertices().length != elementCount * config.getVertexSize())) {
            throw new IllegalArgumentException(
                    "There needs to be one vertex for every element in the segment. Vertices: "
                    + segment.getVertices().length + " Expected: " + elementCount * config.getVertexSize());
        }

        // Add segment and update totals
        segments.add(segment);
        totalVertices += segment.getElementCount();

        // Update booleans
        updateBooleansFromSegment();
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
        this.indexes = indexes;
    }

    public AABB getAabb() {
        return aabb;
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

            Map<TextureType, String> textures = segmentMaterial.getTextures();
            Iterator<String> iterator = textures.values().iterator();
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
        segment.setColors(colors);
    }

    public void updateBooleansFromSegment() {
        hasColors = false;
        hasIndexes = false;
        hasNormals = false;
        hasTexCoords = false;
        hasVertices = false;

        // Update booleans
        for (Segment segment : segments) {
            hasColors |= ((segment.getColors() != null) && (segment.getColors().length > 0));
            hasNormals |= ((segment.getNormals() != null) && (segment.getNormals().length > 0));
            hasTexCoords |= ((segment.getTexCoords() != null) && (segment.getTexCoords().length > 0));
            hasVertices |= ((segment.getVertices() != null) && (segment.getVertices().length > 0));
            hasIndexes |= ((getIndexes() != null) && (getIndexes().length > 0));
        }
    }

}
