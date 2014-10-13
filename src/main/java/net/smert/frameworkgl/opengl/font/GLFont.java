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

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;
import net.smert.frameworkgl.utils.HashMapIntGeneric;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class GLFont {

    private final Builder builder;
    private Glyph missingGlyph;
    private final HashMapIntGeneric<CodePage> codePages;
    private final List<CodePointRange> glyphsToLoad;

    public GLFont(Font font) {
        this(true, true, font);
    }

    public GLFont(boolean leftToRight, Font font) {
        this(true, leftToRight, font);
    }

    public GLFont(boolean antiAliasing, boolean leftToRight, Font font) {
        builder = new Builder(antiAliasing, leftToRight, font);
        builder.createDefault();
        codePages = new HashMapIntGeneric<>();
        glyphsToLoad = new ArrayList<>();
    }

    public void addAscii7BitGlyphs() {
        addGlyphs(32, 127);
    }

    public void addAscii8BitGlyphs() {
        addGlyphs(32, 255);
    }

    public void addGlyphs(int startCodePoint, int endCodePoint) {
        glyphsToLoad.add(new CodePointRange(startCodePoint, endCodePoint));
    }

    public void addUsAsciiGlyphs() {
        addAscii7BitGlyphs();
    }

    public void destroy() {
        Iterator<CodePage> iterator = codePages.values().iterator();
        while (iterator.hasNext()) {
            CodePage codePage = iterator.next();
            codePage.destroy();
        }
        codePages.clear();
        glyphsToLoad.clear();
    }

    public int getFontHeight() {
        return builder.getFontHeight();
    }

    public int getFontPaddingX() {
        return builder.getFontPaddingX();
    }

    public void setFontPaddingX(int fontPaddingX) {
        builder.setFontPaddingX(fontPaddingX);
    }

    public int getFontPaddingY() {
        return builder.getFontPaddingY();
    }

    public void setFontPaddingY(int fontPaddingY) {
        builder.setFontPaddingY(fontPaddingY);
    }

    public int getFontSpacing() {
        return builder.getFontSpacing();
    }

    public void setFontSpacing(int fontSpacing) {
        builder.setFontSpacing(fontSpacing);
    }

    public int getFontWidth() {
        return builder.getFontHeight();
    }

    public Glyph getGlyph(int codePoint) {

        // Get the code page for the code point
        int codePageIndex = CodePage.GetPageIndex(codePoint);
        CodePage codePage = codePages.get(codePageIndex);
        if (codePage == null) {
            return missingGlyph;
        }

        // Get the glyph for the code point
        int glyphIndex = Glyph.GetGlyphIndex(codePoint);
        Glyph glyph = codePage.getGlyph(glyphIndex);
        if (glyph == null) {
            return missingGlyph;
        }

        return glyph;
    }

    public int getImageDimensions() {
        return builder.getImageDimensions();
    }

    public void setImageDimensions(int imageDimensions) {
        builder.setImageDimensions(imageDimensions);
    }

    public HashMapIntGeneric<CodePage> getCodePages() {
        return codePages;
    }

    public Glyph getMissingGlyph() {
        return missingGlyph;
    }

    public void setMissingGlyph(Glyph missingGlyph) {
        this.missingGlyph = missingGlyph;
    }

    public String getFilename(int codePoint) {
        int codePageIndex = CodePage.GetPageIndex(codePoint);
        return "__Internal_GLFont_" + builder.hashCode() + "_CPI_" + codePageIndex;
    }

    public void loadGlyphs() {
        for (CodePointRange codePointRange : glyphsToLoad) {
            int startCodePoint = codePointRange.getStartCodePoint();
            int endCodePoint = codePointRange.getEndCodePoint();

            for (int codePoint = startCodePoint; codePoint <= endCodePoint; codePoint++) {
                char[] chars = Character.toChars(codePoint);
                String text = new String(chars);
                GlyphVector vector = builder.createGlyphVector(chars);

                for (int i = 0; i < vector.getNumGlyphs(); i++) {
                    int fontCodePoint = text.codePointAt(vector.getGlyphCharIndex(i));

                    // Make sure this is legit
                    if (!Character.isValidCodePoint(fontCodePoint) || !Character.isDefined(fontCodePoint)) {
                        continue;
                    }

                    // Get the pixel bounds for the current character
                    Rectangle bounds = vector.getGlyphPixelBounds(i, builder.getDefaultFontRenderContext(), 0, 0);

                    // Do not define a glyph for zero length characters
                    double charWidth = bounds.getWidth();
                    if (charWidth == 0) {
                        continue;
                    }

                    // Create a code page if it does not exist
                    int codePageIndex = CodePage.GetPageIndex(fontCodePoint);
                    CodePage codePage = codePages.get(codePageIndex);
                    if (codePage == null) {
                        codePage = new CodePage(builder);
                        codePages.put(codePageIndex, codePage);
                    }

                    // Create a glphy if it does not exist
                    int glyphIndex = Glyph.GetGlyphIndex(fontCodePoint);
                    Glyph glyph = codePage.getGlyph(glyphIndex);
                    if (glyph == null) {
                        glyph = new Glyph();
                        codePage.setGlyph(fontCodePoint, glyph);
                    }

                    // Set glyph values
                    glyph.codePage = codePage;
                    glyph.codePoint = fontCodePoint;
                    glyph.h = builder.getFontHeight();
                    glyph.w = builder.getCharWidth(fontCodePoint);

                    // Ensure the min height and width are at least 1
                    if (glyph.h <= 0) {
                        glyph.h = 1;
                    }
                    if (glyph.w <= 0) {
                        glyph.w = 1;
                    }

                    // Draw the glyph inside the code page image
                    codePage.drawGlyph(builder, glyph);
                }
            }
        }
        glyphsToLoad.clear();
    }

    public void showBufferedImagesFromCodePages() {
        Iterator<CodePage> iterator = codePages.values().iterator();
        while (iterator.hasNext()) {
            CodePage codePage = iterator.next();
            JFrame frame = new JFrame();
            JScrollPane scrollPane = new JScrollPane(new JLabel(new ImageIcon(codePage.image)));
            frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
        }
    }

    public static class Builder {

        private final boolean antiAliasing;
        private int fontAscent;
        private int fontDescent;
        private final int fontDirection;
        private int fontHeight;
        private int fontLeading;
        private int fontPaddingX;
        private int fontPaddingY;
        private int fontSpacing;
        private int imageDimensions;
        private BufferedImage defaultImage;
        private Font font;
        private FontMetrics defaultFontMetrics;
        private FontRenderContext defaultFontRenderContext;
        private Graphics2D defaultGraphics;

        public Builder(boolean antiAliasing, boolean leftToRight, Font font) {
            this.antiAliasing = antiAliasing;

            // Font direction
            if (leftToRight) {
                fontDirection = Font.LAYOUT_LEFT_TO_RIGHT;
            } else {
                fontDirection = Font.LAYOUT_RIGHT_TO_LEFT;
            }

            Map attributes = font.getAttributes();
            attributes.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
            this.font = font.deriveFont(attributes);
        }

        public void createDefault() {
            defaultImage = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
            defaultGraphics = createGraphics(defaultImage);
            defaultFontMetrics = defaultGraphics.getFontMetrics();
            defaultFontRenderContext = defaultGraphics.getFontRenderContext();
            fontAscent = defaultFontMetrics.getAscent();
            fontDescent = defaultFontMetrics.getDescent();
            fontHeight = defaultFontMetrics.getHeight();
            fontLeading = defaultFontMetrics.getLeading();
            fontPaddingX = 5;
            fontPaddingY = 2;
            fontSpacing = defaultFontMetrics.charWidth(' ');
            imageDimensions = 64 * ((font.getSize() / 5) + 1) + 32;
        }

        public BufferedImage createImage() {
            return new BufferedImage(imageDimensions, imageDimensions, BufferedImage.TYPE_INT_ARGB);
        }

        public GlyphVector createGlyphVector(char[] chars) {
            return font.layoutGlyphVector(defaultFontRenderContext, chars, 0, chars.length, fontDirection);
        }

        public Graphics2D createGraphics(BufferedImage image) {
            Graphics2D graphics = image.createGraphics();
            graphics.setComposite(AlphaComposite.SrcOver);
            graphics.setFont(font);
            if (antiAliasing) {
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            }
            return graphics;
        }

        public void destroy() {
            defaultImage = null;
            font = null;
            defaultFontMetrics = null;
            defaultFontRenderContext = null;
            if (defaultGraphics != null) {
                defaultGraphics.dispose();
                defaultGraphics = null;
            }
        }

        public int getCharWidth(int codePoint) {
            return defaultFontMetrics.charWidth(codePoint);
        }

        public int getFontAscent() {
            return fontAscent;
        }

        public int getFontDescent() {
            return fontDescent;
        }

        public int getFontHeight() {
            return fontHeight;
        }

        public int getFontLeading() {
            return fontLeading;
        }

        public int getFontPaddingX() {
            return fontPaddingX;
        }

        public void setFontPaddingX(int fontPaddingX) {
            this.fontPaddingX = fontPaddingX;
        }

        public int getFontPaddingY() {
            return fontPaddingY;
        }

        public void setFontPaddingY(int fontPaddingY) {
            this.fontPaddingY = fontPaddingY;
        }

        public int getFontSpacing() {
            return fontSpacing;
        }

        public void setFontSpacing(int fontSpacing) {
            this.fontSpacing = fontSpacing;
        }

        public int getImageDimensions() {
            return imageDimensions;
        }

        public void setImageDimensions(int imageDimensions) {
            this.imageDimensions = imageDimensions;
        }

        public FontRenderContext getDefaultFontRenderContext() {
            return defaultFontRenderContext;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 71 * hash + (this.antiAliasing ? 1 : 0);
            hash = 71 * hash + this.fontAscent;
            hash = 71 * hash + this.fontDescent;
            hash = 71 * hash + this.fontDirection;
            hash = 71 * hash + this.fontHeight;
            hash = 71 * hash + this.fontLeading;
            hash = 71 * hash + this.fontPaddingX;
            hash = 71 * hash + this.fontPaddingY;
            hash = 71 * hash + this.imageDimensions;
            hash = 71 * hash + Objects.hashCode(this.font);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Builder other = (Builder) obj;
            if (this.antiAliasing != other.antiAliasing) {
                return false;
            }
            if (this.fontAscent != other.fontAscent) {
                return false;
            }
            if (this.fontDescent != other.fontDescent) {
                return false;
            }
            if (this.fontDirection != other.fontDirection) {
                return false;
            }
            if (this.fontHeight != other.fontHeight) {
                return false;
            }
            if (this.fontLeading != other.fontLeading) {
                return false;
            }
            if (this.fontPaddingX != other.fontPaddingX) {
                return false;
            }
            if (this.fontPaddingY != other.fontPaddingY) {
                return false;
            }
            if (this.imageDimensions != other.imageDimensions) {
                return false;
            }
            return Objects.equals(this.font, other.font);
        }

    }

    public static class CodePage {

        public final static int GLYPHS_PER_PAGE = 256;
        public final static int MAX_PAGES = Character.MAX_CODE_POINT / GLYPHS_PER_PAGE;

        private int imageX;
        private int imageY;
        private BufferedImage image;
        private Graphics2D graphics;
        private final HashMapIntGeneric<Glyph> glyphs;
        private String fontTextureFilename;

        public CodePage(Builder builder) {
            imageX = 0;
            imageY = 0;
            image = builder.createImage();
            graphics = builder.createGraphics(image);
            glyphs = new HashMapIntGeneric<>();
        }

        public void destroy() {
            image = null;
            if (graphics != null) {
                graphics.dispose();
                graphics = null;
            }
            destroyGlyphsAndTexture();
        }

        public void destroyGlyphsAndTexture() {
            Iterator<Glyph> iterator = glyphs.values().iterator();
            while (iterator.hasNext()) {
                Glyph glyph = iterator.next();
                glyph.destroy();
            }
            glyphs.clear();
            fontTextureFilename = null;
        }

        public void drawGlyph(Builder builder, Glyph glyph) {

            // Create new image and graphics from glyph
            BufferedImage glyphImage = new BufferedImage(glyph.w, glyph.h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D glyphGraphics = builder.createGraphics(glyphImage);

            // Render the glyph
            String charString = new String(Character.toChars(glyph.codePoint));
            glyphGraphics.setColor(Color.WHITE);
            glyphGraphics.drawString(charString, 0, glyph.h - builder.getFontDescent());
            glyphGraphics.dispose();

            // Form a new row if the current image will be past the bounds in the X direction
            if (imageX + glyphImage.getWidth() + builder.getFontPaddingX() >= image.getWidth()) {
                imageX = 0;
                imageY += glyph.h + builder.getFontPaddingY();
            }

            // Set glyph to the current X, Y
            glyph.x = imageX;
            glyph.y = imageY;

            // Draw the glyph image in the code page image
            graphics.drawImage(glyphImage, imageX, imageY, null);

            // Advance to the next position
            imageX += glyph.w + builder.getFontPaddingX();
        }

        public int getImageX() {
            return imageX;
        }

        public int getImageY() {
            return imageY;
        }

        public BufferedImage getImage() {
            return image;
        }

        public Glyph getGlyph(int index) {
            return glyphs.get(index);
        }

        public void setGlyph(int index, Glyph glyph) {
            glyphs.put(index, glyph);
        }

        public String getFontTextureFilename() {
            return fontTextureFilename;
        }

        public void setFontTextureFilename(String fontTextureFilename) {
            this.fontTextureFilename = fontTextureFilename;
        }

        public void resetImage() {

            // Clear image
            graphics.setComposite(AlphaComposite.Clear);
            graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
            graphics.setComposite(AlphaComposite.SrcOver);

            // Set image X, Y back to origin
            imageX = 0;
            imageY = 0;

            // Destroy any existing glyphs and textures since they won't match anymore
            destroyGlyphsAndTexture();
        }

        public static int GetPageIndex(int codePoint) {
            return codePoint / GLYPHS_PER_PAGE;
        }

    }

    public static class CodePointRange {

        private final int endCodePoint;
        private final int startCodePoint;

        public CodePointRange(int startCodePoint, int endCodePoint) {
            this.endCodePoint = endCodePoint;
            this.startCodePoint = startCodePoint;
        }

        public int getEndCodePoint() {
            return endCodePoint;
        }

        public int getStartCodePoint() {
            return startCodePoint;
        }

    }

    public static class Glyph {

        public int codePoint;
        public int x, y, w, h;
        public AbstractRenderable renderable;
        public CodePage codePage;

        public void destroy() {
            if (renderable != null) {
                renderable.destroy();
                renderable = null;
            }
        }

        public static int GetGlyphIndex(int codePoint) {
            return codePoint % CodePage.GLYPHS_PER_PAGE;
        }

    }

}
