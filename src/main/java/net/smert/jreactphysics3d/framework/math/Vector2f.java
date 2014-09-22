package net.smert.jreactphysics3d.framework.math;

import java.nio.FloatBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Vector2f {

    private final static Logger log = LoggerFactory.getLogger(Vector3f.class);
    public final static Vector2f WORLD_X_AXIS = new Vector2f(1.0f, 0.0f);
    public final static Vector2f WORLD_Y_AXIS = new Vector2f(0.0f, 1.0f);

    float x;
    float y;

    // Constructors
    public Vector2f() {
        x = 0.0f;
        y = 0.0f;
    }

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(Vector2f v) {
        x = v.x;
        y = v.y;
    }

    // Scalar results
    public float dot(Vector2f v) {
        return x * v.x + y * v.y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float magnitude() {
        return MathHelper.Sqrt(x * x + y * y);
    }

    public float magnitudeSquared() {
        return x * x + y * y;
    }

    // Vector results
    public Vector2f add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector2f add(Vector2f v) {
        x += v.x;
        y += v.y;
        return this;
    }

    public Vector2f addScaledVector(Vector2f v, float scale) {
        x += v.x * scale;
        y += v.y * scale;
        return this;
    }

    public Vector2f addX(float x) {
        this.x += x;
        return this;
    }

    public Vector2f addY(float y) {
        this.y += y;
        return this;
    }

    public Vector2f invert() {
        x = -x;
        y = -y;
        return this;
    }

    public Vector2f multiply(float value) {
        x *= value;
        y *= value;
        return this;
    }

    public Vector2f normalize() {
        float mag = magnitude();

        if (mag < MathHelper.ZERO_EPSILON) {
            log.warn("Divide By Zero. Magnitude: {} x: {} y: {}", mag, x, y);

            x = 1.0f;
            y = 0.0f;

            return this;
        }

        return multiply(1.0f / mag);
    }

    public Vector2f set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2f set(Vector2f v) {
        assert (this != v);
        x = v.x;
        y = v.y;
        return this;
    }

    public Vector2f setInvert(Vector2f v) {
        x = -v.x;
        y = -v.y;
        return this;
    }

    public Vector2f setX(float x) {
        this.x = x;
        return this;
    }

    public Vector2f setY(float y) {
        this.y = y;
        return this;
    }

    public Vector2f subtract(Vector2f v) {
        x -= v.x;
        y -= v.y;
        return this;
    }

    public Vector2f toFloatBuffer(FloatBuffer fb) {
        fb.put(x);
        fb.put(y);
        return this;
    }

    @Override
    public String toString() {
        return "Vector2f: {x: " + x + " y: " + y + "}";
    }

}
