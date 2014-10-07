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
package net.smert.frameworkgl.opengl.factory;

import net.smert.frameworkgl.opengl.AmbientLight;
import net.smert.frameworkgl.opengl.DisplayList;
import net.smert.frameworkgl.opengl.FrameBufferObject;
import net.smert.frameworkgl.opengl.Light;
import net.smert.frameworkgl.opengl.MaterialLight;
import net.smert.frameworkgl.opengl.RenderBufferObject;
import net.smert.frameworkgl.opengl.Shader;
import net.smert.frameworkgl.opengl.Shadow;
import net.smert.frameworkgl.opengl.Texture;
import net.smert.frameworkgl.opengl.VertexArray;
import net.smert.frameworkgl.opengl.VertexArrayObject;
import net.smert.frameworkgl.opengl.VertexBufferObject;
import net.smert.frameworkgl.opengl.VertexBufferObjectInterleaved;
import org.picocontainer.MutablePicoContainer;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class GLFactory {

    private final MutablePicoContainer container;

    public GLFactory(MutablePicoContainer glFactoryContainer) {
        container = glFactoryContainer;
    }

    public AmbientLight createAmbientLight() {
        return container.getComponent(AmbientLight.class);
    }

    public DisplayList createDisplayList() {
        return container.getComponent(DisplayList.class);
    }

    public FrameBufferObject createFrameBufferObject() {
        return container.getComponent(FrameBufferObject.class);
    }

    public Light createLight() {
        return container.getComponent(Light.class);
    }

    public MaterialLight createMaterialLight() {
        return container.getComponent(MaterialLight.class);
    }

    public RenderBufferObject createRenderBufferObject() {
        return container.getComponent(RenderBufferObject.class);
    }

    public Shader createShader() {
        return container.getComponent(Shader.class);
    }

    public Shadow createShadow() {
        return container.getComponent(Shadow.class);
    }

    public Texture createTexture() {
        return container.getComponent(Texture.class);
    }

    public VertexArray createVertexArray() {
        return container.getComponent(VertexArray.class);
    }

    public VertexArrayObject createVertexArrayObject() {
        return container.getComponent(VertexArrayObject.class);
    }

    public VertexBufferObject createVertexBufferObject() {
        return container.getComponent(VertexBufferObject.class);
    }

    public VertexBufferObjectInterleaved createVertexBufferObjectInterleaved() {
        return container.getComponent(VertexBufferObjectInterleaved.class);
    }

}
