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
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VertexArrayHelper {

    public void bindColors(int size, int type, ByteBuffer byteBuffer) {
        GL11.glColorPointer(size, type, 0, byteBuffer);
    }

    public void bindNormals(int type, ByteBuffer byteBuffer) {
        GL11.glNormalPointer(type, 0, byteBuffer);
    }

    public void bindVertexAttrib(int index, int size, int type, ByteBuffer byteBuffer) {
        GL20.glVertexAttribPointer(index, size, type, false, 0, byteBuffer);
    }

    public void bindVertexAttribNormalized(int index, int size, int type, ByteBuffer byteBuffer) {
        GL20.glVertexAttribPointer(index, size, type, true, 0, byteBuffer);
    }

    public void bindVertices(int size, int type, ByteBuffer byteBuffer) {
        GL11.glVertexPointer(size, type, 0, byteBuffer);
    }

    public void bindTexCoords(int size, int type, ByteBuffer byteBuffer) {
        GL11.glTexCoordPointer(size, type, 0, byteBuffer);
    }

    public void disableColors() {
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
    }

    public void disableNormals() {
        GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
    }

    public void disableTexCoords() {
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
    }

    public void disableVertexAttribArray(int index) {
        GL20.glDisableVertexAttribArray(index);
    }

    public void disableVertices() {
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
    }

    public void drawArrays(int mode, int first, int count) {
        GL11.glDrawArrays(mode, first, count);
    }

    public void drawElements(int mode, int count, int type, long offsetBytes) {
        GL11.glDrawElements(mode, count, type, offsetBytes);
    }

    public void drawElements(int mode, int count, ByteBuffer buffer) {
        GL11.glDrawElements(mode, count, buffer);
    }

    public void drawElements(int mode, ByteBuffer buffer) {
        GL11.glDrawElements(mode, buffer);
    }

    public void drawElements(int mode, ShortBuffer buffer) {
        GL11.glDrawElements(mode, buffer);
    }

    public void drawElements(int mode, IntBuffer buffer) {
        GL11.glDrawElements(mode, buffer);
    }

    public void enableColors() {
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
    }

    public void enableNormals() {
        GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
    }

    public void enableTexCoords() {
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
    }

    public void enableVertexAttribArray(int index) {
        GL20.glEnableVertexAttribArray(index);
    }

    public void enableVertices() {
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
    }

}
