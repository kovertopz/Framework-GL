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

import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;
import net.smert.frameworkgl.opengl.renderable.gl3.DynamicVertexArrayObjectInterleavedGL3Renderable;
import net.smert.frameworkgl.opengl.renderable.gl3.DynamicVertexArrayObjectNonInterleavedGL3Renderable;
import net.smert.frameworkgl.opengl.renderable.gl3.VertexArrayObjectInterleavedGL3Renderable;
import net.smert.frameworkgl.opengl.renderable.gl3.VertexArrayObjectNonInterleavedGL3Renderable;
import org.picocontainer.MutablePicoContainer;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class RenderableFactoryGL3 implements RenderableFactory {

    private final MutablePicoContainer container;

    public RenderableFactoryGL3(MutablePicoContainer renderableFactoryGL3Container) {
        container = renderableFactoryGL3Container;
    }

    @Override
    public AbstractRenderable createArrayRenderable() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public AbstractRenderable createDisplayListRenderable() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public DynamicVertexArrayObjectInterleavedGL3Renderable createDynamicInterleavedRenderable() {
        return container.getComponent(DynamicVertexArrayObjectInterleavedGL3Renderable.class);
    }

    @Override
    public DynamicVertexArrayObjectNonInterleavedGL3Renderable createDynamicNonInterleavedRenderable() {
        return container.getComponent(DynamicVertexArrayObjectNonInterleavedGL3Renderable.class);
    }

    @Override
    public AbstractRenderable createImmediateModeRenderable() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public VertexArrayObjectInterleavedGL3Renderable createInterleavedRenderable() {
        return container.getComponent(VertexArrayObjectInterleavedGL3Renderable.class);
    }

    @Override
    public VertexArrayObjectNonInterleavedGL3Renderable createNonInterleavedRenderable() {
        return container.getComponent(VertexArrayObjectNonInterleavedGL3Renderable.class);
    }

}
