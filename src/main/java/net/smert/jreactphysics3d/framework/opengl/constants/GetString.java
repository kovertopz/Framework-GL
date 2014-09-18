package net.smert.jreactphysics3d.framework.opengl.constants;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class GetString {

    public final static int EXTENSIONS = GL11.GL_EXTENSIONS;
    public final static int RENDERER = GL11.GL_RENDERER;
    public final static int SHADING_LANGUAGE_VERSION = GL20.GL_SHADING_LANGUAGE_VERSION;
    public final static int VENDOR = GL11.GL_VENDOR;
    public final static int VERSION = GL11.GL_VERSION;

    private GetString() {
    }

}
