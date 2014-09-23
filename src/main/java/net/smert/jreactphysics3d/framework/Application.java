package net.smert.jreactphysics3d.framework;

import net.smert.jreactphysics3d.framework.opengl.GL;
import net.smert.jreactphysics3d.framework.opengl.OpenGL1;
import net.smert.jreactphysics3d.framework.opengl.OpenGL2;
import net.smert.jreactphysics3d.framework.opengl.OpenGL3;
import net.smert.jreactphysics3d.framework.opengl.helpers.DisplayListHelper;
import net.smert.jreactphysics3d.framework.opengl.helpers.FrameBufferObjectHelper;
import net.smert.jreactphysics3d.framework.opengl.helpers.LegacyRenderHelper;
import net.smert.jreactphysics3d.framework.opengl.helpers.RenderBufferObjectHelper;
import net.smert.jreactphysics3d.framework.opengl.helpers.ShaderHelper;
import net.smert.jreactphysics3d.framework.opengl.helpers.TextureHelper;
import net.smert.jreactphysics3d.framework.opengl.helpers.VertexBufferObjectHelper;
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
        Fw.graphics = new Graphics();
        Fw.input = new Input(config);
        Fw.net = new Network();
        Fw.timer = new Timer(config);
        Fw.window = new Window(config);
        GL.displayListHelper = new DisplayListHelper();
        GL.fboHelper = new FrameBufferObjectHelper();
        GL.o1 = new OpenGL1();
        GL.o2 = new OpenGL2();
        GL.o3 = new OpenGL3();
        GL.rboHelper = new RenderBufferObjectHelper();
        GL.renderHelper = new LegacyRenderHelper();
        GL.shaderHelper = new ShaderHelper();
        GL.textureHelper = new TextureHelper();
        GL.vboHelper = new VertexBufferObjectHelper();
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

        // Initialization of OpenGL must happen here since we are in a new thread
        Fw.window.create();
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
        Fw.graphics.destroy();
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
