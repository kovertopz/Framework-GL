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
package net.smert.frameworkgl.opengl.renderable.displaylist;

import java.util.List;
import net.smert.frameworkgl.opengl.DisplayList;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.displaylist.factory.DisplayListRenderCallFactory;
import net.smert.frameworkgl.opengl.renderable.shared.AbstractRenderCall;
import net.smert.frameworkgl.opengl.renderable.shared.AbstractRenderCallBuilder;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class DisplayListRenderCallBuilder extends AbstractRenderCallBuilder {

    private final DisplayListRenderCallFactory displayListRenderCallFactory;

    public DisplayListRenderCallBuilder(DisplayListRenderCallFactory displayListRenderCallFactory) {
        this.displayListRenderCallFactory = displayListRenderCallFactory;
    }

    public AbstractRenderCall createRenderCall(Mesh mesh, List<DisplayList> displayLists) {
        DisplayListRenderCall displayListRenderCall = displayListRenderCallFactory.createDisplayList();

        // Convert display lists
        int[] displayListIDs = new int[displayLists.size()];
        for (int i = 0; i < displayListIDs.length; i++) {
            displayListIDs[i] = displayLists.get(i).getDisplayListID();
        }

        displayListRenderCall.setDisplayListIDs(displayListIDs);

        // Attach shaders and textures to the render call
        super.createRenderCall(mesh, displayListRenderCall);

        return displayListRenderCall;
    }

}
