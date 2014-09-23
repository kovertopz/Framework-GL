package net.smert.jreactphysics3d.framework.opengl.renderable.factory;

import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.renderable.AbstractRenderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.gl1.VertexBufferObjectRenderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.gl1.VertexBufferObjectRenderableInterleaved;
import net.smert.jreactphysics3d.framework.opengl.renderable.shared.AbstractDrawCall;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.BindState;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.Builder;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class RenderableFactoryGL1 {

    private static boolean initialized;
    private static boolean vboInitialized;
    private static BindState vboBindState;
    private static Builder vboBuilder;
    private static ClassProvider classProvider;
    private static Configuration renderableConfig;

    private void initializeClassProvider() {
        if (initialized == false) {
            if (classProvider == null) {
                classProvider = new ClassProvider();
            }
            initialized = true;
        }
    }

    private void initializeVBORenderables() {

        // Initialize only once
        if (vboInitialized == false) {

            // Create classes
            if (renderableConfig == null) {
                renderableConfig = new Configuration();
            }
            renderableConfig.makeImmutable();
            if (vboBindState == null) {
                vboBindState = new BindState(renderableConfig);
            }
            if (vboBuilder == null) {
                vboBuilder = new Builder();
            }

            // Reset bind state
            vboBindState.reset();

            // Statically initialize all classes
            AbstractDrawCall.SetRenderableConfiguration(renderableConfig);
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
        RenderableFactoryGL1.renderableConfig = renderableConfig;
        if (renderableConfig.isImmutable() == false) {
            throw new RuntimeException("Renderable configuration must be made immutable");
        }
    }

    public static void SetVboBindState(BindState vboBindState) {
        if (vboInitialized) {
            throw new RuntimeException("The bind state can only be set before vbo initialization");
        }
        RenderableFactoryGL1.vboBindState = vboBindState;
    }

    public static void SetVboBuilder(Builder vboBuilder) {
        if (vboInitialized) {
            throw new RuntimeException("The builder can only be set before vbo initialization");
        }
        RenderableFactoryGL1.vboBuilder = vboBuilder;
    }

}
