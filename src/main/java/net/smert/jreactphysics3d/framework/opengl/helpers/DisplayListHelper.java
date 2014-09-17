package net.smert.jreactphysics3d.framework.opengl.helpers;

import org.lwjgl.opengl.GL11;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class DisplayListHelper {

    public final void begin(int id) {
        GL11.glNewList(id, GL11.GL_COMPILE);
    }

    public final void call(int id) {
        GL11.glCallList(id);
    }

    public final int create() {
        return GL11.glGenLists(1);
    }

    public final void delete(int id) {
        GL11.glDeleteLists(id, 1);
    }

    public final void end() {
        GL11.glEndList();
    }

}
