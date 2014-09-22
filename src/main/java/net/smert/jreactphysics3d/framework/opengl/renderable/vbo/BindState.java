package net.smert.jreactphysics3d.framework.opengl.renderable.vbo;

import net.smert.jreactphysics3d.framework.opengl.GL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class BindState {

    private final static Logger log = LoggerFactory.getLogger(BindState.class);

    private boolean colorEnabled;
    private boolean normalEnabled;
    private boolean texCoordEnabled;
    private boolean vertexEnabled;
    private int vboColorID;
    private int vboNormalID;
    private int vboTexCoordID;
    private int vboVertexID;
    private int vboVertexIndexID;
    private final Configuration vboConfiguration;

    public BindState(Configuration vboConfiguration) {
        this.vboConfiguration = vboConfiguration;

        if (log.isInfoEnabled()) {
            log.info("Creating BindState:"
                    + " Color Size: " + vboConfiguration.getColorSize()
                    + " Color Type: " + vboConfiguration.getColorType()
                    + " Normal Type: " + vboConfiguration.getNormalType()
                    + " Texture Coordinate Size: " + vboConfiguration.getTexCoordSize()
                    + " Texture Coordinate Type: " + vboConfiguration.getTexCoordType()
                    + " Vertex Size: " + vboConfiguration.getVertexSize()
                    + " Vertex Type: " + vboConfiguration.getVertexType());
        }
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
                int colorSize = vboConfiguration.getColorSize();
                int colorType = vboConfiguration.getColorType();
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
                int normalType = vboConfiguration.getNormalType();
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
                int texCoordSize = vboConfiguration.getTexCoordSize();
                int texCoordType = vboConfiguration.getTexCoordType();
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
                int vertexSize = vboConfiguration.getVertexSize();
                int vertexType = vboConfiguration.getVertexType();
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

}
