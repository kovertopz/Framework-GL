package net.smert.jreactphysics3d.framework.opengl.helpers;

import org.lwjgl.opengl.GL11;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class RenderHelper {

    public void begin(int primitive) {
        GL11.glBegin(primitive);
    }

    public void color4(float r, float g, float b, float a) {
        GL11.glColor4f(r, g, b, a);
    }

    public void end() {
        GL11.glEnd();
    }

    public void normal3(float x, float y, float z) {
        GL11.glNormal3f(x, y, z);
    }

    public void texCoord2(float s, float t) {
        GL11.glTexCoord2f(s, t);
    }

    public void vertex3(float x, float y, float z) {
        GL11.glVertex3f(x, y, z);
    }

}
