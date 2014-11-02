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
import java.nio.IntBuffer;
import org.lwjgl.openal.AL10;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class ALSourceHelper {

    public int create() {
        return AL10.alGenSources();
    }

    public void delete(int sourceID) {
        AL10.alDeleteSources(sourceID);
    }

    public int getBuffer(int sourceID) {
        return AL10.alGetSourcei(sourceID, AL10.AL_BUFFER);
    }

    public int getBuffersProcessed(int sourceID) {
        return AL10.alGetSourcei(sourceID, AL10.AL_BUFFERS_PROCESSED);
    }

    public int getBuffersQueued(int sourceID) {
        return AL10.alGetSourcei(sourceID, AL10.AL_BUFFERS_QUEUED);
    }

    public float getConeInnerAngle(int sourceID) {
        return AL10.alGetSourcef(sourceID, AL10.AL_CONE_INNER_ANGLE);
    }

    public float getConeOuterAngle(int sourceID) {
        return AL10.alGetSourcef(sourceID, AL10.AL_CONE_OUTER_ANGLE);
    }

    public float getConeOuterGain(int sourceID) {
        return AL10.alGetSourcef(sourceID, AL10.AL_CONE_OUTER_GAIN);
    }

    public int getConstFalse() {
        return AL10.AL_FALSE;
    }

    public int getConstNoError() {
        return AL10.AL_NO_ERROR;
    }

    public int getConstTrue() {
        return AL10.AL_TRUE;
    }

    public void getDirection(int sourceID, FloatBuffer direction) {
        AL10.alGetSource(sourceID, AL10.AL_DIRECTION, direction);
    }

    /**
     * Clears the error state so multiple calls will not return the same value
     *
     * @return
     */
    public int getError() {
        return AL10.alGetError();
    }

    public float getGain(int sourceID) {
        return AL10.alGetSourcef(sourceID, AL10.AL_GAIN);
    }

    public int getLooping(int sourceID) {
        return AL10.alGetSourcei(sourceID, AL10.AL_LOOPING);
    }

    public float getMaxDistance(int sourceID) {
        return AL10.alGetSourcef(sourceID, AL10.AL_MAX_DISTANCE);
    }

    public float getMaxGain(int sourceID) {
        return AL10.alGetSourcef(sourceID, AL10.AL_MAX_GAIN);
    }

    public float getMinGain(int sourceID) {
        return AL10.alGetSourcef(sourceID, AL10.AL_MIN_GAIN);
    }

    public float getPitch(int sourceID) {
        return AL10.alGetSourcef(sourceID, AL10.AL_PITCH);
    }

    public void getPosition(int sourceID, FloatBuffer position) {
        AL10.alGetSource(sourceID, AL10.AL_POSITION, position);
    }

    public float getReferenceDistance(int sourceID) {
        return AL10.alGetSourcef(sourceID, AL10.AL_REFERENCE_DISTANCE);
    }

    public float getRolloffFactor(int sourceID) {
        return AL10.alGetSourcef(sourceID, AL10.AL_ROLLOFF_FACTOR);
    }

    public int getSourceRelative(int sourceID) {
        return AL10.alGetSourcei(sourceID, AL10.AL_SOURCE_RELATIVE);
    }

    public int getSourceState(int sourceID) {
        return AL10.alGetSourcei(sourceID, AL10.AL_SOURCE_STATE);
    }

    public int getSourceType(int bufferID) {
        return AL10.alGetSourcei(bufferID, AL10.AL_SOURCE_TYPE);
    }

    public void getVelocity(int sourceID, FloatBuffer velocity) {
        AL10.alGetSource(sourceID, AL10.AL_VELOCITY, velocity);
    }

    public boolean isValid(int sourceID) {
        return AL10.alIsSource(sourceID);
    }

    public void rewind(int sourceID) {
        AL10.alSourceRewind(sourceID);
    }

    public void pause(int sourceID) {
        AL10.alSourcePause(sourceID);
    }

    public void play(int sourceID) {
        AL10.alSourcePlay(sourceID);
    }

    public void queueBuffers(int sourceID, int bufferID) {
        AL10.alSourceQueueBuffers(sourceID, bufferID);
    }

    public void queueBuffers(int sourceID, IntBuffer buffers) {
        AL10.alSourceQueueBuffers(sourceID, buffers);
    }

    public void setBuffer(int sourceID, int bufferID) {
        AL10.alSourcei(sourceID, AL10.AL_BUFFER, bufferID);
    }

    public void setConeInnerAngle(int sourceID, float coneInnerAngle) {
        AL10.alSourcef(sourceID, AL10.AL_CONE_INNER_ANGLE, coneInnerAngle);
    }

    public void setConeOuterAngle(int sourceID, float coneOuterAngle) {
        AL10.alSourcef(sourceID, AL10.AL_CONE_OUTER_ANGLE, coneOuterAngle);
    }

    public void setConeOuterGain(int sourceID, float coneOuterGain) {
        AL10.alSourcef(sourceID, AL10.AL_CONE_OUTER_GAIN, coneOuterGain);
    }

    public void setDirection(int sourceID, float x, float y, float z) {
        AL10.alSource3f(sourceID, AL10.AL_DIRECTION, x, y, z);
    }

    public void setDirection(int sourceID, FloatBuffer direction) {
        AL10.alSource(sourceID, AL10.AL_DIRECTION, direction);
    }

    public void setGain(int sourceID, float gain) {
        AL10.alSourcef(sourceID, AL10.AL_GAIN, gain);
    }

    public void setLooping(int sourceID, int trueOrFalse) {
        AL10.alSourcei(sourceID, AL10.AL_LOOPING, trueOrFalse);
    }

    public void setMaxDistance(int sourceID, float maxDistance) {
        AL10.alSourcef(sourceID, AL10.AL_MAX_DISTANCE, maxDistance);
    }

    public void setMaxGain(int sourceID, float maxGain) {
        AL10.alSourcef(sourceID, AL10.AL_MAX_GAIN, maxGain);
    }

    public void setMinGain(int sourceID, float minGain) {
        AL10.alSourcef(sourceID, AL10.AL_MIN_GAIN, minGain);
    }

    public void setPitch(int sourceID, float pitch) {
        AL10.alSourcef(sourceID, AL10.AL_PITCH, pitch);
    }

    public void setPosition(int sourceID, float x, float y, float z) {
        AL10.alSource3f(sourceID, AL10.AL_POSITION, x, y, z);
    }

    public void setPosition(int sourceID, FloatBuffer position) {
        AL10.alSource(sourceID, AL10.AL_POSITION, position);
    }

    public void setReferenceDistance(int sourceID, float referenceDistance) {
        AL10.alSourcef(sourceID, AL10.AL_REFERENCE_DISTANCE, referenceDistance);
    }

    public void setRolloffFactor(int sourceID, float rolloffFactor) {
        AL10.alSourcef(sourceID, AL10.AL_ROLLOFF_FACTOR, rolloffFactor);
    }

    public void setSourceRelative(int sourceID, int trueOrFalse) {
        AL10.alSourcef(sourceID, AL10.AL_SOURCE_RELATIVE, trueOrFalse);
    }

    public void setSourceStateInitial(int sourceID) {
        AL10.alSourcef(sourceID, AL10.AL_SOURCE_STATE, AL10.AL_INITIAL);
    }

    public void setSourceStatePaused(int sourceID) {
        AL10.alSourcef(sourceID, AL10.AL_SOURCE_STATE, AL10.AL_PAUSED);
    }

    public void setSourceStatePlaying(int sourceID) {
        AL10.alSourcef(sourceID, AL10.AL_SOURCE_STATE, AL10.AL_PLAYING);
    }

    public void setSourceStateStopped(int sourceID) {
        AL10.alSourcef(sourceID, AL10.AL_SOURCE_STATE, AL10.AL_STOPPED);
    }

    public void setVelocity(int sourceID, float x, float y, float z) {
        AL10.alSource3f(sourceID, AL10.AL_VELOCITY, x, y, z);
    }

    public void setVelocity(int sourceID, FloatBuffer velocity) {
        AL10.alSource(sourceID, AL10.AL_VELOCITY, velocity);
    }

    public void stop(int sourceID) {
        AL10.alSourceStop(sourceID);
    }

    public void unqueueBuffers(int sourceID) {
        AL10.alSourceUnqueueBuffers(sourceID);
    }

    public void unqueueBuffers(int sourceID, IntBuffer buffers) {
        AL10.alSourceUnqueueBuffers(sourceID, buffers);
    }

}
