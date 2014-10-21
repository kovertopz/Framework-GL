/**
 * Copyright 2012 Jason Sorensen (sorensenj@smert.net)
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
package net.smert.frameworkgl.opengl.renderer;

import java.util.List;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.gameobjects.GameObject;
import net.smert.frameworkgl.math.Transform4f;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.camera.Camera;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderable.shared.DisplayListRenderable;
import net.smert.frameworkgl.opengl.renderable.shared.ImmediateModeRenderable;
import net.smert.frameworkgl.opengl.renderable.shared.VertexArrayRenderable;
import net.smert.frameworkgl.opengl.renderable.shared.VertexBufferObjectDynamicInterleavedRenderable;
import net.smert.frameworkgl.opengl.renderable.shared.VertexBufferObjectDynamicNonInterleavedRenderable;
import net.smert.frameworkgl.opengl.renderable.shared.VertexBufferObjectInterleavedRenderable;
import net.smert.frameworkgl.opengl.renderable.shared.VertexBufferObjectNonInterleavedRenderable;
import net.smert.frameworkgl.opengl.shader.AbstractShader;
import net.smert.frameworkgl.utils.Color;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class RendererGL2 extends AbstractRendererGL {

    public RendererGL2(GLFontRenderer glFontRenderer) {
        super(glFontRenderer);
    }

    private void render(AbstractRenderable renderable) {
        Renderable.shaderBindState.sendUniformMatrices();
        renderable.render();
    }

    public VertexArrayRenderable createArrayRenderable() {
        return GL.rf2.createArrayRenderable();
    }

    public DisplayListRenderable createDisplayListRenderable() {
        return GL.rf2.createDisplayList();
    }

    public VertexBufferObjectDynamicInterleavedRenderable createDynamicInterleavedRenderable() {
        return GL.rf2.createDynamicInterleavedRenderable();
    }

    public VertexBufferObjectDynamicNonInterleavedRenderable createDynamicNonInterleavedRenderable() {
        return GL.rf2.createDynamicNonInterleavedRenderable();
    }

    public ImmediateModeRenderable createImmediateModeRenderable() {
        return GL.rf2.createImmediateMode();
    }

    public VertexBufferObjectInterleavedRenderable createInterleavedRenderable() {
        return GL.rf2.createInterleavedRenderable();
    }

    public VertexBufferObjectNonInterleavedRenderable createNonInterleavedRenderable() {
        return GL.rf2.createNonInterleavedRenderable();
    }

    public void destroy() {
        Renderable.bindState2.reset();
    }

    public void init() {
        Renderable.bindState2.setAttribLocations(GL.defaultAttribLocations);
    }

    @Override
    public void color(float r, float g, float b, float a) {
        GL.o1.color(r, g, b, a);
    }

    @Override
    public void render(AbstractRenderable renderable, float x, float y, float z) {
        GL.matrixHelper.setModeModel();
        GL.matrixHelper.loadIdentity();
        GL.matrixHelper.translate(x, y, z);
        render(renderable);
    }

    @Override
    public void render(AbstractRenderable renderable, Transform4f transform) {
        GL.matrixHelper.setModeModel();
        GL.matrixHelper.load(transform);
        render(renderable);
    }

    @Override
    public void render(AbstractRenderable renderable, Vector3f position) {
        GL.matrixHelper.setModeModel();
        GL.matrixHelper.loadIdentity();
        GL.matrixHelper.translate(position.getX(), position.getY(), position.getZ());
        renderable.render();
    }

    @Override
    public void render(GameObject gameObject) {
        GL.matrixHelper.setModeModel();
        GL.matrixHelper.load(gameObject.getWorldTransform());
        render(gameObject.getRenderable());
    }

    @Override
    public void render(List<GameObject> gameObjects) {
        for (GameObject gameObject : gameObjects) {
            render(gameObject);
        }
    }

    @Override
    public void renderBlend(GameObject gameObject) {
        if (gameObject.getRenderableState().isOpaque()) {
            return;
        }
        GL.o1.enableBlending();
        render(gameObject);
        GL.o1.disableBlending();
    }

    @Override
    public void renderBlend(List<GameObject> gameObjects) {
        GL.o1.enableBlending();
        for (GameObject gameObject : gameObjects) {
            if (gameObject.getRenderableState().isOpaque()) {
                continue;
            }
            render(gameObject);
        }
        GL.o1.disableBlending();
    }

    @Override
    public void renderOpaque(GameObject gameObject) {
        if (!gameObject.getRenderableState().isOpaque()) {
            return;
        }
        render(gameObject);
    }

    @Override
    public void renderOpaque(List<GameObject> gameObjects) {
        for (GameObject gameObject : gameObjects) {
            if (!gameObject.getRenderableState().isOpaque()) {
                continue;
            }
            render(gameObject);
        }
    }

    @Override
    public void set2DMode() {
        GL.matrixHelper.setModeProjection();
        GL.matrixHelper.setOrthogonal(0f, Fw.config.getCurrentWidth(), 0f, Fw.config.getCurrentHeight(), -1f, 1f);
        GL.matrixHelper.setModeView();
        GL.matrixHelper.loadIdentity();
        GL.matrixHelper.setModeModel();
        GL.matrixHelper.loadIdentity();
    }

    @Override
    public void set2DMode(int width, int height) {
        GL.matrixHelper.setModeProjection();
        GL.matrixHelper.setOrthogonal(0f, width, 0f, height, -1f, 1f);
        GL.matrixHelper.setModeView();
        GL.matrixHelper.loadIdentity();
        GL.matrixHelper.setModeModel();
        GL.matrixHelper.loadIdentity();
    }

    @Override
    public void setCamera(Camera camera) {
        camera.updateViewMatrix();
        GL.matrixHelper.setModeProjection();
        GL.matrixHelper.load(camera.getProjectionMatrix());
        GL.matrixHelper.setModeView();
        GL.matrixHelper.load(camera.getViewMatrix());
        GL.matrixHelper.setModeModel();
    }

    @Override
    public void switchShader(AbstractShader shader) {
        Renderable.shaderBindState.switchShader(shader);
    }

    @Override
    public void unbindShader() {
        Renderable.shaderBindState.unbindShader();
    }

    @Override
    public void colorText(Color color) {
        GL.o1.color(color.getR(), color.getG(), color.getB(), color.getA());
    }

    @Override
    public AbstractRenderable createGlyphRenderable() {
        return createInterleavedRenderable();
    }

    @Override
    public void popMatrix() {
        GL.matrixHelper.setModeModel();
        GL.matrixHelper.pop();
    }

    @Override
    public void pushMatrix() {
        GL.matrixHelper.setModeModel();
        GL.matrixHelper.push();
    }

    @Override
    public void renderGlyph(AbstractRenderable renderable) {
        render(renderable);
    }

    @Override
    public void translateText(float x, float y) {
        GL.matrixHelper.setModeModel();
        GL.matrixHelper.translate(x, y, 0f);
    }

}
