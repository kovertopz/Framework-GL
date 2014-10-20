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
public class VertexBufferObjectNonInterleavedUpdateStrategy {

    public void update(Mesh mesh, VertexBufferObjectNonInterleavedData data) {

        // Get configuration
        RenderableConfiguration config = Renderable.configPool.get(data.renderableConfigID);

        // Create non interleaved buffer data
        if ((mesh.hasColors()) || (mesh.hasNormals()) || (mesh.hasTexCoords()) || (mesh.hasVertices())) {
            Renderable.renderableBuilder.createNonInterleavedBufferData(mesh, Renderable.byteBuffers, config);
        }

        // Send byte buffer data for colors
        if ((mesh.hasColors()) && (data.vboColor != null)) {
            GL.vboHelper.updateBufferData(data.vboColor.getVboID(), 0, Renderable.byteBuffers.getColor());
        }

        // Send byte buffer data for normals
        if ((mesh.hasNormals()) && (data.vboNormal != null)) {
            GL.vboHelper.updateBufferData(data.vboNormal.getVboID(), 0, Renderable.byteBuffers.getNormal());
        }

        // Send byte buffer data for texture coordinates
        if ((mesh.hasTexCoords()) && (data.vboTexCoord != null)) {
            GL.vboHelper.updateBufferData(data.vboTexCoord.getVboID(), 0, Renderable.byteBuffers.getTexCoord());
        }

        // Send byte buffer data for vertices
        if ((mesh.hasVertices()) && (data.vboVertex != null)) {
            GL.vboHelper.updateBufferData(data.vboVertex.getVboID(), 0, Renderable.byteBuffers.getVertex());
        }

        // Create byte buffer data and send byte buffer data for indexes
        if ((mesh.hasIndexes()) && (data.vboVertexIndex != null)) {
            Renderable.renderableBuilder.createIndexBufferData(mesh, Renderable.byteBuffers, config);
            GL.vboHelper.updateBufferElementData(data.vboVertexIndex.getVboID(), 0,
                    Renderable.byteBuffers.getVertexIndex());
        }

        GL.vboHelper.unbind();

        // Create draw call
        data.drawCall = Renderable.vboDrawCallBuilder.createDrawCall(mesh, config);
    }

}
