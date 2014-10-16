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
package net.smert.frameworkgl.opengl.renderable.gl2;

import net.smert.frameworkgl.opengl.VertexBufferObject;
import net.smert.frameworkgl.opengl.VertexBufferObjectInterleaved;
import net.smert.frameworkgl.opengl.renderable.Renderable;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VertexBufferObjectBindStrategyGL2
        implements net.smert.frameworkgl.opengl.renderable.shared.VertexBufferObjectBindStrategy {

    @Override
    public void bind(int renderableConfigID, VertexBufferObject vboColor, VertexBufferObject vboNormal,
            VertexBufferObject vboTexCoord, VertexBufferObject vboVertex, VertexBufferObject vboVertexIndex) {

        // Switch the renderable configuration first
        Renderable.bindState2.switchRenderableConfiguration(renderableConfigID);

        // Bind each VBO
        if (vboColor != null) {
            Renderable.bindState2.bindColor(vboColor.getVboID(), 0, 0);
        } else {
            Renderable.bindState2.bindColor(0, 0, 0);
        }
        if (vboNormal != null) {
            Renderable.bindState2.bindNormal(vboNormal.getVboID(), 0, 0);
        } else {
            Renderable.bindState2.bindNormal(0, 0, 0);
        }
        if (vboTexCoord != null) {
            Renderable.bindState2.bindTexCoord(vboTexCoord.getVboID(), 0, 0);
        } else {
            Renderable.bindState2.bindTexCoord(0, 0, 0);
        }
        if (vboVertex != null) {
            Renderable.bindState2.bindVertex(vboVertex.getVboID(), 0, 0);
        } else {
            Renderable.bindState2.bindVertex(0, 0, 0);
        }
        if (vboVertexIndex != null) {
            Renderable.bindState2.bindVertexIndex(vboVertexIndex.getVboID());
        } else {
            Renderable.bindState2.bindVertexIndex(0);
        }
    }

    @Override
    public void bindInterleaved(int renderableConfigID, boolean hasColors, boolean hasNormals, boolean hasTexCoords,
            boolean hasVertices, boolean hasIndexes, VertexBufferObjectInterleaved vboInterleaved,
            VertexBufferObject vboVertexIndex) {
        int strideBytes = vboInterleaved.getStrideBytes();

        // Switch the renderable configuration first
        Renderable.bindState2.switchRenderableConfiguration(renderableConfigID);

        // Bind VBO for each type
        if (hasColors) {
            Renderable.bindState2.bindColor(
                    vboInterleaved.getVboID(), strideBytes, vboInterleaved.getColorOffsetBytes());
        } else {
            Renderable.bindState2.bindColor(0, 0, 0);
        }
        if (hasNormals) {
            Renderable.bindState2.bindNormal(
                    vboInterleaved.getVboID(), strideBytes, vboInterleaved.getNormalOffsetBytes());
        } else {
            Renderable.bindState2.bindNormal(0, 0, 0);
        }
        if (hasTexCoords) {
            Renderable.bindState2.bindTexCoord(
                    vboInterleaved.getVboID(), strideBytes, vboInterleaved.getTexCoordOffsetBytes());
        } else {
            Renderable.bindState2.bindTexCoord(0, 0, 0);
        }
        if (hasVertices) {
            Renderable.bindState2.bindVertex(
                    vboInterleaved.getVboID(), strideBytes, vboInterleaved.getVertexOffsetBytes());
        } else {
            Renderable.bindState2.bindVertex(0, 0, 0);
        }
        if (hasIndexes) {
            Renderable.bindState2.bindVertexIndex(vboVertexIndex.getVboID());
        } else {
            Renderable.bindState2.bindVertexIndex(0);
        }
    }

}
