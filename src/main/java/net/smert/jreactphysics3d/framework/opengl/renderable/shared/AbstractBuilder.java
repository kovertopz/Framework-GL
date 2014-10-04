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

import java.util.Iterator;
import java.util.Map;
import net.smert.jreactphysics3d.framework.opengl.constants.GLTypes;
import net.smert.jreactphysics3d.framework.opengl.mesh.Material;
import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.mesh.Segment;
import net.smert.jreactphysics3d.framework.opengl.renderable.Renderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.RenderableConfiguration;
import net.smert.jreactphysics3d.framework.opengl.texture.TextureType;
import net.smert.jreactphysics3d.framework.opengl.texture.TextureTypeMapping;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public abstract class AbstractBuilder {

    private void createBufferData(Mesh mesh, MultipleBuffers multipleBuffers, RenderableConfiguration config) {

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
                    config.convertColorToByteBuffer(colors, j, multipleBuffers.getColor());
                }
                if (mesh.hasNormals()) {
                    config.convertNormalToByteBuffer(normals, j, multipleBuffers.getNormal());
                }
                if (mesh.hasTexCoords()) {
                    config.convertTexCoordToByteBuffer(texCoords, j, multipleBuffers.getTexCoord());
                }
                if (mesh.hasVertices()) {
                    config.convertVertexToByteBuffer(vertices, j, multipleBuffers.getVertex());
                }
            }
        }
    }

    public void createDrawCall(Mesh mesh, AbstractDrawCall drawCall) {

        int totalSegments = mesh.getTotalSegments();

        // Convert element counts from each segment
        int[] elementCounts = new int[totalSegments];
        for (int i = 0; i < elementCounts.length; i++) {
            elementCounts[i] = mesh.getSegment(i).getElementCount();
        }

        // Convert primitive modes from each segment
        int[] primitiveModes = new int[totalSegments];
        for (int i = 0; i < primitiveModes.length; i++) {
            primitiveModes[i] = mesh.getSegment(i).getPrimitiveMode();
        }

        // Convert shaders for each segment
        int[] shaders = new int[totalSegments];
        for (int i = 0; i < shaders.length; i++) {
            shaders[i] = 0;

            Material material = mesh.getSegment(i).getMaterial();
            if (material == null) {
                continue;
            }
            String shader = material.getShader();
            if (shader == null) {
                continue;
            }

            shaders[i] = Renderable.shaderPool.getUniqueID(shader);
        }

        // Convert textures for each segment
        TextureTypeMapping[][] textureTypeMappings = new TextureTypeMapping[totalSegments][];
        for (int i = 0; i < textureTypeMappings.length; i++) {

            Material material = mesh.getSegment(i).getMaterial();
            if (material == null) {
                continue;
            }

            int j = 0;
            Map<TextureType, String> textures = material.getTextures();
            textureTypeMappings[i] = new TextureTypeMapping[textures.size()];

            Iterator<Map.Entry<TextureType, String>> entries = textures.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<TextureType, String> entry = entries.next();
                TextureType textureType = entry.getKey();
                String filename = entry.getValue();
                int textureTypeID = textureType.ordinal();
                int uniqueTextureID = Renderable.texturePool.getUniqueID(filename);
                textureTypeMappings[i][j++] = new TextureTypeMapping(textureTypeID, uniqueTextureID);
            }
        }

        drawCall.setElementCounts(elementCounts);
        drawCall.setPrimitiveModes(primitiveModes);
        drawCall.setShaders(shaders);
        drawCall.setTextureTypeMappings(textureTypeMappings);
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

        // Set all other buffers to point to the interleaved buffer
        multipleBuffers.setInterleavedBufferToOthers();

        createBufferData(mesh, multipleBuffers, config);

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

        createBufferData(mesh, multipleBuffers, config);

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
