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
package net.smert.jreactphysics3d.framework.utils;

import net.smert.jreactphysics3d.framework.Fw;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class FpsTimer {

    private int currentFps;
    private int displayFps;
    private final TimeSpan fpsTimeSpan;

    public FpsTimer() {
        currentFps = 0;
        displayFps = 0;
        fpsTimeSpan = new TimeSpan();
    }

    public int getDisplayFps() {
        return displayFps;
    }

    public void update() {
        currentFps++;
        long deltatime = fpsTimeSpan.diffNowToLastUpdate();
        if (deltatime < 1000000000L) {
            return;
        }

        displayFps = currentFps;
        currentFps = 0;
        fpsTimeSpan.addToCurrentTime(1000000000L);
        Fw.window.setTitle(Fw.config.getWindowTitle() + " | FPS: " + displayFps);
    }

}
