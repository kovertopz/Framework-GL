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
package net.smert.jreactphysics3d.framework;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import net.smert.jreactphysics3d.framework.opengl.GL;
import net.smert.jreactphysics3d.framework.opengl.Texture;
import net.smert.jreactphysics3d.framework.opengl.image.bmp.BMPReader;
import net.smert.jreactphysics3d.framework.opengl.image.gif.GIFReader;
import net.smert.jreactphysics3d.framework.opengl.image.jpg.JPGReader;
import net.smert.jreactphysics3d.framework.opengl.image.png.PNGReader;
import net.smert.jreactphysics3d.framework.opengl.image.tga.TGAReader;
import net.smert.jreactphysics3d.framework.opengl.image.tiff.TIFFReader;
import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.mesh.MeshReader;
import net.smert.jreactphysics3d.framework.opengl.model.obj.ObjReader;
import net.smert.jreactphysics3d.framework.opengl.renderable.AbstractRenderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.factory.RenderableFactoryGL1;
import net.smert.jreactphysics3d.framework.opengl.texture.TextureReader;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Graphics {

    public final MeshReader meshReader;
    public final RenderableFactoryGL1 renderableFactoryGL1;
    public final TextureReader textureReader;

    public Graphics() {
        meshReader = new MeshReader();
        meshReader.registerExtension("obj", new ObjReader());
        renderableFactoryGL1 = new RenderableFactoryGL1();
        textureReader = new TextureReader();
        textureReader.registerExtension("bmp", new BMPReader());
        textureReader.registerExtension("gif", new GIFReader());
        textureReader.registerExtension("jpeg", new JPGReader());
        textureReader.registerExtension("jpg", new JPGReader());
        textureReader.registerExtension("png", new PNGReader());
        textureReader.registerExtension("tga", new TGAReader());
        textureReader.registerExtension("tif", new TIFFReader());
        textureReader.registerExtension("tiff", new TIFFReader());
    }

    public AbstractRenderable createDisplayListRenderable(Mesh mesh) {
        return renderableFactoryGL1.createDisplayList(mesh);
    }

    public AbstractRenderable createImmediateModeRenderable(Mesh mesh) {
        return renderableFactoryGL1.createImmediateMode(mesh);
    }

    public AbstractRenderable createVertexArrayRenderable(Mesh mesh) {
        return renderableFactoryGL1.createVertexArray(mesh);
    }

    public AbstractRenderable createVertexBufferObjectRenderable(Mesh mesh) {
        return renderableFactoryGL1.createVertexBufferObject(mesh);
    }

    public AbstractRenderable createVertexBufferObjectInterleavedRenderable(Mesh mesh) {
        return renderableFactoryGL1.createVertexBufferObjectInterleaved(mesh);
    }

    /**
     * This method should be called just before the Display is destroyed. This is automatically called in Application
     * during the normal shutdown process.
     */
    public void destroy() {
        RenderableFactoryGL1.Destroy();
        GL.vboHelper.unbind();
    }

    public void loadMesh(String filename, Mesh mesh) throws IOException {
        meshReader.load(filename, mesh);
    }

    public void loadTexture(String filename) throws IOException {
        Texture texture = textureReader.load(filename);
    }

    public void loadTextures(Mesh mesh) throws IOException {
        List<String> textures = mesh.getTextures();
        for (String filename : textures) {
            Texture texture = textureReader.load(filename);
        }
    }

    public void render(AbstractRenderable renderable, float x, float y, float z) {
        GL.o1.pushMatrix();
        GL.o1.translate(x, y, z);
        renderable.render();
        GL.o1.popMatrix();
    }

}
