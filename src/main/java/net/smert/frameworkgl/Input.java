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
package net.smert.frameworkgl;

import java.util.ArrayList;
import java.util.List;
import net.smert.frameworkgl.helpers.InputProcessor;
import net.smert.frameworkgl.helpers.Keyboard;
import net.smert.frameworkgl.helpers.KeyboardHelper;
import net.smert.frameworkgl.helpers.Mouse;
import net.smert.frameworkgl.helpers.MouseHelper;
import net.smert.frameworkgl.utils.HashMapStringInt;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Input {

    public final static float MOUSE_POLL = 1f / 125f;

    private final HashMapStringInt actionToKey;
    private final HashMapStringInt actionToMouse;
    private final KeyboardHelper keyboardHelper;
    private final List<InputProcessor> inputProcessors;
    private final MouseHelper mouseHelper;

    public Input(KeyboardHelper keyboardHelper, MouseHelper mouseHelper) {
        this.keyboardHelper = keyboardHelper;
        this.mouseHelper = mouseHelper;
        actionToKey = new HashMapStringInt();
        actionToMouse = new HashMapStringInt();
        inputProcessors = new ArrayList<>();
    }

    /**
     * The purpose of an InputProcessor is to provide a managed way to register
     * actions and to remove those actions when we remove the input processor.
     *
     * @param inputProcessor
     */
    public void addInputProcessor(InputProcessor inputProcessor) {
        if (inputProcessors.contains(inputProcessor)) {
            throw new IllegalArgumentException("Tried to add an InputProcessor that already exists: "
                    + inputProcessor.getClass().getSimpleName());
        }
        inputProcessor.registerActions();
        inputProcessors.add(inputProcessor);
    }

    /**
     * Adds a keyboard event to the queue until clearEvents or
     * clearKeyboardEvents is called.
     *
     * @param key
     * @param modifiers
     * @param scancode
     * @param state
     */
    public void addKeyboardEvent(int key, int modifiers, int scancode, boolean state) {
        keyboardHelper.addEvent(key, modifiers, scancode, state);
    }

    /**
     * Adds a mouse event to the queue until clearEvents or clearMouseEvents is
     * called.
     *
     * @param button
     * @param modifiers
     * @param state
     */
    public void addMouseEvent(int button, int modifiers, boolean state) {
        mouseHelper.addEvent(button, modifiers, state);
    }

    /**
     * Center the mouse cursor
     */
    public void centerMouseCursor() {
        mouseHelper.centerCursor();
    }

    /**
     * Clear key and mouse actions.
     */
    public void clearActions() {
        actionToKey.clear();
        actionToMouse.clear();
    }

    /**
     * Clear keyboard and mouse events.
     */
    public void clearEvents() {
        keyboardHelper.clearEvents();
        mouseHelper.clearEvents();
    }

    /**
     * Clear keyboard and mouse next state.
     */
    public void clearNextState() {
        keyboardHelper.clearNextState();
        mouseHelper.clearNextState();
    }

    /**
     * Clear registered key actions.
     */
    public void clearKeyActions() {
        actionToKey.clear();
    }

    /**
     * Clear keyboard events.
     */
    public void clearKeyboardEvents() {
        keyboardHelper.clearEvents();
    }

    /**
     * Clear keyboard next state.
     */
    public void clearKeyboardNextState() {
        keyboardHelper.clearNextState();
    }

    /**
     * Clear registered mouse actions.
     */
    public void clearMouseActions() {
        actionToMouse.clear();
    }

    /**
     * Clear mouse delta.
     */
    public void clearMouseDelta() {
        mouseHelper.clearDelta();
    }

    /**
     * Clear mouse events.
     */
    public void clearMouseEvents() {
        mouseHelper.clearEvents();
    }

    /**
     * Clear mouse next state.
     */
    public void clearMouseNextState() {
        mouseHelper.clearNextState();
    }

    public int getDeltaWheel() {
        return mouseHelper.getDeltaWheel();
    }

    public float getDeltaWheelWithSensitivity() {
        return mouseHelper.getDeltaWheelWithSensitivity();
    }

    public int getDeltaX() {
        return mouseHelper.getDeltaX();
    }

    public float getDeltaXWithSensitivity() {
        return mouseHelper.getDeltaXWithSensitivity();
    }

    public int getDeltaY() {
        return mouseHelper.getDeltaY();
    }

    public float getDeltaYWithSensitivity() {
        return mouseHelper.getDeltaYWithSensitivity();
    }

    public int getMouseX() {
        return mouseHelper.getMouseX();
    }

    public int getMouseY() {
        return mouseHelper.getMouseY();
    }

    /**
     * Sets the mouse cursor position.
     *
     * @param x
     * @param y
     */
    public void setMouseCursorPosition(int x, int y) {
        mouseHelper.setCursorPosition(x, y);
    }

    public List<KeyboardHelper.KeyboardEvent> getKeyboardEvents() {
        return keyboardHelper.getKeyboardEvents();
    }

    public List<MouseHelper.MouseEvent> getMouseEvents() {
        return mouseHelper.getMouseEvents();
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
     * Was the action associated with a key on the keyboard down in the last
     * frame?
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
     * Check to see if the mouse button is down.
     *
     * @param mouse
     * @return
     */
    public boolean isButtonDown(Mouse mouse) {
        return mouseHelper.isButtonDown(mouse);
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
     * Is the mouse cursor grabbed by the window?
     *
     * @return
     */
    public boolean isGrabbed() {
        return mouseHelper.isGrabbed();
    }

    /**
     * Check to see if the keyboard alt key is down.
     *
     * @return
     */
    public boolean isKeyAltDown() {
        return keyboardHelper.isKeyDown(Keyboard.LEFT_ALT) || keyboardHelper.isKeyDown(Keyboard.RIGHT_ALT);
    }

    /**
     * Was an alt key on the keyboard down in the last frame?
     *
     * @return
     */
    public boolean wasKeyAltDown() {
        return keyboardHelper.wasKeyDown(Keyboard.LEFT_ALT) || keyboardHelper.wasKeyDown(Keyboard.RIGHT_ALT);
    }

    /**
     * Check to see if the keyboard control key is down.
     *
     * @return
     */
    public boolean isKeyControlDown() {
        return keyboardHelper.isKeyDown(Keyboard.LEFT_CONTROL) || keyboardHelper.isKeyDown(Keyboard.RIGHT_CONTROL);
    }

    /**
     * Was a control key on the keyboard down in the last frame?
     *
     * @return
     */
    public boolean wasKeyControlDown() {
        return keyboardHelper.wasKeyDown(Keyboard.LEFT_CONTROL) || keyboardHelper.wasKeyDown(Keyboard.RIGHT_CONTROL);
    }

    /**
     * Check to see if the keyboard key is down.
     *
     * @param key
     * @return
     */
    public boolean isKeyDown(int key) {
        return keyboardHelper.isKeyDown(key);
    }

    /**
     * Was the key on the keyboard down in the last frame?
     *
     * @param key
     * @return
     */
    public boolean wasKeyDown(int key) {
        return keyboardHelper.wasKeyDown(key);
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

    /**
     * Was the key on the keyboard down in the last frame?
     *
     * @param keyboard
     * @return
     */
    public boolean wasKeyDown(Keyboard keyboard) {
        return keyboardHelper.wasKeyDown(keyboard);
    }

    /**
     * Check to see if the keyboard shift key is down.
     *
     * @return
     */
    public boolean isKeyShiftDown() {
        return keyboardHelper.isKeyDown(Keyboard.LEFT_SHIFT) || keyboardHelper.isKeyDown(Keyboard.RIGHT_SHIFT);
    }

    /**
     * Was a shift key on the keyboard down in the last frame?
     *
     * @return
     */
    public boolean wasKeyShiftDown() {
        return keyboardHelper.wasKeyDown(Keyboard.LEFT_SHIFT) || keyboardHelper.wasKeyDown(Keyboard.RIGHT_SHIFT);
    }

    /**
     * Check to see if the keyboard super (Windows) key is down.
     *
     * @return
     */
    public boolean isKeySuperDown() {
        return keyboardHelper.isKeyDown(Keyboard.LEFT_SUPER) || keyboardHelper.isKeyDown(Keyboard.RIGHT_SUPER);
    }

    /**
     * Was a super (Windows) key on the keyboard down in the last frame?
     *
     * @return
     */
    public boolean wasKeySuperDown() {
        return keyboardHelper.wasKeyDown(Keyboard.LEFT_SUPER) || keyboardHelper.wasKeyDown(Keyboard.RIGHT_SUPER);
    }

    /**
     * Grab mouse cursor and hide it preventing the cursor from going outside
     * the window bounds.
     */
    public void grabMouseCursor() {
        mouseHelper.grabMouseCursor();
    }

    /**
     * Handle a move event which updates mouse X and Y position.
     *
     * @param x
     * @param y
     */
    public void handleMouseMoveEvent(double x, double y) {
        mouseHelper.handleMoveEvent(x, y);
    }

    /**
     * Handle a scroll event which updates delta wheel.
     *
     * @param xoffset is currently ignored.
     * @param yoffset
     */
    public void handleMouseScrollEvent(double xoffset, double yoffset) {
        mouseHelper.handleScrollEvent(xoffset, yoffset);
    }

    /**
     * This method must be called before Input is used.
     */
    public void init() {
        keyboardHelper.init();
        mouseHelper.init();
    }

    /**
     * Unhides the cursor and centers it inside the window.
     */
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
     * Removes an input processor and unregisters the actions associated with
     * it.
     *
     * @param inputProcessor
     */
    public void removeInputProcessor(InputProcessor inputProcessor) {
        if (!inputProcessors.remove(inputProcessor)) {
            throw new IllegalArgumentException("Did not find an instance of the InputProcessor: "
                    + inputProcessor.getClass().getSimpleName());
        }
        inputProcessor.unregisterActions();
    }

    /**
     * Reset keyboard and mouse button state
     */
    public void reset() {
        keyboardHelper.reset();
        mouseHelper.reset();
    }

    /**
     * Reset keyboard button state.
     */
    public void resetKeyboard() {
        keyboardHelper.reset();
    }

    /**
     * Reset mouse button state.
     */
    public void resetMouse() {
        mouseHelper.reset();
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
     * Updates the input mode which will hide/unhide the cursor depending on the
     * state.
     */
    public void updateInputMode() {
        mouseHelper.updateInputMode();
    }

}
