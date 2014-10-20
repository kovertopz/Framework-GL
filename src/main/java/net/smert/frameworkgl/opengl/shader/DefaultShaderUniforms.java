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
package net.smert.frameworkgl.opengl.shader;

import java.nio.FloatBuffer;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.renderable.Renderable;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class DefaultShaderUniforms {

    private int uniformModelMatrixID;
    private int uniformNormalMatrixID;
    private int uniformProjectionMatrixID;
    private int uniformProjectionViewMatrixID;
    private int uniformProjectionViewModelMatrixID;
    private int uniformTextureFlagID;
    private int uniformViewMatrixID;
    private int uniformViewModelMatrixID;
    protected final int programID;

    public DefaultShaderUniforms(int programID) {
        this.programID = programID;
    }

    public void setModelMatrix(boolean transpose, FloatBuffer matrix) {
        GL.shaderUniformHelper.setUniformMatrix4(uniformModelMatrixID, transpose, matrix);
    }

    public void setNormalMatrix(boolean transpose, FloatBuffer matrix) {
        GL.shaderUniformHelper.setUniformMatrix3(uniformNormalMatrixID, transpose, matrix);
    }

    public void setProjectionMatrix(boolean transpose, FloatBuffer matrix) {
        GL.shaderUniformHelper.setUniformMatrix4(uniformProjectionMatrixID, transpose, matrix);
    }

    public void setProjectionViewMatrix(boolean transpose, FloatBuffer matrix) {
        GL.shaderUniformHelper.setUniformMatrix4(uniformProjectionViewMatrixID, transpose, matrix);
    }

    public void setProjectionViewModelMatrix(boolean transpose, FloatBuffer matrix) {
        GL.shaderUniformHelper.setUniformMatrix4(uniformProjectionViewModelMatrixID, transpose, matrix);
    }

    public void setTextureFlag(float textureFlag) {
        GL.shaderUniformHelper.setUniform(uniformTextureFlagID, textureFlag);
    }

    public void setTextureLocations() {
        int maxTextureUnits = Renderable.textureBindState.getMaxTextureUnits();
        for (int i = 0; i < maxTextureUnits; i++) {
            int uniformTextureID = GL.shaderUniformHelper.getUniformLocation(programID, "uTexture" + i);
            GL.shaderUniformHelper.setUniform(uniformTextureID, i);
        }
    }

    public void setViewMatrix(boolean transpose, FloatBuffer matrix) {
        GL.shaderUniformHelper.setUniformMatrix4(uniformViewMatrixID, transpose, matrix);
    }

    public void setViewModelMatrix(boolean transpose, FloatBuffer matrix) {
        GL.shaderUniformHelper.setUniformMatrix4(uniformViewModelMatrixID, transpose, matrix);
    }

    public void updateUniformLocations() {
        uniformModelMatrixID = GL.shaderUniformHelper.getUniformLocation(programID, "uModelMatrix");
        uniformNormalMatrixID = GL.shaderUniformHelper.getUniformLocation(programID, "uNormalMatrix");
        uniformProjectionMatrixID = GL.shaderUniformHelper.getUniformLocation(programID, "uProjectionMatrix");
        uniformProjectionViewMatrixID = GL.shaderUniformHelper.getUniformLocation(programID, "uProjectionViewMatrix");
        uniformProjectionViewModelMatrixID
                = GL.shaderUniformHelper.getUniformLocation(programID, "uProjectionViewModelMatrix");
        uniformTextureFlagID = GL.shaderUniformHelper.getUniformLocation(programID, "uTextureFlag");
        uniformViewMatrixID = GL.shaderUniformHelper.getUniformLocation(programID, "uViewMatrix");
        uniformViewModelMatrixID = GL.shaderUniformHelper.getUniformLocation(programID, "uViewModelMatrix");
    }

}
