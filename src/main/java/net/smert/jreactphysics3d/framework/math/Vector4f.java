package net.smert.jreactphysics3d.framework.math;

import java.nio.FloatBuffer;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Vector4f {

    float w;
    float x;
    float y;
    float z;

    public Vector4f() {
        w = 0.0f;
        x = 0.0f;
        y = 0.0f;
        z = 0.0f;
    }

    public Vector4f(float x, float y, float z, float w) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getW() {
        return w;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void set(float x, float y, float z, float w) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setW(float w) {
        this.w = w;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public void toFloatBuffer(FloatBuffer fb) {
        fb.put(x);
        fb.put(y);
        fb.put(z);
        fb.put(w);
    }

    @Override
    public String toString() {
        return "X: " + x + " Y: " + y + " Z: " + z;
    }

}
