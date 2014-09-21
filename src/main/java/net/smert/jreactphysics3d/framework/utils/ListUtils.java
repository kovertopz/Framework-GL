package net.smert.jreactphysics3d.framework.utils;

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

        for (int i = 0, max = array.length; i < max; i++) {
            array[i] = iterator.next();
        }

        return array;
    }

    public static byte[] ToPrimitiveByteArray(List<Byte> list) {
        Iterator<Byte> iterator = list.iterator();
        byte[] array = new byte[list.size()];

        for (int i = 0, max = array.length; i < max; i++) {
            array[i] = iterator.next();
        }

        return array;
    }

    public static char[] ToPrimitiveCharArray(List<Character> list) {
        Iterator<Character> iterator = list.iterator();
        char[] array = new char[list.size()];

        for (int i = 0, max = array.length; i < max; i++) {
            array[i] = iterator.next();
        }

        return array;
    }

    public static double[] ToPrimitiveDoubleArray(List<Double> list) {
        Iterator<Double> iterator = list.iterator();
        double[] array = new double[list.size()];

        for (int i = 0, max = array.length; i < max; i++) {
            array[i] = iterator.next();
        }

        return array;
    }

    public static float[] ToPrimitiveFloatArray(List<Float> list) {
        Iterator<Float> iterator = list.iterator();
        float[] array = new float[list.size()];

        for (int i = 0, max = array.length; i < max; i++) {
            array[i] = iterator.next();
        }

        return array;
    }

    public static int[] ToPrimitiveIntArray(List<Integer> list) {
        Iterator<Integer> iterator = list.iterator();
        int[] array = new int[list.size()];

        for (int i = 0, max = array.length; i < max; i++) {
            array[i] = iterator.next();
        }

        return array;
    }

    public static long[] ToPrimitiveLongArray(List<Long> list) {
        Iterator<Long> iterator = list.iterator();
        long[] array = new long[list.size()];

        for (int i = 0, max = array.length; i < max; i++) {
            array[i] = iterator.next();
        }

        return array;
    }

    public static short[] ToPrimitiveShortArray(List<Short> list) {
        Iterator<Short> iterator = list.iterator();
        short[] array = new short[list.size()];

        for (int i = 0, max = array.length; i < max; i++) {
            array[i] = iterator.next();
        }

        return array;
    }

}
