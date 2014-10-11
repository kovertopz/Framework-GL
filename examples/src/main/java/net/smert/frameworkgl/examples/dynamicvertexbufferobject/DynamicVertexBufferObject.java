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
package net.smert.frameworkgl.examples.dynamicvertexbufferobject;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.gameobjects.GameObject;
import net.smert.frameworkgl.Screen;
import net.smert.frameworkgl.examples.common.DynamicMeshWorld;
import net.smert.frameworkgl.gameobjects.ViewFrustumGameObject;
import net.smert.frameworkgl.helpers.Keyboard;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.camera.Camera;
import net.smert.frameworkgl.opengl.camera.CameraController;
import net.smert.frameworkgl.opengl.camera.FrustumCullingClipSpaceSymmetrical;
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
public class DynamicVertexBufferObject extends Screen {

    private final static Logger log = LoggerFactory.getLogger(DynamicVertexBufferObject.class);

    private boolean wireframe;
    private DynamicMeshWorld dynamicMeshesWorld;
    private DynamicSphereGameObject dynamicSphereGameObject;
    private FloatBuffer lightFloatBuffer;
    private FloatBuffer viewMatrixFloatBuffer;
    private FloatBuffer projectionMatrixFloatBuffer;
    private FloatBuffer transformWorldFloatBuffer;
    private FpsTimer fpsTimer;
    private Camera camera;
    private CameraController cameraController;
    private final List<GameObject> gameObjectsToRender;
    private MemoryUsage memoryUsage;
    private ViewFrustumGameObject viewFrustumGameObject;

    public DynamicVertexBufferObject(String[] args) {
        wireframe = false;
        gameObjectsToRender = new ArrayList<>();
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
        if (Fw.input.isKeyDown(Keyboard.F)) {
            camera.updatePlanes();
            viewFrustumGameObject.getRenderableState().setInFrustum(true);
            viewFrustumGameObject.getWorldTransform().getRotation().set(camera.getRotationMatrix());
            viewFrustumGameObject.setWorldPosition(camera.getPosition());
            viewFrustumGameObject.update(
                    camera.getAspectRatio(), camera.getFieldOfView(), camera.getZNear(), camera.getZFar());
            Fw.graphics.updateAabb(viewFrustumGameObject);
            Fw.graphics.performCulling(camera, dynamicMeshesWorld.getGameObjects());
            Fw.graphics.performCulling(camera, dynamicSphereGameObject);
            updateGameObjectsToRender();
        }
        cameraController.update();
    }

    private void updateGameObjectsToRender() {
        gameObjectsToRender.clear();
        for (GameObject gameObject : dynamicMeshesWorld.getGameObjects()) {
            if (gameObject.getRenderableState().isInFrustum()) {
                gameObjectsToRender.add(gameObject);
            }
        }
        if (dynamicSphereGameObject.getRenderableState().isInFrustum()) {
            gameObjectsToRender.add(dynamicSphereGameObject);
        }
        if (viewFrustumGameObject.getRenderableState().isInFrustum()) {
            gameObjectsToRender.add(viewFrustumGameObject);
        }
        // Sorting is not truly necessary in this demo since there is only one object
        // that needs blending and it gets added to the end of the list.
        Fw.graphics.sort(gameObjectsToRender, camera.getPosition());
    }

    @Override
    public void destroy() {
        for (GameObject gameObject : dynamicMeshesWorld.getGameObjects()) {
            gameObject.destroy();
        }
        dynamicSphereGameObject.destroy();
        viewFrustumGameObject.destroy();
        Fw.input.removeInputProcessor(cameraController);
        Fw.input.releaseMouseCursor();
    }

    @Override
    public void init() {

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

        // Float buffer for light and matrices
        lightFloatBuffer = GL.bufferHelper.createFloatBuffer(4);
        viewMatrixFloatBuffer = GL.bufferHelper.createFloatBuffer(16);
        projectionMatrixFloatBuffer = GL.bufferHelper.createFloatBuffer(16);
        transformWorldFloatBuffer = GL.bufferHelper.createFloatBuffer(16);

        // Create dynamic mesh world
        dynamicMeshesWorld = new DynamicMeshWorld();
        dynamicMeshesWorld.init();

        // Dynamic sphere
        dynamicSphereGameObject = new DynamicSphereGameObject();
        dynamicSphereGameObject.init();

        // View frustum game object
        viewFrustumGameObject = new ViewFrustumGameObject();
        viewFrustumGameObject.getColor0().set("black");
        viewFrustumGameObject.getColor1().set("yellow");
        viewFrustumGameObject.getColor2().set("yellow");
        viewFrustumGameObject.getColor3().set("white");
        viewFrustumGameObject.getColor3().setA(.4f);
        viewFrustumGameObject.getRenderableState().setInFrustum(false);
        viewFrustumGameObject.init(
                camera.getAspectRatio(), camera.getFieldOfView(), camera.getZNear(), camera.getZFar());

        // Frustum culling
        FrustumCullingClipSpaceSymmetrical frustumCulling = GL.cameraFactory.createFrustumCullingClipSpaceSymmetrical();
        camera.setFrustumCulling(frustumCulling);
        updateGameObjectsToRender();

        // Update AABBs
        Fw.graphics.updateAabb(dynamicMeshesWorld.getGameObjects());

        GL.o1.setBlendingFunctionSrcAlphaAndOneMinusSrcAlpha();
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

            // Clear screen
            GL.o1.clear();

            // Update dynamic mesh
            dynamicSphereGameObject.update();

            // Update AABB
            Fw.graphics.updateAabb(dynamicSphereGameObject);

            // Update camera
            camera.updateViewMatrix();
            camera.getProjectionMatrix().toFloatBuffer(projectionMatrixFloatBuffer);
            camera.getViewMatrix().toFloatBuffer(viewMatrixFloatBuffer);
            projectionMatrixFloatBuffer.flip();
            viewMatrixFloatBuffer.flip();
            GL.o1.switchProjection();
            GL.o1.loadMatrix(projectionMatrixFloatBuffer);
            GL.o1.switchModelView();
            GL.o1.loadMatrix(viewMatrixFloatBuffer);

            GL.o1.light(Light.LIGHT0, Light.POSITION, lightFloatBuffer);

            // Render directly
            Fw.graphics.getLegacyRenderer().renderOpaque(gameObjectsToRender, transformWorldFloatBuffer);
            Fw.graphics.getLegacyRenderer().renderBlend(gameObjectsToRender, transformWorldFloatBuffer);
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
