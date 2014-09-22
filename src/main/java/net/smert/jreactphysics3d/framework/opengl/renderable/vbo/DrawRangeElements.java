package net.smert.jreactphysics3d.framework.opengl.renderable.vbo;

import net.smert.jreactphysics3d.framework.opengl.GL;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class DrawRangeElements extends AbstractDrawCall {

    @Override
    public void render() {
        for (int i = 0, max = renderModes.length; i < max; i++) {
            GL.vboHelper.drawRangeElements(renderModes[i], firstElements[i], elementCounts[i] - 1, elementCounts[i], elementType);
        }
    }

}
