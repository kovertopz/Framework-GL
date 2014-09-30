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
package net.smert.jreactphysics3d.framework.opengl.renderable.gl1;

import net.smert.jreactphysics3d.framework.opengl.DisplayList;
import net.smert.jreactphysics3d.framework.opengl.GL;
import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.renderable.AbstractRenderable;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class DisplayListRenderable extends AbstractRenderable {

    private DisplayList displayList;

    @Override
    public void create(Mesh mesh) {

        // Destroy existing display list
        destroy();

        // Create display list
        displayList = GL.glf.createDisplayList();
        displayList.create();
        int displayListID = displayList.getDisplayListID();

        // Compile display list
        assert (displayListID != 0);
        GL.displayListHelper.begin(displayListID);
        for (int i = 0; i < mesh.getTotalSegments(); i++) {
            DrawCommands drawCommands = mesh.getSegment(i).getDrawCommands();
            drawCommands.execCommands(mesh);
        }
        GL.displayListHelper.end();
    }

    @Override
    public void destroy() {
        if (displayList == null) {
            return;
        }
        displayList.destroy();
        displayList = null;
    }

    @Override
    public void render() {
        assert (displayList != null);
        int displayListID = displayList.getDisplayListID();
        assert (displayListID != 0);
        GL.displayListHelper.call(displayListID);
    }

}
