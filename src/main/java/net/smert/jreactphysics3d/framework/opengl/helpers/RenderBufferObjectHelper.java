package net.smert.jreactphysics3d.framework.opengl.helpers;

import org.lwjgl.opengl.GL30;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class RenderBufferObjectHelper {

    public void bind(int rboid) {
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, rboid);
    }

    public int create() {
        return GL30.glGenRenderbuffers();
    }

    public void delete(int rboid) {
        GL30.glDeleteRenderbuffers(rboid);
    }

    public void storage(int internalformat, int width, int height) {
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, internalformat, width, height);
    }

    public void unbind() {
        GL30.glBindFramebuffer(GL30.GL_RENDERBUFFER, 0);
    }

}
