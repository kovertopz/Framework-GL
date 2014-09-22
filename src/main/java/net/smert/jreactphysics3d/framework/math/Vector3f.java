package net.smert.jreactphysics3d.framework.math;

import java.nio.FloatBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Vector3f {

    private final static Logger log = LoggerFactory.getLogger(Vector3f.class);
    public final static Vector3f WORLD_X_AXIS = new Vector3f(1.0f, 0.0f, 0.0f);
    public final static Vector3f WORLD_Y_AXIS = new Vector3f(0.0f, 1.0f, 0.0f);
    public final static Vector3f WORLD_Z_AXIS = new Vector3f(0.0f, 0.0f, 1.0f);

    float x;
    float y;
    float z;

    // Constructors
    public Vector3f() {
        x = 0.0f;
        y = 0.0f;
        z = 0.0f;
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f(Vector3f v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    // Scalar results
    public float dot(Vector3f v) {
        return x * v.x + y * v.y + z * v.z;
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

    public float magnitude() {
        return MathHelper.Sqrt(x * x + y * y + z * z);
    }

    public float magnitudeSquared() {
        return x * x + y * y + z * z;
    }

    // Vector results
    public Vector3f add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vector3f add(Vector3f v) {
        x += v.x;
        y += v.y;
        z += v.z;
        return this;
    }

    public Vector3f addScaledVector(Vector3f v, float scale) {
        x += v.x * scale;
        y += v.y * scale;
        z += v.z * scale;
        return this;
    }

    public Vector3f addX(float x) {
        this.x += x;
        return this;
    }

    public Vector3f addY(float y) {
        this.y += y;
        return this;
    }

    public Vector3f addZ(float z) {
        this.z += z;
        return this;
    }

    public Vector3f cross(Vector3f v) {
        set(
                y * v.z - z * v.y,
                z * v.x - x * v.z,
                x * v.y - y * v.x);
        return this;
    }

    public Vector3f invert() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    public Vector3f multiply(float value) {
        x *= value;
        y *= value;
        z *= value;
        return this;
    }

    public Vector3f normalize() {
        float mag = magnitude();

        if (mag < MathHelper.ZERO_EPSILON) {
            log.warn("Divide By Zero. Magnitude: {} x: {} y: {} z: {}", mag, x, y, z);

            x = 0.0f;
            y = 0.0f;
            z = -1.0f;

            return this;
        }

        return multiply(1.0f / mag);
    }

    public Vector3f set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3f set(Vector3f v) {
        assert (this != v);
        x = v.x;
        y = v.y;
        z = v.z;
        return this;
    }

    public Vector3f setInvert(Vector3f v) {
        x = -v.x;
        y = -v.y;
        z = -v.z;
        return this;
    }

    public Vector3f setX(float x) {
        this.x = x;
        return this;
    }

    public Vector3f setY(float y) {
        this.y = y;
        return this;
    }

    public Vector3f setZ(float z) {
        this.z = z;
        return this;
    }

    public Vector3f subtract(Vector3f v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        return this;
    }

    public Vector3f toFloatBuffer(FloatBuffer fb) {
        fb.put(x);
        fb.put(y);
        fb.put(z);
        return this;
    }

    @Override
    public String toString() {
        return "Vector3f: {x: " + x + " y: " + y + " z: " + z + "}";
    }

}
