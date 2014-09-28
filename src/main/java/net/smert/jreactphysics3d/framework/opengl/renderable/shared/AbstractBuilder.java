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
package net.smert.jreactphysics3d.framework.opengl.renderable.shared;

import java.util.List;
import net.smert.jreactphysics3d.framework.math.Vector3f;
import net.smert.jreactphysics3d.framework.math.Vector4f;
import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.mesh.Segment;
import net.smert.jreactphysics3d.framework.opengl.renderable.factory.RenderableConfiguration;
import net.smert.jreactphysics3d.framework.opengl.renderable.factory.RenderableFactory;
import net.smert.jreactphysics3d.framework.utils.Color;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public abstract class AbstractBuilder {

    private void createBufferData(Mesh mesh, MultipleBuffers multipleBuffers) {

        RenderableConfiguration config = RenderableFactory.config;

        // For each segment in the mesh
        for (int i = 0, max = mesh.getTotalSegments(); i < max; i++) {
            Segment segment = mesh.getSegment(i);

            // For each vertex in the segment
            for (int j = 0, max2 = segment.getVertices().size(); j < max2; j++) {

                // For each type convert the data and add to the byte buffer
                if (mesh.hasColors()) {
                    Color color = segment.getColors().get(j);
                    config.convertColorToByteBuffer(color, multipleBuffers.getColor());
                }
                if (mesh.hasNormals()) {
                    Vector3f normal = segment.getNormals().get(j);
                    config.convertNormalToByteBuffer(normal, multipleBuffers.getNormal());
                }
                if (mesh.hasTexCoords()) {
                    Vector3f texCoord = segment.getTexCoords().get(j);
                    config.convertTexCoordToByteBuffer(texCoord, multipleBuffers.getTexCoord());
                }
                if (mesh.hasVertices()) {
                    Vector4f vertex = segment.getVertices().get(j);
                    config.convertVertexToByteBuffer(vertex, multipleBuffers.getVertex());
                }
            }
        }
    }

    public void createIndexBufferData(Mesh mesh, MultipleBuffers multipleBuffers) {

        RenderableConfiguration config = RenderableFactory.config;

        // Index byte buffer
        int byteSize = config.convertGLTypeToByteSize(config.getIndexType());
        int bufferSize = byteSize * mesh.getIndexes().size();
        multipleBuffers.createVertexIndex(bufferSize);

        // Convert list to primitive array
        List<Integer> indexes = mesh.getIndexes();
        for (Integer integer : indexes) {
            multipleBuffers.getVertexIndex().putInt(integer);
        }
        multipleBuffers.getVertexIndex().flip();
    }

    public void createInterleavedBufferData(Mesh mesh, int strideBytes, MultipleBuffers multipleBuffers) {

        // Create byte buffer to hold color, normal, texture coordinates and vertex data
        int bufferSize = strideBytes * mesh.getTotalVerticies();
        multipleBuffers.createInterleaved(bufferSize);

        // Set all other buffers to point to the interleaved buffer
        multipleBuffers.setInterleavedBufferToOthers();

        createBufferData(mesh, multipleBuffers);

        // Missy Elliott that shit
        multipleBuffers.getInterleaved().flip();
    }

    public void createNonInterleavedBufferData(Mesh mesh, MultipleBuffers multipleBuffers) {

        int bufferSize, byteSize;
        RenderableConfiguration config = RenderableFactory.config;

        // Color byte buffer
        if (mesh.hasColors()) {
            byteSize = config.convertGLTypeToByteSize(config.getColorType());
            bufferSize = byteSize * config.getColorSize() * mesh.getTotalVerticies();
            multipleBuffers.createColor(bufferSize);
        }

        // Normal byte buffer
        if (mesh.hasNormals()) {
            byteSize = config.convertGLTypeToByteSize(config.getNormalType());
            bufferSize = byteSize * config.getNormalSize() * mesh.getTotalVerticies();
            multipleBuffers.createNormal(bufferSize);
        }

        // Texture coordinate byte buffer
        if (mesh.hasTexCoords()) {
            byteSize = config.convertGLTypeToByteSize(config.getTexCoordType());
            bufferSize = byteSize * config.getTexCoordSize() * mesh.getTotalVerticies();
            multipleBuffers.createTexCoord(bufferSize);
        }

        // Vertex byte buffer
        if (mesh.hasVertices()) {
            byteSize = config.convertGLTypeToByteSize(config.getVertexType());
            bufferSize = byteSize * config.getVertexSize() * mesh.getTotalVerticies();
            multipleBuffers.createVertex(bufferSize);
        }

        createBufferData(mesh, multipleBuffers);

        if (mesh.hasColors()) {
            multipleBuffers.getColor().flip();
        }
        if (mesh.hasNormals()) {
            multipleBuffers.getNormal().flip();
        }
        if (mesh.hasTexCoords()) {
            multipleBuffers.getTexCoord().flip();
        }
        if (mesh.hasVertices()) {
            multipleBuffers.getVertex().flip();
        }
    }

}
