package net.smert.jreactphysics3d.framework.opengl;

import net.smert.jreactphysics3d.framework.opengl.constants.StencilOperations;
import org.lwjgl.opengl.GL20;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class OpenGL2 {

    public void setStencilOpSeparateBackKeepIncrKeep() {
        GL20.glStencilOpSeparate(StencilOperations.BACK, StencilOperations.KEEP, StencilOperations.INCR, StencilOperations.KEEP);
    }

    public void setStencilOpSeparateFrontKeepDecrKeep() {
        GL20.glStencilOpSeparate(StencilOperations.FRONT, StencilOperations.KEEP, StencilOperations.DECR, StencilOperations.KEEP);
    }

}
