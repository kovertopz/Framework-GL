package net.smert.jreactphysics3d.framework.opengl.renderable.factory;

import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.renderable.AbstractRenderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.gl1.DisplayListRenderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.gl1.ImmediateModeRenderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.gl1.VertexBufferObjectRenderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.gl1.VertexBufferObjectRenderableInterleaved;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class ClassProvider {

    public AbstractRenderable createDisplayList(Mesh mesh) {
        return new DisplayListRenderable(mesh);
    }

    public AbstractRenderable createImmediateMode(Mesh mesh) {
        return new ImmediateModeRenderable(mesh);
    }

    public AbstractRenderable createVertexBufferObject(Mesh mesh) {
        return new VertexBufferObjectRenderable(mesh);
    }

    public AbstractRenderable createVertexBufferObjectInterleaved(Mesh mesh) {
        return new VertexBufferObjectRenderableInterleaved(mesh);
    }

}
