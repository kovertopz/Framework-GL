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
    private String fpsMessage;
    private String memoryMessage;
    private TextRenderer renderer;
    private final TimeSpan fpsTimeSpan;

    public RenderStatisticsGameObject() {
        currentFps = 0;
        displayFps = 0;
        color0 = new Color();
        fpsMessage = "";
        memoryMessage = "";
        fpsTimeSpan = new TimeSpan();
    }

    public Color getColor0() {
        return color0;
    }

    public void init(TextRenderer renderer) {
        this.renderer = renderer;
    }

    public void render() {
        renderer.setTextColor(color0);
        renderer.drawString(fpsMessage);
        renderer.textNewLine();
        renderer.drawString(memoryMessage);
    }

    @Override
    public void update() {
        currentFps++;
        long delta = fpsTimeSpan.diffNowToLastUpdate();
        if (delta < 1000000000L) {
            return;
        }

        long freeMem = Runtime.getRuntime().freeMemory();
        long maxMem = Runtime.getRuntime().maxMemory();
        long totalMem = Runtime.getRuntime().totalMemory();
        long usedMem = (totalMem - freeMem);
        String memoryUsed = (usedMem / 1024L / 1024L) + "";
        String memoryTotal = (maxMem / 1024L / 1024L) + "";
        String percentUsed = ((usedMem * 100L) / maxMem) + "";
        fpsMessage = "Current FPS: " + displayFps;
        memoryMessage = "Used memory: " + percentUsed + "% " + memoryUsed + "MB of " + memoryTotal + "MB";

        displayFps = currentFps;
        currentFps = 0;
        fpsTimeSpan.addToCurrentTime(1000000000L);
    }

}
