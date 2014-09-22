package net.smert.jreactphysics3d.framework.opengl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class DisplayList {

    private final static Logger log = LoggerFactory.getLogger(DisplayList.class);

    private int displayListID;

    public DisplayList() {
        displayListID = 0;
    }

    public void create() {
        destroy();
        displayListID = GL.displayListHelper.create();
        log.debug("Created a new display list with ID: {}", displayListID);
    }

    public void destroy() {
        if (displayListID != 0) {
            GL.displayListHelper.delete(displayListID);
            log.debug("Deleted a display list with ID: {}", displayListID);
        }
    }

    public int getDisplayListID() {
        return displayListID;
    }

}
