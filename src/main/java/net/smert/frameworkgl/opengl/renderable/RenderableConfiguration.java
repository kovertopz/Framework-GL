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
package net.smert.frameworkgl.opengl.renderable;

import java.nio.ByteBuffer;
import net.smert.frameworkgl.math.MathHelper;
import net.smert.frameworkgl.opengl.constants.GLTypes;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class RenderableConfiguration {

    private final static int NORMAL_SIZE = 3;
    private final static int NORMAL_TYPE = GLTypes.FLOAT;
    private final static int TEX_COORD_TYPE = GLTypes.FLOAT;
    private final static int VERTEX_TYPE = GLTypes.FLOAT;

    private int colorSize;
    private int colorType;
    private int indexType;
    private int texCoordSize;
    private int vertexSize;

    public RenderableConfiguration() {
        reset();
    }

    public RenderableConfiguration(RenderableConfiguration config) {
        colorSize = config.colorSize;
        colorType = config.colorType;
        indexType = config.indexType;
        texCoordSize = config.texCoordSize;
        vertexSize = config.vertexSize;
    }

    public void convertColorToByteBuffer(float[] colors, int index, ByteBuffer byteBuffer) {

        assert ((colorSize == 3) || (colorSize == 4));

        int offset = colorSize * index;
        float r = colors[offset + 0];
        float g = colors[offset + 1];
        float b = colors[offset + 2];
        float a = 1f;

        assert (r >= 0f && r <= 1f);
        assert (g >= 0f && g <= 1f);
        assert (b >= 0f && b <= 1f);

        if (colorSize == 4) {
            a = colors[offset + 3];
            assert (a >= 0f && a <= 1f);
        }

        // Depending on the GL type and size convert the data and put it into the byte buffer
        switch (colorType) {
            case GLTypes.BYTE:
            case GLTypes.UNSIGNED_BYTE:
                byteBuffer.put(convertFloatToByte(r));
                byteBuffer.put(convertFloatToByte(g));
                byteBuffer.put(convertFloatToByte(b));
                if (colorSize == 4) {
                    byteBuffer.put(convertFloatToByte(a));
                }
                break;

            case GLTypes.FLOAT:
                byteBuffer.putFloat(r);
                byteBuffer.putFloat(g);
                byteBuffer.putFloat(b);
                if (colorSize == 4) {
                    byteBuffer.putFloat(a);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown GL type constant for color: " + colorType);
        }
    }

    private byte convertFloatToByte(float value) {
        return (byte) (MathHelper.Clamp(value, 0f, 1f) * 255);
    }

    public int convertGLTypeToByteSize(int glType) {

        int byteSize = 0;

        // Convert the type to the byte size of the type
        switch (glType) {
            case GLTypes.BYTE:
            case GLTypes.UNSIGNED_BYTE:
                byteSize = 1;
                break;

            case GLTypes.SHORT:
            case GLTypes.UNSIGNED_SHORT:
                byteSize = 2;
                break;

            case GLTypes.FLOAT:
            case GLTypes.INT:
            case GLTypes.UNSIGNED_INT:
                byteSize = 4;
                break;

            default:
                throw new IllegalArgumentException("Unknown GL type constant: " + glType);
        }

        return byteSize;
    }

    public void convertNormalToByteBuffer(float[] normals, int index, ByteBuffer byteBuffer) {

        assert (NORMAL_SIZE == 3);

        int offset = NORMAL_SIZE * index;
        float x = normals[offset + 0];
        float y = normals[offset + 1];
        float z = normals[offset + 2];

        assert (x >= -1f && x <= 1f);
        assert (y >= -1f && y <= 1f);
        assert (z >= -1f && z <= 1f);

        // Depending on the GL type put it into the byte buffer
        switch (NORMAL_TYPE) {
            case GLTypes.FLOAT:
                byteBuffer.putFloat(x);
                byteBuffer.putFloat(y);
                byteBuffer.putFloat(z);
                break;

            default:
                throw new IllegalArgumentException("Unknown GL type constant for normal: " + NORMAL_TYPE);
        }
    }

    public void convertTexCoordToByteBuffer(float[] texCoords, int index, ByteBuffer byteBuffer) {

        assert ((texCoordSize == 2) || (texCoordSize == 3));

        int offset = texCoordSize * index;
        float s = texCoords[offset + 0];
        float t = texCoords[offset + 1];
        float r = 0f;

        assert (s >= -1f && s <= 1f);
        assert (t >= -1f && t <= 1f);

        if (texCoordSize == 3) {
            r = texCoords[offset + 2];
            assert (r >= -1f && r <= 1f);
        }

        // Depending on the GL type and size put it into the byte buffer
        switch (TEX_COORD_TYPE) {
            case GLTypes.FLOAT:
                byteBuffer.putFloat(s);
                byteBuffer.putFloat(t);
                if (texCoordSize == 3) {
                    byteBuffer.putFloat(r);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown GL type constant for texture coordinate: " + TEX_COORD_TYPE);
        }
    }

    public void convertVertexToByteBuffer(float[] vertices, int index, ByteBuffer byteBuffer) {

        assert ((vertexSize >= 2) && (vertexSize <= 4));

        int offset = vertexSize * index;
        float x = vertices[offset + 0];
        float y = vertices[offset + 1];

        // Depending on the GL type and size put it into the byte buffer
        switch (VERTEX_TYPE) {
            case GLTypes.FLOAT:
                byteBuffer.putFloat(x);
                byteBuffer.putFloat(y);
                if (vertexSize >= 3) {
                    float z = vertices[offset + 2];
                    byteBuffer.putFloat(z);
                }
                if (vertexSize == 4) {
                    float w = vertices[offset + 3];
                    byteBuffer.putFloat(w);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown GL type constant for vertex: " + VERTEX_TYPE);
        }
    }

    public int getColorSize() {
        return colorSize;
    }

    public void setColorSize(int colorSize) {
        if ((colorSize != 3) && (colorSize != 4)) {
            throw new IllegalArgumentException("The color size must be 3 or 4. Was given: " + colorSize);
        }
        this.colorSize = colorSize;
    }

    public int getColorType() {
        return colorType;
    }

    public void setColorTypeByte() {
        this.colorType = GLTypes.BYTE;
    }

    public void setColorTypeFloat() {
        this.colorType = GLTypes.FLOAT;
    }

    public void setColorTypeUnsignedByte() {
        this.colorType = GLTypes.UNSIGNED_BYTE;
    }

    public int getIndexType() {
        return indexType;
    }

    public void setIndexTypeUnsignedInt() {
        this.indexType = GLTypes.UNSIGNED_INT;
    }

    public void setIndexTypeUnsignedShort() {
        this.indexType = GLTypes.UNSIGNED_SHORT;
    }

    public int getNormalSize() {
        return NORMAL_SIZE;
    }

    public int getNormalType() {
        return NORMAL_TYPE;
    }

    public int getTexCoordSize() {
        return texCoordSize;
    }

    public void setTexCoordSize(int texCoordSize) {
        if ((texCoordSize != 2) && (texCoordSize != 3)) {
            throw new IllegalArgumentException("The texture coordinate size must be 2 or 3. Was given: " + texCoordSize);
        }
        this.texCoordSize = texCoordSize;
    }

    public int getTexCoordType() {
        return TEX_COORD_TYPE;
    }

    public int getVertexSize() {
        return vertexSize;
    }

    public void setVertexSize(int vertexSize) {
        if ((vertexSize < 2) || (vertexSize > 4)) {
            throw new IllegalArgumentException("The vertex size must be 2, 3 or 4. Was given: " + vertexSize);
        }
        this.vertexSize = vertexSize;
    }

    public int getVertexType() {
        return VERTEX_TYPE;
    }

    public final void reset() {
        colorSize = 4;
        colorType = GLTypes.FLOAT;
        indexType = GLTypes.UNSIGNED_INT;
        texCoordSize = 2;
        vertexSize = 3;
    }

    @Override
    public RenderableConfiguration clone() {
        return new RenderableConfiguration(this);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + this.colorSize;
        hash = 29 * hash + this.colorType;
        hash = 29 * hash + this.indexType;
        hash = 29 * hash + this.texCoordSize;
        hash = 29 * hash + this.vertexSize;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RenderableConfiguration other = (RenderableConfiguration) obj;
        if (this.colorSize != other.colorSize) {
            return false;
        }
        if (this.colorType != other.colorType) {
            return false;
        }
        if (this.indexType != other.indexType) {
            return false;
        }
        if (this.texCoordSize != other.texCoordSize) {
            return false;
        }
        return this.vertexSize == other.vertexSize;
    }

}
