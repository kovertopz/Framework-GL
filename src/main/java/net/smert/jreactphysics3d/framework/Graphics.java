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
import java.nio.FloatBuffer;
import java.util.List;
import net.smert.jreactphysics3d.framework.opengl.GL;
import net.smert.jreactphysics3d.framework.opengl.Texture;
import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.mesh.Segment;
import net.smert.jreactphysics3d.framework.opengl.renderable.AbstractRenderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.Renderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.gl1.DrawCommands;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Graphics {

    public AbstractRenderable createDisplayListRenderable() {
        return GL.rf1.createDisplayList();
    }

    public AbstractRenderable createImmediateModeRenderable() {
        return GL.rf1.createImmediateMode();
    }

    public Mesh createMeshWithDrawCommands(DrawCommands drawCommands) {
        Segment segment = GL.mf.createSegment();
        segment.setDrawCommands(drawCommands);
        Mesh mesh = GL.mf.createMesh();
        mesh.addSegment(segment);
        return mesh;
    }

    public AbstractRenderable createVertexArrayRenderable() {
        return GL.rf1.createVertexArray();
    }

    public AbstractRenderable createVertexBufferObjectRenderable() {
        return GL.rf1.createVertexBufferObject();
    }

    public AbstractRenderable createVertexBufferObjectInterleavedRenderable() {
        return GL.rf1.createVertexBufferObjectInterleaved();
    }

    /**
     * This method should be called just before the Display is destroyed. This is automatically called in Application
     * during the normal shutdown process.
     */
    public void destroy() {
        Renderable.shaderBindState.reset();
        Renderable.textureBindState.reset();
        Renderable.vaBindState.reset();
        Renderable.vboBindState.reset();
        GL.fboHelper.unbind();
        GL.textureHelper.unbind();
        GL.vboHelper.unbind();
        Renderable.shaderPool.destroy();
        Renderable.texturePool.destroy();
    }

    public Texture getTexture(String filename) {
        return Renderable.texturePool.get(filename);
    }

    public void loadMesh(String filename, Mesh mesh) throws IOException {
        GL.meshReader.load(filename, mesh);
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

    public void render(AbstractRenderable renderable, float x, float y, float z) {
        GL.o1.pushMatrix();
        GL.o1.translate(x, y, z);
        renderable.render();
        GL.o1.popMatrix();
    }

    public void render(AbstractRenderable renderable, FloatBuffer transformWorldFloatBuffer) {
        GL.o1.pushMatrix();
        GL.o1.multiplyMatrix(transformWorldFloatBuffer);
        renderable.render();
        GL.o1.popMatrix();
    }

    public void render(GameObject gameObject, FloatBuffer transformWorldFloatBuffer) {
        GL.o1.pushMatrix();
        transformWorldFloatBuffer.rewind();
        gameObject.getWorldTransform().toFloatBuffer(transformWorldFloatBuffer);
        transformWorldFloatBuffer.flip();
        GL.o1.multiplyMatrix(transformWorldFloatBuffer);
        gameObject.getRenderable().render();
        GL.o1.popMatrix();
    }

}
