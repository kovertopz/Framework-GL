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
import java.util.HashMap;
import java.util.Map;
import net.smert.frameworkgl.math.Matrix3f;
import net.smert.frameworkgl.math.Matrix4f;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.Shader;
import net.smert.frameworkgl.opengl.TextureType;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public abstract class AbstractShader {

    protected final static Matrix3f normalMatrix = new Matrix3f();
    protected final static Matrix4f projectionViewMatrix = new Matrix4f();
    protected final static Matrix4f projectionViewModelMatrix = new Matrix4f();
    protected final static Matrix4f viewModelMatrix = new Matrix4f();

    private final DefaultShaderUniforms shaderUniforms;
    private final Map<TextureType, Integer> textureTypeToTextureUnit;
    private final Shader shader;

    public AbstractShader(DefaultShaderUniforms shaderUniforms, Shader shader) {
        this.shaderUniforms = shaderUniforms;
        textureTypeToTextureUnit = new HashMap<>();
        this.shader = shader;
    }

    public void bind() {
        GL.shaderHelper.bind(shader.getProgramID());
    }

    public void destroy() {
        shader.destroy();
    }

    public int getTextureUnit(TextureType textureType) {
        Integer textureUnit = textureTypeToTextureUnit.get(textureType);
        if (textureUnit == null) {
            throw new RuntimeException("The shader does not support the texture type: " + textureType);
        }
        return textureUnit;
    }

    public void setTextureUnit(TextureType textureType, int textureUnit) {
        textureTypeToTextureUnit.put(textureType, textureUnit);
    }

    public DefaultShaderUniforms getDefaultShaderUniforms() {
        return shaderUniforms;
    }

    public void init() {
        bind();
        shaderUniforms.setTextureLocations();
        shaderUniforms.updateUniformLocations();
        unbind();
    }

    public void sendUniformMatrices(FloatBuffer matrixFloatBuffer) {
        GL.matrixHelper.multiplyViewAndModelMatrix(viewModelMatrix);
        GL.matrixHelper.multiplyProjectionAndViewModelMatrix(viewModelMatrix, projectionViewModelMatrix);
        projectionViewModelMatrix.toFloatBuffer(matrixFloatBuffer);
        matrixFloatBuffer.flip();
        shaderUniforms.setProjectionViewModelMatrix(false, matrixFloatBuffer);
    }

    public void sendUniformTextureFlag(float flag) {
        shaderUniforms.setTextureFlag(flag);
    }

    public void unbind() {
        GL.shaderHelper.unbind();
    }

}
