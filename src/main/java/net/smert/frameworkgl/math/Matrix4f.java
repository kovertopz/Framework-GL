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
    public void extractPlanes(Vector4f[] planes) {

        // Near Plane (row 2 + row 3)
        planes[0].x = d2 + d3;
        planes[0].y = d6 + d7;
        planes[0].z = d10 + d11;
        planes[0].w = d14 + d15;

        // Far Plane (row 3 - row 2)
        planes[1].x = d3 - d2;
        planes[1].y = d7 - d6;
        planes[1].z = d11 - d10;
        planes[1].w = d15 - d14;

        // Left Plane (row 0 + row 3)
        planes[2].x = d0 + d3;
        planes[2].y = d4 + d7;
        planes[2].z = d8 + d11;
        planes[2].w = d12 + d15;

        // Right Plane (row 3 - row 0)
        planes[3].x = d3 - d0;
        planes[3].y = d7 - d4;
        planes[3].z = d11 - d8;
        planes[3].w = d15 - d12;

        // Bottom Plane (row 1 + row 3)
        planes[4].x = d1 + d3;
        planes[4].y = d5 + d7;
        planes[4].z = d9 + d11;
        planes[4].w = d13 + d15;

        // Top Plane (row 3 - row 1)
        planes[5].x = d3 - d1;
        planes[5].y = d7 - d5;
        planes[5].z = d11 - d9;
        planes[5].w = d15 - d13;

        planes[0].normalize();
        planes[1].normalize();
        planes[2].normalize();
        planes[3].normalize();
        planes[4].normalize();
        planes[5].normalize();
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
        out.d4 = 0;
        out.d8 = d0 * matrix.d8 + d12 * matrix.d11;
        out.d12 = d0 * matrix.d12 + d12 * matrix.d15;
        out.d1 = 0;
        out.d5 = d5 * matrix.d5;
        out.d9 = d5 * matrix.d9 + d13 * matrix.d11;
        out.d13 = d5 * matrix.d13 + d13 * matrix.d15;
        out.d2 = 0;
        out.d6 = 0;
        out.d10 = d10 * matrix.d10 + d14 * matrix.d11;
        out.d14 = d10 * matrix.d14 + d14 * matrix.d15;
        out.d3 = 0;
        out.d7 = 0;
        out.d11 = matrix.d11;
        out.d15 = matrix.d15;
        return this;
    }

    public Matrix4f fromAxisAngle(Vector3f vector, float degrees) {
        float radians = MathHelper.ToRadians(degrees);
        float c = MathHelper.Cos(radians);
        float s = MathHelper.Sin(radians);
        float t = (1.0f - c);
        d0 = t * (vector.x * vector.x) + c;
        d1 = t * (vector.x * vector.y) + (vector.z * s);
        d2 = t * (vector.x * vector.z) - (vector.y * s);
        d3 = 0;
        d4 = t * (vector.y * vector.x) - (vector.z * s);
        d5 = t * (vector.y * vector.y) + c;
        d6 = t * (vector.y * vector.z) + (vector.x * s);
        d7 = 0;
        d8 = t * (vector.z * vector.x) + (vector.y * s);
        d9 = t * (vector.z * vector.y) - (vector.x * s);
        d10 = t * (vector.z * vector.z) + c;
        d11 = 0;
        return this;
    }

    public final Matrix4f identity() {
        d0 = 1.0f;
        d1 = 0;
        d2 = 0;
        d3 = 0;
        d4 = 0;
        d5 = 1.0f;
        d6 = 0;
        d7 = 0;
        d8 = 0;
        d9 = 0;
        d10 = 1.0f;
        d11 = 0;
        d12 = 0;
        d13 = 0;
        d14 = 0;
        d15 = 1.0f;
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

    public Matrix4f setInverse(Matrix3f rotation, Vector3f position) {
        d0 = rotation.xAxis.x;
        d1 = rotation.yAxis.x;
        d2 = rotation.zAxis.x;
        d3 = 0;
        d4 = rotation.xAxis.y;
        d5 = rotation.yAxis.y;
        d6 = rotation.zAxis.y;
        d7 = 0;
        d8 = rotation.xAxis.z;
        d9 = rotation.yAxis.z;
        d10 = rotation.zAxis.z;
        d11 = 0;
        d12 = -rotation.xAxis.dot(position);
        d13 = -rotation.yAxis.dot(position);
        d14 = -rotation.zAxis.dot(position);
        d15 = 1;
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
        d3 = 0;
        d4 = transform.rotation.yAxis.x;
        d5 = transform.rotation.yAxis.y;
        d6 = transform.rotation.yAxis.z;
        d7 = 0;
        d8 = transform.rotation.zAxis.x;
        d9 = transform.rotation.zAxis.y;
        d10 = transform.rotation.zAxis.z;
        d11 = 0;
        d12 = transform.position.x;
        d13 = transform.position.y;
        d14 = transform.position.z;
        d15 = 1;
        return this;
    }

    public Matrix4f setDiagonal(float radius) {
        d0 = radius;
        d5 = radius;
        d10 = radius;
        return this;
    }

    public Matrix4f setFrustum(float left, float right, float bottom, float top, float znear, float zfar) {
        float invdeltax = 1.0f / (right - left);
        float invdeltay = 1.0f / (top - bottom);
        float invdeltaz = 1.0f / (zfar - znear);
        float h = 2.0f * znear * invdeltay;
        setRow(0, 2.0f * znear * invdeltax, 0.0f, (right + left) * invdeltax, 0.0f);
        setRow(1, 0.0f, h, (top + bottom) * invdeltay, 0.0f);
        setRow(2, 0.0f, 0.0f, -(zfar + znear) * invdeltaz, -2.0f * zfar * znear * invdeltaz);
        setRow(3, 0.0f, 0.0f, -1.0f, 0.0f);
        return this;
    }

    public Matrix4f setOrthogonal(float left, float right, float bottom, float top, float zNear, float zFar) {
        float invdeltax = 1.0f / (right - left);
        float invdeltay = 1.0f / (top - bottom);
        float invdeltaz = 1.0f / (zFar - zNear);
        float h = 2.0f * invdeltay;
        setRow(0, 2.0f * invdeltax, 0.0f, 0.0f, -(right + left) * invdeltax);
        setRow(1, 0.0f, h, 0.0f, -(top + bottom) * invdeltay);
        setRow(2, 0.0f, 0.0f, -2.0f * invdeltaz, -(zFar + zNear) * invdeltaz);
        setRow(3, 0.0f, 0.0f, 0.0f, 1.0f);
        return this;
    }

    public Matrix4f setPerspective(float fieldOfViewY, float aspectRatio, float zNear, float zFar) {
        float cotangent = 1.0f / MathHelper.Tan(fieldOfViewY * MathHelper.PI_OVER_360);
        float invdeltaz = 1.0f / (zFar - zNear);
        setRow(0, cotangent / aspectRatio, 0.0f, 0.0f, 0.0f);
        setRow(1, 0.0f, cotangent, 0.0f, 0.0f);
        setRow(2, 0.0f, 0.0f, -(zFar + zNear) * invdeltaz, -2.0f * zNear * zFar * invdeltaz);
        setRow(3, 0.0f, 0.0f, -1.0f, 0.0f);
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
        out.d3 = 0;
        out.d7 = 0;
        out.d11 = 0;
        out.d15 = 1;
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
    public String toString() {
        return "(d0: " + d0 + " d4: " + d4 + " d8: " + d8 + " d12: " + d12 + ")\n"
                + "(d1: " + d1 + " d5: " + d5 + " d9: " + d9 + " d13: " + d13 + ")\n"
                + "(d2: " + d2 + " d6: " + d6 + " d10: " + d10 + " d14: " + d14 + ")\n"
                + "(d3: " + d3 + " d7: " + d7 + " d11: " + d11 + " d15: " + d15 + ")";
    }

}
