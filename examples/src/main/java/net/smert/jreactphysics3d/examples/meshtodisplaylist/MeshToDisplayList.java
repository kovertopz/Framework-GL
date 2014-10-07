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
package net.smert.jreactphysics3d.examples.meshtodisplaylist;

import net.smert.jreactphysics3d.examples.common.CubeMeshForQuads;
import net.smert.jreactphysics3d.examples.common.CubeMeshForQuadsWithPerVertexColors;
import net.smert.jreactphysics3d.examples.common.CubeMeshForTriangles;
import net.smert.jreactphysics3d.framework.Fw;
import net.smert.jreactphysics3d.framework.Screen;
import net.smert.jreactphysics3d.framework.helpers.Keyboard;
import net.smert.jreactphysics3d.framework.opengl.GL;
import net.smert.jreactphysics3d.framework.opengl.camera.LegacyCamera;
import net.smert.jreactphysics3d.framework.opengl.camera.LegacyCameraController;
import net.smert.jreactphysics3d.framework.opengl.constants.GetString;
import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.renderable.AbstractRenderable;
import net.smert.jreactphysics3d.framework.utils.FpsTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class MeshToDisplayList extends Screen {

    private final static Logger log = LoggerFactory.getLogger(MeshToDisplayList.class);

    private AbstractRenderable renderableQuads;
    private AbstractRenderable renderableQuadsWithPerVertexColors;
    private AbstractRenderable renderableTriangles;
    private FpsTimer fpsTimer;
    private LegacyCamera camera;
    private LegacyCameraController cameraController;
    private Mesh meshQuads;
    private Mesh meshQuadsWithPerVertexColors;
    private Mesh meshTriangles;

    public MeshToDisplayList(String[] args) {
    }

    private void handleInput() {
        if (Fw.input.isKeyDown(Keyboard.ESCAPE) == true) {
            Fw.app.stopRunning();
        }
        cameraController.update();
    }

    @Override
    public void destroy() {
        renderableQuads.destroy();
        renderableQuadsWithPerVertexColors.destroy();
        renderableTriangles.destroy();
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

        // Create meshes
        meshQuads = new CubeMeshForQuads();
        meshQuadsWithPerVertexColors = new CubeMeshForQuadsWithPerVertexColors();
        meshTriangles = new CubeMeshForTriangles();

        // Create display list renderables
        renderableQuads = Fw.graphics.createDisplayListRenderable();
        renderableQuads.create(meshQuads);
        renderableQuadsWithPerVertexColors = Fw.graphics.createDisplayListRenderable();
        renderableQuadsWithPerVertexColors.create(meshQuadsWithPerVertexColors);
        renderableTriangles = Fw.graphics.createDisplayListRenderable();
        renderableTriangles.create(meshTriangles);

        GL.o1.enableCulling();
        GL.o1.cullBackFaces();
        GL.o1.enableDepthTest();
        GL.o1.setDepthFuncLess();
        GL.o1.enableDepthMask();
        GL.o1.setClearDepth(1.0f);
        GL.o1.setSmoothLighting(true);
        GL.o1.clear();

        GL.o1.setProjectionPerspective(
                70.0f,
                (float) Fw.config.getCurrentWidth() / (float) Fw.config.getCurrentHeight(),
                0.05f, 128.0f);
        GL.o1.setModelViewIdentity();

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

        if (Fw.timer.isGameTick()) {
            // Do nothing
        }

        if (Fw.timer.isRenderTick()) {
            handleInput();

            // Clear screen and reset modelview matrix
            GL.o1.clear();
            GL.o1.setModelViewIdentity();

            camera.updateOpenGL();

            // Render directly
            Fw.graphics.render(renderableTriangles, -2.0f, 0.0f, 0.0f);
            Fw.graphics.render(renderableQuads, 2.0f, 0.0f, 0.0f);
            Fw.graphics.render(renderableQuadsWithPerVertexColors, 0.0f, 2.0f, 0.0f);
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
