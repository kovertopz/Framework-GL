package net.smert.jreactphysics3d.framework;

import net.smert.jreactphysics3d.framework.opengl.Legacy;
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
    private Configuration config;
    private Logging logging;
    private Screen screen;
    private Thread mainLoopThread;
    private ThrowableHandler throwableHandler;

    public Application() {
        throwableHandler = new ThrowableHandler();
    }

    private void configureLogging() {
        logging = new Logging(config);
        logging.reset();
    }

    private void createStaticClass() {
        Fw.app = this;
        Fw.audio = new Audio();
        Fw.config = config;
        Fw.files = new Files();
        Fw.gl = new Legacy();
        Fw.graphics = new Graphics();
        Fw.input = new Input(config);
        Fw.net = new Network();
        Fw.timer = new Timer(config);
        Fw.window = new Window(config);
    }

    protected void setScreen(Screen screen) {
        this.screen = screen;
    }

    protected void setConfig(Configuration config) {
        this.config = config;
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

    void mainLoop() {
        Fw.window.create();
        Fw.input.init();
        screen.init();
        Fw.timer.reset();

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

            Fw.input.update();
            Fw.timer.update();
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
        configureLogging();
        createStaticClass();
        startRunning();
        startMainLoopThread();
    }

    public void setThrowableHandler(ThrowableHandler throwableHandler) {
        this.throwableHandler = throwableHandler;
    }

    public void startRunning() {
        isRunning = true;
    }

    public void stopRunning() {
        isRunning = false;
    }

}
