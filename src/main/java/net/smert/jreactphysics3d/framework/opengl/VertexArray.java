/**
 * Copyright 2012 Jason Sorensen (sorensenj@smert.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package net.smert.jreactphysics3d.framework.opengl;

import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VertexArray {

    private final static Logger log = LoggerFactory.getLogger(VertexArray.class);

    private ByteBuffer byteBuffer;

    public VertexArray() {
        byteBuffer = null;
    }

    public void create(int bufferSize) {
        destroy();
        byteBuffer = GL.bufferHelper.createByteBuffer(bufferSize);
        log.debug("Created a new vertex array with a size: {}", bufferSize);
    }

    public void destroy() {
        if (byteBuffer == null) {
            return;
        }
        int bufferSize = byteBuffer.capacity();
        byteBuffer = null;
        log.debug("Deleted a vertex array with a size: {}", bufferSize);
    }

    public ByteBuffer getByteBuffer() {
        return byteBuffer;
    }

}
