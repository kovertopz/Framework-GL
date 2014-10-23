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

import java.io.IOException;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.opengl.Shader;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class DiffuseSpotTwoConeShader extends AbstractDiffuseShader {

    public DiffuseSpotTwoConeShader(DiffuseUniforms uniforms, Shader shader) {
        super(uniforms, shader);
    }

    public static class Factory {

        public static DiffuseSpotTwoConeShader Create() throws IOException {
            Shader shader = Fw.graphics.buildShader(
                    "pixellit/multi/diffuse_spot_two_cone.fsh",
                    "pixellit/multi/diffuse_spot_two_cone.vsh",
                    "pixelLitMultiDiffuseSpot");
            return new DiffuseSpotTwoConeShader(new DiffuseUniforms(shader.getProgramID()), shader);
        }

    }

}
