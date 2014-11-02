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
public class OpenALSource {

    private final static Logger log = LoggerFactory.getLogger(OpenALSource.class);

    private int sourceID;

    public OpenALSource() {
        sourceID = 0;
    }

    public void create() {
        destroy();
        sourceID = AL.sourceHelper.create();
        log.debug("Created a new OpenAL source with ID: {}", sourceID);
    }

    public void destroy() {
        if (sourceID == 0) {
            return;
        }
        AL.sourceHelper.delete(sourceID);
        log.debug("Deleted a OpenAL source with ID: {}", sourceID);
        sourceID = 0;
    }

    public int getSourceID() {
        return sourceID;
    }

}
