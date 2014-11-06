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
package net.smert.frameworkgl.gui.render;

import java.nio.IntBuffer;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.helpers.MouseHelper;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class MouseCursor implements de.lessvoid.nifty.spi.render.MouseCursor {

    private final MouseHelper.MouseCursor mouseCursor;

    public MouseCursor() {
        mouseCursor = new MouseHelper.MouseCursor();
    }

    public void create(int width, int height, int xHotspot, int yHotspot, int numImages, IntBuffer images,
            IntBuffer delays) {
        mouseCursor.create(width, height, xHotspot, yHotspot, numImages, images, delays);
    }

    @Override
    public void enable() {
        Fw.input.setNativeCursor(mouseCursor.getCursor());
    }

    @Override
    public void disable() {
        Fw.input.setNativeCursor(null);
    }

    @Override
    public void dispose() {
        mouseCursor.getCursor().destroy();
    }

}
