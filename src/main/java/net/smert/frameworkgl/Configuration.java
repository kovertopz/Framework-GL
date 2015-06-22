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
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.PixelFormat;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Configuration {

    boolean fullscreenEnabled;
    boolean vSyncEnabled;
    ContextAttribs contextAttribs;
    PixelFormat pixelFormat;
    protected boolean desktopResizable;
    protected boolean inFocus;
    protected boolean fullscreenRequested;
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
    protected int fullscreenDepth;
    protected int fullscreenFreq;
    protected int fullscreenHeight;
    protected int fullscreenWidth;
    protected int glslVersion;
    protected int gameTicksPerSecond;
    protected int openglMajorVersion;
    protected int openglMinorVersion;
    protected Level logLevel;
    protected String logFilename;
    protected String logProperties;
    protected String windowTitle;
    protected String[] commandLineArgs;

    public Configuration(String[] args) {
        commandLineArgs = args;

        desktopResizable = true;
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
        fullscreenDepth = 32;
        fullscreenFreq = 60;
        fullscreenHeight = 720;
        fullscreenWidth = 1280;
        gameTicksPerSecond = 60;

        withOpenGL33ProfileCore();
        logLevel = Level.INFO;
        withDefaultPixelFormat();
        logFilename = "framework.log";
        logProperties = null;
        windowTitle = "OpenGL Framework";
    }

    /**
     * This is an initial configuration parameter to make the desktop window resizable. This does not reflect the
     * current setting of the Display.
     *
     * @return
     */
    public boolean isDesktopResizable() {
        return desktopResizable;
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
     * This is an initial configuration parameter to make the window enter full screen mode. This does not reflect the
     * current setting of the Display.
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
     * This is an initial configuration parameter to enable console logging (stderr).
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
     * Should we call the pause method on the screen when the window is no longer in the foreground?
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
     * This is an initial configuration parameter to make the window enter full screen mode. This does not reflect the
     * current setting of the Display.
     *
     * @return
     */
    public boolean isVSyncRequested() {
        return vSyncRequested;
    }

    /**
     * Gets the expected GLSL version (shaders) associated with the context selected.
     *
     * @return
     */
    public int getGlslVersion() {
        return glslVersion;
    }

    /**
     * Get the mouse sensitivity when the mouse is moved. This value is multiplied with the current value and the result
     * is used by the application. This should be in the range > 0 and <= 1.0.
     *
     * @return
     */
    public float getMouseMoveSensitivity() {
        return mouseMoveSensitivity;
    }

    /**
     * Get the mouse wheel sensitivity when the scroll wheel is rotated. This value is multiplied with the current value
     * and the result is used by the application. This should be in the range > 0 and <= 1.0.
     *
     * @return
     */
    public float getMouseWheelSensitivity() {
        return mouseWheelSensitivity;
    }

    /**
     * This is the maximum frame rate when the window is currently in the background. If this is set to zero then the
     * feature will be disabled.
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
     * This is an initial configuration parameter to set the desktop window height. This does not reflect the current
     * setting of the Display.
     *
     * @return
     */
    public int getDesktopHeight() {
        return desktopHeight;
    }

    /**
     * This is an initial configuration parameter to set the desktop window's horizontal location. This does not reflect
     * the current setting of the Display. Zero is in the left side of the screen and increased until the desktop
     * resolution is met. The window's left most side will be moved to this position. Setting this to -1 will cause the
     * window to be centered horizontally.
     *
     * @return
     */
    public int getDesktopLocationX() {
        return desktopLocationX;
    }

    /**
     * This is an initial configuration parameter to set the desktop window's vertical location. This does not reflect
     * the current setting of the Display. Zero is in the top side of the screen and increased until the desktop
     * resolution is met. The window's top most side will be moved to this position. Setting this to -1 will cause the
     * window to be centered vertically.
     *
     * @return
     */
    public int getDesktopLocationY() {
        return desktopLocationY;
    }

    /**
     * This is an initial configuration parameter to set the desktop window width. This does not reflect the current
     * setting of the Display.
     *
     * @return
     */
    public int getDesktopWidth() {
        return desktopWidth;
    }

    /**
     * This is the maximum frame rate when the window is currently in the foreground. If this is set to zero then the
     * feature will be disabled.
     *
     * @return
     */
    public int getForegroundFrameRate() {
        return foregroundFrameRate;
    }

    /**
     * This is an initial configuration parameter to set the full screen window depth. This does not reflect the current
     * setting of the Display. This should be either 16 or 32 bit color.
     *
     * @return
     */
    public int getFullscreenDepth() {
        return fullscreenDepth;
    }

    /**
     * This is an initial configuration parameter to set the full screen window refresh rate. This does not reflect the
     * current setting of the Display. This should be set to match the current monitors refresh rate. 60 should be a
     * safe default.
     *
     * @return
     */
    public int getFullscreenFreq() {
        return fullscreenFreq;
    }

    /**
     * This is an initial configuration parameter to set the full screen window height. This does not reflect the
     * current setting of the Display.
     *
     * @return
     */
    public int getFullscreenHeight() {
        return fullscreenHeight;
    }

    /**
     * This is an initial configuration parameter to set the full screen window width. This does not reflect the current
     * setting of the Display.
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
     * This is the requested OpenGL version associated with the requested context. The format of the numbers is
     * MAJOR.MINOR when requesting a specific version.
     *
     * @return
     */
    public int getOpenglMajorVersion() {
        return openglMajorVersion;
    }

    /**
     * This is the requested OpenGL version associated with the requested context. The format of the numbers is
     * MAJOR.MINOR when requesting a specific version.
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
     * This will change the default logging level. This value should be set before logging is initialized which happens
     * when Application.run() is called.
     *
     * @param logLevel
     */
    public void setLogLevel(Level logLevel) {
        this.logLevel = logLevel;
    }

    /**
     * This is the name of the filename for file based logging. This file will appear in the current working directory
     * where the Java application was ran from.
     *
     * @return
     */
    public String getLogFilename() {
        return logFilename;
    }

    /**
     * This sets the name of the filename for file based logging. This file will appear in the current working directory
     * where the Java application was ran from. Must be set before logging is initialized in Application.run().
     *
     * @param logFilename
     */
    public void setLogFilename(String logFilename) {
        this.logFilename = logFilename;
    }

    /**
     * This is the requested file that contains the logging properties. This will be used during initialization of
     * logging to set granular parameters.
     *
     * @return
     */
    public String getLogProperties() {
        return logProperties;
    }

    /**
     * This sets the requested file that contains the logging properties. This will be used during initialization of
     * logging to set granular parameters. Must be set before logging is initialized in Application.run().
     *
     * @param logProperties
     */
    public void setLogProperties(String logProperties) {
        this.logProperties = logProperties;
    }

    /**
     * This is the requested window title which will be set when the window is first created.
     *
     * @return
     */
    public String getWindowTitle() {
        return windowTitle;
    }

    /**
     * This sets the requested window title which will be set when the window is first created. This should only be
     * changed before the window is created.
     *
     * @param windowTitle
     */
    public void setWindowTitle(String windowTitle) {
        this.windowTitle = windowTitle;
    }

    /**
     * Sets the default pixel format which requests a 24 bit depth buffer and a 8 bit stencil buffer.
     */
    public final void withDefaultPixelFormat() {
        pixelFormat = new PixelFormat()
                .withDepthBits(24)
                .withStencilBits(8);
    }

    /**
     * Sets the OpenGL context to request version 3.2 with a core profile. All fixed-function pipeline features have
     * been removed. It is recommended that you pick a 3.3 core profile. This is only useful if you must support Mac OSX
     * 10.7 or 10.8.
     */
    public final void withOpenGL32ProfileCore() {
        glslVersion = 150;
        openglMajorVersion = 3;
        openglMinorVersion = 2;
        contextAttribs = new ContextAttribs(openglMajorVersion, openglMinorVersion)
                .withForwardCompatible(true)
                .withProfileCompatibility(false)
                .withProfileCore(true);
    }

    /**
     * This gives us a compatibility context and you should only stick to OpenGL 1.X and 2.X features.
     */
    public final void withOpenGL33ProfileCompatibility() {
        glslVersion = 120;
        openglMajorVersion = 2;
        openglMinorVersion = 1;
        contextAttribs = new ContextAttribs(3, 3)
                .withProfileCompatibility(true)
                .withProfileCore(false);
    }

    /**
     * Sets the OpenGL context to request version 3.3 with a core profile. All fixed-function pipeline features have
     * been removed. This is the recommended profile for the framework. This supports Mac OSX 10.9 and later.
     */
    public final void withOpenGL33ProfileCore() {
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            // Because on Mac OSX you have to request a 3.2 core profile to get a 3.3 core profile...DUH
            withOpenGL32ProfileCore();
            // Not sure why the shaders have errors validating, yet the info log shows nothing specific.
            validateShaders = false;
            glslVersion = 330;
            openglMajorVersion = 3;
            openglMinorVersion = 3;
            return;
        }
        glslVersion = 330;
        openglMajorVersion = 3;
        openglMinorVersion = 3;
        contextAttribs = new ContextAttribs(openglMajorVersion, openglMinorVersion)
                .withProfileCompatibility(false)
                .withProfileCore(true);
    }

    /**
     * Creates a new pixel format object.
     */
    public void withNewPixelFormat() {
        pixelFormat = new PixelFormat();
    }

    /**
     * Sets the alpha bits on the pixel format. The default is 0.
     *
     * @param alpha
     */
    public void withPixelFormatAlphaBits(int alpha) {
        pixelFormat = pixelFormat.withAlphaBits(alpha);
    }

    /**
     * I'm not sure what this setting does.
     *
     * @param accum_alpha
     */
    public void withPixelFormatAccumulationAlpha(int accum_alpha) {
        pixelFormat = pixelFormat.withAccumulationAlpha(accum_alpha);
    }

    /**
     * I'm not sure what this setting does. Please school me on the subject.
     *
     * @param accum_bpp
     */
    public void withPixelFormatAccumulationBitsPerPixel(int accum_bpp) {
        pixelFormat = pixelFormat.withAccumulationBitsPerPixel(accum_bpp);
    }

    /**
     * I'm not sure what this setting does. Please school me on the subject.
     *
     * @param num_aux_buffers
     */
    public void withPixelFormatAuxBuffers(int num_aux_buffers) {
        pixelFormat = pixelFormat.withAuxBuffers(num_aux_buffers);
    }

    /**
     * Changes the bit per pixel excluding alpha of the pixel format. This parameter is ignored in Display.create().
     *
     * @param bpp
     */
    public void withPixelFormatBitsPerPixel(int bpp) {
        pixelFormat = pixelFormat.withBitsPerPixel(bpp);
    }

    /**
     * I'm not sure what this setting does. Please school me on the subject. Coverage Sample Anti-aliasing (CSAA)
     *
     * @param colorSamples
     */
    public void withPixelFormatCoverageSamples(int colorSamples) {
        pixelFormat = pixelFormat.withCoverageSamples(colorSamples);
    }

    /**
     * I'm not sure what this setting does. Please school me on the subject. Coverage Sample Anti-aliasing (CSAA)
     *
     * @param colorSamples
     * @param coverageSamples
     */
    public void withPixelFormatCoverageSamples(int colorSamples, int coverageSamples) {
        pixelFormat = pixelFormat.withCoverageSamples(colorSamples, coverageSamples);
    }

    /**
     * This sets the requested number of depth bits for the depth buffer. More bits the higher the precision. All
     * hardware should support a 24 bit depth buffer.
     *
     * @param depth
     */
    public void withPixelFormatDepthBits(int depth) {
        pixelFormat = pixelFormat.withDepthBits(depth);
    }

    /**
     * I'm not sure what this setting does. Please school me on the subject.
     *
     * @param floating_point
     */
    public void withPixelFormatFloatingPoint(boolean floating_point) {
        pixelFormat = pixelFormat.withFloatingPoint(floating_point);
    }

    /**
     * I'm not sure what this setting does. Please school me on the subject.
     *
     * @param floating_point_packed
     */
    public void withPixelFormatFloatingPointPacked(boolean floating_point_packed) {
        pixelFormat = pixelFormat.withFloatingPointPacked(floating_point_packed);
    }

    /**
     * This is the number of samples to use for Multi Sample Anti-aliasing (MSAA).
     *
     * @param samples
     */
    public void withPixelFormatSamples(int samples) {
        pixelFormat = pixelFormat.withSamples(samples);
    }

    /**
     * I'm not sure what this setting does. Please school me on the subject.
     *
     * @param sRGB
     */
    public void withPixelFormatSRGB(boolean sRGB) {
        pixelFormat = pixelFormat.withSRGB(sRGB);
    }

    /**
     * This requests the number of bits to be used with the stencil buffer.
     *
     * @param stencil
     */
    public void withPixelFormatStencilBits(int stencil) {
        pixelFormat = pixelFormat.withStencilBits(stencil);
    }

    /**
     * I'm not sure what this setting does. Please school me on the subject.
     *
     * @param stereo
     */
    public void withPixelFormatStereo(boolean stereo) {
        pixelFormat = pixelFormat.withStereo(stereo);
    }

}
