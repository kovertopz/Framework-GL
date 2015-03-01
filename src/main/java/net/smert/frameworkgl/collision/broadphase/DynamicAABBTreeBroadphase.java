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

import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.collision.CollisionGameObject;
import net.smert.frameworkgl.gameobjects.AABBGameObject;
import net.smert.frameworkgl.math.AABB;
import net.smert.frameworkgl.math.AABBUtilities;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.opengl.pipeline.AbstractRenderingPipeline.DebugRenderCallback;
import net.smert.frameworkgl.utils.StackInt;
import net.smert.frameworkgl.utils.ThreadLocalVars;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class DynamicAABBTreeBroadphase implements BroadphaseAlgorithm {

    public final static int NULL = -1;

    private float displacementMultiplier;
    private int capacity;
    private int free;
    private int root;
    private int size;
    private DynamicAABBTreeBroadphaseProxy[] proxies;
    private final OverlappingPairCache overlappingPairCache;
    private final StackInt stackOfIndexes;
    private final Vector3f margin;

    public DynamicAABBTreeBroadphase(OverlappingPairCache overlappingPairCache) {
        this.overlappingPairCache = overlappingPairCache;

        displacementMultiplier = 2f;
        capacity = 16;
        free = 0;
        root = NULL;
        size = 0;
        proxies = new DynamicAABBTreeBroadphaseProxy[capacity];
        stackOfIndexes = new StackInt();
        margin = new Vector3f(.1f, .1f, .1f);
        createProxies(free);
    }

    private void ascendFixingHeightAndAabb(int index) {
        while (index != NULL) {
            index = balance(index);

            assert (index >= 0);
            assert (index < capacity);

            DynamicAABBTreeBroadphaseProxy proxy = proxies[index];

            int indexLeft = proxy.left;
            int indexRight = proxy.right;

            assert (indexLeft >= 0);
            assert (indexLeft < capacity);
            assert (indexRight >= 0);
            assert (indexRight < capacity);

            DynamicAABBTreeBroadphaseProxy proxyLeft = proxies[indexLeft];
            DynamicAABBTreeBroadphaseProxy proxyRight = proxies[indexRight];

            proxy.aabb.combine(proxyLeft.aabb, proxyRight.aabb);
            proxy.height = 1 + Math.max(proxyLeft.height, proxyRight.height);

            index = proxy.parent;
        }
    }

    private int allocateProxy() {

        // Expand proxies array
        if (free == NULL) {
            assert (size == capacity);
            capacity *= 2;
            free = size;
            DynamicAABBTreeBroadphaseProxy[] newProxies = new DynamicAABBTreeBroadphaseProxy[capacity];
            System.arraycopy(proxies, 0, newProxies, 0, size);
            proxies = newProxies;
            createProxies(free);
        }

        // Allocate proxy
        int index = free;
        DynamicAABBTreeBroadphaseProxy proxy = proxies[index];
        free = proxy.parent; // Next free proxy
        size++;

        // Defaults
        proxy.height = 0;
        proxy.index = index;
        proxy.left = NULL;
        proxy.parent = NULL; // Last free proxy
        proxy.right = NULL;

        return index;
    }

    private int balance(int index) {
        assert (index >= 0);
        assert (index < capacity);

        //
        //                           Parent
        //                              |
        //                            Proxy
        //                   ________/     \________
        //          ProxyLeft                       ProxyRight
        //         /         \                     /          \
        // ProxyLeftLeft  ProxyLeftRight   ProxyRightLeft  ProxyRightRight
        DynamicAABBTreeBroadphaseProxy proxy = proxies[index];

        if (proxy.isLeaf() || proxy.height < 2) {
            return index;
        }

        int indexLeft = proxy.left;
        int indexRight = proxy.right;

        assert (indexLeft >= 0);
        assert (indexLeft < capacity);
        assert (indexRight >= 0);
        assert (indexRight < capacity);

        DynamicAABBTreeBroadphaseProxy proxyLeft = proxies[indexLeft];
        DynamicAABBTreeBroadphaseProxy proxyRight = proxies[indexRight];

        int balance = proxyRight.height - proxyLeft.height;

        // Rotate proxyLeft up
        if (balance < -1) {
            int indexLeftLeft = proxyLeft.left;
            int indexLeftRight = proxyLeft.right;

            assert (indexLeftLeft >= 0);
            assert (indexLeftLeft < capacity);
            assert (indexLeftRight >= 0);
            assert (indexLeftRight < capacity);

            DynamicAABBTreeBroadphaseProxy proxyLeftLeft = proxies[indexLeftLeft];
            DynamicAABBTreeBroadphaseProxy proxyLeftRight = proxies[indexLeftRight];

            // Swap proxy and proxyLeft
            proxyLeft.left = index;
            proxyLeft.parent = proxy.parent;
            proxy.parent = indexLeft;

            // proxy's old parent should point to proxyLeft
            int indexOldParent = proxyLeft.parent;
            if (indexOldParent != NULL) {

                // Old parent wasn't the root
                DynamicAABBTreeBroadphaseProxy proxyOldParent = proxies[indexOldParent];
                if (proxyOldParent.left == index) {
                    proxyOldParent.left = indexLeft;
                } else {
                    proxyOldParent.right = indexLeft;
                }
            } else {

                // Old parent was the root
                root = indexLeft;
            }

            if (proxyLeftLeft.height > proxyLeftRight.height) {
                //
                //                           Parent
                //                              |
                //                          ProxyLeft
                //                 ________/         \________
                //          __Proxy__                         ProxyLeftLeft
                //         /         \
                // ProxyLeftRight     ProxyRight
                //                   /          \
                //          ProxyRightLeft  ProxyRightRight
                proxy.left = indexLeftRight;
                proxyLeftRight.parent = index;
                proxyLeft.right = indexLeftLeft;
                proxy.aabb.combine(proxyLeftRight.aabb, proxyRight.aabb);
                proxyLeft.aabb.combine(proxy.aabb, proxyLeftLeft.aabb);
                proxy.height = 1 + Math.max(proxyLeftRight.height, proxyRight.height);
                proxyLeft.height = 1 + Math.max(proxy.height, proxyLeftLeft.height);
            } else {
                //
                //                           Parent
                //                              |
                //                          ProxyLeft
                //                 ________/         \________
                //          __Proxy__                         ProxyLeftRight
                //         /         \
                // ProxyLeftLeft      ProxyRight
                //                   /          \
                //          ProxyRightLeft  ProxyRightRight
                proxy.left = indexLeftLeft;
                proxyLeftLeft.parent = index;
                proxyLeft.right = indexLeftRight;
                proxy.aabb.combine(proxyLeftLeft.aabb, proxyRight.aabb);
                proxyLeft.aabb.combine(proxy.aabb, proxyLeftRight.aabb);
                proxy.height = 1 + Math.max(proxyLeftLeft.height, proxyRight.height);
                proxyLeft.height = 1 + Math.max(proxy.height, proxyLeftRight.height);
            }

            return indexLeft;
        }

        // Rotate proxyRight up
        if (balance > 1) {
            int indexRightLeft = proxyRight.left;
            int indexRightRight = proxyRight.right;

            assert (indexRightLeft >= 0);
            assert (indexRightLeft < capacity);
            assert (indexRightRight >= 0);
            assert (indexRightRight < capacity);

            DynamicAABBTreeBroadphaseProxy proxyRightLeft = proxies[indexRightLeft];
            DynamicAABBTreeBroadphaseProxy proxyRightRight = proxies[indexRightRight];

            // Swap proxy and proxyRight
            proxyRight.left = index;
            proxyRight.parent = proxy.parent;
            proxy.parent = indexRight;

            // proxy's old parent should point to proxyRight
            int indexOldParent = proxyRight.parent;
            if (indexOldParent != NULL) {

                // Old parent wasn't the root
                DynamicAABBTreeBroadphaseProxy proxyOldParent = proxies[indexOldParent];
                if (proxyOldParent.left == index) {
                    proxyOldParent.left = indexRight;
                } else {
                    proxyOldParent.right = indexRight;
                }
            } else {

                // Old parent was the root
                root = indexRight;
            }

            if (proxyRightLeft.height > proxyRightRight.height) {
                //
                //                           Parent
                //                              |
                //                          ProxyRight
                //                 ________/          \________
                //          __Proxy__                          ProxyRightLeft
                //         /         \
                //     ProxyLeft_     ProxyRightRight
                //    /          \
                // ProxyLeftLeft  ProxyLeftRight
                proxy.right = indexRightRight;
                proxyRightRight.parent = index;
                proxyRight.right = indexRightLeft;
                proxy.aabb.combine(proxyLeft.aabb, proxyRightRight.aabb);
                proxyRight.aabb.combine(proxy.aabb, proxyRightLeft.aabb);
                proxy.height = 1 + Math.max(proxyLeft.height, proxyRightRight.height);
                proxyRight.height = 1 + Math.max(proxy.height, proxyRightLeft.height);
            } else {
                //
                //                           Parent
                //                              |
                //                          ProxyRight
                //                 ________/          \________
                //          __Proxy__                          ProxyRightRight
                //         /         \
                //     ProxyLeft_     ProxyRightLeft
                //    /          \
                // ProxyLeftLeft  ProxyLeftRight
                proxy.right = indexRightLeft;
                proxyRightLeft.parent = index;
                proxyRight.right = indexRightRight;
                proxy.aabb.combine(proxyLeft.aabb, proxyRightLeft.aabb);
                proxyRight.aabb.combine(proxy.aabb, proxyRightRight.aabb);
                proxy.height = 1 + Math.max(proxyLeft.height, proxyRightLeft.height);
                proxyRight.height = 1 + Math.max(proxy.height, proxyRightRight.height);
            }

            return indexRight;
        }

        return index;
    }

    private int computeHeight() {
        return computeHeight(root);
    }

    private int computeHeight(int index) {
        assert (index >= 0);
        assert (index < capacity);

        DynamicAABBTreeBroadphaseProxy proxy = proxies[index];

        if (proxy.isLeaf()) {
            return 0;
        }

        int height1 = computeHeight(proxy.left);
        int height2 = computeHeight(proxy.right);
        return 1 + Math.max(height1, height2);
    }

    private void createProxies(int index) {
        assert (index >= 0);
        assert (index < capacity);

        // Create all proxies except the last one
        for (int i = index; i < capacity - 1; i++) {
            DynamicAABBTreeBroadphaseProxy proxy = new DynamicAABBTreeBroadphaseProxy();
            proxy.height = NULL;
            proxy.index = NULL; // Mark not in use
            proxy.parent = i + 1; // Next free proxy
            proxies[i] = proxy;
        }

        // Create last proxy
        DynamicAABBTreeBroadphaseProxy proxy = new DynamicAABBTreeBroadphaseProxy();
        proxy.height = NULL;
        proxy.index = NULL; // Mark not in use
        proxy.parent = NULL; // Last free proxy
        proxies[capacity - 1] = proxy;
    }

    private void freeProxy(int index) {
        assert (index >= 0);
        assert (index < capacity);
        assert (size > 0);

        DynamicAABBTreeBroadphaseProxy proxy = proxies[index];
        proxy.parent = free; // Next free proxy
        free = index;
        size--;
    }

    private int getHeight() {
        if (root == NULL) {
            return 0;
        }
        return proxies[root].height;
    }

    private void insertLeaf(int index) {
        assert (index >= 0);
        assert (index < capacity);

        DynamicAABBTreeBroadphaseProxy proxyLeaf = proxies[index];

        // Create root proxy
        if (root == NULL) {
            root = index;
            proxyLeaf.parent = NULL; // Last free proxy
            return;
        }

        // Temp vars from thread local storage
        ThreadLocalVars vars = ThreadLocalVars.Get();
        AABB combinedAabb = vars.aabb0;

        // Find best sibling for the leaf proxy
        int indexCurrent = root;
        DynamicAABBTreeBroadphaseProxy proxyCurrent;
        while (!(proxyCurrent = proxies[indexCurrent]).isLeaf()) {

            // Combine the current and leaf proxy AABBs
            combinedAabb.combine(proxyCurrent.aabb, proxyLeaf.aabb);

            // Calculate volumes
            float currentVolume = proxyCurrent.aabb.getVolume();
            float leafAndCurrentVolume = combinedAabb.getVolume();

            // Calculate costs
            float combinedCost = 2f * leafAndCurrentVolume;
            float leafCost = 2f * (leafAndCurrentVolume - currentVolume);
            float leftCost;
            float rightCost;

            // Calculate left cost
            DynamicAABBTreeBroadphaseProxy proxyLeft = proxies[proxyCurrent.left];
            combinedAabb.combine(proxyLeft.aabb, proxyLeaf.aabb);
            if (proxyLeft.isLeaf()) {
                leftCost = combinedAabb.getVolume() + leafCost;
            } else {
                float oldCost = proxyLeft.aabb.getVolume();
                float newCost = combinedAabb.getVolume();
                leftCost = (newCost - oldCost) + leafCost;
            }

            // Calculate right cost
            DynamicAABBTreeBroadphaseProxy proxyRight = proxies[proxyCurrent.right];
            combinedAabb.combine(proxyRight.aabb, proxyLeaf.aabb);
            if (proxyRight.isLeaf()) {
                rightCost = combinedAabb.getVolume() + leafCost;
            } else {
                float oldCost = proxyRight.aabb.getVolume();
                float newCost = combinedAabb.getVolume();
                rightCost = (newCost - oldCost) + leafCost;
            }

            // Combined cost of leaf and current proxy might be cheaper
            if (combinedCost < leftCost && combinedCost < rightCost) {
                break;
            }

            // Descend according to the minimum cost
            if (leftCost < rightCost) {
                indexCurrent = proxyCurrent.left;
            } else {
                indexCurrent = proxyCurrent.right;
            }
        }

        // Release vars instance
        vars.release();

        // We now have a sibling proxy
        DynamicAABBTreeBroadphaseProxy proxySibling = proxies[indexCurrent];

        // Create new parent proxy
        int indexOldParent = proxySibling.parent;
        int indexNewParent = allocateProxy();
        DynamicAABBTreeBroadphaseProxy proxyNewParent = proxies[indexNewParent];
        proxyNewParent.aabb.combine(proxySibling.aabb, proxyLeaf.aabb);
        proxyNewParent.height = proxySibling.height + 1;
        proxyNewParent.left = indexCurrent;
        proxyNewParent.parent = indexOldParent;
        proxyNewParent.right = index;

        // Change parent of leaf and sibling
        proxyLeaf.parent = indexNewParent;
        proxySibling.parent = indexNewParent;

        // proxySibling's old parent should point to newParent
        if (indexOldParent != NULL) {

            // Old parent wasn't the root
            DynamicAABBTreeBroadphaseProxy proxyOldParent = proxies[indexOldParent];
            if (proxyOldParent.left == indexCurrent) {
                proxyOldParent.left = indexNewParent;
            } else {
                proxyOldParent.right = indexNewParent;
            }
        } else {

            // Old parent was the root
            root = indexNewParent;
        }

        ascendFixingHeightAndAabb(indexNewParent);
    }

    private void removeLeaf(int index) {
        assert (index >= 0);
        assert (index < capacity);

        if (index == root) {
            root = NULL;
        }

        DynamicAABBTreeBroadphaseProxy proxy = proxies[index];
        int indexParent = proxy.parent;
        assert (indexParent >= 0);
        assert (indexParent < capacity);
        DynamicAABBTreeBroadphaseProxy proxyParent = proxies[indexParent];
        int indexGrandParent = proxyParent.parent;
        assert (indexGrandParent >= NULL); // Grand parent could be the root
        assert (indexGrandParent < capacity);

        int indexSibling;
        if (proxyParent.left == index) {
            indexSibling = proxyParent.right;
        } else {
            indexSibling = proxyParent.left;
        }
        assert (indexSibling >= 0);
        assert (indexSibling < capacity);
        DynamicAABBTreeBroadphaseProxy proxySibling = proxies[indexSibling];

        if (indexGrandParent != NULL) {
            DynamicAABBTreeBroadphaseProxy proxyGrandParent = proxies[indexGrandParent];

            // Destroy parent and connect sibling to grand parent
            if (proxyGrandParent.left == indexParent) {
                proxyGrandParent.left = indexSibling;
            } else {
                proxyGrandParent.right = indexSibling;
            }
            proxySibling.parent = indexGrandParent;

            ascendFixingHeightAndAabb(indexGrandParent);
        } else {
            root = indexSibling;
            proxySibling.parent = NULL; // Last free proxy
        }

        freeProxy(indexParent);
    }

    private void validateMetrics(int index) {
        if (index == NULL) {
            return;
        }

        assert (index >= 0);
        assert (index < capacity);

        DynamicAABBTreeBroadphaseProxy proxy = proxies[index];

        int indexLeft = proxy.left;
        int indexRight = proxy.right;

        if (proxy.isLeaf()) {

            // Test leaf proxy
            assert (proxy.height == 0);
            assert (indexLeft == NULL);
            assert (indexRight == NULL);
            return;
        }

        assert (indexLeft >= 0);
        assert (indexLeft < capacity);
        assert (indexRight >= 0);
        assert (indexRight < capacity);

        DynamicAABBTreeBroadphaseProxy proxyLeft = proxies[indexLeft];
        DynamicAABBTreeBroadphaseProxy proxyRight = proxies[indexRight];

        // Test to make sure the proxy's height equals the children
        int height1 = proxyLeft.height;
        int height2 = proxyRight.height;
        int height = 1 + Math.max(height1, height2);
        assert (proxy.height == height);

        // Temp vars from thread local storage
        ThreadLocalVars vars = ThreadLocalVars.Get();
        AABB combinedAabb = vars.aabb0;

        // Test proxy's left and right children's aabb equals the proxy's
        combinedAabb.combine(proxyLeft.aabb, proxyRight.aabb);
        assert (combinedAabb.equals(proxy.aabb));

        // Release vars instance
        vars.release();

        // Decend
        validateMetrics(indexLeft);
        validateMetrics(indexRight);
    }

    private void validateStructure(int index) {
        if (index == NULL) {
            return;
        }

        assert (index >= 0);
        assert (index < capacity);

        // Make sure the root proxy parent is NULL
        if (index == root) {
            assert (proxies[index].parent == NULL);
        }

        DynamicAABBTreeBroadphaseProxy proxy = proxies[index];

        int indexLeft = proxy.left;
        int indexRight = proxy.right;

        if (proxy.isLeaf()) {

            // Test leaf proxy
            assert (proxy.height == 0);
            assert (indexLeft == NULL);
            assert (indexRight == NULL);
            return;
        }

        assert (indexLeft >= 0);
        assert (indexLeft < capacity);
        assert (indexRight >= 0);
        assert (indexRight < capacity);

        // Test that the proxy's children have the parent of the proxy
        assert (proxies[indexLeft].parent == index);
        assert (proxies[indexRight].parent == index);

        // Decend
        validateStructure(indexLeft);
        validateStructure(indexRight);
    }

    public float getDisplacementMultiplier() {
        return displacementMultiplier;
    }

    public void setDisplacementMultiplier(float displacementMultiplier) {
        this.displacementMultiplier = displacementMultiplier;
    }

    public Vector3f getMargin() {
        return margin;
    }

    public void setMargin(float margin) {
        this.margin.set(margin, margin, margin);
    }

    public void setMargin(float x, float y, float z) {
        this.margin.set(x, y, z);
    }

    public void setMargin(Vector3f margin) {
        this.margin.set(margin);
    }

    public void validate() {
        validateStructure(root);
        validateMetrics(root);

        int freeCount = 0;
        int index = free;
        while (index != NULL) {
            assert (index >= 0);
            assert (index < capacity);
            index = proxies[index].parent;
            freeCount++;
        }

        assert (freeCount + size == capacity);
        assert (computeHeight() == getHeight());
    }

    @Override
    public BroadphaseProxy createProxy(CollisionGameObject collisionGameObject, int collisionGroup, int collisionCollidesWith, AABB worldAabb) {
        int index = allocateProxy();

        DynamicAABBTreeBroadphaseProxy proxy = proxies[index];

        // Set proxy AABB and add margin
        proxy.aabb.set(worldAabb);
        proxy.aabb.expand(margin);
        proxy.collisionCollidesWith = collisionCollidesWith;
        proxy.collisionGameObject = collisionGameObject;
        proxy.collisionGroup = collisionGroup;

        // Insert proxy into tree
        insertLeaf(index);

        // TODO remove me
        validate();

        return proxy;
    }

    @Override
    public void destroyDebugRender() {
        Render.Destroy();
    }

    @Override
    public DebugRenderCallback getDebugRenderCallback() {
        DebugRenderCallback callback = () -> {
            Render.Render(this);
        };
        return callback;
    }

    @Override
    public OverlappingPairCache getOverlappingPairCache() {
        return overlappingPairCache;
    }

    @Override
    public boolean moveProxy(BroadphaseProxy broadphaseProxy, AABB worldAabb) {

        // Downcast
        DynamicAABBTreeBroadphaseProxy proxy = (DynamicAABBTreeBroadphaseProxy) broadphaseProxy;

        // If the new AABB is still contained in the proxy's AABB then we do nothing
        if (AABBUtilities.IsAabb0ContainedInAabb1(worldAabb, proxy.aabb)) {
            return false;
        }

        // Remove proxy from tree
        removeLeaf(proxy.index);

        // Temp vars from thread local storage
        ThreadLocalVars vars = ThreadLocalVars.Get();
        Vector3f worldCenterNew = vars.v3f1;
        Vector3f worldCenterOld = vars.v3f2;

        // Calculate displacement
        worldCenterNew.set(worldAabb.getMin()).add(worldAabb.getMax()).multiply(.5f);
        worldCenterOld.set(proxy.aabb.getMin()).add(proxy.aabb.getMax()).multiply(.5f);
        worldCenterNew.subtract(worldCenterOld).multiply(displacementMultiplier);

        // Set proxy AABB and add margin
        proxy.aabb.set(worldAabb);
        proxy.aabb.expand(margin);

        // Expand AABB by the displacement vector
        if (worldCenterNew.getX() < 0f) {
            proxy.aabb.getMin().addX(worldCenterNew.getX());
        } else {
            proxy.aabb.getMax().addX(worldCenterNew.getX());
        }
        if (worldCenterNew.getY() < 0f) {
            proxy.aabb.getMin().addY(worldCenterNew.getY());
        } else {
            proxy.aabb.getMax().addY(worldCenterNew.getY());
        }
        if (worldCenterNew.getZ() < 0f) {
            proxy.aabb.getMin().addZ(worldCenterNew.getZ());
        } else {
            proxy.aabb.getMax().addZ(worldCenterNew.getZ());
        }

        // Release vars instance
        vars.release();

        // Insert proxy back into tree
        insertLeaf(proxy.index);

        return true;
    }

    @Override
    public void removeProxy(BroadphaseProxy broadphaseProxy) {
        DynamicAABBTreeBroadphaseProxy proxy = (DynamicAABBTreeBroadphaseProxy) broadphaseProxy;
        removeLeaf(proxy.index);
        freeProxy(proxy.index);
    }

    @Override
    public void updateOverlappingPairs() {

        // Clear stack (should be already empty)
        stackOfIndexes.clear();

        // Loop through finding all leaf nodes to query tree
        for (int i = 0; i < proxies.length; i++) {
            DynamicAABBTreeBroadphaseProxy proxy = proxies[i];

            // Skip free and non leaf proxies
            if ((proxy.index == NULL) || (!proxy.isLeaf())) {
                continue;
            }

            // Start with root
            stackOfIndexes.push(root);

            // Loop until empty
            while (!stackOfIndexes.empty()) {

                // Get current element off the stack
                int indexStack = stackOfIndexes.pop();

                // Skip free proxy
                if (indexStack == NULL) {
                    continue;
                }

                DynamicAABBTreeBroadphaseProxy proxyStack = proxies[indexStack];

                // Skip self collisions
                if (proxyStack == proxy) {
                    continue;
                }

                // Test for intersection
                if (AABBUtilities.DoesAabb0IntersectAabb1(proxyStack.aabb, proxy.aabb)) {

                    // There was an intersection but was it with a leaf?
                    if (proxyStack.isLeaf()) {
                        if (overlappingPairCache.findOverlappingPair(proxyStack, proxy) == null) {
                            overlappingPairCache.addOverlappingPair(proxyStack, proxy);
                        }
                    } else {

                        // Decend the tree
                        stackOfIndexes.push(proxyStack.left);
                        stackOfIndexes.push(proxyStack.right);
                    }
                } else {

                    // There was no intersection but was it a leaf?
                    if (proxyStack.isLeaf()) {
                        if (overlappingPairCache.findOverlappingPair(proxyStack, proxy) != null) {
                            overlappingPairCache.removeOverlappingPair(proxyStack, proxy);
                        }
                    }
                }
            }
        }
    }

    public static class Render {

        private static boolean initialized;
        private static AABBGameObject aabbGameObject;

        private static void Decend(DynamicAABBTreeBroadphase tree, int index) {
            if (index == NULL) {
                return;
            }

            assert (index >= 0);
            assert (index < tree.capacity);

            DynamicAABBTreeBroadphaseProxy proxy = tree.proxies[index];

            // Updating AABBs this way is costly
            aabbGameObject.update(proxy.aabb);
            // AABB is already in world coordinates so we don't translate
            Fw.graphics.render(aabbGameObject.getRenderable(), 0f, 0f, 0f);

            if (proxy.isLeaf()) {
                return;
            }

            int indexLeft = proxy.left;
            int indexRight = proxy.right;

            assert (indexLeft >= 0);
            assert (indexLeft < tree.capacity);
            assert (indexRight >= 0);
            assert (indexRight < tree.capacity);

            Decend(tree, indexLeft);
            Decend(tree, indexRight);
        }

        public static void Destroy() {
            if (!initialized) {
                return;
            }
            aabbGameObject.destroy();
            initialized = false;
        }

        public static void Render(DynamicAABBTreeBroadphase tree) {
            if (!initialized) {
                aabbGameObject = new AABBGameObject();
                aabbGameObject.getColor0().set("orange");
                aabbGameObject.init(new AABB()); // Empty AABB
                initialized = true;
            }
            Decend(tree, tree.root);
        }

    }

}
