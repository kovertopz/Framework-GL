package net.smert.frameworkgl.utils;

import java.util.Random;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class RandomUtils {

    private static Random random = new Random();

    public static Random GetRandom() {
        return random;
    }

    public static void NewSeed() {
        random = new Random();
    }

    public static boolean NextBoolean() {
        return random.nextBoolean();
    }

    public static double NextDouble() {
        return random.nextDouble();
    }

    public static float NextFloat() {
        return random.nextFloat();
    }

    public static int NextInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    public static long NextLong() {
        return random.nextLong();
    }

    public static void SetSeed(long seed) {
        random.setSeed(seed);
    }

    public static void SetRandom(Random random) {
        RandomUtils.random = random;
    }

}
