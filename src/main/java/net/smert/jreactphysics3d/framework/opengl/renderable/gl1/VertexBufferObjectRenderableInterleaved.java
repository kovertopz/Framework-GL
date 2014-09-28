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

import net.smert.jreactphysics3d.framework.opengl.GL;
import net.smert.jreactphysics3d.framework.opengl.VertexBufferObject;
import net.smert.jreactphysics3d.framework.opengl.VertexBufferObjectInterleaved;
import net.smert.jreactphysics3d.framework.opengl.constants.VertexBufferObjectTypes;
import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.renderable.AbstractRenderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.factory.Renderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.shared.AbstractDrawCall;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VertexBufferObjectRenderableInterleaved extends AbstractRenderable {

    private boolean hasColors;
    private boolean hasIndexes;
    private boolean hasNormals;
    private boolean hasTexCoords;
    private boolean hasVertices;
    private AbstractDrawCall drawCall;
    private VertexBufferObject vboVertexIndex;
    private VertexBufferObjectInterleaved vboInterleaved;

    @Override
    public void create(Mesh mesh) {

        // Destroy existing VBOs
        destroy();

        Renderable.byteBuffers.reset();

        // Create VBO
        if ((mesh.hasColors()) || (mesh.hasNormals()) || (mesh.hasTexCoords()) || (mesh.hasVertices())) {
            vboInterleaved = GL.glf.createVertexBufferObjectInterleaved();
            vboInterleaved.create();
            Renderable.vboBuilder.calculateOffsetsAndStride(mesh, vboInterleaved);
            Renderable.vboBuilder.createInterleavedBufferData(mesh, vboInterleaved.getStrideBytes(), Renderable.byteBuffers);
            GL.vboHelper.setBufferData(vboInterleaved.getVboID(), Renderable.byteBuffers.getInterleaved(),
                    VertexBufferObjectTypes.STATIC_DRAW);
        }

        if (mesh.hasColors()) {
            hasColors = true;
        }
        if (mesh.hasNormals()) {
            hasNormals = true;
        }
        if (mesh.hasTexCoords()) {
            hasTexCoords = true;
        }
        if (mesh.hasVertices()) {
            hasVertices = true;
        }

        // Create VBO for indexes
        if (mesh.hasIndexes()) {
            vboVertexIndex = GL.glf.createVertexBufferObject();
            vboVertexIndex.create();
            Renderable.vboBuilder.createIndexBufferData(mesh, Renderable.byteBuffers);
            GL.vboHelper.setBufferElementData(vboVertexIndex.getVboID(), Renderable.byteBuffers.getVertexIndex(),
                    VertexBufferObjectTypes.STATIC_DRAW);
            hasIndexes = true;
        }

        GL.vboHelper.unbind();

        // Create draw call
        drawCall = Renderable.vboBuilder.createDrawCall(mesh);
    }

    @Override
    public void destroy() {
        if (vboInterleaved != null) {
            vboInterleaved.destroy();
            vboInterleaved = null;
        }
        if (vboVertexIndex != null) {
            vboVertexIndex.destroy();
            vboVertexIndex = null;
        }
        hasColors = false;
        hasIndexes = false;
        hasNormals = false;
        hasTexCoords = false;
        hasVertices = false;
    }

    @Override
    public void render() {

        int strideBytes = vboInterleaved.getStrideBytes();

        // Bind VBO for each type
        if (hasColors) {
            Renderable.vboBindState.bindColor(
                    vboInterleaved.getVboID(), strideBytes, vboInterleaved.getColorOffsetBytes());
        }
        if (hasNormals) {
            Renderable.vboBindState.bindNormal(
                    vboInterleaved.getVboID(), strideBytes, vboInterleaved.getNormalOffsetBytes());
        }
        if (hasTexCoords) {
            Renderable.vboBindState.bindTextureCoordinate(
                    vboInterleaved.getVboID(), strideBytes, vboInterleaved.getTexCoordOffsetBytes());
        }
        if (hasVertices) {
            Renderable.vboBindState.bindVertex(
                    vboInterleaved.getVboID(), strideBytes, vboInterleaved.getVertexOffsetBytes());
        }
        if (hasIndexes) {
            Renderable.vboBindState.bindVertexIndex(vboInterleaved.getVboID());
        }

        drawCall.render();
    }

}
