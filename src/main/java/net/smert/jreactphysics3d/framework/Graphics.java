package net.smert.jreactphysics3d.framework;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Graphics {

    private final Window window;

    public Graphics(Configuration config) {
        window = new Window(config);
    }

    public void createWindow() {
        window.create();
    }

    public void printDisplayModes() {
        window.printDisplayModes();
    }

    public void setWindowTitle(String title) {
        window.setTitle(title);
    }

    public void toggleFullscreen() {
        window.toggleFullscreen();
    }

    public void toggleVSync() {
        window.toggleVSync();
    }

}
