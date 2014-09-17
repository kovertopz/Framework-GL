package net.smert.jreactphysics3d.framework.opengl.constants;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public final class TextureClamping {

    public final static int CLAMP = GL11.GL_CLAMP;
    public final static int CLAMP_TO_EDGE = GL12.GL_CLAMP_TO_EDGE;
    public final static int REPEAT = GL11.GL_REPEAT;

    private TextureClamping() {
    }

}
