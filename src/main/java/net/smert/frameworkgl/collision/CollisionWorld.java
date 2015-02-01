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
package net.smert.frameworkgl.collision;

import java.util.ArrayList;
import java.util.List;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.collision.broadphase.BroadphaseAlgorithm;
import net.smert.frameworkgl.collision.broadphase.BroadphaseProxy;
import net.smert.frameworkgl.collision.narrowphase.NarrowphaseDispatch;
import net.smert.frameworkgl.collision.response.CollisionResolver;
import net.smert.frameworkgl.opengl.pipeline.AbstractRenderingPipeline.DebugRenderCallback;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class CollisionWorld {

    private final BroadphaseAlgorithm broadphase;
    private final CollisionResolver resolver;
    private final List<CollisionGameObject> collisionGameObjects;
    private final NarrowphaseDispatch dispatch;

    public CollisionWorld(BroadphaseAlgorithm broadphase, CollisionResolver resolver, NarrowphaseDispatch dispatch) {
        this.broadphase = broadphase;
        this.resolver = resolver;
        this.dispatch = dispatch;
        collisionGameObjects = new ArrayList<>();
    }

    public void addCollisionGameObject(CollisionGameObject collisionGameObject) {
        Fw.graphics.updateAabb(collisionGameObject);
        BroadphaseProxy broadphaseProxy = broadphase.createProxy(collisionGameObject,
                collisionGameObject.getCollisionGroup(), collisionGameObject.getCollisionCollidesWith(),
                collisionGameObject.getWorldAabb());
        collisionGameObject.setBroadphaseProxy(broadphaseProxy);
        collisionGameObjects.add(collisionGameObject);
    }

    public void destroy() {
        broadphase.destroyDebugRender();
    }

    public DebugRenderCallback getBroadphaseDebugRenderCallback() {
        return broadphase.getDebugRenderCallback();
    }

    public void removeCollisionGameObject(CollisionGameObject collisionGameObject) {
        BroadphaseProxy broadphaseProxy = collisionGameObject.getBroadphaseProxy();
        broadphase.removeProxy(broadphaseProxy);
        collisionGameObject.setBroadphaseProxy(null);
        collisionGameObjects.remove(collisionGameObject);
    }

    public void update() {

        // Integrate
        float delta = Fw.timer.getDelta();
        for (CollisionGameObject collisionGameObject : collisionGameObjects) {
            collisionGameObject.integrateSemiExplicitEuler(delta);
        }

        // AABBs
        updateAabbs();

        // Broadphase
        broadphase.updateOverlappingPairs();

        // Narrowphase
        dispatch.processOverlappingPairs(broadphase.getOverlappingPairCache());

        // Resolve collisions
        resolver.processContacts(dispatch.getContactData());
    }

    public void updateAabb(CollisionGameObject collisionGameObject) {
        Fw.graphics.updateAabb(collisionGameObject);
        broadphase.moveProxy(collisionGameObject.getBroadphaseProxy(), collisionGameObject.getWorldAabb());
    }

    public void updateAabbs() {
        for (CollisionGameObject collisionGameObject : collisionGameObjects) {
            if (!collisionGameObject.isStaticObject()) {
                updateAabb(collisionGameObject);
            }
        }
    }

}
