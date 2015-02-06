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
import java.util.List;
import net.smert.frameworkgl.math.AABB;
import net.smert.frameworkgl.math.Vector2f;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.math.Vector4f;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.constants.Primitives;
import net.smert.frameworkgl.opengl.renderable.RenderableConfiguration;
import net.smert.frameworkgl.utils.Color;
import net.smert.frameworkgl.utils.ListUtils;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Tessellator {

    private final static int INITIAL_SEGMENTS = 16;
    private final static int INITIAL_ELEMENTS_PER_SEGMENT = INITIAL_SEGMENTS * 1024;
    private final static int INITIAL_COMPONENT_ELEMENTS_PER_SEGMENT = INITIAL_ELEMENTS_PER_SEGMENT * 4;

    private boolean convertToTriangles; // Doesn't get reset
    private boolean enableConversionForPrimitiveMode;
    private boolean isStarted;
    private int elementCount;
    private int maxIndex;
    private int minIndex;
    private int primitiveMode;
    private int vertexIndex;
    private final AABB aabb;
    private final Color color;
    private final ConversionState conversionState;
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
    private RenderableConfiguration config;
    private final Vector3f normal;
    private final Vector3f texCoord;
    private final Vector4f localPosition;
    private final Vector4f vertex;

    public Tessellator() {
        convertToTriangles = true;
        aabb = new AABB();
        color = new Color();
        conversionState = new ConversionState();
        colors = new ArrayList<>(INITIAL_COMPONENT_ELEMENTS_PER_SEGMENT); // Up to four per vertex
        normals = new ArrayList<>(INITIAL_COMPONENT_ELEMENTS_PER_SEGMENT);  // Three per vertex
        texCoords = new ArrayList<>(INITIAL_COMPONENT_ELEMENTS_PER_SEGMENT);  // Up to three per vertex
        vertices = new ArrayList<>(INITIAL_COMPONENT_ELEMENTS_PER_SEGMENT);  // Up to four per vertex
        elementCounts = new ArrayList<>(INITIAL_SEGMENTS); // One per segment
        maxIndexes = new ArrayList<>(INITIAL_SEGMENTS); // One per segment
        minIndexes = new ArrayList<>(INITIAL_SEGMENTS); // One per segment
        primitiveModes = new ArrayList<>(INITIAL_SEGMENTS); // One per segment
        vertexIndexes = new ArrayList<>(INITIAL_ELEMENTS_PER_SEGMENT); // One per vertex
        segments = new ArrayList<>(INITIAL_SEGMENTS);
        normal = new Vector3f();
        texCoord = new Vector3f();
        localPosition = new Vector4f();
        vertex = new Vector4f();
        reset();
    }

    private void findAABBMaxMin(Vector4f vertex) {
        aabb.getMax().setMax(vertex.getX(), vertex.getY(), vertex.getZ());
        aabb.getMin().setMin(vertex.getX(), vertex.getY(), vertex.getZ());
    }

    private void internalAddColor(Color color) {
        if (enableConversionForPrimitiveMode) {
            conversionState.addColorConversion(color);
            return;
        }
        internalAddColorToList(color);
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
        if (enableConversionForPrimitiveMode) {
            conversionState.addNormalConversion(normal);
            return;
        }
        internalAddNormalToList(normal);
    }

    private void internalAddNormalToList(Vector3f normal) {
        normals.add(normal.getX());
        normals.add(normal.getY());
        normals.add(normal.getZ());
    }

    private void internalAddTexCoord(Vector3f texCoord) {
        if (enableConversionForPrimitiveMode) {
            conversionState.addTexCoordConversion(texCoord);
            return;
        }
        internalAddTexCoordToList(texCoord);
    }

    private void internalAddTexCoordToList(Vector3f texCoord) {
        texCoords.add(texCoord.getX());
        texCoords.add(texCoord.getY());
        if (config.getTexCoordSize() == 3) {
            texCoords.add(texCoord.getZ());
        }
    }

    private void internalAddVertex(Vector4f vertex) {
        vertex.add(localPosition);
        findAABBMaxMin(vertex);
        if (enableConversionForPrimitiveMode) {
            conversionState.addVertexConversion(vertex);
            conversionState.convert(this);
            return;
        }
        internalAddVertexToList(vertex);
    }

    private void internalAddVertexToList(Vector4f vertex) {
        vertices.add(vertex.getX());
        vertices.add(vertex.getY());
        if (config.getVertexSize() >= 3) {
            vertices.add(vertex.getZ());
        }
        if (config.getVertexSize() == 4) {
            vertices.add(vertex.getW());
        }
        vertexIndexes.add(vertexIndex);
        elementCount++;
        maxIndex = vertexIndex++;
    }

    public void addColor(float r, float g, float b) {
        color.set(r, g, b, 1f);
        internalAddColor(color);
    }

    public void addColor(float r, float g, float b, float a) {
        color.set(r, g, b, a);
        internalAddColor(color);
    }

    public void addColor(Color color) {
        this.color.set(color);
        internalAddColor(this.color);
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
        conversionState.calculateNormal(pos1, pos2, pos3);
        normal.set(conversionState.getNormal());
        internalAddNormal(normal);
    }

    public void addNormalAgain() {
        internalAddNormal(normal);
    }

    public void addTexCoord(float s, float t) {
        texCoord.set(s, t, 0f);
        internalAddTexCoord(texCoord);
    }

    public void addSegment(String name) {
        if (isStarted) {
            throw new IllegalStateException("You cannot add a segment util stop() has been called");
        }
        Segment segment = createSegment(name);
        segments.add(segment);
    }

    public void addTexCoord(float s, float t, float r) {
        config.setTexCoordSize(3);
        texCoord.set(s, t, r);
        internalAddTexCoord(texCoord);
    }

    public void addVertex(float x, float y) {
        vertex.set(x, y, 1f, 0f);
        internalAddVertex(vertex);
    }

    public void addVertex(float x, float y, float z) {
        vertex.set(x, y, z, 1f);
        internalAddVertex(vertex);
    }

    public void addVertex(float x, float y, float z, float w) {
        vertex.set(x, y, z, w);
        internalAddVertex(vertex);
    }

    public void addVertex(Vector2f vertex) {
        this.vertex.set(vertex, 1f, 0f);
        internalAddVertex(this.vertex);
    }

    public void addVertex(Vector3f vertex) {
        this.vertex.set(vertex, 1f);
        internalAddVertex(this.vertex);
    }

    public void addVertex(Vector4f vertex) {
        this.vertex.set(vertex);
        internalAddVertex(this.vertex);
    }

    public void calculateNormals() {
        if (isStarted) {
            throw new IllegalStateException("You cannot calculate normals until stop() has been called");
        }

        conversionState.setPrimitiveMode(primitiveMode);
        normals.clear();

        for (int i = 0; i < vertices.size(); i += config.getVertexSize()) {
            vertex.zero();
            float x = vertices.get(i + 0);
            vertex.setX(x);
            float y = vertices.get(i + 1);
            vertex.setY(y);
            if (config.getVertexSize() >= 3) {
                float z = vertices.get(i + 2);
                vertex.setZ(z);
            }
            if (config.getVertexSize() == 4) {
                float w = vertices.get(i + 3);
                vertex.setW(w);
            }
            conversionState.addVertexConversion(vertex);
            conversionState.calculateNormals(this);
        }

        conversionState.finishCalculateNormals(this);
        conversionState.reset();
    }

    public Segment createSegment(String name) {
        if (isStarted) {
            throw new IllegalStateException("You cannot create a segment util stop() has been called");
        }

        Segment segment = GL.meshFactory.createSegment();

        segment.setElementCount(elementCount);
        segment.setMaxIndex(maxIndex);
        segment.setMinIndex(minIndex);
        segment.setName(name);
        segment.setPrimitiveMode(primitiveMode);

        if (colors.size() > 0) {
            segment.setData(SegmentDataType.COLOR, getColors());
            colors.clear();
        }
        if (normals.size() > 0) {
            segment.setData(SegmentDataType.NORMAL, getNormals());
            normals.clear();
        }
        if (texCoords.size() > 0) {
            segment.setData(SegmentDataType.TEX_COORD0, getTexCoords());
            texCoords.clear();
        }
        if (vertices.size() > 0) {
            segment.setData(SegmentDataType.VERTEX, getVertices());
            vertices.clear();
        }

        return segment;
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

    public int getElementCount() {
        return elementCount;
    }

    public int getNormalsCount() {
        return normals.size();
    }

    public int getPrimitiveMode() {
        return primitiveMode;
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

    public List<Segment> getSegments() {
        return segments;
    }

    public RenderableConfiguration getRenderableConfiguration() {
        return config;
    }

    public void getAABB(AABB aabb) {
        aabb.setMax(this.aabb.getMax());
        aabb.setMin(this.aabb.getMin());
    }

    public boolean isConvertToTriangles() {
        return convertToTriangles;
    }

    public void setConvertToTriangles(boolean convertToTriangles) {
        this.convertToTriangles = convertToTriangles;
    }

    public final void reset() {
        enableConversionForPrimitiveMode = false;
        isStarted = false;
        elementCount = 0;
        maxIndex = -1;
        minIndex = -1;
        primitiveMode = -1;
        vertexIndex = 0;
        aabb.setMax(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
        aabb.setMin(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        color.setWhite();
        conversionState.reset();
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
        config = GL.meshFactory.createRenderableConfiguration();
        normal.zero();
        texCoord.zero();
        localPosition.zero();
        vertex.zero();
    }

    public void setConfig(RenderableConfiguration config) {
        this.config = config;
    }

    public void setLocalPosition(float x, float y, float z) {
        this.localPosition.set(x, y, z, 0f);
    }

    public void setLocalPosition(Vector3f localPosition) {
        this.localPosition.set(localPosition, 0f);
    }

    public void start(int primitiveMode) {
        start(primitiveMode, false);
    }

    public void start(int primitiveMode, boolean forceConversionForPrimitiveMode) {
        if (convertToTriangles) {
            enableConversionForPrimitiveMode = ((primitiveMode != Primitives.LINES)
                    && (primitiveMode != Primitives.LINE_LOOP) && (primitiveMode != Primitives.LINE_STRIP)
                    && (primitiveMode != Primitives.POINTS) && (primitiveMode != Primitives.POLYGON)
                    && (primitiveMode != Primitives.TRIANGLES));
        }
        if (forceConversionForPrimitiveMode) {
            enableConversionForPrimitiveMode = true;
        }
        isStarted = true;
        elementCount = 0;
        maxIndex = -1;
        minIndex = vertexIndex; // Save first index
        this.primitiveMode = primitiveMode;
        primitiveModes.add(this.primitiveMode);
        conversionState.setPrimitiveMode(primitiveMode);
    }

    public void stop() {
        if (enableConversionForPrimitiveMode) {
            conversionState.reset();
            primitiveMode = Primitives.TRIANGLES;
            primitiveModes.set(primitiveModes.size() - 1, primitiveMode);
            enableConversionForPrimitiveMode = false;
        }
        isStarted = false;
        elementCounts.add(elementCount);
        maxIndexes.add(maxIndex);
        minIndexes.add(minIndex);
    }

    public static ConversionState CreateConversionState(int primitiveMode) {
        ConversionState conversionState = new ConversionState();
        conversionState.setPrimitiveMode(primitiveMode);
        return conversionState;
    }

    public static class ConversionState {

        private boolean firstConvert;
        private boolean flipFlop;
        private boolean hasColorsToConvert;
        private boolean hasNormalsToConvert;
        private boolean hasTexCoordsToConvert;
        private int colorConversionCount;
        private int normalConversionCount;
        private int primitiveMode; // Doesn't get reset
        private int texCoordConversionCount;
        private int vertexConversionCount;
        private final Color color;
        private final Color colorConversion0;
        private final Color colorConversion1;
        private final Color colorConversion2;
        private final Color colorConversion3;
        private final Vector3f normal;
        private final Vector3f normalConversion0;
        private final Vector3f normalConversion1;
        private final Vector3f normalConversion2;
        private final Vector3f normalConversion3;
        private final Vector3f normalCreation1;
        private final Vector3f normalCreation2;
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

        public ConversionState() {
            primitiveMode = -1;
            color = new Color();
            colorConversion0 = new Color();
            colorConversion1 = new Color();
            colorConversion2 = new Color();
            colorConversion3 = new Color();
            normal = new Vector3f();
            normalConversion0 = new Vector3f();
            normalConversion1 = new Vector3f();
            normalConversion2 = new Vector3f();
            normalConversion3 = new Vector3f();
            normalCreation1 = new Vector3f();
            normalCreation2 = new Vector3f();
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
            normalCreation2.set(pos1).subtract(pos2);// pos2 is base pointing to pos1, middle finger
            normal.set(normalCreation1).cross(normalCreation2); // right hand rule, thumb
            normal.normalize();
        }

        private void calculateNormal(Vector4f pos1, Vector4f pos2, Vector4f pos3) {
            // CCW order
            normalCreation1.set(
                    pos3.getX() - pos2.getX(),
                    pos3.getY() - pos2.getY(),
                    pos3.getZ() - pos2.getZ()); // pos2 is base pointing to pos3, index finger
            normalCreation2.set(
                    pos1.getX() - pos2.getX(),
                    pos1.getY() - pos2.getY(),
                    pos1.getZ() - pos2.getZ()); // pos2 is base pointing to pos1, middle finger
            normal.set(normalCreation1).cross(normalCreation2); // right hand rule, thumb
            normal.normalize();
        }

        private void convertQuad(Tessellator tessellator) {
            if (hasColorsToConvert) {
                tessellator.internalAddColorToList(colorConversion0); // Top right
                tessellator.internalAddColorToList(colorConversion1); // Top left
                tessellator.internalAddColorToList(colorConversion2); // Bottom left
                tessellator.internalAddColorToList(colorConversion3); // Bottom right
            }
            if (hasNormalsToConvert) {
                tessellator.internalAddNormalToList(normalConversion0);
                tessellator.internalAddNormalToList(normalConversion1);
                tessellator.internalAddNormalToList(normalConversion2);
                tessellator.internalAddNormalToList(normalConversion3);
            }
            if (hasTexCoordsToConvert) {
                tessellator.internalAddTexCoordToList(texCoordConversion0);
                tessellator.internalAddTexCoordToList(texCoordConversion1);
                tessellator.internalAddTexCoordToList(texCoordConversion2);
                tessellator.internalAddTexCoordToList(texCoordConversion3);
            }
            tessellator.internalAddVertexToList(vertexConversion0);
            tessellator.internalAddVertexToList(vertexConversion1);
            tessellator.internalAddVertexToList(vertexConversion2);
            tessellator.internalAddVertexToList(vertexConversion3);
        }

        private void convertQuadStripToTriangles(Tessellator tessellator) {
            if (hasColorsToConvert) {
                tessellator.internalAddColorToList(colorConversion2); // Top right
                tessellator.internalAddColorToList(colorConversion0); // Top left
                tessellator.internalAddColorToList(colorConversion1); // Bottom left
                tessellator.internalAddColorToList(colorConversion1); // Bottom left
                tessellator.internalAddColorToList(colorConversion3); // Bottom right
                tessellator.internalAddColorToList(colorConversion2); // Top Right
            }
            if (hasNormalsToConvert) {
                tessellator.internalAddNormalToList(normalConversion2);
                tessellator.internalAddNormalToList(normalConversion0);
                tessellator.internalAddNormalToList(normalConversion1);
                tessellator.internalAddNormalToList(normalConversion1);
                tessellator.internalAddNormalToList(normalConversion3);
                tessellator.internalAddNormalToList(normalConversion2);
            }
            if (hasTexCoordsToConvert) {
                tessellator.internalAddTexCoordToList(texCoordConversion2);
                tessellator.internalAddTexCoordToList(texCoordConversion0);
                tessellator.internalAddTexCoordToList(texCoordConversion1);
                tessellator.internalAddTexCoordToList(texCoordConversion1);
                tessellator.internalAddTexCoordToList(texCoordConversion3);
                tessellator.internalAddTexCoordToList(texCoordConversion2);
            }
            tessellator.internalAddVertexToList(vertexConversion2);
            tessellator.internalAddVertexToList(vertexConversion0);
            tessellator.internalAddVertexToList(vertexConversion1);
            tessellator.internalAddVertexToList(vertexConversion1);
            tessellator.internalAddVertexToList(vertexConversion3);
            tessellator.internalAddVertexToList(vertexConversion2);
        }

        private void convertQuadToTriangles(Tessellator tessellator) {
            if (hasColorsToConvert) {
                tessellator.internalAddColorToList(colorConversion0); // Top right
                tessellator.internalAddColorToList(colorConversion1); // Top left
                tessellator.internalAddColorToList(colorConversion2); // Bottom left
                tessellator.internalAddColorToList(colorConversion2); // Bottom left
                tessellator.internalAddColorToList(colorConversion3); // Bottom right
                tessellator.internalAddColorToList(colorConversion0); // Top right
            }
            if (hasNormalsToConvert) {
                tessellator.internalAddNormalToList(normalConversion0);
                tessellator.internalAddNormalToList(normalConversion1);
                tessellator.internalAddNormalToList(normalConversion2);
                tessellator.internalAddNormalToList(normalConversion2);
                tessellator.internalAddNormalToList(normalConversion3);
                tessellator.internalAddNormalToList(normalConversion0);
            }
            if (hasTexCoordsToConvert) {
                tessellator.internalAddTexCoordToList(texCoordConversion0);
                tessellator.internalAddTexCoordToList(texCoordConversion1);
                tessellator.internalAddTexCoordToList(texCoordConversion2);
                tessellator.internalAddTexCoordToList(texCoordConversion2);
                tessellator.internalAddTexCoordToList(texCoordConversion3);
                tessellator.internalAddTexCoordToList(texCoordConversion0);
            }
            tessellator.internalAddVertexToList(vertexConversion0);
            tessellator.internalAddVertexToList(vertexConversion1);
            tessellator.internalAddVertexToList(vertexConversion2);
            tessellator.internalAddVertexToList(vertexConversion2);
            tessellator.internalAddVertexToList(vertexConversion3);
            tessellator.internalAddVertexToList(vertexConversion0);
        }

        private void convertTriangle(Tessellator tessellator) {
            if (hasColorsToConvert) {
                tessellator.internalAddColorToList(colorConversion0); // Center
                tessellator.internalAddColorToList(colorConversion1); // Bottom left
                tessellator.internalAddColorToList(colorConversion2); // Bottom right
            }
            if (hasNormalsToConvert) {
                tessellator.internalAddNormalToList(normalConversion0);
                tessellator.internalAddNormalToList(normalConversion1);
                tessellator.internalAddNormalToList(normalConversion2);
            }
            if (hasTexCoordsToConvert) {
                tessellator.internalAddTexCoordToList(texCoordConversion0);
                tessellator.internalAddTexCoordToList(texCoordConversion1);
                tessellator.internalAddTexCoordToList(texCoordConversion2);
            }
            tessellator.internalAddVertexToList(vertexConversion0);
            tessellator.internalAddVertexToList(vertexConversion1);
            tessellator.internalAddVertexToList(vertexConversion2);
        }

        private boolean hasIncompatiblePrimitiveMode(Tessellator tessellator) {
            return ((tessellator.primitiveMode == Primitives.LINES)
                    || (tessellator.primitiveMode == Primitives.LINE_LOOP)
                    || (tessellator.primitiveMode == Primitives.LINE_STRIP)
                    || (tessellator.primitiveMode == Primitives.POINTS)
                    || (tessellator.primitiveMode == Primitives.POLYGON));
        }

        public void addColorConversion(Color color) {
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

        public void addNormalConversion(Vector3f normal) {
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

        public void addTexCoordConversion(Vector3f texCoord) {
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

        public void addQuad(Tessellator tessellator) {
            if (hasIncompatiblePrimitiveMode(tessellator)) {
                throw new IllegalStateException("You cannot add a quad in the primitive mode: "
                        + tessellator.primitiveMode);
            }
            if ((!tessellator.enableConversionForPrimitiveMode) && (tessellator.primitiveMode != Primitives.QUADS)) {
                throw new IllegalStateException("You cannot add a quad in the primitive mode: "
                        + tessellator.primitiveMode);
            }
            if (vertexConversionCount != 4) {
                throw new IllegalStateException("You must have four vertices to add a quad");
            }
            if (tessellator.enableConversionForPrimitiveMode) {
                convertQuadToTriangles(tessellator);
            } else {
                convertQuad(tessellator);
            }
        }

        public void addTriangle(Tessellator tessellator) {
            if (hasIncompatiblePrimitiveMode(tessellator)) {
                throw new IllegalStateException("You cannot add a triangle in the primitive mode: "
                        + tessellator.primitiveMode);
            }
            if ((!tessellator.enableConversionForPrimitiveMode) && (tessellator.primitiveMode != Primitives.TRIANGLES)) {
                throw new IllegalStateException("You cannot add a triangle in the primitive mode: "
                        + tessellator.primitiveMode);
            }
            if (vertexConversionCount != 3) {
                throw new IllegalStateException("You must have three vertices to add a quad");
            }
            convertTriangle(tessellator);
        }

        public void addVertexConversion(Vector4f vertex) {
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

        public void calculateNormals(Tessellator tessellator) {
            if (hasIncompatiblePrimitiveMode(tessellator)) {
                throw new IllegalStateException("You cannot calculate normals in the primitive mode: "
                        + tessellator.primitiveMode);
            }
            if (tessellator.primitiveMode != primitiveMode) {
                throw new IllegalStateException("The Tessellator and ConversionState's primitiveMode must match");
            }
            if ((tessellator.config.getVertexSize() != 3) && (tessellator.config.getVertexSize()) != 4) {
                throw new IllegalStateException("Currently do not support calculating normals for 2D vectors");
            }
            switch (primitiveMode) {
                case Primitives.QUAD_STRIP:
                    if (vertexConversionCount == 4) {
                        // Top right, top left, bottom left
                        calculateNormal(vertexConversion2, vertexConversion0, vertexConversion1);
                        tessellator.internalAddNormal(normal);
                        tessellator.internalAddNormal(normal);
                        // Top right to top left, bottom right to bottom left
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
                    break;

                case Primitives.QUADS:
                    if (vertexConversionCount == 4) {
                        // Top right, top left, bottom left
                        calculateNormal(vertexConversion0, vertexConversion1, vertexConversion2);
                        tessellator.internalAddNormal(normal);
                        tessellator.internalAddNormal(normal);
                        tessellator.internalAddNormal(normal);
                        tessellator.internalAddNormal(normal);
                        colorConversionCount = 0;
                        normalConversionCount = 0;
                        texCoordConversionCount = 0;
                        vertexConversionCount = 0;
                    }
                    break;

                case Primitives.TRIANGLE_FAN:
                    if (vertexConversionCount == 3) {
                        if (firstConvert) {
                            normal.set(vertexConversion0.getX(), vertexConversion0.getY(), vertexConversion0.getZ());
                            normal.normalize();
                            tessellator.internalAddNormal(normal);
                            firstConvert = false;
                        }
                        // Center, bottom left, bottom right
                        calculateNormal(vertexConversion0, vertexConversion1, vertexConversion2);
                        tessellator.internalAddNormal(normal);
                        // Bottom right to bottom left
                        colorConversion1.set(colorConversion2);
                        normalConversion1.set(normalConversion2);
                        texCoordConversion1.set(texCoordConversion2);
                        vertexConversion1.set(vertexConversion2);
                        colorConversionCount = 2;
                        normalConversionCount = 2;
                        texCoordConversionCount = 2;
                        vertexConversionCount = 2;
                    }
                    break;

                case Primitives.TRIANGLE_STRIP:
                    if (vertexConversionCount == 3) {
                        // Top right, top left, bottom left
                        calculateNormal(vertexConversion2, vertexConversion0, vertexConversion1);
                        tessellator.internalAddNormal(normal);
                        if (flipFlop) {
                            flipFlop = !flipFlop;
                            // Top right to top left
                            colorConversion0.set(colorConversion2);
                            normalConversion0.set(normalConversion2);
                            texCoordConversion0.set(texCoordConversion2);
                            vertexConversion0.set(vertexConversion2);
                        } else {
                            flipFlop = !flipFlop;
                            // Bottom right to bottom left
                            colorConversion1.set(colorConversion2);
                            normalConversion1.set(normalConversion2);
                            texCoordConversion1.set(texCoordConversion2);
                            vertexConversion1.set(vertexConversion2);
                        }
                        colorConversionCount = 2;
                        normalConversionCount = 2;
                        texCoordConversionCount = 2;
                        vertexConversionCount = 2;
                    }
                    break;

                case Primitives.TRIANGLES:
                    if (vertexConversionCount == 3) {
                        // Center, bottom left, bottom right
                        calculateNormal(vertexConversion0, vertexConversion1, vertexConversion2);
                        tessellator.internalAddNormal(normal);
                        tessellator.internalAddNormal(normal);
                        tessellator.internalAddNormal(normal);
                        colorConversionCount = 0;
                        normalConversionCount = 0;
                        texCoordConversionCount = 0;
                        vertexConversionCount = 0;
                    }
                    break;

                default:
                    throw new IllegalStateException("Unknown primitive mode: "
                            + primitiveMode + " for normal calculation");
            }
        }

        public void convert(Tessellator tessellator) {
            if (hasIncompatiblePrimitiveMode(tessellator)) {
                throw new IllegalStateException("You cannot convert in the primitive mode: "
                        + tessellator.primitiveMode);
            }
            if ((!tessellator.enableConversionForPrimitiveMode) && (tessellator.primitiveMode == Primitives.TRIANGLES)) {
                throw new IllegalStateException("You cannot convert if the Tessellator does not have it enabled");
            }
            switch (primitiveMode) {
                case Primitives.QUAD_STRIP:
                    if (vertexConversionCount == 4) {
                        convertQuadStripToTriangles(tessellator);
                        // Top right to top left, bottom right to bottom left
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
                    break;

                case Primitives.QUADS:
                    if (vertexConversionCount == 4) {
                        convertQuadToTriangles(tessellator);
                        colorConversionCount = 0;
                        normalConversionCount = 0;
                        texCoordConversionCount = 0;
                        vertexConversionCount = 0;
                    }
                    break;

                case Primitives.TRIANGLE_FAN:
                    if (vertexConversionCount == 3) {
                        convertTriangle(tessellator);
                        // Bottom right to bottom left
                        colorConversion1.set(colorConversion2);
                        normalConversion1.set(normalConversion2);
                        texCoordConversion1.set(texCoordConversion2);
                        vertexConversion1.set(vertexConversion2);
                        colorConversionCount = 2;
                        normalConversionCount = 2;
                        texCoordConversionCount = 2;
                        vertexConversionCount = 2;
                    }
                    break;

                case Primitives.TRIANGLE_STRIP:
                    if (vertexConversionCount == 3) {
                        convertTriangle(tessellator);
                        if (flipFlop) {
                            flipFlop = !flipFlop;
                            // Top right to top left
                            colorConversion0.set(colorConversion2);
                            normalConversion0.set(normalConversion2);
                            texCoordConversion0.set(texCoordConversion2);
                            vertexConversion0.set(vertexConversion2);
                        } else {
                            flipFlop = !flipFlop;
                            // Bottom right to bottom left
                            colorConversion1.set(colorConversion2);
                            normalConversion1.set(normalConversion2);
                            texCoordConversion1.set(texCoordConversion2);
                            vertexConversion1.set(vertexConversion2);
                        }
                        colorConversionCount = 2;
                        normalConversionCount = 2;
                        texCoordConversionCount = 2;
                        vertexConversionCount = 2;
                    }
                    break;

                default:
                    throw new IllegalStateException("Unknown primitive mode: " + primitiveMode + " for conversion");
            }
        }

        public void finishCalculateNormals(Tessellator tessellator) {
            if (hasIncompatiblePrimitiveMode(tessellator)) {
                throw new IllegalStateException("You cannot finish calculate normals in the primitive mode: "
                        + tessellator.primitiveMode);
            }
            if (tessellator.primitiveMode != primitiveMode) {
                throw new IllegalStateException("The Tessellator and ConversionState's primitiveMode must match");
            }
            if ((tessellator.config.getVertexSize() != 3) && (tessellator.config.getVertexSize()) != 4) {
                throw new IllegalStateException("Currently do not support finish calculating normals for 2D vectors");
            }
            switch (primitiveMode) {
                case Primitives.QUAD_STRIP:
                case Primitives.TRIANGLE_STRIP:
                    calculateNormal(vertexConversion2, vertexConversion0, vertexConversion1);
                    tessellator.internalAddNormal(normal);
                    tessellator.internalAddNormal(normal);
                    break;

                case Primitives.QUADS:
                case Primitives.TRIANGLES:
                    // Do nothing
                    break;

                case Primitives.TRIANGLE_FAN:
                    calculateNormal(vertexConversion0, vertexConversion1, vertexConversion2);
                    tessellator.internalAddNormal(normal);
                    break;

                default:
                    throw new IllegalStateException("Unknown primitive mode: "
                            + primitiveMode + " for finish normal calculation");
            }
        }

        public int getPrimitiveMode() {
            return primitiveMode;
        }

        public void setPrimitiveMode(int primitiveMode) {
            this.primitiveMode = primitiveMode;
        }

        public Color getColor() {
            return color;
        }

        public Vector3f getNormal() {
            return normal;
        }

        public Vector3f getTexCoord() {
            return texCoord;
        }

        public Vector4f getVertex() {
            return vertex;
        }

        public final void reset() {
            firstConvert = true;
            flipFlop = true;
            hasColorsToConvert = false;
            hasNormalsToConvert = false;
            hasTexCoordsToConvert = false;
            colorConversionCount = 0;
            normalConversionCount = 0;
            texCoordConversionCount = 0;
            vertexConversionCount = 0;
            color.setWhite();
            colorConversion0.setWhite();
            colorConversion1.setWhite();
            colorConversion2.setWhite();
            colorConversion3.setWhite();
            normal.zero();
            normalConversion0.zero();
            normalConversion1.zero();
            normalConversion2.zero();
            normalConversion3.zero();
            texCoord.zero();
            texCoordConversion0.zero();
            texCoordConversion1.zero();
            texCoordConversion2.zero();
            texCoordConversion3.zero();
            vertex.zero();
            vertexConversion0.zero();
            vertexConversion1.zero();
            vertexConversion2.zero();
            vertexConversion3.zero();
        }

    }

}
