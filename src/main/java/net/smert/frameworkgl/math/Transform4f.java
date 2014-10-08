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
public class Transform4f {

    final Matrix3f rotation = new Matrix3f();
    final Vector3f position = new Vector3f();

    // Constructors
    public Transform4f() {
        rotation.identity();
    }

    public Transform4f(Matrix3f matrix, Vector3f vector) {
        set(matrix, vector);
    }

    public Transform4f(Transform4f transform) {
        set(transform.rotation, transform.position);
    }

    // Conversion Operations
    public void fromColumnArray(float[] in) {
        rotation.xAxis.x = in[0];
        rotation.xAxis.y = in[1];
        rotation.xAxis.z = in[2];
        rotation.yAxis.x = in[3];
        rotation.yAxis.y = in[4];
        rotation.yAxis.z = in[5];
        rotation.zAxis.x = in[6];
        rotation.zAxis.y = in[7];
        rotation.zAxis.z = in[8];
        position.x = in[9];
        position.y = in[10];
        position.z = in[11];
    }

    public void fromOpenGLArray(float[] in) {
        rotation.xAxis.x = in[0];
        rotation.xAxis.y = in[1];
        rotation.xAxis.z = in[2];
        rotation.yAxis.x = in[4];
        rotation.yAxis.y = in[5];
        rotation.yAxis.z = in[6];
        rotation.zAxis.x = in[8];
        rotation.zAxis.y = in[9];
        rotation.zAxis.z = in[10];
        position.x = in[12];
        position.y = in[13];
        position.z = in[14];
    }

    public void fromRowArray(float[] in) {
        rotation.xAxis.x = in[0];
        rotation.yAxis.x = in[1];
        rotation.zAxis.x = in[2];
        rotation.xAxis.y = in[3];
        rotation.yAxis.y = in[4];
        rotation.zAxis.y = in[5];
        rotation.xAxis.z = in[6];
        rotation.yAxis.z = in[7];
        rotation.zAxis.z = in[8];
        position.x = in[9];
        position.y = in[10];
        position.z = in[11];
    }

    public void toFloatBuffer(FloatBuffer fbOut) {
        fbOut.put(rotation.xAxis.x);
        fbOut.put(rotation.xAxis.y);
        fbOut.put(rotation.xAxis.z);
        fbOut.put(0f);
        fbOut.put(rotation.yAxis.x);
        fbOut.put(rotation.yAxis.y);
        fbOut.put(rotation.yAxis.z);
        fbOut.put(0f);
        fbOut.put(rotation.zAxis.x);
        fbOut.put(rotation.zAxis.y);
        fbOut.put(rotation.zAxis.z);
        fbOut.put(0f);
        fbOut.put(position.x);
        fbOut.put(position.y);
        fbOut.put(position.z);
        fbOut.put(1f);
    }

    public void toColumnArray(float[] out) {
        out[0] = rotation.xAxis.x;
        out[1] = rotation.xAxis.y;
        out[2] = rotation.xAxis.z;
        out[3] = rotation.yAxis.x;
        out[4] = rotation.yAxis.y;
        out[5] = rotation.yAxis.z;
        out[6] = rotation.zAxis.x;
        out[7] = rotation.zAxis.y;
        out[8] = rotation.zAxis.z;
        out[9] = position.x;
        out[10] = position.y;
        out[11] = position.z;
    }

    public void toOpenGLArray(float[] out) {
        out[0] = rotation.xAxis.x;
        out[1] = rotation.xAxis.y;
        out[2] = rotation.xAxis.z;
        out[3] = 0f;
        out[4] = rotation.yAxis.x;
        out[5] = rotation.yAxis.y;
        out[6] = rotation.yAxis.z;
        out[7] = 0f;
        out[8] = rotation.zAxis.x;
        out[9] = rotation.zAxis.y;
        out[10] = rotation.zAxis.z;
        out[11] = 0f;
        out[12] = position.x;
        out[13] = position.y;
        out[14] = position.z;
        out[15] = 1f;
    }

    public void toRowArray(float[] out) {
        out[0] = rotation.xAxis.x;
        out[1] = rotation.yAxis.x;
        out[2] = rotation.zAxis.x;
        out[3] = rotation.xAxis.y;
        out[4] = rotation.yAxis.y;
        out[5] = rotation.zAxis.y;
        out[6] = rotation.xAxis.z;
        out[7] = rotation.yAxis.z;
        out[8] = rotation.zAxis.z;
        out[9] = position.x;
        out[10] = position.y;
        out[11] = position.z;
    }

    // Matrix Operations
    public Matrix3f getRotation() {
        return rotation;
    }

    // Transform Operations
    public Transform4f fromAxisAngle(Vector3f vector, float degrees) {
        rotation.fromAxisAngle(vector, degrees);
        return this;
    }

    public Transform4f multiply(Transform4f transform) {
        position.set(
                rotation.dotRow(0, transform.position) + position.x,
                rotation.dotRow(1, transform.position) + position.y,
                rotation.dotRow(2, transform.position) + position.z);
        rotation.multiply(transform.rotation);
        return this;
    }

    public Transform4f multiplyTranspose(Transform4f transform) {
        position.set(
                transform.position.x - position.x,
                transform.position.y - position.y,
                transform.position.z - position.z);
        rotation.multiplyTransposeOut(position, position);
        rotation.multiplyTranspose(transform.rotation);
        return this;
    }

    public Transform4f set(Transform4f transform) {
        rotation.set(transform.rotation);
        position.set(transform.position);
        return this;
    }

    public final Transform4f set(Matrix3f rotation, Vector3f position) {
        this.rotation.set(rotation);
        this.position.set(position);
        return this;
    }

    public Transform4f setScale(float radius) {
        rotation.setDiagonal(radius);
        return this;
    }

    public Transform4f setPosition(float x, float y, float z) {
        this.position.set(x, y, z);
        return this;
    }

    public Transform4f setPosition(Vector3f position) {
        this.position.set(position);
        return this;
    }

    public Transform4f setRotation(Matrix3f rotation) {
        this.rotation.set(rotation);
        return this;
    }

    public Transform4f setRotation(Quaternion4f quaternion) {
        quaternion.toMatrix3(rotation);
        return this;
    }

    // Vector Operations
    public Vector3f getAxis(int axis) {
        switch (axis) {
            case 3:
                return position;
            default:
                return rotation.getAxis(axis);
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f multiplyOut(Vector3f vector, Vector3f out) {
        rotation.multiplyOut(vector, out);
        out.add(position);
        return out;
    }

    public Vector3f multiplyTransposeOut(Vector3f vector, Vector3f out) {
        out.set(vector).subtract(position);
        rotation.multiplyTransposeOut(out, out);
        return out;
    }

    @Override
    public String toString() {
        return "(position: " + position + " rotation: " + rotation + ")";
    }

}
