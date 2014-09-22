package net.smert.jreactphysics3d.framework.opengl.renderable.vbo;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public abstract class AbstractDrawCall implements DrawCall {

    protected int elementType;
    protected int[] elementCounts;
    protected int[] firstElements;
    protected int[] renderModes;

    public int getElementType() {
        return elementType;
    }

    public void setElementType(int elementType) {
        this.elementType = elementType;
    }

    public int[] getElementCounts() {
        return elementCounts;
    }

    public void setElementCounts(int[] elementCounts) {
        this.elementCounts = elementCounts;
    }

    public int[] getFirstElements() {
        return firstElements;
    }

    public void setFirstElements(int[] firstElements) {
        this.firstElements = firstElements;
    }

    public int[] getRenderModes() {
        return renderModes;
    }

    public void setRenderModes(int[] renderModes) {
        this.renderModes = renderModes;
    }

}
