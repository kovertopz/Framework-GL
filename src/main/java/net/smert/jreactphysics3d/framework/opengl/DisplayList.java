package net.smert.jreactphysics3d.framework.opengl;

import net.smert.jreactphysics3d.framework.opengl.helpers.DisplayListHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class DisplayList {

    private final static Logger log = LoggerFactory.getLogger(DisplayList.class);

    private int displayListID;
    private static DisplayListHelper displayListHelper;

    public DisplayList() {
        displayListID = 0;
    }

    public void create() {
        destroy();
        displayListID = displayListHelper.create();
        log.debug("Created a new display list with ID: {}", displayListID);
    }

    public void destroy() {
        if (displayListID != 0) {
            displayListHelper.delete(displayListID);
            log.debug("Deleted a display list with ID: {}", displayListID);
        }
    }

    public int getDisplayListID() {
        return displayListID;
    }

    public static void SetDisplayListHelper(DisplayListHelper displayListHelper) {
        DisplayList.displayListHelper = displayListHelper;
    }

}
