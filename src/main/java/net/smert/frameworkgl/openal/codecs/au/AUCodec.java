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
package net.smert.frameworkgl.openal.codecs.au;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import net.smert.frameworkgl.Files;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.openal.codecs.Codec;
import net.smert.frameworkgl.openal.codecs.Codec.Data;
import net.smert.frameworkgl.openal.codecs.Conversion;
import org.lwjgl.openal.AL10;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class AUCodec implements Codec {

    private final static Logger log = LoggerFactory.getLogger(AUCodec.class);

    private Data create(AudioInputStream ais) throws IOException {

        // Get the audio format
        AudioFormat audioFormat = ais.getFormat();

        // Determine format based on the number of channels and sample size
        int channels = audioFormat.getChannels();
        int format = 0;
        int sampleSizeInBits = audioFormat.getSampleSizeInBits();
        if (channels == 1) {
            if (sampleSizeInBits == 8) {
                format = AL10.AL_FORMAT_MONO8;
            } else if (sampleSizeInBits == 16) {
                format = AL10.AL_FORMAT_MONO16;
            } else {
                throw new RuntimeException("Sample size in bits must be 8 or 16: " + sampleSizeInBits);
            }
        } else if (audioFormat.getChannels() == 2) {
            if (sampleSizeInBits == 8) {
                format = AL10.AL_FORMAT_STEREO8;
            } else if (sampleSizeInBits == 16) {
                format = AL10.AL_FORMAT_STEREO16;
            } else {
                throw new RuntimeException("Sample size in bits must be 8 or 16: " + sampleSizeInBits);
            }
        } else {
            throw new RuntimeException("Channels must be 1 or 2: " + channels);
        }

        // Read data
        int available = ais.available();
        assert (available > 0);
        byte[] byteArray = new byte[available];
        int read;
        int total = 0;
        while ((read = ais.read(byteArray, total, byteArray.length - total)) != -1 && (total < byteArray.length)) {
            total += read;
        }

        // Convert data
        ByteBuffer buffer = Conversion.ConvertEndianness(byteArray, sampleSizeInBits / 8, ByteOrder.nativeOrder(),
                audioFormat.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);

        // Create codec data
        Data data = new Data();
        data.buffer = buffer;
        data.channels = channels;
        data.format = format;
        data.sampleRate = (int) audioFormat.getSampleRate();

        return data;
    }

    @Override
    public Data load(String audioFile) throws IOException {
        log.info("Loading AU audio file: {}", audioFile);
        Files.FileAsset fileAsset = Fw.files.getAudio(audioFile);

        // Open file stream and read data
        try (InputStream is = fileAsset.openStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                AudioInputStream ais = AudioSystem.getAudioInputStream(bis)) {
            return create(ais);
        } catch (UnsupportedAudioFileException ex) {
            throw new IOException(ex);
        }
    }

}
