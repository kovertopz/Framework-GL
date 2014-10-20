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

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public abstract class AbstractDrawCallBuilder extends AbstractRenderCallBuilder {

    public void createDrawCall(Mesh mesh, AbstractDrawCall drawCall) {

        int totalSegments = mesh.getTotalSegments();

        // Convert element counts from each segment
        int[] elementCounts = new int[totalSegments];
        for (int i = 0; i < elementCounts.length; i++) {
            elementCounts[i] = mesh.getSegment(i).getElementCount();
        }

        // Convert primitive modes from each segment
        int[] primitiveModes = new int[totalSegments];
        for (int i = 0; i < primitiveModes.length; i++) {
            primitiveModes[i] = mesh.getSegment(i).getPrimitiveMode();
        }

        drawCall.setElementCounts(elementCounts);
        drawCall.setPrimitiveModes(primitiveModes);

        // Attach shaders and textures to the draw call
        createRenderCall(mesh, drawCall);
    }

}
