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

import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.constants.Primitives;
import net.smert.frameworkgl.opengl.mesh.Mesh;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class CubeMeshForQuadsWithPerVertexColors extends Mesh {

    public CubeMeshForQuadsWithPerVertexColors() {
        super();
        addSegment();
    }

    private void addSegment() {
        GL.tessellator.reset();
        GL.tessellator.setConvertToTriangles(false);
        GL.tessellator.start(Primitives.QUADS);

        // Front
        GL.tessellator.addColor(1f, 0f, 0f);
        GL.tessellator.addVertex(.5f, .5f, .5f);
        GL.tessellator.addColor(0f, 1f, 0f);
        GL.tessellator.addVertex(-.5f, .5f, .5f);
        GL.tessellator.addColor(0f, 1f, 1f);
        GL.tessellator.addVertex(-.5f, -.5f, .5f);
        GL.tessellator.addColor(1f, 0f, 1f);
        GL.tessellator.addVertex(.5f, -.5f, .5f);

        // Back
        GL.tessellator.addColor(1f, 0f, 0f);
        GL.tessellator.addVertex(-.5f, .5f, -.5f);
        GL.tessellator.addColor(0f, 1f, 0f);
        GL.tessellator.addVertex(.5f, .5f, -.5f);
        GL.tessellator.addColor(0f, 1f, 1f);
        GL.tessellator.addVertex(.5f, -.5f, -.5f);
        GL.tessellator.addColor(1f, 0f, 1f);
        GL.tessellator.addVertex(-.5f, -.5f, -.5f);

        // Left
        GL.tessellator.addColor(0f, 1f, 0f);
        GL.tessellator.addVertex(-.5f, .5f, .5f);
        GL.tessellator.addColor(1f, 0f, 0f);
        GL.tessellator.addVertex(-.5f, .5f, -.5f);
        GL.tessellator.addColor(1f, 0f, 1f);
        GL.tessellator.addVertex(-.5f, -.5f, -.5f);
        GL.tessellator.addColor(0f, 1f, 1f);
        GL.tessellator.addVertex(-.5f, -.5f, .5f);

        // Right
        GL.tessellator.addColor(0f, 1f, 0f);
        GL.tessellator.addVertex(.5f, .5f, -.5f);
        GL.tessellator.addColor(1f, 0f, 0f);
        GL.tessellator.addVertex(.5f, .5f, .5f);
        GL.tessellator.addColor(1f, 0f, 1f);
        GL.tessellator.addVertex(.5f, -.5f, .5f);
        GL.tessellator.addColor(0f, 1f, 1f);
        GL.tessellator.addVertex(.5f, -.5f, -.5f);

        // Top
        GL.tessellator.addColor(0f, 1f, 0f);
        GL.tessellator.addVertex(.5f, .5f, -.5f);
        GL.tessellator.addColor(1f, 0f, 0f);
        GL.tessellator.addVertex(-.5f, .5f, -.5f);
        GL.tessellator.addColor(0f, 1f, 0f);
        GL.tessellator.addVertex(-.5f, .5f, .5f);
        GL.tessellator.addColor(1f, 0f, 0f);
        GL.tessellator.addVertex(.5f, .5f, .5f);

        // Bottom
        GL.tessellator.addColor(1f, 0f, 1f);
        GL.tessellator.addVertex(-.5f, -.5f, -.5f);
        GL.tessellator.addColor(0f, 1f, 1f);
        GL.tessellator.addVertex(.5f, -.5f, -.5f);
        GL.tessellator.addColor(1f, 0f, 1f);
        GL.tessellator.addVertex(.5f, -.5f, .5f);
        GL.tessellator.addColor(0f, 1f, 1f);
        GL.tessellator.addVertex(-.5f, -.5f, .5f);

        GL.tessellator.stop();
        GL.tessellator.addSegment("Cube mesh for quads with per vertex colors");

        Fw.graphics.createMesh(GL.tessellator, this);
    }

}
