package net.smert.jreactphysics3d.framework;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Window {

    private boolean fullscreen;
    private boolean vSync;
    private final Configuration config;

    public Window(Configuration config) {
        this.config = config;
        fullscreen = config.fullscreen;
        vSync = config.vSync;
    }

    private DisplayMode findDisplayMode(int width, int height, int bpp, int freq) {
        DisplayMode displayMode = null;

        try {
            DisplayMode[] displayModes = Display.getAvailableDisplayModes();

            for (int i = 0, max = displayModes.length; i < max; i++) {
                if ((displayModes[i].getWidth() == width)
                        && (displayModes[i].getHeight() == height)
                        && (displayModes[i].getBitsPerPixel() == bpp)
                        && (displayModes[i].getFrequency() == freq)) {
                    displayMode = displayModes[i];
                }
            }
        } catch (LWJGLException e) {
            throw new RuntimeException(e);
        }

        return displayMode;
    }

    public void create() {
        try {
            DisplayMode displayMode = null;

            if (fullscreen) {
                displayMode = findDisplayMode(config.fullscreenWidth, config.fullscreenHeight,
                        config.fullscreenDepth, config.fullscreenFreq);
            }
            if (displayMode == null) {
                displayMode = new DisplayMode(config.desktopWidth, config.desktopHeight);
            }

            Display.setDisplayMode(displayMode);
            Display.setTitle(config.windowTitle);
            if (Display.isCreated() == false) {
                Display.create(config.pixelFormat, config.contextAttribs);
            }
            Display.setFullscreen(fullscreen);
            Display.setVSyncEnabled(vSync);
            Display.sync(3000); // Create "LWJGL Timer" thread
        } catch (LWJGLException e) {
            throw new RuntimeException(e);
        }
    }

    public void printDisplayModes() {
        try {
            // These modes should always be fullscreen compatible
            DisplayMode[] displayModes = Display.getAvailableDisplayModes();

            for (int i = 0, max = displayModes.length; i < max; i++) {
                System.out.println("Found fullscreen compatible mode: "
                        + "Width: " + displayModes[i].getWidth() + "px "
                        + "Height: " + displayModes[i].getHeight() + "px "
                        + "Depth: " + displayModes[i].getBitsPerPixel() + "bpp "
                        + "Freq: " + displayModes[i].getFrequency());
            }
        } catch (LWJGLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setTitle(String title) {
        Display.setTitle(title);
    }

    public void toggleFullscreen() {
        fullscreen = !fullscreen;
        create();
    }

    public void toggleVSync() {
        vSync = !vSync;
        create();
    }

}
