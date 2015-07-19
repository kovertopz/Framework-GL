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

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.openal.AL10;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class ALBufferHelper {

    public int create() {
        return AL10.alGenBuffers();
    }

    public void delete(int bufferID) {
        AL10.alDeleteBuffers(bufferID);
    }

    public int getBits(int bufferID) {
        return AL10.alGetBufferi(bufferID, AL10.AL_BITS);
    }

    public int getChannels(int bufferID) {
        return AL10.alGetBufferi(bufferID, AL10.AL_CHANNELS);
    }

    public int getConstFalse() {
        return AL10.AL_FALSE;
    }

    public int getConstFormatMono8() {
        return AL10.AL_FORMAT_MONO8;
    }

    public int getConstFormatMono16() {
        return AL10.AL_FORMAT_MONO16;
    }

    public int getConstFormatStereo8() {
        return AL10.AL_FORMAT_STEREO8;
    }

    public int getConstFormatStereo16() {
        return AL10.AL_FORMAT_STEREO16;
    }

    public int getConstNoError() {
        return AL10.AL_NO_ERROR;
    }

    public int getConstTrue() {
        return AL10.AL_TRUE;
    }

    /**
     * Clears the error state so multiple calls will not return the same value
     *
     * @return
     */
    public int getError() {
        return AL10.alGetError();
    }

    public int getFrequency(int bufferID) {
        return AL10.alGetBufferi(bufferID, AL10.AL_FREQUENCY);
    }

    public int getSize(int bufferID) {
        return AL10.alGetBufferi(bufferID, AL10.AL_SIZE);
    }

    public boolean isValid(int bufferID) {
        return AL10.alIsBuffer(bufferID);
    }

    public void setData(int bufferID, int format, ByteBuffer data, int freq) {
        AL10.alBufferData(bufferID, format, data, freq);
    }

    public void setData(int bufferID, int format, IntBuffer data, int freq) {
        AL10.alBufferData(bufferID, format, data, freq);
    }

    public void setData(int bufferID, int format, ShortBuffer data, int freq) {
        AL10.alBufferData(bufferID, format, data, freq);
    }

}
