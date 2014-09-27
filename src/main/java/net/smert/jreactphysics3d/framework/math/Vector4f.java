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
package net.smert.jreactphysics3d.framework.math;

import java.nio.FloatBuffer;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Vector4f {

    float w;
    float x;
    float y;
    float z;

    public Vector4f() {
        w = 0;
        x = 0;
        y = 0;
        z = 0;
    }

    public Vector4f(float x, float y, float z, float w) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getW() {
        return w;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void set(float x, float y, float z, float w) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(Vector4f vector) {
        w = vector.w;
        x = vector.x;
        y = vector.y;
        z = vector.z;
    }

    public void setW(float w) {
        this.w = w;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public void toFloatBuffer(FloatBuffer fb) {
        fb.put(x);
        fb.put(y);
        fb.put(z);
        fb.put(w);
    }

    @Override
    public String toString() {
        return "(x: " + x + " y: " + y + " z: " + z + " w: " + w + ")";
    }

}
