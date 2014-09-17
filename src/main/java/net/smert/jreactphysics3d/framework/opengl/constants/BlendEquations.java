package net.smert.jreactphysics3d.framework.opengl.constants;

import org.lwjgl.opengl.GL14;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public final class BlendEquations {

    public final static int ADD = GL14.GL_FUNC_ADD;
    public final static int MAX = GL14.GL_MAX;
    public final static int MIN = GL14.GL_MIN;
    public final static int REVERSE_SUBTRACT = GL14.GL_FUNC_REVERSE_SUBTRACT;
    public final static int SUBTRACT = GL14.GL_FUNC_SUBTRACT;

    private BlendEquations() {
    }

}
