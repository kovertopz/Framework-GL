package net.smert.jreactphysics3d.framework.opengl.renderable.vbo;

import net.smert.jreactphysics3d.framework.opengl.constants.GLTypes;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Configuration {

    private int colorSize;
    private int colorType;
    private final int normalType;
    private int texCoordSize;
    private final int texCoordType;
    private int vertexSize;
    private final int vertexType;

    public Configuration() {
        colorSize = 4;
        colorType = GLTypes.FLOAT;
        normalType = GLTypes.FLOAT;
        texCoordSize = 2;
        texCoordType = GLTypes.FLOAT;
        vertexSize = 3;
        vertexType = GLTypes.FLOAT;
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

    public int getNormalType() {
        return normalType;
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
        return texCoordType;
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
        return vertexType;
    }

}
