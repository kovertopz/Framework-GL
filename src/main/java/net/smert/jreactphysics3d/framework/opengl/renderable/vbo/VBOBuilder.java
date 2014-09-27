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
import net.smert.jreactphysics3d.framework.opengl.renderable.factory.Configuration;
import net.smert.jreactphysics3d.framework.opengl.renderable.shared.AbstractDrawCall;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VBOBuilder extends net.smert.jreactphysics3d.framework.opengl.renderable.shared.Builder {

    public void calculateOffsetsAndStride(
            Mesh mesh, Configuration renderableConfig, VertexBufferObjectInterleaved vboInterleaved) {

        int total = 0;

        // Calculate byte size of each type and add to the total. Save the total as
        // the current offset before increasing it.
        if (mesh.hasColors()) {
            vboInterleaved.setColorOffsetBytes(total);
            int byteSize = renderableConfig.convertGLTypeToByteSize(renderableConfig.getColorType());
            total += renderableConfig.getColorSize() * byteSize;
        }
        if (mesh.hasNormals()) {
            vboInterleaved.setNormalOffsetBytes(total);
            int byteSize = renderableConfig.convertGLTypeToByteSize(renderableConfig.getNormalType());
            total += renderableConfig.getNormalSize() * byteSize;
        }
        if (mesh.hasTexCoords()) {
            vboInterleaved.setTexCoordOffsetBytes(total);
            int byteSize = renderableConfig.convertGLTypeToByteSize(renderableConfig.getTexCoordType());
            total += renderableConfig.getTexCoordSize() * byteSize;
        }
        if (mesh.hasVertices()) {
            vboInterleaved.setVertexOffsetBytes(total);
            int byteSize = renderableConfig.convertGLTypeToByteSize(renderableConfig.getVertexType());
            total += renderableConfig.getVertexSize() * byteSize;
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
                for (int i = 0, max = maxIndexes.length; i < max; i++) {
                    maxIndexes[i] = mesh.getSegment(i).getMaxIndex();
                }

                // Convert min indexes from each segment
                int[] minIndexes = new int[totalSegments];
                for (int i = 0, max = minIndexes.length; i < max; i++) {
                    minIndexes[i] = mesh.getSegment(i).getMinIndex();
                }

                // Create concrete class and set specific data
                DrawRangeElements drawRangedElements = new DrawRangeElements();
                drawRangedElements.setMaxIndexes(maxIndexes);
                drawRangedElements.setMinIndexes(minIndexes);

                // Make sure we set the abstract class
                drawCall = drawRangedElements;
            } else {
                drawCall = new DrawElements();
            }
        } else {

            // Convert first indexes
            int[] firstElements = new int[totalSegments];
            List<Integer> firstIndexes = mesh.getFirstIndexes();
            for (int i = 0, max = firstElements.length; i < max; i++) {
                firstElements[i] = firstIndexes.get(i);
            }

            // Create concrete class and set specific data
            DrawArrays drawArrays = new DrawArrays();
            drawArrays.setFirstElements(firstElements);

            // Make sure we set the abstract class
            drawCall = drawArrays;
        }

        // Convert element counts from each segment
        int[] elementCounts = new int[totalSegments];
        for (int i = 0, max = elementCounts.length; i < max; i++) {
            elementCounts[i] = mesh.getSegment(i).getVertices().size();
        }

        // Convert primitive modes from each segment
        int[] primitiveModes = new int[totalSegments];
        for (int i = 0, max = primitiveModes.length; i < max; i++) {
            primitiveModes[i] = mesh.getSegment(i).getPrimitiveMode();
        }

        drawCall.setElementCounts(elementCounts);
        drawCall.setPrimitiveModes(primitiveModes);

        return drawCall;
    }

}
