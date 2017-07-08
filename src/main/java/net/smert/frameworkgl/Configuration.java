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
package net.smert.frameworkgl;

import java.util.logging.Level;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Configuration {

    boolean fullscreenEnabled;
    boolean vSyncEnabled;
    protected boolean coreProfile;
    protected boolean desktopResizable;
    protected boolean forwardCompatible;
    protected boolean framebufferSrgb;
    protected boolean framebufferStereo;
    protected boolean fullscreenRequested;
    protected boolean inFocus;
    protected boolean logConsole;
    protected boolean logFile;
    protected boolean pauseNotInFocus;
    protected boolean validateShaders;
    protected boolean vSyncRequested;
    protected float mouseMoveSensitivity;
    protected float mouseWheelSensitivity;
    protected int backgroundFrameRate;
    protected int currentHeight;
    protected int currentWidth;
    protected int desktopHeight;
    protected int desktopLocationX;
    protected int desktopLocationY;
    protected int desktopWidth;
    protected int foregroundFrameRate;
    protected int framebufferBlueBits;
    protected int framebufferDepthBits;
    protected int framebufferGreenBits;
    protected int framebufferRedBits;
    protected int framebufferSamples;
    protected int framebufferStencilBits;
    protected int fullscreenHeight;
    protected int fullscreenRefreshRate;
    protected int fullscreenWidth;
    protected int gameTicksPerSecond;
    protected int glslVersion;
    protected int openglMajorVersion;
    protected int openglMinorVersion;
    protected int requestedOpenglMajorVersion;
    protected int requestedOpenglMinorVersion;
    protected Level logLevel;
    protected String logFilename;
    protected String logProperties;
    protected String windowTitle;
    protected String[] commandLineArgs;

    public Configuration(String[] args) {
        commandLineArgs = args;

        coreProfile = true;
        desktopResizable = true;
        forwardCompatible = false;
        framebufferSrgb = false;
        framebufferStereo = false;
        fullscreenEnabled = false;
        fullscreenRequested = false;
        inFocus = false;
        logConsole = true;
        logFile = true;
        pauseNotInFocus = true;
        validateShaders = true;
        vSyncEnabled = false;
        vSyncRequested = false;
        mouseMoveSensitivity = 1f;
        mouseWheelSensitivity = 1f;
        backgroundFrameRate = 30;
        currentHeight = 0;
        currentWidth = 0;
        desktopHeight = 480;
        desktopLocationX = -1;
        desktopLocationY = -1;
        desktopWidth = 854;
        foregroundFrameRate = 3000;
        framebufferBlueBits = 8;
        framebufferDepthBits = 24;
        framebufferGreenBits = 8;
        framebufferRedBits = 8;
        framebufferSamples = 0;
        framebufferStencilBits = 8;
        fullscreenHeight = 720;
        fullscreenRefreshRate = 60;
        fullscreenWidth = 1280;
        gameTicksPerSecond = 60;
        glslVersion = -1;
        openglMajorVersion = -1;
        openglMinorVersion = -1;
        requestedOpenglMajorVersion = -1;
        requestedOpenglMinorVersion = -1;

        logLevel = Level.INFO;
        logFilename = "framework.log";
        logProperties = null;
        windowTitle = "OpenGL Framework";
    }

    /**
     * This is an initial configuration parameter to request a OpenGL core
     * profile. If set to false a compatibility profile will be requested.
     *
     * @return
     */
    public boolean isCoreProfile() {
        return coreProfile;
    }

    /**
     * This is an initial configuration parameter to make the desktop window
     * resizable. This does not reflect the current setting of the Display.
     *
     * @return
     */
    public boolean isDesktopResizable() {
        return desktopResizable;
    }

    /**
     * This is an initial configuration parameter to request a OpenGL profile
     * that is forward compatible.
     *
     * @return
     */
    public boolean isForwardCompatible() {
        return forwardCompatible;
    }

    /**
     * This is an initial configuration parameter to set the framebuffer to be
     * sRGB capable.
     *
     * @return
     */
    public boolean isFramebufferSrgb() {
        return framebufferSrgb;
    }

    /**
     * This is an initial configuration parameter to set the framebuffer to use
     * stereoscopic rendering.
     *
     * @return
     */
    public boolean isFramebufferStereo() {
        return framebufferStereo;
    }

    /**
     * Is the window currently in full screen mode?
     *
     * @return
     */
    public boolean isFullscreenEnabled() {
        return fullscreenEnabled;
    }

    /**
     * This is an initial configuration parameter to make the window enter full
     * screen mode. This does not reflect the current setting of the Display.
     *
     * @return
     */
    public boolean isFullscreenRequested() {
        return fullscreenRequested;
    }

    /**
     * Is the window currently in the foreground?
     *
     * @return
     */
    public boolean isInFocus() {
        return inFocus;
    }

    /**
     * This is an initial configuration parameter to enable console logging
     * (stderr).
     *
     * @return
     */
    public boolean isLogConsole() {
        return logConsole;
    }

    /**
     * This is an initial configuration parameter to enable file logging.
     *
     * @return
     */
    public boolean isLogFile() {
        return logFile;
    }

    /**
     * Should we call the pause method on the screen when the window is no
     * longer in the foreground?
     *
     * @return
     */
    public boolean isPauseNotInFocus() {
        return pauseNotInFocus;
    }

    /**
     * Should we validate shaders after linking?
     *
     * @return
     */
    public boolean isValidateShaders() {
        return validateShaders;
    }

    /**
     * Does the current window have vSync enabled?
     *
     * @return
     */
    public boolean isVSyncEnabled() {
        return vSyncEnabled;
    }

    /**
     * This is an initial configuration parameter to make the window enter full
     * screen mode. This does not reflect the current setting of the Display.
     *
     * @return
     */
    public boolean isVSyncRequested() {
        return vSyncRequested;
    }

    /**
     * Get the mouse sensitivity when the mouse is moved. This value is
     * multiplied with the current value and the result is used by the
     * application. This should be in the range > 0 and <= 1.0.
     *
     * @return
     */
    public float getMouseMoveSensitivity() {
        return mouseMoveSensitivity;
    }

    /**
     * Get the mouse wheel sensitivity when the scroll wheel is rotated. This
     * value is multiplied with the current value and the result is used by the
     * application. This should be in the range > 0 and <= 1.0.
     *
     * @return
     */
    public float getMouseWheelSensitivity() {
        return mouseWheelSensitivity;
    }

    /**
     * This is the maximum frame rate when the window is currently in the
     * background. If this is set to zero then the feature will be disabled.
     *
     * @return
     */
    public int getBackgroundFrameRate() {
        return backgroundFrameRate;
    }

    /**
     * This is the current height of the window.
     *
     * @return
     */
    public int getCurrentHeight() {
        return currentHeight;
    }

    /**
     * This is the current width of the window.
     *
     * @return
     */
    public int getCurrentWidth() {
        return currentWidth;
    }

    /**
     * This is an initial configuration parameter to set the desktop window
     * height. This does not reflect the current setting of the Display.
     *
     * @return
     */
    public int getDesktopHeight() {
        return desktopHeight;
    }

    /**
     * This is an initial configuration parameter to set the desktop window's
     * horizontal location. This does not reflect the current setting of the
     * Display. Zero is in the left side of the screen and increased until the
     * desktop resolution is met. The window's left most side will be moved to
     * this position. Setting this to -1 will cause the window to be centered
     * horizontally.
     *
     * @return
     */
    public int getDesktopLocationX() {
        return desktopLocationX;
    }

    /**
     * This is an initial configuration parameter to set the desktop window's
     * vertical location. This does not reflect the current setting of the
     * Display. Zero is in the top side of the screen and increased until the
     * desktop resolution is met. The window's top most side will be moved to
     * this position. Setting this to -1 will cause the window to be centered
     * vertically.
     *
     * @return
     */
    public int getDesktopLocationY() {
        return desktopLocationY;
    }

    /**
     * This is an initial configuration parameter to set the desktop window
     * width. This does not reflect the current setting of the Display.
     *
     * @return
     */
    public int getDesktopWidth() {
        return desktopWidth;
    }

    /**
     * This is the maximum frame rate when the window is currently in the
     * foreground. If this is set to zero then the feature will be disabled.
     *
     * @return
     */
    public int getForegroundFrameRate() {
        return foregroundFrameRate;
    }

    /**
     * This is an initial configuration parameter to set the framebuffer blue
     * bits.
     *
     * @return
     */
    public int getFramebufferBlueBits() {
        return framebufferBlueBits;
    }

    /**
     * This is an initial configuration parameter to set the framebuffer depth
     * bits.
     *
     * @return
     */
    public int getFramebufferDepthBits() {
        return framebufferDepthBits;
    }

    /**
     * This is an initial configuration parameter to set the framebuffer green
     * bits.
     *
     * @return
     */
    public int getFramebufferGreenBits() {
        return framebufferGreenBits;
    }

    /**
     * This is an initial configuration parameter to set the framebuffer red
     * bits.
     *
     * @return
     */
    public int getFramebufferRedBits() {
        return framebufferRedBits;
    }

    /**
     * This is an initial configuration parameter to set the framebuffer samples
     * used in multisampling. Zero disables multisampling.
     *
     * @return
     */
    public int getFramebufferSamples() {
        return framebufferSamples;
    }

    /**
     * This is an initial configuration parameter to set the framebuffer stencil
     * bits.
     *
     * @return
     */
    public int getFramebufferStencilBits() {
        return framebufferStencilBits;
    }

    /**
     * This is an initial configuration parameter to set the full screen window
     * height. This does not reflect the current setting of the Display.
     *
     * @return
     */
    public int getFullscreenHeight() {
        return fullscreenHeight;
    }

    /**
     * This is an initial configuration parameter to set the full screen window
     * refresh rate. This does not reflect the current setting of the Display.
     * This should be set to match the current monitors refresh rate. 60 should
     * be a safe default.
     *
     * @return
     */
    public int getFullscreenRefreshRate() {
        return fullscreenRefreshRate;
    }

    /**
     * This is an initial configuration parameter to set the full screen window
     * width. This does not reflect the current setting of the Display.
     *
     * @return
     */
    public int getFullscreenWidth() {
        return fullscreenWidth;
    }

    /**
     * The total number of game ticks that can happen in one second.
     *
     * @return
     */
    public int getGameTicksPerSecond() {
        return gameTicksPerSecond;
    }

    /**
     * Gets the expected GLSL version (shaders) associated with the context
     * selected.
     *
     * @return
     */
    public int getGlslVersion() {
        return glslVersion;
    }

    /**
     * This is the requested OpenGL version associated with the requested
     * context. The format of the numbers is MAJOR.MINOR when requesting a
     * specific version.
     *
     * @return
     */
    public int getOpenglMajorVersion() {
        return openglMajorVersion;
    }

    /**
     * This is the requested OpenGL version associated with the requested
     * context. The format of the numbers is MAJOR.MINOR when requesting a
     * specific version.
     *
     * @return
     */
    public int getOpenglMinorVersion() {
        return openglMinorVersion;
    }

    /**
     * This is the currently requested logging level.
     *
     * @return
     */
    public Level getLogLevel() {
        return logLevel;
    }

    /**
     * This will change the default logging level. This value should be set
     * before logging is initialized which happens when Application.run() is
     * called.
     *
     * @param logLevel
     */
    public void setLogLevel(Level logLevel) {
        this.logLevel = logLevel;
    }

    /**
     * This is the name of the filename for file based logging. This file will
     * appear in the current working directory where the Java application was
     * ran from.
     *
     * @return
     */
    public String getLogFilename() {
        return logFilename;
    }

    /**
     * This sets the name of the filename for file based logging. This file will
     * appear in the current working directory where the Java application was
     * ran from. Must be set before logging is initialized in Application.run().
     *
     * @param logFilename
     */
    public void setLogFilename(String logFilename) {
        this.logFilename = logFilename;
    }

    /**
     * This is the requested file that contains the logging properties. This
     * will be used during initialization of logging to set granular parameters.
     *
     * @return
     */
    public String getLogProperties() {
        return logProperties;
    }

    /**
     * This sets the requested file that contains the logging properties. This
     * will be used during initialization of logging to set granular parameters.
     * Must be set before logging is initialized in Application.run().
     *
     * @param logProperties
     */
    public void setLogProperties(String logProperties) {
        this.logProperties = logProperties;
    }

    /**
     * This is the requested window title which will be set when the window is
     * first created.
     *
     * @return
     */
    public String getWindowTitle() {
        return windowTitle;
    }

    /**
     * This sets the requested window title which will be set when the window is
     * first created. This should only be changed before the window is created.
     *
     * @param windowTitle
     */
    public void setWindowTitle(String windowTitle) {
        this.windowTitle = windowTitle;
    }

    /**
     * Gets the command line arguments passed to the application.
     *
     * @return
     */
    public String[] getCommandLineArgs() {
        return commandLineArgs;
    }

    public final void withOpenGL32ProfileCompatibility() {
        coreProfile = false;
        forwardCompatible = false;
        glslVersion = 150;
        openglMajorVersion = requestedOpenglMajorVersion = 3;
        openglMinorVersion = requestedOpenglMinorVersion = 2;
    }

    public final void withOpenGL32ProfileCore() {
        coreProfile = true;
        forwardCompatible = false;
        glslVersion = 150;
        openglMajorVersion = requestedOpenglMajorVersion = 3;
        openglMinorVersion = requestedOpenglMinorVersion = 2;
    }

    /**
     * Sets the OpenGL context to request version 3.2 with a core profile and
     * forward compatible set. All fixed-function pipeline features have been
     * removed. It is recommended that you pick a 3.3 core profile. This will
     * get the highest OpenGL version supported for Mac OSX 10.7 and later.
     * OpenGL major/minor version will not be set correctly of Mac OSX 10.9+
     * which supports OpenGL 3.3/4.1.
     */
    public final void withOpenGL32ProfileCoreForwardCompatible() {
        coreProfile = true;
        forwardCompatible = true;
        glslVersion = 150;
        openglMajorVersion = requestedOpenglMajorVersion = 3;
        openglMinorVersion = requestedOpenglMinorVersion = 2;
    }

    public final void withOpenGL33ProfileCompatibility() {
        coreProfile = false;
        forwardCompatible = false;
        glslVersion = 330;
        openglMajorVersion = requestedOpenglMajorVersion = 3;
        openglMinorVersion = requestedOpenglMinorVersion = 3;
    }

    /**
     * Sets the OpenGL context to request version 3.3 with a core profile. All
     * fixed-function pipeline features have been removed. This is the
     * recommended profile for the framework. Use withOpenGL33ProfileCoreMac()
     * to get the correct context for Mac OSX 10.9.
     */
    public final void withOpenGL33ProfileCore() {
        if (System.getProperty("os.name").toLowerCase().startsWith("mac")) {
            withOpenGL33ProfileCoreMac();
            return;
        }
        coreProfile = true;
        forwardCompatible = false;
        glslVersion = 330;
        openglMajorVersion = requestedOpenglMajorVersion = 3;
        openglMinorVersion = requestedOpenglMinorVersion = 3;
    }

    /**
     * Sets the OpenGL context to request version 3.2 with a core profile and
     * forward compatible set. All fixed-function pipeline features have been
     * removed. This is intended to be called for Mac OSX 10.9 in order to get a
     * OpenGL 3.3 context. Since Mac OSX 10.9 supports both OpenGL 3.3 and 4.1
     * you may still end up with a higher OpenGL version than requested. If you
     * are one of those people I would be nice to know if this actually works in
     * a 4.1 context.
     */
    public final void withOpenGL33ProfileCoreMac() {
        withOpenGL32ProfileCoreForwardCompatible();
        glslVersion = 330;
        openglMajorVersion = 3;
        openglMinorVersion = 3;
        // Not sure why the shaders have errors validating, yet the info log shows nothing specific.
        validateShaders = false;
    }

    public final void withOpenGL33ProfileCoreForwardCompatible() {
        coreProfile = true;
        forwardCompatible = true;
        glslVersion = 330;
        openglMajorVersion = requestedOpenglMajorVersion = 3;
        openglMinorVersion = requestedOpenglMinorVersion = 3;
    }

    /**
     * This gives us a compatibility context and you should only stick to OpenGL
     * 1.X and 2.X features.
     */
    public final void withOpenGLProfileAny() {
        coreProfile = false;
        forwardCompatible = false;
        glslVersion = 120;
        openglMajorVersion = requestedOpenglMajorVersion = 2;
        openglMinorVersion = requestedOpenglMinorVersion = 1;
    }

}
