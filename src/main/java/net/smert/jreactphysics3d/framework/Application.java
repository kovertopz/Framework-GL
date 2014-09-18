package net.smert.jreactphysics3d.framework;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.Util;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Application {

    private boolean isRunning;
    private Configuration config;
    private Screen screen;
    private Thread mainLoopThread;
    private ThrowableHandler throwableHandler;

    public Application() {
        throwableHandler = new ThrowableHandler();
    }

    private void createMainLoopThread() {
        mainLoopThread = new Thread("LWJGL Application - Main Loop") {
            @Override
            public void run() {
                try {
                    Application.this.mainLoop();
                } catch (Throwable t) {
                    Application.this.handleThrowable(t);
                    System.exit(-1);
                }
            }
        };
        mainLoopThread.start();
    }

    private void createStaticClass() {
        Fgl.app = this;
        Fgl.audio = new Audio();
        Fgl.config = config;
        Fgl.files = new Files();
        Fgl.graphics = new Graphics();
        Fgl.input = new Input(config);
        Fgl.net = new Network();
        Fgl.timer = new Timer(config);
        Fgl.window = new Window(config);
    }

    protected void setScreen(Screen screen) {
        this.screen = screen;
    }

    protected void setConfig(Configuration config) {
        this.config = config;
    }

    void handleThrowable(Throwable t) {
        throwableHandler.process(t);
    }

    void mainLoop() {
        Fgl.window.create();
        Fgl.input.init();
        screen.init();
        Fgl.timer.reset();

        boolean wasActive = true;

        while (isRunning()) {
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

            if (Display.isCloseRequested()) {
                stopRunning();
                break;
            }

            if (Display.wasResized()) {
                int x = Display.getWidth();
                int y = Display.getHeight();
                config.currentHeight = y;
                config.currentWidth = x;
                screen.resize(x, y);
            }

            Fgl.input.update();
            Fgl.timer.update();
            screen.render();
            Display.update(false);
            Util.checkGLError();

            int frameRateLimit;
            if (config.inFocus) {
                frameRateLimit = config.foregroundFrameRate;
            } else {
                frameRateLimit = config.backgroundFrameRate;
            }
            Display.sync(frameRateLimit);
        }

        screen.pause();
        screen.destroy();
        Display.destroy();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void run(Configuration config, Screen screen) {
        setConfig(config);
        setScreen(screen);
        startRunning();
        createStaticClass();
        createMainLoopThread();
    }

    public void setExceptionHandler(ThrowableHandler exceptionHandler) {
        this.throwableHandler = exceptionHandler;
    }

    public void startRunning() {
        isRunning = true;
    }

    public void stopRunning() {
        isRunning = false;
    }

}
