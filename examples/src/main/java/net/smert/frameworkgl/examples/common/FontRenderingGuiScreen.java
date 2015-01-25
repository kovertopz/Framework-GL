/**
 * Copyright 2014 Jason Sorensen (sorensenj@smert.net)
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
package net.smert.frameworkgl.examples.common;

import net.smert.frameworkgl.gui.GuiScreen;
import net.smert.frameworkgl.opengl.renderer.TextRenderer;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class FontRenderingGuiScreen implements GuiScreen {

    private TextRenderer textRenderer;

    public void init(TextRenderer textRenderer) {
        this.textRenderer = textRenderer;
    }

    @Override
    public void onEnd() {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void render() {
        textRenderer.resetTextRendering();
        textRenderer.setTextColor("red");
        textRenderer.drawString("THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG?");
        textRenderer.textNewLine();
        textRenderer.setTextColor("green");
        textRenderer.drawString("the quick brown fox jumps over the lazy dog!");
        textRenderer.textNewLine();
        textRenderer.setTextColor("blue");
        textRenderer.drawString("0123456789!@#$%^&*()-_=+[]{}\\|;':\",./<>?");
        textRenderer.textNewLine();
        textRenderer.setTextColor("yellow");
        textRenderer.drawString("The Quick Brown Fox Jumps Over The Lazy Dog.");
        textRenderer.textNewLine();
    }

    @Override
    public void update() {
    }

}
