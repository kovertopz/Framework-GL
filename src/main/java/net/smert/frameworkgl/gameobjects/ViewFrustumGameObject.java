package net.smert.frameworkgl.gameobjects;

import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.gl1.DynamicVertexBufferObjectRenderable;
import net.smert.frameworkgl.utils.Color;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class ViewFrustumGameObject extends GameObject {

    private final Color color0;
    private final Color color1;
    private final Color color2;
    private final Color color3;
    private DynamicVertexBufferObjectRenderable viewFrustumRenderable;

    public ViewFrustumGameObject() {
        color0 = new Color();
        color1 = new Color();
        color2 = new Color();
        color3 = new Color();
    }

    public Color getColor0() {
        return color0;
    }

    public Color getColor1() {
        return color1;
    }

    public Color getColor2() {
        return color2;
    }

    public Color getColor3() {
        return color3;
    }

    public void init(float aspectRatio, float fieldOfView, float zNear, float zFar) {

        // Create mesh and renderable
        Mesh mesh = GL.meshFactory.createMesh();
        GL.dynamicMeshBuilder.
                setColor(0, color0).
                setColor(1, color1).
                setColor(2, color2).
                setColor(3, color3).
                setCustomData(0, aspectRatio).
                setCustomData(1, fieldOfView).
                setCustomData(2, zNear).
                setCustomData(3, zFar).
                build("view_frustum").
                createMesh(false, mesh);
        viewFrustumRenderable = Fw.graphics.createDynamicVertexBufferObjectRenderable();
        viewFrustumRenderable.create(mesh);

        // Attach to game object
        setMesh(mesh);
        setRenderable(viewFrustumRenderable);
    }

    public void update(float aspectRatio, float fieldOfView, float zNear, float zFar) {

        // Update mesh and renderable
        GL.dynamicMeshBuilder.
                setColor(0, color0).
                setColor(1, color1).
                setColor(2, color2).
                setColor(3, color3).
                setCustomData(0, aspectRatio).
                setCustomData(1, fieldOfView).
                setCustomData(2, zNear).
                setCustomData(3, zFar).
                build("view_frustum").
                createMesh(false, getMesh());
        viewFrustumRenderable.update(getMesh());
    }

}
