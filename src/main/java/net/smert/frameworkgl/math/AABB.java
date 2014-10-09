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
package net.smert.frameworkgl.math;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class AABB {

    final Vector3f max;
    final Vector3f min;

    public AABB() {
        max = new Vector3f();
        min = new Vector3f();
    }

    public AABB(AABB aabb) {
        max = new Vector3f(aabb.max);
        min = new Vector3f(aabb.min);
    }

    public AABB(Vector3f min, Vector3f max) {
        this.max = max;
        this.min = min;
    }

    public Vector3f getMax() {
        return max;
    }

    public void setMax(Vector3f max) {
        this.max.set(max);
    }

    public void setMax(Vector4f max) {
        this.max.set(max.x, max.y, max.z);
    }

    public void setMax(float x, float y, float z) {
        max.set(x, y, z);
    }

    public Vector3f getMin() {
        return min;
    }

    public void setMin(Vector3f min) {
        this.min.set(min);
    }

    public void setMin(Vector4f min) {
        this.min.set(min.x, min.y, min.z);
    }

    public void setMin(float x, float y, float z) {
        min.set(x, y, z);
    }

    public boolean testCollision(AABB aabb) {
        if ((max.getX() < aabb.min.getX()) || (aabb.max.getX() < min.getX())) {
            return false;
        }
        if ((max.getZ() < aabb.min.getZ()) || (aabb.max.getZ() < min.getZ())) {
            return false;
        }
        return ((max.getY() >= aabb.min.getY()) && (aabb.max.getY() >= min.getY()));
    }

    @Override
    public String toString() {
        return "min: " + min + " max: " + max;
    }

}
