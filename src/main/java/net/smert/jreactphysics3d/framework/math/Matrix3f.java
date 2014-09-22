package net.smert.jreactphysics3d.framework.math;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Matrix3f {

    final Vector3f xAxis = new Vector3f();
    final Vector3f yAxis = new Vector3f();
    final Vector3f zAxis = new Vector3f();

    // Constructors
    public Matrix3f() {
    }

    // Matrix results
    public Matrix3f fromAxisAngle(Vector3f v, float degrees) {
        float radians = MathHelper.ToRadians(degrees);

        float c = MathHelper.Cos(radians);
        float s = MathHelper.Sin(radians);
        float t = (1.0f - c);

        xAxis.set(
                t * (v.x * v.x) + c,
                t * (v.x * v.y) + (v.z * s),
                t * (v.x * v.z) - (v.y * s));

        yAxis.set(
                t * (v.y * v.x) - (v.z * s),
                t * (v.y * v.y) + c,
                t * (v.y * v.z) + (v.x * s));

        zAxis.set(
                t * (v.z * v.x) + (v.y * s),
                t * (v.z * v.y) - (v.x * s),
                t * (v.z * v.z) + c);

        return this;
    }

    public Matrix3f identity() {
        xAxis.set(1.0f, 0.0f, 0.0f);
        yAxis.set(0.0f, 1.0f, 0.0f);
        zAxis.set(0.0f, 0.0f, 1.0f);
        return this;
    }

    public Matrix3f orthonormalize() {
        zAxis.normalize();
        yAxis.set(zAxis).cross(xAxis);
        yAxis.normalize();
        xAxis.set(yAxis).cross(zAxis);
        xAxis.normalize();
        return this;
    }

    public Matrix3f orthonormalize(Vector3f position, Vector3f target, Vector3f up) {
        zAxis.set(position).subtract(target);
        zAxis.normalize();

        float zdotup = zAxis.dot(up);

        if ((zdotup < -MathHelper.TOLERANCE_DOT_PRODUCT_PARALLEL) || (zdotup > MathHelper.TOLERANCE_DOT_PRODUCT_PARALLEL)) {
            yAxis.set(zAxis).cross(Vector3f.WORLD_X_AXIS);
            yAxis.normalize();
            xAxis.set(yAxis).cross(zAxis);
            xAxis.normalize();
        } else {
            xAxis.set(up).cross(zAxis);
            xAxis.normalize();
            yAxis.set(zAxis).cross(xAxis);
            yAxis.normalize();
        }

        return this;
    }

    public Matrix3f setAxes(Vector3f xaxis, Vector3f yaxis, Vector3f zaxis) {
        xAxis.set(xaxis);
        yAxis.set(yaxis);
        zAxis.set(zaxis);
        return this;
    }

    // Scalar results
    public float dotRow(int row, Vector3f v) {
        switch (row) {
            case 0:
                return xAxis.x * v.x + yAxis.x * v.y + zAxis.x * v.z;
            case 1:
                return xAxis.y * v.x + yAxis.y * v.y + zAxis.y * v.z;
            case 2:
                return xAxis.z * v.x + yAxis.z * v.y + zAxis.z * v.z;
            default:
                throw new IllegalArgumentException("Invalid Row");
        }
    }

    public float getElement(int column, int row) {
        switch (column) {
            case 0:
                switch (row) {
                    case 0:
                        return xAxis.x;
                    case 1:
                        return xAxis.y;
                    case 2:
                        return xAxis.z;
                    default:
                        throw new IllegalArgumentException("Invalid Row");
                }

            case 1:
                switch (row) {
                    case 0:
                        return yAxis.x;
                    case 1:
                        return yAxis.y;
                    case 2:
                        return yAxis.z;
                    default:
                        throw new IllegalArgumentException("Invalid Row");
                }

            case 2:
                switch (row) {
                    case 0:
                        return zAxis.x;
                    case 1:
                        return zAxis.y;
                    case 2:
                        return zAxis.z;
                    default:
                        throw new IllegalArgumentException("Invalid Row");
                }

            default:
                throw new IllegalArgumentException("Invalid Column");
        }
    }

    public float getHeading() {
        float h, t = xAxis.y;

        if ((t >= MathHelper.TOLERANCE_EULER_CONVERSION) || (t <= -MathHelper.TOLERANCE_EULER_CONVERSION)) {
            h = MathHelper.ArcTan2(zAxis.x, zAxis.z);
        } else {
            h = MathHelper.ArcTan2(-xAxis.z, xAxis.x);
        }

        return 180.0f - MathHelper.ToDegrees(h);
    }

    public float getPitch() {
        float p, t = xAxis.y;

        if ((t >= MathHelper.TOLERANCE_EULER_CONVERSION) || (t <= -MathHelper.TOLERANCE_EULER_CONVERSION)) {
            p = 0.0f;
        } else {
            p = MathHelper.ArcTan2(-zAxis.y, yAxis.y);
        }

        return MathHelper.ToDegrees(p);
    }

    public float getRoll() {
        float r, t = xAxis.y;

        if (t >= MathHelper.TOLERANCE_EULER_CONVERSION) {
            r = MathHelper.PI_OVER_2;
        } else if (t <= -MathHelper.TOLERANCE_EULER_CONVERSION) {
            r = -MathHelper.PI_OVER_2;
        } else {
            r = MathHelper.ArcSin(t);
        }

        return MathHelper.ToDegrees(r);
    }

    // Vector results
    public Vector3f getXAxis() {
        return xAxis;
    }

    public Vector3f getYAxis() {
        return yAxis;
    }

    public Vector3f getZAxis() {
        return zAxis;
    }

    public Vector3f multiplyOut(Vector3f v, Vector3f out) {
        out.set(
                dotRow(0, v),
                dotRow(1, v),
                dotRow(2, v));
        return out;
    }

    @Override
    public String toString() {
        return "Matrix3f:\n"
                + "X-Axis: " + xAxis.toString() + "\n"
                + "Y-Axis: " + yAxis.toString() + "\n"
                + "Z-Axis: " + zAxis.toString();
    }

}
