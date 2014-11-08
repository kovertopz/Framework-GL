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
package net.smert.frameworkgl.examples.gl3gui;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.Screen;
import net.smert.frameworkgl.examples.common.DynamicMeshWorld;
import net.smert.frameworkgl.gameobjects.GameObject;
import net.smert.frameworkgl.helpers.Keyboard;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.math.Vector4f;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.GLLight;
import net.smert.frameworkgl.opengl.camera.Camera;
import net.smert.frameworkgl.opengl.camera.CameraController;
import net.smert.frameworkgl.opengl.constants.GetString;
import net.smert.frameworkgl.opengl.shader.basic.DiffuseTextureShader;
import net.smert.frameworkgl.opengl.shader.vertexlit.single.DiffusePointShader;
import net.smert.frameworkgl.utils.FpsTimer;
import net.smert.frameworkgl.utils.MemoryUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class GUI extends Screen {

    private final static Logger log = LoggerFactory.getLogger(GUI.class);

    private boolean wireframe;
    private Camera camera;
    private CameraController cameraController;
    private DiffusePointShader vertexLitSingleDiffusePointShader;
    private DiffuseTextureShader diffuseTextureShader;
    private DynamicMeshWorld dynamicMeshesWorld;
    private FpsTimer fpsTimer;
    private GLLight glLight;
    private MemoryUsage memoryUsage;

    public GUI(String[] args) {
        wireframe = false;
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
        cameraController.update();
    }

    @Override
    public void destroy() {
        List<GameObject> gameObjects = dynamicMeshesWorld.getGameObjects();
        for (GameObject gameObject : gameObjects) {
            gameObject.destroy();
        }
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

        // Initialize audio (used by GUI)
        Fw.audio.init();

        // Initialize GUI
        Fw.gui.init();
        try {
            Fw.gui.loadGuiFromXml("gl3gui.xml", "start");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        Fw.gui.setClearScreen(false);

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

        // Create glLight
        glLight = GL.glFactory.createGLLight();
        glLight.setPosition(new Vector4f(0f, 15f, 10f, 1f));
        glLight.setRadius(256f); // Shader uses this value and OpenGL does not

        // Create dynamic mesh world
        dynamicMeshesWorld = new DynamicMeshWorld();
        dynamicMeshesWorld.init();

        // Build shaders
        try {
            diffuseTextureShader = DiffuseTextureShader.Factory.Create();
            diffuseTextureShader.init();
            vertexLitSingleDiffusePointShader = DiffusePointShader.Factory.Create();
            vertexLitSingleDiffusePointShader.init();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // OpenGL settings
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
//        Fw.input.grabMouseCursor();
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

            // Update audio
            Fw.audio.update();

            // Update GUI
            Fw.gui.update();

            // Clear screen
            GL.o1.clear();

            // Update camera
            Fw.graphics.setCamera(camera);

            // Update global uniform variables
            GL.uniformVariables.setGlLight(glLight);

            // Bind shader
            Fw.graphics.switchShader(vertexLitSingleDiffusePointShader);

            // Render directly
            List<GameObject> gameObjects = dynamicMeshesWorld.getGameObjects();
            Fw.graphics.render(gameObjects);

            // Unbind shader
            Fw.graphics.unbindShader();

            // Bind diffuse texture shader (no lighting)
            Fw.graphics.switchShader(diffuseTextureShader);

            // Render 2D
            GL.o1.enableBlending();
            GL.o1.disableDepthTest();
            Fw.graphics.set2DMode();
            Fw.graphics.resetTextRendering();
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
