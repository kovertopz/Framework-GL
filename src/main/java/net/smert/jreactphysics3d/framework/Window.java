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

    public Window() {
        fullscreen = Fw.config.fullscreenRequested;
        vSync = Fw.config.vSyncRequested;
    }

    private DisplayMode findDisplayMode(int width, int height, int bpp, int freq) throws LWJGLException {

        DisplayMode displayMode = null;
        DisplayMode[] displayModes = Display.getAvailableDisplayModes();

        for (DisplayMode displayMode1 : displayModes) {

            // If the display mode matches save it
            if ((displayMode1.getWidth() == width)
                    && (displayMode1.getHeight() == height)
                    && (displayMode1.getBitsPerPixel() == bpp)
                    && (displayMode1.getFrequency() == freq)) {
                displayMode = displayMode1; // Don't break in case of logging
            }

            log.debug("Found fullscreen compatible mode: Width: {}px Height: {}px Depth: {}bpp Freq: {}hz",
                    displayMode1.getWidth(),
                    displayMode1.getHeight(),
                    displayMode1.getBitsPerPixel(),
                    displayMode1.getFrequency());
        }

        return displayMode;
    }

    public void create() throws LWJGLException {

        // Update the configuration
        Configuration config = Fw.config;
        config.fullscreenEnabled = fullscreen;
        config.vSyncEnabled = vSync;

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
        if (!Display.isCreated()) {
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

        // Save the current window width and height
        config.currentHeight = Display.getHeight();
        config.currentWidth = Display.getWidth();
    }

    public void printDisplayModes() throws LWJGLException {

        // These modes should always be fullscreen compatible
        DisplayMode[] displayModes = Display.getAvailableDisplayModes();

        for (DisplayMode displayMode : displayModes) {
            System.out.println("Found fullscreen compatible mode: "
                    + "Width: " + displayMode.getWidth() + "px "
                    + "Height: " + displayMode.getHeight() + "px "
                    + "Depth: " + displayMode.getBitsPerPixel() + "bpp "
                    + "Freq: " + displayMode.getFrequency() + "hz");
        }
    }

    public void setTitle(String title) {
        Display.setTitle(title);
    }

    public void toggleFullscreen() throws LWJGLException {
        fullscreen = !fullscreen;
        create();
    }

    public void toggleVSync() throws LWJGLException {
        vSync = !vSync;
        create();
    }

}
