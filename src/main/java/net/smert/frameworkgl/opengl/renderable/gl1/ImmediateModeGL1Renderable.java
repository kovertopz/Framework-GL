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
package net.smert.frameworkgl.opengl.renderable.gl1;

import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderable.shared.RenderCall;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class ImmediateModeGL1Renderable extends AbstractRenderable {

    private RenderCall renderCall;

    public ImmediateModeGL1Renderable() {
        renderCall = null;
    }

    @Override
    public void create(Mesh mesh) {

        // Create render call
        renderCall = Renderable.immediateModeRenderCallBuilder.createRenderCall(mesh);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void render() {
        renderCall.render();
    }

}
