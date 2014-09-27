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
public class DisplayListHelper {

    public void begin(int id) {
        GL11.glNewList(id, GL11.GL_COMPILE);
    }

    public void call(int id) {
        GL11.glCallList(id);
    }

    public int create() {
        return GL11.glGenLists(1);
    }

    public void delete(int id) {
        GL11.glDeleteLists(id, 1);
    }

    public void end() {
        GL11.glEndList();
    }

}
