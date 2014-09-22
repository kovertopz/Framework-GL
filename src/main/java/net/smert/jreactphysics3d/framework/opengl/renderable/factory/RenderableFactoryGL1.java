package net.smert.jreactphysics3d.framework.opengl.renderable.factory;

import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.renderable.AbstractRenderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.gl1.VertexBufferObjectRenderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.gl1.VertexBufferObjectRenderableInterleaved;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.BindState;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.Builder;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.Configuration;

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
    private static Configuration vboConfiguration;

    public RenderableFactoryGL1() {
        vboInitialized = false;
    }

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
            if (vboConfiguration == null) {
                vboConfiguration = new Configuration();
            }
            if (vboBindState == null) {
                vboBindState = new BindState(vboConfiguration);
            }
            if (vboBuilder == null) {
                vboBuilder = new Builder();
            }

            // Statically initialize all classes
            VertexBufferObjectRenderable.SetVboBindState(vboBindState);
            VertexBufferObjectRenderable.SetVboBuilder(vboBuilder);
            VertexBufferObjectRenderableInterleaved.SetVboBindState(vboBindState);
            VertexBufferObjectRenderableInterleaved.SetVboBuilder(vboBuilder);
            VertexBufferObjectRenderableInterleaved.SetVboConfiguration(vboConfiguration);

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

    public static void SetClassProvider(ClassProvider classProvider) {
        if (initialized) {
            throw new RuntimeException("The class provider can only be set before initialization");
        }
        RenderableFactoryGL1.classProvider = classProvider;
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

    public static void SetVboConfiguration(Configuration vboConfiguration) {
        if (vboInitialized) {
            throw new RuntimeException("The configuration can only be set before vbo initialization");
        }
        RenderableFactoryGL1.vboConfiguration = vboConfiguration;
    }

}
