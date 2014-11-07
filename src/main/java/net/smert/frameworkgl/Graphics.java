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
package net.smert.frameworkgl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.smert.frameworkgl.gameobjects.GameObject;
import net.smert.frameworkgl.math.AABB;
import net.smert.frameworkgl.math.AABBUtilities;
import net.smert.frameworkgl.math.Transform4f;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.MaterialLight;
import net.smert.frameworkgl.opengl.Shader;
import net.smert.frameworkgl.opengl.Texture;
import net.smert.frameworkgl.opengl.camera.Camera;
import net.smert.frameworkgl.opengl.constants.ShaderTypes;
import net.smert.frameworkgl.opengl.constants.TextureTargets;
import net.smert.frameworkgl.opengl.font.AwtFont;
import net.smert.frameworkgl.opengl.image.ImageReader;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.mesh.Segment;
import net.smert.frameworkgl.opengl.mesh.Tessellator;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderable.RenderableConfiguration;
import net.smert.frameworkgl.opengl.renderable.factory.RenderableFactory;
import net.smert.frameworkgl.opengl.renderable.shared.DrawCommands;
import net.smert.frameworkgl.opengl.renderer.AbstractRendererGL;
import net.smert.frameworkgl.opengl.renderer.AwtFontRenderer;
import net.smert.frameworkgl.opengl.renderer.FontRenderer;
import net.smert.frameworkgl.opengl.renderer.GLRenderer;
import net.smert.frameworkgl.opengl.renderer.TextRenderer;
import net.smert.frameworkgl.opengl.shader.AbstractShader;
import net.smert.frameworkgl.utils.Color;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Graphics implements GLRenderer, TextRenderer {

    private static RenderableComparison renderableComparison = new RenderableComparison();

    private AbstractRendererGL renderer;
    private FontRenderer fontRenderer;
    private RenderableFactory renderableFactory;

    public Shader buildShader(String fragmentShaderFilename, String vertexShaderFilename, String shaderName)
            throws IOException {
        return GL.shaderBuilder.
                load(fragmentShaderFilename, ShaderTypes.FRAGMENT_SHADER).
                load(vertexShaderFilename, ShaderTypes.VERTEX_SHADER).
                compileShaders().
                buildProgram(shaderName).
                createShader(true);
    }

    public AbstractRenderable createArrayRenderable() {
        return renderableFactory.createArrayRenderable();
    }

    public AbstractRenderable createDisplayListRenderable() {
        return renderableFactory.createDisplayListRenderable();
    }

    public AbstractRenderable createDynamicInterleavedRenderable() {
        return renderableFactory.createDynamicInterleavedRenderable();
    }

    public AbstractRenderable createDynamicNonInterleavedRenderable() {
        return renderableFactory.createDynamicNonInterleavedRenderable();
    }

    public AbstractRenderable createImmediateModeRenderable() {
        return renderableFactory.createImmediateModeRenderable();
    }

    public AbstractRenderable createInterleavedRenderable() {
        return renderableFactory.createInterleavedRenderable();
    }

    public Mesh createMesh(DrawCommands drawCommands) {

        // Create new segment with the draw commands
        Segment segment = GL.meshFactory.createSegment();
        segment.setDrawCommands(drawCommands);

        // Check to see if a renderable configuration exists before adding it
        RenderableConfiguration config = GL.meshFactory.createRenderableConfiguration();
        int renderableConfigID = Renderable.configPool.getOrAdd(config);

        // Create mesh and set config ID and add segment
        Mesh mesh = GL.meshFactory.createMesh();
        mesh.setRenderableConfigID(renderableConfigID);
        mesh.addSegment(segment);
        return mesh;
    }

    public Mesh createMesh(Tessellator tessellator) {
        Mesh mesh = GL.meshFactory.createMesh();
        return createMesh(tessellator, mesh);
    }

    public Mesh createMesh(Tessellator tessellator, Mesh mesh) {

        // Reset the mesh
        mesh.reset();

        // Save AABB
        tessellator.getAABB(mesh.getAabb());

        // Check to see if a renderable configuration exists before adding it
        RenderableConfiguration config = tessellator.getRenderableConfiguration();
        int renderableConfigID = Renderable.configPool.getOrAdd(config);

        // Set config ID
        mesh.setRenderableConfigID(renderableConfigID);

        // Add segments
        List<Segment> segments = tessellator.getSegments();
        for (Segment segment : segments) {
            mesh.addSegment(segment);
        }

        return mesh;
    }

    public AbstractRenderable createNonInterleavedRenderable() {
        return renderableFactory.createNonInterleavedRenderable();
    }

    /**
     * This method should be called just before the Display is destroyed. This is automatically called in Application
     * during the normal shutdown process.
     */
    public void destroy() {
        GL.renderer1.destroy();
        GL.renderer2.destroy();
        GL.renderer3.destroy();
        Renderable.bindState.reset();
        Renderable.shaderBindState.reset();
        Renderable.textureBindState.reset();
        GL.fboHelper.unbind();
        GL.textureHelper.unbind();
        GL.vboHelper.unbind();
        Renderable.configPool.destroy();
        Renderable.materialLightPool.destroy();
        Renderable.shaderPool.destroy();
        Renderable.texturePool.destroy();
    }

    public AbstractRendererGL getRenderer() {
        return renderer;
    }

    public Comparator<GameObject> getRenderableComparison() {
        return renderableComparison;
    }

    public void setRenderableComparison(RenderableComparison renderableComparison) {
        Graphics.renderableComparison = renderableComparison;
    }

    public FontRenderer getFontRenderer() {
        return fontRenderer;
    }

    public void setFontRenderer(FontRenderer fontRenderer) {
        this.fontRenderer = fontRenderer;
    }

    public Texture getTexture(String filename) {
        return Renderable.texturePool.get(filename);
    }

    public void init() {
        MaterialLight materialLight = GL.glFactory.createMaterialLight();
        GL.renderer1.init();
        GL.renderer2.init();
        GL.renderer3.init();
        GL.uniformVariables.setAmbientLight(GL.glFactory.createAmbientLight());
        GL.uniformVariables.setDefaultMaterialLight(materialLight);
        GL.uniformVariables.setGlLight(GL.glFactory.createGLLight());
        GL.uniformVariables.setGlLights(new ArrayList<>());
        Renderable.bindState.setAttribLocations(GL.defaultAttribLocations);
        Renderable.materialLightPool.add("default", materialLight);
        Renderable.shaderBindState.init();
        AwtFont awtFont = GL.awtFontBuilder.
                addUsAsciiGlyphs().
                setAntiAliasing(true).
                setBold(true).
                setFamily("Dialog").
                setLeftToRight(true).
                setSize(16).
                buildFont().
                createFont(true);
        AwtFontRenderer awtFontRenderer = GL.rendererFactory.createAwtFontRenderer();
        awtFontRenderer.init(awtFont);
        setFontRenderer(awtFontRenderer);
        switchOpenGLVersion(1); // Switch to OpenGL 1.X
    }

    public void loadMesh(String filename, Mesh mesh) throws IOException {
        GL.meshReader.load(filename, mesh);
    }

    public void loadCubeMapTexture(String folderName, String fileExtension) throws IOException {
        ImageReader imageReader = GL.textureReader.getImageReader("." + fileExtension);
        String filename;
        filename = folderName + "/xpos." + fileExtension;
        BufferedImage xPosBufferedImage = imageReader.load(filename);
        filename = folderName + "/xneg." + fileExtension;
        BufferedImage xNegBufferedImage = imageReader.load(filename);
        filename = folderName + "/ypos." + fileExtension;
        BufferedImage yPosBufferedImage = imageReader.load(filename);
        filename = folderName + "/yneg." + fileExtension;
        BufferedImage yNegBufferedImage = imageReader.load(filename);
        filename = folderName + "/zpos." + fileExtension;
        BufferedImage zPosBufferedImage = imageReader.load(filename);
        filename = folderName + "/zneg." + fileExtension;
        BufferedImage zNegBufferedImage = imageReader.load(filename);
        GL.textureBuilder.
                setLoadFlipVertically(false)
                .loadCube(xPosBufferedImage, TextureTargets.TEXTURE_CUBE_MAP_POSITIVE_X)
                .loadCube(xNegBufferedImage, TextureTargets.TEXTURE_CUBE_MAP_NEGATIVE_X)
                .loadCube(yPosBufferedImage, TextureTargets.TEXTURE_CUBE_MAP_POSITIVE_Y)
                .loadCube(yNegBufferedImage, TextureTargets.TEXTURE_CUBE_MAP_NEGATIVE_Y)
                .loadCube(zPosBufferedImage, TextureTargets.TEXTURE_CUBE_MAP_POSITIVE_Z)
                .loadCube(zNegBufferedImage, TextureTargets.TEXTURE_CUBE_MAP_NEGATIVE_Z)
                .setClampingWrapRClampToEdge().setClampingWrapSClampToEdge().setClampingWrapTClampToEdge()
                .setFilterMagLinear().setFilterMinLinear().setTextureTargetCubeMap().buildTexture();
        Texture texture = GL.textureBuilder.createTexture(true);
        Renderable.texturePool.add(folderName + "/cubemap." + fileExtension, texture);
    }

    public void loadTexture(String filename) throws IOException {
        Texture texture = GL.textureReader.load(filename);
        Renderable.texturePool.add(filename, texture);
    }

    public void loadTextures(Mesh mesh) throws IOException {
        List<String> textures = mesh.getTextures();
        for (String filename : textures) {
            Texture texture = GL.textureReader.load(filename);
            Renderable.texturePool.add(filename, texture);
        }
    }

    public void performCulling(Camera camera, GameObject gameObject) {
        boolean inFrustum = camera.getFrustumCulling().isAABBInFrustum(gameObject.getWorldAabb());
        gameObject.getRenderableState().setInFrustum(inFrustum);
    }

    public void performCulling(Camera camera, List<GameObject> gameObjects) {
        for (GameObject gameObject : gameObjects) {
            performCulling(camera, gameObject);
        }
    }

    public void sort(List<GameObject> gameObjects, Vector3f cameraPosition) {
        sort(gameObjects, cameraPosition, renderableComparison);
    }

    public void sort(List<GameObject> gameObjects, Vector3f cameraPosition, RenderableComparison renderableComparison) {
        renderableComparison.setCameraPosition(cameraPosition);
        Collections.sort(gameObjects, renderableComparison);
    }

    public void switchOpenGLVersion(int openglMajorVersion) {
        switch (openglMajorVersion) {
            case 1:
                renderableFactory = GL.rf1;
                renderer = GL.renderer1;
                break;
            case 2:
                renderableFactory = GL.rf2;
                renderer = GL.renderer2;
                break;
            case 3:
                renderableFactory = GL.rf3;
                renderer = GL.renderer3;
                break;
            default:
                throw new RuntimeException("Unknown OpenGL version: " + openglMajorVersion);
        }
        setDefaultFontRenderer(fontRenderer);
    }

    public void updateAabb(GameObject gameObject) {
        AABB localAabb = gameObject.getMesh().getAabb();
        AABB worldAabb = gameObject.getWorldAabb();
        Transform4f worldTransform = gameObject.getWorldTransform();
        AABBUtilities.Transform(localAabb, worldTransform, worldAabb);
    }

    public void updateAabb(GameObject gameObject, float margin) {
        AABB localAabb = gameObject.getMesh().getAabb();
        AABB worldAabb = gameObject.getWorldAabb();
        Transform4f worldTransform = gameObject.getWorldTransform();
        AABBUtilities.Transform(localAabb, margin, worldTransform, worldAabb);
    }

    public void updateAabb(List<GameObject> gameObjects) {
        for (GameObject gameObject : gameObjects) {
            updateAabb(gameObject);
        }
    }

    public void updateAabb(List<GameObject> gameObjects, float margin) {
        for (GameObject gameObject : gameObjects) {
            updateAabb(gameObject, margin);
        }
    }

    @Override
    public void color(float r, float g, float b, float a) {
        renderer.color(r, g, b, a);
    }

    @Override
    public void disableTexture2D() {
        renderer.disableTexture2D();
    }

    @Override
    public void disableTexture3D() {
        renderer.disableTexture3D();
    }

    @Override
    public void disableTextureCubeMap() {
        renderer.disableTextureCubeMap();
    }

    @Override
    public void enableTexture2D() {
        renderer.enableTexture2D();
    }

    @Override
    public void enableTexture3D() {
        renderer.enableTexture3D();
    }

    @Override
    public void enableTextureCubeMap() {
        renderer.enableTextureCubeMap();
    }

    @Override
    public void popMatrix() {
        renderer.popMatrix();
    }

    @Override
    public void pushMatrix() {
        renderer.pushMatrix();
    }

    @Override
    public void render(AbstractRenderable renderable) {
        renderer.render(renderable);
    }

    @Override
    public void render(AbstractRenderable renderable, float x, float y, float z) {
        renderer.render(renderable, x, y, z);
    }

    @Override
    public void render(AbstractRenderable renderable, Transform4f transform) {
        renderer.render(renderable, transform);
    }

    @Override
    public void render(AbstractRenderable renderable, Vector3f position) {
        renderer.render(renderable, position);
    }

    @Override
    public void render(GameObject gameObject) {
        renderer.render(gameObject);
    }

    @Override
    public void render(List<GameObject> gameObjects) {
        renderer.render(gameObjects);
    }

    @Override
    public void renderBlend(GameObject gameObject) {
        renderer.renderBlend(gameObject);
    }

    @Override
    public void renderBlend(List<GameObject> gameObjects) {
        renderer.renderBlend(gameObjects);
    }

    @Override
    public void renderOpaque(GameObject gameObject) {
        renderer.renderOpaque(gameObject);
    }

    @Override
    public void renderOpaque(List<GameObject> gameObjects) {
        renderer.renderOpaque(gameObjects);
    }

    @Override
    public void scale(float x, float y, float z) {
        renderer.scale(x, y, z);
    }

    @Override
    public void scale(Vector3f scaling) {
        renderer.scale(scaling);
    }

    @Override
    public void set2DMode() {
        renderer.set2DMode();
    }

    @Override
    public void set2DMode(int width, int height) {
        renderer.set2DMode(width, height);
    }

    @Override
    public void setCamera(Camera camera) {
        renderer.setCamera(camera);
    }

    @Override
    public void switchShader(AbstractShader shader) {
        renderer.switchShader(shader);
    }

    @Override
    public void translate(float x, float y, float z) {
        renderer.translate(x, y, z);
    }

    @Override
    public void translate(Vector3f position) {
        renderer.translate(position);
    }

    @Override
    public void unbindShader() {
        renderer.unbindShader();
    }

    @Override
    public void drawString(String text) {
        renderer.drawString(text);
    }

    @Override
    public void drawString(String text, float x, float y) {
        renderer.drawString(text, x, y);
    }

    @Override
    public void drawString(String text, float x, float y, FontRenderer fontRenderer) {
        renderer.drawString(text, x, y, fontRenderer);
    }

    @Override
    public void drawString(String text, float x, float y, float sizeX, float sizeY) {
        renderer.drawString(text, x, y, sizeX, sizeY);
    }

    @Override
    public void drawString(String text, float x, float y, float sizeX, float sizeY, FontRenderer fontRenderer) {
        renderer.drawString(text, x, y, sizeX, sizeY, fontRenderer);
    }

    @Override
    public void drawString(String text, FontRenderer fontRenderer) {
        renderer.drawString(text, fontRenderer);
    }

    @Override
    public Color getTextColor() {
        return renderer.getTextColor();
    }

    @Override
    public void resetTextRendering() {
        renderer.resetTextRendering();
    }

    @Override
    public void setDefaultFontRenderer(FontRenderer defaultFontRenderer) {
        renderer.setDefaultFontRenderer(defaultFontRenderer);
    }

    @Override
    public void setTextColor(float r, float g, float b, float a) {
        renderer.setTextColor(r, g, b, a);
    }

    @Override
    public void setTextColor(Color color) {
        renderer.setTextColor(color);
    }

    @Override
    public void setTextColor(String colorName) {
        renderer.setTextColor(colorName);
    }

    @Override
    public void setTextColorHex(String hexCode) {
        renderer.setTextColorHex(hexCode);
    }

    @Override
    public void textNewHalfLine() {
        renderer.textNewHalfLine();
    }

    @Override
    public void textNewHalfLine(FontRenderer fontRenderer) {
        renderer.textNewHalfLine(fontRenderer);
    }

    @Override
    public void textNewLine() {
        renderer.textNewLine();
    }

    @Override
    public void textNewLine(FontRenderer fontRenderer) {
        renderer.textNewLine(fontRenderer);
    }

    public static class RenderableComparison implements Comparator<GameObject> {

        private Vector3f cameraPosition;

        public void setCameraPosition(Vector3f cameraPosition) {
            this.cameraPosition = cameraPosition;
        }

        @Override
        public int compare(GameObject o1, GameObject o2) {
            boolean o1Opaque = o1.getRenderableState().isOpaque();
            boolean o2Opaque = o2.getRenderableState().isOpaque();
            if ((o1Opaque == o2Opaque) && (o1Opaque)) {
                return 0;
            }
            if ((o1Opaque != o2Opaque) && (!o1Opaque)) {
                return 1;
            }
            if ((o1Opaque != o2Opaque) && (o1Opaque)) {
                return -1;
            }
            // Both objects are not opaque
            Vector3f o1Position = o1.getWorldTransform().getPosition();
            Vector3f o2Position = o2.getWorldTransform().getPosition();
            return (int) cameraPosition.distanceSquared(o2Position)
                    - (int) cameraPosition.distanceSquared(o1Position);
        }

    }

}
