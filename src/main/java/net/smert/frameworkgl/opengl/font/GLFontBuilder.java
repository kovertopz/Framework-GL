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
package net.smert.frameworkgl.opengl.font;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.Texture;
import net.smert.frameworkgl.opengl.font.GLFont.CodePage;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.utils.HashMapIntGeneric;
import net.smert.frameworkgl.utils.HashMapIntGeneric.Entry;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class GLFontBuilder {

    private boolean antiAliasing;
    private boolean bold;
    private boolean italic;
    private boolean leftToRight;
    private boolean useMipmap;
    private float lodBias;
    private int size;
    private GLFont glFont;
    private final List<GLFont.CodePointRange> glyphsToLoad;
    private String family;

    public GLFontBuilder() {
        glyphsToLoad = new ArrayList<>();
        reset();
    }

    protected GLFont createFontClass(boolean antiAliasing, boolean leftToRight, Font font) {
        return new GLFont(antiAliasing, leftToRight, font);
    }

    public GLFontBuilder addAscii7BitGlyphs() {
        addGlyphs(32, 127);
        return this;
    }

    public GLFontBuilder addAscii8BitGlyphs() {
        addGlyphs(32, 255);
        return this;
    }

    public GLFontBuilder addGlyphs(int startCodePoint, int endCodePoint) {
        glyphsToLoad.add(new GLFont.CodePointRange(startCodePoint, endCodePoint));
        return this;
    }

    public GLFontBuilder addUsAsciiGlyphs() {
        addAscii7BitGlyphs();
        return this;
    }

    public GLFontBuilder buildFont() {

        // Configure font style
        int style = 0;
        style |= (bold) ? Font.BOLD : 0;
        style |= (italic) ? Font.ITALIC : 0;

        // Create fonts
        Font font = new Font(family, style, size);
        glFont = createFontClass(antiAliasing, leftToRight, font);

        // Add and load glyphs
        for (GLFont.CodePointRange codePointRange : glyphsToLoad) {
            glFont.addGlyphs(codePointRange.getStartCodePoint(), codePointRange.getEndCodePoint());
        }
        glFont.loadGlyphs();

        // Get each image from the code pages so we can create textures
        HashMapIntGeneric<GLFont.CodePage> codePages = glFont.getCodePages();
        Iterator<Entry<CodePage>> iterator = codePages.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<CodePage> entry = iterator.next();
            int key = entry.getKey();
            CodePage codePage = entry.getValue();

            // Use texture builder to create texture
            GL.textureBuilder.setLoadFlipVertically(true).load2D(codePage.getImage());
            if (useMipmap) {
                GL.textureBuilder.setLodBias(lodBias).setUseMipmaps(useMipmap).
                        setFilterMagLinear().setFilterMinLinearMipmapLinear();
            } else {
                GL.textureBuilder.setFilterMagLinear().setFilterMinLinear();
            }
            Texture texture = GL.textureBuilder.buildTexture().createTexture(true);

            // Remove texture from the pool if it exists. Add texture
            // to the pool and set filename of the texture in the code page.
            String textureFilename = glFont.getFilename(key);
            Texture existingTexture = Renderable.texturePool.remove(textureFilename);
            if (existingTexture != null) {
                existingTexture.destroy();
            }
            Renderable.texturePool.add(textureFilename, texture);
            codePage.setFontTextureFilename(textureFilename);
        }

        return this;
    }

    public GLFont createFont(boolean reset) {
        GLFont temp = glFont;
        if (reset) {
            reset();
        }
        return temp;
    }

    public final GLFontBuilder reset() {
        antiAliasing = true;
        bold = true;
        italic = false;
        useMipmap = false;
        lodBias = 0f;
        size = 16;
        glFont = null;
        glyphsToLoad.clear();
        family = "Dialog";
        return this;
    }

    public GLFontBuilder setAntiAliasing(boolean antiAliasing) {
        this.antiAliasing = antiAliasing;
        return this;
    }

    public GLFontBuilder setBold(boolean bold) {
        this.bold = bold;
        return this;
    }

    public GLFontBuilder setFamily(String family) {
        this.family = family;
        return this;
    }

    public GLFontBuilder setItalic(boolean italic) {
        this.italic = italic;
        return this;
    }

    public GLFontBuilder setLeftToRight(boolean leftToRight) {
        this.leftToRight = leftToRight;
        return this;
    }

    public GLFontBuilder setLodBias(float lodBias) {
        this.lodBias = lodBias;
        return this;
    }

    public GLFontBuilder setPlain() {
        this.bold = false;
        this.italic = false;
        return this;
    }

    public GLFontBuilder setSize(int size) {
        this.size = size;
        return this;
    }

    public GLFontBuilder setUseMipmap(boolean useMipmap) {
        this.useMipmap = useMipmap;
        return this;
    }

}
