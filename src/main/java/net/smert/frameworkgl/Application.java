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

import java.io.IOException;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Application {

    private final static Logger log = LoggerFactory.getLogger(Application.class);

    private boolean isRunning;
    private final Configuration config;
    private final Logging logging;
    private Screen screen;
    private Thread mainLoopThread;
    private ThrowableHandler throwableHandler;

    public Application(Configuration config, Logging logging, ThrowableHandler throwableHandler) {
        this.config = config;
        this.logging = logging;
        this.throwableHandler = throwableHandler;
    }

    private void startMainLoopThread() {
        mainLoopThread = new Thread("LWJGL Application - Main Loop") {
            @Override
            public void run() {
                try {
                    Application.this.mainLoop();
                } catch (Throwable t) {
                    log.error("Main Loop Exception", t);
                    Application.this.handleThrowable(t);
                    System.exit(-1);
                }
            }
        };
        mainLoopThread.start();
        log.info("Thread Started");
    }

    void handleThrowable(Throwable t) {
        throwableHandler.process(t);
    }

    void mainLoop() throws LWJGLException {

        // Initialization of OpenGL must happen here since we are in a new thread
        Fw.window.create();
        Fw.graphics.init();
        Fw.input.init();
        screen.init();
        Fw.timer.reset();

        boolean wasActive = true;

        while (isRunning()) {

            // Process operating system events
            Display.processMessages();

            config.inFocus = Display.isActive();

            // Lost focus
            if (wasActive && !config.inFocus && config.pauseNotInFocus) {
                wasActive = false;
                screen.pause();
            }
            // Regained focus
            if (!wasActive && config.inFocus && config.pauseNotInFocus) {
                wasActive = true;
                screen.resume();
            }

            // Was the window closed?
            if (Display.isCloseRequested()) {
                stopRunning();
                break;
            }

            // Was the window resized?
            if (Display.wasResized()) {
                int x = Display.getWidth();
                int y = Display.getHeight();
                config.currentHeight = y;
                config.currentWidth = x;
                screen.resize(x, y);
            }

            // Update and render
            Fw.input.update();
            Fw.timer.update();
            screen.render();
            Display.update(false); // Do not process operating system events again
            Util.checkGLError();

            // Limit frame rate
            int frameRateLimit;
            if (config.inFocus) {
                frameRateLimit = config.foregroundFrameRate;
            } else {
                frameRateLimit = config.backgroundFrameRate;
            }
            Display.sync(frameRateLimit);
        }

        // Shutdown
        screen.pause();
        screen.destroy();
        Fw.audio.destroy();
        Fw.graphics.destroy();
        Display.destroy();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void run(Screen screen) throws IOException {
        this.screen = screen;
        logging.reset(); // Configure logging
        startRunning();
        startMainLoopThread();
    }

    public void setThrowableHandler(ThrowableHandler throwableHandler) {
        this.throwableHandler = throwableHandler;
    }

    /**
     * Switches to a new screen. This method calls pause() and destroy() on the existing screen. With this in mind you
     * should only call this method from inside the current screen's render() method. The new screen's init() and
     * resize() methods will be called.
     *
     * @param screen The new screen which will be switched to.
     */
    public void switchScreen(Screen screen) {
        if (this.screen != null) {
            this.screen.pause();
            this.screen.destroy();
        }
        screen.init();
        screen.resize(config.currentWidth, config.currentHeight);
        this.screen = screen;
    }

    public void startRunning() {
        isRunning = true;
    }

    public void stopRunning() {
        isRunning = false;
    }

}
