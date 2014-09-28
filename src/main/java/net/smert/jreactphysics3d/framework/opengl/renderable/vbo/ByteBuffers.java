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
package net.smert.jreactphysics3d.framework.opengl.renderable.vbo;

import java.nio.ByteBuffer;
import net.smert.jreactphysics3d.framework.opengl.renderable.shared.MultipleBuffers;
import org.lwjgl.BufferUtils;

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
        color = BufferUtils.createByteBuffer(bufferSize);
    }

    @Override
    public void createInterleaved(int bufferSize) {
        interleaved = BufferUtils.createByteBuffer(bufferSize);
    }

    @Override
    public void createNormal(int bufferSize) {
        normal = BufferUtils.createByteBuffer(bufferSize);
    }

    @Override
    public void createTexCoord(int bufferSize) {
        texCoord = BufferUtils.createByteBuffer(bufferSize);
    }

    @Override
    public void createVertex(int bufferSize) {
        vertex = BufferUtils.createByteBuffer(bufferSize);
    }

    @Override
    public void createVertexIndex(int bufferSize) {
        vertexIndex = BufferUtils.createByteBuffer(bufferSize);
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

    @Override
    public void reset() {
        color = null;
        interleaved = null;
        normal = null;
        texCoord = null;
        vertex = null;
        vertexIndex = null;
    }

    @Override
    public void setInterleavedBufferToOthers() {
        color = normal = texCoord = vertex = interleaved;
    }

}
