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
package net.smert.frameworkgl.examples.dynamicvertexbufferobject;

import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.gameobjects.GameObject;
import net.smert.frameworkgl.math.MathHelper;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.gl1.DynamicVertexBufferObjectRenderable;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class DynamicSphereGameObject extends GameObject {

    private final static float SPHERE_UPDATE_DELTA = 1f / 60f;

    private float sphereRadiansX;
    private float sphereRadiansY;
    private float sphereRadiansZ;
    private float sphereRadiusX;
    private float sphereRadiusY;
    private float sphereRadiusZ;
    private float sphereUpdateTimer;
    private DynamicVertexBufferObjectRenderable dynamicVBORenderable;

    @Override
    public void init() {

        // Set float values
        sphereRadiansX = 0f;
        sphereRadiansY = 1f;
        sphereRadiansZ = 2f;
        sphereRadiusX = 2f;
        sphereRadiusY = 2f;
        sphereRadiusZ = 2f;
        sphereUpdateTimer = SPHERE_UPDATE_DELTA;

        // Set position
        getWorldTransform().setPosition(0f, 10f, 0f);

        // Create initial mesh
        Mesh dynamicMesh = GL.meshFactory.createMesh();
        GL.dynamicMeshBuilder.
                setColor(0, "medium_purple").
                setQuality(8, 1, 1).
                setRadius(sphereRadiusX, sphereRadiusY, sphereRadiusZ).
                setSize(0f, 0f, 0f).
                build("sphere").
                createMesh(false, dynamicMesh);

        // Create renderable
        dynamicVBORenderable = GL.legacyRenderer.createDynamicVertexBufferObjectRenderable();
        dynamicVBORenderable.create(dynamicMesh);

        // Attach to game object
        setMesh(dynamicMesh);
        setRenderable(dynamicVBORenderable);
    }

    @Override
    public void update() {

        // Update timer
        float delta = Fw.timer.getDelta();
        sphereUpdateTimer -= delta;

        // Is it time to update?
        if (sphereUpdateTimer < 0) {

            // Reset timer
            sphereUpdateTimer += SPHERE_UPDATE_DELTA;

            // Update radians and radiuses
            sphereRadiansX += SPHERE_UPDATE_DELTA * 3f;
            sphereRadiansY -= SPHERE_UPDATE_DELTA * 3f;
            sphereRadiansZ += SPHERE_UPDATE_DELTA * 3f;
            float newSphereRadiusX = sphereRadiusX + ((MathHelper.Cos(sphereRadiansX) + 1) * 4f);
            float newSphereRadiusY = sphereRadiusY + ((MathHelper.Cos(sphereRadiansY) + 1) * 4f);
            float newSphereRadiusZ = sphereRadiusZ + ((MathHelper.Cos(sphereRadiansZ) + 1) * 2f);

            // Generate new mesh
            GL.dynamicMeshBuilder.
                    setColor(0, "gold").
                    setQuality(8, 1, 1).
                    setRadius(newSphereRadiusX, newSphereRadiusY, newSphereRadiusZ).
                    setSize(0f, 0f, 0f).
                    build("sphere").
                    createMesh(false, getMesh());

            // Update renderable with new mesh
            dynamicVBORenderable.update(getMesh());
        }
    }

}
