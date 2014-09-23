package net.smert.jreactphysics3d.framework.opengl.renderable.vbo;

import net.smert.jreactphysics3d.framework.opengl.renderable.factory.Configuration;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public abstract class AbstractDrawCall implements DrawCall {

    protected int[] elementCounts;
    protected int[] primitiveModes;
    protected static Configuration renderableConfig;

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

    public static void SetRenderableConfiguration(Configuration renderableConfig) {
        AbstractDrawCall.renderableConfig = renderableConfig;
        if (renderableConfig.isImmutable() == false) {
            throw new RuntimeException("Renderable configuration must be made immutable");
        }
    }

}
