package net.smert.jreactphysics3d.framework.opengl.renderable.vbo;

import net.smert.jreactphysics3d.framework.opengl.GL;
import net.smert.jreactphysics3d.framework.opengl.renderable.factory.Configuration;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class BindState {

    private boolean colorEnabled;
    private boolean normalEnabled;
    private boolean texCoordEnabled;
    private boolean vertexEnabled;
    private int vboColorID;
    private int vboNormalID;
    private int vboTexCoordID;
    private int vboVertexID;
    private int vboVertexIndexID;
    private final Configuration renderableConfig;

    public BindState(Configuration renderableConfig) {
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
                GL.vboHelper.enableColors();
            } else {
                GL.vboHelper.disableColors();
            }
        }
    }

    private void setNormalEnabled(boolean enabled) {
        if (normalEnabled != enabled) {
            normalEnabled = enabled;

            if (enabled == true) {
                GL.vboHelper.enableNormals();
            } else {
                GL.vboHelper.disableNormals();
            }
        }
    }

    private void setTextureCoordinateEnabled(boolean enabled) {
        if (texCoordEnabled != enabled) {
            texCoordEnabled = enabled;

            if (enabled == true) {
                GL.vboHelper.enableTextureCoordinates();
            } else {
                GL.vboHelper.disableTextureCoordinates();
            }
        }
    }

    private void setVertexEnabled(boolean enabled) {
        if (vertexEnabled != enabled) {
            vertexEnabled = enabled;

            if (enabled == true) {
                GL.vboHelper.enableVertices();
            } else {
                GL.vboHelper.disableVertices();
            }
        }
    }

    public void bindColor(int vboid, int strideBytes, int colorOffsetBytes) {
        if (vboColorID != vboid) {
            vboColorID = vboid;

            if (vboid != 0) {
                int colorSize = renderableConfig.getColorSize();
                int colorType = renderableConfig.getColorType();
                setColorEnabled(true);
                GL.vboHelper.bindColors(vboid, colorSize, colorType, strideBytes, colorOffsetBytes);
            } else {
                setColorEnabled(false);
            }
        }
    }

    public void bindNormal(int vboid, int strideBytes, int normalOffsetBytes) {
        if (vboNormalID != vboid) {
            vboNormalID = vboid;

            if (vboid != 0) {
                int normalType = renderableConfig.getNormalType();
                setNormalEnabled(true);
                GL.vboHelper.bindNormals(vboid, normalType, strideBytes, normalOffsetBytes);
            } else {
                setNormalEnabled(false);
            }
        }
    }

    public void bindTextureCoordinate(int vboid, int strideBytes, int texCoordOffsetBytes) {
        if (vboTexCoordID != vboid) {
            vboTexCoordID = vboid;

            if (vboid != 0) {
                int texCoordSize = renderableConfig.getTexCoordSize();
                int texCoordType = renderableConfig.getTexCoordType();
                setTextureCoordinateEnabled(true);
                GL.vboHelper.bindTextureCoordinates(
                        vboid, texCoordSize, texCoordType, strideBytes, texCoordOffsetBytes);
            } else {
                setTextureCoordinateEnabled(false);
            }
        }
    }

    public void bindVertex(int vboid, int strideBytes, int vertexOffsetBytes) {
        if (vboVertexID != vboid) {
            vboVertexID = vboid;

            if (vboid != 0) {
                int vertexSize = renderableConfig.getVertexSize();
                int vertexType = renderableConfig.getVertexType();
                setVertexEnabled(true);
                GL.vboHelper.bindVertices(vboid, vertexSize, vertexType, strideBytes, vertexOffsetBytes);
            } else {
                setVertexEnabled(false);
            }
        }
    }

    public void bindVertexIndex(int vboid) {
        if (vboVertexIndexID != vboid) {
            vboVertexIndexID = vboid;

            if (vboid != 0) {
                GL.vboHelper.bindVerticesIndex(vboid);
            }
        }
    }

    public final void reset() {
        colorEnabled = false;
        normalEnabled = false;
        texCoordEnabled = false;
        vertexEnabled = false;
        vboColorID = 0;
        vboNormalID = 0;
        vboTexCoordID = 0;
        vboVertexID = 0;
        vboVertexIndexID = 0;
    }

    public void unbind() {
        bindColor(0, 0, 0);
        bindNormal(0, 0, 0);
        bindTextureCoordinate(0, 0, 0);
        bindVertex(0, 0, 0);
        bindVertexIndex(0);
        GL.vboHelper.unbind();
    }

}
