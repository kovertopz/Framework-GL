package net.smert.jreactphysics3d.framework.opengl.constants;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL43;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public final class ShaderTypes {

    public final static int COMPUTE_SHADER = GL43.GL_COMPUTE_SHADER;
    public final static int FRAGMENT_SHADER = GL20.GL_FRAGMENT_SHADER;
    public final static int GEOMETRY_SHADER = GL32.GL_GEOMETRY_SHADER;
    public final static int TESS_CONTROL_SHADER = GL40.GL_TESS_CONTROL_SHADER;
    public final static int TESS_EVALUATION_SHADER = GL40.GL_TESS_EVALUATION_SHADER;
    public final static int VERTEX_SHADER = GL20.GL_VERTEX_SHADER;

    private ShaderTypes() {
    }

}
