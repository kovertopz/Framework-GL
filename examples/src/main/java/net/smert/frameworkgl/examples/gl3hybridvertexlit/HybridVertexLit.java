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
package net.smert.frameworkgl.examples.gl3hybridvertexlit;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.Screen;
import net.smert.frameworkgl.examples.common.DynamicMeshWorld;
import net.smert.frameworkgl.examples.common.HybridPixelOrVertexLitGuiScreen;
import net.smert.frameworkgl.examples.common.MultipleLightsOfTheSameType;
import net.smert.frameworkgl.gameobjects.AABBGameObject;
import net.smert.frameworkgl.gameobjects.GameObject;
import net.smert.frameworkgl.gameobjects.SimpleOrientationAxisGameObject;
import net.smert.frameworkgl.gameobjects.SkyboxGameObject;
import net.smert.frameworkgl.gameobjects.ViewFrustumGameObject;
import net.smert.frameworkgl.helpers.Keyboard;
import net.smert.frameworkgl.math.AABB;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.math.Vector4f;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.GLLight;
import net.smert.frameworkgl.opengl.camera.Camera;
import net.smert.frameworkgl.opengl.camera.CameraController;
import net.smert.frameworkgl.opengl.camera.FrustumCullingClipSpaceSymmetrical;
import net.smert.frameworkgl.opengl.constants.GetString;
import net.smert.frameworkgl.opengl.shader.AbstractShader;
import net.smert.frameworkgl.opengl.shader.basic.DiffuseTextureShader;
import net.smert.frameworkgl.opengl.shader.basic.SkyboxShader;
import net.smert.frameworkgl.opengl.shader.vertexlit.multi.BlinnPhongSpecularHybridShader;
import net.smert.frameworkgl.opengl.shader.vertexlit.multi.DiffuseHybridShader;
import net.smert.frameworkgl.opengl.shader.vertexlit.multi.PhongSpecularHybridShader;
import net.smert.frameworkgl.utils.FpsTimer;
import net.smert.frameworkgl.utils.MemoryUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class HybridVertexLit extends Screen {

    private final static Logger log = LoggerFactory.getLogger(HybridVertexLit.class);

    private boolean renderAabbs;
    private boolean renderSimpleOrientationAxis;
    private boolean wireframe;
    private AbstractShader currentShader;
    private AABBGameObject aabbGameObject;
    private BlinnPhongSpecularHybridShader vertexLitMultiBlinnPhongSpecularHybridShader;
    private Camera camera;
    private CameraController cameraController;
    private DiffuseHybridShader vertexLitMultiDiffuseHybridShader;
    private DiffuseTextureShader diffuseTextureShader;
    private DynamicMeshWorld dynamicMeshesWorld;
    private FpsTimer fpsTimer;
    private HybridPixelOrVertexLitGuiScreen hybridPixelOrVertexLitGuiScreen;
    private final List<GameObject> gameObjectsToRender;
    private List<GLLight> currentLights;
    private MemoryUsage memoryUsage;
    private MultipleLightsOfTheSameType multipleLightsOfTheSameType;
    private PhongSpecularHybridShader vertexLitMultiPhongSpecularHybridShader;
    private SimpleOrientationAxisGameObject simpleOrientationAxisGameObject;
    private SkyboxGameObject skyboxGameObject;
    private SkyboxShader skyboxShader;
    private ViewFrustumGameObject viewFrustumGameObject;

    public HybridVertexLit(String[] args) {
        renderAabbs = false;
        renderSimpleOrientationAxis = false;
        wireframe = false;
        gameObjectsToRender = new ArrayList<>();
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
        if (Fw.input.isKeyDown(Keyboard.LEFT_BRACKET) && !Fw.input.wasKeyDown(Keyboard.LEFT_BRACKET)) {
            hybridPixelOrVertexLitGuiScreen.decrementShaderIndex();
            updateCurrentShader();
        }
        if (Fw.input.isKeyDown(Keyboard.RIGHT_BRACKET) && !Fw.input.wasKeyDown(Keyboard.RIGHT_BRACKET)) {
            hybridPixelOrVertexLitGuiScreen.incrementShaderIndex();
            updateCurrentShader();
        }
        if (Fw.input.isKeyDown(Keyboard.SEMICOLON) && !Fw.input.wasKeyDown(Keyboard.SEMICOLON)) {
            hybridPixelOrVertexLitGuiScreen.decrementLightIndex();
        }
        if (Fw.input.isKeyDown(Keyboard.APOSTROPHE) && !Fw.input.wasKeyDown(Keyboard.APOSTROPHE)) {
            hybridPixelOrVertexLitGuiScreen.incrementLightIndex();
        }
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
                currentShader = vertexLitMultiBlinnPhongSpecularHybridShader;
                break;
            case 1:
                currentShader = vertexLitMultiDiffuseHybridShader;
                break;
            case 2:
                currentShader = vertexLitMultiPhongSpecularHybridShader;
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

        // AABB game object
        aabbGameObject = new AABBGameObject();
        aabbGameObject.getColor0().set("yellow");
        aabbGameObject.init(new AABB()); // Empty AABB

        // Create dynamic mesh world
        dynamicMeshesWorld = new DynamicMeshWorld();
        dynamicMeshesWorld.init();

        // Simple axis game object
        simpleOrientationAxisGameObject = new SimpleOrientationAxisGameObject();
        simpleOrientationAxisGameObject.getColor0().set("red");
        simpleOrientationAxisGameObject.getColor1().set("green");
        simpleOrientationAxisGameObject.getColor2().set("blue");
        simpleOrientationAxisGameObject.init();

        // Skybox game object
        skyboxGameObject = new SkyboxGameObject();
        skyboxGameObject.init("skybox/intersteller/cubemap.png");

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

        // Initialize GUI
        Fw.gui.init();

        // Create GUI screen
        hybridPixelOrVertexLitGuiScreen = new HybridPixelOrVertexLitGuiScreen();
        hybridPixelOrVertexLitGuiScreen.setIsPixelLit(false);
        hybridPixelOrVertexLitGuiScreen.setSpotInnerCutoff(180f);
        hybridPixelOrVertexLitGuiScreen.setSpotOuterCutoff(180f);
        hybridPixelOrVertexLitGuiScreen.init(Fw.graphics.getRenderer());
        Fw.gui.setScreen(hybridPixelOrVertexLitGuiScreen);

        // Build shaders
        try {
            diffuseTextureShader = DiffuseTextureShader.Factory.Create();
            diffuseTextureShader.init();
            skyboxShader = SkyboxShader.Factory.Create();
            skyboxShader.init();
            vertexLitMultiBlinnPhongSpecularHybridShader = BlinnPhongSpecularHybridShader.Factory.Create();
            vertexLitMultiBlinnPhongSpecularHybridShader.init();
            vertexLitMultiDiffuseHybridShader = DiffuseHybridShader.Factory.Create();
            vertexLitMultiDiffuseHybridShader.init();
            vertexLitMultiPhongSpecularHybridShader = PhongSpecularHybridShader.Factory.Create();
            vertexLitMultiPhongSpecularHybridShader.init();
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

            // Clear screen
            GL.o1.clear();

            // Update camera
            Fw.graphics.setCamera(camera);

            // Update current lights
            updateCurrentLights();

            // Update global uniform variables
            GL.uniformVariables.setGlLights(currentLights);

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

            // Bind shader
            Fw.graphics.switchShader(currentShader);

            // Render directly
            Fw.graphics.render(gameObjectsToRender);

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
            Fw.gui.update();
            Fw.gui.render();
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
