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
package net.smert.frameworkgl.collision.narrowphase;

import net.smert.frameworkgl.collision.CollisionGameObject;
import net.smert.frameworkgl.collision.broadphase.OverlappingPair;
import net.smert.frameworkgl.collision.broadphase.OverlappingPairCache;
import net.smert.frameworkgl.collision.narrowphase.algorithm.AABBAABBAlgorithm;
import net.smert.frameworkgl.collision.narrowphase.algorithm.NarrowphaseAlgorithm;
import net.smert.frameworkgl.collision.shapes.ShapeType;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class NarrowphaseDispatch {

    private final ContactData contactData;
    private final NarrowphaseAlgorithm[][] doubleDispatch;
    private final NarrowphaseFilterCallback narrowphaseFilterCallback;

    public NarrowphaseDispatch(NarrowphaseFilterCallback narrowphaseFilterCallback) {
        this.narrowphaseFilterCallback = narrowphaseFilterCallback;
        contactData = new ContactData();
        doubleDispatch = new NarrowphaseAlgorithm[ShapeType.MAX_SHAPE.ordinal()][ShapeType.MAX_SHAPE.ordinal()];
        registerAlgorithm(new AABBAABBAlgorithm(), ShapeType.AABB, ShapeType.AABB);
    }

    public NarrowphaseAlgorithm findAlgorithm(CollisionGameObject collisionGameObject0,
            CollisionGameObject collisionGameObject1) {
        int value0 = collisionGameObject0.getShapeType().ordinal();
        int value1 = collisionGameObject1.getShapeType().ordinal();
        return doubleDispatch[value0][value1];
    }

    public ContactData getContactData() {
        return contactData;
    }

    public boolean processOverlappingPair(OverlappingPair pair) {
        CollisionGameObject collisionGameObject0 = pair.proxy0.collisionGameObject;
        CollisionGameObject collisionGameObject1 = pair.proxy1.collisionGameObject;

        if (narrowphaseFilterCallback.needsNarrowphaseCollision(collisionGameObject0, collisionGameObject1)) {
            NarrowphaseAlgorithm algorithm = findAlgorithm(collisionGameObject0, collisionGameObject1);
            int contactsAdded = algorithm.processCollision(collisionGameObject0, collisionGameObject1, contactData);
            return (contactsAdded > 0);
        }

        return false;
    }

    public void processOverlappingPairs(OverlappingPairCache overlappingPairCache) {
        overlappingPairCache.processOverlappingPairs(this);
    }

    public final void registerAlgorithm(NarrowphaseAlgorithm algorithm, ShapeType shapeType0, ShapeType shapeType1) {
        int value0 = shapeType0.ordinal();
        int value1 = shapeType1.ordinal();
        doubleDispatch[value0][value1] = algorithm;
    }

}
