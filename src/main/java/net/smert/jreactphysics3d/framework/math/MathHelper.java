package net.smert.jreactphysics3d.framework.math;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class MathHelper {

    public final static float DEG_TO_RAD = (float) Math.PI / 180.0f;
    public final static float FLOAT_EPSILON = 1.1920928955078125E-7f;
    public final static float PI_OVER_2 = (float) Math.PI / 2.0f;
    public final static float PI_OVER_360 = (float) Math.PI / 360.0f;
    public final static float RAD_TO_DEG = 180.0f / (float) Math.PI;
    public final static float TOLERANCE_DOT_PRODUCT_PARALLEL = 0.999999f;
    public final static float TOLERANCE_EULER_CONVERSION = 0.999999f;
    public final static float ZERO_EPSILON = FLOAT_EPSILON * FLOAT_EPSILON * FLOAT_EPSILON;

    private MathHelper() {
    }

    public static float ArcSin(float radians) {
        return (float) StrictMath.asin(radians);
    }

    public static float ArcTan2(float a, float b) {
        return (float) StrictMath.atan2(a, b);
    }

    public static float Cos(float radians) {
        return (float) StrictMath.cos(radians);
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
