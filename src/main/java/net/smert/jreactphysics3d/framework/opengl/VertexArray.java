package net.smert.jreactphysics3d.framework.opengl;

import java.nio.ByteBuffer;
import net.smert.jreactphysics3d.framework.opengl.helpers.BufferHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VertexArray {

    private final static Logger log = LoggerFactory.getLogger(VertexArray.class);

    private ByteBuffer byteBuffer;

    public void create(int bufferSize) {
        destroy();
        byteBuffer = BufferHelper.createByteBuffer(bufferSize);
        log.debug("Created a new vertex array with a size: {}", bufferSize);
    }

    public void destroy() {
        if (byteBuffer != null) {
            int bufferSize = byteBuffer.capacity();
            byteBuffer = null;
            log.debug("Deleted a vertex array with a size: {}", bufferSize);
        }
    }

    public ByteBuffer getByteBuffer() {
        return byteBuffer;
    }

}
