package net.smert.jreactphysics3d.framework.opengl.constants;

import org.lwjgl.opengl.GL11;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class GLTypes {

    public final static int BYTE = GL11.GL_BYTE;
    public final static int UNSIGNED_BYTE = GL11.GL_UNSIGNED_BYTE;
    public final static int DOUBLE = GL11.GL_DOUBLE;
    public final static int FLOAT = GL11.GL_FLOAT;
    public final static int INT = GL11.GL_INT;
    public final static int UNSIGNED_INT = GL11.GL_UNSIGNED_INT;
    public final static int SHORT = GL11.GL_SHORT;
    public final static int UNSIGNED_SHORT = GL11.GL_UNSIGNED_SHORT;

    private GLTypes() {
    }

    public static String ConvertToString(int glType) {

        String string;

        switch (glType) {
            case BYTE:
                string = "BYTE";
                break;
            case UNSIGNED_BYTE:
                string = "UNSIGNED_BYTE";
                break;
            case DOUBLE:
                string = "DOUBLE";
                break;
            case FLOAT:
                string = "FLOAT";
                break;
            case INT:
                string = "INT";
                break;
            case UNSIGNED_INT:
                string = "UNSIGNED_INT";
                break;
            case SHORT:
                string = "SHORT";
                break;
            case UNSIGNED_SHORT:
                string = "UNSIGNED_SHORT";
                break;

            default:
                throw new IllegalArgumentException("Unknown GL type: " + glType);
        }

        return string;
    }

}
