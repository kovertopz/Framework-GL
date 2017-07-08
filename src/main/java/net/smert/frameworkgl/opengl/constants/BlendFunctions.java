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
import org.lwjgl.opengl.GL14;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class BlendFunctions {

    public final static int CONSTANT_ALPHA = GL14.GL_CONSTANT_ALPHA;
    public final static int CONSTANT_COLOR = GL14.GL_CONSTANT_COLOR;
    public final static int DST_ALPHA = GL11.GL_DST_ALPHA;
    public final static int DST_COLOR = GL11.GL_DST_COLOR;
    public final static int ONE = GL11.GL_ONE;
    public final static int ONE_MINUS_CONSTANT_ALPHA = GL14.GL_ONE_MINUS_CONSTANT_ALPHA;
    public final static int ONE_MINUS_CONSTANT_COLOR = GL14.GL_ONE_MINUS_CONSTANT_COLOR;
    public final static int ONE_MINUS_DST_ALPHA = GL11.GL_ONE_MINUS_DST_ALPHA;
    public final static int ONE_MINUS_DST_COLOR = GL11.GL_ONE_MINUS_DST_COLOR;
    public final static int ONE_MINUS_SRC_ALPHA = GL11.GL_ONE_MINUS_SRC_ALPHA;
    public final static int ONE_MINUS_SRC_COLOR = GL11.GL_ONE_MINUS_SRC_COLOR;
    public final static int SRC_ALPHA = GL11.GL_SRC_ALPHA;
    public final static int SRC_ALPHA_SATURATE = GL11.GL_SRC_ALPHA_SATURATE;
    public final static int SRC_COLOR = GL11.GL_SRC_COLOR;
    public final static int ZERO = GL11.GL_ZERO;

    private BlendFunctions() {
    }

}
