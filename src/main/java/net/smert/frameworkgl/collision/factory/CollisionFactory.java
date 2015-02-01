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
package net.smert.frameworkgl.collision.factory;

import net.smert.frameworkgl.collision.CollisionGameObject;
import net.smert.frameworkgl.collision.CollisionWorld;
import net.smert.frameworkgl.collision.broadphase.DefaultOverlappingPairFilterCallback;
import net.smert.frameworkgl.collision.broadphase.DynamicAABBTreeBroadphase;
import net.smert.frameworkgl.collision.broadphase.HashedOverlappingPairCache;
import net.smert.frameworkgl.collision.broadphase.SimpleBroadphase;
import net.smert.frameworkgl.collision.narrowphase.DefaultNarrowphaseFilterCallback;
import net.smert.frameworkgl.collision.narrowphase.NarrowphaseDispatch;
import net.smert.frameworkgl.collision.response.CollisionResolver;
import net.smert.frameworkgl.collision.response.DefaultCollisionResponseFilterCallback;
import net.smert.frameworkgl.collision.shapes.ShapeType;
import org.picocontainer.MutablePicoContainer;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class CollisionFactory {

    private final MutablePicoContainer container;

    public CollisionFactory(MutablePicoContainer collisionFactoryContainer) {
        container = collisionFactoryContainer;
    }

    public CollisionGameObject createCollisionGameObject(ShapeType shapeType) {

        // Would like to use constructor injection so this can be immutable but I have
        // no idea why Pico container won't let me do this. I can add a config but can't
        // remove one. You can't add a component twice and if I remove the component and
        // add it back it defeats the purpose of someone overriding it and injecting a
        // new class.
        CollisionGameObject gameObject = container.getComponent(CollisionGameObject.class);
        gameObject.initSetShapeType(shapeType);
        return gameObject;
    }

    public CollisionResolver createCollisionResolver() {
        return container.getComponent(CollisionResolver.class);
    }

    public CollisionWorld createCollisionWorld() {
        return container.getComponent(CollisionWorld.class);
    }

    public DefaultCollisionResponseFilterCallback createDefaultCollisionResponseFilterCallback() {
        return container.getComponent(DefaultCollisionResponseFilterCallback.class);
    }

    public DefaultNarrowphaseFilterCallback createDefaultNarrowphaseFilterCallback() {
        return container.getComponent(DefaultNarrowphaseFilterCallback.class);
    }

    public DefaultOverlappingPairFilterCallback createDefaultOverlappingPairFilterCallback() {
        return container.getComponent(DefaultOverlappingPairFilterCallback.class);
    }

    public DynamicAABBTreeBroadphase createDynamicAABBTreeBroadphase() {
        return container.getComponent(DynamicAABBTreeBroadphase.class);
    }

    public HashedOverlappingPairCache createHashedOverlappingPairCache() {
        return container.getComponent(HashedOverlappingPairCache.class);
    }

    public NarrowphaseDispatch createNarrowphaseDispatch() {
        return container.getComponent(NarrowphaseDispatch.class);
    }

    public SimpleBroadphase createSimpleBroadphase() {
        return container.getComponent(SimpleBroadphase.class);
    }

}
