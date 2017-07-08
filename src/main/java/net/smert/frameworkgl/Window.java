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

import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
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
    private int createHeight;
    private int createWidth;
    private long lastSyncTime;
    private long variableYieldTime;
    private long window;
    private GLFWCursorPosCallback cursorPosCallback;
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

    private GLFWVidMode findVideoMode(long monitor, int width, int height, int refreshRate,
            int redBits, int greenBits, int blueBits) {
        GLFWVidMode videoMode = null;
        GLFWVidMode.Buffer modes = GLFW.glfwGetVideoModes(monitor);
        while (modes.hasRemaining()) {
            GLFWVidMode mode = modes.get();

            // If the display mode matches save it
            if ((mode.width() == width) && (mode.height() == height)
                    && (mode.refreshRate() == refreshRate)
                    && (mode.redBits() == redBits) && (mode.greenBits() == greenBits) && (mode.blueBits() == blueBits)) {
                videoMode = mode; // Don't break in case of logging
            }

            log.debug("Found fullscreen compatible mode: "
                    + "Width: {}px Height: {}px Refresh Rate: {}hz Red Bits: {} Green Bits: {} Blue Bits: {}",
                    mode.width(), mode.height(), mode.refreshRate(),
                    mode.redBits(), mode.greenBits(), mode.blueBits());
        }

        return videoMode;
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
        GLFWVidMode videoMode = null;

        if (fullscreen) {

            // Attempt to find a matching full screen compatible mode
            videoMode = findVideoMode(monitor, config.fullscreenWidth, config.fullscreenHeight, config.fullscreenRefreshRate,
                    config.framebufferRedBits, config.framebufferGreenBits, config.framebufferBlueBits);

            if (videoMode == null) {
                log.warn("Didn't find a fullscreen display mode for the requested resolution: "
                        + "Width: {}px Height: {}px Refresh Rate: {}hz Red Bits: {} Green Bits: {} Blue Bits: {}",
                        config.fullscreenWidth, config.fullscreenHeight, config.fullscreenRefreshRate,
                        config.framebufferRedBits, config.framebufferGreenBits, config.framebufferBlueBits);
            } else {
                createHeight = videoMode.height();
                createWidth = videoMode.width();
            }
        }

        // Either a full screen mode wasn't requested or we couldn't find one that matched our settings
        if (videoMode == null) {
            videoMode = GLFW.glfwGetVideoMode(monitor);
            createHeight = config.desktopHeight;
            createWidth = config.desktopWidth;
        }

        // Reset window hints to defaults before setting them
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_ALPHA_BITS, GLFW.GLFW_DONT_CARE);
        GLFW.glfwWindowHint(GLFW.GLFW_BLUE_BITS, videoMode.blueBits());
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, config.requestedOpenglMajorVersion);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, config.requestedOpenglMinorVersion);
        GLFW.glfwWindowHint(GLFW.GLFW_DEPTH_BITS, config.framebufferDepthBits);
        GLFW.glfwWindowHint(GLFW.GLFW_GREEN_BITS, videoMode.greenBits());
        GLFW.glfwWindowHint(GLFW.GLFW_RED_BITS, videoMode.redBits());
        GLFW.glfwWindowHint(GLFW.GLFW_REFRESH_RATE, videoMode.refreshRate());
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
        long newWindow = GLFW.glfwCreateWindow(createWidth, createHeight, config.windowTitle, fullscreenMonitor, window);
        if (newWindow == MemoryUtil.NULL) {
            throw new WindowException("Failed to create the GLFW window");
        }

        log.info("Created window with display mode: "
                + "Width: {}px Height: {}px Refresh Rate: {}hz Red Bits: {} Green Bits: {} Blue Bits: {}",
                createWidth, createHeight, videoMode.refreshRate(),
                videoMode.redBits(), videoMode.greenBits(), videoMode.blueBits());

        // Destroy old window and callback
        if (window != MemoryUtil.NULL) {
            GLFW.glfwDestroyWindow(window);
            cursorPosCallback.free();
            keyCallback.free();
            mouseButtonCallback.free();
            scrollCallback.free();
            windowSizeCallback.free();
            Fw.input.clearNextState();
        }

        // Resize
        resize(createWidth, createHeight);

        // Create GL context
        GLFW.glfwMakeContextCurrent(newWindow);
        GL.createCapabilities();

        // Set the window location
        if (!fullscreen) {
            if ((config.desktopLocationX != -1) && (config.desktopLocationY != -1)) {
                GLFW.glfwSetWindowPos(newWindow, config.desktopLocationX, config.desktopLocationY);
            } else {
                // We can't reuse desktopVideoMode from above since after
                // we switch out of full screen it would be the full screen
                // video mode and not the current desktop.
                GLFWVidMode desktopVideoMode = GLFW.glfwGetVideoMode(monitor);
                int x = (desktopVideoMode.width() - config.desktopWidth) / 2;
                int y = (desktopVideoMode.height() - config.desktopHeight) / 2;
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
        cursorPosCallback.free();
        keyCallback.free();
        mouseButtonCallback.free();
        scrollCallback.free();
        windowSizeCallback.free();
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
        return GLFW.glfwWindowShouldClose(window);
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
        GLFWErrorCallback.createThrow();
        if (!GLFW.glfwInit()) {
            GLFW.glfwTerminate();
            throw new WindowException("Unable to initialize GLFW");
        }
        initialized = true;
    }

    public void printDisplayModes() {

        // These modes should always be fullscreen compatible
        long monitor = GLFW.glfwGetPrimaryMonitor();
        GLFWVidMode.Buffer modes = GLFW.glfwGetVideoModes(monitor);
        while (modes.hasRemaining()) {
            GLFWVidMode mode = modes.get();
            System.out.println("Found fullscreen compatible mode: "
                    + "Width: " + mode.width() + "px Height: " + mode.height()
                    + "px Refresh Rate: " + mode.refreshRate() + "hz Red Bits: " + mode.redBits()
                    + " Green Bits: " + mode.greenBits() + " Blue Bits: " + mode.blueBits());
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
