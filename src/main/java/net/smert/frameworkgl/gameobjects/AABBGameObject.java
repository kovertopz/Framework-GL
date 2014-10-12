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

import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.math.AABB;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.gl1.DynamicVertexBufferObjectRenderable;
import net.smert.frameworkgl.utils.Color;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class AABBGameObject extends GameObject {

    private final Color color0;
    private DynamicVertexBufferObjectRenderable aabbRenderable;

    public AABBGameObject() {
        color0 = new Color();
    }

    public Color getColor0() {
        return color0;
    }

    public void init(AABB aabb) {

        // Create mesh and renderable
        Mesh mesh = GL.meshFactory.createMesh();
        GL.dynamicMeshBuilder.
                setColor(0, color0).
                setCustomData(0, aabb).
                build("aabb").
                createMesh(false, mesh);
        aabbRenderable = GL.legacyRenderer.createDynamicVertexBufferObjectRenderable();
        aabbRenderable.create(mesh);

        // Attach to game object
        setMesh(mesh);
        setRenderable(aabbRenderable);
    }

    public void update(AABB aabb) {

        // Update mesh and renderable
        GL.dynamicMeshBuilder.
                setColor(0, color0).
                setCustomData(0, aabb).
                build("aabb").
                createMesh(false, getMesh());
        aabbRenderable.update(getMesh());
    }

}
