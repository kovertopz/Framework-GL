package net.smert.frameworkgl.opengl.shader.vertexlit.single;

import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.shader.DefaultShaderUniforms;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class BlinnPhongSpecularUniforms extends DefaultShaderUniforms {

    private int uniformColorMaterialAmbientID;
    private int uniformColorMaterialDiffuseID;
    private int uniformColorMaterialEmissionID;
    private int uniformColorMaterialSpecularID;
    private int uniformRadiusID;

    public BlinnPhongSpecularUniforms(int programID) {
        super(programID);
    }

    public void setColorMaterialAmbient(float colorMaterialAmbient) {
        GL.shaderUniformHelper.setUniform(uniformColorMaterialAmbientID, colorMaterialAmbient);
    }

    public void setColorMaterialDiffuse(float colorMaterialDiffuse) {
        GL.shaderUniformHelper.setUniform(uniformColorMaterialDiffuseID, colorMaterialDiffuse);
    }

    public void setColorMaterialEmission(float colorMaterialEmission) {
        GL.shaderUniformHelper.setUniform(uniformColorMaterialEmissionID, colorMaterialEmission);
    }

    public void setColorMaterialSpecular(float colorMaterialSpecular) {
        GL.shaderUniformHelper.setUniform(uniformColorMaterialSpecularID, colorMaterialSpecular);
    }

    public void setRadius(float radius) {
        GL.shaderUniformHelper.setUniform(uniformRadiusID, radius);
    }

    @Override
    public void updateUniformLocations() {
        super.updateUniformLocations();
        uniformColorMaterialAmbientID = GL.shaderUniformHelper.getUniformLocation(programID, "uColorMaterialAmbient");
        uniformColorMaterialDiffuseID = GL.shaderUniformHelper.getUniformLocation(programID, "uColorMaterialDiffuse");
        uniformColorMaterialEmissionID = GL.shaderUniformHelper.getUniformLocation(programID, "uColorMaterialEmission");
        uniformColorMaterialSpecularID = GL.shaderUniformHelper.getUniformLocation(programID, "uColorMaterialSpecular");
        uniformRadiusID = GL.shaderUniformHelper.getUniformLocation(programID, "uRadius");
    }

}
