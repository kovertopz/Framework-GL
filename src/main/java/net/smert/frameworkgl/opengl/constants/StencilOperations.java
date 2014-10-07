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
public class StencilOperations {

    public final static int BACK = GL11.GL_BACK;
    public final static int FRONT = GL11.GL_FRONT;
    public final static int FRONT_AND_BACK = GL11.GL_FRONT_AND_BACK;
    public final static int DECR = GL11.GL_DECR;
    public final static int DECR_WRAP = GL14.GL_DECR_WRAP;
    public final static int INCR = GL11.GL_INCR;
    public final static int INCR_WRAP = GL14.GL_INCR_WRAP;
    public final static int INVERT = GL11.GL_INVERT;
    public final static int KEEP = GL11.GL_KEEP;
    public final static int REPLACE = GL11.GL_REPLACE;
    public final static int ZERO = GL11.GL_ZERO;

    private StencilOperations() {
    }

}
