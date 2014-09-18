package net.smert.jreactphysics3d.framework.opengl.constants;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class StencilOperations {

    public final static int BACK = GL11.GL_BACK;
    public final static int FRONT = GL11.GL_FRONT;
    public final static int FRONT_AND_BACK = GL11.GL_FRONT_AND_BACK;
    public final static int DECR = GL11.GL_DECR;
    public final static int DECR_WRAP = GL14.GL_DECR_WRAP;
    public final static int INCR = GL11.GL_INCR;
    public final static int INCR_WRAP = GL14.GL_INCR_WRAP;
    public final static int INVERT = GL11.GL_INVERT;
    public final static int KEEP = GL11.GL_KEEP;
    public final static int REPLACE = GL11.GL_REPLACE;
    public final static int ZERO = GL11.GL_ZERO;

    private StencilOperations() {
    }

}
