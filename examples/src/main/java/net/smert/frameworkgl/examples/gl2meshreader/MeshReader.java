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
package net.smert.frameworkgl.examples.gl2meshreader;

import java.io.IOException;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.Screen;
import net.smert.frameworkgl.helpers.Keyboard;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.math.Vector4f;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.GLLight;
import net.smert.frameworkgl.opengl.camera.Camera;
import net.smert.frameworkgl.opengl.camera.CameraController;
import net.smert.frameworkgl.opengl.constants.GetString;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;
import net.smert.frameworkgl.opengl.shader.vertexlit.single.DiffusePointShader;
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
    private Camera camera;
    private CameraController cameraController;
    private DiffusePointShader vertexLitSingleDiffusePointShader;
    private FpsTimer fpsTimer;
    private GLLight glLight;
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
        if (Fw.input.isKeyDown(Keyboard.ESCAPE)) {
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

        // Switch renderer and factory to OpenGL 2
        Fw.graphics.switchRenderableFactoryAndRenderer(2);

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

        // Memory usage
        memoryUsage = new MemoryUsage();

        // Create glLight
        glLight = GL.glFactory.createGLLight();
        glLight.setPosition(new Vector4f(0f, 15f, 10f, 1f));
        glLight.setRadius(256f); // Shader uses this value and OpenGL does not

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
            meshCapsule.updateHasBooleansFromSegment();
            Fw.graphics.loadMesh("primitives/cone.obj", meshCone);
            meshCone.setAllColors(1f, .5f, .32f, 1f);
            meshCone.updateHasBooleansFromSegment();
            Fw.graphics.loadMesh("primitives/cube.obj", meshCube);
            meshCube.setAllColors(0f, .5f, 1f, 1f);
            meshCube.updateHasBooleansFromSegment();
            Fw.graphics.loadMesh("primitives/cylinder.obj", meshCylinder);
            meshCylinder.setAllColors(1f, .843f, 0f, 1f);
            meshCylinder.updateHasBooleansFromSegment();
            Fw.graphics.loadMesh("primitives/icosphere.obj", meshIcoSphere);
            meshIcoSphere.setAllColors(1f, .271f, 0f, 1f);
            meshIcoSphere.updateHasBooleansFromSegment();
            Fw.graphics.loadMesh("primitives/torus.obj", meshTorus);
            meshTorus.setAllColors(.5f, 1f, .831f, 1f);
            meshTorus.updateHasBooleansFromSegment();
            Fw.graphics.loadMesh("primitives/uvsphere.obj", meshUvSphere);
            meshUvSphere.setAllColors(.576f, .439f, .859f, 1f);
            meshUvSphere.updateHasBooleansFromSegment();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // Create vertex buffer object renderables
        renderableCapsule = Fw.graphics.createNonInterleavedRenderable();
        renderableCapsule.create(meshCapsule);
        renderableCone = Fw.graphics.createNonInterleavedRenderable();
        renderableCone.create(meshCone);
        renderableCube = Fw.graphics.createNonInterleavedRenderable();
        renderableCube.create(meshCube);
        renderableCylinder = Fw.graphics.createNonInterleavedRenderable();
        renderableCylinder.create(meshCylinder);
        renderableIcoSphere = Fw.graphics.createNonInterleavedRenderable();
        renderableIcoSphere.create(meshIcoSphere);
        renderableTorus = Fw.graphics.createNonInterleavedRenderable();
        renderableTorus.create(meshTorus);
        renderableUvSphere = Fw.graphics.createNonInterleavedRenderable();
        renderableUvSphere.create(meshUvSphere);

        // Build shaders
        try {
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

            // Update global uniform variables
            GL.uniformVariables.setGlLight(glLight);

            // Bind shader
            Fw.graphics.switchShader(vertexLitSingleDiffusePointShader);

            // Render directly
            Fw.graphics.render(renderableCapsule, -3f, 0f, 3f);
            Fw.graphics.render(renderableCone, 3f, 0f, 3f);
            Fw.graphics.render(renderableCube, -3f, 0f, 0f);
            Fw.graphics.render(renderableCylinder, 3f, 0f, 0f);
            Fw.graphics.render(renderableIcoSphere, -3f, 0f, -3f);
            Fw.graphics.render(renderableUvSphere, 3f, 0f, -3f);
            Fw.graphics.render(renderableTorus, 0f, 3f, 0f);

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
