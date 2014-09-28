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

    private final KeyboardHelper keyboardHelper;
    private final HashMapStringInt actionToKey;
    private final HashMapStringInt actionToMouse;
    private final List<InputProcessor> inputProcessors;
    private final MouseHelper mouseHelper;
    public final float MOUSE_POLL = 1.0f / 125.0f;

    public Input(KeyboardHelper keyboardHelper, MouseHelper mouseHelper) {
        this.keyboardHelper = keyboardHelper;
        actionToKey = new HashMapStringInt();
        actionToMouse = new HashMapStringInt();
        inputProcessors = new ArrayList<>();
        this.mouseHelper = mouseHelper;
    }

    /**
     * The purpose of an InputProcessor is to provide a managed way to register actions and to remove those actions when
     * we remove the input processor.
     *
     * @param inputProcessor
     */
    public void addInputProcessor(InputProcessor inputProcessor) {
        if (!inputProcessors.contains(inputProcessor)) {
            inputProcessor.registerActions();
            inputProcessors.add(inputProcessor);
        } else {
            throw new IllegalArgumentException("Tried to add an InputProcessor that already exists: "
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

    /**
     * This method must be called before Input is used.
     */
    public void init() {
        keyboardHelper.init();
        mouseHelper.init();
    }

    /**
     * Check to see if the action associated with a mouse button is down.
     *
     * @param action
     * @return
     */
    public boolean isActionButtonDown(String action) {
        int button = actionToMouse.get(action);
        if (button == HashMapStringInt.NOT_FOUND) {
            throw new IllegalArgumentException("Action was not found: " + action);
        }
        return mouseHelper.isButtonDown(button);
    }

    /**
     * Check to see if the action associated with a key on the keyboard is down.
     *
     * @param action
     * @return
     */
    public boolean isActionKeyDown(String action) {
        int key = actionToKey.get(action);
        if (key == HashMapStringInt.NOT_FOUND) {
            throw new IllegalArgumentException("Action was not found: " + action);
        }
        return keyboardHelper.isKeyDown(key);
    }

    /**
     * Check to see if the mouse button is down.
     *
     * @param mouse
     * @return
     */
    public boolean isButtonDown(Mouse mouse) {
        return mouseHelper.isButtonDown(mouse);
    }

    /**
     * Check to see if the keyboard key is down.
     *
     * @param keyboard
     * @return
     */
    public boolean isKeyDown(Keyboard keyboard) {
        return keyboardHelper.isKeyDown(keyboard);
    }

    public void releaseMouseCursor() {
        mouseHelper.releaseMouseCursor();
    }

    /**
     * Remove an action associated with a mouse button.
     *
     * @param action
     */
    public void removeActionButton(String action) {
        if (action == null) {
            throw new IllegalArgumentException("Action cannot be null");
        }
        int button = actionToMouse.remove(action);
        if (button == HashMapStringInt.NOT_FOUND) {
            throw new IllegalArgumentException("Action was not found: " + action);
        }
    }

    /**
     * Remove an action associated with a key on the keyboard.
     *
     * @param action
     */
    public void removeActionKey(String action) {
        if (action == null) {
            throw new IllegalArgumentException("Action cannot be null");
        }
        int key = actionToKey.remove(action);
        if (key == HashMapStringInt.NOT_FOUND) {
            throw new IllegalArgumentException("Action was not found: " + action);
        }
    }

    /**
     * Removes an input processor and unregisters the actions associated with it.
     *
     * @param inputProcessor
     */
    public void removeInputProcessor(InputProcessor inputProcessor) {
        if (inputProcessors.remove(inputProcessor)) {
            inputProcessor.unregisterActions();
        } else {
            throw new IllegalArgumentException("Did not find an instance of the InputProcessor: "
                    + inputProcessor.getClass().getSimpleName());
        }
    }

    /**
     * Associates a key press with an action.
     *
     * @param action
     * @param keyboard
     */
    public void setAction(String action, Keyboard keyboard) {
        if (action == null) {
            throw new IllegalArgumentException("Action cannot be null");
        }
        actionToKey.put(action, keyboard.ordinal());
    }

    /**
     * Associates a mouse button with an action.
     *
     * @param action
     * @param mouse
     */
    public void setAction(String action, Mouse mouse) {
        if (action == null) {
            throw new IllegalArgumentException("Action cannot be null");
        }
        actionToKey.put(action, mouse.ordinal());
    }

    /**
     * This method must be called once per frame.
     */
    public void update() {
        keyboardHelper.update();
        mouseHelper.update();
    }

    /**
     * Was the action associated with the mouse button down in the last frame?
     *
     * @param action
     * @return
     */
    public boolean wasActionButtonDown(String action) {
        int button = actionToMouse.get(action);
        if (button == HashMapStringInt.NOT_FOUND) {
            throw new IllegalArgumentException("Action was not found: " + action);
        }
        return mouseHelper.wasButtonDown(button);
    }

    /**
     * Was the action associated with a key on the keyboard down in the last frame?
     *
     * @param action
     * @return
     */
    public boolean wasActionKeyDown(String action) {
        int key = actionToKey.get(action);
        if (key == HashMapStringInt.NOT_FOUND) {
            throw new IllegalArgumentException("Action was not found: " + action);
        }
        return keyboardHelper.wasKeyDown(key);
    }

    /**
     * Was the mouse button down in the last frame?
     *
     * @param mouse
     * @return
     */
    public boolean wasButtonDown(Mouse mouse) {
        return mouseHelper.wasButtonDown(mouse);
    }

    /**
     * Was the key on the keyboard down in the last frame?
     *
     * @param keyboard
     * @return
     */
    public boolean wasKeyDown(Keyboard keyboard) {
        return keyboardHelper.wasKeyDown(keyboard);
    }

}
