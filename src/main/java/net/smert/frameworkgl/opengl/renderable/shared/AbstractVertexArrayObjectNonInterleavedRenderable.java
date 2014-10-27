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
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderable.RenderableConfiguration;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public abstract class AbstractVertexArrayObjectNonInterleavedRenderable extends AbstractRenderable {

    protected int bufferUsage;
    protected int renderableConfigID;
    protected RenderCall drawCall;
    protected VertexArrayObject vao;
    protected VertexBufferObject vboColor;
    protected VertexBufferObject vboNormal;
    protected VertexBufferObject vboTexCoord;
    protected VertexBufferObject vboVertex;
    protected VertexBufferObject vboVertexIndex;

    public AbstractVertexArrayObjectNonInterleavedRenderable() {
        bufferUsage = -1;
        renderableConfigID = -1;
        drawCall = null;
        vboColor = null;
        vboNormal = null;
        vboTexCoord = null;
        vboVertex = null;
        vboVertexIndex = null;
    }

    protected void bindGL3() {

        // Bind VAO
        Renderable.bindState.bindVAO(vao.getVaoID());

        // Bind each VBO
        if (vboColor != null) {
            GL.vboHelper.enableVertexAttribArray(Renderable.bindState.getColorIndex());
        } else {
            GL.vboHelper.disableVertexAttribArray(Renderable.bindState.getColorIndex());
        }
        if (vboNormal != null) {
            GL.vboHelper.enableVertexAttribArray(Renderable.bindState.getNormalIndex());
        } else {
            GL.vboHelper.disableVertexAttribArray(Renderable.bindState.getNormalIndex());
        }
        if (vboTexCoord != null) {
            GL.vboHelper.enableVertexAttribArray(Renderable.bindState.getTexCoord0Index());
        } else {
            GL.vboHelper.disableVertexAttribArray(Renderable.bindState.getTexCoord0Index());
        }
        if (vboVertex != null) {
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

        // Create non interleaved buffer data
        if ((mesh.hasColors()) || (mesh.hasNormals()) || (mesh.hasTexCoords()) || (mesh.hasVertices())) {
            Renderable.renderableBuilder.createNonInterleavedBufferData(mesh, Renderable.byteBuffers, config);
        }

        // Create VBO, send byte buffer data for colors and update VAO state
        if (mesh.hasColors()) {
            vboColor = GL.glFactory.createVertexBufferObject();
            vboColor.create();
            GL.vboHelper.setBufferData(vboColor.getVboID(), Renderable.byteBuffers.getColor(), bufferUsage);
            GL.vboHelper.bindVertexAttrib(vboColor.getVboID(), GL.defaultAttribLocations.getIndex("color"),
                    config.getColorSize(), config.getColorType(), 0, 0);
        }

        // Create VBO, send byte buffer data for normals and update VAO state
        if (mesh.hasNormals()) {
            vboNormal = GL.glFactory.createVertexBufferObject();
            vboNormal.create();
            GL.vboHelper.setBufferData(vboNormal.getVboID(), Renderable.byteBuffers.getNormal(), bufferUsage);
            GL.vboHelper.bindVertexAttrib(vboNormal.getVboID(), GL.defaultAttribLocations.getIndex("normal"),
                    config.getNormalSize(), config.getNormalType(), 0, 0);
        }

        // Create VBO, send byte buffer data for texture coordinates and update VAO state
        if (mesh.hasTexCoords()) {
            vboTexCoord = GL.glFactory.createVertexBufferObject();
            vboTexCoord.create();
            GL.vboHelper.setBufferData(vboTexCoord.getVboID(), Renderable.byteBuffers.getTexCoord(),
                    bufferUsage);
            GL.vboHelper.bindVertexAttrib(vboTexCoord.getVboID(), GL.defaultAttribLocations.getIndex("texCoord0"),
                    config.getTexCoordSize(), config.getTexCoordType(), 0, 0);
        }

        // Create VBO, send byte buffer data for vertices and update VAO state
        if (mesh.hasVertices()) {
            vboVertex = GL.glFactory.createVertexBufferObject();
            vboVertex.create();
            GL.vboHelper.setBufferData(vboVertex.getVboID(), Renderable.byteBuffers.getVertex(), bufferUsage);
            GL.vboHelper.bindVertexAttrib(vboVertex.getVboID(), GL.defaultAttribLocations.getIndex("vertex"),
                    config.getVertexSize(), config.getVertexType(), 0, 0);
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
        RenderableConfiguration config = Renderable.configPool.get(renderableConfigID);

        // Create non interleaved buffer data
        if ((mesh.hasColors()) || (mesh.hasNormals()) || (mesh.hasTexCoords()) || (mesh.hasVertices())) {
            Renderable.renderableBuilder.createNonInterleavedBufferData(mesh, Renderable.byteBuffers, config);
        }

        // Send byte buffer data for colors
        if ((mesh.hasColors()) && (vboColor != null)) {
            GL.vboHelper.updateBufferData(vboColor.getVboID(), 0, Renderable.byteBuffers.getColor());
        }

        // Send byte buffer data for normals
        if ((mesh.hasNormals()) && (vboNormal != null)) {
            GL.vboHelper.updateBufferData(vboNormal.getVboID(), 0, Renderable.byteBuffers.getNormal());
        }

        // Send byte buffer data for texture coordinates
        if ((mesh.hasTexCoords()) && (vboTexCoord != null)) {
            GL.vboHelper.updateBufferData(vboTexCoord.getVboID(), 0, Renderable.byteBuffers.getTexCoord());
        }

        // Send byte buffer data for vertices
        if ((mesh.hasVertices()) && (vboVertex != null)) {
            GL.vboHelper.updateBufferData(vboVertex.getVboID(), 0, Renderable.byteBuffers.getVertex());
        }

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
        if (vboColor != null) {
            vboColor.destroy();
            vboColor = null;
        }
        if (vboNormal != null) {
            vboNormal.destroy();
            vboNormal = null;
        }
        if (vboTexCoord != null) {
            vboTexCoord.destroy();
            vboTexCoord = null;
        }
        if (vboVertex != null) {
            vboVertex.destroy();
            vboVertex = null;
        }
        if (vboVertexIndex != null) {
            vboVertexIndex.destroy();
            vboVertexIndex = null;
        }
    }

}
