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

import org.lwjgl.opengl.GL11;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class LegacyRenderHelper {

    public void begin(int primitive) {
        GL11.glBegin(primitive);
    }

    public void color(byte r, byte g, byte b) {
        GL11.glColor4ub(r, g, b, (byte) 255);
    }

    public void color(byte r, byte g, byte b, byte a) {
        GL11.glColor4ub(r, g, b, a);
    }

    public void color(float r, float g, float b) {
        GL11.glColor4f(r, g, b, 1.0f);
    }

    public void color(float r, float g, float b, float a) {
        GL11.glColor4f(r, g, b, a);
    }

    public void end() {
        GL11.glEnd();
    }

    public void normal(float x, float y, float z) {
        GL11.glNormal3f(x, y, z);
    }

    public void texCoord(float s, float t) {
        GL11.glTexCoord2f(s, t);
    }

    public void texCoord(float s, float t, float r) {
        GL11.glTexCoord3f(s, t, r);
    }

    public void vertex(float x, float y) {
        GL11.glVertex2f(x, y);
    }

    public void vertex(float x, float y, float z) {
        GL11.glVertex3f(x, y, z);
    }

    public void vertex(float x, float y, float z, float w) {
        GL11.glVertex4f(x, y, z, w);
    }

}
