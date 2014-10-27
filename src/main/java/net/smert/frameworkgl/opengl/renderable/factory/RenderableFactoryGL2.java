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
import net.smert.frameworkgl.opengl.renderable.gl2.DynamicVertexBufferObjectInterleavedGL2Renderable;
import net.smert.frameworkgl.opengl.renderable.gl2.DynamicVertexBufferObjectNonInterleavedGL2Renderable;
import net.smert.frameworkgl.opengl.renderable.gl2.VertexArrayGL2Renderable;
import net.smert.frameworkgl.opengl.renderable.gl2.VertexBufferObjectInterleavedGL2Renderable;
import net.smert.frameworkgl.opengl.renderable.gl2.VertexBufferObjectNonInterleavedGL2Renderable;
import org.picocontainer.MutablePicoContainer;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class RenderableFactoryGL2 implements RenderableFactory {

    private final MutablePicoContainer container;

    public RenderableFactoryGL2(MutablePicoContainer renderableFactoryGL2Container) {
        container = renderableFactoryGL2Container;
    }

    @Override
    public VertexArrayGL2Renderable createArrayRenderable() {
        return container.getComponent(VertexArrayGL2Renderable.class);
    }

    @Override
    public AbstractRenderable createDisplayListRenderable() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public DynamicVertexBufferObjectInterleavedGL2Renderable createDynamicInterleavedRenderable() {
        return container.getComponent(DynamicVertexBufferObjectInterleavedGL2Renderable.class);
    }

    @Override
    public DynamicVertexBufferObjectNonInterleavedGL2Renderable createDynamicNonInterleavedRenderable() {
        return container.getComponent(DynamicVertexBufferObjectNonInterleavedGL2Renderable.class);
    }

    @Override
    public AbstractRenderable createImmediateModeRenderable() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public VertexBufferObjectInterleavedGL2Renderable createInterleavedRenderable() {
        return container.getComponent(VertexBufferObjectInterleavedGL2Renderable.class);
    }

    @Override
    public VertexBufferObjectNonInterleavedGL2Renderable createNonInterleavedRenderable() {
        return container.getComponent(VertexBufferObjectNonInterleavedGL2Renderable.class);
    }

}
