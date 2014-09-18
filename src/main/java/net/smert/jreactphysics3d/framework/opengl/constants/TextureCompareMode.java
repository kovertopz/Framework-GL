package net.smert.jreactphysics3d.framework.opengl.constants;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class TextureCompareMode {

    public final static int COMPARE_R_TO_TEXTURE = GL14.GL_COMPARE_R_TO_TEXTURE;
    public final static int NONE = GL11.GL_NONE;

    private TextureCompareMode() {
    }

}
