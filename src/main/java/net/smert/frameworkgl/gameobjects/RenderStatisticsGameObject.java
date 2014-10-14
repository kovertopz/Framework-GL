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
package net.smert.frameworkgl.gameobjects;

import net.smert.frameworkgl.opengl.renderer.TextRenderer;
import net.smert.frameworkgl.utils.Color;
import net.smert.frameworkgl.utils.TimeSpan;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class RenderStatisticsGameObject extends GameObject {

    private int currentFps;
    private int displayFps;
    private final Color color0;
    private TextRenderer renderer;
    private final TimeSpan fpsTimeSpan;

    public RenderStatisticsGameObject() {
        currentFps = 0;
        displayFps = 0;
        color0 = new Color();
        fpsTimeSpan = new TimeSpan();
    }

    public Color getColor0() {
        return color0;
    }

    public void init(TextRenderer renderer) {
        this.renderer = renderer;
    }

    public void render() {
        long freeMem = Runtime.getRuntime().freeMemory();
        long maxMem = Runtime.getRuntime().maxMemory();
        long totalMem = Runtime.getRuntime().totalMemory();
        long usedMem = (totalMem - freeMem);
        String memoryUsed = (usedMem / 1024L / 1024L) + "";
        String memoryTotal = (maxMem / 1024L / 1024L) + "";
        String percentUsed = ((usedMem * 100L) / maxMem) + "";

        renderer.setTextColor(color0);
        renderer.drawString("Current FPS: " + displayFps);
        renderer.textNewLine();
        renderer.drawString("Used memory: " + percentUsed + "% " + memoryUsed + "MB of " + memoryTotal + "MB");
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
