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

import net.smert.frameworkgl.opengl.renderable.gl1.DisplayListRenderable;
import net.smert.frameworkgl.opengl.renderable.gl1.DynamicVertexBufferObjectRenderable;
import net.smert.frameworkgl.opengl.renderable.gl1.DynamicVertexBufferObjectRenderableInterleaved;
import net.smert.frameworkgl.opengl.renderable.gl1.ImmediateModeRenderable;
import net.smert.frameworkgl.opengl.renderable.gl1.VertexArrayRenderable;
import net.smert.frameworkgl.opengl.renderable.gl1.VertexBufferObjectRenderable;
import net.smert.frameworkgl.opengl.renderable.gl1.VertexBufferObjectRenderableInterleaved;
import org.picocontainer.MutablePicoContainer;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class RenderableFactoryGL1 {

    private final MutablePicoContainer container;

    public RenderableFactoryGL1(MutablePicoContainer renderableFactoryGL1Container) {
        container = renderableFactoryGL1Container;
    }

    public DisplayListRenderable createDisplayList() {
        return container.getComponent(DisplayListRenderable.class);
    }

    public DynamicVertexBufferObjectRenderable createDynamicVertexBufferObject() {
        return container.getComponent(DynamicVertexBufferObjectRenderable.class);
    }

    public DynamicVertexBufferObjectRenderableInterleaved createDynamicVertexBufferObjectInterleaved() {
        return container.getComponent(DynamicVertexBufferObjectRenderableInterleaved.class);
    }

    public ImmediateModeRenderable createImmediateMode() {
        return container.getComponent(ImmediateModeRenderable.class);
    }

    public VertexArrayRenderable createVertexArray() {
        return container.getComponent(VertexArrayRenderable.class);
    }

    public VertexBufferObjectRenderable createVertexBufferObject() {
        return container.getComponent(VertexBufferObjectRenderable.class);
    }

    public VertexBufferObjectRenderableInterleaved createVertexBufferObjectInterleaved() {
        return container.getComponent(VertexBufferObjectRenderableInterleaved.class);
    }

}
