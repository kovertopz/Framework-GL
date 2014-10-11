/**
 * Copyright 2012 Jason Sorensen (sorensenj@smert.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package net.smert.frameworkgl.math;

import java.nio.FloatBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Vector2f {

    private static Logger log = LoggerFactory.getLogger(Vector2f.class);
    public static Vector2f WORLD_X_AXIS = new Vector2f(1f, 0f);
    public static Vector2f WORLD_Y_AXIS = new Vector2f(0f, 1f);

    float x;
    float y;

    // Constructors
    public Vector2f() {
        zero();
    }

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(Vector2f vector) {
        x = vector.x;
        y = vector.y;
    }

    // Boolean Results
    public boolean trim(float length) {
        float magnitude = magnitude();
        if (magnitude <= length) {
            return false;
        }
        multiply(1f / magnitude);
        multiply(length);
        return true;
    }

    public boolean trimSquared(float length) {
        float magnitudeSquared = magnitudeSquared();
        if (magnitudeSquared <= length * length) {
            return false;
        }
        multiply(1f / MathHelper.Sqrt(magnitudeSquared));
        multiply(length);
        return true;
    }

    // Conversion Operations
    public void toFloatBuffer(FloatBuffer fbOut) {
        fbOut.put(x);
        fbOut.put(y);
    }

    // Scalar Results
    public float distance(Vector2f vector) {
        float d0 = x - vector.x;
        float d1 = y - vector.y;
        return MathHelper.Sqrt(d0 * d0 + d1 * d1);
    }

    public float distanceSquared(Vector2f vector) {
        float d0 = x - vector.x;
        float d1 = y - vector.y;
        return d0 * d0 + d1 * d1;
    }

    public float dot(Vector2f vector) {
        return x * vector.x + y * vector.y;
    }

    public float getElement(int index) {
        switch (index) {
            case 0:
                return x;
            case 1:
                return y;
        }
        throw new IllegalArgumentException("Invalid index: " + index);
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

    public int maxAxis() {
        return (x < y) ? 1 : 0;
    }

    public int minAxis() {
        return (x < y) ? 0 : 1;
    }

    // Vector Results
    public Vector2f abs() {
        x = Math.abs(x);
        y = Math.abs(y);
        return this;
    }

    public Vector2f add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector2f add(Vector2f vector) {
        x += vector.x;
        y += vector.y;
        return this;
    }

    public Vector2f addScaled(Vector2f vector, float scale) {
        x += vector.x * scale;
        y += vector.y * scale;
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

            x = 1f;
            y = 0f;

            return this;
        }

        return multiply(1f / mag);
    }

    public Vector2f set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2f set(Vector2f vector) {
        assert (this != vector);
        x = vector.x;
        y = vector.y;
        return this;
    }

    public void setElement(int index, float value) {
        switch (index) {
            case 0:
                x = value;
                return;
            case 1:
                y = value;
                return;
        }
        throw new IllegalArgumentException("Invalid index: " + index);
    }

    public Vector2f setInvert(Vector2f vector) {
        x = -vector.x;
        y = -vector.y;
        return this;
    }

    public Vector2f setInterpolate(Vector2f vector0, Vector2f vector1, float f) {
        float s = 1f - f;
        x = vector0.x * s + vector1.x * f;
        y = vector0.y * s + vector1.y * f;
        return this;
    }

    public Vector2f setMax(float x, float y, float z) {
        if (x > this.x) {
            this.x = x;
        }
        if (y > this.y) {
            this.y = y;
        }
        return this;
    }

    public Vector2f setMax(Vector2f vector) {
        if (vector.x > x) {
            x = vector.x;
        }
        if (vector.y > y) {
            y = vector.y;
        }
        return this;
    }

    public Vector2f setMin(float x, float y, float z) {
        if (x < this.x) {
            this.x = x;
        }
        if (y < this.y) {
            this.y = y;
        }
        return this;
    }

    public Vector2f setMin(Vector2f vector) {
        if (vector.x < x) {
            x = vector.x;
        }
        if (vector.y < y) {
            y = vector.y;
        }
        return this;
    }

    public Vector2f setNormal(float x, float y, float z) {
        this.x = x;
        this.y = y;
        return normalize();
    }

    public Vector2f setX(float x) {
        this.x = x;
        return this;
    }

    public Vector2f setY(float y) {
        this.y = y;
        return this;
    }

    public Vector2f subtract(Vector2f vector) {
        x -= vector.x;
        y -= vector.y;
        return this;
    }

    public final Vector2f zero() {
        x = 0f;
        y = 0f;
        return this;
    }

    @Override
    public String toString() {
        return "(x: " + x + " y: " + y + ")";
    }

}
