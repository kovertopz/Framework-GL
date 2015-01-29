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

import java.util.Objects;

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

    public void combine(AABB aabb0, AABB aabb1) {
        float x, y, z;
        x = Math.max(aabb0.getMax().x, aabb1.getMax().x);
        y = Math.max(aabb0.getMax().y, aabb1.getMax().y);
        z = Math.max(aabb0.getMax().z, aabb1.getMax().z);
        max.set(x, y, z);
        x = Math.min(aabb0.getMin().x, aabb1.getMin().x);
        y = Math.min(aabb0.getMin().y, aabb1.getMin().y);
        z = Math.min(aabb0.getMin().z, aabb1.getMin().z);
        min.set(x, y, z);
    }

    public void expand(Vector3f amount) {
        max.add(amount);
        min.subtract(amount);
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

    public void set(AABB aabb) {
        max.set(aabb.max);
        min.set(aabb.min);
    }

    public float getVolume() {
        float x = max.getX() - min.getX();
        float y = max.getY() - min.getY();
        float z = max.getZ() - min.getZ();
        return (x * y * z);
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
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.max);
        hash = 89 * hash + Objects.hashCode(this.min);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AABB other = (AABB) obj;
        if (!Objects.equals(this.max, other.max)) {
            return false;
        }
        return Objects.equals(this.min, other.min);
    }

    @Override
    public String toString() {
        return "min: " + min + " max: " + max;
    }

}
