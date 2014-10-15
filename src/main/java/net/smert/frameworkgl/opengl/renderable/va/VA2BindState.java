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
package net.smert.frameworkgl.opengl.renderable.va;

import java.nio.ByteBuffer;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderable.RenderableConfiguration;
import net.smert.frameworkgl.opengl.shader.DefaultAttribLocations;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VA2BindState {

    private boolean colorEnabled;
    private boolean normalEnabled;
    private boolean texCoordEnabled;
    private boolean vertexEnabled;
    private int colorIndex;
    private int normalIndex;
    private int texCoord0Index;
    private int vertexIndex;
    private int renderableConfigID;
    private RenderableConfiguration config;

    public VA2BindState() {
        reset();
    }

    private void setColorEnabled(boolean enabled) {
        if (colorEnabled == enabled) {
            return;
        }
        colorEnabled = enabled;
        if (enabled) {
            GL.vaHelper.enableVertexAttribArray(colorIndex);
        } else {
            GL.vaHelper.disableVertexAttribArray(colorIndex);
        }
    }

    private void setNormalEnabled(boolean enabled) {
        if (normalEnabled == enabled) {
            return;
        }
        normalEnabled = enabled;
        if (enabled) {
            GL.vaHelper.enableVertexAttribArray(normalIndex);
        } else {
            GL.vaHelper.disableVertexAttribArray(normalIndex);
        }
    }

    private void setTexCoordEnabled(boolean enabled) {
        if (texCoordEnabled == enabled) {
            return;
        }
        texCoordEnabled = enabled;
        if (enabled) {
            GL.vaHelper.enableVertexAttribArray(texCoord0Index);
        } else {
            GL.vaHelper.disableVertexAttribArray(texCoord0Index);
        }
    }

    private void setVertexEnabled(boolean enabled) {
        if (vertexEnabled == enabled) {
            return;
        }
        vertexEnabled = enabled;
        if (enabled) {
            GL.vaHelper.enableVertexAttribArray(vertexIndex);
        } else {
            GL.vaHelper.disableVertexAttribArray(vertexIndex);
        }
    }

    public void bindColor(ByteBuffer colorByteBuffer) {
        if (colorByteBuffer == null) {
            setColorEnabled(false);
            return;
        }
        setColorEnabled(true);
        GL.vaHelper.bindVertexAttrib(colorIndex, config.getColorSize(), config.getColorType(), colorByteBuffer);
    }

    public void bindNormal(ByteBuffer normalByteBuffer) {
        if (normalByteBuffer == null) {
            setNormalEnabled(false);
            return;
        }
        setNormalEnabled(true);
        GL.vaHelper.bindVertexAttrib(normalIndex, config.getNormalSize(), config.getNormalType(), normalByteBuffer);
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

    public void bindVertex(ByteBuffer vertexByteBuffer) {
        if (vertexByteBuffer == null) {
            setVertexEnabled(false);
            return;
        }
        setVertexEnabled(true);
        GL.vaHelper.bindVertexAttrib(vertexIndex, config.getVertexSize(), config.getVertexType(), vertexByteBuffer);
    }

    public final void reset() {
        colorEnabled = false;
        normalEnabled = false;
        texCoordEnabled = false;
        vertexEnabled = false;
        colorIndex = -1;
        normalIndex = -1;
        texCoord0Index = -1;
        vertexIndex = -1;
        renderableConfigID = Integer.MIN_VALUE; // Default is -1 elsewhere
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

    public void unbind() {
        bindColor(null);
        bindNormal(null);
        bindTexCoord(null);
        bindVertex(null);
    }

}
