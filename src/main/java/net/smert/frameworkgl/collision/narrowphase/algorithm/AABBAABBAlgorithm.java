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
package net.smert.frameworkgl.collision.narrowphase.algorithm;

import net.smert.frameworkgl.collision.CollisionGameObject;
import net.smert.frameworkgl.collision.narrowphase.Contact;
import net.smert.frameworkgl.collision.narrowphase.ContactData;
import net.smert.frameworkgl.math.AABB;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.utils.ThreadLocalVars;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class AABBAABBAlgorithm implements NarrowphaseAlgorithm {

    @Override
    public int processCollision(CollisionGameObject collisionGameObject0, CollisionGameObject collisionGameObject1,
            ContactData contactData) {
        AABB aabb0 = collisionGameObject0.getWorldAabb();
        AABB aabb1 = collisionGameObject1.getWorldAabb();

        // Temp vars from thread local storage
        ThreadLocalVars vars = ThreadLocalVars.Get();
        Vector3f distBetweenCenters = vars.v3f0;
        Vector3f extend0 = vars.v3f1;
        Vector3f extend1 = vars.v3f2;
        Vector3f normal = vars.v3f3;
        Vector3f penetration = vars.v3f4;
        Vector3f pos0 = vars.v3f5;
        Vector3f pos1 = vars.v3f6;

        // Calculate half extends
        extend0.set(aabb0.getMax()).subtract(aabb0.getMin()).multiply(.5f);
        extend1.set(aabb1.getMax()).subtract(aabb1.getMin()).multiply(.5f);

        // Calculate position
        pos0.set(aabb0.getMin()).add(aabb0.getMax()).multiply(.5f);
        pos1.set(aabb1.getMin()).add(aabb1.getMax()).multiply(.5f);

        // Calculate vector between positions from aabb0 to aabb1
        distBetweenCenters.set(pos1).subtract(pos0);

        // Find penetration |dist between centers| - (extend0 + extend1) < 0
        extend0.add(extend1);
        penetration.set(distBetweenCenters).abs().subtract(extend0);

        if ((penetration.getX() >= 0f) || (penetration.getY() >= 0f) || (penetration.getZ() >= 0f)) {

            // Release vars instance
            vars.release();

            return 0; // No penetration
        }

        // Find collision normal of the largest negative penetration depth (closest to zero)
        int maxAxis = penetration.maxAxis();
        switch (maxAxis) {
            case 0:
                normal.set(Vector3f.WORLD_X_AXIS);
                break;
            case 1:
                normal.set(Vector3f.WORLD_Y_AXIS);
                break;
            case 2:
                normal.set(Vector3f.WORLD_Z_AXIS);
                break;
            default:
                throw new IllegalArgumentException("Unknown axis was found " + maxAxis);
        }

        // Flip normal direction
        if (normal.dot(distBetweenCenters) < 0f) {
            normal.multiply(-1f);
        }

        // Create contact
        Contact contact = contactData.allocateContact();
        contact.collisionGameObject0 = collisionGameObject0;
        contact.collisionGameObject1 = collisionGameObject1;
        contact.normal.set(normal);
        contact.penetration = penetration.getElement(maxAxis);

        // Release vars instance
        vars.release();

        return 1;
    }

}
