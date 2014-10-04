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
package net.smert.jreactphysics3d.framework.opengl.renderable.vbo;

import net.smert.jreactphysics3d.framework.opengl.GL;
import net.smert.jreactphysics3d.framework.opengl.renderable.factory.Renderable;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VBOBindState {

    private boolean colorEnabled;
    private boolean normalEnabled;
    private boolean texCoordEnabled;
    private boolean vertexEnabled;
    private int vboColorID;
    private int vboNormalID;
    private int vboTexCoordID;
    private int vboVertexID;
    private int vboVertexIndexID;

    public VBOBindState() {
        Renderable.config.throwExceptionIfNotImmutable();
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

    private void setTextureCoordinateEnabled(boolean enabled) {
        if (texCoordEnabled == enabled) {
            return;
        }
        texCoordEnabled = enabled;
        if (enabled) {
            GL.vboHelper.enableTextureCoordinates();
        } else {
            GL.vboHelper.disableTextureCoordinates();
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

    public void bindColor(int vboid, int strideBytes, int colorOffsetBytes) {
        if (vboColorID == vboid) {
            return;
        }
        vboColorID = vboid;
        if (vboid == 0) {
            setColorEnabled(false);
            return;
        }
        int colorSize = Renderable.config.getColorSize();
        int colorType = Renderable.config.getColorType();
        setColorEnabled(true);
        GL.vboHelper.bindColors(vboid, colorSize, colorType, strideBytes, colorOffsetBytes);
    }

    public void bindNormal(int vboid, int strideBytes, int normalOffsetBytes) {
        if (vboNormalID == vboid) {
            return;
        }
        vboNormalID = vboid;
        if (vboid == 0) {
            setNormalEnabled(false);
            return;
        }
        int normalType = Renderable.config.getNormalType();
        setNormalEnabled(true);
        GL.vboHelper.bindNormals(vboid, normalType, strideBytes, normalOffsetBytes);
    }

    public void bindTextureCoordinate(int vboid, int strideBytes, int texCoordOffsetBytes) {
        if (vboTexCoordID == vboid) {
            return;
        }
        vboTexCoordID = vboid;
        if (vboid == 0) {
            setTextureCoordinateEnabled(false);
            return;
        }
        int texCoordSize = Renderable.config.getTexCoordSize();
        int texCoordType = Renderable.config.getTexCoordType();
        setTextureCoordinateEnabled(true);
        GL.vboHelper.bindTextureCoordinates(
                vboid, texCoordSize, texCoordType, strideBytes, texCoordOffsetBytes);
    }

    public void bindVertex(int vboid, int strideBytes, int vertexOffsetBytes) {
        if (vboVertexID == vboid) {
            return;
        }
        vboVertexID = vboid;
        if (vboid == 0) {
            setVertexEnabled(false);
        }
        int vertexSize = Renderable.config.getVertexSize();
        int vertexType = Renderable.config.getVertexType();
        setVertexEnabled(true);
        GL.vboHelper.bindVertices(vboid, vertexSize, vertexType, strideBytes, vertexOffsetBytes);
    }

    public void bindVertexIndex(int vboid) {
        if (vboVertexIndexID == vboid) {
            return;
        }
        vboVertexIndexID = vboid;
        if (vboid == 0) {
            return;
        }
        GL.vboHelper.bindVerticesIndex(vboid);
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
