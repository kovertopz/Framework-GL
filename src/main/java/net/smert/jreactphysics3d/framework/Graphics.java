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

import java.io.IOException;
import java.util.List;
import net.smert.jreactphysics3d.framework.opengl.GL;
import net.smert.jreactphysics3d.framework.opengl.Texture;
import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.mesh.MeshReader;
import net.smert.jreactphysics3d.framework.opengl.renderable.AbstractRenderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.factory.RenderableFactory;
import net.smert.jreactphysics3d.framework.opengl.renderable.factory.RenderableFactoryGL1;
import net.smert.jreactphysics3d.framework.opengl.texture.TextureReader;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Graphics {

    private final MeshReader meshReader;
    private final RenderableFactoryGL1 renderableFactoryGL1;
    private final TextureReader textureReader;

    public Graphics(MeshReader meshReader, RenderableFactoryGL1 renderableFactoryGL1, TextureReader textureReader) {
        this.meshReader = meshReader;
        this.renderableFactoryGL1 = renderableFactoryGL1;
        this.textureReader = textureReader;
    }

    public AbstractRenderable createDisplayListRenderable() {
        return renderableFactoryGL1.createDisplayList();
    }

    public AbstractRenderable createImmediateModeRenderable() {
        return renderableFactoryGL1.createImmediateMode();
    }

    public AbstractRenderable createVertexArrayRenderable() {
        return renderableFactoryGL1.createVertexArray();
    }

    public AbstractRenderable createVertexBufferObjectRenderable() {
        return renderableFactoryGL1.createVertexBufferObject();
    }

    public AbstractRenderable createVertexBufferObjectInterleavedRenderable() {
        return renderableFactoryGL1.createVertexBufferObjectInterleaved();
    }

    /**
     * This method should be called just before the Display is destroyed. This is automatically called in Application
     * during the normal shutdown process.
     */
    public void destroy() {
        RenderableFactory.vaBindState.reset();
        RenderableFactory.vboBindState.reset();
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
