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
package net.smert.frameworkgl.opengl.shader.pixellit.single;

import java.io.IOException;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.opengl.Shader;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class BlinnPhongSpecularSpotShader extends AbstractSpecularShader {

    public BlinnPhongSpecularSpotShader(SpecularUniforms uniforms, Shader shader) {
        super(uniforms, shader);
    }

    public static class Factory {

        public static BlinnPhongSpecularSpotShader Create() throws IOException {
            Shader shader = Fw.graphics.buildShader(
                    "pixellit/single/blinn_phong_specular_spot.fsh",
                    "pixellit/single/blinn_phong_specular_spot.vsh",
                    "pixelLitSingleBlinnPhongSpecularSpot");
            return new BlinnPhongSpecularSpotShader(new SpecularUniforms(shader.getProgramID()), shader);
        }

    }

}
