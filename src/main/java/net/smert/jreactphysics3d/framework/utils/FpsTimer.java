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

        if (deltatime >= 1000000000L) {
            displayFps = currentFps;
            currentFps = 0;
            fpsTimeSpan.addToCurrentTime(1000000000L);
            Fw.window.setTitle(Fw.config.getWindowTitle() + " | FPS: " + displayFps);
        }
    }

}
