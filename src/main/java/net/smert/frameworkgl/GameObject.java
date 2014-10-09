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
package net.smert.frameworkgl;

import net.smert.frameworkgl.math.Transform4f;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.mesh.Material;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class GameObject {

    private AbstractRenderable renderable;
    private Object collisionShape;
    private Mesh mesh;
    private Material meshMaterial;
    private Object rigidBody;
    private Transform4f scalingTransform;
    private final Transform4f worldTransform;

    public GameObject() {
        worldTransform = new Transform4f();
    }

    public void destroy() {
        if (renderable == null) {
            return;
        }
        renderable.destroy();
    }

    public Object getCollisionShape() {
        return collisionShape;
    }

    public void setCollisionShape(Object collisionShape) {
        this.collisionShape = collisionShape;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public Material getMeshMaterial() {
        return meshMaterial;
    }

    public void setMeshMaterial(Material meshMaterial) {
        this.meshMaterial = meshMaterial;
    }

    public AbstractRenderable getRenderable() {
        return renderable;
    }

    public void setRenderable(AbstractRenderable renderable) {
        this.renderable = renderable;
    }

    public Object getRigidBody() {
        return rigidBody;
    }

    public void setRigidBody(Object rigidBody) {
        this.rigidBody = rigidBody;
    }

    public Transform4f getScalingTransform() {
        return scalingTransform;
    }

    public void setScalingTransform(Transform4f scalingTransform) {
        this.scalingTransform = scalingTransform;
    }

    public Transform4f getWorldTransform() {
        return worldTransform;
    }

    public GameObject setWorldPosition(float x, float y, float z) {
        worldTransform.setPosition(x, y, z);
        return this;
    }

    public GameObject setWorldPosition(Vector3f position) {
        worldTransform.setPosition(position);
        return this;
    }

    public GameObject setWorldRotation(Vector3f axis, float angle) {
        worldTransform.getRotation().fromAxisAngle(axis, angle);
        return this;
    }

}
