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

import java.nio.FloatBuffer;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.Shader;
import net.smert.frameworkgl.opengl.camera.Camera;
import net.smert.frameworkgl.opengl.constants.TextureUnit;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.shader.AbstractShader;
import net.smert.frameworkgl.opengl.shader.DefaultShaderUniforms;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class ShaderBindState {

    private boolean doNothingShaderActive;
    private boolean shaderBinded;
    private float textureFlag;
    private int uniqueShaderID;
    private final AbstractShader defaultDoNothingShader;
    private AbstractShader defaultShader;
    private AbstractShader shader;
    private FloatBuffer matrixFloatBuffer;

    public ShaderBindState() {
        defaultDoNothingShader = new DefaultDoNothingShader();
        reset();
    }

    public void bindShader(int uniqueShaderID) {
        if (this.uniqueShaderID == uniqueShaderID) {
            return;
        }
        this.uniqueShaderID = uniqueShaderID;
        if (uniqueShaderID != -1) {
            AbstractShader abstractShader = Renderable.shaderPool.get(uniqueShaderID);
            switchShader(abstractShader);
        } else {
            switchShader(defaultShader);
        }
    }

    public int getTextureUnit(int textureTypeID) {
        return TextureUnit.TEXTURE0;
    }

    public AbstractShader getDefaultShader() {
        return defaultShader;
    }

    public void setDefaultShader(AbstractShader defaultShader) {
        this.defaultShader = defaultShader;
    }

    public void init() {
        matrixFloatBuffer = GL.bufferHelper.createFloatBuffer(16);
    }

    public final void reset() {
        doNothingShaderActive = true;
        shaderBinded = false;
        textureFlag = 0f;
        uniqueShaderID = Integer.MIN_VALUE; // Default is -1 elsewhere
        defaultShader = null;
        defaultShader = defaultDoNothingShader;
        shader = defaultDoNothingShader;
    }

    public void sendUniformMatrices() {
        if (doNothingShaderActive) {
            return;
        }
        shader.sendUniformMatrices(matrixFloatBuffer);
    }

    public void setCamera(Camera camera) {
        camera.updateViewMatrix();
        GL.matrixHelper.setModeProjection();
        GL.matrixHelper.load(camera.getProjectionMatrix());
        GL.matrixHelper.setModeView();
        GL.matrixHelper.load(camera.getViewMatrix());
        GL.matrixHelper.setModeModel();
        GL.matrixHelper.loadIdentity();
    }

    public void setTextureFlag(float flag) {
        if (textureFlag == flag) {
            return;
        }
        textureFlag = flag;
        if (doNothingShaderActive) {
            return;
        }
        shader.sendUniformTextureFlag(flag);
    }

    public void switchShader(AbstractShader shader) {
        if (this.shader == shader) {
            if (!shaderBinded) {
                shader.bind();
            }
            return;
        }
        doNothingShaderActive = false;
        this.shader = shader;
        shader.bind();
    }

    public void unbindShader() {
        if (shader != null) {
            shader.unbind();
        }
        shaderBinded = false;
    }

    private static class DefaultDoNothingShader extends AbstractShader {

        public DefaultDoNothingShader() {
            super(new DefaultShaderUniforms(0), new Shader());
        }

        @Override
        public void bind() {
        }

        @Override
        public void unbind() {
        }

    }

}
