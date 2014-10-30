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

import java.util.List;
import net.smert.frameworkgl.opengl.AmbientLight;
import net.smert.frameworkgl.opengl.GLLight;
import net.smert.frameworkgl.opengl.MaterialLight;
import net.smert.frameworkgl.opengl.renderable.Renderable;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class UniformVariables {

    private AmbientLight ambientLight;
    private GLLight glLight;
    private List<GLLight> glLights;
    private MaterialLight defaultMaterialLight;

    public AmbientLight getAmbientLight() {
        return ambientLight;
    }

    public void setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
    }

    public GLLight getGlLight() {
        return glLight;
    }

    public void setGlLight(GLLight glLight) {
        this.glLight = glLight;
    }

    public List<GLLight> getGlLights() {
        return glLights;
    }

    public void setGlLights(List<GLLight> glLights) {
        this.glLights = glLights;
    }

    public MaterialLight getDefaultMaterialLight() {
        return defaultMaterialLight;
    }

    public void setDefaultMaterialLight(MaterialLight defaultMaterialLight) {
        this.defaultMaterialLight = defaultMaterialLight;
    }

    public MaterialLight getMaterialLight(String materialLightName) {
        int uniqueID = Renderable.materialLightPool.getUniqueID(materialLightName);
        return Renderable.materialLightPool.get(uniqueID);
    }

}
