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
import net.smert.frameworkgl.opengl.font.AngelCodeFont;
import net.smert.frameworkgl.opengl.mesh.Mesh;
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
        extraFontAdvanceX = 1;
        tempPosition = new Vector2f();
    }

    private void createGlyphRenderable(AngelCodeFont.Glyph glyph, TextHelperRenderer renderer) {
        if (glyph.renderable == null) {
            int fontHeight = angelCodeFont.getFontScaleHeight();
            int fontWidth = angelCodeFont.getFontScaleWidth();
            int characterID = glyph.character.getId();
            String fontTextureFilename = angelCodeFont.getPage(characterID);

            // Calculate texture coordinates for the glyph
            float maxX = (float) (glyph.character.getX() + glyph.character.getWidth()) / fontWidth;
            float minX = (float) (glyph.character.getX()) / fontWidth;
            float maxY = (float) (glyph.character.getY() + glyph.character.getHeight()) / fontHeight;
            float minY = (float) (glyph.character.getY()) / fontHeight;

            // Create quad
            Mesh mesh = GL.meshFactory.createMesh();
            GL.dynamicMeshBuilder.
                    setColor(0, "white").
                    setLocalPosition(0f, 0f, 1f).
                    setQuality(1, 1, 1).
                    setSize(glyph.character.getWidth(), glyph.character.getHeight(), 0f).
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
            AngelCodeFont.Glyph glyph = angelCodeFont.getGlyph(codePoint);
            createGlyphRenderable(glyph, renderer);
            renderer.translateText((glyph.character.getWidth() / 2) * sizeX, 0f);
            renderer.colorText(renderer.getTextColor());
            renderer.scaleText(sizeX, sizeY);
            renderer.renderGlyph(glyph.renderable);
            renderer.translateText((glyph.character.getWidth() / 2) * sizeX + extraFontAdvanceX, 0f);
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
