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

import net.smert.frameworkgl.gui.GuiScreen;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Gui {

    private boolean initialized;
    private GuiScreen guiScreen;

    public Gui() {
        initialized = false;
    }

    public GuiScreen getGuiScreen() {
        return guiScreen;
    }

    public void init() {
        if (initialized) {
            return;
        }

        // Initialize audio
        Fw.audio.init();

        initialized = true;
    }

    public void render() {
        guiScreen.render();
    }

    public void setScreen(GuiScreen guiScreen) {
        if (this.guiScreen != null) {
            this.guiScreen.onEnd();
        }
        guiScreen.onStart();
        this.guiScreen = guiScreen;
    }

    public void update() {
        guiScreen.update();
    }

}
