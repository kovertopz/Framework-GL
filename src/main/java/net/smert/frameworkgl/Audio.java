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

import net.smert.frameworkgl.openal.AL;
import net.smert.frameworkgl.openal.OpenAL;
import net.smert.frameworkgl.openal.OpenALListener;
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
        if (openal != null) {
            openal.destroy();
            openal = null;
            initialized = false;
        }
    }

    public float getDefaultSourceMaxDistance() {
        return openal.getDefaultSourceMaxDistance();
    }

    public void setDefaultSourceMaxDistance(float defaultSourceMaxDistance) {
        openal.setDefaultSourceMaxDistance(defaultSourceMaxDistance);
    }

    public float getDefaultSourceMusicVolume() {
        return openal.getDefaultSourceMusicVolume();
    }

    public void setDefaultSourceMusicVolume(float defaultSourceMusicVolume) {
        openal.setDefaultSourceMusicVolume(defaultSourceMusicVolume);
    }

    public float getDefaultSourceReferenceDistance() {
        return openal.getDefaultSourceReferenceDistance();
    }

    public void setDefaultSourceReferenceDistance(float defaultSourceReferenceDistance) {
        openal.setDefaultSourceReferenceDistance(defaultSourceReferenceDistance);
    }

    public float getDefaultSourceRolloff() {
        return openal.getDefaultSourceRolloff();
    }

    public void setDefaultSourceRolloff(float defaultSourceRolloff) {
        openal.setDefaultSourceRolloff(defaultSourceRolloff);
    }

    public float getDefaultSourceSoundVolume() {
        return openal.getDefaultSourceSoundVolume();
    }

    public void setDefaultSourceSoundVolume(float defaultSourceSoundVolume) {
        openal.setDefaultSourceSoundVolume(defaultSourceSoundVolume);
    }

    public float getDopplerFactor() {
        return openal.getDopplerFactor();
    }

    public void setDopplerFactor(float factor) {
        openal.setDopplerFactor(factor);
    }

    public float getDopplerVelocity() {
        return openal.getDopplerVelocity();
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

    public int getContextFrequency() {
        return openal.getContextFrequency();
    }

    public void setContextFrequency(int contextFrequency) {
        openal.setContextFrequency(contextFrequency);
    }

    public int getContextRefresh() {
        return openal.getContextRefresh();
    }

    public void setContextRefresh(int contextRefresh) {
        openal.setContextRefresh(contextRefresh);
    }

    public int getDistanceModel() {
        return openal.getDistanceModel();
    }

    public int getMaxChannels() {
        return openal.getMaxChannels();
    }

    public void setMaxChannels(int maxChannels) {
        openal.setMaxChannels(maxChannels);
    }

    public int getNumberOfMusicChannels() {
        return openal.getNumberOfMusicChannels();
    }

    public void setNumberOfMusicChannels(int numberOfMusicChannels) {
        openal.setNumberOfMusicChannels(numberOfMusicChannels);
    }

    public int getNumberOfSoundChannels() {
        return openal.getNumberOfSoundChannels();
    }

    public void setNumberOfSoundChannels(int numberOfSoundChannels) {
        openal.setNumberOfSoundChannels(numberOfSoundChannels);
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

    public OpenALListener getListener() {
        return openal.getListener();
    }

    public void setListener(OpenALListener listener) {
        openal.setListener(listener);
    }

    public String getDeviceArguments() {
        return openal.getDeviceArguments();
    }

    public void setDeviceArguments(String deviceArguments) {
        openal.setDeviceArguments(deviceArguments);
    }

    public boolean isContextSynchronized() {
        return openal.isContextSynchronized();
    }

    public void setContextSynchronized(boolean contextSynchronized) {
        openal.setContextSynchronized(contextSynchronized);
    }

    public boolean isOpenDevice() {
        return openal.isOpenDevice();
    }

    public void setOpenDevice(boolean openDevice) {
        openal.setOpenDevice(openDevice);
    }

    public void init() {
        if (initialized) {
            return;
        }
        openal = AL.openal;
        openal.init();
        initialized = true;
    }

    public boolean isPlaying(int soundID) {
        return openal.isPlaying(soundID);
    }

    public void pause(int soundID) {
        openal.pause(soundID);
    }

    public int playMusic(String audioFile, boolean loop) {
        return openal.playMusic(audioFile, loop);
    }

    public int playMusic(String audioFile, boolean loop, boolean priority) {
        return openal.playMusic(audioFile, loop, priority);
    }

    public int playSound(String audioFile, boolean loop, boolean priority) {
        return openal.playSound(audioFile, loop, priority);
    }

    public int playSound(String audioFile, boolean loop, boolean priority, float x, float y, float z) {
        return openal.playSound(audioFile, loop, priority, x, y, z);
    }

    public int playSound(String audioFile, boolean loop, boolean priority, float x, float y, float z,
            float maxDistance) {
        return openal.playSound(audioFile, loop, priority, x, y, z, maxDistance);
    }

    public int playSound(String audioFile, boolean loop, boolean priority, float x, float y, float z,
            float maxDistance, float referenceDistance) {
        return openal.playSound(audioFile, loop, priority, x, y, z, maxDistance, referenceDistance);
    }

    public int playSound(String audioFile, boolean loop, boolean priority, float x, float y, float z,
            float maxDistance, float referenceDistance, float rolloff) {
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
