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
package net.smert.frameworkgl.opengl.renderable.factory;

import net.smert.frameworkgl.opengl.renderable.gl1.DisplayListGL1Renderable;
import net.smert.frameworkgl.opengl.renderable.gl1.DynamicVertexBufferObjectInterleavedGL1Renderable;
import net.smert.frameworkgl.opengl.renderable.gl1.DynamicVertexBufferObjectNonInterleavedGL1Renderable;
import net.smert.frameworkgl.opengl.renderable.gl1.ImmediateModeGL1Renderable;
import net.smert.frameworkgl.opengl.renderable.gl1.VertexArrayGL1Renderable;
import net.smert.frameworkgl.opengl.renderable.gl1.VertexBufferObjectInterleavedGL1Renderable;
import net.smert.frameworkgl.opengl.renderable.gl1.VertexBufferObjectNonInterleavedGL1Renderable;
import org.picocontainer.MutablePicoContainer;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class RenderableFactoryGL1 implements RenderableFactory {

    private final MutablePicoContainer container;

    public RenderableFactoryGL1(MutablePicoContainer renderableFactoryGL1Container) {
        container = renderableFactoryGL1Container;
    }

    @Override
    public VertexArrayGL1Renderable createArrayRenderable() {
        return container.getComponent(VertexArrayGL1Renderable.class);
    }

    @Override
    public DisplayListGL1Renderable createDisplayListRenderable() {
        return container.getComponent(DisplayListGL1Renderable.class);
    }

    @Override
    public DynamicVertexBufferObjectInterleavedGL1Renderable createDynamicInterleavedRenderable() {
        return container.getComponent(DynamicVertexBufferObjectInterleavedGL1Renderable.class);
    }

    @Override
    public DynamicVertexBufferObjectNonInterleavedGL1Renderable createDynamicNonInterleavedRenderable() {
        return container.getComponent(DynamicVertexBufferObjectNonInterleavedGL1Renderable.class);
    }

    @Override
    public ImmediateModeGL1Renderable createImmediateModeRenderable() {
        return container.getComponent(ImmediateModeGL1Renderable.class);
    }

    @Override
    public VertexBufferObjectInterleavedGL1Renderable createInterleavedRenderable() {
        return container.getComponent(VertexBufferObjectInterleavedGL1Renderable.class);
    }

    @Override
    public VertexBufferObjectNonInterleavedGL1Renderable createNonInterleavedRenderable() {
        return container.getComponent(VertexBufferObjectNonInterleavedGL1Renderable.class);
    }

}
