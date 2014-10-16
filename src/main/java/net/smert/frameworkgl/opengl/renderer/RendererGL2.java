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
import net.smert.frameworkgl.gameobjects.GameObject;
import net.smert.frameworkgl.math.Transform4f;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.camera.Camera;
import net.smert.frameworkgl.opengl.font.GLFont;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.utils.Color;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class RendererGL2 implements Renderer, TextHelperRenderer, TextRenderer {

    private FloatBuffer viewMatrixFloatBuffer;
    private FloatBuffer transformWorldFloatBuffer;
    private FloatBuffer projectionMatrixFloatBuffer;
    private final GLFontRenderer glFontRenderer;

    public RendererGL2(GLFontRenderer glFontRenderer) {
        this.glFontRenderer = glFontRenderer;
    }

    public void destroy() {
        Renderable.bindState2.reset();
    }

    public void init() {
        viewMatrixFloatBuffer = GL.bufferHelper.createFloatBuffer(16);
        transformWorldFloatBuffer = GL.bufferHelper.createFloatBuffer(16);
        projectionMatrixFloatBuffer = GL.bufferHelper.createFloatBuffer(16);
        Renderable.bindState2.setAttribLocations(GL.defaultAttribLocations);
    }

    @Override
    public void render(AbstractRenderable renderable, float x, float y, float z) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void render(AbstractRenderable renderable, Transform4f transform) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void render(AbstractRenderable renderable, Vector3f position) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void render(GameObject gameObject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void render(List<GameObject> gameObjects) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void renderBlend(GameObject gameObject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void renderBlend(List<GameObject> gameObjects) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void renderOpaque(GameObject gameObject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void renderOpaque(List<GameObject> gameObjects) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void set2DMode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void set2DMode(int width, int height) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setCamera(Camera camera) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void colorText(Color color) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AbstractRenderable createGlyphRenderable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void popMatrix() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void pushMatrix() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void translateText(float x, float y) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void drawString(String text, float x, float y) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void drawString(String text, float x, float y, GLFont font) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void drawString(String text) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void drawString(String text, GLFont font) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void resetTextRendering() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setTextColor(float r, float g, float b, float a) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setTextColor(Color color) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setTextColor(String colorName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setTextColorHex(String hexCode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void textNewHalfLine() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void textNewHalfLine(GLFont font) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void textNewLine() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void textNewLine(GLFont font) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
