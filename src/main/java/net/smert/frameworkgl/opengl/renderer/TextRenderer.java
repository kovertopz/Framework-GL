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

import net.smert.frameworkgl.utils.Color;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public interface TextRenderer {

    public void drawString(String text);

    public void drawString(String text, float x, float y);

    public void drawString(String text, float x, float y, FontRenderer fontRenderer);

    public void drawString(String text, float x, float y, float sizeX, float sizeY);

    public void drawString(String text, float x, float y, float sizeX, float sizeY, FontRenderer fontRenderer);

    public void drawString(String text, FontRenderer fontRenderer);

    public Color getTextColor();

    public void resetTextRendering();

    public void setDefaultFontRenderer(FontRenderer defaultFontRenderer);

    public void setTextColor(float r, float g, float b, float a);

    public void setTextColor(Color color);

    public void setTextColor(String colorName);

    public void setTextColorHex(String hexCode);

    public void textNewHalfLine();

    public void textNewHalfLine(FontRenderer fontRenderer);

    public void textNewLine();

    public void textNewLine(FontRenderer fontRenderer);

}
