/**
 * Copyright 2014 Jason Sorensen (sorensenj@smert.net)
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
package net.smert.frameworkgl.examples.gl3multipassvertexlit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.Screen;
import net.smert.frameworkgl.examples.common.DynamicMeshWorld;
import net.smert.frameworkgl.gameobjects.AABBGameObject;
import net.smert.frameworkgl.gameobjects.GameObject;
import net.smert.frameworkgl.gameobjects.RenderStatisticsGameObject;
import net.smert.frameworkgl.gameobjects.SimpleOrientationAxisGameObject;
import net.smert.frameworkgl.gameobjects.SkyboxGameObject;
import net.smert.frameworkgl.gameobjects.ViewFrustumGameObject;
import net.smert.frameworkgl.helpers.Keyboard;
import net.smert.frameworkgl.math.AABB;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.math.Vector4f;
import net.smert.frameworkgl.opengl.AmbientLight;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.GLLight;
import net.smert.frameworkgl.opengl.MaterialLight;
import net.smert.frameworkgl.opengl.camera.Camera;
import net.smert.frameworkgl.opengl.camera.CameraController;
import net.smert.frameworkgl.opengl.camera.FrustumCullingClipSpaceSymmetrical;
import net.smert.frameworkgl.opengl.constants.GetString;
import net.smert.frameworkgl.opengl.shader.AbstractShader;
import net.smert.frameworkgl.opengl.shader.basic.DiffuseTextureShader;
import net.smert.frameworkgl.opengl.shader.basic.SkyboxShader;
import net.smert.frameworkgl.opengl.shader.vertexlit.single.BlinnPhongSpecularDirectionalShader;
import net.smert.frameworkgl.opengl.shader.vertexlit.single.BlinnPhongSpecularPointShader;
import net.smert.frameworkgl.opengl.shader.vertexlit.single.BlinnPhongSpecularSpotShader;
import net.smert.frameworkgl.opengl.shader.vertexlit.single.DiffuseDirectionalShader;
import net.smert.frameworkgl.opengl.shader.vertexlit.single.DiffusePointShader;
import net.smert.frameworkgl.opengl.shader.vertexlit.single.DiffuseSpotShader;
import net.smert.frameworkgl.opengl.shader.vertexlit.single.PhongSpecularDirectionalShader;
import net.smert.frameworkgl.opengl.shader.vertexlit.single.PhongSpecularPointShader;
import net.smert.frameworkgl.opengl.shader.vertexlit.single.PhongSpecularSpotShader;
import net.smert.frameworkgl.utils.FpsTimer;
import net.smert.frameworkgl.utils.MemoryUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class MultipassVertexLit extends Screen {

    private final static Logger log = LoggerFactory.getLogger(MultipassVertexLit.class);

    private boolean renderAabbs;
    private boolean renderSimpleOrientationAxis;
    private boolean wireframe;
    private float spotCutoff;
    private int shaderIndex;
    private AbstractShader currentShader;
    private AABBGameObject aabbGameObject;
    private AmbientLight ambientLight;
    private BlinnPhongSpecularDirectionalShader vertexLitSingleBlinnPhongSpecularDirectionalShader;
    private BlinnPhongSpecularPointShader vertexLitSingleBlinnPhongSpecularPointShader;
    private BlinnPhongSpecularSpotShader vertexLitSingleBlinnPhongSpecularSpotShader;
    private Camera camera;
    private CameraController cameraController;
    private DiffuseDirectionalShader vertexLitSingleDiffuseDirectionalShader;
    private DiffusePointShader vertexLitSingleDiffusePointShader;
    private DiffuseSpotShader vertexLitSingleDiffuseSpotShader;
    private DiffuseTextureShader diffuseTextureShader;
    private DynamicMeshWorld dynamicMeshesWorld;
    private FpsTimer fpsTimer;
    private GLLight currentDirectionalLight;
    private GLLight currentPointLight;
    private GLLight currentSpotLight;
    private final List<GameObject> gameObjectsToRender;
    private List<GLLight> currentLights;
    private final List<GLLight> directionalLights;
    private final List<GLLight> pointLights;
    private final List<GLLight> spotLights;
    private MaterialLight materialLight;
    private MemoryUsage memoryUsage;
    private PhongSpecularDirectionalShader vertexLitSinglePhongSpecularDirectionalShader;
    private PhongSpecularPointShader vertexLitSinglePhongSpecularPointShader;
    private PhongSpecularSpotShader vertexLitSinglePhongSpecularSpotShader;
    private RenderStatisticsGameObject renderStatisticsGameObject;
    private SimpleOrientationAxisGameObject simpleOrientationAxisGameObject;
    private SkyboxGameObject skyboxGameObject;
    private SkyboxShader skyboxShader;
    private ViewFrustumGameObject viewFrustumGameObject;

    public MultipassVertexLit(String[] args) {
        renderAabbs = false;
        renderSimpleOrientationAxis = false;
        wireframe = false;
        gameObjectsToRender = new ArrayList<>();
        directionalLights = new ArrayList<>();
        pointLights = new ArrayList<>();
        spotLights = new ArrayList<>();
    }

    private void createDirectionalLights() {
        GLLight directionalLight;

        directionalLight = GL.glFactory.createGLLight();
        directionalLight.setDiffuse(new Vector4f(1f, 0f, 0f, 1f));
        directionalLight.setSpecular(new Vector4f(1f, 0f, 0f, 1f));
        directionalLight.setPosition(new Vector4f(0f, 15f, 15f, 0f));
        directionalLight.setRadius(32f);
        directionalLights.add(directionalLight);

        directionalLight = GL.glFactory.createGLLight();
        directionalLight.setDiffuse(new Vector4f(0f, 1f, 0f, 1f));
        directionalLight.setSpecular(new Vector4f(0f, 1f, 0f, 1f));
        directionalLight.setPosition(new Vector4f(-15f, 15f, 0f, 0f));
        directionalLight.setRadius(32f);
        directionalLights.add(directionalLight);

        directionalLight = GL.glFactory.createGLLight();
        directionalLight.setDiffuse(new Vector4f(0f, 0f, 1f, 1f));
        directionalLight.setSpecular(new Vector4f(0f, 0f, 1f, 1f));
        directionalLight.setPosition(new Vector4f(0f, 15f, -15f, 0f));
        directionalLight.setRadius(32f);
        directionalLights.add(directionalLight);

        directionalLight = GL.glFactory.createGLLight();
        directionalLight.setDiffuse(new Vector4f(1f, 1f, 0f, 1f));
        directionalLight.setSpecular(new Vector4f(1f, 1f, 0f, 1f));
        directionalLight.setPosition(new Vector4f(15f, 15f, 0f, 0f));
        directionalLight.setRadius(32f);
        directionalLights.add(directionalLight);

        directionalLight = GL.glFactory.createGLLight();
        directionalLight.setDiffuse(new Vector4f(0f, 1f, 1f, 1f));
        directionalLight.setSpecular(new Vector4f(0f, 1f, 1f, 1f));
        directionalLight.setPosition(new Vector4f(15f, 15f, 15f, 0f));
        directionalLight.setRadius(32f);
        directionalLights.add(directionalLight);

        directionalLight = GL.glFactory.createGLLight();
        directionalLight.setDiffuse(new Vector4f(1f, 0f, 1f, 1f));
        directionalLight.setSpecular(new Vector4f(1f, 0f, 1f, 1f));
        directionalLight.setPosition(new Vector4f(-15f, 15f, 15f, 0f));
        directionalLight.setRadius(32f);
        directionalLights.add(directionalLight);

        directionalLight = GL.glFactory.createGLLight();
        directionalLight.setDiffuse(new Vector4f(.5f, .5f, 0f, 1f));
        directionalLight.setSpecular(new Vector4f(.5f, .5f, 0f, 1f));
        directionalLight.setPosition(new Vector4f(-15f, 15f, -15f, 0f));
        directionalLight.setRadius(32f);
        directionalLights.add(directionalLight);

        directionalLight = GL.glFactory.createGLLight();
        directionalLight.setDiffuse(new Vector4f(0f, .5f, .5f, 1f));
        directionalLight.setSpecular(new Vector4f(0f, .5f, .5f, 1f));
        directionalLight.setPosition(new Vector4f(15f, 15f, -15f, 0f));
        directionalLight.setRadius(32f);
        directionalLights.add(directionalLight);
    }

    private void createPointLights() {
        GLLight pointLight;

        pointLight = GL.glFactory.createGLLight();
        pointLight.setDiffuse(new Vector4f(1f, 0f, 0f, 1f));
        pointLight.setSpecular(new Vector4f(1f, 0f, 0f, 1f));
        pointLight.setPosition(new Vector4f(0f, 15f, 15f, 1f));
        pointLight.setRadius(32f);
        pointLights.add(pointLight);

        pointLight = GL.glFactory.createGLLight();
        pointLight.setDiffuse(new Vector4f(0f, 1f, 0f, 1f));
        pointLight.setSpecular(new Vector4f(0f, 1f, 0f, 1f));
        pointLight.setPosition(new Vector4f(-15f, 15f, 0f, 1f));
        pointLight.setRadius(32f);
        pointLights.add(pointLight);

        pointLight = GL.glFactory.createGLLight();
        pointLight.setDiffuse(new Vector4f(0f, 0f, 1f, 1f));
        pointLight.setSpecular(new Vector4f(0f, 0f, 1f, 1f));
        pointLight.setPosition(new Vector4f(0f, 15f, -15f, 1f));
        pointLight.setRadius(32f);
        pointLights.add(pointLight);

        pointLight = GL.glFactory.createGLLight();
        pointLight.setDiffuse(new Vector4f(1f, 1f, 0f, 1f));
        pointLight.setSpecular(new Vector4f(1f, 1f, 0f, 1f));
        pointLight.setPosition(new Vector4f(15f, 15f, 0f, 1f));
        pointLight.setRadius(32f);
        pointLights.add(pointLight);

        pointLight = GL.glFactory.createGLLight();
        pointLight.setDiffuse(new Vector4f(0f, 1f, 1f, 1f));
        pointLight.setSpecular(new Vector4f(0f, 1f, 1f, 1f));
        pointLight.setPosition(new Vector4f(15f, 15f, 15f, 1f));
        pointLight.setRadius(32f);
        pointLights.add(pointLight);

        pointLight = GL.glFactory.createGLLight();
        pointLight.setDiffuse(new Vector4f(1f, 0f, 1f, 1f));
        pointLight.setSpecular(new Vector4f(1f, 0f, 1f, 1f));
        pointLight.setPosition(new Vector4f(-15f, 15f, 15f, 1f));
        pointLight.setRadius(32f);
        pointLights.add(pointLight);

        pointLight = GL.glFactory.createGLLight();
        pointLight.setDiffuse(new Vector4f(.5f, .5f, 0f, 1f));
        pointLight.setSpecular(new Vector4f(.5f, .5f, 0f, 1f));
        pointLight.setPosition(new Vector4f(-15f, 15f, -15f, 1f));
        pointLight.setRadius(32f);
        pointLights.add(pointLight);

        pointLight = GL.glFactory.createGLLight();
        pointLight.setDiffuse(new Vector4f(0f, .5f, .5f, 1f));
        pointLight.setSpecular(new Vector4f(0f, .5f, .5f, 1f));
        pointLight.setPosition(new Vector4f(15f, 15f, -15f, 1f));
        pointLight.setRadius(32f);
        pointLights.add(pointLight);
    }

    private void createSpotLights() {
        GLLight spotLight;
        spotCutoff = 180f;

        spotLight = GL.glFactory.createGLLight();
        spotLight.setDiffuse(new Vector4f(1f, 0f, 0f, 1f));
        spotLight.setSpecular(new Vector4f(1f, 0f, 0f, 1f));
        spotLight.setPosition(new Vector4f(0f, 15f, 15f, 1f));
        spotLight.setRadius(32f);
        spotLight.setSpotCutoff(spotCutoff);
        spotLight.setSpotDirection(new Vector4f(0f, -15f, -15f, 1f));
        spotLights.add(spotLight);

        spotLight = GL.glFactory.createGLLight();
        spotLight.setDiffuse(new Vector4f(0f, 1f, 0f, 1f));
        spotLight.setSpecular(new Vector4f(0f, 1f, 0f, 1f));
        spotLight.setPosition(new Vector4f(-15f, 15f, 0f, 1f));
        spotLight.setRadius(32f);
        spotLight.setSpotCutoff(spotCutoff);
        spotLight.setSpotDirection(new Vector4f(15f, -15f, 0f, 1f));
        spotLights.add(spotLight);

        spotLight = GL.glFactory.createGLLight();
        spotLight.setDiffuse(new Vector4f(0f, 0f, 1f, 1f));
        spotLight.setSpecular(new Vector4f(0f, 0f, 1f, 1f));
        spotLight.setPosition(new Vector4f(0f, 15f, -15f, 1f));
        spotLight.setRadius(32f);
        spotLight.setSpotCutoff(spotCutoff);
        spotLight.setSpotDirection(new Vector4f(0f, -15f, 15f, 1f));
        spotLights.add(spotLight);

        spotLight = GL.glFactory.createGLLight();
        spotLight.setDiffuse(new Vector4f(1f, 1f, 0f, 1f));
        spotLight.setSpecular(new Vector4f(1f, 1f, 0f, 1f));
        spotLight.setPosition(new Vector4f(15f, 15f, 0f, 1f));
        spotLight.setRadius(32f);
        spotLight.setSpotCutoff(spotCutoff);
        spotLight.setSpotDirection(new Vector4f(-15f, -15f, 0f, 1f));
        spotLights.add(spotLight);

        spotLight = GL.glFactory.createGLLight();
        spotLight.setDiffuse(new Vector4f(0f, 1f, 1f, 1f));
        spotLight.setSpecular(new Vector4f(0f, 1f, 1f, 1f));
        spotLight.setPosition(new Vector4f(15f, 15f, 15f, 1f));
        spotLight.setRadius(32f);
        spotLight.setSpotCutoff(spotCutoff);
        spotLight.setSpotDirection(new Vector4f(-15f, -15f, -15f, 1f));
        spotLights.add(spotLight);

        spotLight = GL.glFactory.createGLLight();
        spotLight.setDiffuse(new Vector4f(1f, 0f, 1f, 1f));
        spotLight.setSpecular(new Vector4f(1f, 0f, 1f, 1f));
        spotLight.setPosition(new Vector4f(-15f, 15f, 15f, 1f));
        spotLight.setRadius(32f);
        spotLight.setSpotCutoff(spotCutoff);
        spotLight.setSpotDirection(new Vector4f(15f, -15f, -15f, 1f));
        spotLights.add(spotLight);

        spotLight = GL.glFactory.createGLLight();
        spotLight.setDiffuse(new Vector4f(.5f, .5f, 0f, 1f));
        spotLight.setSpecular(new Vector4f(.5f, .5f, 0f, 1f));
        spotLight.setPosition(new Vector4f(-15f, 15f, -15f, 1f));
        spotLight.setRadius(32f);
        spotLight.setSpotCutoff(spotCutoff);
        spotLight.setSpotDirection(new Vector4f(15f, -15f, 15f, 1f));
        spotLights.add(spotLight);

        spotLight = GL.glFactory.createGLLight();
        spotLight.setDiffuse(new Vector4f(0f, .5f, .5f, 1f));
        spotLight.setSpecular(new Vector4f(0f, .5f, .5f, 1f));
        spotLight.setPosition(new Vector4f(15f, 15f, -15f, 1f));
        spotLight.setRadius(32f);
        spotLight.setSpotCutoff(spotCutoff);
        spotLight.setSpotDirection(new Vector4f(-15f, -15f, 15f, 1f));
        spotLights.add(spotLight);
    }

    private void handleInput() {
        if (Fw.input.isKeyDown(Keyboard.ESCAPE)) {
            Fw.app.stopRunning();
        }
        if (Fw.input.isKeyDown(Keyboard.F1) && !Fw.input.wasKeyDown(Keyboard.F1)) {
            wireframe = !wireframe;
            if (wireframe) {
                GL.o1.setPolygonModeFrontAndBackLine();
            } else {
                GL.o1.setPolygonModeFrontAndBackFill();
            }
        }
        if (Fw.input.isKeyDown(Keyboard.B) && !Fw.input.wasKeyDown(Keyboard.B)) {
            renderAabbs = !renderAabbs;
        }
        if (Fw.input.isKeyDown(Keyboard.O) && !Fw.input.wasKeyDown(Keyboard.O)) {
            renderSimpleOrientationAxis = !renderSimpleOrientationAxis;
        }
        if (Fw.input.isKeyDown(Keyboard.F)) {
            camera.updatePlanes();
            viewFrustumGameObject.getRenderableState().setInFrustum(true);
            viewFrustumGameObject.getWorldTransform().getRotation().set(camera.getRotationMatrix());
            viewFrustumGameObject.setWorldPosition(camera.getPosition());
            viewFrustumGameObject.update(camera.getAspectRatio(), camera.getFieldOfView(), camera.getZNear(),
                    camera.getZFar());
            Fw.graphics.updateAabb(viewFrustumGameObject);
            Fw.graphics.performCulling(camera, dynamicMeshesWorld.getGameObjects());
            updateGameObjectsToRender();
        }
        if (Fw.input.isKeyDown(Keyboard.C)) {
            if ((spotCutoff == 90f) && !Fw.input.wasKeyDown(Keyboard.C)) {
                spotCutoff = 180f;
            } else if ((spotCutoff == 180f) && !Fw.input.wasKeyDown(Keyboard.C)) {
                spotCutoff = 0f;
            } else if ((spotCutoff != 90f) && (spotCutoff != 180f)) {
                spotCutoff += Fw.timer.getDelta() * 3f;
                if (spotCutoff > 180f) {
                    spotCutoff = 0f;
                }
                if (spotCutoff > 90f) {
                    spotCutoff = 90f;
                }
            }
            for (GLLight light : spotLights) {
                light.setSpotCutoff(spotCutoff);
            }
        }
        if (Fw.input.isKeyDown(Keyboard.LBRACKET) && !Fw.input.wasKeyDown(Keyboard.LBRACKET)) {
            if (--shaderIndex < 0) {
                shaderIndex += 9;
            }
            updateCurrentShader();
        }
        if (Fw.input.isKeyDown(Keyboard.RBRACKET) && !Fw.input.wasKeyDown(Keyboard.RBRACKET)) {
            shaderIndex = ++shaderIndex % 9;
            updateCurrentShader();
        }
        cameraController.update();
    }

    private void renderCurrentShaderName() {
        Fw.graphics.textNewLine();
        Fw.graphics.setTextColor("yellow");
        String shaderName = "";

        switch (shaderIndex) {
            case 0:
                shaderName = "BlinnPhongSpecularDirectionalShader";
                break;
            case 1:
                shaderName = "BlinnPhongSpecularPointShader";
                break;
            case 2:
                shaderName = "BlinnPhongSpecularSpotShader";
                break;
            case 3:
                shaderName = "DiffuseDirectionalShader";
                break;
            case 4:
                shaderName = "DiffusePointShader";
                break;
            case 5:
                shaderName = "DiffuseSpotShader";
                break;
            case 6:
                shaderName = "PhongSpecularDirectionalShader";
                break;
            case 7:
                shaderName = "PhongSpecularPointShader";
                break;
            case 8:
                shaderName = "PhongSpecularSpotShader";
                break;
        }

        Fw.graphics.drawString(shaderName);
        if ((shaderIndex == 2) || (shaderIndex == 5) || (shaderIndex == 8)) {
            Fw.graphics.textNewLine();
            Fw.graphics.setTextColor("lime");
            Fw.graphics.drawString("Spot cutoff: " + spotCutoff);
        }
    }

    private void updateCurrentLights() {
        switch (shaderIndex) {
            case 0:
            case 3:
            case 6:
                currentLights = directionalLights;
                break;
            case 1:
            case 4:
            case 7:
                currentLights = pointLights;
                break;
            case 2:
            case 5:
            case 8:
                currentLights = spotLights;
                break;
        }
    }

    private void updateCurrentShader() {
        switch (shaderIndex) {
            case 0:
                currentShader = vertexLitSingleBlinnPhongSpecularDirectionalShader;
                break;
            case 1:
                currentShader = vertexLitSingleBlinnPhongSpecularPointShader;
                break;
            case 2:
                currentShader = vertexLitSingleBlinnPhongSpecularSpotShader;
                break;
            case 3:
                currentShader = vertexLitSingleDiffuseDirectionalShader;
                break;
            case 4:
                currentShader = vertexLitSingleDiffusePointShader;
                break;
            case 5:
                currentShader = vertexLitSingleDiffuseSpotShader;
                break;
            case 6:
                currentShader = vertexLitSinglePhongSpecularDirectionalShader;
                break;
            case 7:
                currentShader = vertexLitSinglePhongSpecularPointShader;
                break;
            case 8:
                currentShader = vertexLitSinglePhongSpecularSpotShader;
                break;
        }
    }

    private void updateGameObjectsToRender() {
        gameObjectsToRender.clear();
        for (GameObject gameObject : dynamicMeshesWorld.getGameObjects()) {
            if (gameObject.getRenderableState().isInFrustum()) {
                gameObjectsToRender.add(gameObject);
            }
        }
    }

    private void updateShaderUniforms() {
        switch (shaderIndex) {
            case 0:
                vertexLitSingleBlinnPhongSpecularDirectionalShader.getUniforms().setAmbientLight(ambientLight);
                vertexLitSingleBlinnPhongSpecularDirectionalShader.getUniforms().setLight(currentDirectionalLight);
                vertexLitSingleBlinnPhongSpecularDirectionalShader.getUniforms().setMaterialLight(materialLight);
                break;
            case 1:
                vertexLitSingleBlinnPhongSpecularPointShader.getUniforms().setAmbientLight(ambientLight);
                vertexLitSingleBlinnPhongSpecularPointShader.getUniforms().setLight(currentPointLight);
                vertexLitSingleBlinnPhongSpecularPointShader.getUniforms().setMaterialLight(materialLight);
                break;
            case 2:
                vertexLitSingleBlinnPhongSpecularSpotShader.getUniforms().setAmbientLight(ambientLight);
                vertexLitSingleBlinnPhongSpecularSpotShader.getUniforms().setLight(currentSpotLight);
                vertexLitSingleBlinnPhongSpecularSpotShader.getUniforms().setMaterialLight(materialLight);
                break;
            case 3:
                vertexLitSingleDiffuseDirectionalShader.getUniforms().setAmbientLight(ambientLight);
                vertexLitSingleDiffuseDirectionalShader.getUniforms().setLight(currentDirectionalLight);
                vertexLitSingleDiffuseDirectionalShader.getUniforms().setMaterialLight(materialLight);
                break;
            case 4:
                vertexLitSingleDiffusePointShader.getUniforms().setAmbientLight(ambientLight);
                vertexLitSingleDiffusePointShader.getUniforms().setLight(currentPointLight);
                vertexLitSingleDiffusePointShader.getUniforms().setMaterialLight(materialLight);
                break;
            case 5:
                vertexLitSingleDiffuseSpotShader.getUniforms().setAmbientLight(ambientLight);
                vertexLitSingleDiffuseSpotShader.getUniforms().setLight(currentSpotLight);
                vertexLitSingleDiffuseSpotShader.getUniforms().setMaterialLight(materialLight);
                break;
            case 6:
                vertexLitSinglePhongSpecularDirectionalShader.getUniforms().setAmbientLight(ambientLight);
                vertexLitSinglePhongSpecularDirectionalShader.getUniforms().setLight(currentDirectionalLight);
                vertexLitSinglePhongSpecularDirectionalShader.getUniforms().setMaterialLight(materialLight);
                break;
            case 7:
                vertexLitSinglePhongSpecularPointShader.getUniforms().setAmbientLight(ambientLight);
                vertexLitSinglePhongSpecularPointShader.getUniforms().setLight(currentPointLight);
                vertexLitSinglePhongSpecularPointShader.getUniforms().setMaterialLight(materialLight);
                break;
            case 8:
                vertexLitSinglePhongSpecularSpotShader.getUniforms().setAmbientLight(ambientLight);
                vertexLitSinglePhongSpecularSpotShader.getUniforms().setLight(currentSpotLight);
                vertexLitSinglePhongSpecularSpotShader.getUniforms().setMaterialLight(materialLight);
                break;
        }
    }

    @Override
    public void destroy() {
        for (GameObject gameObject : dynamicMeshesWorld.getGameObjects()) {
            gameObject.destroy();
        }
        viewFrustumGameObject.destroy();
        Fw.input.removeInputProcessor(cameraController);
        Fw.input.releaseMouseCursor();
    }

    @Override
    public void init() {

        // Switch renderer and factory to OpenGL 3
        Fw.graphics.switchRenderableFactoryAndRenderer(3);

        // Create timer
        fpsTimer = new FpsTimer();

        // Setup camera and controller
        camera = GL.cameraFactory.createCamera();
        camera.lookAt(new Vector3f(0f, 2f, 5f), new Vector3f(0f, 2f, -1f), Vector3f.WORLD_Y_AXIS);
        camera.setPerspectiveProjection(
                70f,
                (float) Fw.config.getCurrentWidth() / (float) Fw.config.getCurrentHeight(),
                .05f, 128f);
        cameraController = GL.cameraFactory.createCameraController();
        cameraController.setCamera(camera);

        // Memory usage
        memoryUsage = new MemoryUsage();

        // Create ambient light, glLights and material light
        ambientLight = GL.glFactory.createAmbientLight();
        createDirectionalLights();
        createPointLights();
        createSpotLights();
        materialLight = GL.glFactory.createMaterialLight();
        materialLight.setShininess(16);
        materialLight.setSpecular(new Vector4f(.3f, .3f, .3f, 1f));

        // Load textures
        try {
            // Texture must be loaded before the renderable is created
            // Parameters ("skybox/violentdays", "png") will create a texture named "skybox/violentdays/cubemap.png"
            // from "skybox/violentdays/xpos.png", "skybox/violentdays/xneg.png", "skybox/violentdays/ypos.png"
            // "skybox/violentdays/yneg.png", "skybox/violentdays/zpos.png" and "skybox/violentdays/zneg.png"
            Fw.graphics.loadCubeMapTexture("skybox/violentdays", "png");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // AABB game object
        aabbGameObject = new AABBGameObject();
        aabbGameObject.getColor0().set("yellow");
        aabbGameObject.init(new AABB()); // Empty AABB

        // Create dynamic mesh world
        dynamicMeshesWorld = new DynamicMeshWorld();
        dynamicMeshesWorld.init();

        // Render statistics game object
        renderStatisticsGameObject = new RenderStatisticsGameObject();
        renderStatisticsGameObject.init(Fw.graphics);

        // Simple axis game object
        simpleOrientationAxisGameObject = new SimpleOrientationAxisGameObject();
        simpleOrientationAxisGameObject.getColor0().set("red");
        simpleOrientationAxisGameObject.getColor1().set("green");
        simpleOrientationAxisGameObject.getColor2().set("blue");
        simpleOrientationAxisGameObject.init();

        // Skybox game object
        skyboxGameObject = new SkyboxGameObject();
        skyboxGameObject.init("skybox/violentdays/cubemap.png");

        // View frustum game object
        viewFrustumGameObject = new ViewFrustumGameObject();
        viewFrustumGameObject.getColor0().set("black");
        viewFrustumGameObject.getColor1().set("yellow");
        viewFrustumGameObject.getColor2().set("yellow");
        viewFrustumGameObject.getColor3().set("white");
        viewFrustumGameObject.getColor3().setA(.4f);
        viewFrustumGameObject.getRenderableState().setInFrustum(false);
        viewFrustumGameObject.init(camera.getAspectRatio(), camera.getFieldOfView(), camera.getZNear(),
                camera.getZFar());

        // Frustum culling
        FrustumCullingClipSpaceSymmetrical frustumCulling = GL.cameraFactory.createFrustumCullingClipSpaceSymmetrical();
        camera.setFrustumCulling(frustumCulling);
        updateGameObjectsToRender();

        // Update AABBs
        Fw.graphics.updateAabb(dynamicMeshesWorld.getGameObjects());

        // Build shaders
        try {
            diffuseTextureShader = DiffuseTextureShader.Factory.Create();
            diffuseTextureShader.init();
            skyboxShader = SkyboxShader.Factory.Create();
            skyboxShader.init();
            vertexLitSingleBlinnPhongSpecularDirectionalShader = BlinnPhongSpecularDirectionalShader.Factory.Create();
            vertexLitSingleBlinnPhongSpecularDirectionalShader.init();
            vertexLitSingleBlinnPhongSpecularPointShader = BlinnPhongSpecularPointShader.Factory.Create();
            vertexLitSingleBlinnPhongSpecularPointShader.init();
            vertexLitSingleBlinnPhongSpecularSpotShader = BlinnPhongSpecularSpotShader.Factory.Create();
            vertexLitSingleBlinnPhongSpecularSpotShader.init();
            vertexLitSingleDiffuseDirectionalShader = DiffuseDirectionalShader.Factory.Create();
            vertexLitSingleDiffuseDirectionalShader.init();
            vertexLitSingleDiffusePointShader = DiffusePointShader.Factory.Create();
            vertexLitSingleDiffusePointShader.init();
            vertexLitSingleDiffuseSpotShader = DiffuseSpotShader.Factory.Create();
            vertexLitSingleDiffuseSpotShader.init();
            vertexLitSinglePhongSpecularDirectionalShader = PhongSpecularDirectionalShader.Factory.Create();
            vertexLitSinglePhongSpecularDirectionalShader.init();
            vertexLitSinglePhongSpecularPointShader = PhongSpecularPointShader.Factory.Create();
            vertexLitSinglePhongSpecularPointShader.init();
            vertexLitSinglePhongSpecularSpotShader = PhongSpecularSpotShader.Factory.Create();
            vertexLitSinglePhongSpecularSpotShader.init();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // Set current shader
        shaderIndex = 4;
        updateCurrentShader();

        // OpenGL settings
        GL.o1.setBlendingFunctionSrcAlphaAndOneMinusSrcAlpha();
        GL.o1.enableCulling();
        GL.o1.cullBackFaces();
        GL.o1.enableDepthTest();
        GL.o1.setDepthFuncLess();
        GL.o1.enableDepthMask();
        GL.o1.setClearDepth(1f);
        GL.o1.clear();

        log.info("OpenGL version: " + GL.o1.getString(GetString.VERSION));

        // Add camera controller to input
        Fw.input.addInputProcessor(cameraController);
        Fw.input.grabMouseCursor();
    }

    @Override
    public void pause() {
    }

    @Override
    public void render() {
        fpsTimer.update();
        memoryUsage.update();
        renderStatisticsGameObject.update();

        if (Fw.timer.isGameTick()) {
            // Do nothing
        }

        if (Fw.timer.isRenderTick()) {
            handleInput();

            // Clear screen
            GL.o1.clear();

            // Update camera
            Fw.graphics.setCamera(camera);

            // Bind shader
            Fw.graphics.switchShader(skyboxShader);

            // Render skybox
            Fw.graphics.color(1f, 1f, 1f, 1f);
            skyboxGameObject.getWorldTransform().setPosition(camera.getPosition());
            GL.o1.disableCulling();
            GL.o1.disableDepthTest();
            Fw.graphics.render(skyboxGameObject);
            GL.o1.enableDepthTest();
            GL.o1.enableCulling();

            // Unbind shader
            Fw.graphics.unbindShader();

            // Update current lights
            updateCurrentLights();

            // Bind shader
            Fw.graphics.switchShader(currentShader);

            // Enable blending
            GL.o1.enableBlending();

            // Loop through all lights
            boolean firstPass = true;
            for (GLLight light : currentLights) {

                if (firstPass) {
                    ambientLight.reset();
                    GL.o1.setBlendingFunctionOneAndZero();
                    GL.o1.setDepthFuncLess();
                    GL.o1.enableDepthMask();
                    firstPass = false;
                } else {
                    // Ambient only enabled on the first pass
                    ambientLight.setAmbient(new Vector4f());
                    GL.o1.setBlendingFunctionOneAndOne();
                    GL.o1.setDepthFuncEqual();
                    GL.o1.disableDepthMask();
                }

                // I'm not using a switch statement and instead will set all lights
                // since updateShaderUniforms() will do the right thing anyways.
                currentDirectionalLight = light;
                currentPointLight = light;
                currentSpotLight = light;

                // Update uniforms
                updateShaderUniforms();

                // Render directly
                Fw.graphics.render(gameObjectsToRender);
            }

            // Disable blending, then restore blending function, depth test function and mask
            GL.o1.disableBlending();
            GL.o1.setBlendingFunctionSrcAlphaAndOneMinusSrcAlpha();
            GL.o1.setDepthFuncLess();
            GL.o1.enableDepthMask();

            // Unbind shader
            Fw.graphics.unbindShader();

            // Bind diffuse texture shader (no lighting)
            Fw.graphics.switchShader(diffuseTextureShader);

            // View frustum
            if (viewFrustumGameObject.getRenderableState().isInFrustum()) {
                Fw.graphics.renderBlend(viewFrustumGameObject);
            }

            // AABBs
            if (renderAabbs) {
                for (GameObject gameObject : gameObjectsToRender) {
                    AABB worldAabb = gameObject.getWorldAabb();
                    // Updating AABBs this way is costly
                    aabbGameObject.update(worldAabb);
                    // AABB is already in world coordinates so we don't translate
                    Fw.graphics.render(aabbGameObject.getRenderable(), 0f, 0f, 0f);
                }
            }

            // Orientation axis
            if (renderSimpleOrientationAxis) {
                GL.o1.disableDepthTest();
                for (GameObject gameObject : gameObjectsToRender) {
                    simpleOrientationAxisGameObject.setWorldTransform(gameObject.getWorldTransform());
                    Fw.graphics.render(simpleOrientationAxisGameObject);
                }
                GL.o1.enableDepthTest();
            }

            // Render 2D
            GL.o1.enableBlending();
            GL.o1.disableDepthTest();
            Fw.graphics.set2DMode();
            Fw.graphics.resetTextRendering();
            Fw.graphics.textNewHalfLine();
            renderStatisticsGameObject.render(); // Game object has no renderable
            renderCurrentShaderName();
            GL.o1.enableDepthTest();
            GL.o1.disableBlending();

            // Unbind shader
            Fw.graphics.unbindShader();
        }
    }

    @Override
    public void resize(int width, int height) {
        GL.o1.setViewport(0, 0, width, height);
    }

    @Override
    public void resume() {
    }

}
