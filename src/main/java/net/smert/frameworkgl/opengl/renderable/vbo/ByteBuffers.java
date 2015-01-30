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
package net.smert.frameworkgl.opengl.renderable.vbo;

import java.nio.ByteBuffer;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.renderable.shared.MultipleBuffers;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class ByteBuffers implements MultipleBuffers {

    private ByteBuffer color;
    private ByteBuffer interleaved;
    private ByteBuffer normal;
    private ByteBuffer texCoord;
    private ByteBuffer vertex;
    private ByteBuffer vertexIndex;

    @Override
    public void createColor(int bufferSize) {
        if ((color == null) || (color.capacity() < bufferSize)) {
            color = GL.bufferHelper.createByteBuffer(bufferSize);
        }
        color.clear();
    }

    @Override
    public void createInterleaved(int bufferSize) {
        if ((interleaved == null) || (interleaved.capacity() < bufferSize)) {
            interleaved = GL.bufferHelper.createByteBuffer(bufferSize);
        }
        interleaved.clear();
    }

    @Override
    public void createNormal(int bufferSize) {
        if ((normal == null) || (normal.capacity() < bufferSize)) {
            normal = GL.bufferHelper.createByteBuffer(bufferSize);
        }
        normal.clear();
    }

    @Override
    public void createTexCoord(int bufferSize) {
        if ((texCoord == null) || (texCoord.capacity() < bufferSize)) {
            texCoord = GL.bufferHelper.createByteBuffer(bufferSize);
        }
        texCoord.clear();
    }

    @Override
    public void createVertex(int bufferSize) {
        if ((vertex == null) || (vertex.capacity() < bufferSize)) {
            vertex = GL.bufferHelper.createByteBuffer(bufferSize);
        }
        vertex.clear();
    }

    @Override
    public void createVertexIndex(int bufferSize) {
        if ((vertexIndex == null) || (vertexIndex.capacity() < bufferSize)) {
            vertexIndex = GL.bufferHelper.createByteBuffer(bufferSize);
        }
        vertexIndex.clear();
    }

    @Override
    public ByteBuffer getColor() {
        return color;
    }

    @Override
    public ByteBuffer getInterleaved() {
        return interleaved;
    }

    @Override
    public ByteBuffer getNormal() {
        return normal;
    }

    @Override
    public ByteBuffer getTexCoord() {
        return texCoord;
    }

    @Override
    public ByteBuffer getVertex() {
        return vertex;
    }

    @Override
    public ByteBuffer getVertexIndex() {
        return vertexIndex;
    }

}
