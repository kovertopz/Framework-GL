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
package net.smert.frameworkgl.gui;

import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;
import java.nio.IntBuffer;
import java.util.List;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.helpers.KeyboardHelper;
import net.smert.frameworkgl.helpers.MouseHelper;
import net.smert.frameworkgl.opengl.GL;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class InputSystem implements de.lessvoid.nifty.spi.input.InputSystem {

    private final IntBuffer viewportBuffer;

    public InputSystem() {
        viewportBuffer = GL.bufferHelper.createIntBuffer(16);
    }

    private int getViewportHeight() {
        viewportBuffer.clear();
        GL.o1.getViewportDimensions(viewportBuffer); // X, Y, Width, Height
        return viewportBuffer.get(3);
    }

    @Override
    public void setResourceLoader(NiftyResourceLoader niftyResourceLoader) {
    }

    @Override
    public void forwardEvents(NiftyInputConsumer inputEventConsumer) {

        // Handle keyboard events
        boolean controlDown = Fw.input.isKeyControlDown();
        boolean shiftDown = Fw.input.isKeyShiftDown();
        List<KeyboardHelper.KeyboardEvent> keyboardEvents = Fw.input.getKeyboardEvents();
        for (KeyboardHelper.KeyboardEvent keyboardEvent : keyboardEvents) {
            KeyboardInputEvent niftyKeyboardEvent = new KeyboardInputEvent(keyboardEvent.key, keyboardEvent.character,
                    keyboardEvent.state, shiftDown, controlDown);
            boolean processed = inputEventConsumer.processKeyboardEvent(niftyKeyboardEvent);
        }

        // Handle mouse events
        int viewportHeight = getViewportHeight();
        List<MouseHelper.MouseEvent> mouseEvents = Fw.input.getMouseEvents();
        for (MouseHelper.MouseEvent mouseEvent : mouseEvents) {
            int mouseX = mouseEvent.mouseX;
            int mouseY = viewportHeight - mouseEvent.mouseY;
            boolean processed = inputEventConsumer.processMouseEvent(mouseX, mouseY, mouseEvent.deltaWheel,
                    mouseEvent.button, mouseEvent.state);
        }
    }

    @Override
    public void setMousePosition(int x, int y) {
        int viewportHeight = getViewportHeight();
        Fw.input.setCursorPosition(x, viewportHeight - y);
    }

}
