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
public class Transform4f {

    Matrix3f rotation = new Matrix3f();
    Vector3f position = new Vector3f();

    public Transform4f() {
        rotation.identity();
    }

    public Transform4f setPosition(float x, float y, float z) {
        this.position.set(x, y, z);
        return this;
    }

    public void toFloatBuffer(FloatBuffer fbout) {
        fbout.put(rotation.xAxis.x);
        fbout.put(rotation.xAxis.y);
        fbout.put(rotation.xAxis.z);
        fbout.put(0);
        fbout.put(rotation.yAxis.x);
        fbout.put(rotation.yAxis.y);
        fbout.put(rotation.yAxis.z);
        fbout.put(0);
        fbout.put(rotation.zAxis.x);
        fbout.put(rotation.zAxis.y);
        fbout.put(rotation.zAxis.z);
        fbout.put(0);
        fbout.put(position.x);
        fbout.put(position.y);
        fbout.put(position.z);
        fbout.put(1.0f);
    }

    @Override
    public String toString() {
        return "(position: " + position + " rotation: " + rotation + ")";
    }

}
