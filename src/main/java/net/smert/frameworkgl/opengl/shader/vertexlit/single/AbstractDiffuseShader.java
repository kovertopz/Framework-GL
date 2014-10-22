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
package net.smert.frameworkgl.opengl.shader.vertexlit.single;

import java.nio.FloatBuffer;
import net.smert.frameworkgl.opengl.Shader;
import net.smert.frameworkgl.opengl.TextureType;
import net.smert.frameworkgl.opengl.constants.TextureUnit;
import net.smert.frameworkgl.opengl.shader.AbstractShader;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public abstract class AbstractDiffuseShader extends AbstractShader {

    private final DiffuseUniforms uniforms;

    public AbstractDiffuseShader(DiffuseUniforms uniforms, Shader shader) {
        super(uniforms, shader);
        this.uniforms = uniforms;
        setTextureUnit(TextureType.DIFFUSE, TextureUnit.TEXTURE0);
    }

    public DiffuseUniforms getUniforms() {
        return uniforms;
    }

    @Override
    public void sendUniformMatrices(FloatBuffer matrixFloatBuffer) {
        super.sendUniformMatrices(matrixFloatBuffer);
        viewModelMatrix.toMatrix3f(normalMatrix);
        normalMatrix.toFloatBuffer(matrixFloatBuffer);
        matrixFloatBuffer.flip();
        uniforms.setNormalMatrix(false, matrixFloatBuffer);
        matrixFloatBuffer.clear();
        viewModelMatrix.toFloatBuffer(matrixFloatBuffer);
        matrixFloatBuffer.flip();
        uniforms.setViewModelMatrix(false, matrixFloatBuffer);
    }

}
