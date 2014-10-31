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

import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Audio {

    private SoundSystem soundSystem;

    public void backgroundMusic(String audioFile) {
        Files.FileAsset audio = Fw.files.getAudio(audioFile);
        soundSystem.backgroundMusic(audioFile, audio.toURL(), audio.getFullPathToFile(), true);
    }

    public void destroy() {
        if (soundSystem != null) {
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

    public void init() {

        // Libraries and codecs are loaded in BootStrap
        soundSystem = new SoundSystem();
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

    public void resume(String sourceName) {
        soundSystem.play(sourceName);
    }

    public void stop(String sourceName) {
        soundSystem.stop(sourceName);
    }

}
