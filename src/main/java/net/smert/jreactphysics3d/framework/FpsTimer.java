package net.smert.jreactphysics3d.framework;

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

    public void update() {
        currentFps++;

        long deltatime = fpsTimeSpan.diffNowToLastUpdate();

        if (deltatime >= 1000000000L) {
            displayFps = currentFps;
            currentFps = 0;
            fpsTimeSpan.addToCurrentTime(1000000000L);
            Fgl.graphics.setWindowTitle(Fgl.config.windowTitle + " | FPS: " + displayFps);
        }
    }

}
