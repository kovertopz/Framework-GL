package net.smert.jreactphysics3d.framework;

import java.util.logging.Level;
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
    boolean logConsole;
    boolean logFile;
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
    int openglMajorVersion;
    int openglMinorVersion;
    ContextAttribs contextAttribs;
    Level logLevel;
    PixelFormat pixelFormat;
    String logFilename;
    String logProperties;
    String windowTitle;

    public Configuration(String[] args) {
        desktopResizable = true;
        fullscreen = false;
        inFocus = false;
        inFullscreen = false;
        inVSync = false;
        logConsole = true;
        logFile = true;
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

        withOpenGL33ProfileCore();
        logLevel = Level.INFO;
        withDefaultPixelFormat();
        logFilename = "framework.log";
        logProperties = null;
        windowTitle = "OpenGL Framework";
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

    public boolean isLogConsole() {
        return logConsole;
    }

    public boolean isLogFile() {
        return logFile;
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

    public int getOpenglMajorVersion() {
        return openglMajorVersion;
    }

    public int getOpenglMinorVersion() {
        return openglMinorVersion;
    }

    public Level getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(Level logLevel) {
        this.logLevel = logLevel;
    }

    public String getLogFilename() {
        return logFilename;
    }

    public void setLogFilename(String logFilename) {
        this.logFilename = logFilename;
    }

    public String getLogProperties() {
        return logProperties;
    }

    public void setLogProperties(String logProperties) {
        this.logProperties = logProperties;
    }

    public String getWindowTitle() {
        return windowTitle;
    }

    public void setWindowTitle(String windowTitle) {
        this.windowTitle = windowTitle;
    }

    public final void withDefaultPixelFormat() {
        pixelFormat = new PixelFormat()
                .withDepthBits(24)
                .withStencilBits(8);
    }

    public final void withOpenGL32ProfileCore() {
        openglMajorVersion = 3;
        openglMinorVersion = 2;
        contextAttribs = new ContextAttribs(openglMajorVersion, openglMinorVersion)
                .withForwardCompatible(true)
                .withProfileCompatibility(false)
                .withProfileCore(true);
    }

    public final void withOpenGL33ProfileCompatibility() {
        openglMajorVersion = 2;
        openglMinorVersion = 1;
        contextAttribs = new ContextAttribs(3, 3)
                .withProfileCompatibility(true)
                .withProfileCore(false);
    }

    public final void withOpenGL33ProfileCore() {
        openglMajorVersion = 3;
        openglMinorVersion = 3;
        contextAttribs = new ContextAttribs(3, 3)
                .withProfileCompatibility(false)
                .withProfileCore(true);
    }

    public final void withNewPixelFormat() {
        pixelFormat = new PixelFormat();
    }

    public final void withPixelFormatAlphaBits(int alpha) {
        pixelFormat = pixelFormat.withAlphaBits(alpha);
    }

    public final void withPixelFormatAccumulationAlpha(int accum_alpha) {
        pixelFormat = pixelFormat.withAccumulationAlpha(accum_alpha);
    }

    public final void withPixelFormatAccumulationBitsPerPixel(int accum_bpp) {
        pixelFormat = pixelFormat.withAccumulationBitsPerPixel(accum_bpp);
    }

    public final void withPixelFormatAuxBuffers(int num_aux_buffers) {
        pixelFormat = pixelFormat.withAuxBuffers(num_aux_buffers);
    }

    public final void withPixelFormatBitsPerPixel(int bpp) {
        pixelFormat = pixelFormat.withBitsPerPixel(bpp);
    }

    public final void withPixelFormatCoverageSamples(int colorSamples) {
        pixelFormat = pixelFormat.withCoverageSamples(colorSamples);
    }

    public final void withPixelFormatCoverageSamples(int colorSamples, int coverageSamples) {
        pixelFormat = pixelFormat.withCoverageSamples(colorSamples, coverageSamples);
    }

    public final void withPixelFormatDepthBits(int depth) {
        pixelFormat = pixelFormat.withDepthBits(depth);
    }

    public final void withPixelFormatFloatingPoint(boolean floating_point) {
        pixelFormat = pixelFormat.withFloatingPoint(floating_point);
    }

    public final void withPixelFormatFloatingPointPacked(boolean floating_point_packed) {
        pixelFormat = pixelFormat.withFloatingPointPacked(floating_point_packed);
    }

    public final void withPixelFormatSamples(int samples) {
        pixelFormat = pixelFormat.withSamples(samples);
    }

    public final void withPixelFormatSRGB(boolean sRGB) {
        pixelFormat = pixelFormat.withSRGB(sRGB);
    }

    public final void withPixelFormatStencilBits(int stencil) {
        pixelFormat = pixelFormat.withStencilBits(stencil);
    }

    public final void withPixelFormatStereo(boolean stereo) {
        pixelFormat = pixelFormat.withStereo(stereo);
    }

}
