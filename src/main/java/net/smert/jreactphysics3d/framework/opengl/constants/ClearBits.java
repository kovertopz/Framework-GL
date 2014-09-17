package net.smert.jreactphysics3d.framework.opengl.constants;

import org.lwjgl.opengl.GL11;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public final class ClearBits {

    public static final int COLOR_BUFFER_BIT = GL11.GL_COLOR_BUFFER_BIT;
    public static final int DEPTH_BUFFER_BIT = GL11.GL_DEPTH_BUFFER_BIT;
    public static final int STENCIL_BUFFER_BIT = GL11.GL_STENCIL_BUFFER_BIT;

    private ClearBits() {
    }

}
