package net.smert.jreactphysics3d.framework.opengl.renderable.vbo;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public abstract class AbstractDrawCall implements DrawCall {

    protected int[] elementCounts;
    protected int[] primitiveModes;
    protected static Configuration vboConfiguration;

    public int[] getElementCounts() {
        return elementCounts;
    }

    public void setElementCounts(int[] elementCounts) {
        this.elementCounts = elementCounts;
    }

    public int[] getPrimitiveModes() {
        return primitiveModes;
    }

    public void setPrimitiveModes(int[] primitiveModes) {
        this.primitiveModes = primitiveModes;
    }

    public static void SetVboConfiguration(Configuration vboConfiguration) {
        AbstractDrawCall.vboConfiguration = vboConfiguration;
        if (vboConfiguration.isImmutable() == false) {
            throw new RuntimeException("VBO configuration must be made immutable");
        }
    }

}
