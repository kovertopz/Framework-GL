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
package net.smert.jreactphysics3d.examples.common;

import net.smert.jreactphysics3d.framework.opengl.GL;
import net.smert.jreactphysics3d.framework.opengl.constants.Primitives;
import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.renderable.gl1.DrawCommands;

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
                .color(1.0f, 0.0f, 0.0f, 1.0f) // Red
                .vertex(-1.0f, 0.5f, 0.5f)
                .color(0.0f, 1.0f, 0.0f, 1.0f) // Green
                .vertex(-1.0f, -0.5f, 0.5f)
                .color(0.0f, 0.0f, 1.0f, 1.0f) // Blue
                .vertex(0.0f, 0.5f, 0.5f)
                .color(1.0f, 1.0f, 0.0f, 1.0f) // Yellow
                .vertex(0.0f, -0.5f, 0.5f)
                .color(0.0f, 1.0f, 1.0f, 1.0f) // Cyan
                .vertex(1.0f, 0.5f, 0.5f)
                .color(1.0f, 0.0f, 1.0f, 1.0f) // Purple
                .vertex(1.0f, -0.5f, 0.5f);
        GL.o1.end();

        GL.o1.begin(Primitives.TRIANGLE_FAN)
                .color(1.0f, 0.0f, 0.0f, 1.0f) // Red
                .vertex(0.0f, -1.5f, 0.5f)
                .color(0.0f, 1.0f, 0.0f, 1.0f) // Green
                .vertex(-2.0f, -2.2f, 0.5f)
                .color(0.0f, 0.0f, 1.0f, 1.0f) // Blue
                .vertex(-1.0f, -2.7f, 0.5f)
                .color(1.0f, 1.0f, 0.0f, 1.0f) // Yellow
                .vertex(0.0f, -2.9f, 0.5f)
                .color(0.0f, 1.0f, 1.0f, 1.0f) // Cyan
                .vertex(1.0f, -2.7f, 0.5f)
                .color(1.0f, 0.0f, 1.0f, 1.0f) // Purple
                .vertex(2.0f, -2.2f, 0.5f);
        GL.o1.end();

        GL.o1.begin(Primitives.TRIANGLE_STRIP)
                .color(1.0f, 0.0f, 0.0f, 1.0f) // Red
                .vertex(-1.0f, 2.5f, 0.5f)
                .color(0.0f, 1.0f, 0.0f, 1.0f) // Green
                .vertex(-1.0f, 1.5f, 0.5f)
                .color(0.0f, 0.0f, 1.0f, 1.0f) // Blue
                .vertex(0.0f, 2.5f, 0.5f)
                .color(1.0f, 1.0f, 0.0f, 1.0f) // Yellow
                .vertex(0.0f, 1.5f, 0.5f)
                .color(0.0f, 1.0f, 1.0f, 1.0f) // Cyan
                .vertex(1.0f, 2.5f, 0.5f)
                .color(1.0f, 0.0f, 1.0f, 1.0f) // Purple
                .vertex(1.0f, 1.5f, 0.5f);
        GL.o1.end();
    }

}
