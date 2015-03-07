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

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Matrix4f {

    /**
     * Layout of the matrix as follows:
     *
     * Column 1 | Column 2 | Column 3 | Column 4
     *
     * d0 | d4 | d8 | d12
     *
     * d1 | d5 | d9 | d13
     *
     * d2 | d6 | d10 | d14
     *
     * d3 | d7 | d11 | d15
     */
    float d0;
    float d1;
    float d2;
    float d3;
    float d4;
    float d5;
    float d6;
    float d7;
    float d8;
    float d9;
    float d10;
    float d11;
    float d12;
    float d13;
    float d14;
    float d15;

    // Constructors
    public Matrix4f() {
        identity();
    }

    public Matrix4f(Matrix4f matrix) {
        set(matrix);
    }

    // Conversion Operations
    public void extractPlanes(
            Vector4f nearPlane, Vector4f farPlane,
            Vector4f leftPlane, Vector4f rightPlane,
            Vector4f bottomPlane, Vector4f topPlane) {

        // Near Plane (row 2 + row 3)
        nearPlane.x = d2 + d3;
        nearPlane.y = d6 + d7;
        nearPlane.z = d10 + d11;
        nearPlane.w = d14 + d15;

        // Far Plane (row 3 - row 2)
        farPlane.x = d3 - d2;
        farPlane.y = d7 - d6;
        farPlane.z = d11 - d10;
        farPlane.w = d15 - d14;

        // Left Plane (row 0 + row 3)
        leftPlane.x = d0 + d3;
        leftPlane.y = d4 + d7;
        leftPlane.z = d8 + d11;
        leftPlane.w = d12 + d15;

        // Right Plane (row 3 - row 0)
        rightPlane.x = d3 - d0;
        rightPlane.y = d7 - d4;
        rightPlane.z = d11 - d8;
        rightPlane.w = d15 - d12;

        // Bottom Plane (row 1 + row 3)
        bottomPlane.x = d1 + d3;
        bottomPlane.y = d5 + d7;
        bottomPlane.z = d9 + d11;
        bottomPlane.w = d13 + d15;

        // Top Plane (row 3 - row 1)
        topPlane.x = d3 - d1;
        topPlane.y = d7 - d5;
        topPlane.z = d11 - d9;
        topPlane.w = d15 - d13;

        nearPlane.normalize();
        farPlane.normalize();
        leftPlane.normalize();
        rightPlane.normalize();
        bottomPlane.normalize();
        topPlane.normalize();
    }

    public void extractPlanes(ClipPlanes clipPlanes) {

        // Near Plane (row 2 + row 3)
        clipPlanes.npX = d2 + d3;
        clipPlanes.npY = d6 + d7;
        clipPlanes.npZ = d10 + d11;
        clipPlanes.npW = d14 + d15;

        // Far Plane (row 3 - row 2)
        clipPlanes.fpX = d3 - d2;
        clipPlanes.fpY = d7 - d6;
        clipPlanes.fpZ = d11 - d10;
        clipPlanes.fpW = d15 - d14;

        // Left Plane (row 0 + row 3)
        clipPlanes.lpX = d0 + d3;
        clipPlanes.lpY = d4 + d7;
        clipPlanes.lpZ = d8 + d11;
        clipPlanes.lpW = d12 + d15;

        // Right Plane (row 3 - row 0)
        clipPlanes.rpX = d3 - d0;
        clipPlanes.rpY = d7 - d4;
        clipPlanes.rpZ = d11 - d8;
        clipPlanes.rpW = d15 - d12;

        // Bottom Plane (row 1 + row 3)
        clipPlanes.bpX = d1 + d3;
        clipPlanes.bpY = d5 + d7;
        clipPlanes.bpZ = d9 + d11;
        clipPlanes.bpW = d13 + d15;

        // Top Plane (row 3 - row 1)
        clipPlanes.tpX = d3 - d1;
        clipPlanes.tpY = d7 - d5;
        clipPlanes.tpZ = d11 - d9;
        clipPlanes.tpW = d15 - d13;

        clipPlanes.normalize();
    }

    public void fromColumnArray(float[] in) {
        d0 = in[0];
        d1 = in[1];
        d2 = in[2];
        d3 = in[3];
        d4 = in[4];
        d5 = in[5];
        d6 = in[6];
        d7 = in[7];
        d8 = in[8];
        d9 = in[9];
        d10 = in[10];
        d11 = in[11];
        d12 = in[12];
        d13 = in[13];
        d14 = in[14];
        d15 = in[15];
    }

    public void fromColumnFloatBuffer(FloatBuffer fbIn) {
        d0 = fbIn.get();
        d1 = fbIn.get();
        d2 = fbIn.get();
        d3 = fbIn.get();
        d4 = fbIn.get();
        d5 = fbIn.get();
        d6 = fbIn.get();
        d7 = fbIn.get();
        d8 = fbIn.get();
        d9 = fbIn.get();
        d10 = fbIn.get();
        d11 = fbIn.get();
        d12 = fbIn.get();
        d13 = fbIn.get();
        d14 = fbIn.get();
        d15 = fbIn.get();
    }

    public void fromRowArray(float[] in) {
        d0 = in[0];
        d4 = in[1];
        d8 = in[2];
        d12 = in[3];
        d1 = in[4];
        d5 = in[5];
        d9 = in[6];
        d13 = in[7];
        d2 = in[8];
        d6 = in[9];
        d10 = in[10];
        d14 = in[11];
        d3 = in[12];
        d7 = in[13];
        d11 = in[14];
        d15 = in[15];
    }

    public void toFloatBuffer(FloatBuffer fbOut) {
        fbOut.put(d0);
        fbOut.put(d1);
        fbOut.put(d2);
        fbOut.put(d3);
        fbOut.put(d4);
        fbOut.put(d5);
        fbOut.put(d6);
        fbOut.put(d7);
        fbOut.put(d8);
        fbOut.put(d9);
        fbOut.put(d10);
        fbOut.put(d11);
        fbOut.put(d12);
        fbOut.put(d13);
        fbOut.put(d14);
        fbOut.put(d15);
    }

    public void toMatrix3f(Matrix3f out) {
        out.xAxis.x = d0;
        out.xAxis.y = d1;
        out.xAxis.z = d2;
        out.yAxis.x = d4;
        out.yAxis.y = d5;
        out.yAxis.z = d6;
        out.zAxis.x = d8;
        out.zAxis.y = d9;
        out.zAxis.z = d10;
    }

    public void toColumnArray(float[] out) {
        out[0] = d0;
        out[1] = d1;
        out[2] = d2;
        out[3] = d3;
        out[4] = d4;
        out[5] = d5;
        out[6] = d6;
        out[7] = d7;
        out[8] = d8;
        out[9] = d9;
        out[10] = d10;
        out[11] = d11;
        out[12] = d12;
        out[13] = d13;
        out[14] = d14;
        out[15] = d15;
    }

    public void toRowArray(float[] out) {
        out[0] = d0;
        out[1] = d4;
        out[2] = d8;
        out[3] = d12;
        out[4] = d1;
        out[5] = d5;
        out[6] = d9;
        out[7] = d13;
        out[8] = d2;
        out[9] = d6;
        out[10] = d10;
        out[11] = d14;
        out[12] = d3;
        out[13] = d7;
        out[14] = d11;
        out[15] = d15;
    }

    // Scalar Operations
    public float dotRow(int row, float c0, float c1, float c2, float c3) {
        switch (row) {
            case 0:
                return d0 * c0 + d4 * c1 + d8 * c2 + d12 * c3;
            case 1:
                return d1 * c0 + d5 * c1 + d9 * c2 + d13 * c3;
            case 2:
                return d2 * c0 + d6 * c1 + d10 * c2 + d14 * c3;
            case 3:
                return d3 * c0 + d7 * c1 + d11 * c2 + d15 * c3;
        }
        throw new IllegalArgumentException("Invalid row: " + row);
    }

    public float dotRow3(int row, Vector3f vector) {
        switch (row) {
            case 0:
                return d0 * vector.x + d4 * vector.y + d8 * vector.z;
            case 1:
                return d1 * vector.x + d5 * vector.y + d9 * vector.z;
            case 2:
                return d2 * vector.x + d6 * vector.y + d10 * vector.z;
        }
        throw new IllegalArgumentException("Invalid row: " + row);
    }

    public float getDeterminant() {
        float a1b1 = d10 * d15 - d14 * d11;
        float a2c1 = d6 * d15 - d14 * d7;
        float a3d1 = d6 * d11 - d10 * d7;
        float b2c2 = d2 * d15 - d14 * d3;
        float b3d2 = d2 * d11 - d10 * d3;
        float c3d3 = d2 * d7 - d6 * d3;
        return d0 * (d5 * a1b1 - d9 * a2c1 + d13 * a3d1)
                - d4 * (d1 * a1b1 - d9 * b2c2 + d13 * b3d2)
                + d8 * (d1 * a2c1 - d5 * b2c2 + d13 * c3d3)
                - d12 * (d1 * a3d1 - d5 * b3d2 + d9 * c3d3);
    }

    public float getPositionX() {
        return d12;
    }

    public float getPositionY() {
        return d13;
    }

    public float getPositionZ() {
        return d14;
    }

    // Matrix Operations
    public Matrix4f biasMultiplyProjectionOut(Matrix4f matrix, Matrix4f out) {
        assert (this != matrix);
        assert (this != out);
        // Bias         Project        = Result
        // x 0 0 px  }  d0 0  d8  d12  = (x d0) (0)    (x d8 + px d11)  (x d12 + px d15)
        // 0 y 0 py  |  0  d5 d9  d13  = (0)    (y d5) (y d9 + py d11)  (y d13 + py d15)
        // 0 0 z pz  |  0  0  d10 d14  = (0)    (0)    (z d10 + pz d11) (z d14 + pz d15)
        // 0 0 0 1   |  0  0  d11 d15  = (0)    (0)    (d11)            (d15)
        out.d0 = d0 * matrix.d0;
        out.d4 = 0f;
        out.d8 = d0 * matrix.d8 + d12 * matrix.d11;
        out.d12 = d0 * matrix.d12 + d12 * matrix.d15;
        out.d1 = 0f;
        out.d5 = d5 * matrix.d5;
        out.d9 = d5 * matrix.d9 + d13 * matrix.d11;
        out.d13 = d5 * matrix.d13 + d13 * matrix.d15;
        out.d2 = 0f;
        out.d6 = 0f;
        out.d10 = d10 * matrix.d10 + d14 * matrix.d11;
        out.d14 = d10 * matrix.d14 + d14 * matrix.d15;
        out.d3 = 0f;
        out.d7 = 0f;
        out.d11 = matrix.d11;
        out.d15 = matrix.d15;
        return this;
    }

    public Matrix4f fromAxisAngle(Vector3f vector, float degrees) {
        float radians = MathHelper.ToRadians(degrees);
        float c = MathHelper.Cos(radians);
        float s = MathHelper.Sin(radians);
        float t = (1f - c);
        d0 = t * (vector.x * vector.x) + c;
        d1 = t * (vector.x * vector.y) + (vector.z * s);
        d2 = t * (vector.x * vector.z) - (vector.y * s);
        d3 = 0f;
        d4 = t * (vector.y * vector.x) - (vector.z * s);
        d5 = t * (vector.y * vector.y) + c;
        d6 = t * (vector.y * vector.z) + (vector.x * s);
        d7 = 0f;
        d8 = t * (vector.z * vector.x) + (vector.y * s);
        d9 = t * (vector.z * vector.y) - (vector.x * s);
        d10 = t * (vector.z * vector.z) + c;
        d11 = 0f;
        return this;
    }

    public final Matrix4f identity() {
        d0 = 1f;
        d1 = 0f;
        d2 = 0f;
        d3 = 0f;
        d4 = 0f;
        d5 = 1f;
        d6 = 0f;
        d7 = 0f;
        d8 = 0f;
        d9 = 0f;
        d10 = 1f;
        d11 = 0f;
        d12 = 0f;
        d13 = 0f;
        d14 = 0f;
        d15 = 1f;
        return this;
    }

    public Matrix4f multiply(float value) {
        d0 *= value;
        d1 *= value;
        d2 *= value;
        d3 *= value;
        d4 *= value;
        d5 *= value;
        d6 *= value;
        d7 *= value;
        d8 *= value;
        d9 *= value;
        d10 *= value;
        d11 *= value;
        d12 *= value;
        d13 *= value;
        d14 *= value;
        d15 *= value;
        return this;
    }

    public Matrix4f multiply(Matrix4f matrix) {
        float t1, t2, t3, t4;
        t1 = dotRow(0, matrix.d0, matrix.d1, matrix.d2, matrix.d3);
        t2 = dotRow(0, matrix.d4, matrix.d5, matrix.d6, matrix.d7);
        t3 = dotRow(0, matrix.d8, matrix.d9, matrix.d10, matrix.d11);
        t4 = dotRow(0, matrix.d12, matrix.d13, matrix.d14, matrix.d15);
        d0 = t1;
        d4 = t2;
        d8 = t3;
        d12 = t4;
        t1 = dotRow(1, matrix.d0, matrix.d1, matrix.d2, matrix.d3);
        t2 = dotRow(1, matrix.d4, matrix.d5, matrix.d6, matrix.d7);
        t3 = dotRow(1, matrix.d8, matrix.d9, matrix.d10, matrix.d11);
        t4 = dotRow(1, matrix.d12, matrix.d13, matrix.d14, matrix.d15);
        d1 = t1;
        d5 = t2;
        d9 = t3;
        d13 = t4;
        t1 = dotRow(2, matrix.d0, matrix.d1, matrix.d2, matrix.d3);
        t2 = dotRow(2, matrix.d4, matrix.d5, matrix.d6, matrix.d7);
        t3 = dotRow(2, matrix.d8, matrix.d9, matrix.d10, matrix.d11);
        t4 = dotRow(2, matrix.d12, matrix.d13, matrix.d14, matrix.d15);
        d2 = t1;
        d6 = t2;
        d10 = t3;
        d14 = t4;
        t1 = dotRow(3, matrix.d0, matrix.d1, matrix.d2, matrix.d3);
        t2 = dotRow(3, matrix.d4, matrix.d5, matrix.d6, matrix.d7);
        t3 = dotRow(3, matrix.d8, matrix.d9, matrix.d10, matrix.d11);
        t4 = dotRow(3, matrix.d12, matrix.d13, matrix.d14, matrix.d15);
        d3 = t1;
        d7 = t2;
        d11 = t3;
        d15 = t4;
        return this;
    }

    public Matrix4f multiply(Vector3f vector) {
        d12 += d0 * vector.x + d4 * vector.y + d8 * vector.z;
        d13 += d1 * vector.x + d5 * vector.y + d9 * vector.z;
        d14 += d2 * vector.x + d6 * vector.y + d10 * vector.z;
        d15 += d3 * vector.x + d7 * vector.y + d11 * vector.z;
        return this;
    }

    public Matrix4f multiplyOut(Matrix4f matrix, Matrix4f out) {
        assert (this != matrix);
        assert (this != out);
        out.d0 = dotRow(0, matrix.d0, matrix.d1, matrix.d2, matrix.d3);
        out.d4 = dotRow(0, matrix.d4, matrix.d5, matrix.d6, matrix.d7);
        out.d8 = dotRow(0, matrix.d8, matrix.d9, matrix.d10, matrix.d11);
        out.d12 = dotRow(0, matrix.d12, matrix.d13, matrix.d14, matrix.d15);
        out.d1 = dotRow(1, matrix.d0, matrix.d1, matrix.d2, matrix.d3);
        out.d5 = dotRow(1, matrix.d4, matrix.d5, matrix.d6, matrix.d7);
        out.d9 = dotRow(1, matrix.d8, matrix.d9, matrix.d10, matrix.d11);
        out.d13 = dotRow(1, matrix.d12, matrix.d13, matrix.d14, matrix.d15);
        out.d2 = dotRow(2, matrix.d0, matrix.d1, matrix.d2, matrix.d3);
        out.d6 = dotRow(2, matrix.d4, matrix.d5, matrix.d6, matrix.d7);
        out.d10 = dotRow(2, matrix.d8, matrix.d9, matrix.d10, matrix.d11);
        out.d14 = dotRow(2, matrix.d12, matrix.d13, matrix.d14, matrix.d15);
        out.d3 = dotRow(3, matrix.d0, matrix.d1, matrix.d2, matrix.d3);
        out.d7 = dotRow(3, matrix.d4, matrix.d5, matrix.d6, matrix.d7);
        out.d11 = dotRow(3, matrix.d8, matrix.d9, matrix.d10, matrix.d11);
        out.d15 = dotRow(3, matrix.d12, matrix.d13, matrix.d14, matrix.d15);
        return this;
    }

    public Matrix4f projectionMultiplyViewOut(Matrix4f matrix, Matrix4f out) {
        assert (this != matrix);
        assert (this != out);
        // http://www.songho.ca/opengl/gl_projectionmatrix.html
        // Project        |  View         = Result
        // d0 0  d8  d12  |  xx yx zx px  = (d0 xx + d8 xz) (d0 yx + d8 yz) (d0 zx + d8 zz) (d0 px + d8 pz) + d12
        // 0  d5 d9  d13  |  xy yy zy py  = (d5 xy + d9 xz) (d5 yy + d9 yz) (d5 zy + d9 zz) (d5 py + d9 pz) + d13
        // 0  0  d10 d14  |  xz yz zz pz  = (d10 xz)        (d10 yz)        (d10 zz)        (d10 pz) + d14
        // 0  0  d11 d15  |  0  0  0  1   = (d11 xz)        (d11 yz)        (d11 zz)        (d11 pz) + d15
        out.d0 = d0 * matrix.d0 + d8 * matrix.d2;
        out.d4 = d0 * matrix.d4 + d8 * matrix.d6;
        out.d8 = d0 * matrix.d8 + d8 * matrix.d10;
        out.d12 = d0 * matrix.d12 + d8 * matrix.d14 + d12;
        out.d1 = d5 * matrix.d1 + d9 * matrix.d2;
        out.d5 = d5 * matrix.d5 + d9 * matrix.d6;
        out.d9 = d5 * matrix.d9 + d9 * matrix.d10;
        out.d13 = d5 * matrix.d13 + d9 * matrix.d14 + d13;
        out.d2 = d10 * matrix.d2;
        out.d6 = d10 * matrix.d6;
        out.d10 = d10 * matrix.d10;
        out.d14 = d10 * matrix.d14 + d14;
        out.d3 = d11 * matrix.d2;
        out.d7 = d11 * matrix.d6;
        out.d11 = d11 * matrix.d10;
        out.d15 = d11 * matrix.d14 + d15;
        return this;
    }

    public Matrix4f projectionSymmetricalMultiplyViewOut(Matrix4f matrix, Matrix4f out) {
        assert (this != matrix);
        assert (this != out);
        // http://www.songho.ca/opengl/gl_projectionmatrix.html
        // Project        |  View         = Result
        // d0 0  0   0    |  xx yx zx px  = (d0 xx)  (d0 yx)  (d0 zx)  (d0 px)
        // 0  d5 0   0    |  xy yy zy py  = (d5 xy)  (d5 yy)  (d5 zy)  (d5 py)
        // 0  0  d10 d14  |  xz yz zz pz  = (d10 xz) (d10 yz) (d10 zz) (d10 pz) + d14
        // 0  0  d11 d15  |  0  0  0  1   = (d11 xz) (d11 yz) (d11 zz) (d11 pz) + d15
        out.d0 = d0 * matrix.d0;
        out.d4 = d0 * matrix.d4;
        out.d8 = d0 * matrix.d8;
        out.d12 = d0 * matrix.d12;
        out.d1 = d5 * matrix.d1;
        out.d5 = d5 * matrix.d5;
        out.d9 = d5 * matrix.d9;
        out.d13 = d5 * matrix.d13;
        out.d2 = d10 * matrix.d2;
        out.d6 = d10 * matrix.d6;
        out.d10 = d10 * matrix.d10;
        out.d14 = d10 * matrix.d14 + d14;
        out.d3 = d11 * matrix.d2;
        out.d7 = d11 * matrix.d6;
        out.d11 = d11 * matrix.d10;
        out.d15 = d11 * matrix.d14 + d15;
        return this;
    }

    public final Matrix4f set(Matrix4f matrix) {
        d0 = matrix.d0;
        d1 = matrix.d1;
        d2 = matrix.d2;
        d3 = matrix.d3;
        d4 = matrix.d4;
        d5 = matrix.d5;
        d6 = matrix.d6;
        d7 = matrix.d7;
        d8 = matrix.d8;
        d9 = matrix.d9;
        d10 = matrix.d10;
        d11 = matrix.d11;
        d12 = matrix.d12;
        d13 = matrix.d13;
        d14 = matrix.d14;
        d15 = matrix.d15;
        return this;
    }

    public Matrix4f set(Transform4f transform) {
        d0 = transform.rotation.xAxis.x;
        d1 = transform.rotation.xAxis.y;
        d2 = transform.rotation.xAxis.z;
        d3 = 0f;
        d4 = transform.rotation.yAxis.x;
        d5 = transform.rotation.yAxis.y;
        d6 = transform.rotation.yAxis.z;
        d7 = 0f;
        d8 = transform.rotation.zAxis.x;
        d9 = transform.rotation.zAxis.y;
        d10 = transform.rotation.zAxis.z;
        d11 = 0f;
        d12 = transform.position.x;
        d13 = transform.position.y;
        d14 = transform.position.z;
        d15 = 1f;
        return this;
    }

    public Matrix4f setDiagonal(float radius) {
        d0 = radius;
        d5 = radius;
        d10 = radius;
        return this;
    }

    public Matrix4f setDiagonal(float x, float y, float z) {
        d0 = x;
        d5 = y;
        d10 = z;
        return this;
    }

    public Matrix4f setFrustum(float left, float right, float bottom, float top, float zNear, float zFar) {
        float invDeltaX = 1f / (right - left);
        float invDeltaY = 1f / (top - bottom);
        float invDeltaZ = 1f / (zFar - zNear);
        float h = 2f * zNear * invDeltaY;
        setRow(0, 2f * zNear * invDeltaX, 0f, (right + left) * invDeltaX, 0f);
        setRow(1, 0f, h, (top + bottom) * invDeltaY, 0f);
        setRow(2, 0f, 0f, -(zFar + zNear) * invDeltaZ, -2f * zFar * zNear * invDeltaZ);
        setRow(3, 0f, 0f, -1f, 0f);
        return this;
    }

    public Matrix4f setInverse(Matrix3f rotation, Vector3f position) {
        d0 = rotation.xAxis.x;
        d1 = rotation.yAxis.x;
        d2 = rotation.zAxis.x;
        d3 = 0f;
        d4 = rotation.xAxis.y;
        d5 = rotation.yAxis.y;
        d6 = rotation.zAxis.y;
        d7 = 0f;
        d8 = rotation.xAxis.z;
        d9 = rotation.yAxis.z;
        d10 = rotation.zAxis.z;
        d11 = 0f;
        d12 = -rotation.xAxis.dot(position);
        d13 = -rotation.yAxis.dot(position);
        d14 = -rotation.zAxis.dot(position);
        d15 = 1f;
        return this;
    }

    public Matrix4f setInverse(Matrix4f matrix) {
        assert (this != matrix);

        float a1b1 = matrix.d10 * matrix.d15 - matrix.d14 * matrix.d11;
        float a2c1 = matrix.d6 * matrix.d15 - matrix.d14 * matrix.d7;
        float a3d1 = matrix.d6 * matrix.d11 - matrix.d10 * matrix.d7;
        float b2c2 = matrix.d2 * matrix.d15 - matrix.d14 * matrix.d3;
        float b3d2 = matrix.d2 * matrix.d11 - matrix.d10 * matrix.d3;
        float c3d3 = matrix.d2 * matrix.d7 - matrix.d6 * matrix.d3;

        float t0 = matrix.d5 * a1b1 - matrix.d9 * a2c1 + matrix.d13 * a3d1;
        float t4 = matrix.d1 * a1b1 - matrix.d9 * b2c2 + matrix.d13 * b3d2;
        float t8 = matrix.d1 * a2c1 - matrix.d5 * b2c2 + matrix.d13 * c3d3;
        float t12 = matrix.d1 * a3d1 - matrix.d5 * b3d2 + matrix.d9 * c3d3;

        float determinant = matrix.d0 * t0 - matrix.d4 * t4 + matrix.d8 * t8 - matrix.d12 * t12;
        if ((determinant < MathHelper.ZERO_EPSILON) && (determinant > -MathHelper.ZERO_EPSILON)) {
            throw new RuntimeException("Unable to invert matrix");
        }

        // Row 1
        d0 = t0;
        d4 = -t4;
        d8 = t8;
        d12 = -t12;
        // Row 2
        a1b1 = matrix.d10 * matrix.d15 - matrix.d14 * matrix.d11;
        a2c1 = matrix.d6 * matrix.d15 - matrix.d14 * matrix.d7;
        a3d1 = matrix.d6 * matrix.d11 - matrix.d10 * matrix.d7;
        b2c2 = matrix.d2 * matrix.d15 - matrix.d14 * matrix.d3;
        b3d2 = matrix.d2 * matrix.d11 - matrix.d10 * matrix.d3;
        c3d3 = matrix.d2 * matrix.d7 - matrix.d6 * matrix.d3;
        d1 = -(matrix.d4 * a1b1 - matrix.d8 * a2c1 + matrix.d12 * a3d1);
        d5 = matrix.d0 * a1b1 - matrix.d8 * b2c2 + matrix.d12 * b3d2;
        d9 = -(matrix.d0 * a2c1 - matrix.d4 * b2c2 + matrix.d12 * c3d3);
        d13 = matrix.d0 * a3d1 - matrix.d4 * b3d2 + matrix.d8 * c3d3;
        // Row 3
        a1b1 = matrix.d9 * matrix.d15 - matrix.d13 * matrix.d11;
        a2c1 = matrix.d5 * matrix.d15 - matrix.d13 * matrix.d7;
        a3d1 = matrix.d5 * matrix.d11 - matrix.d9 * matrix.d7;
        b2c2 = matrix.d1 * matrix.d15 - matrix.d13 * matrix.d3;
        b3d2 = matrix.d1 * matrix.d11 - matrix.d9 * matrix.d3;
        c3d3 = matrix.d1 * matrix.d7 - matrix.d5 * matrix.d3;
        d2 = matrix.d4 * a1b1 - matrix.d8 * a2c1 + matrix.d12 * a3d1;
        d6 = -(matrix.d0 * a1b1 - matrix.d8 * b2c2 + matrix.d12 * b3d2);
        d10 = matrix.d0 * a2c1 - matrix.d4 * b2c2 + matrix.d12 * c3d3;
        d14 = -(matrix.d0 * a3d1 - matrix.d4 * b3d2 + matrix.d8 * c3d3);
        // Row 4
        a1b1 = matrix.d9 * matrix.d14 - matrix.d13 * matrix.d10;
        a2c1 = matrix.d5 * matrix.d14 - matrix.d13 * matrix.d6;
        a3d1 = matrix.d5 * matrix.d10 - matrix.d9 * matrix.d6;
        b2c2 = matrix.d1 * matrix.d14 - matrix.d13 * matrix.d2;
        b3d2 = matrix.d1 * matrix.d10 - matrix.d9 * matrix.d2;
        c3d3 = matrix.d1 * matrix.d6 - matrix.d5 * matrix.d2;
        d3 = -(matrix.d4 * a1b1 - matrix.d8 * a2c1 + matrix.d12 * a3d1);
        d7 = matrix.d0 * a1b1 - matrix.d8 * b2c2 + matrix.d12 * b3d2;
        d11 = -(matrix.d0 * a2c1 - matrix.d4 * b2c2 + matrix.d12 * c3d3);
        d15 = matrix.d0 * a3d1 - matrix.d4 * b3d2 + matrix.d8 * c3d3;
        return multiply(1f / determinant);
    }

    public Matrix4f setOrthogonal(float left, float right, float bottom, float top, float zNear, float zFar) {
        float invDeltaX = 1f / (right - left);
        float invDeltaY = 1f / (top - bottom);
        float invDeltaZ = 1f / (zFar - zNear);
        float h = 2f * invDeltaY;
        setRow(0, 2f * invDeltaX, 0f, 0f, -(right + left) * invDeltaX);
        setRow(1, 0f, h, 0f, -(top + bottom) * invDeltaY);
        setRow(2, 0f, 0f, -2f * invDeltaZ, -(zFar + zNear) * invDeltaZ);
        setRow(3, 0f, 0f, 0f, 1f);
        return this;
    }

    public Matrix4f setPerspective(float fieldOfViewY, float aspectRatio, float zNear, float zFar) {
        float coTangent = 1f / MathHelper.Tan(fieldOfViewY * MathHelper.PI_OVER_360);
        float invDeltaZ = 1f / (zFar - zNear);
        setRow(0, coTangent / aspectRatio, 0f, 0f, 0f);
        setRow(1, 0f, coTangent, 0f, 0f);
        setRow(2, 0f, 0f, -(zFar + zNear) * invDeltaZ, -2f * zNear * zFar * invDeltaZ);
        setRow(3, 0f, 0f, -1f, 0f);
        return this;
    }

    public Matrix4f setPosition(float x, float y, float z) {
        d12 = x;
        d13 = y;
        d14 = z;
        return this;
    }

    public Matrix4f setPosition(Vector3f position) {
        d12 = position.x;
        d13 = position.y;
        d14 = position.z;
        return this;
    }

    public Matrix4f setRow(int row, float c0, float c1, float c2, float c3) {
        switch (row) {
            case 0:
                d0 = c0;
                d4 = c1;
                d8 = c2;
                d12 = c3;
                break;

            case 1:
                d1 = c0;
                d5 = c1;
                d9 = c2;
                d13 = c3;
                break;

            case 2:
                d2 = c0;
                d6 = c1;
                d10 = c2;
                d14 = c3;
                break;

            case 3:
                d3 = c0;
                d7 = c1;
                d11 = c2;
                d15 = c3;
                break;

            default:
                throw new IllegalArgumentException("Invalid row: " + row);
        }
        return this;
    }

    public void viewMultiplyModelOut(Matrix4f matrix, Matrix4f out) {
        assert (this != matrix);
        assert (this != out);
        // View         |  Model        = Result
        // vxx vyx vzx vpx  |  mxx myx mzx mpx  = o1
        // vxy vyy vzy vpy  |  mxy myy mzy mpy  = o2
        // vxz vyz vzz vpz  |  mxz myz mzz mpz  = o3
        // 0  0  0  1       |  0  0  0  1       = o4
        // o1 = (vxx mxx + vyx mxy + vzx mxz) (vxx myx + vyx myy + vzx myz) (vxx mzx + vyx mzy + vzx mzz) (vxx mpx + vyx mpy + vzx mpz) + vpx
        // o2 = (vxy mxx + vyy mxy + vzy mxz) (vxy myx + vyy myy + vzy myz) (vxy mzx + vyy mzy + vzy mzz) (vxy mpx + vyy mpy + vzy mpz) + vpy
        // o3 = (vxz mxx + vyz mxy + vzz mxz) (vxz myx + vyz myy + vzz myz) (vxz mzx + vyz mzy + vzz mzz) (vxz mpx + vyz mpy + vzz mpz) + vpz
        // o4 = 0                             0                             0                             1
        out.d0 = d0 * matrix.d0 + d4 * matrix.d1 + d8 * matrix.d2;
        out.d4 = d0 * matrix.d4 + d4 * matrix.d5 + d8 * matrix.d6;
        out.d8 = d0 * matrix.d8 + d4 * matrix.d9 + d8 * matrix.d10;
        out.d12 = d0 * matrix.d12 + d4 * matrix.d13 + d8 * matrix.d14 + d12;
        out.d1 = d1 * matrix.d0 + d5 * matrix.d1 + d9 * matrix.d2;
        out.d5 = d1 * matrix.d4 + d5 * matrix.d5 + d9 * matrix.d6;
        out.d9 = d1 * matrix.d8 + d5 * matrix.d9 + d9 * matrix.d10;
        out.d13 = d1 * matrix.d12 + d5 * matrix.d13 + d9 * matrix.d14 + d13;
        out.d2 = d2 * matrix.d0 + d6 * matrix.d1 + d10 * matrix.d2;
        out.d6 = d2 * matrix.d4 + d6 * matrix.d5 + d10 * matrix.d6;
        out.d10 = d2 * matrix.d8 + d6 * matrix.d9 + d10 * matrix.d10;
        out.d14 = d2 * matrix.d12 + d6 * matrix.d13 + d10 * matrix.d14 + d14;
        out.d3 = 0f;
        out.d7 = 0f;
        out.d11 = 0f;
        out.d15 = 1f;
    }

    // Vector Operations
    public Vector3f multiplyOut(Vector3f vector, Vector3f out) {
        out.set(
                dotRow3(0, vector),
                dotRow3(1, vector),
                dotRow3(2, vector));
        out.add(d12, d13, d14);
        return out;
    }

    public Vector3f multiplyDirectionOut(Vector3f vector, Vector3f out) {
        out.set(
                dotRow3(0, vector),
                dotRow3(1, vector),
                dotRow3(2, vector));
        return out;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Float.floatToIntBits(this.d0);
        hash = 47 * hash + Float.floatToIntBits(this.d1);
        hash = 47 * hash + Float.floatToIntBits(this.d2);
        hash = 47 * hash + Float.floatToIntBits(this.d3);
        hash = 47 * hash + Float.floatToIntBits(this.d4);
        hash = 47 * hash + Float.floatToIntBits(this.d5);
        hash = 47 * hash + Float.floatToIntBits(this.d6);
        hash = 47 * hash + Float.floatToIntBits(this.d7);
        hash = 47 * hash + Float.floatToIntBits(this.d8);
        hash = 47 * hash + Float.floatToIntBits(this.d9);
        hash = 47 * hash + Float.floatToIntBits(this.d10);
        hash = 47 * hash + Float.floatToIntBits(this.d11);
        hash = 47 * hash + Float.floatToIntBits(this.d12);
        hash = 47 * hash + Float.floatToIntBits(this.d13);
        hash = 47 * hash + Float.floatToIntBits(this.d14);
        hash = 47 * hash + Float.floatToIntBits(this.d15);
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
        final Matrix4f other = (Matrix4f) obj;
        if (Float.floatToIntBits(this.d0) != Float.floatToIntBits(other.d0)) {
            return false;
        }
        if (Float.floatToIntBits(this.d1) != Float.floatToIntBits(other.d1)) {
            return false;
        }
        if (Float.floatToIntBits(this.d2) != Float.floatToIntBits(other.d2)) {
            return false;
        }
        if (Float.floatToIntBits(this.d3) != Float.floatToIntBits(other.d3)) {
            return false;
        }
        if (Float.floatToIntBits(this.d4) != Float.floatToIntBits(other.d4)) {
            return false;
        }
        if (Float.floatToIntBits(this.d5) != Float.floatToIntBits(other.d5)) {
            return false;
        }
        if (Float.floatToIntBits(this.d6) != Float.floatToIntBits(other.d6)) {
            return false;
        }
        if (Float.floatToIntBits(this.d7) != Float.floatToIntBits(other.d7)) {
            return false;
        }
        if (Float.floatToIntBits(this.d8) != Float.floatToIntBits(other.d8)) {
            return false;
        }
        if (Float.floatToIntBits(this.d9) != Float.floatToIntBits(other.d9)) {
            return false;
        }
        if (Float.floatToIntBits(this.d10) != Float.floatToIntBits(other.d10)) {
            return false;
        }
        if (Float.floatToIntBits(this.d11) != Float.floatToIntBits(other.d11)) {
            return false;
        }
        if (Float.floatToIntBits(this.d12) != Float.floatToIntBits(other.d12)) {
            return false;
        }
        if (Float.floatToIntBits(this.d13) != Float.floatToIntBits(other.d13)) {
            return false;
        }
        if (Float.floatToIntBits(this.d14) != Float.floatToIntBits(other.d14)) {
            return false;
        }
        return Float.floatToIntBits(this.d15) == Float.floatToIntBits(other.d15);
    }

    @Override
    public String toString() {
        return "(d0: " + d0 + " d4: " + d4 + " d8: " + d8 + " d12: " + d12 + ")\n"
                + "(d1: " + d1 + " d5: " + d5 + " d9: " + d9 + " d13: " + d13 + ")\n"
                + "(d2: " + d2 + " d6: " + d6 + " d10: " + d10 + " d14: " + d14 + ")\n"
                + "(d3: " + d3 + " d7: " + d7 + " d11: " + d11 + " d15: " + d15 + ")";
    }

}
