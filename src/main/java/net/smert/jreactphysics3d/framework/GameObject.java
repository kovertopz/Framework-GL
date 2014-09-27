package net.smert.jreactphysics3d.framework;

import net.smert.jreactphysics3d.framework.math.Transform4f;
import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.mesh.Material;
import net.smert.jreactphysics3d.framework.opengl.renderable.gl1.Renderable;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class GameObject {

    private Mesh mesh;
    private Material meshMaterial;
    private Renderable renderable;
    private Object rigidBody;
    private Object collisionBodyShape;
    private final Transform4f worldTransform;

    public GameObject() {
        worldTransform = new Transform4f();
    }

    public void destroy() {
    }

    public void render() {
    }

    public void update() {
    }

}
