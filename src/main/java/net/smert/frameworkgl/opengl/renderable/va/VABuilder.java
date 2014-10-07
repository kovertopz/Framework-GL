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
package net.smert.frameworkgl.opengl.renderable.va;

import java.nio.ByteBuffer;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.RenderableConfiguration;
import net.smert.frameworkgl.opengl.renderable.shared.AbstractDrawCall;
import net.smert.frameworkgl.opengl.renderable.va.factory.VADrawCallFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VABuilder extends net.smert.frameworkgl.opengl.renderable.shared.AbstractBuilder {

    private final VADrawCallFactory vaDrawCallFactory;

    public VABuilder(VADrawCallFactory vaDrawCallFactory) {
        this.vaDrawCallFactory = vaDrawCallFactory;
    }

    public AbstractDrawCall createDrawCall(Mesh mesh, RenderableConfiguration config, ByteBuffer vertexIndexBuffer) {
        AbstractDrawCall drawCall;

        int totalSegments = mesh.getTotalSegments();

        if (mesh.hasIndexes()) {

            assert (vertexIndexBuffer != null);

            // Create concrete class and set specific data
            VADrawElements drawElements = vaDrawCallFactory.createDrawElements();
            drawElements.setIndexType(config.getIndexType());
            drawElements.setVertexIndexBuffer(vertexIndexBuffer);

            // Make sure we set the abstract class
            drawCall = drawElements;
        } else {

            // Convert first indexes
            int[] firstElements = new int[totalSegments];
            for (int i = 0; i < firstElements.length; i++) {
                firstElements[i] = mesh.getSegment(i).getMinIndex();
            }

            // Create concrete class and set specific data
            VADrawArrays drawArrays = vaDrawCallFactory.createDrawArrays();
            drawArrays.setFirstElements(firstElements);

            // Make sure we set the abstract class
            drawCall = drawArrays;
        }

        // Do common things
        super.createDrawCall(mesh, drawCall);

        return drawCall;
    }

}
