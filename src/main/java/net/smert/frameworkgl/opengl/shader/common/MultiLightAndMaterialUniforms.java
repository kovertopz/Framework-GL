/**
 * Copyright 2012 Jason Sorensen (sorensenj@smert.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package net.smert.frameworkgl.opengl.shader.common;

import java.util.List;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.math.Vector4f;
import net.smert.frameworkgl.opengl.AmbientLight;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.GLLight;
import net.smert.frameworkgl.opengl.MaterialLight;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class MultiLightAndMaterialUniforms {

    private final static int MAX_LIGHTS = 32; // Must match "#define MAX_LIGHTS" in GLSL shader
    private final static Vector3f eyePosition = new Vector3f();
    private final static Vector3f spotEyeDirection = new Vector3f();
    private final static Vector3f spotWorldDirection = new Vector3f();
    private final static Vector3f worldPosition = new Vector3f();

    private final int programID;
    private int uniformGlobalAmbientLightID;
    private int uniformNumberOfLightsID;
    private final int[] uniformMaterialLightIDs;
    private final int[][] uniformLightIDs;

    public MultiLightAndMaterialUniforms(int programID) {
        this.programID = programID;
        uniformMaterialLightIDs = new int[5];
        uniformLightIDs = new int[MAX_LIGHTS][];
        for (int i = 0, max = uniformLightIDs.length; i < max; i++) {
            uniformLightIDs[i] = new int[12];
        }
    }

    public void setAmbientLight(AmbientLight ambientLight) {
        Vector4f ambient = ambientLight.getAmbient();
        GL.shaderUniformHelper.setUniform(uniformGlobalAmbientLightID,
                ambient.getX(), ambient.getY(), ambient.getZ(), ambient.getW());
    }

    public void setLight(int index, GLLight glLight) {
        Vector4f spotDirection = glLight.getSpotDirection();
        Vector4f ambient = glLight.getAmbient();
        Vector4f diffuse = glLight.getDiffuse();
        Vector4f position = glLight.getPosition();
        Vector4f specular = glLight.getSpecular();

        worldPosition.set(position);
        if (position.getW() != 0.0f) {
            GL.matrixHelper.getViewMatrix().multiplyOut(worldPosition, eyePosition);
        } else {
            GL.matrixHelper.getViewMatrix().multiplyDirectionOut(worldPosition, eyePosition);
        }
        spotWorldDirection.set(spotDirection);
        GL.matrixHelper.getViewMatrix().multiplyDirectionOut(spotWorldDirection, spotEyeDirection);

        GL.shaderUniformHelper.setUniform(uniformLightIDs[index][0], glLight.getConstantAttenuation());
        GL.shaderUniformHelper.setUniform(uniformLightIDs[index][1], glLight.getLinearAttenuation());
        GL.shaderUniformHelper.setUniform(uniformLightIDs[index][2], glLight.getQuadraticAttenuation());
        GL.shaderUniformHelper.setUniform(uniformLightIDs[index][3], glLight.getRadius());
        GL.shaderUniformHelper.setUniform(uniformLightIDs[index][4], glLight.getSpotInnerCutoffCos());
        GL.shaderUniformHelper.setUniform(uniformLightIDs[index][5], glLight.getSpotOuterCutoffCos());
        GL.shaderUniformHelper.setUniform(uniformLightIDs[index][6], (float) glLight.getSpotExponent());
        GL.shaderUniformHelper.setUniform(uniformLightIDs[index][7],
                spotEyeDirection.getX(), spotEyeDirection.getY(), spotEyeDirection.getZ());
        GL.shaderUniformHelper.setUniform(uniformLightIDs[index][8],
                ambient.getX(), ambient.getY(), ambient.getZ(), ambient.getW());
        GL.shaderUniformHelper.setUniform(uniformLightIDs[index][9],
                diffuse.getX(), diffuse.getY(), diffuse.getZ(), diffuse.getW());
        GL.shaderUniformHelper.setUniform(uniformLightIDs[index][10],
                eyePosition.getX(), eyePosition.getY(), eyePosition.getZ(), position.getW());
        GL.shaderUniformHelper.setUniform(uniformLightIDs[index][11],
                specular.getX(), specular.getY(), specular.getZ(), specular.getW());
    }

    public void setLights(List<GLLight> glLights) {
        int numberOfLights = 0;
        for (GLLight light : glLights) {
            if (numberOfLights >= MAX_LIGHTS) {
                break;
            }
            setLight(numberOfLights, light);
            numberOfLights++;
        }
        setNumberOfLights(numberOfLights);
    }

    public void setMaterialLight(MaterialLight materialLight) {
        Vector4f ambient = materialLight.getAmbient();
        Vector4f diffuse = materialLight.getDiffuse();
        Vector4f emission = materialLight.getEmission();
        Vector4f specular = materialLight.getSpecular();
        GL.shaderUniformHelper.setUniform(uniformMaterialLightIDs[0], (float) materialLight.getShininess());
        GL.shaderUniformHelper.setUniform(uniformMaterialLightIDs[1],
                ambient.getX(), ambient.getY(), ambient.getZ(), ambient.getW());
        GL.shaderUniformHelper.setUniform(uniformMaterialLightIDs[2],
                diffuse.getX(), diffuse.getY(), diffuse.getZ(), diffuse.getW());
        GL.shaderUniformHelper.setUniform(uniformMaterialLightIDs[3],
                emission.getX(), emission.getY(), emission.getZ(), emission.getW());
        GL.shaderUniformHelper.setUniform(uniformMaterialLightIDs[4],
                specular.getX(), specular.getY(), specular.getZ(), specular.getW());
    }

    public void setNumberOfLights(int numberOfLights) {
        GL.shaderUniformHelper.setUniform(uniformNumberOfLightsID, numberOfLights);
    }

    public void updateUniformLocations() {
        uniformGlobalAmbientLightID = GL.shaderUniformHelper.getUniformLocation(programID, "uGlobalAmbientLight");
        uniformNumberOfLightsID = GL.shaderUniformHelper.getUniformLocation(programID, "uNumberOfLights");

        uniformMaterialLightIDs[0] = GL.shaderUniformHelper.getUniformLocation(programID, "uMaterialLight.shininess");
        uniformMaterialLightIDs[1] = GL.shaderUniformHelper.getUniformLocation(programID, "uMaterialLight.ambient");
        uniformMaterialLightIDs[2] = GL.shaderUniformHelper.getUniformLocation(programID, "uMaterialLight.diffuse");
        uniformMaterialLightIDs[3] = GL.shaderUniformHelper.getUniformLocation(programID, "uMaterialLight.emission");
        uniformMaterialLightIDs[4] = GL.shaderUniformHelper.getUniformLocation(programID, "uMaterialLight.specular");

        for (int i = 0, max = uniformLightIDs.length; i < max; i++) {
            uniformLightIDs[i][0] = GL.shaderUniformHelper.getUniformLocation(programID, "uLights[" + i + "].constantAttenuation");
            uniformLightIDs[i][1] = GL.shaderUniformHelper.getUniformLocation(programID, "uLights[" + i + "].linearAttenuation");
            uniformLightIDs[i][2] = GL.shaderUniformHelper.getUniformLocation(programID, "uLights[" + i + "].quadraticAttenuation");
            uniformLightIDs[i][3] = GL.shaderUniformHelper.getUniformLocation(programID, "uLights[" + i + "].radius");
            uniformLightIDs[i][4] = GL.shaderUniformHelper.getUniformLocation(programID, "uLights[" + i + "].spotInnerCutoffCos");
            uniformLightIDs[i][5] = GL.shaderUniformHelper.getUniformLocation(programID, "uLights[" + i + "].spotOuterCutoffCos");
            uniformLightIDs[i][6] = GL.shaderUniformHelper.getUniformLocation(programID, "uLights[" + i + "].spotExponent");
            uniformLightIDs[i][7] = GL.shaderUniformHelper.getUniformLocation(programID, "uLights[" + i + "].spotEyeDirection");
            uniformLightIDs[i][8] = GL.shaderUniformHelper.getUniformLocation(programID, "uLights[" + i + "].ambient");
            uniformLightIDs[i][9] = GL.shaderUniformHelper.getUniformLocation(programID, "uLights[" + i + "].diffuse");
            uniformLightIDs[i][10] = GL.shaderUniformHelper.getUniformLocation(programID, "uLights[" + i + "].eyePosition");
            uniformLightIDs[i][11] = GL.shaderUniformHelper.getUniformLocation(programID, "uLights[" + i + "].specular");
        }
    }

}
