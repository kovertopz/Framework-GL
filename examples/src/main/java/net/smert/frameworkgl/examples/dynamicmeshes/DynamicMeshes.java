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
package net.smert.frameworkgl.examples.dynamicmeshes;

import java.nio.FloatBuffer;
import java.util.List;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.gameobjects.GameObject;
import net.smert.frameworkgl.Screen;
import net.smert.frameworkgl.examples.common.DynamicMeshWorld;
import net.smert.frameworkgl.helpers.Keyboard;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.camera.LegacyCamera;
import net.smert.frameworkgl.opengl.camera.LegacyCameraController;
import net.smert.frameworkgl.opengl.constants.GetString;
import net.smert.frameworkgl.opengl.constants.Light;
import net.smert.frameworkgl.utils.FpsTimer;
import net.smert.frameworkgl.utils.MemoryUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class DynamicMeshes extends Screen {

    private final static Logger log = LoggerFactory.getLogger(DynamicMeshes.class);

    private boolean wireframe;
    private DynamicMeshWorld dynamicMeshesWorld;
    private FloatBuffer lightFloatBuffer;
    private FpsTimer fpsTimer;
    private LegacyCamera camera;
    private LegacyCameraController cameraController;
    private MemoryUsage memoryUsage;

    public DynamicMeshes(String[] args) {
        wireframe = false;
    }

    private void handleInput() {
        if (Fw.input.isKeyDown(Keyboard.ESCAPE) == true) {
            Fw.app.stopRunning();
        }
        if ((Fw.input.isKeyDown(Keyboard.F1) == true) && (Fw.input.wasKeyDown(Keyboard.F1) == false)) {
            wireframe = !wireframe;
            if (wireframe) {
                GL.o1.setPolygonModeFrontLine();
            } else {
                GL.o1.setPolygonModeFrontFill();
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

        // Create timer
        fpsTimer = new FpsTimer();

        // Setup camera and controller
        camera = GL.cameraFactory.createLegacyCamera();
        camera.setPosition(0f, 2f, 5f);
        cameraController = GL.cameraFactory.createLegacyCameraController();
        cameraController.setCamera(camera);

        // Memory usage
        memoryUsage = new MemoryUsage();

        // Float buffer for light
        lightFloatBuffer = GL.bufferHelper.createFloatBuffer(4);

        // Create dynamic mesh world
        dynamicMeshesWorld = new DynamicMeshWorld();
        dynamicMeshesWorld.init();

        GL.o1.enableCulling();
        GL.o1.cullBackFaces();
        GL.o1.enableDepthTest();
        GL.o1.setDepthFuncLess();
        GL.o1.enableDepthMask();
        GL.o1.setClearDepth(1f);
        GL.o1.enableColorMaterial();
        GL.o1.enableLight0();
        GL.o1.enableLighting();
        GL.o1.setSmoothLighting(true);
        GL.o1.clear();

        GL.o1.setProjectionPerspective(
                70f,
                (float) Fw.config.getCurrentWidth() / (float) Fw.config.getCurrentHeight(),
                .05f, 128f);
        GL.o1.setModelViewIdentity();

        // Light position
        lightFloatBuffer.put(0f);
        lightFloatBuffer.put(15f);
        lightFloatBuffer.put(10f);
        lightFloatBuffer.put(1f);
        lightFloatBuffer.flip();

        log.info("OpenGL version: " + GL.o1.getString(GetString.VERSION));

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

            // Clear screen and reset modelview matrix
            GL.o1.clear();
            GL.o1.setModelViewIdentity();

            camera.updateOpenGL();

            GL.o1.light(Light.LIGHT0, Light.POSITION, lightFloatBuffer);

            // Render directly
            List<GameObject> gameObjects = dynamicMeshesWorld.getGameObjects();
            GL.renderer1.render(gameObjects);
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
