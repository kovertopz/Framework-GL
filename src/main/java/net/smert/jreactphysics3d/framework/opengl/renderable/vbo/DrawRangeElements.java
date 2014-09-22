package net.smert.jreactphysics3d.framework.opengl.renderable.vbo;

import net.smert.jreactphysics3d.framework.opengl.GL;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class DrawRangeElements extends AbstractDrawCall {

    private int[] maxIndexes;
    private int[] minIndexes;

    public int[] getMaxIndexes() {
        return maxIndexes;
    }

    public void setMaxIndexes(int[] maxIndexes) {
        this.maxIndexes = maxIndexes;
    }

    public int[] getMinIndexes() {
        return minIndexes;
    }

    public void setMinIndexes(int[] minIndexes) {
        this.minIndexes = minIndexes;
    }

    @Override
    public void render() {
        for (int i = 0, max = primitiveModes.length; i < max; i++) {
            GL.vboHelper.drawRangeElements(
                    primitiveModes[i], minIndexes[i], maxIndexes[i], elementCounts[i], vboConfiguration.getIndexType());
        }
    }

}
