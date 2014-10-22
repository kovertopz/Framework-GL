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
package net.smert.frameworkgl.opengl;

import net.smert.frameworkgl.opengl.constants.StencilOperations;
import org.lwjgl.opengl.GL20;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class OpenGL2 {

    public void setStencilOpSeparateBackKeepIncrKeep() {
        GL20.glStencilOpSeparate(StencilOperations.BACK, StencilOperations.KEEP, StencilOperations.INCR, StencilOperations.KEEP);
    }

    public void setStencilOpSeparateFrontKeepDecrKeep() {
        GL20.glStencilOpSeparate(StencilOperations.FRONT, StencilOperations.KEEP, StencilOperations.DECR, StencilOperations.KEEP);
    }

    public void vertexAttrib(int index, float x) {
        GL20.glVertexAttrib1f(index, x);
    }

    public void vertexAttrib(int index, float x, float y) {
        GL20.glVertexAttrib2f(index, x, y);
    }

    public void vertexAttrib(int index, float x, float y, float z) {
        GL20.glVertexAttrib3f(index, x, y, z);
    }

    public void vertexAttrib(int index, byte x, byte y, byte z, byte w) {
        GL20.glVertexAttrib4Nub(index, x, y, z, w);
    }

    public void vertexAttrib(int index, float x, float y, float z, float w) {
        GL20.glVertexAttrib4f(index, x, y, z, w);
    }

}
