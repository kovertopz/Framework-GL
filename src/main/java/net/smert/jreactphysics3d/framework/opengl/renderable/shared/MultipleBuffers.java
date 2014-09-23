package net.smert.jreactphysics3d.framework.opengl.renderable.shared;

import java.nio.ByteBuffer;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public interface MultipleBuffers {

    public void createColor(int bufferSize);

    public void createInterleaved(int bufferSize);

    public void createNormal(int bufferSize);

    public void createTexCoord(int bufferSize);

    public void createVertex(int bufferSize);

    public void createVertexIndex(int bufferSize);

    public ByteBuffer getColor();

    public ByteBuffer getInterleaved();

    public ByteBuffer getNormal();

    public ByteBuffer getTexCoord();

    public ByteBuffer getVertex();

    public ByteBuffer getVertexIndex();

    public void setInterleavedBufferToOthers();

}
