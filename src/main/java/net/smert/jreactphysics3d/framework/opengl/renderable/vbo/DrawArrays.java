package net.smert.jreactphysics3d.framework.opengl.renderable.vbo;

import net.smert.jreactphysics3d.framework.opengl.GL;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class DrawArrays extends AbstractDrawCall {

    private int[] firstElements;

    public int[] getFirstElements() {
        return firstElements;
    }

    public void setFirstElements(int[] firstElements) {
        this.firstElements = firstElements;
    }

    @Override
    public void render() {
        for (int i = 0, max = primitiveModes.length; i < max; i++) {
            GL.vboHelper.drawArrays(primitiveModes[i], firstElements[i], elementCounts[i]);
        }
    }

}
