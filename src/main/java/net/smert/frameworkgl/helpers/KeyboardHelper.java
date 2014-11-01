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

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class KeyboardHelper {

    private final static int KEYBOARD_SIZE = org.lwjgl.input.Keyboard.KEYBOARD_SIZE;
    private boolean[] isDown;
    private boolean[] nextState;
    private boolean[] wasDown;
    private int[] lwjglToKeyboard;
    private final List<KeyboardEvent> keyboardEvents;

    public KeyboardHelper() {
        keyboardEvents = new ArrayList<>();
    }

    private void mapLwglToKeyboard() {

        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_NONE] = Keyboard.NONE.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_ESCAPE] = Keyboard.ESCAPE.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_F1] = Keyboard.F1.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_F2] = Keyboard.F2.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_F3] = Keyboard.F3.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_F4] = Keyboard.F4.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_F5] = Keyboard.F5.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_F6] = Keyboard.F6.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_F7] = Keyboard.F7.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_F8] = Keyboard.F8.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_F9] = Keyboard.F9.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_F10] = Keyboard.F10.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_F11] = Keyboard.F11.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_F12] = Keyboard.F12.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_SYSRQ] = Keyboard.PRINT.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_SCROLL] = Keyboard.SCROLL.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_PAUSE] = Keyboard.PAUSE.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_GRAVE] = Keyboard.GRAVE.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_1] = Keyboard.NUM1.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_2] = Keyboard.NUM2.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_3] = Keyboard.NUM3.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_4] = Keyboard.NUM4.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_5] = Keyboard.NUM5.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_6] = Keyboard.NUM6.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_7] = Keyboard.NUM7.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_8] = Keyboard.NUM8.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_9] = Keyboard.NUM9.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_0] = Keyboard.NUM0.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_MINUS] = Keyboard.DASH.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_EQUALS] = Keyboard.EQUALS.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_BACK] = Keyboard.BACK.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_A] = Keyboard.A.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_B] = Keyboard.B.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_C] = Keyboard.C.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_D] = Keyboard.D.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_E] = Keyboard.E.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_F] = Keyboard.F.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_G] = Keyboard.G.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_H] = Keyboard.H.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_I] = Keyboard.I.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_J] = Keyboard.J.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_K] = Keyboard.K.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_L] = Keyboard.L.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_M] = Keyboard.M.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_N] = Keyboard.N.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_O] = Keyboard.O.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_P] = Keyboard.P.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_Q] = Keyboard.Q.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_R] = Keyboard.R.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_S] = Keyboard.S.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_T] = Keyboard.T.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_U] = Keyboard.U.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_V] = Keyboard.V.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_W] = Keyboard.W.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_X] = Keyboard.X.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_Y] = Keyboard.Y.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_Z] = Keyboard.Z.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_TAB] = Keyboard.TAB.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_LBRACKET] = Keyboard.LBRACKET.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_RBRACKET] = Keyboard.RBRACKET.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_BACKSLASH] = Keyboard.BACKSLASH.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_CAPITAL] = Keyboard.CAPS.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_SEMICOLON] = Keyboard.SEMICOLON.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_APOSTROPHE] = Keyboard.APOSTROPHE.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_RETURN] = Keyboard.RETURN.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_RSHIFT] = Keyboard.RSHIFT.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_COMMA] = Keyboard.COMMA.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_PERIOD] = Keyboard.PERIOD.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_SLASH] = Keyboard.SLASH.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_LSHIFT] = Keyboard.LSHIFT.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_LCONTROL] = Keyboard.LCONTROL.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_LMETA] = Keyboard.LMETA.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_LMENU] = Keyboard.LALT.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_SPACE] = Keyboard.SPACE.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_RMENU] = Keyboard.RALT.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_RMETA] = Keyboard.RMETA.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_RCONTROL] = Keyboard.RCONTROL.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_INSERT] = Keyboard.INSERT.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_HOME] = Keyboard.HOME.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_PRIOR] = Keyboard.PAGE_UP.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_DELETE] = Keyboard.DELETE.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_END] = Keyboard.END.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_NEXT] = Keyboard.PAGE_DOWN.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_DOWN] = Keyboard.DOWN.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_LEFT] = Keyboard.LEFT.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_RIGHT] = Keyboard.RIGHT.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_UP] = Keyboard.UP.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_NUMLOCK] = Keyboard.NUMPAD_NUMLOCK.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_DIVIDE] = Keyboard.NUMPAD_DIVIDE.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_MULTIPLY] = Keyboard.NUMPAD_MULTIPLY.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_SUBTRACT] = Keyboard.NUMPAD_SUBTRACT.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_ADD] = Keyboard.NUMPAD_ADD.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_NUMPAD0] = Keyboard.NUMPAD_NUM0.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_NUMPAD1] = Keyboard.NUMPAD_NUM1.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_NUMPAD2] = Keyboard.NUMPAD_NUM2.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_NUMPAD3] = Keyboard.NUMPAD_NUM3.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_NUMPAD4] = Keyboard.NUMPAD_NUM4.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_NUMPAD5] = Keyboard.NUMPAD_NUM5.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_NUMPAD6] = Keyboard.NUMPAD_NUM6.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_NUMPAD7] = Keyboard.NUMPAD_NUM7.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_NUMPAD8] = Keyboard.NUMPAD_NUM8.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_NUMPAD9] = Keyboard.NUMPAD_NUM9.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_DECIMAL] = Keyboard.NUMPAD_DECIMAL.ordinal();
        lwjglToKeyboard[org.lwjgl.input.Keyboard.KEY_RETURN] = Keyboard.NUMPAD_ENTER.ordinal();
    }

    private void updateButtonState() {
        for (int i = 0; i < KEYBOARD_SIZE; i++) {
            wasDown[i] = isDown[i]; // Save last frame
            isDown[i] = nextState[i]; // Set current frame
        }
    }

    public List<KeyboardEvent> getKeyboardEvents() {
        return keyboardEvents;
    }

    public void init() {
        isDown = new boolean[KEYBOARD_SIZE];
        nextState = new boolean[KEYBOARD_SIZE];
        wasDown = new boolean[KEYBOARD_SIZE];
        lwjglToKeyboard = new int[KEYBOARD_SIZE];

        // Set defaults
        for (int i = 0; i < KEYBOARD_SIZE; i++) {
            isDown[i] = false;
            nextState[i] = false;
            wasDown[i] = false;
            lwjglToKeyboard[i] = 0;
        }

        mapLwglToKeyboard();
    }

    public boolean isKeyDown(int key) {
        if ((key < 0) || (key >= isDown.length)) {
            throw new IllegalArgumentException("Invalid key");
        }
        return isDown[key];
    }

    public boolean isKeyDown(Keyboard keyboard) {
        return isDown[keyboard.ordinal()];
    }

    public void update() {

        // Clear last frames events
        keyboardEvents.clear();

        // Handle queued events
        while (org.lwjgl.input.Keyboard.next()) {
            KeyboardEvent event = new KeyboardEvent();
            event.character = org.lwjgl.input.Keyboard.getEventCharacter();
            event.key = org.lwjgl.input.Keyboard.getEventKey();
            event.mappedKey = lwjglToKeyboard[event.key];
            event.state = org.lwjgl.input.Keyboard.getEventKeyState();
            keyboardEvents.add(event);
            nextState[event.mappedKey] = event.state;
        }

        // Update states
        updateButtonState();
    }

    public boolean wasKeyDown(int key) {
        if ((key < 0) || (key >= wasDown.length)) {
            throw new IllegalArgumentException("Invalid key");
        }
        return wasDown[key];
    }

    public boolean wasKeyDown(Keyboard keyboard) {
        return wasDown[keyboard.ordinal()];
    }

    public static class KeyboardEvent {

        /**
         * The state indicates that the key has been pressed when true
         */
        public boolean state;

        /**
         * The character of the keyboard associated with the event
         */
        public char character;

        /**
         * The raw key from LWJGL
         */
        public int key;

        /**
         * The mapped key to the framework's keyboard
         */
        public int mappedKey;

    }

}
