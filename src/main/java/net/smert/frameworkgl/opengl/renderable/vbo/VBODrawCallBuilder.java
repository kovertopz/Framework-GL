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
package net.smert.frameworkgl.opengl.renderable.vbo;

import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.RenderableConfiguration;
import net.smert.frameworkgl.opengl.renderable.shared.AbstractDrawCall;
import net.smert.frameworkgl.opengl.renderable.shared.AbstractDrawCallBuilder;
import net.smert.frameworkgl.opengl.renderable.vbo.factory.VBODrawCallFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VBODrawCallBuilder extends AbstractDrawCallBuilder {

    private boolean canRenderRanged;
    private final VBODrawCallFactory vboDrawCallFactory;

    public VBODrawCallBuilder(VBODrawCallFactory vboDrawCallFactory) {
        canRenderRanged = false;
        this.vboDrawCallFactory = vboDrawCallFactory;
    }

    public AbstractDrawCall createDrawCall(Mesh mesh, RenderableConfiguration config) {
        AbstractDrawCall drawCall;

        int totalSegments = mesh.getTotalSegments();

        if (mesh.hasIndexes()) {
            if (canRenderRanged) {

                // Convert max indexes from each segment
                int[] maxIndexes = new int[totalSegments];
                for (int i = 0; i < maxIndexes.length; i++) {
                    maxIndexes[i] = mesh.getSegment(i).getMaxIndex();
                }

                // Convert min indexes from each segment
                int[] minIndexes = new int[totalSegments];
                for (int i = 0; i < minIndexes.length; i++) {
                    minIndexes[i] = mesh.getSegment(i).getMinIndex();
                }

                // Create concrete class and set specific data
                VBODrawRangeElements drawRangedElements = vboDrawCallFactory.createDrawRangeElements();
                drawRangedElements.setIndexType(config.getIndexType());
                drawRangedElements.setMaxIndexes(maxIndexes);
                drawRangedElements.setMinIndexes(minIndexes);

                // Make sure we set the abstract class
                drawCall = drawRangedElements;
            } else {
                VBODrawElements drawElements = vboDrawCallFactory.createDrawElements();
                drawElements.setIndexType(config.getIndexType());
                drawCall = drawElements;
            }
        } else {

            // Convert first indexes
            int[] firstElements = new int[totalSegments];
            for (int i = 0; i < firstElements.length; i++) {
                firstElements[i] = mesh.getSegment(i).getMinIndex();
            }

            // Create concrete class and set specific data
            VBODrawArrays drawArrays = vboDrawCallFactory.createDrawArrays();
            drawArrays.setFirstElements(firstElements);

            // Make sure we set the abstract class
            drawCall = drawArrays;
        }

        // Do common things
        super.createDrawCall(mesh, drawCall);

        return drawCall;
    }

    public boolean isCanRenderRanged() {
        return canRenderRanged;
    }

    public void setCanRenderRanged(boolean canRenderRanged) {
        this.canRenderRanged = canRenderRanged;
    }

}
