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
import net.smert.frameworkgl.opengl.pipeline.AbstractRenderingPipeline.DebugRenderCallback;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public interface BroadphaseAlgorithm {

    public BroadphaseProxy createProxy(CollisionGameObject collisionGameObject, int collisionGroup,
            int collisionCollidesWith, AABB worldAabb);

    public void destroyDebugRender();

    public DebugRenderCallback getDebugRenderCallback();

    public OverlappingPairCache getOverlappingPairCache();

    public boolean moveProxy(BroadphaseProxy broadphaseProxy, AABB worldAabb);

    public void removeProxy(BroadphaseProxy broadphaseProxy);

    public void updateOverlappingPairs();

}
