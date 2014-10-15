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
public class VertexBufferObjectInterleavedCreateStrategy {

    public void create(Mesh mesh, VertexBufferObjectInterleavedData data) {

        // Get configuration
        data.renderableConfigID = mesh.getRenderableConfigID();
        RenderableConfiguration config = Renderable.configPool.get(data.renderableConfigID);

        // Destroy existing VBOs
        data.destroy();

        // Create interleaved buffer data, VBO and send interleaved buffer data
        if ((mesh.hasColors()) || (mesh.hasNormals()) || (mesh.hasTexCoords()) || (mesh.hasVertices())) {
            data.vboInterleaved = GL.glFactory.createVertexBufferObjectInterleaved();
            data.vboInterleaved.create();
            Renderable.vboBuilder.calculateOffsetsAndStride(mesh, data.vboInterleaved, config);
            Renderable.vboBuilder.createInterleavedBufferData(mesh, data.vboInterleaved.getStrideBytes(),
                    Renderable.byteBuffers, config);
            GL.vboHelper.setBufferData(data.vboInterleaved.getVboID(), Renderable.byteBuffers.getInterleaved(),
                    data.bufferUsage);
        }

        data.hasColors = mesh.hasColors();
        data.hasNormals = mesh.hasNormals();
        data.hasTexCoords = mesh.hasTexCoords();
        data.hasVertices = mesh.hasVertices();

        // Create VBO, byte buffer data and send byte buffer data for indexes
        if (mesh.hasIndexes()) {
            data.vboVertexIndex = GL.glFactory.createVertexBufferObject();
            data.vboVertexIndex.create();
            Renderable.vboBuilder.createIndexBufferData(mesh, Renderable.byteBuffers, config);
            GL.vboHelper.setBufferElementData(data.vboVertexIndex.getVboID(), Renderable.byteBuffers.getVertexIndex(),
                    data.bufferUsage);
            data.hasIndexes = true;
        }

        GL.vboHelper.unbind();

        // Create draw call
        data.drawCall = Renderable.vboBuilder.createDrawCall(mesh, config);
    }

}
