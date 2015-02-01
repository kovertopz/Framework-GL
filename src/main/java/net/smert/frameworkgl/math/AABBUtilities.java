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

import net.smert.frameworkgl.utils.ThreadLocalVars;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class AABBUtilities {

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

        // Temp vars from thread local storage
        ThreadLocalVars vars = ThreadLocalVars.Get();
        Vector3f posA = vars.v3f0;
        Vector3f posB = vars.v3f1;

        // Calculate position
        posA.set(aabb0.min).add(aabb0.max).multiply(.5f);
        posB.set(aabb1.min).add(aabb1.max).multiply(.5f);

        // Absolute vector between both positions
        posA.subtract(posB).abs();
        float dist = posA.getX() + posA.getY() + posA.getZ();

        // Release vars instance
        vars.release();

        return dist;
    }

    public static void Swap(AABB aabb0, AABB aabb1) {

        // Temp vars from thread local storage
        ThreadLocalVars vars = ThreadLocalVars.Get();
        Vector3f swap = vars.v3f0;

        // Swap AABBs
        swap.set(aabb0.min);
        aabb0.min.set(aabb1.min);
        aabb1.min.set(swap);
        swap.set(aabb0.max);
        aabb0.max.set(aabb1.max);
        aabb1.max.set(swap);

        // Release vars instance
        vars.release();
    }

    public static void Transform(AABB localAabb, Transform4f worldTransform, AABB worldAabb) {

        // Temp vars from thread local storage
        ThreadLocalVars vars = ThreadLocalVars.Get();
        Matrix3f rotationAbsolute = vars.m3f0;
        Vector3f localExtent = vars.v3f0;
        Vector3f localPosition = vars.v3f1;
        Vector3f worldExtent = vars.v3f2;
        Vector3f worldPosition = vars.v3f3;

        localPosition.set(localAabb.min).add(localAabb.max).multiply(.5f);
        worldTransform.multiplyOut(localPosition, worldPosition);
        localExtent.set(localAabb.max).subtract(localAabb.min).multiply(.5f);
        rotationAbsolute.set(worldTransform.getRotation()).absolute();
        worldExtent.set(
                rotationAbsolute.dotRow(0, localExtent),
                rotationAbsolute.dotRow(1, localExtent),
                rotationAbsolute.dotRow(2, localExtent));
        worldAabb.max.set(worldPosition).add(worldExtent);
        worldAabb.min.set(worldPosition).subtract(worldExtent);

        // Release vars instance
        vars.release();
    }

    public static void Transform(AABB localAabb, float margin, Transform4f worldTransform, AABB worldAabb) {

        // Temp vars from thread local storage
        ThreadLocalVars vars = ThreadLocalVars.Get();
        Matrix3f rotationAbsolute = vars.m3f0;
        Vector3f localExtent = vars.v3f0;
        Vector3f localPosition = vars.v3f1;
        Vector3f margins = vars.v3f2;
        Vector3f worldExtent = vars.v3f3;
        Vector3f worldPosition = vars.v3f4;

        localPosition.set(localAabb.min).add(localAabb.max).multiply(.5f);
        worldTransform.multiplyOut(localPosition, worldPosition);
        margins.set(margin, margin, margin);
        localExtent.set(localAabb.max).subtract(localAabb.min).multiply(.5f).add(margins);
        rotationAbsolute.set(worldTransform.getRotation()).absolute();
        worldExtent.set(
                rotationAbsolute.dotRow(0, localExtent),
                rotationAbsolute.dotRow(1, localExtent),
                rotationAbsolute.dotRow(2, localExtent));
        worldAabb.max.set(worldPosition).add(worldExtent);
        worldAabb.min.set(worldPosition).subtract(worldExtent);

        // Release vars instance
        vars.release();
    }

}
