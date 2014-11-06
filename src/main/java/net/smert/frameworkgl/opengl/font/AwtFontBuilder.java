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
import net.smert.frameworkgl.opengl.font.AwtFont.CodePage;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.utils.HashMapIntGeneric;
import net.smert.frameworkgl.utils.HashMapIntGeneric.Entry;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class AwtFontBuilder {

    private boolean antiAliasing;
    private boolean bold;
    private boolean italic;
    private boolean leftToRight;
    private boolean useMipmap;
    private float lodBias;
    private int size;
    private AwtFont awtFont;
    private final List<AwtFont.CodePointRange> glyphsToLoad;
    private String family;

    public AwtFontBuilder() {
        glyphsToLoad = new ArrayList<>();
        reset();
    }

    protected AwtFont createFontClass(boolean antiAliasing, boolean leftToRight, Font font) {
        return new AwtFont(antiAliasing, leftToRight, font);
    }

    public AwtFontBuilder addAscii7BitGlyphs() {
        addGlyphs(32, 127);
        return this;
    }

    public AwtFontBuilder addAscii8BitGlyphs() {
        addGlyphs(32, 255);
        return this;
    }

    public AwtFontBuilder addGlyphs(int startCodePoint, int endCodePoint) {
        glyphsToLoad.add(new AwtFont.CodePointRange(startCodePoint, endCodePoint));
        return this;
    }

    public AwtFontBuilder addUsAsciiGlyphs() {
        addAscii7BitGlyphs();
        return this;
    }

    public AwtFontBuilder buildFont() {

        // Configure font style
        int style = 0;
        style |= (bold) ? Font.BOLD : 0;
        style |= (italic) ? Font.ITALIC : 0;

        // Create fonts
        Font font = new Font(family, style, size);
        awtFont = createFontClass(antiAliasing, leftToRight, font);

        // Add and load glyphs
        for (AwtFont.CodePointRange codePointRange : glyphsToLoad) {
            awtFont.addGlyphs(codePointRange.getStartCodePoint(), codePointRange.getEndCodePoint());
        }
        awtFont.loadGlyphs();

        // Get each image from the code pages so we can create textures
        HashMapIntGeneric<AwtFont.CodePage> codePageIndexToCodePage = awtFont.getCodePages();
        Iterator<Entry<CodePage>> iterator = codePageIndexToCodePage.entrySet().iterator();
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
            String textureFilename = awtFont.getFilename(key);
            Texture existingTexture = Renderable.texturePool.remove(textureFilename);
            if (existingTexture != null) {
                existingTexture.destroy();
            }
            Renderable.texturePool.add(textureFilename, texture);
            codePage.setFontTextureFilename(textureFilename);
        }

        return this;
    }

    public AwtFont createFont(boolean reset) {
        AwtFont temp = awtFont;
        if (reset) {
            reset();
        }
        return temp;
    }

    public final AwtFontBuilder reset() {
        antiAliasing = true;
        bold = true;
        italic = false;
        useMipmap = false;
        lodBias = 0f;
        size = 16;
        awtFont = null;
        glyphsToLoad.clear();
        family = "Dialog";
        return this;
    }

    public AwtFontBuilder setAntiAliasing(boolean antiAliasing) {
        this.antiAliasing = antiAliasing;
        return this;
    }

    public AwtFontBuilder setBold(boolean bold) {
        this.bold = bold;
        return this;
    }

    public AwtFontBuilder setFamily(String family) {
        this.family = family;
        return this;
    }

    public AwtFontBuilder setItalic(boolean italic) {
        this.italic = italic;
        return this;
    }

    public AwtFontBuilder setLeftToRight(boolean leftToRight) {
        this.leftToRight = leftToRight;
        return this;
    }

    public AwtFontBuilder setLodBias(float lodBias) {
        this.lodBias = lodBias;
        return this;
    }

    public AwtFontBuilder setPlain() {
        this.bold = false;
        this.italic = false;
        return this;
    }

    public AwtFontBuilder setSize(int size) {
        this.size = size;
        return this;
    }

    public AwtFontBuilder setUseMipmap(boolean useMipmap) {
        this.useMipmap = useMipmap;
        return this;
    }

}
