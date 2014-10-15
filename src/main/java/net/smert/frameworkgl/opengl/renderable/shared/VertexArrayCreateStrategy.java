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

import java.nio.ByteBuffer;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderable.RenderableConfiguration;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VertexArrayCreateStrategy {

    public void create(Mesh mesh, VertexArrayData data) {

        // Get configuration
        data.renderableConfigID = mesh.getRenderableConfigID();
        RenderableConfiguration config = Renderable.configPool.get(data.renderableConfigID);

        // Destroy existing vertex array
        data.destroy();

        // Create vertex arrays
        if ((mesh.hasColors()) || (mesh.hasNormals()) || (mesh.hasTexCoords()) || (mesh.hasVertices())) {
            Renderable.vaBuilder.createNonInterleavedBufferData(mesh, Renderable.vertexArrays, config);
        }

        ByteBuffer vertexIndexBuffer = null;

        // Save vertex arrays
        if (mesh.hasColors()) {
            data.vaColor = Renderable.vertexArrays.getColorVertexArray();
        }
        if (mesh.hasNormals()) {
            data.vaNormal = Renderable.vertexArrays.getNormalVertexArray();
        }
        if (mesh.hasTexCoords()) {
            data.vaTexCoord = Renderable.vertexArrays.getTexCoordVertexArray();
        }
        if (mesh.hasVertices()) {
            data.vaVertex = Renderable.vertexArrays.getVertexVertexArray();
        }
        if (mesh.hasIndexes()) {
            Renderable.vboBuilder.createIndexBufferData(mesh, Renderable.vertexArrays, config);
            data.vaVertexIndex = Renderable.vertexArrays.getVertexIndexVertexArray();
            vertexIndexBuffer = data.vaVertexIndex.getByteBuffer();
        }

        // Create draw call
        data.drawCall = Renderable.vaBuilder.createDrawCall(mesh, config, vertexIndexBuffer);
    }

}
