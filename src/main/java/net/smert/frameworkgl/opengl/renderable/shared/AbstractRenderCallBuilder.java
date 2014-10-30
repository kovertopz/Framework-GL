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
package net.smert.frameworkgl.opengl.renderable.shared;

import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.mesh.Segment;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public abstract class AbstractRenderCallBuilder {

    public void createRenderCall(Mesh mesh, AbstractRenderCall renderCall) {

        int totalSegments = mesh.getTotalSegments();

        // Create segment array
        Segment[] segments = new Segment[totalSegments];
        for (int i = 0; i < segments.length; i++) {
            segments[i] = mesh.getSegment(i);
        }

        renderCall.setSegments(segments);
    }

}
