package net.smert.jreactphysics3d.framework.opengl;

import net.smert.jreactphysics3d.framework.opengl.helpers.VertexBufferObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VertexBufferObject {

    private final static Logger log = LoggerFactory.getLogger(VertexBufferObject.class);

    private int vboID;
    private static VertexBufferObjectHelper vboHelper;

    public VertexBufferObject() {
        vboID = 0;
    }

    public void create() {
        destroy();
        vboID = vboHelper.create();
        log.debug("Created a new VBO with ID: {}", vboID);
    }

    public void destroy() {
        if (vboID != 0) {
            vboHelper.delete(vboID);
            log.debug("Deleted a VBO with ID: {}", vboID);
        }
    }

    public int getVboID() {
        return vboID;
    }

    public static void SetVertexBufferObjectHelper(VertexBufferObjectHelper vboHelper) {
        VertexBufferObject.vboHelper = vboHelper;
    }

}