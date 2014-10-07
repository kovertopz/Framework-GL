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

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class StencilFunctions {

    public final static int ALWAYS = GL11.GL_ALWAYS;
    public final static int EQUAL = GL11.GL_EQUAL;
    public final static int GREATER = GL11.GL_GREATER;
    public final static int GEQUAL = GL11.GL_GEQUAL;
    public final static int LESS = GL11.GL_LESS;
    public final static int LEQUAL = GL11.GL_LEQUAL;
    public final static int NEVER = GL11.GL_NEVER;
    public final static int NOTEQUAL = GL11.GL_NOTEQUAL;

    private StencilFunctions() {
    }

}
