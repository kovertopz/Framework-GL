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

import org.lwjgl.opengl.GL30;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class RenderBufferObjectHelper {

    public void bind(int rboID) {
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, rboID);
    }

    public int create() {
        return GL30.glGenRenderbuffers();
    }

    public void delete(int rboID) {
        GL30.glDeleteRenderbuffers(rboID);
    }

    public void storage(int internalFormat, int width, int height) {
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, internalFormat, width, height);
    }

    public void unbind() {
        GL30.glBindFramebuffer(GL30.GL_RENDERBUFFER, 0);
    }

}
