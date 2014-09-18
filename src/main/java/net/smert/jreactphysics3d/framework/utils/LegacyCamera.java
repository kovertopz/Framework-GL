package net.smert.jreactphysics3d.framework.utils;

import net.smert.jreactphysics3d.framework.Fw;
import net.smert.jreactphysics3d.framework.math.Vector3f;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class LegacyCamera {

    private final Vector3f rotation;
    private final Vector3f position;

    public LegacyCamera() {
        rotation = new Vector3f();
        position = new Vector3f();
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
    }

    public void setPositionX(float x) {
        position.setX(x);
    }

    public void setPositionY(float y) {
        position.setY(y);
    }

    public void setPositionZ(float z) {
        position.setZ(z);
    }

    public void setRotation(float x, float y, float z) {
        rotation.set(x, y, z);
    }

    public void setRotationX(float x) {
        rotation.setX(x);
    }

    public void setRotationY(float y) {
        rotation.setY(y);
    }

    public void setRotationZ(float z) {
        rotation.setZ(z);
    }

    public void updateOpenGL() {
        Fw.gl.rotate(rotation.getX(), 1.0f, 0, 0);
        Fw.gl.rotate(360.0f - rotation.getY(), 0, 1.0f, 0);
        Fw.gl.rotate(rotation.getZ(), 0, 0, 1.0f);
        Fw.gl.translate(-position.getX(), position.getY(), -position.getZ());
    }

}
