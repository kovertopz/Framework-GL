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
package net.smert.jreactphysics3d.framework.opengl.constants;

import org.lwjgl.opengl.GL11;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Primitives {

    public final static int LINES = GL11.GL_LINES;
    public final static int LINE_LOOP = GL11.GL_LINE_LOOP;
    public final static int LINE_STRIP = GL11.GL_LINE_STRIP;
    public final static int POINTS = GL11.GL_POINTS;
    public final static int POLYGON = GL11.GL_POLYGON;
    public final static int QUADS = GL11.GL_QUADS;
    public final static int QUAD_STRIP = GL11.GL_QUAD_STRIP;
    public final static int TRIANGLES = GL11.GL_TRIANGLES;
    public final static int TRIANGLE_FAN = GL11.GL_TRIANGLE_FAN;
    public final static int TRIANGLE_STRIP = GL11.GL_TRIANGLE_STRIP;

    private Primitives() {
    }

}
