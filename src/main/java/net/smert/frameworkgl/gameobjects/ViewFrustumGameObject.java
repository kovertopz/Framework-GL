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
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.gl1.DynamicVertexBufferObjectRenderable;
import net.smert.frameworkgl.utils.Color;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class ViewFrustumGameObject extends GameObject {

    private final Color color0;
    private final Color color1;
    private final Color color2;
    private final Color color3;
    private DynamicVertexBufferObjectRenderable viewFrustumRenderable;

    public ViewFrustumGameObject() {
        color0 = new Color();
        color1 = new Color();
        color2 = new Color();
        color3 = new Color();
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

    public Color getColor3() {
        return color3;
    }

    public void init(float aspectRatio, float fieldOfView, float zNear, float zFar) {

        // Create mesh and renderable
        Mesh mesh = GL.meshFactory.createMesh();
        GL.dynamicMeshBuilder.
                setColor(0, color0).
                setColor(1, color1).
                setColor(2, color2).
                setColor(3, color3).
                setCustomData(0, aspectRatio).
                setCustomData(1, fieldOfView).
                setCustomData(2, zNear).
                setCustomData(3, zFar).
                build("view_frustum").
                createMesh(false, mesh);
        viewFrustumRenderable = Fw.graphics.createDynamicVertexBufferObjectRenderable();
        viewFrustumRenderable.create(mesh);

        // Attach to game object
        setMesh(mesh);
        setRenderable(viewFrustumRenderable);
    }

    public void update(float aspectRatio, float fieldOfView, float zNear, float zFar) {

        // Update mesh and renderable
        GL.dynamicMeshBuilder.
                setColor(0, color0).
                setColor(1, color1).
                setColor(2, color2).
                setColor(3, color3).
                setCustomData(0, aspectRatio).
                setCustomData(1, fieldOfView).
                setCustomData(2, zNear).
                setCustomData(3, zFar).
                build("view_frustum").
                createMesh(false, getMesh());
        viewFrustumRenderable.update(getMesh());
    }

}
