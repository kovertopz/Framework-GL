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
package net.smert.jreactphysics3d.framework.opengl.mesh;

import java.util.ArrayList;
import java.util.List;
import net.smert.jreactphysics3d.framework.math.AABB;
import net.smert.jreactphysics3d.framework.math.Vector3f;
import net.smert.jreactphysics3d.framework.math.Vector4f;
import net.smert.jreactphysics3d.framework.opengl.GL;
import net.smert.jreactphysics3d.framework.opengl.constants.Primitives;
import net.smert.jreactphysics3d.framework.opengl.renderable.Renderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.RenderableConfiguration;
import net.smert.jreactphysics3d.framework.utils.Color;
import net.smert.jreactphysics3d.framework.utils.ListUtils;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Tessellator {

    private final static int INITIAL_SEGMENTS = 16;
    private final static int INITIAL_ELEMENTS_PER_SEGMENT = INITIAL_SEGMENTS * 1024;
    private final static int INITIAL_COMPONENT_ELEMENTS_PER_SEGMENT = INITIAL_ELEMENTS_PER_SEGMENT * 4;

    private boolean convert; // Doesn't get reset
    private boolean convertQuads; // Doesn't get reset
    private boolean convertTriangleFans; // Doesn't get reset
    private boolean convertTriangleStrips; // Doesn't get reset
    private boolean enableConversionForPrimitiveMode;
    private boolean hasColorsToConvert;
    private boolean hasNormalsToConvert;
    private boolean hasTexCoordsToConvert;
    private int colorConversionCount;
    private int elementCount;
    private int maxIndex;
    private int minIndex;
    private int normalConversionCount;
    private int primitiveMode;
    private int texCoordConversionCount;
    private int triangleStripConversionOutput; // Doesn't get reset
    private int vertexConversionCount;
    private int vertexIndex;
    private final AABB aabb;
    private final Color color;
    private final Color colorConversion0;
    private final Color colorConversion1;
    private final Color colorConversion2;
    private final Color colorConversion3;
    private final List<Float> colors;
    private final List<Float> normals;
    private final List<Float> texCoords;
    private final List<Float> vertices;
    private final List<Integer> elementCounts;
    private final List<Integer> maxIndexes;
    private final List<Integer> minIndexes;
    private final List<Integer> primitiveModes;
    private final List<Integer> vertexIndexes;
    private final List<Segment> segments;
    private final RenderableConfiguration config;
    private final Vector3f normal;
    private final Vector3f normalCreation1;
    private final Vector3f normalCreation2;
    private final Vector3f normalConversion0;
    private final Vector3f normalConversion1;
    private final Vector3f normalConversion2;
    private final Vector3f normalConversion3;
    private final Vector3f texCoord;
    private final Vector3f texCoordConversion0;
    private final Vector3f texCoordConversion1;
    private final Vector3f texCoordConversion2;
    private final Vector3f texCoordConversion3;
    private final Vector4f vertex;
    private final Vector4f vertexConversion0;
    private final Vector4f vertexConversion1;
    private final Vector4f vertexConversion2;
    private final Vector4f vertexConversion3;

    public Tessellator() {
        convert = true;
        convertQuads = true;
        convertTriangleFans = true;
        convertTriangleStrips = true;
        triangleStripConversionOutput = Primitives.TRIANGLES;
        aabb = new AABB();
        color = new Color();
        colorConversion0 = new Color();
        colorConversion1 = new Color();
        colorConversion2 = new Color();
        colorConversion3 = new Color();
        colors = new ArrayList<>(INITIAL_ELEMENTS_PER_SEGMENT); // One per vertex
        normals = new ArrayList<>(INITIAL_COMPONENT_ELEMENTS_PER_SEGMENT);  // Three per vertex
        texCoords = new ArrayList<>(INITIAL_COMPONENT_ELEMENTS_PER_SEGMENT);  // Up to three per vertex
        vertices = new ArrayList<>(INITIAL_COMPONENT_ELEMENTS_PER_SEGMENT);  // Up to four per vertex
        elementCounts = new ArrayList<>(INITIAL_SEGMENTS); // One per segment
        maxIndexes = new ArrayList<>(INITIAL_SEGMENTS); // One per segment
        minIndexes = new ArrayList<>(INITIAL_SEGMENTS); // One per segment
        primitiveModes = new ArrayList<>(INITIAL_SEGMENTS); // One per segment
        vertexIndexes = new ArrayList<>(INITIAL_ELEMENTS_PER_SEGMENT); // One per vertex
        segments = new ArrayList<>(INITIAL_SEGMENTS);
        config = GL.mf.createRenderableConfiguration();
        normal = new Vector3f();
        normalCreation1 = new Vector3f();
        normalCreation2 = new Vector3f();
        normalConversion0 = new Vector3f();
        normalConversion1 = new Vector3f();
        normalConversion2 = new Vector3f();
        normalConversion3 = new Vector3f();
        texCoord = new Vector3f();
        texCoordConversion0 = new Vector3f();
        texCoordConversion1 = new Vector3f();
        texCoordConversion2 = new Vector3f();
        texCoordConversion3 = new Vector3f();
        vertex = new Vector4f();
        vertexConversion0 = new Vector4f();
        vertexConversion1 = new Vector4f();
        vertexConversion2 = new Vector4f();
        vertexConversion3 = new Vector4f();
        reset();
    }

    private void calculateNormal(Vector3f pos1, Vector3f pos2, Vector3f pos3) {
        // CCW order
        normalCreation1.set(pos3).subtract(pos2); // pos2 is base pointing to pos3, index finger
        normalCreation2.set(pos1).subtract(pos2); // pos2 is base pointing to pos1, middle finger
        normal.set(normalCreation1).cross(normalCreation2); // right hand rule, thumb
        normal.normalize();
    }

    private void findAABBMaxMin(Vector4f vertex) {
        aabb.setMax(vertex);
        aabb.setMin(vertex);
    }

    private void internalAddColor(Color color) {
        if (needToConvert()) {
            internalAddColorConversion(color);
            return;
        }
        internalAddColorToList(color);
    }

    private void internalAddColorConversion(Color color) {
        if (colorConversionCount == 0) {
            colorConversion0.set(color);
        } else if (colorConversionCount == 1) {
            colorConversion1.set(color);
        } else if (colorConversionCount == 2) {
            colorConversion2.set(color);
        } else if (colorConversionCount == 3) {
            colorConversion3.set(color);
        }
        hasColorsToConvert = true;
        colorConversionCount++;
    }

    private void internalAddColorToList(Color color) {
        colors.add(color.getR());
        colors.add(color.getG());
        colors.add(color.getB());
        if (config.getColorSize() == 4) {
            colors.add(color.getA());
        }
    }

    private void internalAddNormal(Vector3f normal) {
        if (needToConvert()) {
            internalAddNormalConversion(normal);
            return;
        }
        internalAddNormalToList(normal);
    }

    private void internalAddNormalConversion(Vector3f normal) {
        if (normalConversionCount == 0) {
            normalConversion0.set(normal);
        } else if (normalConversionCount == 1) {
            normalConversion1.set(normal);
        } else if (normalConversionCount == 2) {
            normalConversion2.set(normal);
        } else if (normalConversionCount == 3) {
            normalConversion3.set(normal);
        }
        hasNormalsToConvert = true;
        normalConversionCount++;
    }

    private void internalAddNormalToList(Vector3f normal) {
        normals.add(normal.getX());
        normals.add(normal.getY());
        normals.add(normal.getZ());
    }

    private void internalAddTexCoord(Vector3f texCoord) {
        if (needToConvert()) {
            internalAddTexCoordConversion(texCoord);
            return;
        }
        internalAddTexCoordToList(texCoord);
    }

    private void internalAddTexCoordConversion(Vector3f texCoord) {
        if (texCoordConversionCount == 0) {
            texCoordConversion0.set(texCoord);
        } else if (texCoordConversionCount == 1) {
            texCoordConversion1.set(texCoord);
        } else if (texCoordConversionCount == 2) {
            texCoordConversion2.set(texCoord);
        } else if (texCoordConversionCount == 3) {
            texCoordConversion3.set(texCoord);
        }
        hasTexCoordsToConvert = true;
        texCoordConversionCount++;
    }

    private void internalAddTexCoordToList(Vector3f texCoord) {
        texCoords.add(texCoord.getX());
        texCoords.add(texCoord.getY());
        if (config.getTexCoordSize() == 3) {
            texCoords.add(texCoord.getZ());
        }
    }

    private void internalAddVertex(Vector4f vertex) {
        findAABBMaxMin(vertex);
        if (needToConvert()) {
            internalAddVertexConversion(vertex);
            return;
        }
        internalAddVertexToList(vertex);
    }

    private void internalAddVertexConversion(Vector4f vertex) {
        if (vertexConversionCount == 0) {
            vertexConversion0.set(vertex);
        } else if (vertexConversionCount == 1) {
            vertexConversion1.set(vertex);
        } else if (vertexConversionCount == 2) {
            vertexConversion2.set(vertex);
        } else if (vertexConversionCount == 3) {
            vertexConversion3.set(vertex);
        }
        vertexConversionCount++;
    }

    private void internalAddVertexToList(Vector4f vertex) {
        vertices.add(vertex.getX());
        vertices.add(vertex.getY());
        vertices.add(vertex.getZ());
        if (config.getVertexSize() == 4) {
            vertices.add(vertex.getW());
        }
        vertexIndexes.add(vertexIndex);
        elementCount++;
        maxIndex = vertexIndex++;
    }

    private void internalConvertQuadToTriangles() {
        if (hasColorsToConvert == true) {
            internalAddColorToList(colorConversion0); // Top right
            internalAddColorToList(colorConversion1); // Top left
            internalAddColorToList(colorConversion2); // Bottom left
            internalAddColorToList(colorConversion2); // Bottom left
            internalAddColorToList(colorConversion3); // Bottom right
            internalAddColorToList(colorConversion0); // Top right
        }
        if (hasNormalsToConvert == true) {
            internalAddNormalToList(normalConversion0);
            internalAddNormalToList(normalConversion1);
            internalAddNormalToList(normalConversion2);
            internalAddNormalToList(normalConversion2);
            internalAddNormalToList(normalConversion3);
            internalAddNormalToList(normalConversion0);
        }
        if (hasTexCoordsToConvert == true) {
            internalAddTexCoordToList(texCoordConversion0);
            internalAddTexCoordToList(texCoordConversion1);
            internalAddTexCoordToList(texCoordConversion2);
            internalAddTexCoordToList(texCoordConversion2);
            internalAddTexCoordToList(texCoordConversion3);
            internalAddTexCoordToList(texCoordConversion0);
        }
        internalAddVertexToList(vertexConversion0);
        internalAddVertexToList(vertexConversion1);
        internalAddVertexToList(vertexConversion2);
        internalAddVertexToList(vertexConversion2);
        internalAddVertexToList(vertexConversion3);
        internalAddVertexToList(vertexConversion0);
    }

    private void internalConvertTriangleFanToTriangles() {
        if (hasColorsToConvert == true) {
            internalAddColorToList(colorConversion0); // Center
            internalAddColorToList(colorConversion1); // Bottom right
            internalAddColorToList(colorConversion2); // Bottom left
        }
        if (hasNormalsToConvert == true) {
            internalAddNormalToList(normalConversion0);
            internalAddNormalToList(normalConversion1);
            internalAddNormalToList(normalConversion2);
        }
        if (hasTexCoordsToConvert == true) {
            internalAddTexCoordToList(texCoordConversion0);
            internalAddTexCoordToList(texCoordConversion1);
            internalAddTexCoordToList(texCoordConversion2);
        }
        internalAddVertexToList(vertexConversion0);
        internalAddVertexToList(vertexConversion1);
        internalAddVertexToList(vertexConversion2);
    }

    private void internalConvertTriangleStripToQuads() {
        if (hasColorsToConvert == true) {
            internalAddColorToList(colorConversion3); // Top right
            internalAddColorToList(colorConversion1); // Top left
            internalAddColorToList(colorConversion0); // Bottom left
            internalAddColorToList(colorConversion2); // Bottom right
        }
        if (hasNormalsToConvert == true) {
            internalAddNormalToList(normalConversion3);
            internalAddNormalToList(normalConversion1);
            internalAddNormalToList(normalConversion0);
            internalAddNormalToList(normalConversion2);
        }
        if (hasTexCoordsToConvert == true) {
            internalAddTexCoordToList(texCoordConversion3);
            internalAddTexCoordToList(texCoordConversion1);
            internalAddTexCoordToList(texCoordConversion0);
            internalAddTexCoordToList(texCoordConversion2);
        }
        internalAddVertexToList(vertexConversion3);
        internalAddVertexToList(vertexConversion1);
        internalAddVertexToList(vertexConversion0);
        internalAddVertexToList(vertexConversion2);
    }

    private void internalConvertTriangleStripToTriangles() {
        if (hasColorsToConvert == true) {
            internalAddColorToList(colorConversion3); // Top right
            internalAddColorToList(colorConversion1); // Top left
            internalAddColorToList(colorConversion0); // Bottom left
            internalAddColorToList(colorConversion0); // Bottom left
            internalAddColorToList(colorConversion2); // Bottom right
            internalAddColorToList(colorConversion3); // Top Right
        }
        if (hasNormalsToConvert == true) {
            internalAddNormalToList(normalConversion3);
            internalAddNormalToList(normalConversion1);
            internalAddNormalToList(normalConversion0);
            internalAddNormalToList(normalConversion0);
            internalAddNormalToList(normalConversion2);
            internalAddNormalToList(normalConversion3);
        }
        if (hasTexCoordsToConvert == true) {
            internalAddTexCoordToList(texCoordConversion3);
            internalAddTexCoordToList(texCoordConversion1);
            internalAddTexCoordToList(texCoordConversion0);
            internalAddTexCoordToList(texCoordConversion0);
            internalAddTexCoordToList(texCoordConversion2);
            internalAddTexCoordToList(texCoordConversion3);
        }
        internalAddVertexToList(vertexConversion3);
        internalAddVertexToList(vertexConversion1);
        internalAddVertexToList(vertexConversion0);
        internalAddVertexToList(vertexConversion0);
        internalAddVertexToList(vertexConversion2);
        internalAddVertexToList(vertexConversion3);
    }

    private boolean needToConvert() {
        if (enableConversionForPrimitiveMode == true) {
            return ((convertQuads && (primitiveMode == Primitives.QUADS))
                    || (convertTriangleFans && (primitiveMode == Primitives.TRIANGLE_FAN))
                    || (convertTriangleStrips && (primitiveMode == Primitives.TRIANGLE_STRIP)));
        }
        return false;
    }

    public void addColor(float r, float g, float b) {
        color.set(r, g, b, 1.0f);
        internalAddColor(color);
    }

    public void addColor(float r, float g, float b, float a) {
        color.set(r, g, b, a);
        internalAddColor(color);
    }

    public void addNormal(float x, float y, float z) {
        normal.set(x, y, z);
        internalAddNormal(normal);
    }

    public void addNormal(Vector3f normal) {
        this.normal.set(normal);
        internalAddNormal(this.normal);
    }

    public void addNormal(Vector3f pos1, Vector3f pos2, Vector3f pos3) {
        calculateNormal(pos1, pos2, pos3);
        internalAddNormal(normal);
    }

    public void addNormalAgain() {
        internalAddNormal(normal);
    }

    public void addTexCoord(float s, float t) {
        texCoord.set(s, t, 0);
        internalAddTexCoord(texCoord);
    }

    public void addTexCoord(float s, float t, float r) {
        config.setTexCoordSize(3);
        texCoord.set(s, t, r);
        internalAddTexCoord(texCoord);
    }

    public void addVertex(float x, float y, float z) {
        vertex.set(x, y, z, 1.0f);
        internalAddVertex(vertex);
    }

    public void addVertex(Vector3f vertex) {
        this.vertex.set(vertex, 1.0f);
        internalAddVertex(this.vertex);
    }

    public void convert() {
        if (enableConversionForPrimitiveMode == true) {
            if ((primitiveMode == Primitives.QUADS) && (convertQuads == true)) {
                if (vertexConversionCount == 4) {
                    internalConvertQuadToTriangles();
                    colorConversionCount = 0;
                    normalConversionCount = 0;
                    texCoordConversionCount = 0;
                    vertexConversionCount = 0;
                }
            } else if ((primitiveMode == Primitives.TRIANGLE_FAN) && (convertTriangleFans == true)) {
                if (vertexConversionCount == 3) {
                    internalConvertTriangleFanToTriangles();
                    colorConversion1.set(colorConversion2);
                    normalConversion1.set(normalConversion2);
                    texCoordConversion1.set(texCoordConversion2);
                    vertexConversion1.set(vertexConversion2);
                    colorConversionCount = 2;
                    normalConversionCount = 2;
                    texCoordConversionCount = 2;
                    vertexConversionCount = 2;
                }
            } else if ((primitiveMode == Primitives.TRIANGLE_STRIP) && (convertTriangleStrips == true)) {
                if (vertexConversionCount == 4) {
                    if (triangleStripConversionOutput == Primitives.QUADS) {
                        internalConvertTriangleStripToQuads();
                    } else if (triangleStripConversionOutput == Primitives.TRIANGLES) {
                        internalConvertTriangleStripToTriangles();
                    }
                    colorConversion0.set(colorConversion2);
                    colorConversion1.set(colorConversion3);
                    normalConversion0.set(normalConversion2);
                    normalConversion1.set(normalConversion3);
                    texCoordConversion0.set(texCoordConversion2);
                    texCoordConversion1.set(texCoordConversion3);
                    vertexConversion0.set(vertexConversion2);
                    vertexConversion1.set(vertexConversion3);
                    colorConversionCount = 2;
                    normalConversionCount = 2;
                    texCoordConversionCount = 2;
                    vertexConversionCount = 2;
                }
            }
        }
    }

    public Segment createSegment(String name) {
        Segment segment = GL.mf.createSegment();

        segment.setElementCount(elementCount);
        segment.setMaxIndex(maxIndex);
        segment.setMinIndex(minIndex);
        segment.setName(name);
        segment.setPrimitiveMode(primitiveMode);

        if (colors.size() > 0) {
            segment.setColors(getColors());
        }
        if (normals.size() > 0) {
            segment.setNormals(getNormals());
        }
        if (texCoords.size() > 0) {
            segment.setTexCoords(getTexCoords());
        }
        if (vertices.size() > 0) {
            segment.setVertices(getVertices());
        }

        return segment;
    }

    public boolean isConvert() {
        return convert;
    }

    public boolean isConvertQuads() {
        return convertQuads;
    }

    public boolean isConvertTriangleFans() {
        return convertTriangleFans;
    }

    public boolean isConvertTriangleStrips() {
        return convertTriangleStrips;
    }

    public float[] getColors() {
        return ListUtils.ToPrimitiveFloatArray(colors);
    }

    public float[] getNormals() {
        return ListUtils.ToPrimitiveFloatArray(normals);
    }

    public float[] getTexCoords() {
        return ListUtils.ToPrimitiveFloatArray(texCoords);
    }

    public float[] getVertices() {
        return ListUtils.ToPrimitiveFloatArray(vertices);
    }

    public int getColorsCount() {
        return colors.size();
    }

    public int getNormalsCount() {
        return normals.size();
    }

    public int getOrAddConfigToPool() {
        RenderableConfiguration newConfig = this.config.clone();
        return Renderable.configPool.getOrAdd(newConfig);
    }

    public int getTexCoordsCount() {
        return texCoords.size();
    }

    public int getVerticesCount() {
        return vertices.size();
    }

    public int getVertexIndexesCount() {
        return vertexIndexes.size();
    }

    public int[] getElementCounts() {
        return ListUtils.ToPrimitiveIntArray(elementCounts);
    }

    public int[] getMaxIndexes() {
        return ListUtils.ToPrimitiveIntArray(maxIndexes);
    }

    public int[] getMinIndexes() {
        return ListUtils.ToPrimitiveIntArray(minIndexes);
    }

    public int[] getPrimitiveModes() {
        return ListUtils.ToPrimitiveIntArray(primitiveModes);
    }

    public int[] getVertexIndexes() {
        return ListUtils.ToPrimitiveIntArray(vertexIndexes);
    }

    public RenderableConfiguration getRenderableConfiguration() {
        return config;
    }

    public void getAABB(AABB aabb) {
        aabb.getMax().set(this.aabb.getMax());
        aabb.getMin().set(this.aabb.getMin());
    }

    public final void reset() {
        enableConversionForPrimitiveMode = false;
        hasColorsToConvert = false;
        hasNormalsToConvert = false;
        hasTexCoordsToConvert = false;
        colorConversionCount = 0;
        elementCount = 0;
        maxIndex = -1;
        minIndex = -1;
        normalConversionCount = 0;
        primitiveMode = -1;
        texCoordConversionCount = 0;
        vertexConversionCount = 0;
        vertexIndex = 0;
        aabb.setMax(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        aabb.setMin(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
        color.setWhite();
        colorConversion0.setWhite();
        colorConversion1.setWhite();
        colorConversion2.setWhite();
        colorConversion3.setWhite();
        colors.clear();
        normals.clear();
        texCoords.clear();
        vertices.clear();
        elementCounts.clear();
        maxIndexes.clear();
        minIndexes.clear();
        primitiveModes.clear();
        vertexIndexes.clear();
        segments.clear();
        config.reset();
        normal.zero();
        normalCreation1.zero();
        normalCreation2.zero();
        normalConversion0.zero();
        normalConversion1.zero();
        normalConversion2.zero();
        normalConversion3.zero();
        texCoordConversion0.zero();
        texCoordConversion1.zero();
        texCoordConversion2.zero();
        texCoordConversion3.zero();
        vertexConversion0.zero();
        vertexConversion1.zero();
        vertexConversion2.zero();
        vertexConversion3.zero();
    }

    public void setConvert(boolean convert) {
        this.convert = convert;
    }

    public void setConvertQuads(boolean convertQuads) {
        this.convertQuads = convertQuads;
    }

    public void setConvertTriangleFans(boolean convertTriangleFans) {
        this.convertTriangleFans = convertTriangleFans;
    }

    public void setConvertTriangleStrips(boolean convertTriangleStrips) {
        this.convertTriangleStrips = convertTriangleStrips;
    }

    public void setTriangleStripConversionOutput(int triangleStripConversionOutput) {
        this.triangleStripConversionOutput = triangleStripConversionOutput;
    }

    public void start(int primitiveMode) {
        if (convert == true) {
            enableConversionForPrimitiveMode
                    = ((primitiveMode != Primitives.LINES) && (primitiveMode != Primitives.TRIANGLES));
        }
        elementCount = 0;
        maxIndex = -1;
        minIndex = vertexIndex; // Save first index
        this.primitiveMode = primitiveMode;
        primitiveModes.add(this.primitiveMode);
    }

    public void stop() {
        if (enableConversionForPrimitiveMode == true) {
            colorConversionCount = 0;
            normalConversionCount = 0;
            texCoordConversionCount = 0;
            vertexConversionCount = 0;
            if ((primitiveMode == Primitives.QUADS) && (convertQuads == true)) {
                primitiveMode = Primitives.TRIANGLES;
                primitiveModes.set(primitiveModes.size() - 1, primitiveMode);
            } else if ((primitiveMode == Primitives.TRIANGLE_FAN) && (convertTriangleFans == true)) {
                primitiveMode = Primitives.TRIANGLES;
                primitiveModes.set(primitiveModes.size() - 1, primitiveMode);
            } else if ((primitiveMode == Primitives.TRIANGLE_STRIP) && (convertTriangleStrips == true)) {
                primitiveMode = triangleStripConversionOutput;
                primitiveModes.set(primitiveModes.size() - 1, triangleStripConversionOutput);
            }
        }
        elementCounts.add(elementCount);
        maxIndexes.add(maxIndex);
        minIndexes.add(minIndex);
    }

}
