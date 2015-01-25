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

import net.smert.frameworkgl.opengl.renderer.TextRenderer;
import net.smert.frameworkgl.utils.Color;
import net.smert.frameworkgl.utils.TimeSpan;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class SimpleDebugGuiScreen implements GuiScreen {

    private int currentFps;
    private int displayFps;
    private final Color textureColor0;
    private TextRenderer textRenderer;
    private final TimeSpan fpsTimeSpan;

    public SimpleDebugGuiScreen() {
        currentFps = 0;
        displayFps = 0;
        textureColor0 = new Color();
        fpsTimeSpan = new TimeSpan();
    }

    public Color getTextureColor0() {
        return textureColor0;
    }

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
        long freeMem = Runtime.getRuntime().freeMemory();
        long maxMem = Runtime.getRuntime().maxMemory();
        long totalMem = Runtime.getRuntime().totalMemory();
        long usedMem = (totalMem - freeMem);
        String memoryUsed = (usedMem / 1024L / 1024L) + "";
        String memoryTotal = (maxMem / 1024L / 1024L) + "";
        String percentUsed = ((usedMem * 100L) / maxMem) + "";

        textRenderer.resetTextRendering();
        textRenderer.setTextColor(textureColor0);
        textRenderer.drawString("Current FPS: " + displayFps);
        textRenderer.textNewLine();
        textRenderer.drawString("Used memory: " + percentUsed + "% " + memoryUsed + "MB of " + memoryTotal + "MB");
    }

    @Override
    public void update() {
        currentFps++;
        long delta = fpsTimeSpan.diffNowToLastUpdate();
        if (delta < 1000000000L) {
            return;
        }

        displayFps = currentFps;
        currentFps = 0;
        fpsTimeSpan.addToCurrentTime(1000000000L);
    }

}
