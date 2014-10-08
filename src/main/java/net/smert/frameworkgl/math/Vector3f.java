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
public class Vector3f {

    private static Logger log = LoggerFactory.getLogger(Vector3f.class);
    public static Vector3f WORLD_X_AXIS = new Vector3f(1f, 0f, 0f);
    public static Vector3f WORLD_Y_AXIS = new Vector3f(0f, 1f, 0f);
    public static Vector3f WORLD_Z_AXIS = new Vector3f(0f, 0f, 1f);

    float x;
    float y;
    float z;

    // Constructors
    public Vector3f() {
        zero();
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f(Vector3f vector) {
        x = vector.x;
        y = vector.y;
        z = vector.z;
    }

    public Vector3f(Vector4f vector) {
        x = vector.x;
        y = vector.y;
        z = vector.z;
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
        fbOut.put(z);
    }

    // Scalar Results
    public float dot(Vector3f vector) {
        return x * vector.x + y * vector.y + z * vector.z;
    }

    public float getElement(int index) {
        switch (index) {
            case 0:
                return x;
            case 1:
                return y;
            case 2:
                return z;
        }
        throw new IllegalArgumentException("Invalid index: " + index);
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

    public int maxAxis() {
        return (x < y) ? ((y < z) ? 2 : 1) : ((x < z) ? 2 : 0);
    }

    public int minAxis() {
        return (x < y) ? ((x < z) ? 0 : 2) : ((y < z) ? 1 : 2);
    }

    // Vector Results
    public Vector3f add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vector3f add(Vector3f vector) {
        x += vector.x;
        y += vector.y;
        z += vector.z;
        return this;
    }

    public Vector3f addScaled(Vector3f vector, float scale) {
        x += vector.x * scale;
        y += vector.y * scale;
        z += vector.z * scale;
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

    public Vector3f cross(Vector3f vector) {
        set(
                y * vector.z - z * vector.y,
                z * vector.x - x * vector.z,
                x * vector.y - y * vector.x);
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

            x = 0f;
            y = 0f;
            z = -1f;

            return this;
        }

        return multiply(1f / mag);
    }

    public Vector3f set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3f set(Vector3f vector) {
        x = vector.x;
        y = vector.y;
        z = vector.z;
        return this;
    }

    public Vector3f set(Vector4f vector) {
        x = vector.x;
        y = vector.y;
        z = vector.z;
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
            case 2:
                z = value;
                return;
        }
        throw new IllegalArgumentException("Invalid index: " + index);
    }

    public Vector3f setInterpolate(Vector3f vector0, Vector3f vector1, float f) {
        float s = 1f - f;
        x = vector0.x * s + vector1.x * f;
        y = vector0.y * s + vector1.y * f;
        z = vector0.z * s + vector1.z * f;
        return this;
    }

    public Vector3f setInvert(Vector3f vector) {
        x = -vector.x;
        y = -vector.y;
        z = -vector.z;
        return this;
    }

    public Vector3f setMax(float x, float y, float z) {
        if (x > this.x) {
            this.x = x;
        }
        if (y > this.y) {
            this.y = y;
        }
        if (z > this.z) {
            this.z = z;
        }
        return this;
    }

    public Vector3f setMax(Vector3f vector) {
        if (vector.x > x) {
            x = vector.x;
        }
        if (vector.y > y) {
            y = vector.y;
        }
        if (vector.z > z) {
            z = vector.z;
        }
        return this;
    }

    public Vector3f setMin(float x, float y, float z) {
        if (x < this.x) {
            this.x = x;
        }
        if (y < this.y) {
            this.y = y;
        }
        if (z < this.z) {
            this.z = z;
        }
        return this;
    }

    public Vector3f setMin(Vector3f vector) {
        if (vector.x < x) {
            x = vector.x;
        }
        if (vector.y < y) {
            y = vector.y;
        }
        if (vector.z < z) {
            z = vector.z;
        }
        return this;
    }

    public Vector3f setNormal(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return normalize();
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

    public Vector3f subtract(Vector3f vector) {
        x -= vector.x;
        y -= vector.y;
        z -= vector.z;
        return this;
    }

    public final Vector3f zero() {
        x = 0f;
        y = 0f;
        z = 0f;
        return this;
    }

    @Override
    public String toString() {
        return "(x: " + x + " y: " + y + " z: " + z + ")";
    }

}
