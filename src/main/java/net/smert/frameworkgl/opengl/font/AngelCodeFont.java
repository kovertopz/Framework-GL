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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.StringTokenizer;
import net.smert.frameworkgl.Files;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.opengl.Texture;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.utils.HashMapIntGeneric;
import net.smert.frameworkgl.utils.HashMapIntInt;
import net.smert.frameworkgl.utils.HashMapIntString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://www.angelcode.com/products/bmfont/doc/file_format.html
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class AngelCodeFont implements GLFont {

    private final static Logger log = LoggerFactory.getLogger(AngelCodeFont.class);

    private boolean fontBold; // The font is bold
    private boolean fontItalic; // The font is italic
    // Set to 1 if the monochrome characters have been packed into each of the texture
    // channels. In this case alphaChnl describes what is stored in each channel.
    private boolean fontPacked;
    private boolean fontSmooth; // Set to 1 if smoothing was turned on
    private boolean fontUnicode; // Set to 1 if it is the unicode charset
    // Set to 0 if the channel holds the glyph data, 1 if it holds the outline, 2 if it
    // holds the glyph and the outline, 3 if its set to zero, and 4 if its set to one.
    private int fontAlphaChannel;
    private int fontAntiAliasing; // The supersampling level used. 1 means no supersampling was used.
    private int fontBase; // The number of pixels from the absolute top of the line to the base of the characters
    // Set to 0 if the channel holds the glyph data, 1 if it holds the outline, 2 if it
    // holds the glyph and the outline, 3 if its set to zero, and 4 if its set to one.
    private int fontBlueChannel;
    // Set to 0 if the channel holds the glyph data, 1 if it holds the outline, 2 if it
    // holds the glyph and the outline, 3 if its set to zero, and 4 if its set to one.
    private int fontGreenChannel;
    private int fontLineHeight; // This is the distance in pixels between each line of text
    private int fontOutline; // The outline thickness for the characters
    private int fontPaddingDown; // The padding for each character (up, right, down, left)
    private int fontPaddingLeft;
    private int fontPaddingRight;
    private int fontPaddingUp;
    private int fontPages; // The number of texture pages included in the font
    // Set to 0 if the channel holds the glyph data, 1 if it holds the outline, 2 if it
    // holds the glyph and the outline, 3 if its set to zero, and 4 if its set to one.
    private int fontRedChannel;
    private int fontScaleHeight; // The height of the texture, normally used to scale the y pos of the character image
    private int fontScaleWidth; // The width of the texture, normally used to scale the x pos of the character image
    private int fontSize; // The size of the true type font
    private int fontSpacingHorizontal; // The spacing for each character (horizontal, vertical)
    private int fontSpacingVertical;
    private int fontStretchHeight; // The font height stretch in percentage. 100% means no stretch.
    private int totalChars;
    private int totalKernings;
    private Glyph missingGlyph;
    private final HashMapIntString pages; // The page id. The texture file name.
    private final HashMapIntGeneric<Character> characters;
    private final HashMapIntGeneric<Glyph> glyphs;
    private String fontCharset; // The name of the OEM charset used (when not unicode)
    private String fontFace; // This is the name of the true type font

    public AngelCodeFont() {
        pages = new HashMapIntString();
        characters = new HashMapIntGeneric<>();
        glyphs = new HashMapIntGeneric<>();
    }

    private void addChar(StringTokenizer tokenizer) {
        Character character = new Character();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            String[] keyValue = token.split("=");
            if (keyValue.length != 2) {
                log.warn("Skipping invalid key value for char line: {}", token);
                continue;
            }

            // Parse key and value
            String key = keyValue[0];
            String value = keyValue[1];
            switch (key) {
                case "chnl":
                    character.setChannel(Integer.parseInt(value));
                    break;
                case "height":
                    character.setHeight(Integer.parseInt(value));
                    break;
                case "id":
                    character.setId(Integer.parseInt(value));
                    break;
                case "page":
                    character.setPage(Integer.parseInt(value));
                    break;
                case "width":
                    character.setWidth(Integer.parseInt(value));
                    break;
                case "x":
                    character.setX(Integer.parseInt(value));
                    break;
                case "xadvance":
                    character.setXAdvance(Integer.parseInt(value));
                    break;
                case "xoffset":
                    character.setXOffset(Integer.parseInt(value));
                    break;
                case "y":
                    character.setY(Integer.parseInt(value));
                    break;
                case "yoffset":
                    character.setYOffset(Integer.parseInt(value));
                    break;

                default:
                    log.warn("Skipping unknown key value for char line: {}", token);
            }
        }
        characters.put(character.getId(), character);
    }

    private void addChars(StringTokenizer tokenizer) {
        String countToken = tokenizer.nextToken();
        String[] countKeyValue = countToken.split("=");
        if (countKeyValue.length != 2) {
            log.warn("Skipping invalid key value for count token during chars line: {}", countToken);
            return;
        }
        if (!countKeyValue[0].equals("count")) {
            log.warn("Skipping invalid key for count token during chars line: {}", countToken);
        }
        totalChars = Integer.parseInt(countKeyValue[1]);
    }

    private void addCommon(StringTokenizer tokenizer) {
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            String[] keyValue = token.split("=");
            if (keyValue.length != 2) {
                log.warn("Skipping invalid key value for common line: {}", token);
                continue;
            }

            // Parse key and value
            String key = keyValue[0];
            String value = keyValue[1];
            switch (key) {
                case "alphaChnl":
                    fontAlphaChannel = Integer.parseInt(value);
                    break;
                case "base":
                    fontBase = Integer.parseInt(value);
                    break;
                case "blueChnl":
                    fontBlueChannel = Integer.parseInt(value);
                    break;
                case "greenChnl":
                    fontGreenChannel = Integer.parseInt(value);
                    break;
                case "lineHeight":
                    fontLineHeight = Integer.parseInt(value);
                    break;
                case "packed":
                    fontPacked = (Integer.parseInt(value) == 1);
                    break;
                case "pages":
                    fontPages = Integer.parseInt(value);
                    break;
                case "redChnl":
                    fontRedChannel = Integer.parseInt(value);
                    break;
                case "scaleH":
                    fontScaleHeight = Integer.parseInt(value);
                    break;
                case "scaleW":
                    fontScaleWidth = Integer.parseInt(value);
                    break;

                default:
                    log.warn("Skipping unknown key value for common line: {}", token);
            }
        }
    }

    private void addInfo(StringTokenizer tokenizer) {
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            String[] keyValue = token.split("=");
            if (keyValue.length != 2) {
                log.warn("Skipping invalid key value for info line: {}", token);
                continue;
            }

            // Parse key and value
            String key = keyValue[0];
            String value = keyValue[1];
            switch (key) {
                case "aa":
                    fontAntiAliasing = Integer.parseInt(value);
                    break;
                case "bold":
                    fontBold = (Integer.parseInt(value) == 1);
                    break;
                case "charset":
                    fontCharset = value;
                    break;
                case "face":
                    fontFace = value;
                    break;
                case "italic":
                    fontItalic = (Integer.parseInt(value) == 1);
                    break;
                case "outline":
                    fontOutline = Integer.parseInt(value);
                    break;
                case "padding":
                    String[] padding = value.split(",");
                    if (padding.length != 4) {
                        log.warn("Invalid padding defined: {}", token);
                        continue;
                    }
                    fontPaddingUp = Integer.parseInt(padding[0]);
                    fontPaddingRight = Integer.parseInt(padding[1]);
                    fontPaddingDown = Integer.parseInt(padding[2]);
                    fontPaddingLeft = Integer.parseInt(padding[3]);
                    break;
                case "size":
                    fontSize = Integer.parseInt(value);
                    break;
                case "smooth":
                    fontSmooth = (Integer.parseInt(value) == 1);
                    break;
                case "spacing":
                    String[] spacing = value.split(",");
                    if (spacing.length != 2) {
                        log.warn("Invalid spacing defined: {}", token);
                        continue;
                    }
                    fontSpacingHorizontal = Integer.parseInt(spacing[0]);
                    fontSpacingVertical = Integer.parseInt(spacing[1]);
                    break;
                case "stretchH":
                    fontStretchHeight = Integer.parseInt(value);
                    break;
                case "unicode":
                    fontUnicode = (Integer.parseInt(value) == 1);
                    break;

                default:
                    log.warn("Skipping unknown key value for info line: {}", token);
            }
        }
    }

    private void addKerning(StringTokenizer tokenizer) {
        String firstToken = tokenizer.nextToken();
        String[] firstKeyValue = firstToken.split("=");
        if (firstKeyValue.length != 2) {
            throw new RuntimeException("Invalid key value for first token during kerning line: " + firstToken);
        }
        if (!firstKeyValue[0].equals("first")) {
            throw new RuntimeException("Invalid key for first token during kerning line: " + firstToken);
        }

        String secondToken = tokenizer.nextToken();
        String[] secondKeyValue = secondToken.split("=");
        if (secondKeyValue.length != 2) {
            throw new RuntimeException("Invalid key value for second token during kerning line: " + secondToken);
        }
        if (!secondKeyValue[0].equals("second")) {
            throw new RuntimeException("Invalid key for second token during kerning line: " + firstToken);
        }

        String amountToken = tokenizer.nextToken();
        String[] amountKeyValue = amountToken.split("=");
        if (amountKeyValue.length != 2) {
            throw new RuntimeException("Invalid key value for amount token during kerning line: " + amountToken);
        }
        if (!amountKeyValue[0].equals("amount")) {
            throw new RuntimeException("Invalid key for amount token during kerning line: " + firstToken);
        }

        int first = Integer.parseInt(firstKeyValue[1]);
        int second = Integer.parseInt(secondKeyValue[1]);
        int amount = Integer.parseInt(amountKeyValue[1]);

        Character character = characters.get(first);
        if (character == null) {
            character = new Character();
            character.setId(first);
            characters.put(first, character);
        }
        character.setKerning(second, amount);
    }

    private void addKernings(StringTokenizer tokenizer) {
        String countToken = tokenizer.nextToken();
        String[] countKeyValue = countToken.split("=");
        if (countKeyValue.length != 2) {
            log.warn("Skipping invalid key value for count token during kernings line: {}", countToken);
            return;
        }
        if (!countKeyValue[0].equals("count")) {
            log.warn("Skipping invalid key for count token during kernings line: {}", countToken);
        }
        totalKernings = Integer.parseInt(countKeyValue[1]);
    }

    private void addPage(StringTokenizer tokenizer) {
        String idToken = tokenizer.nextToken();
        String[] idKeyValue = idToken.split("=");
        if (idKeyValue.length != 2) {
            throw new RuntimeException("Invalid key value for ID token during page line: " + idToken);
        }
        if (!idKeyValue[0].equals("id")) {
            throw new RuntimeException("Invalid key for ID token during page line: " + idToken);
        }

        String fileToken = tokenizer.nextToken();
        String[] fileKeyValue = fileToken.split("=");
        if (fileKeyValue.length != 2) {
            throw new RuntimeException("Invalid key value for file token during page line: " + fileToken);
        }
        if (!fileKeyValue[0].equals("file")) {
            throw new RuntimeException("Invalid key for file token during page line: " + idToken);
        }

        int id = Integer.parseInt(idKeyValue[1]);
        String filename = fileKeyValue[1];

        // Remove leading double quotes
        while (filename.startsWith("\"")) {
            filename = filename.substring(1);
        }

        // Remove trailing double quotes
        while (filename.endsWith("\"")) {
            filename = filename.substring(0, filename.length() - 1);
        }

        pages.put(id, filename);
    }

    private void parse(String line) {
        StringTokenizer tokenizer = new StringTokenizer(line);

        if (!tokenizer.hasMoreTokens()) {
            return;
        }

        int totalTokens = tokenizer.countTokens();
        String token = tokenizer.nextToken();

        switch (token) {
            case "char":
                if (totalTokens >= 7) {
                    addChar(tokenizer);
                } else {
                    log.warn("Invalid char definition: {}", line);
                }
                break;

            case "chars":
                if (totalTokens == 2) {
                    addChars(tokenizer);
                } else {
                    log.warn("Invalid chars definition: {}", line);
                }
                break;

            case "common":
                if (totalTokens > 2) {
                    addCommon(tokenizer);
                } else {
                    log.warn("Invalid common definition: {}", line);
                }
                break;

            case "info":
                if (totalTokens > 2) {
                    addInfo(tokenizer);
                } else {
                    log.warn("Invalid info definition: {}", line);
                }
                break;

            case "kerning":
                if (totalTokens == 4) {
                    addKerning(tokenizer);
                } else {
                    log.warn("Invalid kerning definition: {}", line);
                }
                break;

            case "kernings":
                if (totalTokens == 2) {
                    addKernings(tokenizer);
                } else {
                    log.warn("Invalid kernings definition: {}", line);
                }
                break;

            case "page":
                if (totalTokens == 3) {
                    addPage(tokenizer);
                } else {
                    log.warn("Invalid page definition: {}", line);
                }
                break;

            default:
                log.warn("Skipped line with an unsupported token: {}", line);
        }
    }

    private void read(String filename) throws IOException {
        Files.FileAsset fileAsset = Fw.files.getFont(filename);

        try (InputStream is = fileAsset.openStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(isr)) {
            String line;

            while ((line = reader.readLine()) != null) {
                parse(line);
            }
        }
    }

    private void reset() {
        fontBold = false;
        fontItalic = false;
        fontPacked = false;
        fontSmooth = false;
        fontUnicode = false;
        fontAlphaChannel = -1;
        fontAntiAliasing = 0;
        fontBase = 0;
        fontBlueChannel = -1;
        fontGreenChannel = -1;
        fontLineHeight = 0;
        fontOutline = 0;
        fontPaddingDown = 0;
        fontPaddingLeft = 0;
        fontPaddingRight = 0;
        fontPaddingUp = 0;
        fontPages = 0;
        fontRedChannel = -1;
        fontScaleHeight = 0;
        fontScaleWidth = 0;
        fontSize = 0;
        fontSpacingHorizontal = 0;
        fontSpacingVertical = 0;
        fontStretchHeight = 0;
        totalChars = 0;
        totalKernings = 0;
        pages.clear();
        characters.clear();
        glyphs.clear();
        fontCharset = "";
        fontFace = "";
    }

    public void destroy() {
        characters.clear();
        Iterator<Glyph> iteratorGlyph = glyphs.values().iterator();
        while (iteratorGlyph.hasNext()) {
            Glyph glyph = iteratorGlyph.next();
            glyph.renderable.destroy();
        }
        glyphs.clear();
        Iterator<HashMapIntString.Entry> iteratorPage = pages.entrySet().iterator();
        while (iteratorPage.hasNext()) {
            HashMapIntString.Entry entry = iteratorPage.next();
            String filename = entry.getValue();
            Texture texture = Renderable.texturePool.remove(filename);
            if (texture != null) {
                texture.destroy();
            }
        }
        pages.clear();
    }

    public int getFontAlphaChannel() {
        return fontAlphaChannel;
    }

    public int getFontAntiAliasing() {
        return fontAntiAliasing;
    }

    public int getFontBase() {
        return fontBase;
    }

    public int getFontBlueChannel() {
        return fontBlueChannel;
    }

    public int getFontGreenChannel() {
        return fontGreenChannel;
    }

    public int getFontLineHeight() {
        return fontLineHeight;
    }

    public int getFontOutline() {
        return fontOutline;
    }

    public int getFontPaddingDown() {
        return fontPaddingDown;
    }

    public int getFontPaddingLeft() {
        return fontPaddingLeft;
    }

    public int getFontPaddingRight() {
        return fontPaddingRight;
    }

    public int getFontPaddingUp() {
        return fontPaddingUp;
    }

    public int getFontPages() {
        return fontPages;
    }

    public int getFontRedChannel() {
        return fontRedChannel;
    }

    public int getFontScaleHeight() {
        return fontScaleHeight;
    }

    public int getFontScaleWidth() {
        return fontScaleWidth;
    }

    public int getFontSize() {
        return fontSize;
    }

    public int getFontSpacingHorizontal() {
        return fontSpacingHorizontal;
    }

    public int getFontSpacingVertical() {
        return fontSpacingVertical;
    }

    public int getFontStretchHeight() {
        return fontStretchHeight;
    }

    public int getTotalChars() {
        return totalChars;
    }

    public int getTotalKernings() {
        return totalKernings;
    }

    public Glyph getGlyph(int codePoint) {
        return null;
    }

    public Glyph getMissingGlyph() {
        return missingGlyph;
    }

    public void setMissingGlyph(Glyph missingGlyph) {
        this.missingGlyph = missingGlyph;
    }

    public HashMapIntString getPages() {
        return pages;
    }

    public HashMapIntGeneric<Character> getCharacters() {
        return characters;
    }

    public String getFontCharset() {
        return fontCharset;
    }

    public String getFontFace() {
        return fontFace;
    }

    public String getPage(int codePoint) {
        return pages.get(codePoint);
    }

    public boolean isFontBold() {
        return fontBold;
    }

    public boolean isFontItalic() {
        return fontItalic;
    }

    public boolean isFontPacked() {
        return fontPacked;
    }

    public boolean isFontSmooth() {
        return fontSmooth;
    }

    public boolean isFontUnicode() {
        return fontUnicode;
    }

    public void load(String filename) throws IOException {
        log.info("Loading Angel Code Font: {}", filename);
        reset();
        read(filename);
    }

    @Override
    public int getCharacterAdvance(char currentCharacter, char nextCharacter, float sizeX) {
        int currentCodePoint = (int) currentCharacter;
        int nextCodePoint = (int) nextCharacter;
        Character character = characters.get(currentCodePoint);
        if (character == null) {
            return -1;
        }
        return (int) ((character.getXAdvance() + character.getKerning(nextCodePoint)) * sizeX);
    }

    @Override
    public int getWidth(String text, float sizeX) {
        int length = 0;
        for (int i = 0; i < text.length(); i++) {
            char currentCharacter = text.charAt(i);
            char nextCharacter = 0;
            if (i < text.length() - 1) {
                nextCharacter = text.charAt(i + 1);
            }
            int advance = getCharacterAdvance(currentCharacter, nextCharacter, sizeX);
            if (advance != -1) {
                length += advance;
            }
        }
        return length;
    }

    @Override
    public String toString() {
        return "(fontBold= " + fontBold + " fontItalic= " + fontItalic + " fontPacked= " + fontPacked
                + " fontSmooth= " + fontSmooth + " fontUnicode= " + fontUnicode
                + " fontAlphaChannel= " + fontAlphaChannel + " fontAntiAliasing= " + fontAntiAliasing
                + " fontBase= " + fontBase + " fontBlueChannel= " + fontBlueChannel
                + " fontGreenChannel= " + fontGreenChannel + " fontLineHeight= " + fontLineHeight
                + " fontOutline= " + fontOutline + " fontPaddingDown= " + fontPaddingDown
                + " fontPaddingLeft= " + fontPaddingLeft + " fontPaddingRight= " + fontPaddingRight
                + " fontPaddingUp= " + fontPaddingUp + " fontPages= " + fontPages + " fontRedChannel= " + fontRedChannel
                + " fontScaleHeight= " + fontScaleHeight + " fontScaleWidth= " + fontScaleWidth
                + " fontSize= " + fontSize + " fontSpacingHorizontal= " + fontSpacingHorizontal
                + " fontSpacingVertical= " + fontSpacingVertical + " fontStretchHeight= " + fontStretchHeight
                + " totalChars= " + totalChars + " totalKernings= " + totalKernings
                + " pages(size)= " + pages.size() + " characters(size)= " + characters.size()
                + " fontCharset= " + fontCharset + " fontFace= " + fontFace + ")";
    }

    public static class Character {

        // The texture channel where the character image is found (1 = blue,
        // 2 = green, 4 = red, 8 = alpha, 15 = all channels)
        private int channel;
        private int height; // The height of the character image in the texture
        private int id; // The character id
        private int page; // The texture page where the character image is found
        private int width; // The width of the character image in the texture
        private int x; // The left position of the character image in the texture
        private int xAdvance; // How much the current position should be advanced after drawing the character
        // How much the current position should be offset when copying the image from the texture to the screen
        private int xOffset;
        private int y; // The top position of the character image in the texture
        // How much the current position should be offset when copying the image from the texture to the screen
        private int yOffset;
        // The second character id
        // How much the x position should be adjusted when drawing the second character immediately following the first
        private final HashMapIntInt kerning;

        public Character() {
            kerning = new HashMapIntInt();
        }

        public int getChannel() {
            return channel;
        }

        public void setChannel(int channel) {
            this.channel = channel;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getKerning(int second) {
            int amount = kerning.get(second);
            if (amount == HashMapIntInt.NOT_FOUND) {
                return 0;
            }
            return amount;
        }

        public void setKerning(int second, int amount) {
            kerning.put(second, amount);
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getXAdvance() {
            return xAdvance;
        }

        public void setXAdvance(int xAdvance) {
            this.xAdvance = xAdvance;
        }

        public int getXOffset() {
            return xOffset;
        }

        public void setXOffset(int xOffset) {
            this.xOffset = xOffset;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getYOffset() {
            return yOffset;
        }

        public void setYOffset(int yOffset) {
            this.yOffset = yOffset;
        }

        @Override
        public String toString() {
            return "(id= " + id + " channel= " + channel + " height= " + height + " page= " + page + " width= " + width
                    + " x= " + x + " xAdvance= " + xAdvance + " xOffset= " + xOffset + " y= " + y
                    + " yOffset= " + yOffset + " kerning(size)= " + kerning.size();
        }

    }

    public static class Glyph {

        public AbstractRenderable renderable;
        public Character character;

    }

}
