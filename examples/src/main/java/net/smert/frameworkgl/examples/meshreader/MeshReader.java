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
package net.smert.frameworkgl.examples.meshreader;

import java.io.IOException;
import java.nio.FloatBuffer;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.Screen;
import net.smert.frameworkgl.helpers.Keyboard;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.camera.LegacyCamera;
import net.smert.frameworkgl.opengl.camera.LegacyCameraController;
import net.smert.frameworkgl.opengl.constants.GetString;
import net.smert.frameworkgl.opengl.constants.Light;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;
import net.smert.frameworkgl.utils.FpsTimer;
import net.smert.frameworkgl.utils.MemoryUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class MeshReader extends Screen {

    private final static Logger log = LoggerFactory.getLogger(MeshReader.class);

    private AbstractRenderable renderableCapsule;
    private AbstractRenderable renderableCone;
    private AbstractRenderable renderableCube;
    private AbstractRenderable renderableCylinder;
    private AbstractRenderable renderableIcoSphere;
    private AbstractRenderable renderableTorus;
    private AbstractRenderable renderableUvSphere;
    private FloatBuffer lightFloatBuffer;
    private FpsTimer fpsTimer;
    private LegacyCamera camera;
    private LegacyCameraController cameraController;
    private MemoryUsage memoryUsage;
    private Mesh meshCapsule;
    private Mesh meshCone;
    private Mesh meshCube;
    private Mesh meshCylinder;
    private Mesh meshIcoSphere;
    private Mesh meshTorus;
    private Mesh meshUvSphere;

    public MeshReader(String[] args) {
    }

    private void handleInput() {
        if (Fw.input.isKeyDown(Keyboard.ESCAPE) == true) {
            Fw.app.stopRunning();
        }
        cameraController.update();
    }

    @Override
    public void destroy() {
        renderableCapsule.destroy();
        renderableCone.destroy();
        renderableCube.destroy();
        renderableCylinder.destroy();
        renderableIcoSphere.destroy();
        renderableTorus.destroy();
        renderableUvSphere.destroy();
        Fw.input.removeInputProcessor(cameraController);
        Fw.input.releaseMouseCursor();
    }

    @Override
    public void init() {

        // Create timer
        fpsTimer = new FpsTimer();

        // Setup camera and controller
        camera = new LegacyCamera();
        camera.setPosition(0.0f, 0.0f, 5.0f);
        cameraController = new LegacyCameraController(camera);

        // Memory usage
        memoryUsage = new MemoryUsage();

        // Float buffer for light
        lightFloatBuffer = GL.bufferHelper.createFloatBuffer(4);

        // Create meshes
        meshCapsule = GL.mf.createMesh();
        meshCone = GL.mf.createMesh();
        meshCube = GL.mf.createMesh();
        meshCylinder = GL.mf.createMesh();
        meshIcoSphere = GL.mf.createMesh();
        meshTorus = GL.mf.createMesh();
        meshUvSphere = GL.mf.createMesh();

        // Load the meshes from obj models
        try {
            Fw.graphics.loadMesh("primitives/capsule.obj", meshCapsule);
            meshCapsule.setAllColors(0.5f, 0.0f, 0.0f, 1.0f);
            meshCapsule.updateBooleansFromSegment();
            Fw.graphics.loadMesh("primitives/cone.obj", meshCone);
            meshCone.setAllColors(1.0f, 0.5f, 0.32f, 1.0f);
            meshCone.updateBooleansFromSegment();
            Fw.graphics.loadMesh("primitives/cube.obj", meshCube);
            meshCube.setAllColors(0.0f, 0.5f, 1.0f, 1.0f);
            meshCube.updateBooleansFromSegment();
            Fw.graphics.loadMesh("primitives/cylinder.obj", meshCylinder);
            meshCylinder.setAllColors(1.0f, 0.843f, 0.0f, 1.0f);
            meshCylinder.updateBooleansFromSegment();
            Fw.graphics.loadMesh("primitives/icosphere.obj", meshIcoSphere);
            meshIcoSphere.setAllColors(1.0f, 0.271f, 0.0f, 1.0f);
            meshIcoSphere.updateBooleansFromSegment();
            Fw.graphics.loadMesh("primitives/torus.obj", meshTorus);
            meshTorus.setAllColors(0.5f, 1.0f, 0.831f, 1.0f);
            meshTorus.updateBooleansFromSegment();
            Fw.graphics.loadMesh("primitives/uvsphere.obj", meshUvSphere);
            meshUvSphere.setAllColors(0.576f, 0.439f, 0.859f, 1.0f);
            meshUvSphere.updateBooleansFromSegment();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // Create vertex buffer object renderables
        renderableCapsule = Fw.graphics.createVertexBufferObjectRenderable();
        renderableCapsule.create(meshCapsule);
        renderableCone = Fw.graphics.createVertexBufferObjectRenderable();
        renderableCone.create(meshCone);
        renderableCube = Fw.graphics.createVertexBufferObjectRenderable();
        renderableCube.create(meshCube);
        renderableCylinder = Fw.graphics.createVertexBufferObjectRenderable();
        renderableCylinder.create(meshCylinder);
        renderableIcoSphere = Fw.graphics.createVertexBufferObjectRenderable();
        renderableIcoSphere.create(meshIcoSphere);
        renderableTorus = Fw.graphics.createVertexBufferObjectRenderable();
        renderableTorus.create(meshTorus);
        renderableUvSphere = Fw.graphics.createVertexBufferObjectRenderable();
        renderableUvSphere.create(meshUvSphere);

        GL.o1.enableCulling();
        GL.o1.cullBackFaces();
        GL.o1.enableDepthTest();
        GL.o1.setDepthFuncLess();
        GL.o1.enableDepthMask();
        GL.o1.setClearDepth(1.0f);
        GL.o1.enableColorMaterial();
        GL.o1.enableLight0();
        GL.o1.enableLighting();
        GL.o1.setSmoothLighting(true);
        GL.o1.clear();

        GL.o1.setProjectionPerspective(
                70.0f,
                (float) Fw.config.getCurrentWidth() / (float) Fw.config.getCurrentHeight(),
                0.05f, 512.0f);
        GL.o1.setModelViewIdentity();

        // Light position
        lightFloatBuffer.put(0.0f);
        lightFloatBuffer.put(15.0f);
        lightFloatBuffer.put(10.0f);
        lightFloatBuffer.put(1.0f);
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
            Fw.graphics.render(renderableCapsule, -3.0f, 0.0f, 3.0f);
            Fw.graphics.render(renderableCone, 3.0f, 0.0f, 3.0f);
            Fw.graphics.render(renderableCube, -3.0f, 0.0f, 0.0f);
            Fw.graphics.render(renderableCylinder, 3.0f, 0.0f, 0.0f);
            Fw.graphics.render(renderableIcoSphere, -3.0f, 0.0f, -3.0f);
            Fw.graphics.render(renderableUvSphere, 3.0f, 0.0f, -3.0f);
            Fw.graphics.render(renderableTorus, 0.0f, 3.0f, 0.0f);
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
