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
package net.smert.frameworkgl.openal.codecs.ogg;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import net.smert.frameworkgl.Files;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.openal.codecs.Codec;
import net.smert.frameworkgl.opengl.GL;
import org.lwjgl.openal.AL10;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class OGGCodec implements Codec {

    private final static Logger log = LoggerFactory.getLogger(OGGCodec.class);

    private Data create(OggInputStream ois) throws IOException {

        // Read data
        byte[] byteArray = new byte[128 * 1024];
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        int read;
        int total = 0;
        while ((read = ois.read(byteArray)) != -1) {
            total += read;
            result.write(byteArray);
        }

        // Create buffer
        ByteBuffer buffer = GL.bufferHelper.createByteBuffer(total);
        buffer.put(result.toByteArray(), 0, total).flip();

        // Create codec data
        Data data = new Data();
        data.buffer = buffer;
        data.channels = ois.getChannels();
        data.format = (data.channels == 2) ? AL10.AL_FORMAT_STEREO16 : AL10.AL_FORMAT_MONO16;
        data.sampleRate = ois.getSampleRate();

        return data;
    }

    @Override
    public Data load(String audioFile) throws IOException {
        log.info("Loading OGG audio file: {}", audioFile);
        Files.FileAsset fileAsset = Fw.files.getAudio(audioFile);

        try (
                InputStream is = fileAsset.openStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                OggInputStream ois = new OggInputStream(bis)) {
            return create(ois);
        } catch (IOException ex) {
            throw ex;
        }
    }

}
