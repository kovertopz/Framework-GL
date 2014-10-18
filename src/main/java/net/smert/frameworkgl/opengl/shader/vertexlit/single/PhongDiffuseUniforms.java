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

import net.smert.frameworkgl.opengl.AmbientLight;
import net.smert.frameworkgl.opengl.GLLight;
import net.smert.frameworkgl.opengl.MaterialLight;
import net.smert.frameworkgl.opengl.shader.DefaultShaderUniforms;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class PhongDiffuseUniforms extends DefaultShaderUniforms {

    private final DiffuseUniforms diffuseUniforms;

    public PhongDiffuseUniforms(int programID) {
        super(programID);
        diffuseUniforms = new DiffuseUniforms(programID);
    }

    public void setColorMaterialAmbient(float colorMaterialAmbient) {
        diffuseUniforms.setColorMaterialAmbient(colorMaterialAmbient);
    }

    public void setColorMaterialDiffuse(float colorMaterialDiffuse) {
        diffuseUniforms.setColorMaterialDiffuse(colorMaterialDiffuse);
    }

    public void setColorMaterialEmission(float colorMaterialEmission) {
        diffuseUniforms.setColorMaterialEmission(colorMaterialEmission);
    }

    public void setAmbientLight(AmbientLight ambientLight) {
        diffuseUniforms.setAmbientLight(ambientLight);
    }

    public void setLight(GLLight glLight) {
        diffuseUniforms.setLight(glLight);
    }

    public void setMaterialLight(MaterialLight materialLight) {
        diffuseUniforms.setMaterialLight(materialLight);
    }

    @Override
    public void updateUniformLocations() {
        super.updateUniformLocations();
        diffuseUniforms.updateUniformLocations();
    }

}
