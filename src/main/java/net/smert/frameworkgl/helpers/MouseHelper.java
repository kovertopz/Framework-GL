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

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import net.smert.frameworkgl.Fw;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;

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
    private int mouseX;
    private int mouseY;
    private int[] lwjglToMouse;
    private final List<MouseEvent> mouseEvents;

    public MouseHelper() {
        isGrabbed = false;
        mouseEvents = new ArrayList<>();
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
        mouseX = org.lwjgl.input.Mouse.getX();
        mouseY = org.lwjgl.input.Mouse.getY();
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

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public void grabMouseCursor() {
        clearDelta();
        isGrabbed = true;
        centerCursor();
    }

    public List<MouseEvent> getMouseEvents() {
        return mouseEvents;
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

    public void setCursorPosition(int x, int y) {
        org.lwjgl.input.Mouse.setCursorPosition(x, y);
    }

    public void setNativeCursor(Cursor cursor) {
        try {
            org.lwjgl.input.Mouse.setNativeCursor(cursor);
        } catch (LWJGLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void update() {

        // Clear last frames events
        mouseEvents.clear();

        // Handle queued events
        while (org.lwjgl.input.Mouse.next()) {
            MouseEvent event = new MouseEvent();
            event.button = org.lwjgl.input.Mouse.getEventButton();
            event.deltaWheel = org.lwjgl.input.Mouse.getEventDWheel();
            event.deltaX = org.lwjgl.input.Mouse.getEventDX();
            event.deltaY = org.lwjgl.input.Mouse.getEventDY();
            event.mappedButton = lwjglToMouse[mapLwglToArray(event.button)];
            event.mouseX = org.lwjgl.input.Mouse.getEventX();
            event.mouseY = org.lwjgl.input.Mouse.getEventY();
            event.state = org.lwjgl.input.Mouse.getEventButtonState();
            mouseEvents.add(event);

            // LWJGL_MOUSE_MOVE is handled in updateXYChange()
            if (event.button != LWJGL_MOUSE_MOVE) {
                nextState[event.mappedButton] = event.state;
            }
        }

        // Update states
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

    public static class MouseCursor {

        private Cursor cursor;

        public void create(int width, int height, int xHotspot, int yHotspot, int numImages, IntBuffer images,
                IntBuffer delays) {
            try {
                cursor = new Cursor(width, height, xHotspot, yHotspot, numImages, images, delays);
            } catch (LWJGLException ex) {
                throw new RuntimeException(ex);
            }
        }

        public Cursor getCursor() {
            return cursor;
        }

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
         * The mouse X position associated with the event
         */
        public int mouseX;

        /**
         * The mouse Y position associated with the event
         */
        public int mouseY;

    }

}
