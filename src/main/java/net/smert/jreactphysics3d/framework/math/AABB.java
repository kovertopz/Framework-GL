package net.smert.jreactphysics3d.framework.math;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class AABB {

    private final Vector3f max;
    private final Vector3f min;

    public AABB() {
        max = new Vector3f();
        min = new Vector3f();
    }

    public AABB(AABB aabb) {
        max = new Vector3f(aabb.max);
        min = new Vector3f(aabb.min);
    }

    public AABB(Vector3f min, Vector3f max) {
        this.max = max;
        this.min = min;
    }

    public Vector3f getMax() {
        return max;
    }

    public void setMax(Vector3f max) {
        max.set(max);
    }

    public void setMax(float x, float y, float z) {
        max.set(x, y, z);
    }

    public Vector3f getMin() {
        return min;
    }

    public void setMin(Vector3f min) {
        min.set(min);
    }

    public void setMin(float x, float y, float z) {
        min.set(x, y, z);
    }

    public boolean testCollision(AABB aabb) {
        if ((max.getX() < aabb.min.getX()) || (aabb.max.getX() < min.getX())) {
            return false;
        }
        if ((max.getZ() < aabb.min.getZ()) || (aabb.max.getZ() < min.getZ())) {
            return false;
        }
        return ((max.getY() >= aabb.min.getY()) && (aabb.max.getY() >= min.getY()));
    }

}
