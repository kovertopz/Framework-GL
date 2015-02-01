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

import net.smert.frameworkgl.math.AABB;
import net.smert.frameworkgl.math.Matrix3f;
import net.smert.frameworkgl.math.Transform4f;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;
import net.smert.frameworkgl.opengl.renderable.RenderableState;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class GameObject {

    private final AABB worldAabb;
    private AbstractRenderable renderable;
    private Mesh mesh;
    private final RenderableState renderableState;
    private Transform4f scalingTransform;
    private final Transform4f worldTransform;

    public GameObject() {
        worldAabb = new AABB();
        renderableState = new RenderableState();
        worldTransform = new Transform4f();
    }

    public void destroy() {
        if (renderable == null) {
            return;
        }
        renderable.destroy();
    }

    public AABB getWorldAabb() {
        return worldAabb;
    }

    public AbstractRenderable getRenderable() {
        return renderable;
    }

    public void setRenderable(AbstractRenderable renderable) {
        this.renderable = renderable;
    }

    public Matrix3f getWorldRotation() {
        return worldTransform.getRotation();
    }

    public GameObject setWorldRotation(Vector3f axis, float angle) {
        worldTransform.getRotation().fromAxisAngle(axis, angle);
        return this;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public RenderableState getRenderableState() {
        return renderableState;
    }

    public Transform4f getScalingTransform() {
        return scalingTransform;
    }

    public GameObject setScalingTransform(Transform4f scalingTransform) {
        this.scalingTransform = scalingTransform;
        return this;
    }

    public Transform4f getWorldTransform() {
        return worldTransform;
    }

    public GameObject setWorldTransform(Transform4f worldTransform) {
        this.worldTransform.set(worldTransform);
        return this;
    }

    public Vector3f getWorldPosition() {
        return worldTransform.getPosition();
    }

    public GameObject setWorldPosition(float x, float y, float z) {
        worldTransform.setPosition(x, y, z);
        return this;
    }

    public GameObject setWorldPosition(Vector3f position) {
        worldTransform.setPosition(position);
        return this;
    }

    public void init() {
    }

    public void update() {
    }

}
