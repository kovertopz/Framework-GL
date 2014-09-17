package net.smert.jreactphysics3d.framework;

import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.PixelFormat;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Configuration {

    boolean desktopResizable;
    boolean fullscreen;
    boolean inFocus;
    boolean inFullscreen;
    boolean inVSync;
    boolean pauseNotInFocus;
    boolean vSync;
    float mouseMoveSensitivity;
    float mouseWheelSensitivity;
    int backgroundFrameRate;
    int currentHeight;
    int currentWidth;
    int desktopHeight;
    int desktopLocationX;
    int desktopLocationY;
    int desktopWidth;
    int foregroundFrameRate;
    int fullscreenDepth;
    int fullscreenFreq;
    int fullscreenHeight;
    int fullscreenWidth;
    int gameTicksPerSecond;
    ContextAttribs contextAttribs;
    PixelFormat pixelFormat;
    String windowTitle;

    public Configuration(String[] args) {
        desktopResizable = true;
        fullscreen = false;
        inFocus = false;
        inFullscreen = false;
        inVSync = false;
        pauseNotInFocus = true;
        vSync = false;
        mouseMoveSensitivity = 1.0f;
        mouseWheelSensitivity = 1.0f;
        backgroundFrameRate = 30;
        currentHeight = 0;
        currentWidth = 0;
        desktopHeight = 480;
        desktopLocationX = -1;
        desktopLocationY = -1;
        desktopWidth = 854;
        foregroundFrameRate = 3000;
        fullscreenDepth = 32;
        fullscreenFreq = 60;
        fullscreenHeight = 720;
        fullscreenWidth = 1280;
        gameTicksPerSecond = 60;

        pixelFormat = new PixelFormat()
                .withDepthBits(24)
                .withStencilBits(8);
        contextAttribs = new ContextAttribs(3, 2)
                .withForwardCompatible(true)
                .withProfileCompatibility(false)
                .withProfileCore(true);
        windowTitle = "Title From Configuration";
    }

    public boolean isDesktopResizable() {
        return desktopResizable;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public boolean isInFocus() {
        return inFocus;
    }

    public boolean isInFullscreen() {
        return inFullscreen;
    }

    public boolean isInVSync() {
        return inVSync;
    }

    public boolean isPauseNotInFocus() {
        return pauseNotInFocus;
    }

    public boolean isvSync() {
        return vSync;
    }

    public float getMouseMoveSensitivity() {
        return mouseMoveSensitivity;
    }

    public float getMouseWheelSensitivity() {
        return mouseWheelSensitivity;
    }

    public int getBackgroundFrameRate() {
        return backgroundFrameRate;
    }

    public int getCurrentHeight() {
        return currentHeight;
    }

    public int getCurrentWidth() {
        return currentWidth;
    }

    public int getDesktopHeight() {
        return desktopHeight;
    }

    public int getDesktopLocationX() {
        return desktopLocationX;
    }

    public int getDesktopLocationY() {
        return desktopLocationY;
    }

    public int getDesktopWidth() {
        return desktopWidth;
    }

    public int getForegroundFrameRate() {
        return foregroundFrameRate;
    }

    public int getFullscreenDepth() {
        return fullscreenDepth;
    }

    public int getFullscreenFreq() {
        return fullscreenFreq;
    }

    public int getFullscreenHeight() {
        return fullscreenHeight;
    }

    public int getFullscreenWidth() {
        return fullscreenWidth;
    }

    public int getGameTicksPerSecond() {
        return gameTicksPerSecond;
    }

    public String getWindowTitle() {
        return windowTitle;
    }

}
