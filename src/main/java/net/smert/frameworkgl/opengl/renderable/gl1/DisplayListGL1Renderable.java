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
package net.smert.frameworkgl.opengl.renderable.gl1;

import java.util.ArrayList;
import java.util.List;
import net.smert.frameworkgl.opengl.DisplayList;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderable.shared.DrawCommands;
import net.smert.frameworkgl.opengl.renderable.shared.RenderCall;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class DisplayListGL1Renderable extends AbstractRenderable {

    private final List<DisplayList> displayLists;
    private RenderCall renderCall;

    public DisplayListGL1Renderable() {
        displayLists = new ArrayList<>();
        renderCall = null;
    }

    @Override
    public void create(Mesh mesh) {

        // Destroy existing display lists
        destroy();

        for (int i = 0; i < mesh.getTotalSegments(); i++) {

            // Create display list
            DisplayList displayList = GL.glFactory.createDisplayList();
            displayList.create();
            int displayListID = displayList.getDisplayListID();

            // Compile display list
            assert (displayListID != 0);
            GL.displayListHelper.begin(displayListID);
            DrawCommands drawCommands = mesh.getSegment(i).getDrawCommands();
            drawCommands.execCommands(mesh);
            GL.displayListHelper.end();

            // Save display list
            displayLists.add(displayList);
        }

        // Create render call
        renderCall = Renderable.displayListRenderCallBuilder.createRenderCall(mesh, displayLists);
    }

    @Override
    public void destroy() {
        for (DisplayList displayList : displayLists) {
            displayList.destroy();
        }
        displayLists.clear();
    }

    @Override
    public void render() {
        renderCall.render();
    }

}
