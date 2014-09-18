package net.smert.jreactphysics3d.framework.opengl.constants;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public final class TextureFormats {

    public final static int DEPTH_COMPONENT = GL11.GL_DEPTH_COMPONENT;
    public final static int DEPTH_STENCIL = GL30.GL_DEPTH_STENCIL;
    public final static int R = GL11.GL_RED;
    public final static int RG = GL30.GL_RG;
    public final static int RGB = GL11.GL_RGB;
    public final static int RGBA = GL11.GL_RGBA;

    private TextureFormats() {
    }

}
