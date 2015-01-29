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
import java.util.Objects;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Matrix3f {

    /**
     * Layout of the matrix as follows:
     *
     * Column 1 | Column 2 | Column 3
     *
     * xAxis.x | yAxis.x | zAxis.x
     *
     * xAxis.y | yAxis.y | zAxis.y
     *
     * xAxis.z | yAxis.z | zAxis.z
     */
    final Vector3f xAxis = new Vector3f();
    final Vector3f yAxis = new Vector3f();
    final Vector3f zAxis = new Vector3f();

    // Constructors
    public Matrix3f() {
    }

    public Matrix3f(float xx, float xy, float xz, float yx, float yy, float yz, float zx, float zy, float zz) {
        setColumnMajor(xx, xy, xz, yx, yy, yz, zx, zy, zz);
    }

    public Matrix3f(Matrix3f matrix) {
        set(matrix);
    }

    public Matrix3f(Vector3f xAxis, Vector3f yAxis, Vector3f zAxis) {
        setAxes(xAxis, yAxis, zAxis);
    }

    // Conversion Operations
    public void fromColumnArray(float[] in) {
        xAxis.x = in[0];
        xAxis.y = in[1];
        xAxis.z = in[2];
        yAxis.x = in[3];
        yAxis.y = in[4];
        yAxis.z = in[5];
        zAxis.x = in[6];
        zAxis.y = in[7];
        zAxis.z = in[8];
    }

    public void fromRowArray(float[] in) {
        xAxis.x = in[0];
        yAxis.x = in[1];
        zAxis.x = in[2];
        xAxis.y = in[3];
        yAxis.y = in[4];
        zAxis.y = in[5];
        xAxis.z = in[6];
        yAxis.z = in[7];
        zAxis.z = in[8];
    }

    public void toFloatBuffer(FloatBuffer fbOut) {
        fbOut.put(xAxis.x);
        fbOut.put(xAxis.y);
        fbOut.put(xAxis.z);
        fbOut.put(yAxis.x);
        fbOut.put(yAxis.y);
        fbOut.put(yAxis.z);
        fbOut.put(zAxis.x);
        fbOut.put(zAxis.y);
        fbOut.put(zAxis.z);
    }

    public void toColumnArray(float[] out) {
        out[0] = xAxis.x;
        out[1] = xAxis.y;
        out[2] = xAxis.z;
        out[3] = yAxis.x;
        out[4] = yAxis.y;
        out[5] = yAxis.z;
        out[6] = zAxis.x;
        out[7] = zAxis.y;
        out[8] = zAxis.z;
    }

    public void toRowArray(float[] out) {
        out[0] = xAxis.x;
        out[1] = yAxis.x;
        out[2] = zAxis.x;
        out[3] = xAxis.y;
        out[4] = yAxis.y;
        out[5] = zAxis.y;
        out[6] = xAxis.z;
        out[7] = yAxis.z;
        out[8] = zAxis.z;
    }

    // Matrix Operations
    public Matrix3f absolute() {
        xAxis.x = Math.abs(xAxis.x);
        yAxis.x = Math.abs(yAxis.x);
        zAxis.x = Math.abs(zAxis.x);
        xAxis.y = Math.abs(xAxis.y);
        yAxis.y = Math.abs(yAxis.y);
        zAxis.y = Math.abs(zAxis.y);
        xAxis.z = Math.abs(xAxis.z);
        yAxis.z = Math.abs(yAxis.z);
        zAxis.z = Math.abs(zAxis.z);
        return this;
    }

    public Matrix3f add(Matrix3f matrix) {
        xAxis.add(matrix.xAxis);
        yAxis.add(matrix.yAxis);
        zAxis.add(matrix.zAxis);
        return this;
    }

    public Matrix3f addDiagonal(float value) {
        xAxis.x += value;
        yAxis.y += value;
        zAxis.z += value;
        return this;
    }

    public Matrix3f addDiagonal(float x, float y, float z) {
        xAxis.x += x;
        yAxis.y += y;
        zAxis.z += z;
        return this;
    }

    public Matrix3f addDiagonal(Vector3f vector) {
        xAxis.x += vector.x;
        yAxis.y += vector.y;
        zAxis.z += vector.z;
        return this;
    }

    public Matrix3f fromAxisAngle(Vector3f vector, float degrees) {
        float radians = MathHelper.ToRadians(degrees);
        float c = MathHelper.Cos(radians);
        float s = MathHelper.Sin(radians);
        float t = (1f - c);
        xAxis.set(
                t * (vector.x * vector.x) + c,
                t * (vector.x * vector.y) + (vector.z * s),
                t * (vector.x * vector.z) - (vector.y * s));
        yAxis.set(
                t * (vector.y * vector.x) - (vector.z * s),
                t * (vector.y * vector.y) + c,
                t * (vector.y * vector.z) + (vector.x * s));
        zAxis.set(
                t * (vector.z * vector.x) + (vector.y * s),
                t * (vector.z * vector.y) - (vector.x * s),
                t * (vector.z * vector.z) + c);
        return this;
    }

    public Matrix3f identity() {
        xAxis.set(1f, 0f, 0f);
        yAxis.set(0f, 1f, 0f);
        zAxis.set(0f, 0f, 1f);
        return this;
    }

    public Matrix3f multiply(float value) {
        xAxis.multiply(value);
        yAxis.multiply(value);
        zAxis.multiply(value);
        return this;
    }

    public Matrix3f multiply(Matrix3f matrix) {
        float t1, t2, t3;
        t1 = dotRow(0, matrix.xAxis);
        t2 = dotRow(0, matrix.yAxis);
        t3 = dotRow(0, matrix.zAxis);
        xAxis.x = t1;
        yAxis.x = t2;
        zAxis.x = t3;
        t1 = dotRow(1, matrix.xAxis);
        t2 = dotRow(1, matrix.yAxis);
        t3 = dotRow(1, matrix.zAxis);
        xAxis.y = t1;
        yAxis.y = t2;
        zAxis.y = t3;
        t1 = dotRow(2, matrix.xAxis);
        t2 = dotRow(2, matrix.yAxis);
        t3 = dotRow(2, matrix.zAxis);
        xAxis.z = t1;
        yAxis.z = t2;
        zAxis.z = t3;
        return this;
    }

    public Matrix3f multiplyTranspose(Matrix3f matrix) {
        float t1, t2, t3;
        t1 = dotRow(0, matrix.xAxis.x, matrix.yAxis.x, matrix.zAxis.x);
        t2 = dotRow(0, matrix.xAxis.y, matrix.yAxis.y, matrix.zAxis.y);
        t3 = dotRow(0, matrix.xAxis.z, matrix.yAxis.z, matrix.zAxis.z);
        xAxis.x = t1;
        yAxis.x = t2;
        zAxis.x = t3;
        t1 = dotRow(1, matrix.xAxis.x, matrix.yAxis.x, matrix.zAxis.x);
        t2 = dotRow(1, matrix.xAxis.y, matrix.yAxis.y, matrix.zAxis.y);
        t3 = dotRow(1, matrix.xAxis.z, matrix.yAxis.z, matrix.zAxis.z);
        xAxis.y = t1;
        yAxis.y = t2;
        zAxis.y = t3;
        t1 = dotRow(2, matrix.xAxis.x, matrix.yAxis.x, matrix.zAxis.x);
        t2 = dotRow(2, matrix.xAxis.y, matrix.yAxis.y, matrix.zAxis.y);
        t3 = dotRow(2, matrix.xAxis.z, matrix.yAxis.z, matrix.zAxis.z);
        xAxis.z = t1;
        yAxis.z = t2;
        zAxis.z = t3;
        return this;
    }

    public Matrix3f orthonormalize() {
        zAxis.normalize();
        yAxis.set(zAxis).cross(xAxis);
        yAxis.normalize();
        xAxis.set(yAxis).cross(zAxis);
        xAxis.normalize();
        return this;
    }

    public Matrix3f orthonormalize(Vector3f position, Vector3f target, Vector3f up) {
        zAxis.set(position).subtract(target);
        zAxis.normalize();

        float zDotUp = zAxis.dot(up);

        if ((zDotUp < -MathHelper.TOLERANCE_DOT_PRODUCT_PARALLEL) || (zDotUp > MathHelper.TOLERANCE_DOT_PRODUCT_PARALLEL)) {
            yAxis.set(zAxis).cross(Vector3f.WORLD_X_AXIS);
            yAxis.normalize();
            xAxis.set(yAxis).cross(zAxis);
            xAxis.normalize();
        } else {
            xAxis.set(up).cross(zAxis);
            xAxis.normalize();
            yAxis.set(zAxis).cross(xAxis);
            yAxis.normalize();
        }

        return this;
    }

    public final Matrix3f set(Matrix3f matrix) {
        xAxis.set(matrix.xAxis);
        yAxis.set(matrix.yAxis);
        zAxis.set(matrix.zAxis);
        return this;
    }

    public final Matrix3f setAxes(Vector3f xAxis, Vector3f yAxis, Vector3f zAxis) {
        this.xAxis.set(xAxis);
        this.yAxis.set(yAxis);
        this.zAxis.set(zAxis);
        return this;
    }

    public final Matrix3f setColumnMajor(
            float xx, float xy, float xz,
            float yx, float yy, float yz,
            float zx, float zy, float zz) {
        xAxis.set(xx, xy, xz);
        yAxis.set(yx, yy, yz);
        zAxis.set(zx, zy, zz);
        return this;
    }

    public Matrix3f setDiagonal(float radius) {
        xAxis.x = radius;
        yAxis.y = radius;
        zAxis.z = radius;
        return this;
    }

    public Matrix3f setDiagonal(float x, float y, float z) {
        xAxis.x = x;
        yAxis.y = y;
        zAxis.z = z;
        return this;
    }

    public Matrix3f setDiagonal(Vector3f vector) {
        xAxis.x = vector.x;
        yAxis.y = vector.y;
        zAxis.z = vector.z;
        return this;
    }

    public Matrix3f setInverse(Matrix3f matrix) {
        assert (this != matrix);
        float determinant = matrix.getDeterminant();
        if (determinant < MathHelper.ZERO_EPSILON) {
            return null;
        }
        xAxis.x = (matrix.yAxis.y * matrix.zAxis.z - matrix.zAxis.y * matrix.yAxis.z);
        xAxis.y = -(matrix.xAxis.y * matrix.zAxis.z - matrix.zAxis.y * matrix.xAxis.z);
        xAxis.z = (matrix.xAxis.y * matrix.yAxis.z - matrix.yAxis.y * matrix.xAxis.z);
        yAxis.x = -(matrix.yAxis.x * matrix.zAxis.z - matrix.zAxis.x * matrix.yAxis.z);
        yAxis.y = (matrix.xAxis.x * matrix.zAxis.z - matrix.zAxis.x * matrix.xAxis.z);
        yAxis.z = -(matrix.xAxis.x * matrix.yAxis.z - matrix.yAxis.x * matrix.xAxis.z);
        zAxis.x = (matrix.yAxis.x * matrix.zAxis.y - matrix.zAxis.x * matrix.yAxis.y);
        zAxis.y = -(matrix.xAxis.x * matrix.zAxis.y - matrix.zAxis.x * matrix.xAxis.y);
        zAxis.z = (matrix.xAxis.x * matrix.yAxis.y - matrix.yAxis.x * matrix.xAxis.y);
        return multiply(1f / determinant);
    }

    public Matrix3f setRowMajor(
            float xx, float yx, float zx,
            float xy, float yy, float zy,
            float xz, float yz, float zz) {
        xAxis.set(xx, xy, xz);
        yAxis.set(yx, yy, yz);
        zAxis.set(zx, zy, zz);
        return this;
    }

    public Matrix3f setSkewSymmetric(Vector3f vector) {
        xAxis.x = 0f;
        xAxis.y = vector.z;
        xAxis.z = -vector.y;
        yAxis.x = -vector.z;
        yAxis.y = 0f;
        yAxis.z = vector.x;
        zAxis.x = vector.y;
        zAxis.y = -vector.x;
        zAxis.z = 0f;
        return this;
    }

    public Matrix3f setTranspose(Matrix3f matrix) {
        assert (this != matrix);
        xAxis.x = matrix.xAxis.x;
        yAxis.x = matrix.xAxis.y;
        zAxis.x = matrix.xAxis.z;
        xAxis.y = matrix.yAxis.x;
        yAxis.y = matrix.yAxis.y;
        zAxis.y = matrix.yAxis.z;
        xAxis.z = matrix.zAxis.x;
        yAxis.z = matrix.zAxis.y;
        zAxis.z = matrix.zAxis.z;
        return this;
    }

    // Scalar Operations
    public float dotRow(int row, float x, float y, float z) {
        switch (row) {
            case 0:
                return xAxis.x * x + yAxis.x * y + zAxis.x * z;
            case 1:
                return xAxis.y * x + yAxis.y * y + zAxis.y * z;
            case 2:
                return xAxis.z * x + yAxis.z * y + zAxis.z * z;
        }
        throw new IllegalArgumentException("Invalid row: " + row);
    }

    public float dotRow(int row, Vector3f vector) {
        switch (row) {
            case 0:
                return xAxis.x * vector.x + yAxis.x * vector.y + zAxis.x * vector.z;
            case 1:
                return xAxis.y * vector.x + yAxis.y * vector.y + zAxis.y * vector.z;
            case 2:
                return xAxis.z * vector.x + yAxis.z * vector.y + zAxis.z * vector.z;
        }
        throw new IllegalArgumentException("Invalid row: " + row);
    }

    public float getDeterminant() {
        return xAxis.x * (yAxis.y * zAxis.z - zAxis.y * yAxis.z)
                - yAxis.x * (xAxis.y * zAxis.z - zAxis.y * xAxis.z)
                + zAxis.x * (xAxis.y * yAxis.z - yAxis.y * xAxis.z);
    }

    public float getElement(int column, int row) {
        switch (column) {
            case 0:
                switch (row) {
                    case 0:
                        return xAxis.x;
                    case 1:
                        return xAxis.y;
                    case 2:
                        return xAxis.z;
                }
                throw new IllegalArgumentException("Invalid row: " + row);

            case 1:
                switch (row) {
                    case 0:
                        return yAxis.x;
                    case 1:
                        return yAxis.y;
                    case 2:
                        return yAxis.z;
                }
                throw new IllegalArgumentException("Invalid row: " + row);

            case 2:
                switch (row) {
                    case 0:
                        return zAxis.x;
                    case 1:
                        return zAxis.y;
                    case 2:
                        return zAxis.z;
                }
                throw new IllegalArgumentException("Invalid row: " + row);
        }
        throw new IllegalArgumentException("Invalid column: " + column);
    }

    public float getHeading() {
        float h, t = xAxis.y;

        if ((t >= MathHelper.TOLERANCE_EULER_CONVERSION) || (t <= -MathHelper.TOLERANCE_EULER_CONVERSION)) {
            h = MathHelper.ArcTan2(zAxis.x, zAxis.z);
        } else {
            h = MathHelper.ArcTan2(-xAxis.z, xAxis.x);
        }

        h = -MathHelper.ToDegrees(h) + 360f;
        if (h > 360f) {
            h -= 360f;
        }
        return h;
    }

    public float getPitch() {
        float p, t = xAxis.y;

        if ((t >= MathHelper.TOLERANCE_EULER_CONVERSION) || (t <= -MathHelper.TOLERANCE_EULER_CONVERSION)) {
            p = 0f;
        } else {
            p = MathHelper.ArcTan2(-zAxis.y, yAxis.y);
        }

        return MathHelper.ToDegrees(p);
    }

    public float getRoll() {
        float r, t = xAxis.y;

        if (t >= MathHelper.TOLERANCE_EULER_CONVERSION) {
            r = MathHelper.PI_OVER_2;
        } else if (t <= -MathHelper.TOLERANCE_EULER_CONVERSION) {
            r = -MathHelper.PI_OVER_2;
        } else {
            r = MathHelper.ArcSin(t);
        }

        return MathHelper.ToDegrees(r);
    }

    // Vector Operations
    public Vector3f getAxis(int axis) {
        switch (axis) {
            case 0:
                return xAxis;
            case 1:
                return yAxis;
            case 2:
                return zAxis;
        }
        throw new IllegalArgumentException("Invalid axis: " + axis);
    }

    public Vector3f getXAxis() {
        return xAxis;
    }

    public Vector3f getYAxis() {
        return yAxis;
    }

    public Vector3f getZAxis() {
        return zAxis;
    }

    public Vector3f multiplyOut(Vector3f vector, Vector3f out) {
        out.set(
                dotRow(0, vector),
                dotRow(1, vector),
                dotRow(2, vector));
        return out;
    }

    public Vector3f multiplyTransposeOut(Vector3f vector, Vector3f out) {
        out.set(
                xAxis.dot(vector),
                yAxis.dot(vector),
                zAxis.dot(vector));
        return out;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.xAxis);
        hash = 79 * hash + Objects.hashCode(this.yAxis);
        hash = 79 * hash + Objects.hashCode(this.zAxis);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Matrix3f other = (Matrix3f) obj;
        if (!Objects.equals(this.xAxis, other.xAxis)) {
            return false;
        }
        if (!Objects.equals(this.yAxis, other.yAxis)) {
            return false;
        }
        return Objects.equals(this.zAxis, other.zAxis);
    }

    @Override
    public String toString() {
        return "(X-Axis: " + xAxis + "\nY-Axis: " + yAxis + "\nZ-Axis: " + zAxis + ")";
    }

}
