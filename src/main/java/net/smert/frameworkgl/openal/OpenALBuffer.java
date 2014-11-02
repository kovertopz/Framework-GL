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
package net.smert.frameworkgl.openal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class OpenALBuffer {

    private final static Logger log = LoggerFactory.getLogger(OpenALBuffer.class);

    private int bufferID;

    public OpenALBuffer() {
        bufferID = 0;
    }

    public void create() {
        destroy();
        bufferID = AL.bufferHelper.create();
        log.debug("Created a new OpenAL buffer with ID: {}", bufferID);
    }

    public void destroy() {
        if (bufferID == 0) {
            return;
        }
        AL.bufferHelper.delete(bufferID);
        log.debug("Deleted a OpenAL buffer with ID: {}", bufferID);
        bufferID = 0;
    }

    public int getBufferID() {
        return bufferID;
    }

}
