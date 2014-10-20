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
package net.smert.frameworkgl.opengl.renderable.vbo;

import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderable.shared.AbstractDrawCall;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VBODrawArrays extends AbstractDrawCall {

    private int[] firstElements;

    public int[] getFirstElements() {
        return firstElements;
    }

    public void setFirstElements(int[] firstElements) {
        this.firstElements = firstElements;
    }

    @Override
    public void render() {
        for (int i = 0; i < primitiveModes.length; i++) {
            Renderable.renderCallBindState.bind(uniqueShaderIDs[i], textureTypeMappings[i]);
            GL.vboHelper.drawArrays(primitiveModes[i], firstElements[i], elementCounts[i]);
        }
    }

}
