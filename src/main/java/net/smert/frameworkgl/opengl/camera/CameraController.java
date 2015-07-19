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
import net.smert.frameworkgl.helpers.InputProcessor;
import net.smert.frameworkgl.helpers.Keyboard;
import net.smert.frameworkgl.math.Vector3f;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class CameraController implements InputProcessor {

    private final static String KEY_ACTION_LOOK_DOWN = "camLookDown";
    private final static String KEY_ACTION_LOOK_UP = "camLookUp";
    private final static String KEY_ACTION_MOVE_BACK = "camMoveBack";
    private final static String KEY_ACTION_MOVE_DOWN = "camMoveDown";
    private final static String KEY_ACTION_MOVE_FORWARD = "camMoveForward";
    private final static String KEY_ACTION_MOVE_LEFT = "camMoveLeft";
    private final static String KEY_ACTION_MOVE_RIGHT = "camMoveRight";
    private final static String KEY_ACTION_MOVE_UP = "camMoveUp";
    private final static String KEY_ACTION_TURN_LEFT = "camTurnLeft";
    private final static String KEY_ACTION_TURN_RIGHT = "camTurnRight";

    private float lookSpeed;
    private float moveSpeed;
    private Camera camera;
    private final Vector3f positionDelta;
    private final Vector3f rotationDelta;

    public CameraController() {
        lookSpeed = 10f;
        moveSpeed = 9f;
        positionDelta = new Vector3f();
        rotationDelta = new Vector3f();
    }

    public float getLookSpeed() {
        return lookSpeed;
    }

    public void setLookSpeed(float lookSpeed) {
        this.lookSpeed = lookSpeed;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void update() {

        float delta = Fw.timer.getDelta();
        positionDelta.zero();

        if (Fw.input.isActionKeyDown(KEY_ACTION_MOVE_BACK)) {
            positionDelta.setZ(-1.0f);
        }
        if (Fw.input.isActionKeyDown(KEY_ACTION_MOVE_FORWARD)) {
            positionDelta.setZ(1.0f);
        }
        if (Fw.input.isActionKeyDown(KEY_ACTION_MOVE_LEFT)) {
            positionDelta.setX(-1.0f);
        }
        if (Fw.input.isActionKeyDown(KEY_ACTION_MOVE_RIGHT)) {
            positionDelta.setX(1.0f);
        }
        if (Fw.input.isActionKeyDown(KEY_ACTION_MOVE_DOWN)) {
            positionDelta.setY(-1.0f);
        }
        if (Fw.input.isActionKeyDown(KEY_ACTION_MOVE_UP)) {
            positionDelta.setY(1.0f);
        }

        if (positionDelta.magnitudeSquared() > 0) {
            positionDelta.normalize();
            positionDelta.multiply(delta * moveSpeed);
            camera.moveForward(positionDelta.getX(), 0, positionDelta.getZ());
            camera.move(0, positionDelta.getY(), 0);
        }

        // Positive rotation delta = counterclockwise rotation
        rotationDelta.zero();

        if (Fw.input.isActionKeyDown(KEY_ACTION_LOOK_DOWN)) {
            rotationDelta.addX(-1.0f);
        }
        if (Fw.input.isActionKeyDown(KEY_ACTION_LOOK_UP)) {
            rotationDelta.addX(1.0f);
        }
        if (Fw.input.isActionKeyDown(KEY_ACTION_TURN_LEFT)) {
            rotationDelta.addY(1.0f);
        }
        if (Fw.input.isActionKeyDown(KEY_ACTION_TURN_RIGHT)) {
            rotationDelta.addY(-1.0f);
        }

        rotationDelta.addX(Fw.input.getDeltaYWithSensitivity());
        rotationDelta.addY(-Fw.input.getDeltaXWithSensitivity());

        // LWJGL will return 0 for mouse movement if the frame rate is higher than 125fps. If we just used the delta
        // amount then the mouse speed would be reduced. MOUSE_POLL is set for 125fps to compensate for this issue.
        // If the frame rate is lower than 125fps then we want the larger delta.
        if (rotationDelta.magnitudeSquared() > 0) {
            float mousePoll = Math.max(delta, Fw.input.MOUSE_POLL);
            rotationDelta.multiply(mousePoll * lookSpeed);
            camera.rotate(rotationDelta.getX(), rotationDelta.getY(), rotationDelta.getZ());
        }
    }

    @Override
    public void registerActions() {
        Fw.input.setAction(KEY_ACTION_LOOK_DOWN, Keyboard.DOWN);
        Fw.input.setAction(KEY_ACTION_LOOK_UP, Keyboard.UP);
        Fw.input.setAction(KEY_ACTION_MOVE_BACK, Keyboard.S);
        Fw.input.setAction(KEY_ACTION_MOVE_DOWN, Keyboard.LEFT_SHIFT);
        Fw.input.setAction(KEY_ACTION_MOVE_FORWARD, Keyboard.W);
        Fw.input.setAction(KEY_ACTION_MOVE_LEFT, Keyboard.A);
        Fw.input.setAction(KEY_ACTION_MOVE_RIGHT, Keyboard.D);
        Fw.input.setAction(KEY_ACTION_MOVE_UP, Keyboard.SPACE);
        Fw.input.setAction(KEY_ACTION_TURN_LEFT, Keyboard.LEFT);
        Fw.input.setAction(KEY_ACTION_TURN_RIGHT, Keyboard.RIGHT);
    }

    @Override
    public void unregisterActions() {
        Fw.input.removeActionKey(KEY_ACTION_LOOK_DOWN);
        Fw.input.removeActionKey(KEY_ACTION_LOOK_UP);
        Fw.input.removeActionKey(KEY_ACTION_MOVE_BACK);
        Fw.input.removeActionKey(KEY_ACTION_MOVE_DOWN);
        Fw.input.removeActionKey(KEY_ACTION_MOVE_FORWARD);
        Fw.input.removeActionKey(KEY_ACTION_MOVE_LEFT);
        Fw.input.removeActionKey(KEY_ACTION_MOVE_RIGHT);
        Fw.input.removeActionKey(KEY_ACTION_MOVE_UP);
        Fw.input.removeActionKey(KEY_ACTION_TURN_LEFT);
        Fw.input.removeActionKey(KEY_ACTION_TURN_RIGHT);
    }

}
