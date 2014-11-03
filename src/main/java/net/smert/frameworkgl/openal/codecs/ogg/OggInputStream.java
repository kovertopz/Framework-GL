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

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Info;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class OggInputStream extends InputStream {

    private final static int BUFFER_SIZE = 2 * 1024;
    private final static int CONV_BUFFER_SIZE = 4 * 1024;
    private final static int PCM_BUFFER_SIZE = 256 * 1024; // Hopefully large enought to decode a page
    private final static Logger log = LoggerFactory.getLogger(OGGCodec.class);

    private boolean bigEndian;
    private boolean endOfStream;
    private boolean initialized;
    private byte[] convertedBuffer;
    private float[][][] pcmInfo;
    private int count;
    private int index;
    private int packet;
    private int pcmBufferIndex;
    private int[] pcmIndexes;
    private final Block jorbisBlock;
    private ByteBuffer pcmBuffer;
    private final Comment jorbisComment;
    private final DspState jorbisDspState;
    private HeaderParser headerParser;
    private final Info jorbisInfo;
    private final InputStream in;
    private final Packet joggPacket;
    private final Page joggPage;
    private PCMParser pcmParser;
    private final StreamState joggStreamState;
    private final SyncState joggSyncState;

    public OggInputStream(InputStream in) {
        jorbisComment = new Comment();
        jorbisDspState = new DspState();
        jorbisInfo = new Info();
        jorbisBlock = new Block(jorbisDspState); // Noobs
        this.in = in;
        joggPacket = new Packet();
        joggPage = new Page();
        joggStreamState = new StreamState();
        joggSyncState = new SyncState();
        init();
    }

    private void init() {
        bigEndian = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);
        endOfStream = false;
        initialized = false;
        headerParser = new HeaderParser(jorbisComment, jorbisInfo, joggPacket, joggPage, joggStreamState, joggSyncState);
        joggSyncState.init();
        count = 0;
        packet = 1;
    }

    private void initPCM() {
        convertedBuffer = new byte[CONV_BUFFER_SIZE];
        pcmBufferIndex = 0;
        jorbisDspState.synthesis_init(jorbisInfo);
        jorbisBlock.init(jorbisDspState); // Noobs
        pcmBuffer = ByteBuffer.allocateDirect(PCM_BUFFER_SIZE).order(ByteOrder.nativeOrder());
        pcmBuffer.limit(0);
        pcmParser = new PCMParser(joggPacket, joggPage, joggStreamState, joggSyncState);
        pcmInfo = new float[1][][];
        pcmIndexes = new int[getChannels()];
    }

    private void readHeader() throws IOException {
        boolean finished = false;
        boolean needsData = true;

        while (!finished) {
            if (needsData) {

                // This will clear out any data that we already processed
                index = joggSyncState.buffer(BUFFER_SIZE);

                // Read new data
                count = in.read(joggSyncState.data, index, BUFFER_SIZE);
                if (count == -1) {
                    throw new IOException("OGG stream ended prematurely");
                }

                // Must tell the sync state how many bytes were read
                joggSyncState.wrote(count);
                needsData = false;
            }

            // Read page and packet
            if (!headerParser.read(packet)) {
                needsData = true;
                continue;
            }

            // Increase packet count and reset parser state
            packet++;
            headerParser.reset();

            // If we read 3 packets then the header is complete
            if (packet == 4) {
                finished = true;
            }
        }
    }

    private void readPCM() throws IOException {
        boolean finished = false;
        boolean needsData = false;

        // Return if we are at the end of the stream
        if (endOfStream) {
            return;
        }

        while (!finished) {
            if (needsData) {

                // This will clear out any data that we already processed
                index = joggSyncState.buffer(BUFFER_SIZE);

                // Read new data
                count = in.read(joggSyncState.data, index, BUFFER_SIZE);
                if (count == -1) {
                    throw new IOException("OGG stream ended prematurely");
                }

                // Must tell the sync state how many bytes were read
                joggSyncState.wrote(count);
                needsData = false;
            }

            // Read page and packet
            if (!pcmParser.read(packet)) {
                needsData = true;
                continue;
            }

            // Increase packet count
            packet++;

            // If there was an error get new data
            if (pcmParser.isError()) {
                pcmParser.reset(); // Reset parser state for new data
                continue;
            }

            boolean hasPacket = true;
            boolean savedData = false;
            while (hasPacket) {

                // Check packet for errors
                if (jorbisBlock.synthesis(joggPacket) == 0) {
                    jorbisDspState.synthesis_blockin(jorbisBlock);
                }

                // Process samples
                int samples;
                while ((samples = jorbisDspState.synthesis_pcmout(pcmInfo, pcmIndexes)) > 0) {
                    int maxSamples = (samples < CONV_BUFFER_SIZE ? samples : CONV_BUFFER_SIZE);

                    for (int channel = 0; channel < getChannels(); channel++) {
                        float[][] pcm = pcmInfo[0]; // Channel, Index
                        int convertedIndex = channel * 2;
                        int pcmIndex = pcmIndexes[channel];

                        for (int i = 0; i < maxSamples; i++) {
                            int value = (int) (pcm[channel][pcmIndex + i] * 32767f);

                            // Clamp values
                            if (value > 32767) {
                                value = 32767;
                            } else if (value < -32768) {
                                value = -32768;
                            }
                            if (value < 0) {
                                value = value | 0x8000;
                            }

                            // Convert data
                            if (bigEndian) {
                                convertedBuffer[convertedIndex] = (byte) (value >>> 8);
                                convertedBuffer[convertedIndex + 1] = (byte) (value);
                            } else {
                                convertedBuffer[convertedIndex] = (byte) (value);
                                convertedBuffer[convertedIndex + 1] = (byte) (value >>> 8);
                            }

                            // Advance by stride
                            convertedIndex += 2 * getChannels();
                        }
                    }

                    // Tell jOrbis how many samples we processed
                    jorbisDspState.synthesis_read(maxSamples);

                    // Save converted data in PCM buffer
                    int bytesToWrite = 2 * getChannels() * maxSamples;
                    if (bytesToWrite > pcmBuffer.remaining()) {
                        throw new RuntimeException("PCM buffer too full: Bytes to write: " + bytesToWrite
                                + " Remaining: " + pcmBuffer.remaining());
                    }
                    pcmBuffer.put(convertedBuffer, 0, bytesToWrite);
                    savedData = true;
                }

                // Read another packet
                if (!pcmParser.read(packet)) {

                    // Flip buffer once only if we saved data
                    if (savedData) {
                        pcmBuffer.flip();
                    }
                    pcmParser.reset(); // Reset parser state for new data
                    hasPacket = false;
                }

                // Increase packet count
                packet++;
            }

            // Have we reached the end of the stream?
            if (joggPage.eos() != 0) {
                endOfStream = true;
            }

            // If we are at the end of the stream or we saved data
            // to the PCM buffer then we can break. Read will continue
            // to pull bytes out of the PCM buffer until we hit the
            // limit again.
            if (endOfStream || savedData) {
                break;
            }
        }
    }

    public int getChannels() {
        return jorbisInfo.channels;
    }

    public int getSampleRate() {
        return jorbisInfo.rate;
    }

    @Override
    public int available() throws IOException {
        return in.available();
    }

    @Override
    public void close() throws IOException {
        joggStreamState.clear();
        joggSyncState.clear();
        jorbisBlock.clear();
        jorbisDspState.clear();
        jorbisInfo.clear();
        in.close();
    }

    @Override
    public int read() throws IOException {

        // Initialize once
        if (!initialized) {
            readHeader();
            initPCM();
            initialized = true;
        }

        // Have we read past the limit of the buffer?
        if (pcmBufferIndex >= pcmBuffer.limit()) {

            // End of stream when we try to read past the limit
            // since there maybe data in PCM buffer
            if (endOfStream) {
                return -1;
            }
            pcmBuffer.clear();
            pcmBufferIndex = 0;
            readPCM();
        }

        // Get the value from the PCM buffer
        int value = pcmBuffer.get(pcmBufferIndex++);
        if (value < 0) {
            value = 256 + value; // Must be in the range 0 to 255
        }
        return value;
    }

    private static class HeaderParser {

        private int state;
        private final Comment jorbisComment;
        private final Info jorbisInfo;
        private final Packet joggPacket;
        private final Page joggPage;
        private final StreamState joggStreamState;
        private final SyncState joggSyncState;

        public HeaderParser(Comment jorbisComment, Info jorbisInfo, Packet joggPacket, Page joggPage,
                StreamState joggStreamState, SyncState joggSyncState) {
            this.jorbisComment = jorbisComment;
            this.jorbisInfo = jorbisInfo;
            this.joggPacket = joggPacket;
            this.joggPage = joggPage;
            this.joggStreamState = joggStreamState;
            this.joggSyncState = joggSyncState;
        }

        public boolean read(int packet) throws IOException {

            switch (state) {

                // Read page
                case 0:
                    switch (joggSyncState.pageout(joggPage)) {
                        case 0:
                            return false;
                        case 1:
                            break;
                        default:
                            throw new IOException("There was a hole in the data for packet: " + packet);
                    }

                    if (packet == 1) {
                        joggStreamState.init(joggPage.serialno());
                        joggStreamState.reset();
                        jorbisComment.init();
                        jorbisInfo.init();
                    }

                    if (joggStreamState.pagein(joggPage) == -1) {
                        throw new IOException("Unable read the header page for packet: " + packet);
                    }
                    state = 1;

                // Read packet
                case 1:
                    switch (joggStreamState.packetout(joggPacket)) {
                        case 0:
                            return false;
                        case 1:
                            break;
                        default:
                            throw new IOException("There was a hole in the data for packet: " + packet);
                    }

                    if (jorbisInfo.synthesis_headerin(jorbisComment, joggPacket) < 0) {
                        throw new IOException("This is not an OGG stream. Unable to read header info for packet: "
                                + packet);
                    }
                    state = 2;
            }

            if (state != 2) {
                throw new IllegalStateException("Unknown state: " + state);
            }

            return true;
        }

        public final void reset() {
            state = 0;
        }

    }

    private static class PCMParser {

        private boolean error;
        private int state;
        private final Packet joggPacket;
        private final Page joggPage;
        private final StreamState joggStreamState;
        private final SyncState joggSyncState;

        public PCMParser(Packet joggPacket, Page joggPage, StreamState joggStreamState, SyncState joggSyncState) {
            this.joggPacket = joggPacket;
            this.joggPage = joggPage;
            this.joggStreamState = joggStreamState;
            this.joggSyncState = joggSyncState;
        }

        public boolean isError() {
            return error;
        }

        public boolean read(int packet) throws IOException {

            switch (state) {

                // Read page
                case 0:
                    switch (joggSyncState.pageout(joggPage)) {
                        case 0:
                            return false;
                        case 1:
                            break;
                        default:
                            log.error("Error in OGG stream: Missing data in page for packet: {}", packet);
                            error = true;
                    }

                    joggStreamState.pagein(joggPage);
                    state = 1;

                // Read packet
                case 1:
                    switch (joggStreamState.packetout(joggPacket)) {
                        case 0:
                            return false;
                        case 1:
                            break;
                        default:
                            log.error("Error in OGG stream: Missing data in packet for packet: {}", packet);
                    }
            }

            // State in the state so all new calls to read will
            // handle a packet.
            if (state != 1) {
                throw new IllegalStateException("Unknown state: " + state);
            }

            return true;
        }

        public final void reset() {
            error = false;
            state = 0;
        }

    }

}
