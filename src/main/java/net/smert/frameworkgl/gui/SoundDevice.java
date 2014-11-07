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
package net.smert.frameworkgl.gui;

import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.spi.sound.SoundHandle;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;
import java.io.IOException;
import net.smert.frameworkgl.Fw;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class SoundDevice implements de.lessvoid.nifty.spi.sound.SoundDevice {

    @Override
    public void setResourceLoader(NiftyResourceLoader niftyResourceLoader) {
    }

    @Override
    public SoundHandle loadSound(SoundSystem soundSystem, String filename) {
        return new Sound(filename);
    }

    @Override
    public SoundHandle loadMusic(SoundSystem soundSystem, String filename) {
        return new Music(filename);
    }

    @Override
    public void update(int delta) {
    }

    public static abstract class AbstractAudio implements SoundHandle {

        protected float volume;
        protected int sourceID;
        protected final String audioFile;

        public AbstractAudio(String audioFile) {
            volume = 1f;
            sourceID = 0;
            this.audioFile = audioFile;
        }

        @Override
        public void stop() {
            Fw.audio.stop(sourceID);
        }

        @Override
        public void setVolume(float volume) {
            this.volume = volume;
            Fw.audio.setVolume(sourceID, volume);
        }

        @Override
        public float getVolume() {
            if (sourceID == 0) {
                return volume;
            }
            return Fw.audio.getVolume(sourceID);
        }

        @Override
        public boolean isPlaying() {
            return Fw.audio.isPlaying(sourceID);
        }

        @Override
        public void dispose() {
            // Objects are automatically destroyed by the audio system
        }

    }

    public static class Music extends AbstractAudio {

        public Music(String audioFile) {
            super(audioFile);
        }

        @Override
        public void play() {
            try {
                sourceID = Fw.audio.playMusic(audioFile, false);
                setVolume(volume);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    public static class Sound extends AbstractAudio {

        public Sound(String audioFile) {
            super(audioFile);
        }

        @Override
        public void play() {
            try {
                sourceID = Fw.audio.playSound(audioFile, false, true);
                setVolume(volume);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

}
