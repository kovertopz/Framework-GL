package net.smert.frameworkgl.opengl.shader.vertexlit.single;

import net.smert.frameworkgl.opengl.shader.common.SingleLightAndMaterialUniforms;
import net.smert.frameworkgl.opengl.AmbientLight;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.GLLight;
import net.smert.frameworkgl.opengl.MaterialLight;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class DiffuseUniforms {

    private final int programID;
    private int uniformColorMaterialAmbientID;
    private int uniformColorMaterialDiffuseID;
    private int uniformColorMaterialEmissionID;
    private final SingleLightAndMaterialUniforms singleLightAndMaterialUniforms;

    public DiffuseUniforms(int programID) {
        this.programID = programID;
        singleLightAndMaterialUniforms = new SingleLightAndMaterialUniforms(programID);
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

    public void setAmbientLight(AmbientLight ambientLight) {
        singleLightAndMaterialUniforms.setAmbientLight(ambientLight);
    }

    public void setLight(GLLight glLight) {
        singleLightAndMaterialUniforms.setLight(glLight);
    }

    public void setMaterialLight(MaterialLight materialLight) {
        singleLightAndMaterialUniforms.setMaterialLight(materialLight);
    }

    public void updateUniformLocations() {
        singleLightAndMaterialUniforms.updateUniformLocations();
        uniformColorMaterialAmbientID = GL.shaderUniformHelper.getUniformLocation(programID, "uColorMaterialAmbient");
        uniformColorMaterialDiffuseID = GL.shaderUniformHelper.getUniformLocation(programID, "uColorMaterialDiffuse");
        uniformColorMaterialEmissionID = GL.shaderUniformHelper.getUniformLocation(programID, "uColorMaterialEmission");
    }

}
