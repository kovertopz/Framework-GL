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

import net.smert.frameworkgl.opengl.constants.VertexBufferObjectTypes;
import net.smert.frameworkgl.opengl.mesh.Mesh;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VertexBufferObjectInterleavedRenderable extends InterleavedRenderable {

    protected VertexBufferObjectBindStrategy bind;
    protected final VertexBufferObjectInterleavedCreateStrategy create;
    protected final VertexBufferObjectInterleavedData data;
    protected final VertexBufferObjectInterleavedRenderStrategy render;

    public VertexBufferObjectInterleavedRenderable(
            VertexBufferObjectInterleavedCreateStrategy create,
            VertexBufferObjectInterleavedRenderStrategy render) {
        this.create = create;
        this.data = new VertexBufferObjectInterleavedData();
        this.render = render;
        data.bufferUsage = VertexBufferObjectTypes.STATIC_DRAW;
    }

    public VertexBufferObjectBindStrategy getBind() {
        return bind;
    }

    public void setBind(VertexBufferObjectBindStrategy bind) {
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
