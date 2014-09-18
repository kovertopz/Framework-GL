package net.smert.jreactphysics3d.framework.opengl.constants;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class TextureTargets {

    public final static int TEXTURE_2D = GL11.GL_TEXTURE_2D;
    public final static int TEXTURE_3D = GL12.GL_TEXTURE_3D;
    public final static int TEXTURE_CUBE_MAP = GL13.GL_TEXTURE_CUBE_MAP;
    public final static int TEXTURE_CUBE_MAP_NEGATIVE_X = GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X;
    public final static int TEXTURE_CUBE_MAP_POSITIVE_X = GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
    public final static int TEXTURE_CUBE_MAP_NEGATIVE_Y = GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y;
    public final static int TEXTURE_CUBE_MAP_POSITIVE_Y = GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y;
    public final static int TEXTURE_CUBE_MAP_NEGATIVE_Z = GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z;
    public final static int TEXTURE_CUBE_MAP_POSITIVE_Z = GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z;

    private TextureTargets() {
    }

}
