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
import net.smert.frameworkgl.opengl.font.AwtFont;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.mesh.SegmentMaterial;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class AwtFontRenderer implements FontRenderer {

    private int extraFontAdvanceX;
    private AwtFont awtFont;
    private final Vector2f tempPosition;

    public AwtFontRenderer() {
        extraFontAdvanceX = 1;
        tempPosition = new Vector2f();
    }

    private void createGlyphRenderable(AwtFont.Glyph glyph, TextHelperRenderer renderer) {
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
            Mesh mesh = GL.meshFactory.createMesh();
            GL.dynamicMeshBuilder.
                    setColor(0, "white").
                    setLocalPosition(0f, 0f, 1f).
                    setQuality(1, 1, 1).
                    setSize(glyph.w, glyph.h, 0f).
                    setTexCoordMinMaxX(minX, maxX).
                    setTexCoordMinMaxY(1f - maxY, 1f - minY).
                    build("quad").
                    createMesh(true, mesh);

            // Create segment material
            SegmentMaterial segmentMaterial = mesh.getSegment(0).getMaterial();
            if (segmentMaterial == null) {
                segmentMaterial = GL.meshFactory.createSegmentMaterial();
                mesh.getSegment(0).setMaterial(segmentMaterial);
            }

            // Set the diffuse texture
            mesh.getSegment(0).getMaterial().setTexture(TextureType.DIFFUSE, fontTextureFilename);

            // Create renderable from mesh
            glyph.renderable = renderer.createGlyphRenderable();
            glyph.renderable.create(mesh);
        }
    }

    private void drawString(String text, Vector2f position, float sizeX, float sizeY, TextHelperRenderer renderer) {
        renderer.pushMatrix();
        renderer.translateText(position.getX(), position.getY());
        for (int i = 0; i < text.length(); i++) {
            int codePoint = text.codePointAt(i);
            if (codePoint == 32) {
                renderer.translateText(awtFont.getFontSpacing() * sizeX, 0f);
                position.addX(awtFont.getFontSpacing());
                continue;
            }
            AwtFont.Glyph glyph = awtFont.getGlyph(codePoint);
            createGlyphRenderable(glyph, renderer);
            renderer.translateText((glyph.w / 2) * sizeX, 0f);
            renderer.colorText(renderer.getTextColor());
            renderer.scaleText(sizeX, sizeY);
            renderer.renderGlyph(glyph.renderable);
            renderer.translateText((glyph.w / 2) * sizeX + extraFontAdvanceX, 0f);
            position.addX(glyph.w * sizeX + extraFontAdvanceX);
        }
        renderer.popMatrix();
    }

    public AwtFont getFont() {
        return awtFont;
    }

    public void init(AwtFont awtFont) {
        this.awtFont = awtFont;
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
        position.addY(-awtFont.getFontHeight() / 2);
    }

    @Override
    public void newLine(TextHelperRenderer renderer, Vector2f position) {
        position.setX(renderer.getTextDefaultX());
        position.addY(-awtFont.getFontHeight());
    }

    @Override
    public void reset(TextHelperRenderer renderer, Vector2f position) {
        position.setX(renderer.getTextDefaultX());
        position.setY(Fw.config.getCurrentHeight() - renderer.getTextDefaultY());
    }

}
