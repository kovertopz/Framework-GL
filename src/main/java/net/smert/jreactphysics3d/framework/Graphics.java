package net.smert.jreactphysics3d.framework;

import java.io.IOException;
import java.util.List;
import net.smert.jreactphysics3d.framework.opengl.GL;
import net.smert.jreactphysics3d.framework.opengl.image.bmp.BMPReader;
import net.smert.jreactphysics3d.framework.opengl.image.gif.GIFReader;
import net.smert.jreactphysics3d.framework.opengl.image.jpg.JPGReader;
import net.smert.jreactphysics3d.framework.opengl.image.png.PNGReader;
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

    public void loadTextures(Mesh mesh) throws IOException {
        List<String> textures = mesh.getTextures();
        for (String texture : textures) {
            textureReader.load(texture);
        }
    }

    public void render(AbstractRenderable renderable, float x, float y, float z) {
        GL.o1.pushMatrix();
        GL.o1.translate(x, y, z);
        renderable.render();
        GL.o1.popMatrix();
    }

}
