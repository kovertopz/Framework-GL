package net.smert.jreactphysics3d.framework;

import net.smert.jreactphysics3d.framework.helpers.Keyboard;
import net.smert.jreactphysics3d.framework.helpers.KeyboardHelper;
import net.smert.jreactphysics3d.framework.helpers.Mouse;
import net.smert.jreactphysics3d.framework.helpers.MouseHelper;
import net.smert.jreactphysics3d.framework.utils.HashMapStringInt;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Input {

    private final Configuration config;
    private final KeyboardHelper keyboardHelper;
    private final HashMapStringInt actionToKey;
    private final HashMapStringInt actionToMouse;
    private final MouseHelper mouseHelper;

    public Input(Configuration config) {
        this.config = config;
        this.keyboardHelper = new KeyboardHelper();
        actionToKey = new HashMapStringInt();
        actionToMouse = new HashMapStringInt();
        this.mouseHelper = new MouseHelper(config);
    }

    public void clearActions() {
        actionToKey.clear();
        actionToMouse.clear();
    }

    public void clearKeyActions() {
        actionToKey.clear();
    }

    public void clearMouseActions() {
        actionToMouse.clear();
    }

    public boolean getGrabbed() {
        return mouseHelper.getGrabbed();
    }

    public float getDeltaWheel() {
        return mouseHelper.getDeltaWheel();
    }

    public float getDeltaX() {
        return mouseHelper.getDeltaX();
    }

    public float getDeltaY() {
        return mouseHelper.getDeltaY();
    }

    public int getButtonCount() {
        return mouseHelper.getButtonCount();
    }

    public void grabMouseCursor() {
        mouseHelper.grabMouseCursor();
    }

    public void init() {
        keyboardHelper.init();
        mouseHelper.init();
    }

    public boolean isActionButtonDown(String action) {
        int button = actionToMouse.get(action);
        if (button == HashMapStringInt.NOT_FOUND) {
            throw new IllegalArgumentException("Action was not found: " + action);
        }
        return mouseHelper.isButtonDown(button);
    }

    public boolean isActionKeyDown(String action) {
        int key = actionToKey.get(action);
        if (key == HashMapStringInt.NOT_FOUND) {
            throw new IllegalArgumentException("Action was not found: " + action);
        }
        return keyboardHelper.isKeyDown(key);
    }

    public boolean isButtonDown(Mouse mouse) {
        return mouseHelper.isButtonDown(mouse);
    }

    public boolean isKeyDown(Keyboard keyboard) {
        return keyboardHelper.isKeyDown(keyboard);
    }

    public void releaseMouseCursor() {
        mouseHelper.releaseMouseCursor();
    }

    public void removeActionButton(String action) {
        if (action == null) {
            throw new IllegalArgumentException("Action cannot be null");
        }
        int button = actionToMouse.remove(action);
        if (button == HashMapStringInt.NOT_FOUND) {
            throw new IllegalArgumentException("Action was not found: " + action);
        }
    }

    public void removeActionKey(String action) {
        if (action == null) {
            throw new IllegalArgumentException("Action cannot be null");
        }
        int key = actionToKey.remove(action);
        if (key == HashMapStringInt.NOT_FOUND) {
            throw new IllegalArgumentException("Action was not found: " + action);
        }
    }

    public void setAction(String action, Keyboard keyboard) {
        if (action == null) {
            throw new IllegalArgumentException("Action cannot be null");
        }
        actionToKey.put(action, keyboard.ordinal());
    }

    public void setAction(String action, Mouse mouse) {
        if (action == null) {
            throw new IllegalArgumentException("Action cannot be null");
        }
        actionToKey.put(action, mouse.ordinal());
    }

    public void update() {
        keyboardHelper.update();
        mouseHelper.update();
    }

    public boolean wasActionButtonDown(String action) {
        int button = actionToMouse.get(action);
        if (button == HashMapStringInt.NOT_FOUND) {
            throw new IllegalArgumentException("Action was not found: " + action);
        }
        return mouseHelper.wasButtonDown(button);
    }

    public boolean wasActionKeyDown(String action) {
        int key = actionToKey.get(action);
        if (key == HashMapStringInt.NOT_FOUND) {
            throw new IllegalArgumentException("Action was not found: " + action);
        }
        return keyboardHelper.wasKeyDown(key);
    }

    public boolean wasButtonDown(Mouse mouse) {
        return mouseHelper.wasButtonDown(mouse);
    }

    public boolean wasKeyDown(Keyboard keyboard) {
        return keyboardHelper.wasKeyDown(keyboard);
    }

}
