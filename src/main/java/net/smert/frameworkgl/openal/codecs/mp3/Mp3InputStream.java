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
package net.smert.frameworkgl.openal.codecs.mp3;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.DecoderException;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.MP3Decoder;
import javazoom.jl.decoder.OutputBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Mp3InputStream extends InputStream {

    private final static int MAX_READ_SIZE = 96 * 1024;
    private final static int MP3_BUFFER_SIZE = 128 * 1024;
    private final static Logger log = LoggerFactory.getLogger(Mp3InputStream.class);

    private boolean bigEndian;
    private boolean endOfStream;
    private boolean initialized;
    private int channels;
    private int frameCount;
    private int mp3BufferIndex;
    private int sampleRate;
    private Bitstream mp3Bitstream;
    private ByteBuffer mp3Buffer;
    private final InputStream in;
    private MP3Decoder mp3Decoder;
    private OutputBuffer mp3OutputBuffer;

    public Mp3InputStream(InputStream in) {
        this.in = in;
        init();
    }

    private void init() {
        bigEndian = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);
        endOfStream = false;
        initialized = false;
        channels = -1;
        frameCount = 0;
        mp3BufferIndex = 0;
        sampleRate = -1;
        mp3Bitstream = new Bitstream(in);
        mp3Buffer = ByteBuffer.allocateDirect(MP3_BUFFER_SIZE).order(ByteOrder.nativeOrder());
        mp3Buffer.limit(0);
        mp3Decoder = new MP3Decoder();
    }

    private void readHeader(Header header) {
        channels = (header.mode() == Header.SINGLE_CHANNEL) ? 1 : 2;
        mp3OutputBuffer = new OutputBuffer(channels, bigEndian);
        mp3Decoder.setOutputBuffer(mp3OutputBuffer);
        sampleRate = header.getSampleRate();
    }

    private void readMP3() throws IOException {

        // Return if we are at the end of the stream
        if (endOfStream) {
            return;
        }

        int total = 0;
        while (total < MAX_READ_SIZE) {
            Header header;
            try {
                header = mp3Bitstream.readFrame();
            } catch (BitstreamException ex) {
                throw new IOException(ex);
            }
            if (header == null) {
                endOfStream = true;
                break;
            }
            frameCount++;
            if (!initialized) {
                readHeader(header);
                initialized = true;
            }
            try {
                mp3Decoder.decodeFrame(header, mp3Bitstream);
            } catch (DecoderException ex) {
                throw new IOException(ex);
            }
            mp3Bitstream.closeFrame();
            int bytesRead = mp3OutputBuffer.reset();
            mp3Buffer.put(mp3OutputBuffer.getBuffer(), 0, bytesRead);
            total += bytesRead;
        }

        // Flip the buffer once after reading
        mp3Buffer.flip();
    }

    public int getChannels() {
        return channels;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    @Override
    public int available() throws IOException {
        return in.available();
    }

    @Override
    public void close() throws IOException {
        try {
            mp3Bitstream.close();
        } catch (BitstreamException ex) {
            throw new IOException(ex);
        }
        in.close();
    }

    @Override
    public int read() throws IOException {

        // Have we read past the limit of the buffer?
        if (mp3BufferIndex >= mp3Buffer.limit()) {

            // End of stream when we try to read past the limit
            // since there maybe data in MP3 buffer
            if (endOfStream) {
                return -1;
            }
            mp3Buffer.clear();
            mp3BufferIndex = 0;
            readMP3();
        }

        // Get the value from the MP3 buffer
        int value = mp3Buffer.get(mp3BufferIndex++);
        if (value < 0) {
            value = 256 + value; // Must be in the range 0 to 255
        }
        return value;
    }

}
