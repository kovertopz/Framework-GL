package net.smert.jreactphysics3d.framework.opengl.constants;

import org.lwjgl.opengl.GL11;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Primitives {

    public final static int LINES = GL11.GL_LINES;
    public final static int LINE_LOOP = GL11.GL_LINE_LOOP;
    public final static int LINE_STRIP = GL11.GL_LINE_STRIP;
    public final static int POINTS = GL11.GL_POINTS;
    public final static int POLYGON = GL11.GL_POLYGON;
    public final static int QUADS = GL11.GL_QUADS;
    public final static int QUAD_STRIP = GL11.GL_QUAD_STRIP;
    public final static int TRIANGLES = GL11.GL_TRIANGLES;
    public final static int TRIANGLE_FAN = GL11.GL_TRIANGLE_FAN;
    public final static int TRIANGLE_STRIP = GL11.GL_TRIANGLE_STRIP;

    private Primitives() {
    }

}
