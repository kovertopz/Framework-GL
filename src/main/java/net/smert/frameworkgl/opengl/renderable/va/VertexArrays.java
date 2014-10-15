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
package net.smert.frameworkgl.opengl.renderable.va;

import java.nio.ByteBuffer;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.VertexArray;
import net.smert.frameworkgl.opengl.renderable.shared.MultipleBuffers;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VertexArrays implements MultipleBuffers {

    private ByteBuffer colorByteBuffer;
    private ByteBuffer normalByteBuffer;
    private ByteBuffer texCoordByteBuffer;
    private ByteBuffer vertexByteBuffer;
    private ByteBuffer vertexIndexByteBuffer;
    private VertexArray color;
    private VertexArray normal;
    private VertexArray texCoord;
    private VertexArray vertex;
    private VertexArray vertexIndex;

    @Override
    public void createColor(int bufferSize) {
        color = GL.glFactory.createVertexArray();
        color.create(bufferSize);
        colorByteBuffer = color.getByteBuffer();
    }

    @Override
    public void createInterleaved(int bufferSize) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void createNormal(int bufferSize) {
        normal = GL.glFactory.createVertexArray();
        normal.create(bufferSize);
        normalByteBuffer = normal.getByteBuffer();
    }

    @Override
    public void createTexCoord(int bufferSize) {
        texCoord = GL.glFactory.createVertexArray();
        texCoord.create(bufferSize);
        texCoordByteBuffer = texCoord.getByteBuffer();
    }

    @Override
    public void createVertex(int bufferSize) {
        vertex = GL.glFactory.createVertexArray();
        vertex.create(bufferSize);
        vertexByteBuffer = vertex.getByteBuffer();
    }

    @Override
    public void createVertexIndex(int bufferSize) {
        vertexIndex = GL.glFactory.createVertexArray();
        vertexIndex.create(bufferSize);
        vertexIndexByteBuffer = vertexIndex.getByteBuffer();
    }

    @Override
    public ByteBuffer getColor() {
        return colorByteBuffer;
    }

    @Override
    public ByteBuffer getInterleaved() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public ByteBuffer getNormal() {
        return normalByteBuffer;
    }

    @Override
    public ByteBuffer getTexCoord() {
        return texCoordByteBuffer;
    }

    @Override
    public ByteBuffer getVertex() {
        return vertexByteBuffer;
    }

    @Override
    public ByteBuffer getVertexIndex() {
        return vertexIndexByteBuffer;
    }

    public VertexArray getColorVertexArray() {
        return color;
    }

    public VertexArray getNormalVertexArray() {
        return normal;
    }

    public VertexArray getTexCoordVertexArray() {
        return texCoord;
    }

    public VertexArray getVertexVertexArray() {
        return vertex;
    }

    public VertexArray getVertexIndexVertexArray() {
        return vertexIndex;
    }

}
