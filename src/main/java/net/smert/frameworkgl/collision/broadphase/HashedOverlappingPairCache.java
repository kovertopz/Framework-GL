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
package net.smert.frameworkgl.collision.broadphase;

import java.util.Iterator;
import net.smert.frameworkgl.collision.narrowphase.NarrowphaseDispatch;
import net.smert.frameworkgl.utils.HashMapIntGeneric;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class HashedOverlappingPairCache implements OverlappingPairCache {

    private final HashMapIntGeneric<OverlappingPair> overlappingPairs;
    private final OverlappingPairFilterCallback overlappingPairFilterCallback;

    public HashedOverlappingPairCache(OverlappingPairFilterCallback overlappingPairFilterCallback) {
        this.overlappingPairFilterCallback = overlappingPairFilterCallback;
        overlappingPairs = new HashMapIntGeneric<>();
    }

    private int getHashCode(BroadphaseProxy broadphaseProxy0, BroadphaseProxy broadphaseProxy1) {
        int hash0 = broadphaseProxy0.hashCode();
        int hash1 = broadphaseProxy1.hashCode();

        if (hash0 < hash1) {
            int tmp = hash0;
            hash0 = hash1;
            hash1 = tmp;
        }

        return (hash0 * 17) ^ (hash1 * 31);
    }

    @Override
    public OverlappingPair addOverlappingPair(BroadphaseProxy broadphaseProxy0, BroadphaseProxy broadphaseProxy1) {
        if (!overlappingPairFilterCallback.needsBroadphaseCollision(broadphaseProxy0, broadphaseProxy1)) {
            return null;
        }

        // Calculate hash code and create new pair
        int hashCode = getHashCode(broadphaseProxy0, broadphaseProxy1);
        OverlappingPair pair = new OverlappingPair();
        pair.proxy0 = broadphaseProxy0;
        pair.proxy1 = broadphaseProxy1;

        // Save pair
        OverlappingPair oldPair = overlappingPairs.put(hashCode, pair);

        return pair;
    }

    @Override
    public OverlappingPair findOverlappingPair(BroadphaseProxy broadphaseProxy0, BroadphaseProxy broadphaseProxy1) {
        int hashCode = getHashCode(broadphaseProxy0, broadphaseProxy1);
        return overlappingPairs.get(hashCode);
    }

    @Override
    public void processOverlappingPairs(NarrowphaseDispatch dispatch) {
        Iterator it = overlappingPairs.values().iterator();

        while (it.hasNext()) {
            OverlappingPair pair = (OverlappingPair) it.next();

            if (!dispatch.processOverlappingPair(pair)) {
                it.remove();
            }
        }
    }

    @Override
    public void removeOverlappingPair(BroadphaseProxy broadphaseProxy0, BroadphaseProxy broadphaseProxy1) {
        int hashCode = getHashCode(broadphaseProxy0, broadphaseProxy1);
        overlappingPairs.remove(hashCode);
    }

    @Override
    public void removeOverlappingPairsContainingProxy(BroadphaseProxy broadphaseProxy) {
        Iterator it = overlappingPairs.values().iterator();

        while (it.hasNext()) {
            OverlappingPair pair = (OverlappingPair) it.next();

            if ((pair.proxy0 == broadphaseProxy) || (pair.proxy1 == broadphaseProxy)) {
                it.remove();
            }
        }
    }

}
