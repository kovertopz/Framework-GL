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
import net.smert.frameworkgl.gameobjects.GameObject;
import net.smert.frameworkgl.math.Transform4f;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.opengl.camera.Camera;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;
import net.smert.frameworkgl.opengl.shader.AbstractShader;
import net.smert.frameworkgl.utils.Color;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class RendererGL3 extends AbstractRendererGL {

    public RendererGL3(GLFontRenderer glFontRenderer) {
        super(glFontRenderer);
    }

    public void destroy() {
    }

    public void init() {
    }

    @Override
    public void color(float r, float g, float b, float a) {
        throw new UnsupportedOperationException("Not supported yet.");
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
    public void switchShader(AbstractShader shader) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void unbindShader() {
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
    public void renderGlyph(AbstractRenderable renderable) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void translateText(float x, float y) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
