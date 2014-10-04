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
package net.smert.jreactphysics3d.framework.opengl.renderable.vbo;

import java.util.List;
import net.smert.jreactphysics3d.framework.opengl.VertexBufferObjectInterleaved;
import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.renderable.Renderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.RenderableConfiguration;
import net.smert.jreactphysics3d.framework.opengl.renderable.shared.AbstractDrawCall;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.factory.VBODrawCallFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VBOBuilder extends net.smert.jreactphysics3d.framework.opengl.renderable.shared.AbstractBuilder {

    private final VBODrawCallFactory vboDrawCallFactory;

    public VBOBuilder(VBODrawCallFactory vboDrawCallFactory) {
        this.vboDrawCallFactory = vboDrawCallFactory;
    }

    public void calculateOffsetsAndStride(Mesh mesh, VertexBufferObjectInterleaved vboInterleaved) {

        int total = 0;
        RenderableConfiguration config = Renderable.config;

        // Calculate byte size of each type and add to the total. Save the total as
        // the current offset before increasing it.
        if (mesh.hasColors()) {
            vboInterleaved.setColorOffsetBytes(total);
            int byteSize = config.convertGLTypeToByteSize(config.getColorType());
            total += config.getColorSize() * byteSize;
        }
        if (mesh.hasNormals()) {
            vboInterleaved.setNormalOffsetBytes(total);
            int byteSize = config.convertGLTypeToByteSize(config.getNormalType());
            total += config.getNormalSize() * byteSize;
        }
        if (mesh.hasTexCoords()) {
            vboInterleaved.setTexCoordOffsetBytes(total);
            int byteSize = config.convertGLTypeToByteSize(config.getTexCoordType());
            total += config.getTexCoordSize() * byteSize;
        }
        if (mesh.hasVertices()) {
            vboInterleaved.setVertexOffsetBytes(total);
            int byteSize = config.convertGLTypeToByteSize(config.getVertexType());
            total += config.getVertexSize() * byteSize;
        }

        vboInterleaved.setStrideBytes(total);
    }

    public AbstractDrawCall createDrawCall(Mesh mesh) {
        AbstractDrawCall drawCall;

        int totalSegments = mesh.getTotalSegments();

        if (mesh.hasIndexes()) {
            if (mesh.canRenderRanged()) {

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
                drawRangedElements.setMaxIndexes(maxIndexes);
                drawRangedElements.setMinIndexes(minIndexes);

                // Make sure we set the abstract class
                drawCall = drawRangedElements;
            } else {
                drawCall = vboDrawCallFactory.createDrawElements();
            }
        } else {

            // Convert first indexes
            int[] firstElements = new int[totalSegments];
            List<Integer> firstIndexes = mesh.getFirstIndexes();
            for (int i = 0; i < firstElements.length; i++) {
                firstElements[i] = firstIndexes.get(i);
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

}
