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

import net.smert.frameworkgl.opengl.font.GLFont;
import net.smert.frameworkgl.utils.Color;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public abstract class AbstractRendererGL implements Renderer, TextHelperRenderer, TextRenderer {

    protected final GLFontRenderer glFontRenderer;

    public AbstractRendererGL(GLFontRenderer glFontRenderer) {
        this.glFontRenderer = glFontRenderer;
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
