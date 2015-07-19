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
package net.smert.frameworkgl;

import java.io.IOException;
import net.smert.frameworkgl.openal.AL;
import net.smert.frameworkgl.openal.OpenAL;
import net.smert.frameworkgl.openal.codecs.Codec;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Audio {

    private boolean initialized;
    private OpenAL openal;

    public void createMusic() {
    }

    public void createSound() {
    }

    public void destroy() {
        if (!initialized) {
            return;
        }
        openal.destroy();
        openal = null;
        initialized = false;
    }

    public float getDopplerFactor() {
        return openal.getDopplerFactor();
    }

    public void setDopplerFactor(float factor) {
        openal.setDopplerFactor(factor);
    }

    public void setDopplerVelocity(float velocity) {
        openal.setDopplerVelocity(velocity);
    }

    public float getMasterVolume() {
        return openal.getMasterVolume();
    }

    public void setMasterVolume(float masterVolume) {
        openal.setMasterVolume(masterVolume);
    }

    public float getVolume(int soundID) {
        return openal.getVolume(soundID);
    }

    public void setVolume(int soundID, float volume) {
        openal.setVolume(soundID, volume);
    }

    public int getDistanceModel() {
        return openal.getDistanceModel();
    }

    public void setDistanceModelInverseDistance() {
        openal.setDistanceModelInverseDistance();
    }

    public void setDistanceModelInverseDistanceClamped() {
        openal.setDistanceModelInverseDistanceClamped();
    }

    public void setDistanceModelNone() {
        openal.setDistanceModelNone();
    }

    public OpenAL.Config getConfig() {
        return openal.getConfig();
    }

    public boolean isPlaying(int soundID) {
        return openal.isPlaying(soundID);
    }

    public void init() {
        if (initialized) {
            return;
        }
        openal = AL.openal;
        openal.init();
        initialized = true;
    }

    public void pause(int soundID) {
        openal.pause(soundID);
    }

    public int playMusic(String audioFile, boolean loop) throws IOException {
        return openal.playMusic(audioFile, loop);
    }

    public int playMusic(String audioFile, boolean loop, boolean priority) throws IOException {
        return openal.playMusic(audioFile, loop, priority);
    }

    public int playSound(String audioFile, boolean loop, boolean priority) throws IOException {
        return openal.playSound(audioFile, loop, priority);
    }

    public int playSound(String audioFile, boolean loop, boolean priority, float x, float y, float z)
            throws IOException {
        return openal.playSound(audioFile, loop, priority, x, y, z);
    }

    public int playSound(String audioFile, boolean loop, boolean priority, float x, float y, float z,
            float maxDistance) throws IOException {
        return openal.playSound(audioFile, loop, priority, x, y, z, maxDistance);
    }

    public int playSound(String audioFile, boolean loop, boolean priority, float x, float y, float z,
            float maxDistance, float referenceDistance) throws IOException {
        return openal.playSound(audioFile, loop, priority, x, y, z, maxDistance, referenceDistance);
    }

    public int playSound(String audioFile, boolean loop, boolean priority, float x, float y, float z,
            float maxDistance, float referenceDistance, float rolloff) throws IOException {
        return openal.playSound(audioFile, loop, priority, x, y, z, maxDistance, referenceDistance, rolloff);
    }

    public void registerCodec(String extension, Codec codec) {
        openal.registerCodec(extension, null);
    }

    public void resume(int soundID) {
        openal.resume(soundID);
    }

    public void rewind(int soundID) {
        openal.rewind(soundID);
    }

    public void stop(int soundID) {
        openal.stop(soundID);
    }

    public void unregisterCodec(String extension) {
        openal.unregisterCodec(extension);
    }

    public void update() {
        openal.update();
    }

}
