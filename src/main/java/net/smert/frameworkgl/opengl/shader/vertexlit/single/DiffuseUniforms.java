package net.smert.frameworkgl.opengl.shader.vertexlit.single;

import net.smert.frameworkgl.math.Vector4f;
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
    private int uniformGlobalAmbientLightID;
    private int uniformRadiusID;
    private final LightAndMaterialUniforms lightAndMaterialUniforms;

    public DiffuseUniforms(int programID) {
        this.programID = programID;
        lightAndMaterialUniforms = new LightAndMaterialUniforms(programID);
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
        Vector4f ambient = ambientLight.getAmbient();
        GL.shaderUniformHelper.setUniform(uniformGlobalAmbientLightID,
                ambient.getX(), ambient.getY(), ambient.getZ(), ambient.getW());
    }

    public void setLight(GLLight glLight) {
        lightAndMaterialUniforms.setLight(glLight);
    }

    public void setMaterialLight(MaterialLight materialLight) {
        lightAndMaterialUniforms.setMaterialLight(materialLight);
    }

    public void setRadius(float radius) {
        GL.shaderUniformHelper.setUniform(uniformRadiusID, radius);
    }

    public void updateUniformLocations() {
        lightAndMaterialUniforms.updateUniformLocations();
        uniformColorMaterialAmbientID = GL.shaderUniformHelper.getUniformLocation(programID, "uColorMaterialAmbient");
        uniformColorMaterialDiffuseID = GL.shaderUniformHelper.getUniformLocation(programID, "uColorMaterialDiffuse");
        uniformColorMaterialEmissionID = GL.shaderUniformHelper.getUniformLocation(programID, "uColorMaterialEmission");
        uniformGlobalAmbientLightID = GL.shaderUniformHelper.getUniformLocation(programID, "uGlobalAmbientLight");
        uniformRadiusID = GL.shaderUniformHelper.getUniformLocation(programID, "uRadius");
    }

}
