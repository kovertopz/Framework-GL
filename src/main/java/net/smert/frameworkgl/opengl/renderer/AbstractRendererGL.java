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

import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.math.Vector2f;
import net.smert.frameworkgl.utils.Color;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public abstract class AbstractRendererGL implements Renderer, TextHelperRenderer, TextRenderer {

    protected int textDefaultX;
    protected int textDefaultY;
    protected final Color textColor;
    protected FontRenderer defaultFontRenderer;
    protected final Vector2f textPosition;

    public AbstractRendererGL() {
        textColor = new Color();
        textPosition = new Vector2f();
    }

    @Override
    public void drawString(String text) {
        defaultFontRenderer.drawString(text, this);
    }

    @Override
    public void drawString(String text, float x, float y) {
        defaultFontRenderer.drawString(text, x, y, this);
    }

    @Override
    public void drawString(String text, float x, float y, FontRenderer fontRenderer) {
        fontRenderer.drawString(text, x, y, this);
    }

    @Override
    public void drawString(String text, float x, float y, float sizeX, float sizeY) {
        defaultFontRenderer.drawString(text, x, y, sizeX, sizeY, this);
    }

    @Override
    public void drawString(String text, float x, float y, float sizeX, float sizeY, FontRenderer fontRenderer) {
        fontRenderer.drawString(text, x, y, sizeX, sizeY, this);
    }

    @Override
    public void drawString(String text, FontRenderer fontRenderer) {
        fontRenderer.drawString(text, this);
    }

    @Override
    public Color getTextColor() {
        return textColor;
    }

    @Override
    public void resetTextRendering() {
        textPosition.setX(textDefaultX);
        textPosition.setY(Fw.config.getCurrentHeight() - textDefaultY);
    }

    @Override
    public void setDefaultFontRenderer(FontRenderer defaultFontRenderer) {
        this.defaultFontRenderer = defaultFontRenderer;
    }

    @Override
    public void setTextColor(float r, float g, float b, float a) {
        textColor.set(r, g, b, a);
    }

    @Override
    public void setTextColor(Color color) {
        textColor.set(color);
    }

    @Override
    public void setTextColor(String colorName) {
        textColor.set(colorName);
    }

    @Override
    public void setTextColorHex(String hexCode) {
        textColor.setHex(hexCode);
    }

    @Override
    public void textNewHalfLine() {
        defaultFontRenderer.newHalfLine(this, textPosition);
    }

    @Override
    public void textNewHalfLine(FontRenderer fontRenderer) {
        fontRenderer.newHalfLine(this, textPosition);
    }

    @Override
    public void textNewLine() {
        defaultFontRenderer.newLine(this, textPosition);
    }

    @Override
    public void textNewLine(FontRenderer fontRenderer) {
        fontRenderer.newLine(this, textPosition);
    }

}
