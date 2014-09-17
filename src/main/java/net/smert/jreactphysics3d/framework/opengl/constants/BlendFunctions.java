package net.smert.jreactphysics3d.framework.opengl.constants;

import org.lwjgl.opengl.GL11;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public final class BlendFunctions {

    public final static int CONSTANT_ALPHA = GL11.GL_CONSTANT_ALPHA;
    public final static int CONSTANT_COLOR = GL11.GL_CONSTANT_COLOR;
    public final static int DST_ALPHA = GL11.GL_DST_ALPHA;
    public final static int DST_COLOR = GL11.GL_DST_COLOR;
    public final static int ONE = GL11.GL_ONE;
    public final static int ONE_MINUS_CONSTANT_ALPHA = GL11.GL_ONE_MINUS_CONSTANT_ALPHA;
    public final static int ONE_MINUS_CONSTANT_COLOR = GL11.GL_ONE_MINUS_CONSTANT_COLOR;
    public final static int ONE_MINUS_DST_ALPHA = GL11.GL_ONE_MINUS_DST_ALPHA;
    public final static int ONE_MINUS_DST_COLOR = GL11.GL_ONE_MINUS_DST_COLOR;
    public final static int ONE_MINUS_SRC_ALPHA = GL11.GL_ONE_MINUS_SRC_ALPHA;
    public final static int ONE_MINUS_SRC_COLOR = GL11.GL_ONE_MINUS_SRC_COLOR;
    public final static int SRC_ALPHA = GL11.GL_SRC_ALPHA;
    public final static int SRC_ALPHA_SATURATE = GL11.GL_SRC_ALPHA_SATURATE;
    public final static int SRC_COLOR = GL11.GL_SRC_COLOR;
    public final static int ZERO = GL11.GL_ZERO;

    private BlendFunctions() {
    }

}
