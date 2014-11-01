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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOgg;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryJavaSound;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Audio {

    private boolean initialized;
    private final List<Class> librariesToLoad;
    private final Map<String, Class> codecsToLoad;
    private SoundSystem soundSystem;

    public Audio() {
        librariesToLoad = new ArrayList<>();
        codecsToLoad = new HashMap<>();
        reset();
    }

    public void addCodec(String extension, Class clazz) {
        codecsToLoad.put(extension, clazz);
    }

    public void addLibrary(Class clazz) {
        librariesToLoad.add(clazz);
    }

    public String backgroundMusic(String audioFile, boolean toLoop) {
        Files.FileAsset audio = Fw.files.getAudio(audioFile);
        soundSystem.backgroundMusic(audioFile, audio.toURL(), audio.getFullPathToFile(), toLoop);
        return audioFile;
    }

    public void clearCodecsToLoad() {
        codecsToLoad.clear();
    }

    public void clearLibrariesToLoad() {
        librariesToLoad.clear();
    }

    public void destroy() {
        if (soundSystem != null) {
            initialized = false;
            soundSystem.cleanup();
            soundSystem = null;
        }
    }

    public void fadeOut(String sourceName, String audioFile, long milliseconds) {
        Files.FileAsset audio = Fw.files.getAudio(audioFile);
        soundSystem.fadeOut(sourceName, audio.toURL(), audio.getFullPathToFile(), milliseconds);
    }

    public void fadeOutIn(String sourceName, String audioFile, long millisecondsOut, long millisecondsIn) {
        Files.FileAsset audio = Fw.files.getAudio(audioFile);
        soundSystem.fadeOutIn(sourceName, audio.toURL(), audio.getFullPathToFile(), millisecondsOut, millisecondsIn);
    }

    public float getMasterVolume() {
        return soundSystem.getMasterVolume();
    }

    public void setMasterVolume(float volume) {
        soundSystem.setMasterVolume(volume);
    }

    public float getPitch(String sourceName) {
        return soundSystem.getPitch(sourceName);
    }

    public void setPitch(String sourceName, float pitch) {
        soundSystem.setPitch(sourceName, pitch);
    }

    public float getVolume(String sourceName) {
        return soundSystem.getVolume(sourceName);
    }

    public void setVolume(String sourceName, float volume) {
        soundSystem.setVolume(sourceName, volume);
    }

    public void init() {
        if (initialized) {
            return;
        }

        // Load libraries and codecs
        try {
            for (Class clazz : librariesToLoad) {
                SoundSystemConfig.addLibrary(clazz);
            }
            Iterator<Map.Entry<String, Class>> iterator = codecsToLoad.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Class> entry = iterator.next();
                SoundSystemConfig.setCodec(entry.getKey(), entry.getValue());
            }
            SoundSystemConfig.setSoundFilesPackage("");
        } catch (SoundSystemException ex) {
            throw new RuntimeException(ex);
        }

        // Create sound system
        soundSystem = new SoundSystem();
        initialized = true;
    }

    public boolean isPlaying(String sourceName) {
        return soundSystem.playing(sourceName);
    }

    public void load(String audioFile) {
        Files.FileAsset audio = Fw.files.getAudio(audioFile);
        soundSystem.loadSound(audio.toURL(), audio.getFullPathToFile());
    }

    public void pause(String sourceName) {
        soundSystem.pause(sourceName);
    }

    public String play(String audioFile, boolean priority, boolean toLoop) {
        return play(audioFile, priority, toLoop, 0, 0, 0);
    }

    public String play(String audioFile, boolean priority, boolean toLoop, float x, float y, float z) {
        Files.FileAsset audio = Fw.files.getAudio(audioFile);
        return soundSystem.quickPlay(priority, audio.toURL(), audio.getFullPathToFile(), toLoop, x, y, z,
                SoundSystemConfig.ATTENUATION_NONE, 0);
    }

    public String playWithLinearAttenuation(String audioFile, boolean priority, boolean toLoop, float x, float y,
            float z, float maxDistance) {
        Files.FileAsset audio = Fw.files.getAudio(audioFile);
        return soundSystem.quickPlay(priority, audio.toURL(), audio.getFullPathToFile(), toLoop, x, y, z,
                SoundSystemConfig.ATTENUATION_LINEAR, maxDistance);
    }

    public String playWithLogrithmicAttenuation(String audioFile, boolean priority, boolean toLoop, float x, float y,
            float z, float maxDistance) {
        Files.FileAsset audio = Fw.files.getAudio(audioFile);
        return soundSystem.quickPlay(priority, audio.toURL(), audio.getFullPathToFile(), toLoop, x, y, z,
                SoundSystemConfig.ATTENUATION_ROLLOFF, maxDistance);
    }

    public void remove(String sourceName) {
        soundSystem.removeSource(sourceName);
    }

    public final void reset() {
        initialized = false;
        librariesToLoad.clear();
        codecsToLoad.clear();
        soundSystem = null;
        addLibrary(LibraryLWJGLOpenAL.class);
        addLibrary(LibraryJavaSound.class);
        addCodec("ogg", CodecJOgg.class);
        addCodec("wav", CodecWav.class);
    }

    public void resume(String sourceName) {
        soundSystem.play(sourceName);
    }

    public void setLinearAttenuation(String sourceName) {
        soundSystem.setAttenuation(sourceName, SoundSystemConfig.ATTENUATION_LINEAR);
    }

    public void setListenerAngle(float radians) {
        soundSystem.setListenerAngle(radians);
    }

    public void setListenerOrientation(float lookX, float lookY, float lookZ, float upX, float upY, float upZ) {
        soundSystem.setListenerOrientation(lookX, lookY, lookZ, upX, upY, upZ);
    }

    public void setListenerPosition(float x, float y, float z) {
        soundSystem.setListenerPosition(x, y, z);
    }

    public void setListenerVelocity(float x, float y, float z) {
        soundSystem.setListenerVelocity(x, y, z);
    }

    public void setLogrithmicAttenuation(String sourceName) {
        soundSystem.setAttenuation(sourceName, SoundSystemConfig.ATTENUATION_ROLLOFF);
    }

    public void setLooping(String sourceName, boolean toLoop) {
        soundSystem.setLooping(sourceName, toLoop);
    }

    public void setMaxDistance(String sourceName, float maxDistance) {
        soundSystem.setDistOrRoll(sourceName, maxDistance);
    }

    public void setNoAttenuation(String sourceName) {
        soundSystem.setAttenuation(sourceName, SoundSystemConfig.ATTENUATION_NONE);
    }

    public void setPosition(String sourceName, float x, float y, float z) {
        soundSystem.setPosition(sourceName, x, y, z);
    }

    public void setPriority(String sourceName, boolean priority) {
        soundSystem.setPriority(sourceName, priority);
    }

    public void setTemporary(String sourceName, boolean temporary) {
        soundSystem.setTemporary(sourceName, temporary);
    }

    public void setVelocity(String sourceName, float x, float y, float z) {
        soundSystem.setVelocity(sourceName, x, y, z);
    }

    public void stop(String sourceName) {
        soundSystem.stop(sourceName);
    }

}
