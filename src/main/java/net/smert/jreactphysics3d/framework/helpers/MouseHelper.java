package net.smert.jreactphysics3d.framework.helpers;

import net.smert.jreactphysics3d.framework.Configuration;

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

    private boolean grabbed = false;
    private boolean[] isDown;
    private boolean[] nextState;
    private boolean[] wasDown;
    private float deltaWheel = 0.0f;
    private float deltaX = 0.0f;
    private float deltaY = 0.0f;
    private int buttonCount;
    private int[] lwjglToMouse;
    private final Configuration config;

    public MouseHelper(Configuration config) {
        this.config = config;
    }

    private void centerCursor() {
        org.lwjgl.input.Mouse.setCursorPosition(config.getCurrentWidth() / 2, config.getCurrentHeight() / 2);
        org.lwjgl.input.Mouse.setGrabbed(grabbed);
    }

    private void clearDelta() {
        deltaWheel = 0.0f;
        deltaX = 0.0f;
        deltaY = 0.0f;
    }

    private int mapLwglToArray(int button) {
        if (button == -1) {
            return 0;
        }
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
            wasDown[i] = isDown[i];
            isDown[i] = nextState[i];
        }
    }

    private void updateWheelChange() {
        deltaWheel = org.lwjgl.input.Mouse.getDWheel();
        deltaWheel *= config.getMouseWheelSensitivity();

        if (deltaWheel == 0) {
            nextState[mapLwglToArray(LWJGL_MOUSE_WHEEL_UP)] = false;
            nextState[mapLwglToArray(LWJGL_MOUSE_WHEEL_DOWN)] = false;
        } else if (deltaWheel > 0) {
            nextState[mapLwglToArray(LWJGL_MOUSE_WHEEL_UP)] = true;
        } else {
            nextState[mapLwglToArray(LWJGL_MOUSE_WHEEL_DOWN)] = true;
        }
    }

    private void updateXYChange() {
        deltaX = org.lwjgl.input.Mouse.getDX();
        deltaY = org.lwjgl.input.Mouse.getDY();
        deltaX *= config.getMouseMoveSensitivity();
        deltaY *= config.getMouseMoveSensitivity();
        float totalDelta = deltaX + deltaY;
        nextState[mapLwglToArray(LWJGL_MOUSE_MOVE)] = (totalDelta != 0);
    }

    public boolean getGrabbed() {
        return grabbed;
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
        grabbed = true;
        centerCursor();
    }

    public void init() {
        buttonCount = org.lwjgl.input.Mouse.getButtonCount();
        int maxButtons = Mouse.MAX_MOUSE.ordinal();
        isDown = new boolean[maxButtons];
        nextState = new boolean[maxButtons];
        wasDown = new boolean[maxButtons];
        lwjglToMouse = new int[maxButtons];

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

    public void releaseMouseCursor() {
        clearDelta();
        grabbed = false;
        centerCursor();
    }

    public void update() {
        while (org.lwjgl.input.Mouse.next() == true) {
            int eventButton = org.lwjgl.input.Mouse.getEventButton();
            if (eventButton != LWJGL_MOUSE_MOVE) {
                int button = lwjglToMouse[mapLwglToArray(eventButton)];
                nextState[button] = org.lwjgl.input.Mouse.getEventButtonState();
            }
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
