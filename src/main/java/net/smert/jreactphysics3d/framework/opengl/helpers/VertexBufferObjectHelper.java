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
package net.smert.jreactphysics3d.framework.opengl.helpers;

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

    public void bindColors(int vboid, int size, int type, int stridebytes, int offsetbytes) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboid);
        GL11.glColorPointer(size, type, stridebytes, offsetbytes);
    }

    public void bindNormals(int vboid, int type, int stridebytes, int offsetbytes) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboid);
        GL11.glNormalPointer(type, stridebytes, offsetbytes);
    }

    public void bindVertices(int vboid, int size, int type, int stridebytes, int offsetbytes) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboid);
        GL11.glVertexPointer(size, type, stridebytes, offsetbytes);
    }

    public void bindVerticesIndex(int vboid) {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboid);
    }

    public void bindTextureCoordinates(int vboid, int size, int type, int stridebytes, int offsetbytes) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboid);
        GL11.glTexCoordPointer(size, type, stridebytes, offsetbytes);
    }

    public int create() {
        return GL15.glGenBuffers();
    }

    public void delete(int vboid) {
        GL15.glDeleteBuffers(vboid);
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

    public void drawRangeElements(int mode, int start, int end, int count, int type) {
        GL12.glDrawRangeElements(mode, start, end, count, type, 0);
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

    public void setBufferData(int vboid, ByteBuffer bytebuffer, int usage) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboid);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, bytebuffer, usage);
    }

    public void setBufferData(int vboid, FloatBuffer floatbuffer, int usage) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboid);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatbuffer, usage);
    }

    public void setBufferData(int vboid, IntBuffer intbuffer, int usage) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboid);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, intbuffer, usage);
    }

    public void setBufferData(int vboid, ShortBuffer shortbuffer, int usage) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboid);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, shortbuffer, usage);
    }

    public void setBufferElementData(int vboid, ByteBuffer byteBuffer, int usage) {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboid);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, byteBuffer, usage);
    }

    public void setBufferElementData(int vboid, IntBuffer intbuffer, int usage) {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboid);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, intbuffer, usage);
    }

    public void setBufferElementData(int vboid, ShortBuffer shortbuffer, int usage) {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboid);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, shortbuffer, usage);
    }

    public void unbind() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

}
