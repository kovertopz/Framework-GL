package net.smert.jreactphysics3d.framework;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Window {

    private final static Logger log = LoggerFactory.getLogger(Window.class);

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

                log.debug("Found fullscreen compatible mode: Width: {}px Height: {}px Depth: {}bpp Freq: {}hz",
                        displayModes[i].getWidth(),
                        displayModes[i].getHeight(),
                        displayModes[i].getBitsPerPixel(),
                        displayModes[i].getFrequency());
            }
        } catch (LWJGLException e) {
            throw new RuntimeException(e);
        }

        return displayMode;
    }

    public void create() {
        config.inFullscreen = fullscreen;
        config.inVSync = vSync;
        try {
            DisplayMode displayMode = null;
            if (fullscreen) {
                displayMode = findDisplayMode(config.fullscreenWidth, config.fullscreenHeight,
                        config.fullscreenDepth, config.fullscreenFreq);

                if (displayMode == null) {
                    log.warn("Didn't find a fullscreen display mode for the requested resolution: "
                            + "Width: {}px Height: {}px Depth: {}bpp Freq: {}hz",
                            config.fullscreenWidth,
                            config.fullscreenHeight,
                            config.fullscreenDepth,
                            config.fullscreenFreq);
                }
            }
            if (displayMode == null) {
                displayMode = new DisplayMode(config.desktopWidth, config.desktopHeight);
            }

            Display.setDisplayMode(displayMode);
            Display.setTitle(config.windowTitle);
            if (Display.isCreated() == false) {
                Display.create(config.pixelFormat, config.contextAttribs);

                log.info("Created window with display mode: Width: {}px Height: {}px Depth: {}bpp Freq: {}hz",
                        displayMode.getWidth(),
                        displayMode.getHeight(),
                        displayMode.getBitsPerPixel(),
                        displayMode.getFrequency());
            }
            if ((config.desktopLocationX != -1) && (config.desktopLocationY != -1)) {
                Display.setLocation(config.desktopLocationX, config.desktopLocationY);
            }
            if (!fullscreen) {
                Display.setResizable(config.desktopResizable);
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
