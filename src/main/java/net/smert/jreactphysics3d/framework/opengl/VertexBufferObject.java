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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VertexBufferObject {

    private final static Logger log = LoggerFactory.getLogger(VertexBufferObject.class);

    private int vboID;

    public VertexBufferObject() {
        vboID = 0;
    }

    public void create() {
        destroy();
        vboID = GL.vboHelper.create();
        log.debug("Created a new VBO with ID: {}", vboID);
    }

    public void destroy() {
        if (vboID != 0) {
            GL.vboHelper.delete(vboID);
            log.debug("Deleted a VBO with ID: {}", vboID);
        }
    }

    public int getVboID() {
        return vboID;
    }

}
