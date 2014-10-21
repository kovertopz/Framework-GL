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
package net.smert.frameworkgl.examples.gl2meshreadertextured;

import java.io.IOException;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.Screen;
import net.smert.frameworkgl.helpers.Keyboard;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.math.Vector4f;
import net.smert.frameworkgl.opengl.AmbientLight;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.GLLight;
import net.smert.frameworkgl.opengl.MaterialLight;
import net.smert.frameworkgl.opengl.camera.Camera;
import net.smert.frameworkgl.opengl.camera.CameraController;
import net.smert.frameworkgl.opengl.constants.GetString;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;
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
public class MeshReaderTextured extends Screen {

    private final static Logger log = LoggerFactory.getLogger(MeshReaderTextured.class);

    private AbstractRenderable renderableCrateAndBarrel;
    private AmbientLight ambientLight;
    private Camera camera;
    private CameraController cameraController;
    private DiffusePointShader vertexLitSingleDiffusePointShader;
    private DiffuseTextureShader diffuseTextureShader;
    private FpsTimer fpsTimer;
    private GLLight glLight;
    private MaterialLight materialLight;
    private MemoryUsage memoryUsage;
    private Mesh meshCrateAndBarrel;

    public MeshReaderTextured(String[] args) {
    }

    private void handleInput() {
        if (Fw.input.isKeyDown(Keyboard.ESCAPE) == true) {
            Fw.app.stopRunning();
        }
        cameraController.update();
    }

    @Override
    public void destroy() {
        renderableCrateAndBarrel.destroy();
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

        // Create ambient light, glLight and material light
        ambientLight = GL.glFactory.createAmbientLight();
        glLight = GL.glFactory.createGLLight();
        glLight.setPosition(new Vector4f(0f, 15f, 10f, 1f));
        glLight.setRadius(256f); // Shader uses this value and OpenGL does not
        materialLight = GL.glFactory.createMaterialLight();

        // Create meshes
        meshCrateAndBarrel = GL.meshFactory.createMesh();

        // Load the meshes from obj models
        try {
            Fw.graphics.loadMesh("crate_barrel/crate and barrel.obj", meshCrateAndBarrel);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // Load textures
        try {
            Fw.graphics.loadTextures(meshCrateAndBarrel);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // Create vertex buffer object renderables
        renderableCrateAndBarrel = Fw.graphics.createNonInterleavedRenderable();
        renderableCrateAndBarrel.create(meshCrateAndBarrel);

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
        GL.o1.enableTexture2D();
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

            // Bind shader
            Fw.graphics.switchShader(vertexLitSingleDiffusePointShader);

            // Update uniforms
            vertexLitSingleDiffusePointShader.getUniforms().setAmbientLight(ambientLight);
            // Disable glEnable(GL_COLOR_MATERIAL) on shader (enabled by default)
            vertexLitSingleDiffusePointShader.getUniforms().setColorMaterialAmbient(0f);
            vertexLitSingleDiffusePointShader.getUniforms().setColorMaterialDiffuse(0f);
            vertexLitSingleDiffusePointShader.getUniforms().setLight(glLight);
            vertexLitSingleDiffusePointShader.getUniforms().setMaterialLight(materialLight);

            // Render directly
            Fw.graphics.render(renderableCrateAndBarrel, -1.5f, -1.5f, 0f);

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
