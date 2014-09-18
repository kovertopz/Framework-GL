package net.smert.jreactphysics3d.framework.opengl.constants;

import org.lwjgl.opengl.GL11;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class StencilFunctions {

    public final static int ALWAYS = GL11.GL_ALWAYS;
    public final static int EQUAL = GL11.GL_EQUAL;
    public final static int GREATER = GL11.GL_GREATER;
    public final static int GEQUAL = GL11.GL_GEQUAL;
    public final static int LESS = GL11.GL_LESS;
    public final static int LEQUAL = GL11.GL_LEQUAL;
    public final static int NEVER = GL11.GL_NEVER;
    public final static int NOTEQUAL = GL11.GL_NOTEQUAL;

    private StencilFunctions() {
    }

}
