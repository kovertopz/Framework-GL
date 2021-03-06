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
package net.smert.frameworkgl.opengl.shader.vertexlit.multi;

import net.smert.frameworkgl.opengl.GL;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class SpecularUniforms extends DiffuseUniforms {

    private int uniformColorMaterialSpecularID;

    public SpecularUniforms(int programID) {
        super(programID);
    }

    public void setColorMaterialSpecular(float colorMaterialSpecular) {
        GL.shaderUniformHelper.setUniform(uniformColorMaterialSpecularID, colorMaterialSpecular);
    }

    @Override
    public void updateUniformLocations() {
        super.updateUniformLocations();
        uniformColorMaterialSpecularID = GL.shaderUniformHelper.getUniformLocation(programID, "uColorMaterialSpecular");
    }

}
