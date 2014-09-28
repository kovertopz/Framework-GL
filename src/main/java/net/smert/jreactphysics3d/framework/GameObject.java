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
package net.smert.jreactphysics3d.framework;

import net.smert.jreactphysics3d.framework.math.Transform4f;
import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.mesh.Material;
import net.smert.jreactphysics3d.framework.opengl.renderable.Renderable;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class GameObject {

    private Mesh mesh;
    private Material meshMaterial;
    private Renderable renderable;
    private Object rigidBody;
    private Object collisionBodyShape;
    private final Transform4f worldTransform;

    public GameObject() {
        worldTransform = new Transform4f();
    }

    public void destroy() {
    }

    public void render() {
    }

    public void update() {
    }

}
