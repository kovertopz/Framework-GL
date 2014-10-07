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
package net.smert.frameworkgl.opengl.constants;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class TextureTypes {

    public final static int FLOAT = GL11.GL_FLOAT;
    public final static int UNSIGNED_BYTE = GL11.GL_UNSIGNED_BYTE;
    public final static int UNSIGNED_INT = GL11.GL_UNSIGNED_INT;
    public final static int UNSIGNED_INT_10_10_10_2 = GL12.GL_UNSIGNED_INT_10_10_10_2;
    public final static int UNSIGNED_INT_24_8 = GL30.GL_UNSIGNED_INT_24_8;
    public final static int UNSIGNED_SHORT = GL11.GL_UNSIGNED_SHORT;

    private TextureTypes() {
    }

}
