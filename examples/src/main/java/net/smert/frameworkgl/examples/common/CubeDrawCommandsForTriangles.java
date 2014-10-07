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
public class CubeDrawCommandsForTriangles implements DrawCommands {

    @Override
    public void execCommands(Mesh mesh) {

        // Must be in object coordinates
        GL.o1.begin(Primitives.TRIANGLES)
                // Front
                .color(1.0f, 0.0f, 0.0f, 1.0f)
                .vertex(0.5f, 0.5f, 0.5f)
                .vertex(-0.5f, 0.5f, 0.5f)
                .vertex(-0.5f, -0.5f, 0.5f)
                .color(0.5f, 0.0f, 0.0f, 1.0f)
                .vertex(0.5f, 0.5f, 0.5f)
                .vertex(-0.5f, -0.5f, 0.5f)
                .vertex(0.5f, -0.5f, 0.5f)
                // Back
                .color(0.0f, 1.0f, 1.0f, 1.0f)
                .vertex(-0.5f, 0.5f, -0.5f)
                .vertex(0.5f, 0.5f, -0.5f)
                .vertex(0.5f, -0.5f, -0.5f)
                .color(0.0f, 0.5f, 0.5f, 1.0f)
                .vertex(-0.5f, 0.5f, -0.5f)
                .vertex(0.5f, -0.5f, -0.5f)
                .vertex(-0.5f, -0.5f, -0.5f)
                // Left
                .color(0.0f, 1.0f, 0.0f, 1.0f)
                .vertex(-0.5f, 0.5f, 0.5f)
                .vertex(-0.5f, 0.5f, -0.5f)
                .vertex(-0.5f, -0.5f, -0.5f)
                .color(0.0f, 0.5f, 0.0f, 1.0f)
                .vertex(-0.5f, 0.5f, 0.5f)
                .vertex(-0.5f, -0.5f, -0.5f)
                .vertex(-0.5f, -0.5f, 0.5f)
                // Right
                .color(1.0f, 0.0f, 1.0f, 1.0f)
                .vertex(0.5f, 0.5f, -0.5f)
                .vertex(0.5f, 0.5f, 0.5f)
                .vertex(0.5f, -0.5f, 0.5f)
                .color(0.5f, 0.0f, 0.5f, 1.0f)
                .vertex(0.5f, 0.5f, -0.5f)
                .vertex(0.5f, -0.5f, 0.5f)
                .vertex(0.5f, -0.5f, -0.5f)
                // Top
                .color(0.0f, 0.0f, 1.0f, 1.0f)
                .vertex(0.5f, 0.5f, -0.5f)
                .vertex(-0.5f, 0.5f, -0.5f)
                .vertex(-0.5f, 0.5f, 0.5f)
                .color(0.0f, 0.0f, 0.5f, 1.0f)
                .vertex(0.5f, 0.5f, -0.5f)
                .vertex(-0.5f, 0.5f, 0.5f)
                .vertex(0.5f, 0.5f, 0.5f)
                // Bottom
                .color(1.0f, 1.0f, 0.0f, 1.0f)
                .vertex(-0.5f, -0.5f, -0.5f)
                .vertex(0.5f, -0.5f, -0.5f)
                .vertex(0.5f, -0.5f, 0.5f)
                .color(0.5f, 0.5f, 0.0f, 1.0f)
                .vertex(-0.5f, -0.5f, -0.5f)
                .vertex(0.5f, -0.5f, 0.5f)
                .vertex(-0.5f, -0.5f, 0.5f);
        GL.o1.end();
    }

}
