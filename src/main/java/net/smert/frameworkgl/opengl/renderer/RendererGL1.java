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

import java.nio.FloatBuffer;
import java.util.List;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.gameobjects.GameObject;
import net.smert.frameworkgl.math.Transform4f;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.camera.Camera;
import net.smert.frameworkgl.opengl.font.GLFont;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderable.gl1.DisplayListRenderable;
import net.smert.frameworkgl.opengl.renderable.gl1.ImmediateModeRenderable;
import net.smert.frameworkgl.opengl.renderable.shared.VertexArrayRenderable;
import net.smert.frameworkgl.opengl.renderable.shared.VertexBufferObjectDynamicInterleavedRenderable;
import net.smert.frameworkgl.opengl.renderable.shared.VertexBufferObjectDynamicNonInterleavedRenderable;
import net.smert.frameworkgl.opengl.renderable.shared.VertexBufferObjectInterleavedRenderable;
import net.smert.frameworkgl.opengl.renderable.shared.VertexBufferObjectNonInterleavedRenderable;
import net.smert.frameworkgl.utils.Color;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class RendererGL1 implements Renderer, TextHelperRenderer, TextRenderer {

    private FloatBuffer viewMatrixFloatBuffer;
    private FloatBuffer transformWorldFloatBuffer;
    private FloatBuffer projectionMatrixFloatBuffer;
    private final GLFontRenderer glFontRenderer;

    public RendererGL1(GLFontRenderer glFontRenderer) {
        this.glFontRenderer = glFontRenderer;
    }

    private void render(AbstractRenderable renderable, FloatBuffer transformWorldFloatBuffer) {
        GL.o1.pushMatrix();
        GL.o1.multiplyMatrix(transformWorldFloatBuffer);
        renderable.render();
        GL.o1.popMatrix();
    }

    public VertexArrayRenderable createArrayRenderable() {
        return GL.rf1.createArrayRenderable();
    }

    public DisplayListRenderable createDisplayListRenderable() {
        return GL.rf1.createDisplayList();
    }

    public VertexBufferObjectDynamicInterleavedRenderable createDynamicInterleavedRenderable() {
        return GL.rf1.createDynamicInterleavedRenderable();
    }

    public VertexBufferObjectDynamicNonInterleavedRenderable createDynamicNonInterleavedRenderable() {
        return GL.rf1.createDynamicNonInterleavedRenderable();
    }

    public ImmediateModeRenderable createImmediateModeRenderable() {
        return GL.rf1.createImmediateMode();
    }

    public VertexBufferObjectInterleavedRenderable createInterleavedRenderable() {
        return GL.rf1.createInterleavedRenderable();
    }

    public VertexBufferObjectNonInterleavedRenderable createNonInterleavedRenderable() {
        return GL.rf1.createNonInterleavedRenderable();
    }

    public void destroy() {
        Renderable.bindState1.reset();
    }

    public void init() {
        viewMatrixFloatBuffer = GL.bufferHelper.createFloatBuffer(16);
        transformWorldFloatBuffer = GL.bufferHelper.createFloatBuffer(16);
        projectionMatrixFloatBuffer = GL.bufferHelper.createFloatBuffer(16);
    }

    @Override
    public void render(AbstractRenderable renderable, float x, float y, float z) {
        GL.o1.pushMatrix();
        GL.o1.translate(x, y, z);
        renderable.render();
        GL.o1.popMatrix();
    }

    @Override
    public void render(AbstractRenderable renderable, Transform4f transform) {
        transform.toFloatBuffer(transformWorldFloatBuffer);
        render(renderable, transformWorldFloatBuffer);
    }

    @Override
    public void render(AbstractRenderable renderable, Vector3f position) {
        GL.o1.pushMatrix();
        GL.o1.translate(position.getX(), position.getY(), position.getZ());
        renderable.render();
        GL.o1.popMatrix();
    }

    @Override
    public void render(GameObject gameObject) {
        gameObject.getWorldTransform().toFloatBuffer(transformWorldFloatBuffer);
        transformWorldFloatBuffer.flip();
        render(gameObject.getRenderable(), transformWorldFloatBuffer);
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
        gameObject.getWorldTransform().toFloatBuffer(transformWorldFloatBuffer);
        transformWorldFloatBuffer.flip();
        render(gameObject.getRenderable(), transformWorldFloatBuffer);
        GL.o1.disableBlending();
    }

    @Override
    public void renderBlend(List<GameObject> gameObjects) {
        GL.o1.enableBlending();
        for (GameObject gameObject : gameObjects) {
            if (gameObject.getRenderableState().isOpaque()) {
                continue;
            }
            gameObject.getWorldTransform().toFloatBuffer(transformWorldFloatBuffer);
            transformWorldFloatBuffer.flip();
            render(gameObject.getRenderable(), transformWorldFloatBuffer);
        }
        GL.o1.disableBlending();
    }

    @Override
    public void renderOpaque(GameObject gameObject) {
        if (!gameObject.getRenderableState().isOpaque()) {
            return;
        }
        gameObject.getWorldTransform().toFloatBuffer(transformWorldFloatBuffer);
        transformWorldFloatBuffer.flip();
        render(gameObject.getRenderable(), transformWorldFloatBuffer);
    }

    @Override
    public void renderOpaque(List<GameObject> gameObjects) {
        for (GameObject gameObject : gameObjects) {
            if (!gameObject.getRenderableState().isOpaque()) {
                continue;
            }
            gameObject.getWorldTransform().toFloatBuffer(transformWorldFloatBuffer);
            transformWorldFloatBuffer.flip();
            render(gameObject.getRenderable(), transformWorldFloatBuffer);
        }
    }

    @Override
    public void set2DMode() {
        GL.o1.setProjectionOrtho(0f, Fw.config.getCurrentWidth(), 0f, Fw.config.getCurrentHeight(), -1f, 1f);
        GL.o1.setModelViewIdentity();
    }

    @Override
    public void set2DMode(int width, int height) {
        GL.o1.setProjectionOrtho(0f, width, 0f, height, -1f, 1f);
        GL.o1.setModelViewIdentity();
    }

    @Override
    public void setCamera(Camera camera) {
        camera.updateViewMatrix();
        camera.getProjectionMatrix().toFloatBuffer(projectionMatrixFloatBuffer);
        camera.getViewMatrix().toFloatBuffer(viewMatrixFloatBuffer);
        projectionMatrixFloatBuffer.flip();
        viewMatrixFloatBuffer.flip();
        GL.o1.switchProjection();
        GL.o1.loadMatrix(projectionMatrixFloatBuffer);
        GL.o1.switchModelView();
        GL.o1.loadMatrix(viewMatrixFloatBuffer);
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
        GL.o1.popMatrix();
    }

    @Override
    public void pushMatrix() {
        GL.o1.pushMatrix();
    }

    @Override
    public void translateText(float x, float y) {
        GL.o1.translate(x, y, 0f);
    }

    @Override
    public void drawString(String text, float x, float y) {
        glFontRenderer.drawString(text, x, y, this);
    }

    @Override
    public void drawString(String text, float x, float y, GLFont font) {
        glFontRenderer.drawString(text, x, y, font, this);
    }

    @Override
    public void drawString(String text) {
        glFontRenderer.drawString(text, this);
    }

    @Override
    public void drawString(String text, GLFont font) {
        glFontRenderer.drawString(text, font, this);
    }

    @Override
    public void resetTextRendering() {
        glFontRenderer.reset();
    }

    @Override
    public void setTextColor(float r, float g, float b, float a) {
        glFontRenderer.setColor(r, g, b, a);
    }

    @Override
    public void setTextColor(Color color) {
        glFontRenderer.setColor(color);
    }

    @Override
    public void setTextColor(String colorName) {
        glFontRenderer.setColor(colorName);
    }

    @Override
    public void setTextColorHex(String hexCode) {
        glFontRenderer.setColorHex(hexCode);
    }

    @Override
    public void textNewHalfLine() {
        glFontRenderer.newHalfLine();
    }

    @Override
    public void textNewHalfLine(GLFont font) {
        glFontRenderer.newHalfLine(font);
    }

    @Override
    public void textNewLine() {
        glFontRenderer.newLine();
    }

    @Override
    public void textNewLine(GLFont font) {
        glFontRenderer.newLine(font);
    }

}
