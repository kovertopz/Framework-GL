package net.smert.jreactphysics3d.framework.utils;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Color {

    // RGBA color components
    float r, g, b, a;

    // Constructor
    public Color() {
        this(1, 1, 1, 1);
    }

    // Constructor
    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    // Copy-constructor
    public Color(Color color) {
        r = color.r;
        g = color.g;
        b = color.b;
        a = color.a;
    }

    public float getA() {
        return a;
    }

    public void setA(float a) {
        this.a = a;
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        this.b = b;
    }

    public float getG() {
        return g;
    }

    public void setG(float g) {
        this.g = g;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }

}
