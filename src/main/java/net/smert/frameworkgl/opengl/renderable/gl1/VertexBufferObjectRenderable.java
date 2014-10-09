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
package net.smert.frameworkgl.opengl.renderable.gl1;

import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.VertexBufferObject;
import net.smert.frameworkgl.opengl.constants.VertexBufferObjectTypes;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderable.RenderableConfiguration;
import net.smert.frameworkgl.opengl.renderable.shared.AbstractDrawCall;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VertexBufferObjectRenderable extends AbstractRenderable {

    private int renderableConfigID;
    private AbstractDrawCall drawCall;
    private VertexBufferObject vboColor;
    private VertexBufferObject vboNormal;
    private VertexBufferObject vboTexCoord;
    private VertexBufferObject vboVertex;
    private VertexBufferObject vboVertexIndex;

    public VertexBufferObjectRenderable() {
        renderableConfigID = -1;
        drawCall = null;
    }

    @Override
    public void create(Mesh mesh) {

        // Get configuration
        renderableConfigID = mesh.getRenderableConfigID();
        RenderableConfiguration config = Renderable.configPool.get(renderableConfigID);

        // Destroy existing VBOs
        destroy();

        Renderable.byteBuffers.reset();

        // Create VBOs
        if ((mesh.hasColors()) || (mesh.hasNormals()) || (mesh.hasTexCoords()) || (mesh.hasVertices())) {
            Renderable.vboBuilder.createNonInterleavedBufferData(mesh, Renderable.byteBuffers, config);
        }

        // Send byte buffer data for colors
        if (mesh.hasColors()) {
            vboColor = GL.glFactory.createVertexBufferObject();
            vboColor.create();
            GL.vboHelper.setBufferData(vboColor.getVboID(), Renderable.byteBuffers.getColor(),
                    VertexBufferObjectTypes.STATIC_DRAW);
        }

        // Send byte buffer data for normals
        if (mesh.hasNormals()) {
            vboNormal = GL.glFactory.createVertexBufferObject();
            vboNormal.create();
            GL.vboHelper.setBufferData(vboNormal.getVboID(), Renderable.byteBuffers.getNormal(),
                    VertexBufferObjectTypes.STATIC_DRAW);
        }

        // Send byte buffer data for texture coordinates
        if (mesh.hasTexCoords()) {
            vboTexCoord = GL.glFactory.createVertexBufferObject();
            vboTexCoord.create();
            GL.vboHelper.setBufferData(vboTexCoord.getVboID(), Renderable.byteBuffers.getTexCoord(),
                    VertexBufferObjectTypes.STATIC_DRAW);
        }

        // Send byte buffer data for vertices
        if (mesh.hasVertices()) {
            vboVertex = GL.glFactory.createVertexBufferObject();
            vboVertex.create();
            GL.vboHelper.setBufferData(vboVertex.getVboID(), Renderable.byteBuffers.getVertex(),
                    VertexBufferObjectTypes.STATIC_DRAW);
        }

        // Create VBO for indexes
        if (mesh.hasIndexes()) {
            vboVertexIndex = GL.glFactory.createVertexBufferObject();
            vboVertexIndex.create();
            Renderable.vboBuilder.createIndexBufferData(mesh, Renderable.byteBuffers, config);
            GL.vboHelper.setBufferElementData(vboVertexIndex.getVboID(), Renderable.byteBuffers.getVertexIndex(),
                    VertexBufferObjectTypes.STATIC_DRAW);
        }

        GL.vboHelper.unbind();

        // Create draw call
        drawCall = Renderable.vboBuilder.createDrawCall(mesh, config);
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

    @Override
    public void render() {

        // Switch the renderable configuration first
        Renderable.vboBindState.switchRenderableConfiguration(renderableConfigID);

        // Bind each VBO
        if (vboColor != null) {
            Renderable.vboBindState.bindColor(vboColor.getVboID(), 0, 0);
        }
        if (vboNormal != null) {
            Renderable.vboBindState.bindNormal(vboNormal.getVboID(), 0, 0);
        }
        if (vboTexCoord != null) {
            Renderable.vboBindState.bindTextureCoordinate(vboTexCoord.getVboID(), 0, 0);
        }
        if (vboVertex != null) {
            Renderable.vboBindState.bindVertex(vboVertex.getVboID(), 0, 0);
        }
        if (vboVertexIndex != null) {
            Renderable.vboBindState.bindVertexIndex(vboVertexIndex.getVboID());
        }

        drawCall.render();
    }

}
