package net.smert.jreactphysics3d.framework;

import java.util.ArrayList;
import java.util.List;
import net.smert.jreactphysics3d.framework.helpers.InputProcessor;
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
    private final List<InputProcessor> inputProcessors;
    private final MouseHelper mouseHelper;
    public final float MOUSE_POLL = 1.0f / 125.0f;

    public Input(Configuration config) {
        this.config = config;
        this.keyboardHelper = new KeyboardHelper();
        actionToKey = new HashMapStringInt();
        actionToMouse = new HashMapStringInt();
        inputProcessors = new ArrayList<>();
        this.mouseHelper = new MouseHelper(config);
    }

    public void addInputProcessor(InputProcessor inputProcessor) {
        if (inputProcessors.contains(inputProcessor) == false) {
            inputProcessor.registerActions();
            inputProcessors.add(inputProcessor);
        } else {
            throw new RuntimeException("Tried to add an InputProcessor that already exists: "
                    + inputProcessor.getClass().getSimpleName());
        }
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

    public boolean isGrabbed() {
        return mouseHelper.isGrabbed();
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

    public void removeInputProcessor(InputProcessor inputProcessor) {
        if (inputProcessors.remove(inputProcessor)) {
            inputProcessor.unregisterActions();
        } else {
            throw new RuntimeException("Did not find an instance of the InputProcessor: "
                    + inputProcessor.getClass().getSimpleName());
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
