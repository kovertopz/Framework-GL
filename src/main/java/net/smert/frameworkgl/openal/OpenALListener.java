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
package net.smert.frameworkgl.openal;

import java.nio.FloatBuffer;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.opengl.GL;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class OpenALListener {

    private final static FloatBuffer BUFFER = GL.bufferHelper.createFloatBuffer(6);

    private float gain;
    private final Vector3f orientatonLook;
    private final Vector3f orientatonUp;
    private final Vector3f position;
    private final Vector3f velocity;

    public OpenALListener() {
        orientatonLook = new Vector3f();
        orientatonUp = new Vector3f();
        position = new Vector3f();
        velocity = new Vector3f();
        reset();
    }

    public float getGain() {
        return gain;
    }

    public void setGain(float gain) {
        this.gain = gain;
    }

    public Vector3f getOrientatonLook() {
        return orientatonLook;
    }

    public void setOrientatonLook(float x, float y, float z) {
        this.orientatonLook.set(x, y, z);
    }

    public void setOrientatonLook(Vector3f orientatonLook) {
        this.orientatonLook.set(orientatonLook);
    }

    public Vector3f getOrientatonUp() {
        return orientatonUp;
    }

    public void setOrientatonUp(float x, float y, float z) {
        this.orientatonUp.set(x, y, z);
    }

    public void setOrientatonUp(Vector3f orientatonUp) {
        this.orientatonUp.set(orientatonUp);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.set(x, y, z);
    }

    public void setPosition(Vector3f position) {
        this.position.set(position);
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public void setVelocity(float x, float y, float z) {
        this.velocity.set(x, y, z);
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity.set(velocity);
    }

    public final void reset() {
        gain = .8f;
        orientatonLook.set(0f, 0f, -1f);
        orientatonUp.set(0f, 1f, 0f);
        position.zero();
        velocity.zero();
    }

    public void update() {
        AL.listenerHelper.setGain(gain);
        BUFFER.clear();
        orientatonLook.toFloatBuffer(BUFFER);
        orientatonUp.toFloatBuffer(BUFFER);
        BUFFER.flip();
        AL.listenerHelper.setOrientation(BUFFER);
        BUFFER.clear();
        position.toFloatBuffer(BUFFER);
        BUFFER.flip();
        AL.listenerHelper.setPosition(BUFFER);
        BUFFER.clear();
        velocity.toFloatBuffer(BUFFER);
        BUFFER.flip();
        AL.listenerHelper.setVelocity(BUFFER);
    }

}
