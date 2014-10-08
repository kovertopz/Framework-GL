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
