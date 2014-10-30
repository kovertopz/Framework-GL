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
import net.smert.frameworkgl.opengl.VertexBufferObject;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderable.RenderableConfiguration;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public abstract class AbstractVertexBufferObjectNonInterleavedRenderable extends AbstractRenderable {

    protected int bufferUsage;
    protected int renderableConfigID;
    protected RenderCall renderCall;
    protected VertexBufferObject vboColor;
    protected VertexBufferObject vboNormal;
    protected VertexBufferObject vboTexCoord;
    protected VertexBufferObject vboVertex;
    protected VertexBufferObject vboVertexIndex;

    public AbstractVertexBufferObjectNonInterleavedRenderable() {
        bufferUsage = -1;
        renderableConfigID = -1;
        renderCall = null;
        vboColor = null;
        vboNormal = null;
        vboTexCoord = null;
        vboVertex = null;
        vboVertexIndex = null;
    }

    protected void bindGL1() {

        // Switch the renderable configuration first
        Renderable.bindState.switchRenderableConfiguration(renderableConfigID);

        // Bind each VBO
        if (vboColor != null) {
            GL.vboHelper.enableColors();
            Renderable.bindState.bindColorGL1(vboColor.getVboID(), 0, 0);
        } else {
            GL.vboHelper.disableColors();
        }
        if (vboNormal != null) {
            GL.vboHelper.enableNormals();
            Renderable.bindState.bindNormalGL1(vboNormal.getVboID(), 0, 0);
        } else {
            GL.vboHelper.disableNormals();
        }
        if (vboTexCoord != null) {
            GL.vboHelper.enableTexCoords();
            Renderable.bindState.bindTexCoordGL1(vboTexCoord.getVboID(), 0, 0);
        } else {
            GL.vboHelper.disableTexCoords();
        }
        if (vboVertex != null) {
            GL.vboHelper.enableVertices();
            Renderable.bindState.bindVertexGL1(vboVertex.getVboID(), 0, 0);
        } else {
            GL.vboHelper.disableVertices();
        }
        if (vboVertexIndex != null) {
            Renderable.bindState.bindVertexIndex(vboVertexIndex.getVboID());
        } else {
            Renderable.bindState.bindVertexIndex(0);
        }
    }

    protected void bindGL2() {

        // Switch the renderable configuration first
        Renderable.bindState.switchRenderableConfiguration(renderableConfigID);

        // Bind each VBO
        if (vboColor != null) {
            GL.vboHelper.enableVertexAttribArray(Renderable.bindState.getColorIndex());
            Renderable.bindState.bindColorGL2(vboColor.getVboID(), 0, 0);
        } else {
            GL.vboHelper.disableVertexAttribArray(Renderable.bindState.getColorIndex());
        }
        if (vboNormal != null) {
            GL.vboHelper.enableVertexAttribArray(Renderable.bindState.getNormalIndex());
            Renderable.bindState.bindNormalGL2(vboNormal.getVboID(), 0, 0);
        } else {
            GL.vboHelper.disableVertexAttribArray(Renderable.bindState.getNormalIndex());
        }
        if (vboTexCoord != null) {
            GL.vboHelper.enableVertexAttribArray(Renderable.bindState.getTexCoord0Index());
            Renderable.bindState.bindTexCoordGL2(vboTexCoord.getVboID(), 0, 0);
        } else {
            GL.vboHelper.disableVertexAttribArray(Renderable.bindState.getTexCoord0Index());
        }
        if (vboVertex != null) {
            GL.vboHelper.enableVertexAttribArray(Renderable.bindState.getVertexIndex());
            Renderable.bindState.bindVertexGL2(vboVertex.getVboID(), 0, 0);
        } else {
            GL.vboHelper.disableVertexAttribArray(Renderable.bindState.getVertexIndex());
        }
        if (vboVertexIndex != null) {
            Renderable.bindState.bindVertexIndex(vboVertexIndex.getVboID());
        } else {
            Renderable.bindState.bindVertexIndex(0);
        }
    }

    protected void createGL1AndGL2(Mesh mesh) {

        // Get configuration
        renderableConfigID = mesh.getRenderableConfigID();
        RenderableConfiguration config = Renderable.configPool.get(renderableConfigID);

        // Destroy existing VBOs
        destroy();

        // Create non interleaved buffer data
        if ((mesh.hasColors()) || (mesh.hasNormals()) || (mesh.hasTexCoords()) || (mesh.hasVertices())) {
            Renderable.renderableBuilder.createNonInterleavedBufferData(mesh, Renderable.byteBuffers, config);
        }

        // Create VBO and send byte buffer data for colors
        if (mesh.hasColors()) {
            vboColor = GL.glFactory.createVertexBufferObject();
            vboColor.create();
            GL.vboHelper.setBufferData(vboColor.getVboID(), Renderable.byteBuffers.getColor(), bufferUsage);
        }

        // Create VBO and send byte buffer data for normals
        if (mesh.hasNormals()) {
            vboNormal = GL.glFactory.createVertexBufferObject();
            vboNormal.create();
            GL.vboHelper.setBufferData(vboNormal.getVboID(), Renderable.byteBuffers.getNormal(), bufferUsage);
        }

        // Create VBO and send byte buffer data for texture coordinates
        if (mesh.hasTexCoords()) {
            vboTexCoord = GL.glFactory.createVertexBufferObject();
            vboTexCoord.create();
            GL.vboHelper.setBufferData(vboTexCoord.getVboID(), Renderable.byteBuffers.getTexCoord(),
                    bufferUsage);
        }

        // Create VBO and send byte buffer data for vertices
        if (mesh.hasVertices()) {
            vboVertex = GL.glFactory.createVertexBufferObject();
            vboVertex.create();
            GL.vboHelper.setBufferData(vboVertex.getVboID(), Renderable.byteBuffers.getVertex(), bufferUsage);
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

        // Create render call
        renderCall = Renderable.vboDrawCallBuilder.createRenderCall(mesh, config);
    }

    protected void renderGL1() {
        bindGL1();
        renderCall.render();
    }

    protected void renderGL2() {
        bindGL2();
        renderCall.render();
    }

    protected void updateGL1AndGL2(Mesh mesh) {

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

        // Create render call
        renderCall = Renderable.vboDrawCallBuilder.createRenderCall(mesh, config);
    }

    @Override
    public void destroy() {
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
