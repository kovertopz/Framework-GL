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
package net.smert.frameworkgl.opengl.renderable.shared;

import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderable.RenderableConfiguration;
import net.smert.frameworkgl.opengl.shader.DefaultAttribLocations;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class BindState {

    private boolean vaoUnbinded;
    private boolean vboUnbinded;
    private int colorIndex;
    private int normalIndex;
    private int texCoord0Index;
    private int renderableConfigID;
    private int vaoID;
    private int vboColorID;
    private int vboNormalID;
    private int vboTexCoordID;
    private int vboVertexID;
    private int vboVertexIndexID;
    private int vertexIndex;
    private RenderableConfiguration config;

    public BindState() {
        reset();
    }

    public void bindColorGL1(int vboID, int strideBytes, int colorOffsetBytes) {
        if (vboColorID == vboID) {
            return;
        }
        vboUnbinded = false;
        vboColorID = vboID;
        GL.vboHelper.bindColors(vboID, config.getColorSize(), config.getColorType(), strideBytes, colorOffsetBytes);
    }

    public void bindColorGL2(int vboID, int strideBytes, int colorOffsetBytes) {
        if (vboColorID == vboID) {
            return;
        }
        vboUnbinded = false;
        vboColorID = vboID;
        GL.vboHelper.bindVertexAttrib(vboID, colorIndex, config.getColorSize(), config.getColorType(), strideBytes,
                colorOffsetBytes);
    }

    public void bindNormalGL1(int vboID, int strideBytes, int normalOffsetBytes) {
        if (vboNormalID == vboID) {
            return;
        }
        vboUnbinded = false;
        vboNormalID = vboID;
        GL.vboHelper.bindNormals(vboID, config.getNormalType(), strideBytes, normalOffsetBytes);
    }

    public void bindNormalGL2(int vboID, int strideBytes, int normalOffsetBytes) {
        if (vboNormalID == vboID) {
            return;
        }
        vboUnbinded = false;
        vboNormalID = vboID;
        GL.vboHelper.bindVertexAttrib(vboID, normalIndex, config.getNormalSize(), config.getNormalType(), strideBytes,
                normalOffsetBytes);
    }

    public void bindTexCoordGL1(int vboID, int strideBytes, int texCoordOffsetBytes) {
        if (vboTexCoordID == vboID) {
            return;
        }
        vboUnbinded = false;
        vboTexCoordID = vboID;
        GL.vboHelper.bindTexCoords(vboID, config.getTexCoordSize(), config.getTexCoordType(), strideBytes,
                texCoordOffsetBytes);
    }

    public void bindTexCoordGL2(int vboID, int strideBytes, int texCoordOffsetBytes) {
        if (vboTexCoordID == vboID) {
            return;
        }
        vboUnbinded = false;
        vboTexCoordID = vboID;
        GL.vboHelper.bindVertexAttrib(vboID, texCoord0Index, config.getTexCoordSize(), config.getTexCoordType(),
                strideBytes, texCoordOffsetBytes);
    }

    public void bindVAO(int vaoID) {
        if (this.vaoID == vaoID) {
            return;
        }
        vaoUnbinded = false;
        vboUnbinded = false;
        this.vaoID = vaoID;
        GL.vaoHelper.bind(vaoID);
    }

    public void bindVertexGL1(int vboID, int strideBytes, int vertexOffsetBytes) {
        if (vboVertexID == vboID) {
            return;
        }
        vboUnbinded = false;
        vboVertexID = vboID;
        GL.vboHelper.bindVertices(vboID, config.getVertexSize(), config.getVertexType(), strideBytes,
                vertexOffsetBytes);
    }

    public void bindVertexGL2(int vboID, int strideBytes, int vertexOffsetBytes) {
        if (vboVertexID == vboID) {
            return;
        }
        vboUnbinded = false;
        vboVertexID = vboID;
        GL.vboHelper.bindVertexAttrib(vboID, vertexIndex, config.getVertexSize(), config.getVertexType(), strideBytes,
                vertexOffsetBytes);
    }

    public void bindVertexIndex(int vboID) {
        if (vboVertexIndexID == vboID) {
            return;
        }
        vboVertexIndexID = vboID;
        GL.vboHelper.bindVerticesIndex(vboID);
    }

    public int getColorIndex() {
        return colorIndex;
    }

    public int getNormalIndex() {
        return normalIndex;
    }

    public int getTexCoord0Index() {
        return texCoord0Index;
    }

    public int getVertexIndex() {
        return vertexIndex;
    }

    public RenderableConfiguration getConfig() {
        return config;
    }

    public final void reset() {
        vaoUnbinded = true;
        vboUnbinded = true;
        colorIndex = -1;
        normalIndex = -1;
        renderableConfigID = Integer.MIN_VALUE; // Default is -1 elsewhere
        texCoord0Index = -1;
        vaoID = 0;
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
        vertexIndex = defaultAttribLocations.getIndex("vertex");
    }

    public void switchRenderableConfiguration(int renderableConfigID) {
        if (this.renderableConfigID == renderableConfigID) {
            return;
        }
        this.renderableConfigID = renderableConfigID;
        config = Renderable.configPool.get(renderableConfigID);
    }

    public void unbindVAO() {
        unbindVBO();
        if (!vaoUnbinded) {
            vaoUnbinded = true;
            vaoID = 0;
            GL.vaoHelper.unbind();
        }
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
