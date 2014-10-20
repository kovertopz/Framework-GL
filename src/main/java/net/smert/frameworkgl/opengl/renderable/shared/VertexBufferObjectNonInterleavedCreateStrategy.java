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
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderable.RenderableConfiguration;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VertexBufferObjectNonInterleavedCreateStrategy {

    public void create(Mesh mesh, VertexBufferObjectNonInterleavedData data) {

        // Get configuration
        data.renderableConfigID = mesh.getRenderableConfigID();
        RenderableConfiguration config = Renderable.configPool.get(data.renderableConfigID);

        // Destroy existing VBOs
        data.destroy();

        // Create non interleaved buffer data
        if ((mesh.hasColors()) || (mesh.hasNormals()) || (mesh.hasTexCoords()) || (mesh.hasVertices())) {
            Renderable.renderableBuilder.createNonInterleavedBufferData(mesh, Renderable.byteBuffers, config);
        }

        // Create VBO and send byte buffer data for colors
        if (mesh.hasColors()) {
            data.vboColor = GL.glFactory.createVertexBufferObject();
            data.vboColor.create();
            GL.vboHelper.setBufferData(data.vboColor.getVboID(), Renderable.byteBuffers.getColor(), data.bufferUsage);
        }

        // Create VBO and send byte buffer data for normals
        if (mesh.hasNormals()) {
            data.vboNormal = GL.glFactory.createVertexBufferObject();
            data.vboNormal.create();
            GL.vboHelper.setBufferData(data.vboNormal.getVboID(), Renderable.byteBuffers.getNormal(), data.bufferUsage);
        }

        // Create VBO and send byte buffer data for texture coordinates
        if (mesh.hasTexCoords()) {
            data.vboTexCoord = GL.glFactory.createVertexBufferObject();
            data.vboTexCoord.create();
            GL.vboHelper.setBufferData(data.vboTexCoord.getVboID(), Renderable.byteBuffers.getTexCoord(),
                    data.bufferUsage);
        }

        // Create VBO and send byte buffer data for vertices
        if (mesh.hasVertices()) {
            data.vboVertex = GL.glFactory.createVertexBufferObject();
            data.vboVertex.create();
            GL.vboHelper.setBufferData(data.vboVertex.getVboID(), Renderable.byteBuffers.getVertex(), data.bufferUsage);
        }

        // Create VBO, byte buffer data and send byte buffer data for indexes
        if (mesh.hasIndexes()) {
            data.vboVertexIndex = GL.glFactory.createVertexBufferObject();
            data.vboVertexIndex.create();
            Renderable.renderableBuilder.createIndexBufferData(mesh, Renderable.byteBuffers, config);
            GL.vboHelper.setBufferElementData(data.vboVertexIndex.getVboID(), Renderable.byteBuffers.getVertexIndex(),
                    data.bufferUsage);
        }

        GL.vboHelper.unbind();

        // Create draw call
        data.drawCall = Renderable.vboDrawCallBuilder.createDrawCall(mesh, config);
    }

}
