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
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.font.GLFont;
import net.smert.frameworkgl.opengl.mesh.Material;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.texture.TextureType;
import net.smert.frameworkgl.utils.Color;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class GLFontRenderer {

    private int defaultX;
    private int defaultY;
    private int fontSpacing;
    private final Color color;
    private GLFont defaultFont;
    private final Vector2f position;
    private final Vector2f tempPosition;

    public GLFontRenderer() {
        defaultX = 3;
        defaultY = 0;
        fontSpacing = 1;
        color = new Color();
        position = new Vector2f();
        tempPosition = new Vector2f();
    }

    private void createGlyphRenderable(GLFont.Glyph glyph, TextRenderer renderer) {
        if (glyph.renderable == null) {
            int fontHeight = glyph.codePage.getImage().getHeight();
            int fontWidth = glyph.codePage.getImage().getWidth();
            String fontTextureFilename = glyph.codePage.getFontTextureFilename();

            // Calculate texture coordinates for the glyph
            float maxX = (float) (glyph.x + glyph.w) / fontWidth;
            float minX = (float) (glyph.x) / fontWidth;
            float maxY = (float) (glyph.y + glyph.h) / fontHeight;
            float minY = (float) (glyph.y) / fontHeight;

            // Create quad
            Mesh mesh = GL.dynamicMeshBuilder.
                    setColor(0, "white").
                    setLocalPosition(0f, 0f, 1f).
                    setQuality(1, 1, 1).
                    setSize(glyph.w, glyph.h, 0f).
                    setTexCoordMinMaxX(minX, maxX).
                    setTexCoordMinMaxY(1f - maxY, 1f - minY).
                    build("quad").
                    createMesh(true);

            // Create material
            Material material = mesh.getSegment(0).getMaterial();
            if (material == null) {
                material = GL.meshFactory.createMaterial();
                mesh.getSegment(0).setMaterial(material);
            }

            // Set the diffuse texture 
            mesh.getSegment(0).getMaterial().setTexture(TextureType.DIFFUSE, fontTextureFilename);

            // Create renderable from mesh
            glyph.renderable = renderer.createGlyphRenderable();
            glyph.renderable.create(mesh);
        }
    }

    private void drawString(String text, Vector2f position, GLFont font, TextRenderer renderer) {
        renderer.pushMatrix();
        renderer.translateText(position.getX(), position.getY());
        for (int i = 0; i < text.length(); i++) {
            int codePoint = text.codePointAt(i);
            if (codePoint == 32) {
                renderer.translateText(font.getFontSpacing(), 0f);
                position.addX(font.getFontSpacing());
                continue;
            }
            GLFont.Glyph glyph = font.getGlyph(codePoint);
            createGlyphRenderable(glyph, renderer);
            renderer.translateText(glyph.w / 2, 0f);
            glyph.renderable.render();
            renderer.translateText((glyph.w / 2) + fontSpacing, 0f);
            position.addX(glyph.w + fontSpacing);
        }
        renderer.popMatrix();
    }

    public void drawString(String text, float x, float y, TextRenderer renderer) {
        drawString(text, x, y, defaultFont, renderer);
    }

    public void drawString(String text, float x, float y, GLFont font, TextRenderer renderer) {
        tempPosition.set(x, y);
        drawString(text, tempPosition, font, renderer);
    }

    public void drawString(String text, TextRenderer renderer) {
        drawString(text, position, defaultFont, renderer);
    }

    public void drawString(String text, GLFont font, TextRenderer renderer) {
        drawString(text, position, font, renderer);
    }

    public int getDefaultX() {
        return defaultX;
    }

    public void setDefaultX(int defaultX) {
        this.defaultX = defaultX;
    }

    public int getDefaultY() {
        return defaultY;
    }

    public void setDefaultY(int defaultY) {
        this.defaultY = defaultY;
    }

    public int getFontSpacing() {
        return fontSpacing;
    }

    public void setFontSpacing(int fontSpacing) {
        this.fontSpacing = fontSpacing;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(float r, float g, float b, float a) {
        color.set(r, g, b, a);
    }

    public void setColor(Color color) {
        this.color.set(color);
    }

    public void setColor(String colorName) {
        color.set(colorName);
    }

    public void setColorHex(String hexCode) {
        this.color.setHex(hexCode);
    }

    public GLFont getDefaultFont() {
        return defaultFont;
    }

    public void setDefaultFont(GLFont defaultFont) {
        this.defaultFont = defaultFont;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position.set(position);
    }

    public void newHalfLine() {
        position.setX(defaultX);
        position.addY(-defaultFont.getFontHeight() / 2);
    }

    public void newHalfLine(GLFont font) {
        position.setX(defaultX);
        position.addY(-font.getFontHeight() / 2);
    }

    public void newLine() {
        position.setX(defaultX);
        position.addY(-defaultFont.getFontHeight());
    }

    public void newLine(GLFont font) {
        position.setX(defaultX);
        position.addY(-font.getFontHeight());
    }

    public void reset() {
        position.setX(defaultX);
        position.setY(Fw.config.getCurrentHeight() - defaultY);
    }

}
