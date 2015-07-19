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
package net.smert.frameworkgl.openal.helpers;

import java.nio.FloatBuffer;
import org.lwjgl.openal.AL10;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class ALListenerHelper {

    public int getConstNoError() {
        return AL10.AL_NO_ERROR;
    }

    /**
     * Clears the error state so multiple calls will not return the same value
     *
     * @return
     */
    public int getError() {
        return AL10.alGetError();
    }

    public void getGain() {
        AL10.alGetListenerf(AL10.AL_GAIN);
    }

    public void getOrientation(FloatBuffer orientation) {
        AL10.alGetListenerfv(AL10.AL_ORIENTATION, orientation);
    }

    public void getPosition(FloatBuffer position) {
        AL10.alGetListenerfv(AL10.AL_POSITION, position);
    }

    public void getVelocity(FloatBuffer velocity) {
        AL10.alGetListenerfv(AL10.AL_VELOCITY, velocity);
    }

    public void setGain(float gain) {
        AL10.alListenerf(AL10.AL_GAIN, gain);
    }

    public void setOrientation(FloatBuffer orientation) {
        AL10.alListenerfv(AL10.AL_ORIENTATION, orientation);
    }

    public void setPosition(float x, float y, float z) {
        AL10.alListener3f(AL10.AL_POSITION, x, y, z);
    }

    public void setPosition(FloatBuffer position) {
        AL10.alListenerfv(AL10.AL_POSITION, position);
    }

    public void setVelocity(float x, float y, float z) {
        AL10.alListener3f(AL10.AL_VELOCITY, x, y, z);
    }

    public void setVelocity(FloatBuffer velocity) {
        AL10.alListenerfv(AL10.AL_VELOCITY, velocity);
    }

}
