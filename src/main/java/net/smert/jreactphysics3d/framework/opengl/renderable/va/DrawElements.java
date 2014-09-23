package net.smert.jreactphysics3d.framework.opengl.renderable.va;

import java.nio.ByteBuffer;
import net.smert.jreactphysics3d.framework.opengl.GL;
import net.smert.jreactphysics3d.framework.opengl.renderable.shared.AbstractDrawCall;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class DrawElements extends AbstractDrawCall {

    private ByteBuffer vertexIndexBuffer;

    public ByteBuffer getVertexIndexBuffer() {
        return vertexIndexBuffer;
    }

    public void setVertexIndexBuffer(ByteBuffer vertexIndexBuffer) {
        this.vertexIndexBuffer = vertexIndexBuffer;
    }

    @Override
    public void render() {
        for (int i = 0, max = primitiveModes.length; i < max; i++) {
            GL.vaHelper.drawElements(primitiveModes[i], elementCounts[i], renderableConfig.getIndexType(), vertexIndexBuffer);
        }
    }

}
