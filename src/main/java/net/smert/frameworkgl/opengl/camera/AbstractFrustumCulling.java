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
package net.smert.frameworkgl.opengl.camera;

import net.smert.frameworkgl.math.AABB;
import net.smert.frameworkgl.math.ClipPlanes;
import net.smert.frameworkgl.math.Matrix4f;
import net.smert.frameworkgl.math.Vector3f;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public abstract class AbstractFrustumCulling {

    protected final ClipPlanes clipPlanes;

    public AbstractFrustumCulling() {
        clipPlanes = new ClipPlanes();
    }

    public boolean isAABBInFrustum(AABB aabb) {
        return clipPlanes.planeAABBEquation(aabb.getMin(), aabb.getMax());
    }

    public boolean isPointInFrustum(float x, float y, float z) {
        return clipPlanes.planePointEquation(x, y, z, 0f);
    }

    public boolean isPointInFrustum(Vector3f point) {
        return clipPlanes.planePointEquation(point, 0f);
    }

    public boolean isSphereInFrustum(float x, float y, float z, float radius) {
        return clipPlanes.planePointEquation(x, y, z, radius);
    }

    public boolean isSphereInFrustum(Vector3f point, float radius) {
        return clipPlanes.planePointEquation(point, radius);
    }

    public abstract void updatePlanes(Matrix4f projectionmatrix, Matrix4f viewmatrix);

}
