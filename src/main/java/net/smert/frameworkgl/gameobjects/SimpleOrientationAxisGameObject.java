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
package net.smert.frameworkgl.gameobjects;

import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.gl1.VertexBufferObjectRenderable;
import net.smert.frameworkgl.utils.Color;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class SimpleOrientationAxisGameObject extends GameObject {

    private final Color color0;
    private final Color color1;
    private final Color color2;

    public SimpleOrientationAxisGameObject() {
        color0 = new Color();
        color1 = new Color();
        color2 = new Color();
    }

    public Color getColor0() {
        return color0;
    }

    public Color getColor1() {
        return color1;
    }

    public Color getColor2() {
        return color2;
    }

    @Override
    public void init() {

        // Create mesh and renderable
        Mesh mesh = GL.meshFactory.createMesh();
        GL.dynamicMeshBuilder.
                setColor(0, color0).
                setColor(1, color1).
                setColor(2, color2).
                build("simple_orientation_axis").
                createMesh(false, mesh);
        VertexBufferObjectRenderable simpleOrientationAxisRenderable
                = GL.legacyRenderer.createVertexBufferObjectRenderable();
        simpleOrientationAxisRenderable.create(mesh);

        // Attach to game object
        setMesh(mesh);
        setRenderable(simpleOrientationAxisRenderable);
    }

}
