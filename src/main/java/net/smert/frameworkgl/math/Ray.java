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
package net.smert.frameworkgl.math;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Ray {

    final Vector3f direction = new Vector3f();
    final Vector3f origin = new Vector3f();

    public Vector3f getDirection() {
        return direction;
    }

    public Ray setDirection(float x, float y, float z) {
        this.direction.set(x, y, z);
        return this;
    }

    public Ray setDirection(Vector3f direction) {
        this.direction.set(direction);
        return this;
    }

    public Vector3f getOrigin() {
        return origin;
    }

    public Ray setOrigin(float x, float y, float z) {
        this.origin.set(x, y, z);
        return this;
    }

    public Ray setOrigin(Vector3f origin) {
        this.origin.set(origin);
        return this;
    }

    @Override
    public String toString() {
        return "(origin: " + origin + " direction: " + direction + ")";
    }

}
