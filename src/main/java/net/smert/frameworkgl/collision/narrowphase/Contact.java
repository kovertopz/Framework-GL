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
import net.smert.frameworkgl.math.Vector3f;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Contact {

    public float penetration;
    public int index;
    public int next;
    public CollisionGameObject collisionGameObject0;
    public CollisionGameObject collisionGameObject1;
    public final Vector3f normal = new Vector3f(); // On collisionGameObject0 pointing to collisionGameObject1

    @Override
    public String toString() {
        return "(collisionGameObject0=" + collisionGameObject0 + " collisionGameObject1= " + collisionGameObject1
                + " normal= " + normal + " penetration= " + penetration + ")";
    }

}
