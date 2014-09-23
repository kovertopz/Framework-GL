package net.smert.jreactphysics3d.framework.opengl.renderable.va;

import java.nio.ByteBuffer;
import net.smert.jreactphysics3d.framework.opengl.GL;
import net.smert.jreactphysics3d.framework.opengl.renderable.factory.Configuration;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VABindState {

    private boolean colorEnabled;
    private boolean normalEnabled;
    private boolean texCoordEnabled;
    private boolean vertexEnabled;
    private final Configuration renderableConfig;

    public VABindState(Configuration renderableConfig) {
        this.renderableConfig = renderableConfig;

        if (renderableConfig.isImmutable() == false) {
            throw new RuntimeException("Renderable configuration must be made immutable");
        }

        reset();
    }

    private void setColorEnabled(boolean enabled) {
        if (colorEnabled != enabled) {
            colorEnabled = enabled;

            if (enabled == true) {
                GL.vaHelper.enableColors();
            } else {
                GL.vaHelper.disableColors();
            }
        }
    }

    private void setNormalEnabled(boolean enabled) {
        if (normalEnabled != enabled) {
            normalEnabled = enabled;

            if (enabled == true) {
                GL.vaHelper.enableNormals();
            } else {
                GL.vaHelper.disableNormals();
            }
        }
    }

    private void setTextureCoordinateEnabled(boolean enabled) {
        if (texCoordEnabled != enabled) {
            texCoordEnabled = enabled;

            if (enabled == true) {
                GL.vaHelper.enableTextureCoordinates();
            } else {
                GL.vaHelper.disableTextureCoordinates();
            }
        }
    }

    private void setVertexEnabled(boolean enabled) {
        if (vertexEnabled != enabled) {
            vertexEnabled = enabled;

            if (enabled == true) {
                GL.vaHelper.enableVertices();
            } else {
                GL.vaHelper.disableVertices();
            }
        }
    }

    public void bindColor(ByteBuffer colorByteBuffer) {
        if (colorByteBuffer != null) {
            int colorSize = renderableConfig.getColorSize();
            int colorType = renderableConfig.getColorType();
            setColorEnabled(true);
            GL.vaHelper.bindColors(colorSize, colorType, colorByteBuffer);
        } else {
            setColorEnabled(false);
        }
    }

    public void bindNormal(ByteBuffer normalByteBuffer) {
        if (normalByteBuffer != null) {
            int normalType = renderableConfig.getNormalType();
            setNormalEnabled(true);
            GL.vaHelper.bindNormals(normalType, normalByteBuffer);
        } else {
            setNormalEnabled(false);
        }
    }

    public void bindTextureCoordinate(ByteBuffer texCoordByteBuffer) {
        if (texCoordByteBuffer != null) {
            int texCoordSize = renderableConfig.getTexCoordSize();
            int texCoordType = renderableConfig.getTexCoordType();
            setTextureCoordinateEnabled(true);
            GL.vaHelper.bindTextureCoordinates(texCoordSize, texCoordType, texCoordByteBuffer);
        } else {
            setTextureCoordinateEnabled(false);
        }
    }

    public void bindVertex(ByteBuffer vertexByteBuffer) {
        if (vertexByteBuffer != null) {
            int vertexSize = renderableConfig.getVertexSize();
            int vertexType = renderableConfig.getVertexType();
            setVertexEnabled(true);
            GL.vaHelper.bindVertices(vertexSize, vertexType, vertexByteBuffer);
        } else {
            setVertexEnabled(false);
        }
    }

    public final void reset() {
        colorEnabled = false;
        normalEnabled = false;
        texCoordEnabled = false;
        vertexEnabled = false;
    }

    public void unbind() {
        bindColor(null);
        bindNormal(null);
        bindTextureCoordinate(null);
        bindVertex(null);
    }

}
