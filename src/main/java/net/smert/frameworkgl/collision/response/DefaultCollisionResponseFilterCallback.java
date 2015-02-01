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
package net.smert.frameworkgl.collision.response;

import net.smert.frameworkgl.collision.CollisionGameObject;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class DefaultCollisionResponseFilterCallback implements CollisionResponseFilterCallback {

    @Override
    public boolean needsCollisionResponse(CollisionGameObject collisionGameObject0,
            CollisionGameObject collisionGameObject1) {
        return ((collisionGameObject0.hasContactResponse()) && (collisionGameObject1.hasContactResponse()));
    }

}
