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
package net.smert.frameworkgl.gui;

import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.Texture;
import net.smert.frameworkgl.opengl.TextureType;
import net.smert.frameworkgl.opengl.constants.Primitives;
import net.smert.frameworkgl.opengl.font.AngelCodeFont;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.mesh.Segment;
import net.smert.frameworkgl.opengl.mesh.SegmentMaterial;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderer.AngelCodeFontRenderer;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class RenderDevice implements de.lessvoid.nifty.spi.render.RenderDevice {

    private boolean blendEnabled;
    private boolean scissorEnabled;
    private boolean textureEnabled;
    private int scissorHeight;
    private int scissorWidth;
    private int scissorX;
    private int scissorY;
    private int statRenderedGlyphs;
    private int statTriangles;
    private int viewportHeight;
    private int viewportWidth;
    private BlendMode blendMode;
    private final List<AbstractRenderable> renderables;
    private MouseCursor mouseCursor;
    private NiftyResourceLoader niftyResourceLoader;

    public RenderDevice() {
        renderables = new ArrayList<>();
    }

    private void tesselateQuad(int x, int y, int width, int height, Color topLeft, Color topRight, Color bottomRight,
            Color bottomLeft, String name) {
        GL.tessellator.setConvertToTriangles(true);
        GL.tessellator.reset();
        GL.tessellator.setLocalPosition(0, 0, 0);
        GL.tessellator.start(Primitives.QUADS);

        GL.tessellator.addColor(topRight.getRed(), topRight.getGreen(), topRight.getBlue(), topRight.getAlpha());
        GL.tessellator.addVertex(x + width, y, 0);
        GL.tessellator.addColor(topLeft.getRed(), topLeft.getGreen(), topLeft.getBlue(), topLeft.getAlpha());
        GL.tessellator.addVertex(x, y, 0);
        GL.tessellator.addColor(bottomLeft.getRed(), bottomLeft.getGreen(), bottomLeft.getBlue(), bottomLeft.getAlpha());
        GL.tessellator.addVertex(x, y - height, 0);
        GL.tessellator.addColor(bottomRight.getRed(), bottomRight.getGreen(), bottomRight.getBlue(), bottomRight.getAlpha());
        GL.tessellator.addVertex(x + width, y - height, 0);

        GL.tessellator.stop();
        GL.tessellator.addSegment(name);
    }

    private void tesselateQuad(int x, int y, int width, int height, float uvX1, float uvY1, float uvX2, float uvY2,
            Color color, String name) {
        GL.tessellator.setConvertToTriangles(true);
        GL.tessellator.reset();
        GL.tessellator.setLocalPosition(0, 0, 0);
        GL.tessellator.start(Primitives.QUADS);

        GL.tessellator.addColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        GL.tessellator.addTexCoord(uvX2, uvY2);
        GL.tessellator.addVertex(x + width, y, 0);
        GL.tessellator.addColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        GL.tessellator.addTexCoord(uvX1, uvY2);
        GL.tessellator.addVertex(x, y, 0);
        GL.tessellator.addColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        GL.tessellator.addTexCoord(uvX1, uvY1);
        GL.tessellator.addVertex(x, y - height, 0);
        GL.tessellator.addColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        GL.tessellator.addTexCoord(uvX2, uvY1);
        GL.tessellator.addVertex(x + width, y - height, 0);

        GL.tessellator.stop();
        GL.tessellator.addSegment(name);
    }

    public void setViewportWidth(int width, int height) {
        viewportHeight = height;
        viewportWidth = width;
    }

    @Override
    public void setResourceLoader(NiftyResourceLoader niftyResourceLoader) {
        this.niftyResourceLoader = niftyResourceLoader;
    }

    @Override
    public RenderImage createImage(String filename, boolean filterLinear) {

        try {
            // Get image reader and load the image
            BufferedImage bufferedImage = GL.textureReader.getBufferedImage(filename);

            // Load the texture which creates a byte buffer
            GL.textureBuilder.load2D(bufferedImage);

            // Set texture filtering
            if (!filterLinear) {
                GL.textureBuilder.
                        setFilterMagNearest().
                        setFilterMinNearest();
            } else {
                GL.textureBuilder.
                        setFilterMagNearest().
                        setFilterMinLinearMipmapLinear().
                        setUseMipmaps(true);
            }

            // Build & create texture and create render image
            Texture texture = GL.textureBuilder.
                    buildTexture().
                    createTexture(true);

            // Add texture to pool
            Renderable.texturePool.add(filename, texture);

            return new net.smert.frameworkgl.gui.render.RenderImage(filename, texture);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public RenderFont createFont(String filename) {
        try {
            AngelCodeFont font = GL.angelCodeFontBuilder.load(filename).buildFont().createFont(true);
            AngelCodeFontRenderer renderer = GL.rendererFactory.createAngelCodeFontRenderer();
            renderer.init(font);
            return new net.smert.frameworkgl.gui.render.RenderFont(font, renderer);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int getWidth() {
        return viewportWidth;
    }

    @Override
    public int getHeight() {
        return viewportHeight;
    }

    @Override
    public void beginFrame() {

        // Reset blending
        blendEnabled = true;
        blendMode = null;
        setBlendMode(BlendMode.BLEND);
        GL.o1.enableBlending();

        // Reset scissor
        scissorEnabled = false;
        scissorX = 0;
        scissorY = 0;
        scissorWidth = 0;
        scissorHeight = 0;
        GL.o1.disableScissorTest();

        // Reset stats
        statRenderedGlyphs = 0;
        statTriangles = 0;

        // Reset texture
        textureEnabled = false;
        Fw.graphics.disableTexture2D();
    }

    @Override
    public void endFrame() {
        blendEnabled = false;
        scissorEnabled = false;
        textureEnabled = false;
        GL.o1.disableBlending();
        GL.o1.disableScissorTest();
        Fw.graphics.disableTexture2D();

        // Destroy renderables at the end of the frame
        for (AbstractRenderable renderable : renderables) {
            renderable.destroy();
        }
        renderables.clear();
    }

    @Override
    public void clear() {
        GL.o1.setClearColor(0, 0, 0, 0);
        GL.o1.clearColorBuffer();
    }

    @Override
    public void setBlendMode(BlendMode blendMode) {
        if (blendMode.equals(this.blendMode)) {
            return;
        }
        this.blendMode = blendMode;
        if (blendMode.equals(BlendMode.BLEND)) {
            GL.o1.setBlendingFunctionSrcAlphaAndOneMinusSrcAlpha();
        } else if (blendMode.equals(BlendMode.MULIPLY)) {
            GL.o1.setBlendingFunctionDstColorAndZero();
        }
    }

    @Override
    public void renderQuad(int x, int y, int width, int height, Color color) {
        renderQuad(x, y, width, height, color, color, color, color);
    }

    @Override
    public void renderQuad(int x, int y, int width, int height, Color topLeft, Color topRight, Color bottomRight,
            Color bottomLeft) {
        if (textureEnabled) {
            Fw.graphics.disableTexture2D();
            Renderable.textureBindState.unbindModel();
            textureEnabled = false;
        }

        y = getHeight() - y;
        tesselateQuad(x, y, width, height, topLeft, topRight, bottomRight, bottomLeft, "RenderDevice - Quad");

        Mesh mesh = Fw.graphics.createMesh(GL.tessellator);
        AbstractRenderable renderable = Fw.graphics.createNonInterleavedRenderable();
        renderable.create(mesh);
        renderables.add(renderable);

        Fw.graphics.render(renderable, 0, 0, 0);

        statTriangles += 2;
    }

    @Override
    public void renderImage(RenderImage image, int x, int y, int width, int height, Color color, float imageScale) {
        if (!textureEnabled) {
            Fw.graphics.enableTexture2D();
            textureEnabled = true;
        }

        y = getHeight() - y;
        tesselateQuad(x, y, width, height, 0f, 0f, 1f, 1f, color, "RenderDevice - Image");

        Mesh mesh = Fw.graphics.createMesh(GL.tessellator);
        Segment segment = mesh.getSegment(0);
        SegmentMaterial material = segment.getMaterial();
        if (material == null) {
            net.smert.frameworkgl.gui.render.RenderImage renderImage
                    = (net.smert.frameworkgl.gui.render.RenderImage) image;
            material = GL.meshFactory.createSegmentMaterial();
            material.setTexture(TextureType.DIFFUSE, renderImage.getFilename());
            segment.setMaterial(material);
        }
        AbstractRenderable renderable = Fw.graphics.createNonInterleavedRenderable();
        renderable.create(mesh);
        renderables.add(renderable);

        Fw.graphics.pushMatrix();
        Vector3f translate = new Vector3f(x + width / 2, y + height / 2, 0f);
        Fw.graphics.translate(translate);
        Fw.graphics.scale(imageScale, imageScale, 1f);
        Fw.graphics.translate(translate.invert());
        Fw.graphics.render(renderable);
        Fw.graphics.popMatrix();

        statTriangles += 2;
    }

    @Override
    public void renderImage(RenderImage image, int x, int y, int width, int height, int srcX, int srcY, int srcW,
            int srcH, Color color, float imageScale, int centerX, int centerY) {
        if (!textureEnabled) {
            Fw.graphics.enableTexture2D();
            textureEnabled = true;
        }

        float textureWidth = (float) image.getWidth();
        float textureHeight = (float) image.getHeight();

        float u0 = srcX / textureWidth;
        float v0 = srcY / textureHeight;
        float u1 = (srcX + srcW) / textureWidth;
        float v1 = (srcY + srcH) / textureHeight;

        y = getHeight() - y;
        tesselateQuad(x, y, width, height, u0, v0, u1, v1, color, "RenderDevice - Image UV");

        Mesh mesh = Fw.graphics.createMesh(GL.tessellator);
        Segment segment = mesh.getSegment(0);
        SegmentMaterial material = segment.getMaterial();
        if (material == null) {
            net.smert.frameworkgl.gui.render.RenderImage renderImage
                    = (net.smert.frameworkgl.gui.render.RenderImage) image;
            material = GL.meshFactory.createSegmentMaterial();
            material.setTexture(TextureType.DIFFUSE, renderImage.getFilename());
            segment.setMaterial(material);
        }
        AbstractRenderable renderable = Fw.graphics.createNonInterleavedRenderable();
        renderable.create(mesh);
        renderables.add(renderable);

        Fw.graphics.pushMatrix();
        Fw.graphics.translate(centerX, centerY, 0f);
        Fw.graphics.scale(imageScale, imageScale, 1f);
        Fw.graphics.translate(-centerX, -centerY, 0f);
        Fw.graphics.render(renderable);
        Fw.graphics.popMatrix();

        statTriangles += 2;
    }

    @Override
    public void renderFont(RenderFont font, String text, int x, int y, Color fontColor, float sizeX, float sizeY) {

        y = getHeight() - y;
        net.smert.frameworkgl.gui.render.RenderFont renderFont
                = (net.smert.frameworkgl.gui.render.RenderFont) font;
        AngelCodeFontRenderer renderer = renderFont.getAngelCodeFontRenderer();
        Fw.graphics.setTextColor(fontColor.getRed(), fontColor.getGreen(), fontColor.getBlue(), fontColor.getAlpha());
        Fw.graphics.drawString(text, x, y, sizeX, sizeY, renderer);

        statRenderedGlyphs += text.length();
        statTriangles += 2;
    }

    @Override
    public void enableClip(int x0, int y0, int x1, int y1) {

        if (scissorEnabled
                && (scissorX == x0) && (scissorY == y0)
                && (scissorWidth == x1) && (scissorHeight == y1)) {
            return;
        }

        scissorEnabled = true;
        scissorX = x0;
        scissorY = y0;
        scissorWidth = x1;
        scissorHeight = y1;

        GL.o1.scissor(x0, getHeight() - y1, x1 - x0, y1 - y0);
        GL.o1.enableScissorTest();
    }

    @Override
    public void disableClip() {
        if (!scissorEnabled) {
            return;
        }
        scissorEnabled = false;
        GL.o1.disableScissorTest();
    }

    @Override
    public MouseCursor createMouseCursor(String filename, int hotspotX, int hotspotY) throws IOException {
        net.smert.frameworkgl.gui.render.MouseCursor cursor = new net.smert.frameworkgl.gui.render.MouseCursor();

        // Get image reader and load the image
        BufferedImage bufferedImage = GL.textureReader.getBufferedImage(filename);

        // Load the texture which creates a byte buffer
        GL.textureBuilder.load2D(bufferedImage);

        // Extract data from texture builder
        int height = GL.textureBuilder.getTextureHeight();
        int width = GL.textureBuilder.getTextureWidth();
        ByteBuffer pixelData = GL.textureBuilder.getPixelByteBuffer();

        // Reset the builder
        GL.textureBuilder.reset();

        // Create the cursor
        cursor.create(width, height, hotspotX, height - hotspotY - 1, 1, pixelData.asIntBuffer(), null);

        return cursor;
    }

    @Override
    public void enableMouseCursor(MouseCursor mouseCursor) {
        this.mouseCursor = mouseCursor;
        mouseCursor.enable();
    }

    @Override
    public void disableMouseCursor() {
        if (mouseCursor != null) {
            mouseCursor.disable();
        }
    }

}
