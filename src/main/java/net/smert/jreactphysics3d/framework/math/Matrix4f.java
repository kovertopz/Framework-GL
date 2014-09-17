package net.smert.jreactphysics3d.framework.math;

import java.nio.FloatBuffer;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Matrix4f {

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

    public Matrix4f() {
        d0 = 1.0f;
        d1 = 0.0f;
        d2 = 0.0f;
        d3 = 0.0f;
        d4 = 0.0f;
        d5 = 1.0f;
        d6 = 0.0f;
        d7 = 0.0f;
        d8 = 0.0f;
        d9 = 0.0f;
        d10 = 1.0f;
        d11 = 0.0f;
        d12 = 0.0f;
        d13 = 0.0f;
        d14 = 0.0f;
        d15 = 1.0f;
    }

    public Matrix4f set(Matrix3f r, Vector3f p) {
        d0 = r.xAxis.x;
        d1 = r.yAxis.x;
        d2 = r.zAxis.x;
        d3 = 0.0f;
        d4 = r.xAxis.y;
        d5 = r.yAxis.y;
        d6 = r.zAxis.y;
        d7 = 0.0f;
        d8 = r.xAxis.z;
        d9 = r.yAxis.z;
        d10 = r.zAxis.z;
        d11 = 0.0f;
        d12 = -r.xAxis.dot(p);
        d13 = -r.yAxis.dot(p);
        d14 = -r.zAxis.dot(p);
        d15 = 1.0f;
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

    public Matrix4f setOrthogonal(float left, float right, float bottom, float top, float znear, float zfar) {
        float invdeltax = 1.0f / (right - left);
        float invdeltay = 1.0f / (top - bottom);
        float invdeltaz = 1.0f / (zfar - znear);
        float h = 2.0f * invdeltay;

        setRow(0, 2.0f * invdeltax, 0.0f, 0.0f, -(right + left) * invdeltax);
        setRow(1, 0.0f, h, 0.0f, -(top + bottom) * invdeltay);
        setRow(2, 0.0f, 0.0f, -2.0f * invdeltaz, -(zfar + znear) * invdeltaz);
        setRow(3, 0.0f, 0.0f, 0.0f, 1.0f);

        return this;
    }

    public Matrix4f setPerspective(float fieldofviewy, float aspectratio, float znear, float zfar) {
        float cotangent = 1.0f / MathHelper.Tan(fieldofviewy * MathHelper.PI_OVER_360);
        float invdeltaz = 1.0f / (zfar - znear);

        setRow(0, cotangent / aspectratio, 0.0f, 0.0f, 0.0f);
        setRow(1, 0.0f, cotangent, 0.0f, 0.0f);
        setRow(2, 0.0f, 0.0f, -(zfar + znear) * invdeltaz, -2.0f * znear * zfar * invdeltaz);
        setRow(3, 0.0f, 0.0f, -1.0f, 0.0f);

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
                throw new IllegalArgumentException("Invalid Row!");
        }

        return this;
    }

    public void toFloatBuffer(FloatBuffer fbout) {
        fbout.put(d0);
        fbout.put(d1);
        fbout.put(d2);
        fbout.put(d3);
        fbout.put(d4);
        fbout.put(d5);
        fbout.put(d6);
        fbout.put(d7);
        fbout.put(d8);
        fbout.put(d9);
        fbout.put(d10);
        fbout.put(d11);
        fbout.put(d12);
        fbout.put(d13);
        fbout.put(d14);
        fbout.put(d15);
    }

    @Override
    public String toString() {
        return "Matrix4f:\n"
                + "{" + d0 + " " + d4 + " " + d8 + " " + d12 + "}\n"
                + "{" + d1 + " " + d5 + " " + d9 + " " + d13 + "}\n"
                + "{" + d2 + " " + d6 + " " + d10 + " " + d14 + "}\n"
                + "{" + d3 + " " + d7 + " " + d11 + " " + d15 + "}";
    }

}
