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
package net.smert.frameworkgl.helpers;

import net.smert.frameworkgl.Fw;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class MouseHelper {

    private final static int LWJGL_MOUSE_MOVE = -1;
    private final static int LWJGL_MOUSE_LEFT = 0;
    private final static int LWJGL_MOUSE_RIGHT = 1;
    private final static int LWJGL_MOUSE_MIDDLE = 2;
    private final static int LWJGL_MOUSE_BACK = 3;
    private final static int LWJGL_MOUSE_FORWARD = 4;
    private final static int LWJGL_MOUSE_WHEEL_UP = 5;
    private final static int LWJGL_MOUSE_WHEEL_DOWN = 6;

    private boolean isGrabbed;
    private boolean[] isDown;
    private boolean[] nextState;
    private boolean[] wasDown;
    private float deltaWheel;
    private float deltaX;
    private float deltaY;
    private int buttonCount;
    private int[] lwjglToMouse;

    public MouseHelper() {
        isGrabbed = false;
        clearDelta();
    }

    private void centerCursor() {
        org.lwjgl.input.Mouse.setCursorPosition(Fw.config.getCurrentWidth() / 2, Fw.config.getCurrentHeight() / 2);
        org.lwjgl.input.Mouse.setGrabbed(isGrabbed);
    }

    private void clearDelta() {
        deltaWheel = 0f;
        deltaX = 0f;
        deltaY = 0f;
    }

    private int mapLwglToArray(int button) {
        return button + 1;
    }

    private void mapLwglToMouse() {
        lwjglToMouse[mapLwglToArray(LWJGL_MOUSE_MOVE)] = Mouse.MOVE.ordinal();
        lwjglToMouse[mapLwglToArray(LWJGL_MOUSE_LEFT)] = Mouse.LEFT.ordinal();
        lwjglToMouse[mapLwglToArray(LWJGL_MOUSE_RIGHT)] = Mouse.RIGHT.ordinal();
        lwjglToMouse[mapLwglToArray(LWJGL_MOUSE_MIDDLE)] = Mouse.MIDDLE.ordinal();
        lwjglToMouse[mapLwglToArray(LWJGL_MOUSE_BACK)] = Mouse.BACK.ordinal();
        lwjglToMouse[mapLwglToArray(LWJGL_MOUSE_FORWARD)] = Mouse.FORWARD.ordinal();
        lwjglToMouse[mapLwglToArray(LWJGL_MOUSE_WHEEL_UP)] = Mouse.WHEEL_UP.ordinal();
        lwjglToMouse[mapLwglToArray(LWJGL_MOUSE_WHEEL_DOWN)] = Mouse.WHEEL_DOWN.ordinal();
    }

    private void updateButtonState() {
        for (int i = 0; i < buttonCount; i++) {
            wasDown[i] = isDown[i]; // Save last frame
            isDown[i] = nextState[i]; // Set current frame
        }
    }

    private void updateWheelChange() {
        deltaWheel = org.lwjgl.input.Mouse.getDWheel();
        deltaWheel *= Fw.config.getMouseWheelSensitivity();

        if (deltaWheel == 0f) {
            nextState[mapLwglToArray(LWJGL_MOUSE_WHEEL_UP)] = false;
            nextState[mapLwglToArray(LWJGL_MOUSE_WHEEL_DOWN)] = false;
        } else if (deltaWheel > 0f) {
            nextState[mapLwglToArray(LWJGL_MOUSE_WHEEL_UP)] = true;
        } else {
            nextState[mapLwglToArray(LWJGL_MOUSE_WHEEL_DOWN)] = true;
        }
    }

    private void updateXYChange() {
        deltaX = org.lwjgl.input.Mouse.getDX();
        deltaY = org.lwjgl.input.Mouse.getDY();
        deltaX *= Fw.config.getMouseMoveSensitivity();
        deltaY *= Fw.config.getMouseMoveSensitivity();
        float totalDelta = deltaX + deltaY;
        nextState[mapLwglToArray(LWJGL_MOUSE_MOVE)] = (totalDelta != 0f);
    }

    public float getDeltaWheel() {
        return deltaWheel;
    }

    public float getDeltaX() {
        return deltaX;
    }

    public float getDeltaY() {
        return deltaY;
    }

    public int getButtonCount() {
        return buttonCount;
    }

    public void grabMouseCursor() {
        clearDelta();
        isGrabbed = true;
        centerCursor();
    }

    public void init() {
        buttonCount = org.lwjgl.input.Mouse.getButtonCount();
        int maxButtons = Mouse.MAX_MOUSE.ordinal();
        isDown = new boolean[maxButtons];
        nextState = new boolean[maxButtons];
        wasDown = new boolean[maxButtons];
        lwjglToMouse = new int[maxButtons];

        // Set defaults
        for (int i = 0; i < maxButtons; i++) {
            isDown[i] = false;
            nextState[i] = false;
            wasDown[i] = false;
            lwjglToMouse[i] = 0;
        }

        mapLwglToMouse();
    }

    public boolean isButtonDown(int button) {
        if ((button < 0) || (button >= isDown.length)) {
            throw new IllegalArgumentException("Invalid button");
        }
        return isDown[button];
    }

    public boolean isButtonDown(Mouse mouse) {
        return isDown[mouse.ordinal()];
    }

    public boolean isGrabbed() {
        return isGrabbed;
    }

    public void releaseMouseCursor() {
        clearDelta();
        isGrabbed = false;
        centerCursor();
    }

    public void update() {

        // Handle queued events
        while (org.lwjgl.input.Mouse.next()) {
            int eventButton = org.lwjgl.input.Mouse.getEventButton();
            if (eventButton == LWJGL_MOUSE_MOVE) {
                continue;
            }
            int button = lwjglToMouse[mapLwglToArray(eventButton)];
            nextState[button] = org.lwjgl.input.Mouse.getEventButtonState();
        }
        updateWheelChange();
        updateXYChange();
        updateButtonState();
    }

    public boolean wasButtonDown(int button) {
        if ((button < 0) || (button >= wasDown.length)) {
            throw new IllegalArgumentException("Invalid button");
        }
        return wasDown[button];
    }

    public boolean wasButtonDown(Mouse mouse) {
        return wasDown[mouse.ordinal()];
    }

}
