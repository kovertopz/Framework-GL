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

import net.smert.frameworkgl.opengl.mesh.Mesh;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VertexArrayRenderable extends ArrayRenderable {

    protected VertexArrayBindStrategy bind;
    protected final VertexArrayCreateStrategy create;
    protected final VertexArrayData data;
    protected final VertexArrayRenderStrategy render;

    public VertexArrayRenderable(
            VertexArrayCreateStrategy create,
            VertexArrayRenderStrategy render) {
        this.create = create;
        this.data = new VertexArrayData();
        this.render = render;
    }

    public VertexArrayBindStrategy getBind() {
        return bind;
    }

    public void setBind(VertexArrayBindStrategy bind) {
        this.bind = bind;
    }

    @Override
    public void create(Mesh mesh) {
        create.create(mesh, data);
    }

    @Override
    public void destroy() {
        data.destroy();
    }

    @Override
    public void render() {
        render.render(bind, data);
    }

}
