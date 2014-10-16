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
package net.smert.frameworkgl.opengl.renderable.gl2;

import java.nio.ByteBuffer;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderable.RenderableConfiguration;
import net.smert.frameworkgl.opengl.shader.DefaultAttribLocations;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class BindStateGL2 {

    private boolean colorEnabled;
    private boolean normalEnabled;
    private boolean texCoordEnabled;
    private boolean vertexEnabled;
    private boolean vboUnbinded;
    private int colorIndex;
    private int normalIndex;
    private int renderableConfigID;
    private int texCoord0Index;
    private int vboColorID;
    private int vboNormalID;
    private int vboTexCoordID;
    private int vboVertexID;
    private int vboVertexIndexID;
    private int vertexIndex;
    private RenderableConfiguration config;

    public BindStateGL2() {
        reset();
    }

    private void setColorEnabled(boolean enabled) {
        if (colorEnabled == enabled) {
            return;
        }
        colorEnabled = enabled;
        if (enabled) {
            GL.vboHelper.enableVertexAttribArray(colorIndex);
        } else {
            GL.vboHelper.disableVertexAttribArray(colorIndex);
        }
    }

    private void setNormalEnabled(boolean enabled) {
        if (normalEnabled == enabled) {
            return;
        }
        normalEnabled = enabled;
        if (enabled) {
            GL.vboHelper.enableVertexAttribArray(normalIndex);
        } else {
            GL.vboHelper.disableVertexAttribArray(normalIndex);
        }
    }

    private void setTexCoordEnabled(boolean enabled) {
        if (texCoordEnabled == enabled) {
            return;
        }
        texCoordEnabled = enabled;
        if (enabled) {
            GL.vboHelper.enableVertexAttribArray(texCoord0Index);
        } else {
            GL.vboHelper.disableVertexAttribArray(texCoord0Index);
        }
    }

    private void setVertexEnabled(boolean enabled) {
        if (vertexEnabled == enabled) {
            return;
        }
        vertexEnabled = enabled;
        if (enabled) {
            GL.vboHelper.enableVertexAttribArray(vertexIndex);
        } else {
            GL.vboHelper.disableVertexAttribArray(vertexIndex);
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
        GL.vboHelper.bindVertexAttrib(vboID, colorIndex, config.getColorSize(), config.getColorType(), strideBytes,
                colorOffsetBytes);
    }

    public void bindColor(ByteBuffer colorByteBuffer) {
        if (colorByteBuffer == null) {
            setColorEnabled(false);
            return;
        }
        setColorEnabled(true);
        GL.vaHelper.bindVertexAttrib(colorIndex, config.getColorSize(), config.getColorType(), colorByteBuffer);
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
        GL.vboHelper.bindVertexAttrib(vboID, normalIndex, config.getNormalSize(), config.getNormalType(), strideBytes,
                normalOffsetBytes);
    }

    public void bindNormal(ByteBuffer normalByteBuffer) {
        if (normalByteBuffer == null) {
            setNormalEnabled(false);
            return;
        }
        setNormalEnabled(true);
        GL.vaHelper.bindVertexAttrib(normalIndex, config.getNormalSize(), config.getNormalType(), normalByteBuffer);
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
        GL.vboHelper.bindVertexAttrib(vboID, texCoord0Index, config.getTexCoordSize(), config.getTexCoordType(),
                strideBytes, texCoordOffsetBytes);
    }

    public void bindTexCoord(ByteBuffer texCoordByteBuffer) {
        if (texCoordByteBuffer == null) {
            setTexCoordEnabled(false);
            return;
        }
        setTexCoordEnabled(true);
        GL.vaHelper.bindVertexAttrib(texCoord0Index, config.getTexCoordSize(), config.getTexCoordType(),
                texCoordByteBuffer);
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
        GL.vboHelper.bindVertexAttrib(vboID, vertexIndex, config.getVertexSize(), config.getVertexType(), strideBytes,
                vertexOffsetBytes);
    }

    public void bindVertex(ByteBuffer vertexByteBuffer) {
        if (vertexByteBuffer == null) {
            setVertexEnabled(false);
            return;
        }
        setVertexEnabled(true);
        GL.vaHelper.bindVertexAttrib(vertexIndex, config.getVertexSize(), config.getVertexType(), vertexByteBuffer);
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
        colorIndex = -1;
        normalIndex = -1;
        renderableConfigID = Integer.MIN_VALUE; // Default is -1 elsewhere
        texCoord0Index = -1;
        vboColorID = 0;
        vboNormalID = 0;
        vboTexCoordID = 0;
        vboVertexID = 0;
        vboVertexIndexID = 0;
        vertexIndex = -1;
        config = null;
    }

    public void setAttribLocations(DefaultAttribLocations defaultAttribLocations) {
        colorIndex = defaultAttribLocations.getIndex("color");
        normalIndex = defaultAttribLocations.getIndex("normal");
        texCoord0Index = defaultAttribLocations.getIndex("texCoord0");
        vertexIndex = defaultAttribLocations.getIndex("color");
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
