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
        fullscreen = config.fullscreenRequested;
        vSync = config.vSyncRequested;
    }

    private DisplayMode findDisplayMode(int width, int height, int bpp, int freq) {

        DisplayMode displayMode = null;

        try {
            DisplayMode[] displayModes = Display.getAvailableDisplayModes();

            for (int i = 0, max = displayModes.length; i < max; i++) {

                // If the display mode matches save it
                if ((displayModes[i].getWidth() == width)
                        && (displayModes[i].getHeight() == height)
                        && (displayModes[i].getBitsPerPixel() == bpp)
                        && (displayModes[i].getFrequency() == freq)) {
                    displayMode = displayModes[i]; // Don't break in case of logging
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

        // Update the configuration
        config.fullscreenEnabled = fullscreen;
        config.vSyncEnabled = vSync;

        try {
            DisplayMode displayMode = null;

            if (fullscreen) {

                // Attempt to find a matching full screen compatible mode
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

            // Either a full screen mode wasn't requested or we couldn't find one that matched our settings
            if (displayMode == null) {
                displayMode = new DisplayMode(config.desktopWidth, config.desktopHeight);
            }

            // Set display mode and title
            Display.setDisplayMode(displayMode);
            Display.setTitle(config.windowTitle);

            // Only create the display when it hasn't already done so. We would get an error when switching from
            // windowed to full screen otherwise.
            if (Display.isCreated() == false) {
                Display.create(config.pixelFormat, config.contextAttribs);

                log.info("Created window with display mode: Width: {}px Height: {}px Depth: {}bpp Freq: {}hz",
                        displayMode.getWidth(),
                        displayMode.getHeight(),
                        displayMode.getBitsPerPixel(),
                        displayMode.getFrequency());
            }

            // Set the window location
            if ((config.desktopLocationX != -1) && (config.desktopLocationY != -1)) {
                Display.setLocation(config.desktopLocationX, config.desktopLocationY);
            }

            // Set resizeable
            if (!fullscreen) {
                Display.setResizable(config.desktopResizable);
            }

            // Set to full screen and vSync
            Display.setFullscreen(fullscreen);
            Display.setVSyncEnabled(vSync);

            // Create "LWJGL Timer" thread so Thread.sleep() is more accurate.
            Display.sync(3000);
        } catch (LWJGLException e) {
            throw new RuntimeException(e);
        }

        // Save the current window width and height
        config.currentHeight = Display.getHeight();
        config.currentWidth = Display.getWidth();
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
