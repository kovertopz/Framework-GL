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
package net.smert.frameworkgl.examples.gl3forwardrenderingpipeline;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.Screen;
import net.smert.frameworkgl.examples.common.DiffuseAndSpecularHybridShaders;
import net.smert.frameworkgl.examples.common.DynamicMeshWorld;
import net.smert.frameworkgl.examples.common.HybridPixelOrVertexLitGuiScreen;
import net.smert.frameworkgl.examples.common.MultipleLightsOfTheSameType;
import net.smert.frameworkgl.gameobjects.GameObject;
import net.smert.frameworkgl.gameobjects.SkyboxGameObject;
import net.smert.frameworkgl.helpers.Keyboard;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.math.Vector4f;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.GLLight;
import net.smert.frameworkgl.opengl.camera.Camera;
import net.smert.frameworkgl.opengl.camera.CameraController;
import net.smert.frameworkgl.opengl.camera.FrustumCullingClipSpaceSymmetrical;
import net.smert.frameworkgl.opengl.constants.GetString;
import net.smert.frameworkgl.opengl.shader.AbstractShader;
import net.smert.frameworkgl.utils.FpsTimer;
import net.smert.frameworkgl.utils.MemoryUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class ForwardRenderingPipeline extends Screen {

    private final static Logger log = LoggerFactory.getLogger(ForwardRenderingPipeline.class);

    private AbstractShader currentShader;
    private Camera camera;
    private CameraController cameraController;
    private DiffuseAndSpecularHybridShaders diffuseAndSpecularHybridShaders;
    private DynamicMeshWorld dynamicMeshesWorld;
    private net.smert.frameworkgl.opengl.pipeline.ForwardRenderingPipeline forwardRenderingPipeline;
    private FpsTimer fpsTimer;
    private HybridPixelOrVertexLitGuiScreen hybridPixelOrVertexLitGuiScreen;
    private List<GLLight> currentLights;
    private MemoryUsage memoryUsage;
    private MultipleLightsOfTheSameType multipleLightsOfTheSameType;
    private SkyboxGameObject skyboxGameObject;

    public ForwardRenderingPipeline(String[] args) {
    }

    private void handleInput() {
        if (Fw.input.isKeyDown(Keyboard.ESCAPE)) {
            Fw.app.stopRunning();
        }
        if (Fw.input.isKeyDown(Keyboard.F1) && !Fw.input.wasKeyDown(Keyboard.F1)) {
            boolean not = !forwardRenderingPipeline.isWireframe();
            forwardRenderingPipeline.setWireframe(not);
        }
        if (Fw.input.isKeyDown(Keyboard.B) && !Fw.input.wasKeyDown(Keyboard.B)) {
            boolean not = !forwardRenderingPipeline.isRenderAabbs();
            forwardRenderingPipeline.setRenderAabbs(not);
            if (not) {
                forwardRenderingPipeline.updateAabbs(); // Update AABBs each time it is enabled
            }
        }
        if (Fw.input.isKeyDown(Keyboard.O) && !Fw.input.wasKeyDown(Keyboard.O)) {
            boolean not = !forwardRenderingPipeline.isRenderSimpleOrientationAxis();
            forwardRenderingPipeline.setRenderSimpleOrientationAxis(not);
        }
        if (Fw.input.isKeyDown(Keyboard.F)) {
            forwardRenderingPipeline.performFrustumCulling();
            forwardRenderingPipeline.setRenderViewFrustum(true);
            forwardRenderingPipeline.updateViewFrustumGameObjectWithCamera();
        }
        float spotInnerCutoff = hybridPixelOrVertexLitGuiScreen.getSpotInnerCutoff();
        float spotOuterCutoff = hybridPixelOrVertexLitGuiScreen.getSpotOuterCutoff();
        if (Fw.input.isKeyDown(Keyboard.C)) {
            if ((spotOuterCutoff == 90f) && !Fw.input.wasKeyDown(Keyboard.C)) {
                spotOuterCutoff = 180f;
            } else if ((spotOuterCutoff == 180f) && !Fw.input.wasKeyDown(Keyboard.C)) {
                spotOuterCutoff = 0f;
            } else if ((spotOuterCutoff != 90f) && (spotOuterCutoff != 180f)) {
                spotOuterCutoff += Fw.timer.getDelta() * 3f;
                if (spotOuterCutoff > 180f) {
                    spotOuterCutoff = 0f;
                }
                if (spotOuterCutoff > 90f) {
                    spotOuterCutoff = 90f;
                }
            }
            for (GLLight light : multipleLightsOfTheSameType.getSpotLights()) {
                light.setSpotOuterCutoff(spotOuterCutoff);
            }
        }
        if (Fw.input.isKeyDown(Keyboard.V)) {
            if ((spotInnerCutoff == 90f) && !Fw.input.wasKeyDown(Keyboard.V)) {
                spotInnerCutoff = 180f;
            } else if ((spotInnerCutoff == 180f) && !Fw.input.wasKeyDown(Keyboard.V)) {
                spotInnerCutoff = 0f;
            } else if ((spotInnerCutoff != 90f) && (spotInnerCutoff != 180f)) {
                spotInnerCutoff += Fw.timer.getDelta() * 3f;
                if (spotInnerCutoff > 180f) {
                    spotInnerCutoff = 0f;
                }
                if (spotInnerCutoff > 90f) {
                    spotInnerCutoff = 90f;
                }
            }
            for (GLLight light : multipleLightsOfTheSameType.getSpotLights()) {
                light.setSpotInnerCutoff(spotInnerCutoff);
            }
        }
        if (Fw.input.isKeyDown(Keyboard.LBRACKET) && !Fw.input.wasKeyDown(Keyboard.LBRACKET)) {
            hybridPixelOrVertexLitGuiScreen.decrementShaderIndex();
            updateCurrentShader();
        }
        if (Fw.input.isKeyDown(Keyboard.RBRACKET) && !Fw.input.wasKeyDown(Keyboard.RBRACKET)) {
            hybridPixelOrVertexLitGuiScreen.incrementShaderIndex();
            updateCurrentShader();
        }
        if (Fw.input.isKeyDown(Keyboard.SEMICOLON) && !Fw.input.wasKeyDown(Keyboard.SEMICOLON)) {
            hybridPixelOrVertexLitGuiScreen.decrementLightIndex();
        }
        if (Fw.input.isKeyDown(Keyboard.APOSTROPHE) && !Fw.input.wasKeyDown(Keyboard.APOSTROPHE)) {
            hybridPixelOrVertexLitGuiScreen.incrementLightIndex();
        }
        hybridPixelOrVertexLitGuiScreen.setSpotInnerCutoff(spotInnerCutoff);
        hybridPixelOrVertexLitGuiScreen.setSpotOuterCutoff(spotOuterCutoff);
        cameraController.update();
    }

    private void updateCurrentLights() {
        switch (hybridPixelOrVertexLitGuiScreen.getLightsIndex()) {
            case 0:
                currentLights = multipleLightsOfTheSameType.getDirectionalLights();
                break;
            case 1:
                currentLights = multipleLightsOfTheSameType.getPointLights();
                break;
            case 2:
                currentLights = multipleLightsOfTheSameType.getSpotLights();
                break;
        }
    }

    private void updateCurrentShader() {
        switch (hybridPixelOrVertexLitGuiScreen.getShaderIndex()) {
            case 0:
                currentShader = diffuseAndSpecularHybridShaders.getPixelLitMultiBlinnPhongSpecularHybridShader();
                break;
            case 1:
                currentShader = diffuseAndSpecularHybridShaders.getPixelLitMultiDiffuseHybridShader();
                break;
            case 2:
                currentShader = diffuseAndSpecularHybridShaders.getPixelLitMultiPhongSpecularHybridShader();
                break;
            case 3:
                currentShader = diffuseAndSpecularHybridShaders.getVertexLitMultiBlinnPhongSpecularHybridShader();
                break;
            case 4:
                currentShader = diffuseAndSpecularHybridShaders.getVertexLitMultiDiffuseHybridShader();
                break;
            case 5:
                currentShader = diffuseAndSpecularHybridShaders.getVertexLitMultiPhongSpecularHybridShader();
                break;
        }
        forwardRenderingPipeline.setDefaultShader(currentShader);
        forwardRenderingPipeline.updateCurrentShader();
    }

    @Override
    public void destroy() {
        diffuseAndSpecularHybridShaders.destroy();
        for (GameObject gameObject : dynamicMeshesWorld.getGameObjects()) {
            gameObject.destroy();
        }
        forwardRenderingPipeline.destroy();
        Fw.input.removeInputProcessor(cameraController);
        Fw.input.releaseMouseCursor();
    }

    @Override
    public void init() {

        // Register assets
        try {
            Fw.files.registerAssets("/net/smert/frameworkgl/examples/assets", true);
        } catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }

        // Switch renderer and factory to OpenGL 3
        Fw.graphics.switchOpenGLVersion(3);

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

        // Create glLights and material light
        multipleLightsOfTheSameType = new MultipleLightsOfTheSameType();
        multipleLightsOfTheSameType.init();
        GL.uniformVariables.getDefaultMaterialLight().setShininess(16);
        // Effectively disables diffuse and per vertex color will be used
        GL.uniformVariables.getDefaultMaterialLight().setDiffuse(new Vector4f(1f, 1f, 1f, 1f));
        GL.uniformVariables.getDefaultMaterialLight().setSpecular(new Vector4f(.3f, .3f, .3f, 1f));

        // Load textures
        try {
            // Texture must be loaded before the renderable is created
            // Parameters ("skybox/intersteller", "png") will create a texture named "skybox/intersteller/cubemap.png"
            // from "skybox/intersteller/xpos.png", "skybox/intersteller/xneg.png", "skybox/intersteller/ypos.png"
            // "skybox/intersteller/yneg.png", "skybox/intersteller/zpos.png" and "skybox/intersteller/zneg.png"
            Fw.graphics.loadCubeMapTexture("skybox/intersteller", "png");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // Create dynamic mesh world
        dynamicMeshesWorld = new DynamicMeshWorld();
        dynamicMeshesWorld.init();

        // Skybox game object
        skyboxGameObject = new SkyboxGameObject();
        skyboxGameObject.init("skybox/intersteller/cubemap.png");

        // Frustum culling
        FrustumCullingClipSpaceSymmetrical frustumCulling = GL.cameraFactory.createFrustumCullingClipSpaceSymmetrical();
        camera.setFrustumCulling(frustumCulling);

        // Create pipeline
        forwardRenderingPipeline = GL.rpFactory.createForwardRenderingPipeline();
        forwardRenderingPipeline.setCamera(camera);
        forwardRenderingPipeline.setDebug(true);
        forwardRenderingPipeline.setFrustumCulling(false);
        forwardRenderingPipeline.setGuiRenderer(GL.rendererFactory.createDefaultGuiRenderer());
        forwardRenderingPipeline.setSkyboxGameObject(skyboxGameObject);
        forwardRenderingPipeline.setWorldGameObjects(dynamicMeshesWorld.getGameObjects());
        forwardRenderingPipeline.addAllGameObjectsToRender(); // Since we turned off frustum culling

        // Initialize GUI
        Fw.gui.init();

        // Create GUI screen
        hybridPixelOrVertexLitGuiScreen = new HybridPixelOrVertexLitGuiScreen();
        hybridPixelOrVertexLitGuiScreen.setIsPixelLit(true);
        hybridPixelOrVertexLitGuiScreen.setSpotInnerCutoff(180f);
        hybridPixelOrVertexLitGuiScreen.setSpotOuterCutoff(180f);
        hybridPixelOrVertexLitGuiScreen.init(Fw.graphics.getRenderer());
        Fw.gui.setScreen(hybridPixelOrVertexLitGuiScreen);

        // Build shaders and init pipeline
        try {
            diffuseAndSpecularHybridShaders = new DiffuseAndSpecularHybridShaders();
            diffuseAndSpecularHybridShaders.init();

            forwardRenderingPipeline.init();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // Set current shader
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

        if (Fw.timer.isGameTick()) {
            // Do nothing
        }

        if (Fw.timer.isRenderTick()) {
            handleInput();

            // Update current lights
            updateCurrentLights();

            // Update global uniform variables
            GL.uniformVariables.setGlLights(currentLights);

            // Render pipeline
            forwardRenderingPipeline.render();
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
