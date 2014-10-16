package net.smert.frameworkgl.opengl.renderable.gl1;

import java.nio.ByteBuffer;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderable.RenderableConfiguration;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class BindStateGL1 {

    private boolean colorEnabled;
    private boolean normalEnabled;
    private boolean texCoordEnabled;
    private boolean vertexEnabled;
    private boolean vboUnbinded;
    private int renderableConfigID;
    private int vboColorID;
    private int vboNormalID;
    private int vboTexCoordID;
    private int vboVertexID;
    private int vboVertexIndexID;
    private RenderableConfiguration config;

    public BindStateGL1() {
        reset();
    }

    private void setColorEnabled(boolean enabled) {
        if (colorEnabled == enabled) {
            return;
        }
        colorEnabled = enabled;
        if (enabled) {
            GL.vboHelper.enableColors();
        } else {
            GL.vboHelper.disableColors();
        }
    }

    private void setNormalEnabled(boolean enabled) {
        if (normalEnabled == enabled) {
            return;
        }
        normalEnabled = enabled;
        if (enabled) {
            GL.vboHelper.enableNormals();
        } else {
            GL.vboHelper.disableNormals();
        }
    }

    private void setTexCoordEnabled(boolean enabled) {
        if (texCoordEnabled == enabled) {
            return;
        }
        texCoordEnabled = enabled;
        if (enabled) {
            GL.vboHelper.enableTexCoords();
        } else {
            GL.vboHelper.disableTexCoords();
        }
    }

    private void setVertexEnabled(boolean enabled) {
        if (vertexEnabled == enabled) {
            return;
        }
        vertexEnabled = enabled;
        if (enabled) {
            GL.vboHelper.enableVertices();
        } else {
            GL.vboHelper.disableVertices();
        }
    }

    public void bindColor(int vboID, int strideBytes, int colorOffsetBytes) {
        if (vboColorID == vboID) {
            return;
        }
        vboUnbinded = false;
        vboColorID = vboID;
        if (vboID == 0) {
            setColorEnabled(false);
            return;
        }
        setColorEnabled(true);
        GL.vboHelper.bindColors(vboID, config.getColorSize(), config.getColorType(), strideBytes, colorOffsetBytes);
    }

    public void bindColor(ByteBuffer colorByteBuffer) {
        if (colorByteBuffer == null) {
            setColorEnabled(false);
            return;
        }
        setColorEnabled(true);
        GL.vaHelper.bindColors(config.getColorSize(), config.getColorType(), colorByteBuffer);
    }

    public void bindNormal(int vboID, int strideBytes, int normalOffsetBytes) {
        if (vboNormalID == vboID) {
            return;
        }
        vboUnbinded = false;
        vboNormalID = vboID;
        if (vboID == 0) {
            setNormalEnabled(false);
            return;
        }
        setNormalEnabled(true);
        GL.vboHelper.bindNormals(vboID, config.getNormalType(), strideBytes, normalOffsetBytes);
    }

    public void bindNormal(ByteBuffer normalByteBuffer) {
        if (normalByteBuffer == null) {
            setNormalEnabled(false);
            return;
        }
        setNormalEnabled(true);
        GL.vaHelper.bindNormals(config.getNormalType(), normalByteBuffer);
    }

    public void bindTexCoord(int vboID, int strideBytes, int texCoordOffsetBytes) {
        if (vboTexCoordID == vboID) {
            return;
        }
        vboUnbinded = false;
        vboTexCoordID = vboID;
        if (vboID == 0) {
            setTexCoordEnabled(false);
            return;
        }
        setTexCoordEnabled(true);
        GL.vboHelper.bindTexCoords(vboID, config.getTexCoordSize(), config.getTexCoordType(), strideBytes,
                texCoordOffsetBytes);
    }

    public void bindTexCoord(ByteBuffer texCoordByteBuffer) {
        if (texCoordByteBuffer == null) {
            setTexCoordEnabled(false);
            return;
        }
        setTexCoordEnabled(true);
        GL.vaHelper.bindTexCoords(config.getTexCoordSize(), config.getTexCoordType(), texCoordByteBuffer);
    }

    public void bindVertex(int vboID, int strideBytes, int vertexOffsetBytes) {
        if (vboVertexID == vboID) {
            return;
        }
        vboUnbinded = false;
        vboVertexID = vboID;
        if (vboID == 0) {
            setVertexEnabled(false);
            return;
        }
        setVertexEnabled(true);
        GL.vboHelper.bindVertices(vboID, config.getVertexSize(), config.getVertexType(), strideBytes,
                vertexOffsetBytes);
    }

    public void bindVertex(ByteBuffer vertexByteBuffer) {
        if (vertexByteBuffer == null) {
            setVertexEnabled(false);
            return;
        }
        setVertexEnabled(true);
        GL.vaHelper.bindVertices(config.getVertexSize(), config.getVertexType(), vertexByteBuffer);
    }

    public void bindVertexIndex(int vboID) {
        if (vboVertexIndexID == vboID) {
            return;
        }
        vboVertexIndexID = vboID;
        GL.vboHelper.bindVerticesIndex(vboID);
    }

    public final void reset() {
        colorEnabled = false;
        normalEnabled = false;
        texCoordEnabled = false;
        vertexEnabled = false;
        vboUnbinded = true;
        renderableConfigID = Integer.MIN_VALUE; // Default is -1 elsewhere
        vboColorID = 0;
        vboNormalID = 0;
        vboTexCoordID = 0;
        vboVertexID = 0;
        vboVertexIndexID = 0;
        config = null;
    }

    public void switchRenderableConfiguration(int renderableConfigID) {
        if (this.renderableConfigID == renderableConfigID) {
            return;
        }
        this.renderableConfigID = renderableConfigID;
        config = Renderable.configPool.get(renderableConfigID);
    }

    public void unbindVBO() {
        if (!vboUnbinded) {
            vboUnbinded = true;
            vboColorID = 0;
            vboNormalID = 0;
            vboTexCoordID = 0;
            vboVertexID = 0;
            vboVertexIndexID = 0;
            GL.vboHelper.unbind();
        }
    }

}
