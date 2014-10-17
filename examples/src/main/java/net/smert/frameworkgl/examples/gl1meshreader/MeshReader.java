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
package net.smert.frameworkgl.examples.gl1meshreader;

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
        camera = GL.cameraFactory.createLegacyCamera();
        camera.setPosition(0f, 0f, 5f);
        cameraController = GL.cameraFactory.createLegacyCameraController();
        cameraController.setCamera(camera);

        // Memory usage
        memoryUsage = new MemoryUsage();

        // Float buffer for light
        lightFloatBuffer = GL.bufferHelper.createFloatBuffer(4);

        // Create meshes
        meshCapsule = GL.meshFactory.createMesh();
        meshCone = GL.meshFactory.createMesh();
        meshCube = GL.meshFactory.createMesh();
        meshCylinder = GL.meshFactory.createMesh();
        meshIcoSphere = GL.meshFactory.createMesh();
        meshTorus = GL.meshFactory.createMesh();
        meshUvSphere = GL.meshFactory.createMesh();

        // Load the meshes from obj models
        try {
            Fw.graphics.loadMesh("primitives/capsule.obj", meshCapsule);
            meshCapsule.setAllColors(.5f, 0f, 0f, 1f);
            meshCapsule.updateBooleansFromSegment();
            Fw.graphics.loadMesh("primitives/cone.obj", meshCone);
            meshCone.setAllColors(1f, .5f, .32f, 1f);
            meshCone.updateBooleansFromSegment();
            Fw.graphics.loadMesh("primitives/cube.obj", meshCube);
            meshCube.setAllColors(0f, .5f, 1f, 1f);
            meshCube.updateBooleansFromSegment();
            Fw.graphics.loadMesh("primitives/cylinder.obj", meshCylinder);
            meshCylinder.setAllColors(1f, .843f, 0f, 1f);
            meshCylinder.updateBooleansFromSegment();
            Fw.graphics.loadMesh("primitives/icosphere.obj", meshIcoSphere);
            meshIcoSphere.setAllColors(1f, .271f, 0f, 1f);
            meshIcoSphere.updateBooleansFromSegment();
            Fw.graphics.loadMesh("primitives/torus.obj", meshTorus);
            meshTorus.setAllColors(.5f, 1f, .831f, 1f);
            meshTorus.updateBooleansFromSegment();
            Fw.graphics.loadMesh("primitives/uvsphere.obj", meshUvSphere);
            meshUvSphere.setAllColors(.576f, .439f, .859f, 1f);
            meshUvSphere.updateBooleansFromSegment();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // Create vertex buffer object renderables
        renderableCapsule = GL.renderer1.createNonInterleavedRenderable();
        renderableCapsule.create(meshCapsule);
        renderableCone = GL.renderer1.createNonInterleavedRenderable();
        renderableCone.create(meshCone);
        renderableCube = GL.renderer1.createNonInterleavedRenderable();
        renderableCube.create(meshCube);
        renderableCylinder = GL.renderer1.createNonInterleavedRenderable();
        renderableCylinder.create(meshCylinder);
        renderableIcoSphere = GL.renderer1.createNonInterleavedRenderable();
        renderableIcoSphere.create(meshIcoSphere);
        renderableTorus = GL.renderer1.createNonInterleavedRenderable();
        renderableTorus.create(meshTorus);
        renderableUvSphere = GL.renderer1.createNonInterleavedRenderable();
        renderableUvSphere.create(meshUvSphere);

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
            GL.renderer1.render(renderableCapsule, -3f, 0f, 3f);
            GL.renderer1.render(renderableCone, 3f, 0f, 3f);
            GL.renderer1.render(renderableCube, -3f, 0f, 0f);
            GL.renderer1.render(renderableCylinder, 3f, 0f, 0f);
            GL.renderer1.render(renderableIcoSphere, -3f, 0f, -3f);
            GL.renderer1.render(renderableUvSphere, 3f, 0f, -3f);
            GL.renderer1.render(renderableTorus, 0f, 3f, 0f);
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
