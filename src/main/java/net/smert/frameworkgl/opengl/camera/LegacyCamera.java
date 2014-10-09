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

import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.opengl.GL;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class LegacyCamera {

    private final Vector3f rotation; // Euler angles
    private final Vector3f position;

    public LegacyCamera() {
        rotation = new Vector3f();
        position = new Vector3f();
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
    }

    public void setPositionX(float x) {
        position.setX(x);
    }

    public void setPositionY(float y) {
        position.setY(y);
    }

    public void setPositionZ(float z) {
        position.setZ(z);
    }

    public void setRotation(float x, float y, float z) {
        rotation.set(x, y, z);
    }

    public void setRotationX(float x) {
        rotation.setX(x);
    }

    public void setRotationY(float y) {
        rotation.setY(y);
    }

    public void setRotationZ(float z) {
        rotation.setZ(z);
    }

    public void updateOpenGL() {
        // Positive rotation turns clockwise...which is counterintuitive since OpenGL uses the right-hand rule
        GL.o1.rotate(rotation.getX(), 1f, 0f, 0f); // Positive CW
        GL.o1.rotate(360f - rotation.getY(), 0f, 1f, 0f); // Positive CW
        GL.o1.rotate(rotation.getZ(), 0f, 0f, 1f); // Positive CW
        GL.o1.translate(-position.getX(), -position.getY(), -position.getZ());
    }

}
