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

import net.smert.frameworkgl.collision.CollisionGameObject;
import net.smert.frameworkgl.math.AABB;
import net.smert.frameworkgl.math.AABBUtilities;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.opengl.pipeline.PipelineRenderDebugCallback;
import net.smert.frameworkgl.utils.ThreadLocalVars;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class SimpleBroadphase implements BroadphaseAlgorithm {

    private final static int NULL = -1;

    private float displacementMultiplier;
    private int capacity;
    private int free;
    private int size;
    private final OverlappingPairCache overlappingPairCache;
    private SimpleBroadphaseProxy[] proxies;
    private final Vector3f margin;

    public SimpleBroadphase(OverlappingPairCache overlappingPairCache) {
        this.overlappingPairCache = overlappingPairCache;

        displacementMultiplier = 2f;
        capacity = 16;
        free = 0;
        size = 0;
        proxies = new SimpleBroadphaseProxy[capacity];
        margin = new Vector3f(.1f, .1f, .1f);
        createProxies(free);
    }

    private SimpleBroadphaseProxy allocateProxy() {

        // Expand proxies array
        if (free == NULL) {
            assert (size == capacity);
            capacity *= 2;
            free = size;
            SimpleBroadphaseProxy[] newProxies = new SimpleBroadphaseProxy[capacity];
            System.arraycopy(proxies, 0, newProxies, 0, size);
            proxies = newProxies;
            createProxies(free);
        }

        // Allocate proxy
        int index = free;
        SimpleBroadphaseProxy proxy = proxies[index];
        free = proxy.next; // Next free proxy
        size++;

        // Defaults
        proxy.index = index; // Mark proxy in use and also used to free proxy
        proxy.next = NULL; // Last free proxy

        return proxy;
    }

    private void createProxies(int index) {
        assert (index >= 0);
        assert (index < capacity);

        // Create all proxies except the last one
        for (int i = index; i < capacity - 1; i++) {
            SimpleBroadphaseProxy proxy = new SimpleBroadphaseProxy();
            proxy.index = NULL; // Mark not in use
            proxy.next = i + 1; // Next free proxy
            proxies[i] = proxy;
        }

        // Create last proxy
        SimpleBroadphaseProxy proxy = new SimpleBroadphaseProxy();
        proxy.index = NULL; // Mark not in use
        proxy.next = NULL; // Last free proxy
        proxies[capacity - 1] = proxy;
    }

    private void freeProxy(SimpleBroadphaseProxy proxy) {
        assert (size > 0);
        proxy.next = free; // Next free proxy
        free = proxy.index;
        size--;
        proxy.index = NULL; // Mark proxy free
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

    @Override
    public BroadphaseProxy createProxy(CollisionGameObject collisionGameObject, int collisionGroup,
            int collisionCollidesWith, AABB worldAabb) {
        SimpleBroadphaseProxy proxy = allocateProxy();

        // Set proxy AABB and add margin
        proxy.aabb.set(worldAabb);
        proxy.aabb.expand(margin);
        proxy.collisionCollidesWith = collisionCollidesWith;
        proxy.collisionGameObject = collisionGameObject;
        proxy.collisionGroup = collisionGroup;

        return proxy;
    }

    @Override
    public void destroyDebugRender() {
    }

    @Override
    public OverlappingPairCache getOverlappingPairCache() {
        return overlappingPairCache;
    }

    @Override
    public PipelineRenderDebugCallback getPipelineRenderDebugCallback() {
        return null;
    }

    @Override
    public boolean moveProxy(BroadphaseProxy broadphaseProxy, AABB worldAabb) {

        // Downcast
        SimpleBroadphaseProxy proxy = (SimpleBroadphaseProxy) broadphaseProxy;

        // If the new AABB is still contained in the proxy's AABB then we do nothing
        if (AABBUtilities.IsAabb0ContainedInAabb1(worldAabb, proxy.aabb)) {
            return false;
        }

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

        return true;
    }

    @Override
    public void removeProxy(BroadphaseProxy broadphaseProxy) {
        SimpleBroadphaseProxy proxy = (SimpleBroadphaseProxy) broadphaseProxy;
        freeProxy(proxy);
    }

    @Override
    public void updateOverlappingPairs() {
        for (int i = 0; i < proxies.length; i++) {
            SimpleBroadphaseProxy proxy0 = proxies[i];

            // Skip free proxy
            if (proxy0.index == NULL) {
                continue;
            }

            for (int j = i + 1; j < proxies.length; j++) {
                SimpleBroadphaseProxy proxy1 = proxies[j];

                // Skip free proxy
                if (proxy1.index == NULL) {
                    continue;
                }

                // Test for intersection
                if (AABBUtilities.DoesAabb0IntersectAabb1(proxy0.aabb, proxy1.aabb)) {
                    if (overlappingPairCache.findOverlappingPair(proxy0, proxy1) == null) {
                        overlappingPairCache.addOverlappingPair(proxy0, proxy1);
                    }
                } else {
                    if (overlappingPairCache.findOverlappingPair(proxy0, proxy1) != null) {
                        overlappingPairCache.removeOverlappingPair(proxy0, proxy1);
                    }
                }
            }
        }
    }

}
