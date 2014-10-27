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
package net.smert.frameworkgl.opengl.renderable.shared;

import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.VertexArrayObject;
import net.smert.frameworkgl.opengl.VertexBufferObject;
import net.smert.frameworkgl.opengl.VertexBufferObjectInterleaved;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderable.RenderableConfiguration;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public abstract class AbstractVertexArrayObjectInterleavedRenderable extends AbstractRenderable {

    protected boolean hasColors;
    protected boolean hasNormals;
    protected boolean hasTexCoords;
    protected boolean hasVertices;
    protected int bufferUsage;
    protected int renderableConfigID;
    protected RenderCall drawCall;
    protected VertexArrayObject vao;
    protected VertexBufferObject vboVertexIndex;
    protected VertexBufferObjectInterleaved vboInterleaved;

    public AbstractVertexArrayObjectInterleavedRenderable() {
        hasColors = false;
        hasNormals = false;
        hasTexCoords = false;
        hasVertices = false;
        bufferUsage = -1;
        renderableConfigID = -1;
        drawCall = null;
        vboVertexIndex = null;
        vboInterleaved = null;
    }

    protected void bindGL3() {

        // Bind VAO
        Renderable.bindState.bindVAO(vao.getVaoID());

        // Bind VBO for each type
        if (hasColors) {
            GL.vboHelper.enableVertexAttribArray(Renderable.bindState.getColorIndex());
        } else {
            GL.vboHelper.disableVertexAttribArray(Renderable.bindState.getColorIndex());
        }
        if (hasNormals) {
            GL.vboHelper.enableVertexAttribArray(Renderable.bindState.getNormalIndex());
        } else {
            GL.vboHelper.disableVertexAttribArray(Renderable.bindState.getNormalIndex());
        }
        if (hasTexCoords) {
            GL.vboHelper.enableVertexAttribArray(Renderable.bindState.getTexCoord0Index());
        } else {
            GL.vboHelper.disableVertexAttribArray(Renderable.bindState.getTexCoord0Index());
        }
        if (hasVertices) {
            GL.vboHelper.enableVertexAttribArray(Renderable.bindState.getVertexIndex());
        } else {
            GL.vboHelper.disableVertexAttribArray(Renderable.bindState.getVertexIndex());
        }
        if (vboVertexIndex != null) {
            Renderable.bindState.bindVertexIndex(vboVertexIndex.getVboID());
        } else {
            Renderable.bindState.bindVertexIndex(0);
        }
    }

    protected void createGL3(Mesh mesh) {

        // Get configuration
        renderableConfigID = mesh.getRenderableConfigID();
        RenderableConfiguration config = Renderable.configPool.get(renderableConfigID);

        // Destroy existing VAO and VBOs
        destroy();

        // Create VAO and bind
        vao = GL.glFactory.createVertexArrayObject();
        vao.create();
        GL.vaoHelper.bind(vao.getVaoID());

        // Create interleaved buffer data, VBO and send interleaved buffer data
        if ((mesh.hasColors()) || (mesh.hasNormals()) || (mesh.hasTexCoords()) || (mesh.hasVertices())) {
            vboInterleaved = GL.glFactory.createVertexBufferObjectInterleaved();
            vboInterleaved.create();
            Renderable.renderableBuilder.calculateOffsetsAndStride(mesh, vboInterleaved, config);
            Renderable.renderableBuilder.createInterleavedBufferData(mesh, vboInterleaved.getStrideBytes(),
                    Renderable.byteBuffers, config);
            GL.vboHelper.setBufferData(vboInterleaved.getVboID(), Renderable.byteBuffers.getInterleaved(),
                    bufferUsage);
        }

        hasColors = mesh.hasColors();
        hasNormals = mesh.hasNormals();
        hasTexCoords = mesh.hasTexCoords();
        hasVertices = mesh.hasVertices();

        // Update VAO state
        if (hasColors) {
            GL.vboHelper.bindVertexAttrib(vboInterleaved.getVboID(), GL.defaultAttribLocations.getIndex("color"),
                    config.getColorSize(), config.getColorType(), vboInterleaved.getStrideBytes(),
                    vboInterleaved.getColorOffsetBytes());
        }
        if (hasNormals) {
            GL.vboHelper.bindVertexAttrib(vboInterleaved.getVboID(), GL.defaultAttribLocations.getIndex("normal"),
                    config.getNormalSize(), config.getNormalType(), vboInterleaved.getStrideBytes(),
                    vboInterleaved.getNormalOffsetBytes());
        }
        if (hasTexCoords) {
            GL.vboHelper.bindVertexAttrib(vboInterleaved.getVboID(), GL.defaultAttribLocations.getIndex("texCoord0"),
                    config.getTexCoordSize(), config.getTexCoordType(), vboInterleaved.getStrideBytes(),
                    vboInterleaved.getTexCoordOffsetBytes());
        }
        if (hasVertices) {
            GL.vboHelper.bindVertexAttrib(vboInterleaved.getVboID(), GL.defaultAttribLocations.getIndex("vertex"),
                    config.getVertexSize(), config.getVertexType(), vboInterleaved.getStrideBytes(),
                    vboInterleaved.getVertexOffsetBytes());
        }

        // Create VBO, byte buffer data and send byte buffer data for indexes
        if (mesh.hasIndexes()) {
            vboVertexIndex = GL.glFactory.createVertexBufferObject();
            vboVertexIndex.create();
            Renderable.renderableBuilder.createIndexBufferData(mesh, Renderable.byteBuffers, config);
            GL.vboHelper.setBufferElementData(vboVertexIndex.getVboID(), Renderable.byteBuffers.getVertexIndex(),
                    bufferUsage);
        }

        GL.vboHelper.unbind();
        GL.vaoHelper.unbind();

        // Create draw call
        drawCall = Renderable.vboDrawCallBuilder.createDrawCall(mesh, config);
    }

    protected void renderGL3() {
        bindGL3();
        drawCall.render();
    }

    protected void updateGL3(Mesh mesh) {

        // Get configuration
        renderableConfigID = mesh.getRenderableConfigID();
        RenderableConfiguration config = Renderable.configPool.get(renderableConfigID);

        // Create interleaved buffer data and send interleaved buffer data
        if ((mesh.hasColors()) || (mesh.hasNormals()) || (mesh.hasTexCoords()) || (mesh.hasVertices())) {
            Renderable.renderableBuilder.calculateOffsetsAndStride(mesh, vboInterleaved, config);
            Renderable.renderableBuilder.createInterleavedBufferData(
                    mesh, vboInterleaved.getStrideBytes(), Renderable.byteBuffers, config);
            GL.vboHelper.updateBufferData(vboInterleaved.getVboID(), 0, Renderable.byteBuffers.getInterleaved());
        }

        hasColors = mesh.hasColors();
        hasNormals = mesh.hasNormals();
        hasTexCoords = mesh.hasTexCoords();
        hasVertices = mesh.hasVertices();

        // Create byte buffer data and send byte buffer data for indexes
        if ((mesh.hasIndexes()) && (vboVertexIndex != null)) {
            Renderable.renderableBuilder.createIndexBufferData(mesh, Renderable.byteBuffers, config);
            GL.vboHelper.updateBufferElementData(vboVertexIndex.getVboID(), 0,
                    Renderable.byteBuffers.getVertexIndex());
        }

        GL.vboHelper.unbind();

        // Create draw call
        drawCall = Renderable.vboDrawCallBuilder.createDrawCall(mesh, config);
    }

    @Override
    public void destroy() {
        if (vao != null) {
            vao.destroy();
            vao = null;
        }
        if (vboInterleaved != null) {
            vboInterleaved.destroy();
            vboInterleaved = null;
        }
        if (vboVertexIndex != null) {
            vboVertexIndex.destroy();
            vboVertexIndex = null;
        }
        hasColors = false;
        hasNormals = false;
        hasTexCoords = false;
        hasVertices = false;
    }

}
