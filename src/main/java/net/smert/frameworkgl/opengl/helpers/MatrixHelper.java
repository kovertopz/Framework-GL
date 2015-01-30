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
package net.smert.frameworkgl.opengl.helpers;

import java.util.Stack;
import net.smert.frameworkgl.math.Matrix4f;
import net.smert.frameworkgl.math.Transform4f;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.utils.ThreadLocalVars;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class MatrixHelper {

    public final static int MATRIX_MODEL = 0;
    public final static int MATRIX_PROJECTION = 1;
    public final static int MATRIX_VIEW = 2;

    private int mode;
    private final Stack<Matrix4f> modelMatrixStack;
    private final Stack<Matrix4f> projectionMatrixStack;
    private final Stack<Matrix4f> viewMatrixStack;

    public MatrixHelper() {
        modelMatrixStack = new Stack<>();
        projectionMatrixStack = new Stack<>();
        viewMatrixStack = new Stack<>();
        reset();
    }

    private Stack<Matrix4f> getCurrentStack() {
        switch (mode) {
            case MATRIX_MODEL:
                return modelMatrixStack;
            case MATRIX_PROJECTION:
                return projectionMatrixStack;
            case MATRIX_VIEW:
                return viewMatrixStack;
            default:
                throw new IllegalArgumentException("Invalid matrix mode: " + mode);
        }
    }

    public int getMode() {
        return mode;
    }

    public void setModeModel() {
        mode = MATRIX_MODEL;
    }

    public void setModeProjection() {
        mode = MATRIX_PROJECTION;
    }

    public void setModeView() {
        mode = MATRIX_VIEW;
    }

    public Matrix4f getCurrentMatrix() {
        return getCurrentStack().peek();
    }

    public Matrix4f getModelMatrix() {
        return modelMatrixStack.peek();
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrixStack.peek();
    }

    public Matrix4f getViewMatrix() {
        return viewMatrixStack.peek();
    }

    public void load(Matrix4f matrix) {
        getCurrentMatrix().set(matrix);
    }

    public void load(Transform4f transform) {
        getCurrentMatrix().set(transform);
    }

    public void loadIdentity() {
        Matrix4f matrix = getCurrentMatrix();
        matrix.identity();
    }

    public void multiplyProjectionAndViewMatrix(Matrix4f out) {
        projectionMatrixStack.peek().projectionMultiplyViewOut(viewMatrixStack.peek(), out);
    }

    public void multiplyProjectionAndViewModelMatrix(Matrix4f viewModelMatrix, Matrix4f out) {
        projectionMatrixStack.peek().projectionMultiplyViewOut(viewModelMatrix, out);
    }

    public void multiplyViewAndModelMatrix(Matrix4f out) {
        viewMatrixStack.peek().viewMultiplyModelOut(modelMatrixStack.peek(), out);
    }

    public void pop() {
        Stack<Matrix4f> stack = getCurrentStack();
        if (stack.size() == 1) {
            throw new IllegalStateException("The last matrix in the stack cannot be popped.");
        }
        getCurrentStack().pop();
    }

    public void push() {
        Stack<Matrix4f> stack = getCurrentStack();
        stack.push(new Matrix4f(stack.peek()));
    }

    public final void reset() {
        mode = MATRIX_MODEL;
        modelMatrixStack.clear();
        modelMatrixStack.push(new Matrix4f());
        projectionMatrixStack.clear();
        projectionMatrixStack.push(new Matrix4f());
        viewMatrixStack.clear();
        viewMatrixStack.push(new Matrix4f());
    }

    public void rotate(float degrees, float x, float y, float z) {

        // Temp vars from thread local storage
        ThreadLocalVars vars = ThreadLocalVars.Get();
        Vector3f vector = vars.v3f0;

        Matrix4f matrix = getCurrentMatrix();
        vector.set(x, y, z);
        matrix.fromAxisAngle(vector, degrees);

        // Release vars instance
        vars.release();
    }

    public void setFrustum(float left, float right, float bottom, float top, float zNear, float zFar) {
        Matrix4f matrix = getCurrentMatrix();
        matrix.setFrustum(left, right, bottom, top, zNear, zFar);
    }

    public void setOrthogonal(float left, float right, float bottom, float top, float zNear, float zFar) {
        Matrix4f matrix = getCurrentMatrix();
        matrix.setOrthogonal(left, right, bottom, top, zNear, zFar);
    }

    public void setPerspective(float fieldOfViewY, float aspectRatio, float zNear, float zFar) {
        Matrix4f matrix = getCurrentMatrix();
        matrix.setPerspective(fieldOfViewY, aspectRatio, zNear, zFar);
    }

    public void scale(float x, float y, float z) {

        // Temp vars from thread local storage
        ThreadLocalVars vars = ThreadLocalVars.Get();
        Matrix4f scale = vars.m4f0;

        Matrix4f matrix = getCurrentMatrix();
        scale.setDiagonal(x, y, z);
        matrix.multiply(scale);

        // Release vars instance
        vars.release();
    }

    public void translate(float x, float y, float z) {

        // Temp vars from thread local storage
        ThreadLocalVars vars = ThreadLocalVars.Get();
        Vector3f vector = vars.v3f0;

        Matrix4f matrix = getCurrentMatrix();
        vector.set(x, y, z);
        matrix.multiply(vector);

        // Release vars instance
        vars.release();
    }

}
