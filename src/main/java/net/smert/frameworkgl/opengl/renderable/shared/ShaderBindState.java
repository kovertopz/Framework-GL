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

import net.smert.frameworkgl.opengl.Shader;
import net.smert.frameworkgl.opengl.constants.TextureUnit;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class ShaderBindState {

    private float textureFlag;
    private Shader shader;

    public ShaderBindState() {
        reset();
    }

    private void setShaderUniformTextureFlag(float flag) {
        //shader.setUniformTextureFlag(flag);
    }

    public final void reset() {
    }

    public void setTextureFlag(float flag) {
        if (textureFlag == flag) {
            return;
        }
        textureFlag = flag;
        setShaderUniformTextureFlag(flag);
    }

    public void bindShader(int shader) {
    }

    public int getTextureUnit(int textureTypeID) {
        return TextureUnit.TEXTURE0;
    }

}
