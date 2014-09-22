package net.smert.jreactphysics3d.framework.opengl.renderable;

import net.smert.jreactphysics3d.framework.opengl.renderable.gl1.Renderable;
import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public abstract class AbstractRenderable implements Renderable {

    protected Mesh mesh;

    public AbstractRenderable(Mesh mesh) {
        this.mesh = mesh;
    }

}
