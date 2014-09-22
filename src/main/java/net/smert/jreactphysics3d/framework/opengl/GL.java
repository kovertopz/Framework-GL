package net.smert.jreactphysics3d.framework.opengl;

import net.smert.jreactphysics3d.framework.opengl.helpers.DisplayListHelper;
import net.smert.jreactphysics3d.framework.opengl.helpers.FrameBufferObjectHelper;
import net.smert.jreactphysics3d.framework.opengl.helpers.LegacyRenderHelper;
import net.smert.jreactphysics3d.framework.opengl.helpers.RenderBufferObjectHelper;
import net.smert.jreactphysics3d.framework.opengl.helpers.ShaderHelper;
import net.smert.jreactphysics3d.framework.opengl.helpers.TextureHelper;
import net.smert.jreactphysics3d.framework.opengl.helpers.VertexBufferObjectHelper;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class GL {

    public static DisplayListHelper displayListHelper;
    public static FrameBufferObjectHelper fboHelper;
    public static OpenGL1 o1;
    public static OpenGL2 o2;
    public static OpenGL3 o3;
    public static LegacyRenderHelper renderHelper;
    public static RenderBufferObjectHelper rboHelper;
    public static ShaderHelper shaderHelper;
    public static TextureHelper textureHelper;
    public static VertexBufferObjectHelper vboHelper;

}
