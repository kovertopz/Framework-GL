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

import java.util.ArrayList;
import java.util.List;
import net.smert.frameworkgl.Fw;
import org.lwjgl.glfw.GLFW;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class MouseHelper {

    private final static int GLFW_MOUSE_BUTTON_MOVE = GLFW.GLFW_MOUSE_BUTTON_LAST + 1;
    private final static int MOUSE_SIZE = GLFW.GLFW_MOUSE_BUTTON_LAST + 2; // Extra for mouse move

    private boolean isGrabbed;
    private final boolean[] isDown;
    private final boolean[] nextState;
    private final boolean[] wasDown;
    private float deltaWheelWithSensitivity;
    private float deltaXWithSensitivity;
    private float deltaYWithSensitivity;
    private int deltaWheel;
    private int deltaX;
    private int deltaY;
    private int mouseRawX;
    private int mouseRawY;
    private int mouseX;
    private int mouseY;
    private final int[] lwjglToMouse;
    private final List<MouseEvent> mouseEvents;

    public MouseHelper() {
        isDown = new boolean[Mouse.MAX_MOUSE.ordinal()];
        nextState = new boolean[Mouse.MAX_MOUSE.ordinal()];
        wasDown = new boolean[Mouse.MAX_MOUSE.ordinal()];
        lwjglToMouse = new int[MOUSE_SIZE];
        mouseEvents = new ArrayList<>();
    }

    private void clampMouseXY() {
        if (mouseX < 0) {
            mouseX = 0;
        } else if (mouseX > Fw.config.getCurrentWidth()) {
            mouseX = Fw.config.getCurrentWidth();
        }
        if (mouseY < 0) {
            mouseY = 0;
        } else if (mouseY > Fw.config.getCurrentHeight()) {
            mouseY = Fw.config.getCurrentHeight();
        }
    }

    private void mapLwglToMouse() {

        // Map to none
        for (int i = 0; i < MOUSE_SIZE; i++) {
            lwjglToMouse[i] = Mouse.NONE.ordinal();
        }

        lwjglToMouse[GLFW.GLFW_MOUSE_BUTTON_1] = Mouse.LEFT.ordinal();
        lwjglToMouse[GLFW.GLFW_MOUSE_BUTTON_2] = Mouse.RIGHT.ordinal();
        lwjglToMouse[GLFW.GLFW_MOUSE_BUTTON_3] = Mouse.MIDDLE.ordinal();
        lwjglToMouse[GLFW.GLFW_MOUSE_BUTTON_4] = Mouse.BACK.ordinal();
        lwjglToMouse[GLFW.GLFW_MOUSE_BUTTON_5] = Mouse.FORWARD.ordinal();
        lwjglToMouse[GLFW.GLFW_MOUSE_BUTTON_6] = Mouse.NONE.ordinal(); // Unknown
        lwjglToMouse[GLFW.GLFW_MOUSE_BUTTON_7] = Mouse.NONE.ordinal(); // Unknown
        lwjglToMouse[GLFW.GLFW_MOUSE_BUTTON_8] = Mouse.NONE.ordinal(); // Unknown
        lwjglToMouse[GLFW_MOUSE_BUTTON_MOVE] = Mouse.MOVE.ordinal();
    }

    public void addEvent(int button, int modifiers, boolean state) {
        MouseEvent event = new MouseEvent();
        event.button = button;
        event.deltaWheel = deltaWheel;
        event.deltaX = deltaX;
        event.deltaY = deltaY;
        event.mappedButton = lwjglToMouse[event.button];
        event.modifiers = modifiers;
        event.mouseX = mouseX;
        event.mouseY = mouseY;
        event.state = state;
        mouseEvents.add(event);
        nextState[event.mappedButton] = event.state;
    }

    public void centerCursor() {
        setCursorPosition(Fw.config.getCurrentWidth() / 2, Fw.config.getCurrentHeight() / 2);
    }

    public void clearDelta() {
        deltaWheelWithSensitivity = 0;
        deltaXWithSensitivity = 0;
        deltaYWithSensitivity = 0;
        deltaWheel = 0;
        deltaX = 0;
        deltaY = 0;
    }

    public void clearEvents() {
        nextState[Mouse.MOVE.ordinal()] = false;
        nextState[Mouse.WHEEL_DOWN.ordinal()] = false;
        nextState[Mouse.WHEEL_UP.ordinal()] = false;
        clearDelta();
        mouseEvents.clear();
    }

    public void clearNextState() {
        for (int i = 0; i < Mouse.MAX_MOUSE.ordinal(); i++) {
            nextState[i] = false;
        }
    }

    public int getDeltaWheel() {
        return deltaWheel;
    }

    public float getDeltaWheelWithSensitivity() {
        return deltaWheelWithSensitivity;
    }

    public int getDeltaX() {
        return deltaX;
    }

    public float getDeltaXWithSensitivity() {
        return deltaXWithSensitivity;
    }

    public int getDeltaY() {
        return deltaY;
    }

    public float getDeltaYWithSensitivity() {
        return deltaYWithSensitivity;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public void setCursorPosition(int x, int y) {
        mouseRawX = mouseX = x;
        mouseRawY = mouseY = y;
        clampMouseXY();
        GLFW.glfwSetCursorPos(Fw.window.getWindow(), mouseX, mouseY);
    }

    public List<MouseEvent> getMouseEvents() {
        return mouseEvents;
    }

    public boolean isButtonDown(int button) {
        if ((button < 0) || (button >= isDown.length)) {
            throw new IllegalArgumentException("Invalid button: " + button);
        }
        return isDown[button];
    }

    public boolean wasButtonDown(int button) {
        if ((button < 0) || (button >= wasDown.length)) {
            throw new IllegalArgumentException("Invalid button: " + button);
        }
        return wasDown[button];
    }

    public boolean isButtonDown(Mouse mouse) {
        return isDown[mouse.ordinal()];
    }

    public boolean wasButtonDown(Mouse mouse) {
        return wasDown[mouse.ordinal()];
    }

    public boolean isGrabbed() {
        return isGrabbed;
    }

    public void grabMouseCursor() {
        isGrabbed = true;
        centerCursor();
        updateInputMode();
    }

    public void handleMoveEvent(double x, double y) {
        int newX = (int) x;
        int newY = (int) y;
        deltaX = (newX - mouseRawX);
        deltaY = -(newY - mouseRawY);
        deltaXWithSensitivity = deltaX * Fw.config.getMouseMoveSensitivity();
        deltaYWithSensitivity = deltaY * Fw.config.getMouseMoveSensitivity();
        mouseRawX = newX;
        mouseRawY = newY;
        mouseX += deltaX;
        mouseY += deltaY;
        clampMouseXY();
        float totalDelta = deltaX + deltaY;
        nextState[Mouse.MOVE.ordinal()] = (totalDelta != 0);
    }

    public void handleScrollEvent(double xoffset, double yoffset) {
        deltaWheel = (int) yoffset;
        deltaWheelWithSensitivity = deltaWheel * Fw.config.getMouseWheelSensitivity();
        if (deltaWheel < 0) {
            nextState[Mouse.WHEEL_DOWN.ordinal()] = true;
        } else if (deltaWheel > 0) {
            nextState[Mouse.WHEEL_UP.ordinal()] = true;
        }
    }

    public void init() {
        mapLwglToMouse();
        reset();
    }

    public void releaseMouseCursor() {
        isGrabbed = false;
        centerCursor();
        updateInputMode();
    }

    public void reset() {
        for (int i = 0; i < Mouse.MAX_MOUSE.ordinal(); i++) {
            isDown[i] = false;
            nextState[i] = false;
            wasDown[i] = false;
        }
    }

    public void update() {
        for (int i = 0; i < MOUSE_SIZE; i++) {
            wasDown[i] = isDown[i]; // Save last frame
            isDown[i] = nextState[i]; // Set current frame
        }
    }

    public void updateInputMode() {
        int value;
        if (isGrabbed) {
            value = GLFW.GLFW_CURSOR_DISABLED;
        } else {
            value = GLFW.GLFW_CURSOR_NORMAL;
        }
        GLFW.glfwSetInputMode(Fw.window.getWindow(), GLFW.GLFW_CURSOR, value);
    }

    public static class MouseEvent {

        /**
         * The state indicates that the button has been pressed when true
         */
        public boolean state;

        /**
         * The raw button from LWJGL
         */
        public int button;

        /**
         * The raw delta mouse wheel change without getMouseWheelSensitivity applied.
         */
        public int deltaWheel;

        /**
         * The raw delta X position change without getMouseMoveSensitivity applied.
         */
        public int deltaX;

        /**
         * The raw delta Y position change without getMouseMoveSensitivity applied.
         */
        public int deltaY;

        /**
         * The mapped button to the framework's mouse
         */
        public int mappedButton;

        /**
         * Bitfield describing which modifiers keys were held down
         */
        public int modifiers;

        /**
         * The mouse X position associated with the event
         */
        public int mouseX;

        /**
         * The mouse Y position associated with the event
         */
        public int mouseY;

    }

}
