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
package net.smert.frameworkgl.opengl.shader.pixellit.multi;

import java.util.List;
import net.smert.frameworkgl.opengl.AmbientLight;
import net.smert.frameworkgl.opengl.GLLight;
import net.smert.frameworkgl.opengl.MaterialLight;
import net.smert.frameworkgl.opengl.shader.DefaultShaderUniforms;
import net.smert.frameworkgl.opengl.shader.common.MultiLightAndMaterialUniforms;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class DiffuseUniforms extends DefaultShaderUniforms {

    private final MultiLightAndMaterialUniforms multiLightAndMaterialUniforms;

    public DiffuseUniforms(int programID) {
        super(programID);
        multiLightAndMaterialUniforms = new MultiLightAndMaterialUniforms(programID);
    }

    public void setAmbientLight(AmbientLight ambientLight) {
        multiLightAndMaterialUniforms.setAmbientLight(ambientLight);
    }

    public void setLights(List<GLLight> glLights) {
        multiLightAndMaterialUniforms.setLights(glLights);
    }

    public void setMaterialLight(MaterialLight materialLight) {
        multiLightAndMaterialUniforms.setMaterialLight(materialLight);
    }

    @Override
    public void updateUniformLocations() {
        super.updateUniformLocations();
        multiLightAndMaterialUniforms.updateUniformLocations();
    }

}
