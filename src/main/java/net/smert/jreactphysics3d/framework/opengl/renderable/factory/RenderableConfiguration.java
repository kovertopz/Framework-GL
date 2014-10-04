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
package net.smert.jreactphysics3d.framework.opengl.renderable.factory;

import java.nio.ByteBuffer;
import net.smert.jreactphysics3d.framework.math.Vector3f;
import net.smert.jreactphysics3d.framework.math.Vector4f;
import net.smert.jreactphysics3d.framework.opengl.constants.GLTypes;
import net.smert.jreactphysics3d.framework.utils.Color;

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
        colorSize = 4;
        colorType = GLTypes.FLOAT;
        indexType = GLTypes.UNSIGNED_INT;
        texCoordSize = 2;
        vertexSize = 3;
    }

    public RenderableConfiguration(RenderableConfiguration config) {
        colorSize = config.colorSize;
        colorType = config.colorType;
        indexType = config.indexType;
        texCoordSize = config.texCoordSize;
        vertexSize = config.vertexSize;
    }

    public void convertColorToByteBuffer(Color color, ByteBuffer byteBuffer) {

        assert ((colorSize == 3) || (colorSize == 4));

        float r = color.getR();
        float g = color.getG();
        float b = color.getB();
        float a = color.getA();

        assert (r >= 0.0f && r <= 1.0f);
        assert (g >= 0.0f && g <= 1.0f);
        assert (b >= 0.0f && b <= 1.0f);
        assert (a >= 0.0f && a <= 1.0f);

        // Depending on the GL type and size convert the data and put it into the byte buffer
        switch (colorType) {
            case GLTypes.BYTE:
            case GLTypes.UNSIGNED_BYTE:
                byteBuffer.put((byte) ((r / 1.0f) * 255));
                byteBuffer.put((byte) ((g / 1.0f) * 255));
                byteBuffer.put((byte) ((b / 1.0f) * 255));
                if (colorSize == 4) {
                    byteBuffer.put((byte) ((a / 1.0f) * 255));
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

    public void convertNormalToByteBuffer(Vector3f vector, ByteBuffer byteBuffer) {

        assert (NORMAL_SIZE == 3);

        float x = vector.getX();
        float y = vector.getY();
        float z = vector.getZ();

        assert (x >= -1.0f && x <= 1.0f);
        assert (y >= -1.0f && y <= 1.0f);
        assert (z >= -1.0f && z <= 1.0f);

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

    public void convertTexCoordToByteBuffer(Vector3f vector, ByteBuffer byteBuffer) {

        assert ((texCoordSize == 2) || (texCoordSize == 3));

        float x = vector.getX();
        float y = vector.getY();
        float z = vector.getZ();

        assert (x >= 0.0f && x <= 1.0f);
        assert (y >= 0.0f && y <= 1.0f);
        assert (z >= 0.0f && z <= 1.0f);

        // Depending on the GL type and size put it into the byte buffer
        switch (TEX_COORD_TYPE) {
            case GLTypes.FLOAT:
                byteBuffer.putFloat(x);
                byteBuffer.putFloat(y);
                if (texCoordSize == 3) {
                    byteBuffer.putFloat(z);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown GL type constant for texture coordinate: " + TEX_COORD_TYPE);
        }
    }

    public void convertVertexToByteBuffer(Vector4f vector, ByteBuffer byteBuffer) {

        assert ((vertexSize == 3) || (vertexSize == 4));

        float x = vector.getX();
        float y = vector.getY();
        float z = vector.getZ();
        float w = vector.getW();

        // Depending on the GL type and size put it into the byte buffer
        switch (VERTEX_TYPE) {
            case GLTypes.FLOAT:
                byteBuffer.putFloat(x);
                byteBuffer.putFloat(y);
                byteBuffer.putFloat(z);
                if (vertexSize == 4) {
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

    public RenderableConfiguration clone() {
        return new RenderableConfiguration(this);
    }

}
