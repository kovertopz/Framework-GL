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
package net.smert.frameworkgl.opengl.camera;

import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.math.MathHelper;
import net.smert.frameworkgl.math.Matrix3f;
import net.smert.frameworkgl.math.Matrix4f;
import net.smert.frameworkgl.math.Ray;
import net.smert.frameworkgl.math.Vector3f;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Camera {

    public final static float MAX_SAFE_PITCH = 90f;
    public final static float MAX_SAFE_ROLL = 30f;

    private boolean invert;
    private float aspectRatio;
    private float fieldOfView;
    private float totalHeading;
    private float totalPitch;
    private float totalRoll;
    private float zFar;
    private float zNear;
    private AbstractFrustumCulling frustumCulling;
    private final Matrix3f movementMatrix;
    private final Matrix3f rotationMatrix;
    private final Matrix3f tempMatrix;
    private final Matrix4f inverseProjectionViewMatrix;
    private final Matrix4f projectionMatrix;
    private final Matrix4f projectionViewMatrix;
    private final Matrix4f viewMatrix;
    private final Vector3f position;
    private final Vector3f viewDirection;

    public Camera() {
        invert = false;
        aspectRatio = 0f;
        fieldOfView = 0f;
        totalHeading = 0f;
        totalPitch = 0f;
        totalRoll = 0f;
        zFar = 0f;
        zNear = 0f;
        movementMatrix = new Matrix3f();
        rotationMatrix = new Matrix3f();
        tempMatrix = new Matrix3f();
        inverseProjectionViewMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();
        projectionViewMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        position = new Vector3f();
        viewDirection = new Vector3f();
    }

    private void updateRotationMatrix() {
        rotationMatrix.orthonormalize();
        viewDirection.setInvert(rotationMatrix.getZAxis());
        totalHeading = rotationMatrix.getHeading();
        totalPitch = rotationMatrix.getPitch();
        totalRoll = rotationMatrix.getRoll();
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public float getFieldOfView() {
        return fieldOfView;
    }

    public float getHeading() {
        return totalHeading;
    }

    public float getPitch() {
        return totalPitch;
    }

    public float getRoll() {
        return totalRoll;
    }

    public float getZFar() {
        return zFar;
    }

    public float getZNear() {
        return zNear;
    }

    public AbstractFrustumCulling getFrustumCulling() {
        return frustumCulling;
    }

    public void setFrustumCulling(AbstractFrustumCulling frustumCulling) {
        this.frustumCulling = frustumCulling;
    }

    public Matrix3f getMovementMatrix() {
        return movementMatrix;
    }

    public Matrix3f getRotationMatrix() {
        return rotationMatrix;
    }

    public Matrix4f getInverseProjectionViewMatrix() {
        return inverseProjectionViewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f getProjectionViewMatrix() {
        return projectionViewMatrix;
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Ray getPickRay(Ray ray, float screenX, float screenY, float viewportX, float viewportY, float viewportWidth,
            float viewportHeight) {
        ray.setOrigin(screenX, screenY, 0f);
        ray.setDirection(screenX, screenY, 1f);
        unproject(ray.getOrigin(), viewportX, viewportY, viewportWidth, viewportHeight);
        unproject(ray.getDirection(), viewportX, viewportY, viewportWidth, viewportHeight);
        ray.getDirection().subtract(ray.getOrigin()).normalize();
        return ray;
    }

    public Vector3f getViewDirection() {
        return viewDirection;
    }

    public boolean isInvert() {
        return invert;
    }

    public void setInvert(boolean invert) {
        this.invert = invert;
    }

    public void lookAt(Vector3f position, Vector3f target, Vector3f up) {
        this.position.set(position);
        rotationMatrix.orthonormalize(position, target, up);
        viewDirection.setInvert(rotationMatrix.getZAxis());
        totalHeading = rotationMatrix.getHeading();
        totalPitch = rotationMatrix.getPitch();
        totalRoll = rotationMatrix.getRoll();
    }

    public void move(float dx, float dy, float dz) {
        position.add(dx, dy, dz);
    }

    public void moveForward(float dx, float dy, float dz) {
        float zDotUp = viewDirection.dot(Vector3f.WORLD_Y_AXIS);

        if ((zDotUp < -MathHelper.TOLERANCE_DOT_PRODUCT_PARALLEL) || (zDotUp > MathHelper.TOLERANCE_DOT_PRODUCT_PARALLEL)) {
            movementMatrix.getZAxis().set(Vector3f.WORLD_Y_AXIS).cross(rotationMatrix.getXAxis()).normalize();
            movementMatrix.getXAxis().set(movementMatrix.getZAxis()).cross(Vector3f.WORLD_Y_AXIS).normalize();
            movementMatrix.getYAxis().set(movementMatrix.getXAxis()).cross(movementMatrix.getZAxis()).normalize();
        } else {
            movementMatrix.getZAxis().set(viewDirection).setY(0).normalize();
            movementMatrix.getXAxis().set(movementMatrix.getZAxis()).cross(Vector3f.WORLD_Y_AXIS).normalize();
            movementMatrix.getYAxis().set(movementMatrix.getXAxis()).cross(movementMatrix.getZAxis()).normalize();
        }

        position.addScaled(movementMatrix.getXAxis(), dx);
        position.addScaled(movementMatrix.getYAxis(), dy);
        position.addScaled(movementMatrix.getZAxis(), dz);
    }

    public void rotate(float pitch, float heading, float roll) {

        if (invert) {
            pitch = -pitch;
        }

        totalPitch += pitch;

        if (totalPitch > MAX_SAFE_PITCH) {
            pitch = MAX_SAFE_PITCH - (totalPitch - pitch);
        }
        if (totalPitch < -MAX_SAFE_PITCH) {
            pitch = -MAX_SAFE_PITCH - (totalPitch - pitch);
        }

        totalRoll += roll;

        if (totalRoll > MAX_SAFE_ROLL) {
            roll = MAX_SAFE_ROLL - (totalRoll - roll);
        }
        if (totalRoll < -MAX_SAFE_ROLL) {
            roll = -MAX_SAFE_ROLL - (totalRoll - roll);
        }

        if (heading != 0f) {
            tempMatrix.fromAxisAngle(Vector3f.WORLD_Y_AXIS, heading);
            tempMatrix.multiplyOut(rotationMatrix.getXAxis(), rotationMatrix.getXAxis());
            tempMatrix.multiplyOut(rotationMatrix.getZAxis(), rotationMatrix.getZAxis());
        }
        if (roll != 0f) {
            tempMatrix.fromAxisAngle(rotationMatrix.getZAxis(), roll);
            tempMatrix.multiplyOut(rotationMatrix.getYAxis(), rotationMatrix.getYAxis());
            tempMatrix.multiplyOut(rotationMatrix.getXAxis(), rotationMatrix.getXAxis());
        }
        if (pitch != 0f) {
            tempMatrix.fromAxisAngle(rotationMatrix.getXAxis(), pitch);
            tempMatrix.multiplyOut(rotationMatrix.getYAxis(), rotationMatrix.getYAxis());
            tempMatrix.multiplyOut(rotationMatrix.getZAxis(), rotationMatrix.getZAxis());
        }

        updateRotationMatrix();
    }

    public void resetRotation() {
        rotationMatrix.setAxes(Vector3f.WORLD_X_AXIS, Vector3f.WORLD_Y_AXIS, Vector3f.WORLD_Z_AXIS);
        updateRotationMatrix();
    }

    public void setFrustum(float left, float right, float bottom, float top, float zNear, float zFar) {
        float invDeltaY = 1f / (top - bottom);
        float h = 2f * zNear * invDeltaY;
        aspectRatio = (right - left) / (top - bottom);
        fieldOfView = MathHelper.ArcTan(1f / h) / MathHelper.PI_OVER_360;
        this.zFar = zFar;
        this.zNear = zNear;
        projectionMatrix.setFrustum(left, right, bottom, top, zNear, zFar);
    }

    public void setOrthogonalProjection(float left, float right, float bottom, float top, float zNear, float zFar) {
        float invDeltaY = 1f / (top - bottom);
        float h = 2f * invDeltaY;
        aspectRatio = 1f;
        fieldOfView = MathHelper.ArcTan(1f / h) / MathHelper.PI_OVER_360;
        this.zFar = zFar;
        this.zNear = zNear;
        projectionMatrix.setOrthogonal(left, right, bottom, top, zNear, zFar);
    }

    public void setPerspectiveProjection(float fieldOfViewY, float aspectRatio, float zNear, float zFar) {
        this.aspectRatio = aspectRatio;
        fieldOfView = fieldOfViewY;
        this.zFar = zFar;
        this.zNear = zNear;
        projectionMatrix.setPerspective(fieldOfViewY, aspectRatio, zNear, zFar);
    }

    public Vector3f unproject(Vector3f windowCoords, float viewportX, float viewportY, float viewportWidth, float viewportHeight) {

        // Map x and y from window coordinates
        float x = (windowCoords.getX() - viewportX) / viewportWidth;
        float y = ((Fw.config.getCurrentHeight() - windowCoords.getY()) - viewportY) / viewportHeight;
        float z = windowCoords.getZ();

        // Map to range -1 to 1
        x = x * 2f - 1f;
        y = y * 2f - 1f;
        z = z * 2f - 1f;
        windowCoords.set(x, y, z);

        // Translate back into world coordinates
        inverseProjectionViewMatrix.multiplyProjectionOut(windowCoords, windowCoords);

        return windowCoords;
    }

    public void update() {
        viewMatrix.setInverse(rotationMatrix, position);
        projectionMatrix.projectionMultiplyViewOut(viewMatrix, projectionViewMatrix);
        inverseProjectionViewMatrix.setInverse(projectionViewMatrix);
    }

    public void updatePlanes() {
        frustumCulling.updatePlanes(projectionMatrix, viewMatrix);
    }

}
