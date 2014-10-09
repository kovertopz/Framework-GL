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
public class AABBUtilities {

    private final static Matrix3f rotationAbsolute = new Matrix3f();
    private final static Vector3f centerA = new Vector3f();
    private final static Vector3f centerB = new Vector3f();
    private final static Vector3f extent = new Vector3f();
    private final static Vector3f margin = new Vector3f();
    private final static Vector3f localCenter = new Vector3f();
    private final static Vector3f localExtent = new Vector3f();
    private final static Vector3f swap = new Vector3f();

    private AABBUtilities() {
    }

    public static boolean DoesAabb0IntersectAabb1(AABB aabb0, AABB aabb1) {
        return ((aabb0.min.getX() <= aabb1.max.getX()) && (aabb1.min.getX() <= aabb0.max.getX())
                && (aabb0.min.getZ() <= aabb1.max.getZ()) && (aabb1.min.getZ() <= aabb0.max.getZ())
                && (aabb0.min.getY() <= aabb1.max.getY()) && (aabb1.min.getY() <= aabb0.max.getY()));
    }

    public static boolean IsAabb0ContainedInAabb1(AABB aabb0, AABB aabb1) {
        return ((aabb0.min.getX() >= aabb1.min.getX()) && (aabb0.max.getX() <= aabb1.max.getX())
                && (aabb0.min.getZ() >= aabb1.min.getZ()) && (aabb0.max.getZ() <= aabb1.max.getZ())
                && (aabb0.min.getY() >= aabb1.min.getY()) && (aabb0.max.getY() <= aabb1.max.getY()));
    }

    public static float ManhattanDistance(AABB aabb0, AABB aabb1) {
        centerA.set(aabb0.min).add(aabb0.max).multiply(.5f);
        centerB.set(aabb1.min).add(aabb1.max).multiply(.5f);
        centerA.subtract(centerB).abs();
        return centerA.getX() + centerA.getY() + centerA.getZ();
    }

    public static void Swap(AABB aabb0, AABB aabb1) {
        swap.set(aabb0.min);
        aabb0.min.set(aabb1.min);
        aabb1.min.set(swap);
        swap.set(aabb0.max);
        aabb0.max.set(aabb1.max);
        aabb1.max.set(swap);
    }

    public static void Transform(AABB localAabb, Transform4f worldTransform, AABB worldAabb) {
        localCenter.set(localAabb.min).add(localAabb.max).multiply(.5f);
        worldTransform.multiplyOut(localCenter, centerA);
        localExtent.set(localAabb.max).subtract(localAabb.min).multiply(.5f);
        rotationAbsolute.set(worldTransform.getRotation()).absolute();
        extent.set(
                rotationAbsolute.dotRow(0, localExtent),
                rotationAbsolute.dotRow(1, localExtent),
                rotationAbsolute.dotRow(2, localExtent));
        worldAabb.max.set(centerA).add(extent);
        worldAabb.min.set(centerA).subtract(extent);
    }

    public static void Transform(AABB localAabb, float margin, Transform4f worldTransform, AABB worldAabb) {
        localCenter.set(localAabb.min).add(localAabb.max).multiply(.5f);
        worldTransform.multiplyOut(localCenter, centerA);
        AABBUtilities.margin.set(margin, margin, margin);
        localExtent.set(localAabb.max).subtract(localAabb.min).multiply(.5f).add(AABBUtilities.margin);
        rotationAbsolute.set(worldTransform.getRotation()).absolute();
        extent.set(
                rotationAbsolute.dotRow(0, localExtent),
                rotationAbsolute.dotRow(1, localExtent),
                rotationAbsolute.dotRow(2, localExtent));
        worldAabb.max.set(centerA).add(extent);
        worldAabb.min.set(centerA).subtract(extent);
    }

}
