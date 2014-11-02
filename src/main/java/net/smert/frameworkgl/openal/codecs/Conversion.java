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
package net.smert.frameworkgl.openal.codecs;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Conversion {

    public static ByteBuffer ConvertEndianness(byte[] sourceArray, int bytes, ByteOrder destOrder,
            ByteOrder sourceOrder) {
        ByteBuffer destBuf = ByteBuffer.allocateDirect(sourceArray.length);
        destBuf.order(destOrder);
        ByteBuffer sourceBuf = ByteBuffer.wrap(sourceArray);
        sourceBuf.order(sourceOrder);
        switch (bytes) {
            case 4:
                IntBuffer destBufInt = destBuf.asIntBuffer();
                IntBuffer sourceBufInt = sourceBuf.asIntBuffer();
                while (sourceBufInt.hasRemaining()) {
                    destBufInt.put(sourceBufInt.get());
                }
                break;

            case 2:
                ShortBuffer destBufShort = destBuf.asShortBuffer();
                ShortBuffer sourceBufShort = sourceBuf.asShortBuffer();
                while (sourceBufShort.hasRemaining()) {
                    destBufShort.put(sourceBufShort.get());
                }
                break;

            case 1:
                while (sourceBuf.hasRemaining()) {
                    destBuf.put(sourceBuf.get());
                }
                break;

            default:
                throw new IllegalArgumentException("Byte size must be 1, 2 or 4: " + bytes);
        }
        destBuf.rewind();
        return destBuf;
    }

}
