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
import net.smert.frameworkgl.opengl.VertexBufferObjectInterleaved;
import net.smert.frameworkgl.opengl.constants.GLTypes;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.mesh.Segment;
import net.smert.frameworkgl.opengl.renderable.RenderableConfiguration;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class RenderableBuilder {

    private void createBufferData(Mesh mesh, RenderableConfiguration config, ByteBuffer colorByteBuffer,
            ByteBuffer normalByteBuffer, ByteBuffer texCoordByteBuffer, ByteBuffer vertexByteBuffer) {

        // For each segment in the mesh
        for (int i = 0; i < mesh.getTotalSegments(); i++) {
            Segment segment = mesh.getSegment(i);

            float[] colors = segment.getColors();
            float[] normals = segment.getNormals();
            float[] texCoords = segment.getTexCoords();
            float[] vertices = segment.getVertices();

            // For each vertex in the segment
            for (int j = 0; j < segment.getElementCount(); j++) {

                // For each type convert the data and add to the byte buffer
                if (mesh.hasColors()) {
                    config.convertColorToByteBuffer(colors, j, colorByteBuffer);
                }
                if (mesh.hasNormals()) {
                    config.convertNormalToByteBuffer(normals, j, normalByteBuffer);
                }
                if (mesh.hasTexCoords()) {
                    config.convertTexCoordToByteBuffer(texCoords, j, texCoordByteBuffer);
                }
                if (mesh.hasVertices()) {
                    config.convertVertexToByteBuffer(vertices, j, vertexByteBuffer);
                }
            }
        }
    }

    public void calculateOffsetsAndStride(Mesh mesh, VertexBufferObjectInterleaved vboInterleaved,
            RenderableConfiguration config) {

        int total = 0;

        // Calculate byte size of each type and add to the total. Save the total as
        // the current offset before increasing it.
        if (mesh.hasColors()) {
            vboInterleaved.setColorOffsetBytes(total);
            int byteSize = config.convertGLTypeToByteSize(config.getColorType());
            total += config.getColorSize() * byteSize;
        }
        if (mesh.hasNormals()) {
            vboInterleaved.setNormalOffsetBytes(total);
            int byteSize = config.convertGLTypeToByteSize(config.getNormalType());
            total += config.getNormalSize() * byteSize;
        }
        if (mesh.hasTexCoords()) {
            vboInterleaved.setTexCoordOffsetBytes(total);
            int byteSize = config.convertGLTypeToByteSize(config.getTexCoordType());
            total += config.getTexCoordSize() * byteSize;
        }
        if (mesh.hasVertices()) {
            vboInterleaved.setVertexOffsetBytes(total);
            int byteSize = config.convertGLTypeToByteSize(config.getVertexType());
            total += config.getVertexSize() * byteSize;
        }

        vboInterleaved.setStrideBytes(total);
    }

    public void createIndexBufferData(Mesh mesh, MultipleBuffers multipleBuffers, RenderableConfiguration config) {

        // Index byte buffer
        int byteSize = config.convertGLTypeToByteSize(config.getIndexType());
        int bufferSize = byteSize * mesh.getIndexes().length;
        multipleBuffers.createVertexIndex(bufferSize);

        // Fill byte buffer with indexes
        int[] indexes = mesh.getIndexes();
        for (int i = 0; i < indexes.length; i++) {
            int index = indexes[i];

            switch (config.getIndexType()) {
                case GLTypes.UNSIGNED_INT:
                    multipleBuffers.getVertexIndex().putInt(index);
                    break;

                case GLTypes.UNSIGNED_SHORT:
                    multipleBuffers.getVertexIndex().putShort((short) index);
                    break;

                default:
                    throw new IllegalArgumentException("Unknown GL type constant for indexes: " + config.getIndexType());
            }
        }

        // Don't forget to flip
        multipleBuffers.getVertexIndex().flip();
    }

    public void createInterleavedBufferData(Mesh mesh, int strideBytes, MultipleBuffers multipleBuffers,
            RenderableConfiguration config) {

        // Create byte buffer to hold color, normal, texture coordinates and vertex data
        int bufferSize = strideBytes * mesh.getTotalVerticies();
        multipleBuffers.createInterleaved(bufferSize);

        createBufferData(mesh, config, multipleBuffers.getInterleaved(), multipleBuffers.getInterleaved(),
                multipleBuffers.getInterleaved(), multipleBuffers.getInterleaved());

        // Missy Elliott that shit
        multipleBuffers.getInterleaved().flip();
    }

    public void createNonInterleavedBufferData(Mesh mesh, MultipleBuffers multipleBuffers,
            RenderableConfiguration config) {

        int bufferSize, byteSize;

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

        createBufferData(mesh, config, multipleBuffers.getColor(), multipleBuffers.getNormal(),
                multipleBuffers.getTexCoord(), multipleBuffers.getVertex());

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
