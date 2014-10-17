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
import net.smert.frameworkgl.opengl.renderable.shared.DrawCommands;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class CubeDrawCommandsForQuadsWithPerVertexColors implements DrawCommands {

    @Override
    public void execCommands(Mesh mesh) {

        // Must be in object coordinates
        GL.o1.begin(Primitives.QUADS)
                // Front
                .color(1f, 0f, 0f, 1f)
                .vertex(.5f, .5f, .5f)
                .color(0f, 1f, 0f, 1f)
                .vertex(-.5f, .5f, .5f)
                .color(0f, 1f, 1f, 1f)
                .vertex(-.5f, -.5f, .5f)
                .color(1f, 0f, 1f, 1f)
                .vertex(.5f, -.5f, .5f)
                // Back
                .color(1f, 0f, 0f, 1f)
                .vertex(-.5f, .5f, -.5f)
                .color(0f, 1f, 0f, 1f)
                .vertex(.5f, .5f, -.5f)
                .color(0f, 1f, 1f, 1f)
                .vertex(.5f, -.5f, -.5f)
                .color(1f, 0f, 1f, 1f)
                .vertex(-.5f, -.5f, -.5f)
                // Left
                .color(0f, 1f, 0f, 1f)
                .vertex(-.5f, .5f, .5f)
                .color(1f, 0f, 0f, 1f)
                .vertex(-.5f, .5f, -.5f)
                .color(1f, 0f, 1f, 1f)
                .vertex(-.5f, -.5f, -.5f)
                .color(0f, 1f, 1f, 1f)
                .vertex(-.5f, -.5f, .5f)
                // Right
                .color(0f, 1f, 0f, 1f)
                .vertex(.5f, .5f, -.5f)
                .color(1f, 0f, 0f, 1f)
                .vertex(.5f, .5f, .5f)
                .color(1f, 0f, 1f, 1f)
                .vertex(.5f, -.5f, .5f)
                .color(0f, 1f, 1f, 1f)
                .vertex(.5f, -.5f, -.5f)
                // Top
                .color(0f, 1f, 0f, 1f)
                .vertex(.5f, .5f, -.5f)
                .color(1f, 0f, 0f, 1f)
                .vertex(-.5f, .5f, -.5f)
                .color(0f, 1f, 0f, 1f)
                .vertex(-.5f, .5f, .5f)
                .color(1f, 0f, 0f, 1f)
                .vertex(.5f, .5f, .5f)
                // Bottom
                .color(1f, 0f, 1f, 1f)
                .vertex(-.5f, -.5f, -.5f)
                .color(0f, 1f, 1f, 1f)
                .vertex(.5f, -.5f, -.5f)
                .color(1f, 0f, 1f, 1f)
                .vertex(.5f, -.5f, .5f)
                .color(0f, 1f, 1f, 1f)
                .vertex(-.5f, -.5f, .5f);
        GL.o1.end();
    }

}
