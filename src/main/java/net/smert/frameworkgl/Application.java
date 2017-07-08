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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Application {

    private final static Logger log = LoggerFactory.getLogger(Application.class);

    private volatile boolean isRunning;
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
                boolean exceptionCaught = false;
                try {
                    Fw.window.init();
                    Fw.window.create();
                    Fw.graphics.init();
                    Fw.input.init();
                    Application.this.mainLoop();
                } catch (Throwable t) {
                    exceptionCaught = true;
                    log.error("Main Loop Thread Exception", t);
                    Application.this.handleThrowable(t);
                } finally {
                    Fw.audio.destroy();
                    Fw.net.destroy();
                    Fw.graphics.destroy(); // Shutdown in reverse order
                    Fw.window.destroy();
                    if (exceptionCaught) {
                        System.exit(-1);
                    }
                }
            }
        };
        mainLoopThread.start();
        log.info("Thread Started");
    }

    void handleThrowable(Throwable t) {
        throwableHandler.process(t);
    }

    void mainLoop() {

        // Initialize screen
        screen.init();

        // Reset timer so delta is correct
        Fw.timer.reset();

        // Set the window previously active
        boolean wasActive = true;

        while (isRunning()) {

            // Process operating system events
            Fw.input.clearEvents();
            Fw.window.processMessages();

            config.inFocus = Fw.window.isFocused();

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
            if (Fw.window.isCloseRequested()) {
                stopRunning();
                break;
            }

            // Was the window resized?
            if (Fw.window.wasResized()) {
                Fw.window.setWasResized(false);
                screen.resize(config.currentWidth, config.currentHeight);
            }

            // Update and render
            Fw.input.update();
            Fw.timer.update();
            screen.render();
            Fw.window.update();

            // Limit frame rate
            int frameRateLimit;
            if (config.inFocus) {
                frameRateLimit = config.foregroundFrameRate;
            } else {
                frameRateLimit = config.backgroundFrameRate;
            }
            Fw.window.sync(frameRateLimit);
        }

        // Shutdown
        screen.pause();
        screen.destroy();
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
     * Switches to a new screen. This method calls pause() and destroy() on the
     * existing screen. With this in mind you should only call this method from
     * inside the current screen's render() method. The new screen's init() and
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
