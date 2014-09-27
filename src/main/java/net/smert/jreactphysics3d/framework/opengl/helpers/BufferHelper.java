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
package net.smert.jreactphysics3d.framework.opengl.helpers;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class BufferHelper {

    public static ByteBuffer createByteBuffer(int size) {
        return ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
    }

    public static CharBuffer createCharBuffer(int size) {
        return createByteBuffer(size).asCharBuffer();
    }

    public static DoubleBuffer createDoubleBuffer(int size) {
        return createByteBuffer(size * 8).asDoubleBuffer();
    }

    public static FloatBuffer createFloatBuffer(int size) {
        return createByteBuffer(size * 4).asFloatBuffer();
    }

    public static IntBuffer createIntBuffer(int size) {
        return createByteBuffer(size * 4).asIntBuffer();
    }

    public static LongBuffer createLongBuffer(int size) {
        return createByteBuffer(size * 8).asLongBuffer();
    }

    public static ShortBuffer createShortBuffer(int size) {
        return createByteBuffer(size * 2).asShortBuffer();
    }

}
