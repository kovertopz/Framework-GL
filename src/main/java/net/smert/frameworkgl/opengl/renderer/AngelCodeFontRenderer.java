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
import net.smert.frameworkgl.opengl.TextureType;
import net.smert.frameworkgl.opengl.constants.Primitives;
import net.smert.frameworkgl.opengl.font.AngelCodeFont;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.mesh.Segment;
import net.smert.frameworkgl.opengl.mesh.SegmentMaterial;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class AngelCodeFontRenderer implements FontRenderer {

    private int extraFontAdvanceX;
    private AngelCodeFont angelCodeFont;
    private final Vector2f tempPosition;

    public AngelCodeFontRenderer() {
        extraFontAdvanceX = 0;
        tempPosition = new Vector2f();
    }

    private void createGlyphRenderable(AngelCodeFont.Glyph glyph, TextHelperRenderer renderer) {
        if (glyph.renderable == null) {
            int fontHeight = angelCodeFont.getFontScaleHeight();
            int fontWidth = angelCodeFont.getFontScaleWidth();
            int page = glyph.character.getPage();
            String fontTextureFilename = angelCodeFont.getPage(page);

            // Calculate texture coordinates for the glyph
            float height = glyph.character.getHeight();
            float maxX = (float) (glyph.character.getX() + glyph.character.getWidth()) / fontWidth;
            float minX = (float) (glyph.character.getX()) / fontWidth;
            float maxY = (float) (glyph.character.getY() + glyph.character.getHeight()) / fontHeight;
            float minY = (float) (glyph.character.getY()) / fontHeight;
            float width = glyph.character.getWidth();
            float x = glyph.character.getXOffset();
            float y = -glyph.character.getYOffset();

            // Use tessellator to create a quad
            GL.tessellator.setConvertToTriangles(true);
            GL.tessellator.reset();
            GL.tessellator.setLocalPosition(0, 0, 0);
            GL.tessellator.start(Primitives.QUADS);

            GL.tessellator.addTexCoord(minX, 1f - maxY);
            GL.tessellator.addVertex(x, y - height, 0);
            GL.tessellator.addTexCoord(maxX, 1f - maxY);
            GL.tessellator.addVertex(x + width, y - height, 0);
            GL.tessellator.addTexCoord(maxX, 1f - minY);
            GL.tessellator.addVertex(x + width, y, 0);
            GL.tessellator.addTexCoord(minX, 1f - minY);
            GL.tessellator.addVertex(x, y, 0);

            GL.tessellator.stop();
            GL.tessellator.addSegment("Angel Code Font Renderer Quad");

            // Create mesh
            Mesh mesh = Fw.graphics.createMesh(GL.tessellator);

            // Create segment material
            Segment segment = mesh.getSegment(0);
            SegmentMaterial segmentMaterial = segment.getMaterial();
            if (segmentMaterial == null) {
                segmentMaterial = GL.meshFactory.createSegmentMaterial();
                segment.setMaterial(segmentMaterial);
            }

            // Set the diffuse texture
            segmentMaterial.setTexture(TextureType.DIFFUSE, fontTextureFilename);

            // Create renderable from mesh
            glyph.renderable = renderer.createGlyphRenderable();
            glyph.renderable.create(mesh);
        }
    }

    private void drawString(String text, Vector2f position, float sizeX, float sizeY, TextHelperRenderer renderer) {
        renderer.pushMatrix();
        renderer.translateText(position.getX(), position.getY());
        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);
            char nextChar = 0;
            if (i < text.length() - 1) {
                nextChar = text.charAt(i + 1);
            }
            int codePoint = text.codePointAt(i);
            int advanceX = angelCodeFont.getCharacterAdvance(currentChar, nextChar, sizeX);
            AngelCodeFont.Glyph glyph = angelCodeFont.getGlyph(codePoint);
            if (glyph == null) {
                continue;
            }
            createGlyphRenderable(glyph, renderer);
            renderer.colorText(renderer.getTextColor());
            renderer.scaleText(sizeX, sizeY);
            renderer.renderGlyph(glyph.renderable);
            renderer.translateText(advanceX + extraFontAdvanceX, 0f);
            position.addX(glyph.character.getWidth() * sizeX + extraFontAdvanceX);
        }
        renderer.popMatrix();
    }

    public AngelCodeFont getFont() {
        return angelCodeFont;
    }

    public void init(AngelCodeFont angelCodeFont) {
        this.angelCodeFont = angelCodeFont;
    }

    @Override
    public void drawString(String text, float x, float y, TextHelperRenderer renderer) {
        tempPosition.set(x, y);
        drawString(text, tempPosition, 1f, 1f, renderer);
    }

    @Override
    public void drawString(String text, float x, float y, float sizeX, float sizeY, TextHelperRenderer renderer) {
        tempPosition.set(x, y);
        drawString(text, tempPosition, sizeX, sizeY, renderer);
    }

    @Override
    public void drawString(String text, TextHelperRenderer renderer) {
        drawString(text, renderer.getTextPosition(), 1f, 1f, renderer);
    }

    @Override
    public int getExtraFontAdvanceX() {
        return extraFontAdvanceX;
    }

    @Override
    public void setExtraFontAdvanceX(int extraFontAdvanceX) {
        this.extraFontAdvanceX = extraFontAdvanceX;
    }

    @Override
    public void newHalfLine(TextHelperRenderer renderer, Vector2f position) {
        position.setX(renderer.getTextDefaultX());
        position.addY(-angelCodeFont.getFontLineHeight() / 2);
    }

    @Override
    public void newLine(TextHelperRenderer renderer, Vector2f position) {
        position.setX(renderer.getTextDefaultX());
        position.addY(-angelCodeFont.getFontLineHeight());
    }

    @Override
    public void reset(TextHelperRenderer renderer, Vector2f position) {
        position.setX(renderer.getTextDefaultX());
        position.setY(Fw.config.getCurrentHeight() - renderer.getTextDefaultY());
    }

}
