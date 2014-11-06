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
package net.smert.frameworkgl.gui.render;

import net.smert.frameworkgl.opengl.font.AngelCodeFont;
import net.smert.frameworkgl.opengl.renderer.AngelCodeFontRenderer;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class RenderFont implements de.lessvoid.nifty.spi.render.RenderFont {

    private final AngelCodeFont angelCodeFont;
    private final AngelCodeFontRenderer angelCodeFontRenderer;

    public RenderFont(AngelCodeFont angelCodeFont, AngelCodeFontRenderer angelCodeFontRenderer) {
        this.angelCodeFont = angelCodeFont;
        this.angelCodeFontRenderer = angelCodeFontRenderer;
    }

    public AngelCodeFont getAngelCodeFont() {
        return angelCodeFont;
    }

    public AngelCodeFontRenderer getAngelCodeFontRenderer() {
        return angelCodeFontRenderer;
    }

    @Override
    public int getWidth(String text) {
        return angelCodeFont.getWidth(text, 1f);
    }

    @Override
    public int getWidth(String text, float size) {
        return angelCodeFont.getWidth(text, size);
    }

    @Override
    public int getHeight() {
        return angelCodeFont.getFontLineHeight();
    }

    @Override
    public int getCharacterAdvance(char currentCharacter, char nextCharacter, float size) {
        return angelCodeFont.getCharacterAdvance(currentCharacter, nextCharacter, size);
    }

    @Override
    public void dispose() {
        angelCodeFont.destroy();
    }

}
