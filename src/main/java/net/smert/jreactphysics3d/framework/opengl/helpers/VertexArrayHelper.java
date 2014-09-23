package net.smert.jreactphysics3d.framework.opengl.helpers;

import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VertexArrayHelper {

    public void bindColors(int size, int type, ByteBuffer byteBuffer) {
        GL11.glColorPointer(size, type, 0, byteBuffer);
    }

    public void bindNormals(int type, ByteBuffer byteBuffer) {
        GL11.glNormalPointer(type, 0, byteBuffer);
    }

    public void bindVertices(int size, int type, ByteBuffer byteBuffer) {
        GL11.glVertexPointer(size, type, 0, byteBuffer);
    }

    public void bindTextureCoordinates(int size, int type, ByteBuffer byteBuffer) {
        GL11.glTexCoordPointer(size, type, 0, byteBuffer);
    }

    public void disableColors() {
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
    }

    public void disableNormals() {
        GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
    }

    public void disableTextureCoordinates() {
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
    }

    public void disableVertices() {
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
    }

    public void drawArrays(int mode, int first, int count) {
        GL11.glDrawArrays(mode, first, count);
    }

    public void drawElements(int mode, int count, int type, ByteBuffer byteBuffer) {
        GL11.glDrawElements(mode, count, type, byteBuffer);
    }

    public void enableColors() {
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
    }

    public void enableNormals() {
        GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
    }

    public void enableTextureCoordinates() {
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
    }

    public void enableVertices() {
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
    }

}
