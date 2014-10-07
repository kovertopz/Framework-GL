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
package net.smert.frameworkgl.utils;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class ListUtils {

    public static boolean[] ToPrimitiveBooleanArray(List<Boolean> list) {
        Iterator<Boolean> iterator = list.iterator();
        boolean[] array = new boolean[list.size()];

        for (int i = 0; i < array.length; i++) {
            array[i] = iterator.next();
        }

        return array;
    }

    public static byte[] ToPrimitiveByteArray(List<Byte> list) {
        Iterator<Byte> iterator = list.iterator();
        byte[] array = new byte[list.size()];

        for (int i = 0; i < array.length; i++) {
            array[i] = iterator.next();
        }

        return array;
    }

    public static char[] ToPrimitiveCharArray(List<Character> list) {
        Iterator<Character> iterator = list.iterator();
        char[] array = new char[list.size()];

        for (int i = 0; i < array.length; i++) {
            array[i] = iterator.next();
        }

        return array;
    }

    public static double[] ToPrimitiveDoubleArray(List<Double> list) {
        Iterator<Double> iterator = list.iterator();
        double[] array = new double[list.size()];

        for (int i = 0; i < array.length; i++) {
            array[i] = iterator.next();
        }

        return array;
    }

    public static float[] ToPrimitiveFloatArray(List<Float> list) {
        Iterator<Float> iterator = list.iterator();
        float[] array = new float[list.size()];

        for (int i = 0; i < array.length; i++) {
            array[i] = iterator.next();
        }

        return array;
    }

    public static int[] ToPrimitiveIntArray(List<Integer> list) {
        Iterator<Integer> iterator = list.iterator();
        int[] array = new int[list.size()];

        for (int i = 0; i < array.length; i++) {
            array[i] = iterator.next();
        }

        return array;
    }

    public static long[] ToPrimitiveLongArray(List<Long> list) {
        Iterator<Long> iterator = list.iterator();
        long[] array = new long[list.size()];

        for (int i = 0; i < array.length; i++) {
            array[i] = iterator.next();
        }

        return array;
    }

    public static short[] ToPrimitiveShortArray(List<Short> list) {
        Iterator<Short> iterator = list.iterator();
        short[] array = new short[list.size()];

        for (int i = 0; i < array.length; i++) {
            array[i] = iterator.next();
        }

        return array;
    }

}
