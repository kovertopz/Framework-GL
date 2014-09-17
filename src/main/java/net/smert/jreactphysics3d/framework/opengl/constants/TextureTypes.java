package net.smert.jreactphysics3d.framework.opengl.constants;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public final class TextureTypes {

    public final static int FLOAT = GL11.GL_FLOAT;
    public final static int UNSIGNED_BYTE = GL11.GL_UNSIGNED_BYTE;
    public final static int UNSIGNED_INT = GL11.GL_UNSIGNED_INT;
    public final static int UNSIGNED_INT_10_10_10_2 = GL12.GL_UNSIGNED_INT_10_10_10_2;
    public final static int UNSIGNED_INT_24_8 = GL30.GL_UNSIGNED_INT_24_8;
    public final static int UNSIGNED_SHORT = GL11.GL_UNSIGNED_SHORT;

    private TextureTypes() {
    }

}
