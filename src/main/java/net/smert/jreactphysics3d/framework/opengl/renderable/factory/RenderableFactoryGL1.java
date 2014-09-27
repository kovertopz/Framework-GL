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
package net.smert.jreactphysics3d.framework.opengl.renderable.factory;

import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.renderable.AbstractRenderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.gl1.VertexArrayRenderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.gl1.VertexBufferObjectRenderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.gl1.VertexBufferObjectRenderableInterleaved;
import net.smert.jreactphysics3d.framework.opengl.renderable.shared.AbstractDrawCall;
import net.smert.jreactphysics3d.framework.opengl.renderable.va.VABindState;
import net.smert.jreactphysics3d.framework.opengl.renderable.va.VABuilder;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.VBOBindState;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.VBOBuilder;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class RenderableFactoryGL1 {

    private static boolean initialized;
    private static boolean vaInitialized;
    private static boolean vboInitialized;
    private static VABindState vaBindState;
    private static VABuilder vaBuilder;
    private static VBOBindState vboBindState;
    private static VBOBuilder vboBuilder;
    private static ClassProvider classProvider;
    private static Configuration renderableConfig;

    private void initializeClassProvider() {
        if (initialized == false) {
            if (classProvider == null) {
                classProvider = new ClassProvider();
            }
            if (renderableConfig == null) {
                renderableConfig = new Configuration();
            }

            // Make configuration immutable
            renderableConfig.makeImmutable();

            // Statically initialize all classes
            AbstractDrawCall.SetRenderableConfiguration(renderableConfig);

            initialized = true;
        }
    }

    private void initializeVARenderables() {

        // Initialize only once
        if (vaInitialized == false) {

            // Create classes
            if (vaBindState == null) {
                vaBindState = new VABindState(renderableConfig);
            }
            if (vaBuilder == null) {
                vaBuilder = new VABuilder();
            }

            // Reset bind state
            vaBindState.reset();

            // Statically initialize all classes
            VertexArrayRenderable.SetRenderableConfiguration(renderableConfig);
            VertexArrayRenderable.SetVaBindState(vaBindState);
            VertexArrayRenderable.SetVaBuilder(vaBuilder);

            vaInitialized = true;
        }
    }

    private void initializeVBORenderables() {

        // Initialize only once
        if (vboInitialized == false) {

            // Create classes
            if (vboBindState == null) {
                vboBindState = new VBOBindState(renderableConfig);
            }
            if (vboBuilder == null) {
                vboBuilder = new VBOBuilder();
            }

            // Reset bind state
            vboBindState.reset();

            // Statically initialize all classes
            VertexBufferObjectRenderable.SetRenderableConfiguration(renderableConfig);
            VertexBufferObjectRenderable.SetVboBindState(vboBindState);
            VertexBufferObjectRenderable.SetVboBuilder(vboBuilder);
            VertexBufferObjectRenderableInterleaved.SetRenderableConfiguration(renderableConfig);
            VertexBufferObjectRenderableInterleaved.SetVboBindState(vboBindState);
            VertexBufferObjectRenderableInterleaved.SetVboBuilder(vboBuilder);

            vboInitialized = true;
        }
    }

    public AbstractRenderable createDisplayList(Mesh mesh) {
        initializeClassProvider();
        return classProvider.createDisplayList(mesh);
    }

    public AbstractRenderable createImmediateMode(Mesh mesh) {
        initializeClassProvider();
        return classProvider.createImmediateMode(mesh);
    }

    public AbstractRenderable createVertexArray(Mesh mesh) {
        initializeClassProvider();
        initializeVARenderables();
        return classProvider.createVertexArray(mesh);
    }

    public AbstractRenderable createVertexBufferObject(Mesh mesh) {
        initializeClassProvider();
        initializeVBORenderables();
        return classProvider.createVertexBufferObject(mesh);
    }

    public AbstractRenderable createVertexBufferObjectInterleaved(Mesh mesh) {
        initializeClassProvider();
        initializeVBORenderables();
        return classProvider.createVertexBufferObjectInterleaved(mesh);
    }

    public static void Destroy() {
        initialized = false;
        vaInitialized = false;
        vaBindState = null;
        vaBuilder = null;
        vboInitialized = false;
        vboBindState = null;
        vboBuilder = null;
        classProvider = null;
        renderableConfig = null;
    }

    public static void SetClassProvider(ClassProvider classProvider) {
        if (initialized) {
            throw new RuntimeException("The class provider can only be set before initialization");
        }
        RenderableFactoryGL1.classProvider = classProvider;
    }

    public static void SetRenderableConfiguration(Configuration renderableConfig) {
        if (initialized) {
            throw new RuntimeException("The renderable configuration can only be set before initialization");
        }
        RenderableFactoryGL1.renderableConfig = renderableConfig;
        if (renderableConfig.isImmutable() == false) {
            throw new RuntimeException("Renderable configuration must be made immutable");
        }
    }

    public static void SetVaBindState(VABindState vaBindState) {
        if (vaInitialized) {
            throw new RuntimeException("The bind state can only be set before va initialization");
        }
        RenderableFactoryGL1.vaBindState = vaBindState;
    }

    public static void SetVaBuilder(VABuilder vaBuilder) {
        if (vaInitialized) {
            throw new RuntimeException("The builder can only be set before va initialization");
        }
        RenderableFactoryGL1.vaBuilder = vaBuilder;
    }

    public static void SetVboBindState(VBOBindState vboBindState) {
        if (vboInitialized) {
            throw new RuntimeException("The bind state can only be set before vbo initialization");
        }
        RenderableFactoryGL1.vboBindState = vboBindState;
    }

    public static void SetVboBuilder(VBOBuilder vboBuilder) {
        if (vboInitialized) {
            throw new RuntimeException("The builder can only be set before vbo initialization");
        }
        RenderableFactoryGL1.vboBuilder = vboBuilder;
    }

}
