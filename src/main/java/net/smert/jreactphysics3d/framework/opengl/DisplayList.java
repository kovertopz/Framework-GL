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
public class DisplayList {

    private final static Logger log = LoggerFactory.getLogger(DisplayList.class);

    private int displayListID;

    public DisplayList() {
        displayListID = 0;
    }

    public void create() {
        destroy();
        displayListID = GL.displayListHelper.create();
        log.debug("Created a new display list with ID: {}", displayListID);
    }

    public void destroy() {
        if (displayListID == 0) {
            return;
        }
        GL.displayListHelper.delete(displayListID);
        displayListID = 0;
        log.debug("Deleted a display list with ID: {}", displayListID);
    }

    public int getDisplayListID() {
        return displayListID;
    }

}
