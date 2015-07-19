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

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Window {

    private final static Logger log = LoggerFactory.getLogger(Window.class);

    private boolean fullscreen;
    private boolean initialized;
    private boolean vSync;
    private boolean wasResized;
    private long lastSyncTime;
    private long variableYieldTime;
    private long window;
    private GLFWCursorPosCallback cursorPosCallback;
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;
    private GLFWMouseButtonCallback mouseButtonCallback;
    private GLFWScrollCallback scrollCallback;
    private GLFWWindowSizeCallback windowSizeCallback;
    private final IntBuffer heightBuffer;
    private final IntBuffer widthBuffer;

    public Window() {
        fullscreen = Fw.config.fullscreenRequested;
        vSync = Fw.config.vSyncRequested;
        heightBuffer = BufferUtils.createIntBuffer(1);
        widthBuffer = BufferUtils.createIntBuffer(1);
    }

    private GLFWvidmode findVideoMode(long monitor, int width, int height, int refreshRate,
            int redBits, int greenBits, int blueBits) {
        GLFWvidmode glfwVidMode = null;
        GLFWvidmode[] modes = getVideoModes(monitor);
        for (GLFWvidmode mode : modes) {

            // If the display mode matches save it
            if ((mode.getWidth() == width) && (mode.getHeight() == height)
                    && (mode.getRefreshRate() == refreshRate)
                    && (mode.getRedBits() == redBits) && (mode.getGreenBits() == greenBits) && (mode.getBlueBits() == blueBits)) {
                glfwVidMode = mode; // Don't break in case of logging
            }

            log.debug("Found fullscreen compatible mode: "
                    + "Width: {}px Height: {}px Refresh Rate: {}hz Red Bits: {} Green Bits: {} Blue Bits: {}",
                    mode.getWidth(), mode.getHeight(), mode.getRefreshRate(),
                    mode.getRedBits(), mode.getGreenBits(), mode.getBlueBits());
        }

        return glfwVidMode;
    }

    private GLFWvidmode getDesktopVideoMode(long monitor) {
        ByteBuffer mode = GLFW.glfwGetVideoMode(monitor);
        GLFWvidmode glfwVidMode = new GLFWvidmode();
        glfwVidMode.setBlueBits(GLFWvidmode.blueBits(mode));
        glfwVidMode.setGreenBits(GLFWvidmode.greenBits(mode));
        glfwVidMode.setHeight(GLFWvidmode.height(mode));
        glfwVidMode.setRedBits(GLFWvidmode.redBits(mode));
        glfwVidMode.setRefreshRate(GLFWvidmode.refreshRate(mode));
        glfwVidMode.setWidth(GLFWvidmode.width(mode));
        return glfwVidMode;
    }

    private GLFWvidmode[] getVideoModes(long monitor) {
        IntBuffer count = BufferUtils.createIntBuffer(1);
        ByteBuffer modes = GLFW.glfwGetVideoModes(monitor, count);
        GLFWvidmode[] videoModes = new GLFWvidmode[count.get(0)];
        for (int i = 0, max = count.get(0); i < max; i++) {

            // Advance position
            modes.position(i * GLFWvidmode.SIZEOF);

            // Create new video mode and extract data
            GLFWvidmode glfwVidMode = new GLFWvidmode();
            glfwVidMode.setBlueBits(GLFWvidmode.blueBits(modes));
            glfwVidMode.setGreenBits(GLFWvidmode.greenBits(modes));
            glfwVidMode.setHeight(GLFWvidmode.height(modes));
            glfwVidMode.setRedBits(GLFWvidmode.redBits(modes));
            glfwVidMode.setRefreshRate(GLFWvidmode.refreshRate(modes));
            glfwVidMode.setWidth(GLFWvidmode.width(modes));

            // Save video mode to array
            videoModes[i] = glfwVidMode;
        }
        return videoModes;
    }

    private void resize(int width, int height) {
        Fw.config.currentHeight = height;
        Fw.config.currentWidth = width;
        wasResized = true;
    }

    public void create() {

        // Update the configuration
        Configuration config = Fw.config;
        config.fullscreenEnabled = fullscreen;
        config.vSyncEnabled = vSync;

        // Setup variables
        long monitor = GLFW.glfwGetPrimaryMonitor();
        GLFWvidmode videoMode = null;

        if (fullscreen) {

            // Attempt to find a matching full screen compatible mode
            videoMode = findVideoMode(monitor, config.fullscreenWidth, config.fullscreenHeight, config.fullscreenRefreshRate,
                    config.framebufferRedBits, config.framebufferGreenBits, config.framebufferBlueBits);

            if (videoMode == null) {
                log.warn("Didn't find a fullscreen display mode for the requested resolution: "
                        + "Width: {}px Height: {}px Refresh Rate: {}hz Red Bits: {} Green Bits: {} Blue Bits: {}",
                        config.fullscreenWidth, config.fullscreenHeight, config.fullscreenRefreshRate,
                        config.framebufferRedBits, config.framebufferGreenBits, config.framebufferBlueBits);
            }
        }

        // Either a full screen mode wasn't requested or we couldn't find one that matched our settings
        if (videoMode == null) {
            GLFWvidmode desktopVideoMode = getDesktopVideoMode(monitor);
            videoMode = new GLFWvidmode();
            videoMode.setBlueBits(desktopVideoMode.getBlueBits());
            videoMode.setGreenBits(desktopVideoMode.getGreenBits());
            videoMode.setHeight(config.desktopHeight);
            videoMode.setRedBits(desktopVideoMode.getRedBits());
            videoMode.setRefreshRate(desktopVideoMode.getRefreshRate());
            videoMode.setWidth(config.desktopWidth);
        }

        // Reset window hints to defaults before setting them
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_ALPHA_BITS, videoMode.getBlueBits());
        GLFW.glfwWindowHint(GLFW.GLFW_BLUE_BITS, videoMode.getBlueBits());
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, config.requestedOpenglMajorVersion);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, config.requestedOpenglMinorVersion);
        GLFW.glfwWindowHint(GLFW.GLFW_DEPTH_BITS, config.framebufferDepthBits);
        GLFW.glfwWindowHint(GLFW.GLFW_GREEN_BITS, videoMode.getGreenBits());
        GLFW.glfwWindowHint(GLFW.GLFW_RED_BITS, videoMode.getRedBits());
        GLFW.glfwWindowHint(GLFW.GLFW_REFRESH_RATE, videoMode.getRefreshRate());
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, (fullscreen) ? GL11.GL_FALSE : ((config.desktopResizable) ? GL11.GL_TRUE : GL11.GL_FALSE));
        if ((config.requestedOpenglMajorVersion >= 3) && (config.requestedOpenglMinorVersion >= 0)) {
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, (config.forwardCompatible) ? GL11.GL_TRUE : GL11.GL_FALSE);
        }
        if (((config.requestedOpenglMajorVersion == 3) && (config.requestedOpenglMinorVersion >= 2)) || (config.requestedOpenglMajorVersion >= 4)) {
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, (config.coreProfile) ? GLFW.GLFW_OPENGL_CORE_PROFILE : GLFW.GLFW_OPENGL_COMPAT_PROFILE);
        } else {
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_ANY_PROFILE);
        }
        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, config.framebufferSamples);
        GLFW.glfwWindowHint(GLFW.GLFW_SRGB_CAPABLE, (config.framebufferSrgb) ? GL11.GL_TRUE : GL11.GL_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_STENCIL_BITS, config.framebufferStencilBits);
        GLFW.glfwWindowHint(GLFW.GLFW_STEREO, (config.framebufferStereo) ? GL11.GL_TRUE : GL11.GL_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);

        // Create new window
        long fullscreenMonitor = (fullscreen) ? monitor : MemoryUtil.NULL;
        long newWindow = GLFW.glfwCreateWindow(videoMode.getWidth(), videoMode.getHeight(), config.windowTitle, fullscreenMonitor, window);
        if (newWindow == MemoryUtil.NULL) {
            throw new WindowException("Failed to create the GLFW window");
        }

        log.info("Created window with display mode: "
                + "Width: {}px Height: {}px Refresh Rate: {}hz Red Bits: {} Green Bits: {} Blue Bits: {}",
                videoMode.getWidth(), videoMode.getHeight(), videoMode.getRefreshRate(),
                videoMode.getRedBits(), videoMode.getGreenBits(), videoMode.getBlueBits());

        // Destroy old window and callback
        if (window != MemoryUtil.NULL) {
            GLFW.glfwDestroyWindow(window);
            cursorPosCallback.release();
            keyCallback.release();
            mouseButtonCallback.release();
            scrollCallback.release();
            windowSizeCallback.release();
            Fw.input.clearNextState();
        }

        // Resize
        resize(videoMode.getWidth(), videoMode.getHeight());

        // Create GL context
        GLFW.glfwMakeContextCurrent(newWindow);
        GLContext.createFromCurrent();

        // Set the window location
        if (!fullscreen) {
            if ((config.desktopLocationX != -1) && (config.desktopLocationY != -1)) {
                GLFW.glfwSetWindowPos(newWindow, config.desktopLocationX, config.desktopLocationY);
            } else {
                // We can't reuse desktopVideoMode from above since after
                // we switch out of full screen it would be the full screen
                // video mode and not the current desktop.
                GLFWvidmode desktopVideoMode = getDesktopVideoMode(monitor);
                int x = (desktopVideoMode.getWidth() - config.desktopWidth) / 2;
                int y = (desktopVideoMode.getHeight() - config.desktopHeight) / 2;
                GLFW.glfwSetWindowPos(newWindow, x, y);
            }
        }

        // Turn on/off vsync
        if (vSync) {
            GLFW.glfwSwapInterval(1);
        } else {
            GLFW.glfwSwapInterval(0);
        }

        // Events
        GLFW.glfwSetCursorPosCallback(newWindow, cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double x, double y) {
                Fw.input.handleMouseMoveEvent(x, y);
            }
        });
        GLFW.glfwSetKeyCallback(newWindow, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                Fw.input.addKeyboardEvent(key, mods, scancode, action != GLFW.GLFW_RELEASE);
            }
        });
        GLFW.glfwSetMouseButtonCallback(newWindow, mouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                Fw.input.addMouseEvent(button, mods, action != GLFW.GLFW_RELEASE);
            }
        });
        GLFW.glfwSetScrollCallback(newWindow, scrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                Fw.input.handleMouseScrollEvent(xoffset, yoffset);
            }
        });
        GLFW.glfwSetWindowSizeCallback(newWindow, windowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                resize(width, height);
            }
        });

        // Show the window
        GLFW.glfwShowWindow(newWindow);

        // Save window
        window = newWindow;

        // Hide/unhide the cursor
        Fw.input.updateInputMode();
    }

    public void destroy() {
        if (!initialized) {
            return;
        }
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
        cursorPosCallback.release();
        errorCallback.release();
        keyCallback.release();
        mouseButtonCallback.release();
        scrollCallback.release();
        windowSizeCallback.release();
        window = MemoryUtil.NULL;
        initialized = false;
    }

    public void destroyWindow() {
        if (window == MemoryUtil.NULL) {
            return;
        }
        GLFW.glfwDestroyWindow(window);
        window = MemoryUtil.NULL;
    }

    public int getHeight() {
        GLFW.glfwGetWindowSize(window, widthBuffer, heightBuffer);
        return heightBuffer.get(0);
    }

    public int getWidth() {
        GLFW.glfwGetWindowSize(window, widthBuffer, heightBuffer);
        return widthBuffer.get(0);
    }

    public long getWindow() {
        return window;
    }

    public boolean isCloseRequested() {
        return (GLFW.glfwWindowShouldClose(window) == GL11.GL_TRUE);
    }

    public boolean isFocused() {
        return (GLFW.glfwGetWindowAttrib(window, GLFW.GLFW_FOCUSED) == GL11.GL_TRUE);
    }

    public boolean wasResized() {
        return wasResized;
    }

    public void setWasResized(boolean wasResized) {
        this.wasResized = wasResized;
    }

    public void init() {
        if (initialized) {
            return;
        }
        GLFW.glfwSetErrorCallback(errorCallback = Callbacks.errorCallbackThrow());
        if (GLFW.glfwInit() != GL11.GL_TRUE) {
            GLFW.glfwTerminate();
            throw new WindowException("Unable to initialize GLFW");
        }
        initialized = true;
    }

    public void printDisplayModes() {

        // These modes should always be fullscreen compatible
        long monitor = GLFW.glfwGetPrimaryMonitor();
        GLFWvidmode[] videoModes = getVideoModes(monitor);

        for (GLFWvidmode videoMode : videoModes) {
            System.out.println("Found fullscreen compatible mode: "
                    + "Width: " + videoMode.getWidth() + "px Height: " + videoMode.getHeight()
                    + "px Refresh Rate: " + videoMode.getRefreshRate() + "hz Red Bits: " + videoMode.getRedBits()
                    + " Green Bits: " + videoMode.getGreenBits() + " Blue Bits: " + videoMode.getBlueBits());
        }
    }

    public void processMessages() {
        GLFW.glfwPollEvents();
    }

    public void setTitle(String title) {
        GLFW.glfwSetWindowTitle(window, title);
    }

    public void sync(int fps) {
        if (fps <= 0) {
            return;
        }

        long overSleep = 0;
        long sleepTime = Timer.ONE_NANO_SECOND / fps;
        long yieldTime = Math.min(sleepTime, variableYieldTime + sleepTime % (1000000L));

        try {
            while (true) {
                long t = System.nanoTime() - lastSyncTime;

                if (t < sleepTime - yieldTime) {
                    Thread.sleep(1);
                } else if (t < sleepTime) {
                    Thread.yield();
                } else {
                    overSleep = t - sleepTime;
                    break;
                }
            }
        } catch (InterruptedException e) {
            // Do nothing
        } finally {
            lastSyncTime = System.nanoTime() - Math.min(overSleep, sleepTime);

            if (overSleep > variableYieldTime) {
                variableYieldTime = Math.min(variableYieldTime + 200 * 1000, sleepTime); // Increse 200 microseconds
            } else if (overSleep < variableYieldTime - 200 * 1000) {
                variableYieldTime = Math.max(variableYieldTime - 2 * 1000, 0); // Decrease 2 microseconds
            }
        }
    }

    public void toggleFullscreen() {
        fullscreen = !fullscreen;
        create();
    }

    public void toggleVSync() {
        vSync = !vSync;
        create();
    }

    public void update() {
        GLFW.glfwSwapBuffers(window);
    }

}
