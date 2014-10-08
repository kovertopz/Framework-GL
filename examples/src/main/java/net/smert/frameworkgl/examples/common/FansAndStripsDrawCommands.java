/**
 * Copyright 2014 Jason Sorensen (sorensenj@smert.net)
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
package net.smert.frameworkgl.examples.common;

import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.constants.Primitives;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.gl1.DrawCommands;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class FansAndStripsDrawCommands implements DrawCommands {

    @Override
    public void execCommands(Mesh mesh) {

        GL.o1.setPolygonModeFrontAndBackLine();

        // Must be in object coordinates
        GL.o1.begin(Primitives.QUAD_STRIP)
                .color(1f, 0f, 0f, 1f) // Red
                .vertex(-1f, .5f, .5f)
                .color(0f, 1f, 0f, 1f) // Green
                .vertex(-1f, -.5f, .5f)
                .color(0f, 0f, 1f, 1f) // Blue
                .vertex(0f, .5f, .5f)
                .color(1f, 1f, 0f, 1f) // Yellow
                .vertex(0f, -.5f, .5f)
                .color(0f, 1f, 1f, 1f) // Cyan
                .vertex(1f, .5f, .5f)
                .color(1f, 0f, 1f, 1f) // Purple
                .vertex(1f, -.5f, .5f);
        GL.o1.end();

        GL.o1.begin(Primitives.TRIANGLE_FAN)
                .color(1f, 0f, 0f, 1f) // Red
                .vertex(0f, -1.5f, .5f)
                .color(0f, 1f, 0f, 1f) // Green
                .vertex(-2f, -2.2f, .5f)
                .color(0f, 0f, 1f, 1f) // Blue
                .vertex(-1f, -2.7f, .5f)
                .color(1f, 1f, 0f, 1f) // Yellow
                .vertex(0f, -2.9f, .5f)
                .color(0f, 1f, 1f, 1f) // Cyan
                .vertex(1f, -2.7f, .5f)
                .color(1f, 0f, 1f, 1f) // Purple
                .vertex(2f, -2.2f, .5f);
        GL.o1.end();

        GL.o1.begin(Primitives.TRIANGLE_STRIP)
                .color(1f, 0f, 0f, 1f) // Red
                .vertex(-1f, 2.5f, .5f)
                .color(0f, 1f, 0f, 1f) // Green
                .vertex(-1f, 1.5f, .5f)
                .color(0f, 0f, 1f, 1f) // Blue
                .vertex(0f, 2.5f, .5f)
                .color(1f, 1f, 0f, 1f) // Yellow
                .vertex(0f, 1.5f, .5f)
                .color(0f, 1f, 1f, 1f) // Cyan
                .vertex(1f, 2.5f, .5f)
                .color(1f, 0f, 1f, 1f) // Purple
                .vertex(1f, 1.5f, .5f);
        GL.o1.end();
    }

}
