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
package net.smert.frameworkgl.opengl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Shader {

    private final static Logger log = LoggerFactory.getLogger(Shader.class);

    private int programID;

    public void create() {
        destroy();
        programID = GL.shaderHelper.createProgram();
        log.debug("Created a new program with ID: {}", programID);
    }

    public void destroy() {
        if (programID == 0) {
            return;
        }
        GL.shaderHelper.deleteProgram(programID);
        log.debug("Deleted a program with ID: {}", programID);
        programID = 0;
    }

    public int getProgramID() {
        return programID;
    }

}
