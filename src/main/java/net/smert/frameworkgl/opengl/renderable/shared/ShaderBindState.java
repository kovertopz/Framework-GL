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
import net.smert.frameworkgl.gameobjects.GameObject;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.TextureType;
import net.smert.frameworkgl.opengl.camera.Camera;
import net.smert.frameworkgl.opengl.mesh.Segment;
import net.smert.frameworkgl.opengl.shader.AbstractShader;
import net.smert.frameworkgl.opengl.shader.DefaultDoNothingShader;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class ShaderBindState {

    private boolean shaderBinded;
    private float textureFlag;
    private final AbstractShader defaultDoNothingShader;
    private AbstractShader shader;
    private FloatBuffer matrixFloatBuffer;

    public ShaderBindState() {
        defaultDoNothingShader = new DefaultDoNothingShader();
        reset();
    }

    public int getTextureUnit(TextureType textureType) {
        return shader.getTextureUnit(textureType);
    }

    public void init() {
        matrixFloatBuffer = GL.bufferHelper.createFloatBuffer(16);
    }

    public final void reset() {
        shaderBinded = false;
        textureFlag = Float.MIN_VALUE; // So that the first call to sendUniformTextureFlag does an update
        shader = defaultDoNothingShader;
    }

    public void sendUniformMatrices() {
        shader.sendUniformMatrices(matrixFloatBuffer);
    }

    public void sendUniformsOncePerBind() {
        shader.sendUniformsOncePerBind(matrixFloatBuffer);
    }

    public void sendUniformsOncePerGameObject(GameObject gameObject) {
        shader.sendUniformsOncePerGameObject(matrixFloatBuffer, gameObject);
    }

    public void sendUniformsOncePerRenderCall(Segment segment) {
        shader.sendUniformsOncePerRenderCall(matrixFloatBuffer, segment);
    }

    public void sendUniformTextureFlag(float flag) {
        if (textureFlag == flag) {
            return;
        }
        textureFlag = flag;
        shader.sendUniformTextureFlag(flag);
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

    public void switchShader(AbstractShader shader) {
        if (this.shader == shader) {
            if (!shaderBinded) {
                shader.bind();
                sendUniformsOncePerBind();
                shaderBinded = true;
            }
            return;
        }
        this.shader = shader;
        shader.bind();
        shaderBinded = true;
        textureFlag = Float.MIN_VALUE; // Reset the flag
        sendUniformsOncePerBind();
        sendUniformTextureFlag(0f); // Reset the uniform for the texture flag
    }

    public void unbindShader() {
        shader.unbind();
        shaderBinded = false;
    }

}
