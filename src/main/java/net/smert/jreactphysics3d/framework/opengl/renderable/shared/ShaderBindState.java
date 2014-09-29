package net.smert.jreactphysics3d.framework.opengl.renderable.shared;

import net.smert.jreactphysics3d.framework.opengl.Shader;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class ShaderBindState {

    private float textureFlag;
    private Shader shader;

    public ShaderBindState() {
        reset();
    }

    private void setShaderUniformTextureFlag(float flag) {
        //shader.setUniformTextureFlag(flag);
    }

    public final void reset() {
    }

    public void setTextureFlag(float flag) {
        if (textureFlag != flag) {
            textureFlag = flag;
            setShaderUniformTextureFlag(flag);
        }
    }

}
