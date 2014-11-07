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
package net.smert.frameworkgl.examples.gl3vertexbufferobject;

import java.io.IOException;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.Screen;
import net.smert.frameworkgl.examples.common.CubeMeshForTriangles;
import net.smert.frameworkgl.examples.common.CubeMeshForTrianglesWithPerVertexColors;
import net.smert.frameworkgl.helpers.Keyboard;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.camera.Camera;
import net.smert.frameworkgl.opengl.camera.CameraController;
import net.smert.frameworkgl.opengl.constants.GetString;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;
import net.smert.frameworkgl.opengl.shader.basic.ColorShader;
import net.smert.frameworkgl.utils.FpsTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VertexBufferObject extends Screen {

    private final static Logger log = LoggerFactory.getLogger(VertexBufferObject.class);

    private AbstractRenderable renderableTriangles;
    private AbstractRenderable renderableTrianglesWithPerVertexColors;
    private Camera camera;
    private CameraController cameraController;
    private ColorShader colorShader;
    private FpsTimer fpsTimer;
    private Mesh meshTriangles;
    private Mesh meshTrianglesWithPerVertexColors;

    public VertexBufferObject(String[] args) {
    }

    private void handleInput() {
        if (Fw.input.isKeyDown(Keyboard.ESCAPE)) {
            Fw.app.stopRunning();
        }
        cameraController.update();
    }

    @Override
    public void destroy() {
        renderableTriangles.destroy();
        renderableTrianglesWithPerVertexColors.destroy();
        Fw.input.removeInputProcessor(cameraController);
        Fw.input.releaseMouseCursor();
    }

    @Override
    public void init() {

        // Switch renderer and factory to OpenGL 3
        Fw.graphics.switchOpenGLVersion(3);

        // Create timer
        fpsTimer = new FpsTimer();

        // Setup camera and controller
        camera = GL.cameraFactory.createCamera();
        camera.lookAt(new Vector3f(0f, 0f, 5f), new Vector3f(0f, 0f, -1f), Vector3f.WORLD_Y_AXIS);
        camera.setPerspectiveProjection(
                70f,
                (float) Fw.config.getCurrentWidth() / (float) Fw.config.getCurrentHeight(),
                .05f, 128f);
        cameraController = GL.cameraFactory.createCameraController();
        cameraController.setCamera(camera);

        // Create meshes
        meshTriangles = new CubeMeshForTriangles();
        meshTrianglesWithPerVertexColors = new CubeMeshForTrianglesWithPerVertexColors();

        // Create vertex buffer object renderables
        renderableTriangles = Fw.graphics.createNonInterleavedRenderable();
        renderableTriangles.create(meshTriangles);
        renderableTrianglesWithPerVertexColors = Fw.graphics.createNonInterleavedRenderable();
        renderableTrianglesWithPerVertexColors.create(meshTrianglesWithPerVertexColors);

        // Build shaders
        try {
            colorShader = ColorShader.Factory.Create();
            colorShader.init();
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
        Fw.input.grabMouseCursor();
    }

    @Override
    public void pause() {
    }

    @Override
    public void render() {
        fpsTimer.update();

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
            Fw.graphics.switchShader(colorShader);

            // Render directly
            Fw.graphics.render(renderableTriangles, -2f, 0f, 0f);
            Fw.graphics.render(renderableTriangles, 2f, 0f, 0f);
            Fw.graphics.render(renderableTrianglesWithPerVertexColors, 0f, 2f, 0f);

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
