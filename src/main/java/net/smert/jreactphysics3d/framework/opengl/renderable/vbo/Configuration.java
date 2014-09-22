package net.smert.jreactphysics3d.framework.opengl.renderable.vbo;

import net.smert.jreactphysics3d.framework.opengl.constants.GLTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Configuration {

    private final static Logger log = LoggerFactory.getLogger(Configuration.class);

    private boolean isImmutable;
    private int colorSize;
    private int colorType;
    private int indexType;
    private final int normalSize;
    private final int normalType;
    private int texCoordSize;
    private final int texCoordType;
    private int vertexSize;
    private final int vertexType;

    public Configuration() {
        isImmutable = false;
        colorSize = 4;
        colorType = GLTypes.FLOAT;
        indexType = GLTypes.UNSIGNED_INT;
        normalSize = 3;
        normalType = GLTypes.FLOAT;
        texCoordSize = 2;
        texCoordType = GLTypes.FLOAT;
        vertexSize = 3;
        vertexType = GLTypes.FLOAT;
    }

    private void checkImmutable() {
        if (isImmutable) {
            throw new RuntimeException("The configuration has already been finalized and cannot be changed");
        }
    }

    public int getColorSize() {
        return colorSize;
    }

    public void setColorSize(int colorSize) {
        checkImmutable();
        if ((colorSize != 3) && (colorSize != 4)) {
            throw new IllegalArgumentException("The color size must be 3 or 4. Was given: " + colorSize);
        }
        this.colorSize = colorSize;
    }

    public int getColorType() {
        return colorType;
    }

    public void setColorTypeByte() {
        checkImmutable();
        this.colorType = GLTypes.BYTE;
    }

    public void setColorTypeFloat() {
        checkImmutable();
        this.colorType = GLTypes.FLOAT;
    }

    public void setColorTypeUnsignedByte() {
        checkImmutable();
        this.colorType = GLTypes.UNSIGNED_BYTE;
    }

    public int getIndexType() {
        return indexType;
    }

    public void setIndexTypeUnsignedInt() {
        checkImmutable();
        this.indexType = GLTypes.UNSIGNED_INT;
    }

    public void setIndexTypeUnsignedShort() {
        checkImmutable();
        this.indexType = GLTypes.UNSIGNED_SHORT;
    }

    public int getNormalSize() {
        return normalSize;
    }

    public int getNormalType() {
        return normalType;
    }

    public int getTexCoordSize() {
        return texCoordSize;
    }

    public void setTexCoordSize(int texCoordSize) {
        checkImmutable();
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
        checkImmutable();
        if ((vertexSize < 2) || (vertexSize > 4)) {
            throw new IllegalArgumentException("The vertex size must be 2, 3 or 4. Was given: " + vertexSize);
        }
        this.vertexSize = vertexSize;
    }

    public int getVertexType() {
        return vertexType;
    }

    public boolean isImmutable() {
        return isImmutable;
    }

    public void makeImmutable() {
        if ((log.isInfoEnabled()) && (isImmutable == false)) {
            log.info("Making VBO configuration immutable:"
                    + " Color Size: " + colorSize
                    + " Color Type: " + GLTypes.ConvertToString(colorType)
                    + " Normal Type: " + GLTypes.ConvertToString(normalType)
                    + " Texture Coordinate Size: " + texCoordSize
                    + " Texture Coordinate Type: " + GLTypes.ConvertToString(texCoordType)
                    + " Vertex Size: " + vertexSize
                    + " Vertex Type: " + GLTypes.ConvertToString(vertexType));
        }

        isImmutable = true;
    }

}
