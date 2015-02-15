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
package net.smert.frameworkgl.math;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class MathHelper {

    public final static float DEG_TO_RAD = (float) Math.PI / 180f;
    public final static float FLOAT_EPSILON = 1.1920928955078125E-7f;
    public final static float PI_OVER_2 = (float) Math.PI / 2f;
    public final static float PI_OVER_180 = (float) Math.PI / 180f;
    public final static float PI_OVER_360 = (float) Math.PI / 360f;
    public final static float RAD_TO_DEG = 180f / (float) Math.PI;
    public final static float TAU = (float) Math.PI * 2f;
    public final static float TOLERANCE_DOT_PRODUCT_PARALLEL = .999999f;
    public final static float TOLERANCE_EULER_CONVERSION = .999999f;
    public final static float ZERO_EPSILON = FLOAT_EPSILON * FLOAT_EPSILON * FLOAT_EPSILON;

    private MathHelper() {
    }

    public static float ArcCos(float radians) {
        return (float) StrictMath.acos(radians);
    }

    public static float ArcSin(float radians) {
        return (float) StrictMath.asin(radians);
    }

    public static float ArcTan(float a) {
        return (float) StrictMath.atan(a);
    }

    public static float ArcTan2(float a, float b) {
        return (float) StrictMath.atan2(a, b);
    }

    public static float CalculateMachineEpsilonFloat() {
        float machEps = 1f;
        do {
            machEps /= 2f;
        } while ((float) (1 + (machEps / 2)) != 1);
        return machEps;
    }

    public static float Clamp(float value, float lowerLimit, float upperLimit) {
        assert (lowerLimit <= upperLimit);
        return Math.min(Math.max(value, lowerLimit), upperLimit);
    }

    public static float Cos(float radians) {
        return (float) StrictMath.cos(radians);
    }

    public static float Log(double a) {
        return (float) StrictMath.log(a);
    }

    public static float Pow(float a, float b) {
        return (float) StrictMath.pow(a, b);
    }

    public static float Sin(float radians) {
        return (float) StrictMath.sin(radians);
    }

    public static float Sqrt(float a) {
        return (float) StrictMath.sqrt(a);
    }

    public static float Tan(float radians) {
        return (float) StrictMath.tan(radians);
    }

    public static float ToDegrees(float radians) {
        return radians * RAD_TO_DEG;
    }

    public static float ToRadians(float degrees) {
        return degrees * DEG_TO_RAD;
    }

}
