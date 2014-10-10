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
package net.smert.frameworkgl.opengl.helpers;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VertexBufferObjectHelper {

    public void bindColors(int vboID, int size, int type, int strideBytes, int offsetBytes) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL11.glColorPointer(size, type, strideBytes, offsetBytes);
    }

    public void bindNormals(int vboID, int type, int strideBytes, int offsetBytes) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL11.glNormalPointer(type, strideBytes, offsetBytes);
    }

    public void bindVertices(int vboID, int size, int type, int strideBytes, int offsetBytes) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL11.glVertexPointer(size, type, strideBytes, offsetBytes);
    }

    public void bindVerticesIndex(int vboID) {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
    }

    public void bindTextureCoordinates(int vboID, int size, int type, int strideBytes, int offsetBytes) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL11.glTexCoordPointer(size, type, strideBytes, offsetBytes);
    }

    public int create() {
        return GL15.glGenBuffers();
    }

    public void delete(int vboID) {
        GL15.glDeleteBuffers(vboID);
    }

    public void disableColors() {
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
    }

    public void disableNormals() {
        GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
    }

    public void disableTextureCoordinates() {
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
    }

    public void disableVertices() {
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
    }

    public void drawArrays(int mode, int first, int count) {
        GL11.glDrawArrays(mode, first, count);
    }

    public void drawElements(int mode, int count, int type) {
        GL11.glDrawElements(mode, count, type, 0);
    }

    public void drawRangeElements(int mode, int minIndex, int maxIndex, int count, int type) {
        GL12.glDrawRangeElements(mode, minIndex, maxIndex, count, type, 0);
    }

    public void enableColors() {
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
    }

    public void enableNormals() {
        GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
    }

    public void enableTextureCoordinates() {
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
    }

    public void enableVertices() {
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
    }

    public void setBufferData(int vboID, ByteBuffer byteBuffer, int usage) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, byteBuffer, usage);
    }

    public void setBufferData(int vboID, FloatBuffer floatBuffer, int usage) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatBuffer, usage);
    }

    public void setBufferData(int vboID, IntBuffer intBuffer, int usage) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, intBuffer, usage);
    }

    public void setBufferData(int vboID, ShortBuffer shortBuffer, int usage) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, shortBuffer, usage);
    }

    public void setBufferElementData(int vboID, ByteBuffer byteBuffer, int usage) {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, byteBuffer, usage);
    }

    public void setBufferElementData(int vboID, IntBuffer intBuffer, int usage) {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, intBuffer, usage);
    }

    public void setBufferElementData(int vboID, ShortBuffer shortBuffer, int usage) {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, shortBuffer, usage);
    }

    public void unbind() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void updateBufferData(int vboID, int offsetBytes, ByteBuffer byteBuffer) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offsetBytes, byteBuffer);
    }

    public void updateBufferData(int vboID, int offsetBytes, FloatBuffer floatBuffer) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offsetBytes, floatBuffer);
    }

    public void updateBufferData(int vboID, int offsetBytes, IntBuffer intBuffer) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offsetBytes, intBuffer);
    }

    public void updateBufferData(int vboID, int offsetBytes, ShortBuffer shortBuffer) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offsetBytes, shortBuffer);
    }

    public void updateBufferElementData(int vboID, int offsetBytes, ByteBuffer byteBuffer) {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        GL15.glBufferSubData(GL15.GL_ELEMENT_ARRAY_BUFFER, offsetBytes, byteBuffer);
    }

    public void updateBufferElementData(int vboID, int offsetBytes, IntBuffer intBuffer) {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        GL15.glBufferSubData(GL15.GL_ELEMENT_ARRAY_BUFFER, offsetBytes, intBuffer);
    }

    public void updateBufferElementData(int vboID, int offsetBytes, ShortBuffer shortBuffer) {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        GL15.glBufferSubData(GL15.GL_ELEMENT_ARRAY_BUFFER, offsetBytes, shortBuffer);
    }

}
