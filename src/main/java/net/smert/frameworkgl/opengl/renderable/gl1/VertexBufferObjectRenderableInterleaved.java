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

import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.VertexBufferObject;
import net.smert.frameworkgl.opengl.VertexBufferObjectInterleaved;
import net.smert.frameworkgl.opengl.constants.VertexBufferObjectTypes;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderable.RenderableConfiguration;
import net.smert.frameworkgl.opengl.renderable.shared.AbstractDrawCall;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VertexBufferObjectRenderableInterleaved extends AbstractRenderable {

    protected boolean hasColors;
    protected boolean hasIndexes;
    protected boolean hasNormals;
    protected boolean hasTexCoords;
    protected boolean hasVertices;
    protected int bufferUsage;
    protected int renderableConfigID;
    protected AbstractDrawCall drawCall;
    protected VertexBufferObject vboVertexIndex;
    protected VertexBufferObjectInterleaved vboInterleaved;

    public VertexBufferObjectRenderableInterleaved() {
        bufferUsage = VertexBufferObjectTypes.STATIC_DRAW;
        renderableConfigID = -1;
        drawCall = null;
    }

    @Override
    public void create(Mesh mesh) {

        // Get configuration
        renderableConfigID = mesh.getRenderableConfigID();
        RenderableConfiguration config = Renderable.configPool.get(renderableConfigID);

        // Destroy existing VBOs
        destroy();

        // Create interleaved buffer data, VBO and send interleaved buffer data
        if ((mesh.hasColors()) || (mesh.hasNormals()) || (mesh.hasTexCoords()) || (mesh.hasVertices())) {
            vboInterleaved = GL.glFactory.createVertexBufferObjectInterleaved();
            vboInterleaved.create();
            Renderable.vboBuilder.calculateOffsetsAndStride(mesh, vboInterleaved, config);
            Renderable.vboBuilder.createInterleavedBufferData(
                    mesh, vboInterleaved.getStrideBytes(), Renderable.byteBuffers, config);
            GL.vboHelper.setBufferData(vboInterleaved.getVboID(), Renderable.byteBuffers.getInterleaved(), bufferUsage);
        }

        hasColors = mesh.hasColors();
        hasNormals = mesh.hasNormals();
        hasTexCoords = mesh.hasTexCoords();
        hasVertices = mesh.hasVertices();

        // Create VBO, byte buffer data and send byte buffer data for indexes
        if (mesh.hasIndexes()) {
            vboVertexIndex = GL.glFactory.createVertexBufferObject();
            vboVertexIndex.create();
            Renderable.vboBuilder.createIndexBufferData(mesh, Renderable.byteBuffers, config);
            GL.vboHelper.setBufferElementData(vboVertexIndex.getVboID(), Renderable.byteBuffers.getVertexIndex(),
                    bufferUsage);
            hasIndexes = true;
        }

        GL.vboHelper.unbind();

        // Create draw call
        drawCall = Renderable.vboBuilder.createDrawCall(mesh, config);
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

        // Switch the renderable configuration first
        Renderable.vboBindState.switchRenderableConfiguration(renderableConfigID);

        // Bind VBO for each type
        if (hasColors) {
            Renderable.vboBindState.bindColor(
                    vboInterleaved.getVboID(), strideBytes, vboInterleaved.getColorOffsetBytes());
        } else {
            Renderable.vboBindState.bindColor(0, 0, 0);
        }
        if (hasNormals) {
            Renderable.vboBindState.bindNormal(
                    vboInterleaved.getVboID(), strideBytes, vboInterleaved.getNormalOffsetBytes());
        } else {
            Renderable.vboBindState.bindNormal(0, 0, 0);
        }
        if (hasTexCoords) {
            Renderable.vboBindState.bindTextureCoordinate(
                    vboInterleaved.getVboID(), strideBytes, vboInterleaved.getTexCoordOffsetBytes());
        } else {
            Renderable.vboBindState.bindTextureCoordinate(0, 0, 0);
        }
        if (hasVertices) {
            Renderable.vboBindState.bindVertex(
                    vboInterleaved.getVboID(), strideBytes, vboInterleaved.getVertexOffsetBytes());
        } else {
            Renderable.vboBindState.bindVertex(0, 0, 0);
        }
        if (hasIndexes) {
            Renderable.vboBindState.bindVertexIndex(vboInterleaved.getVboID());
        } else {
            Renderable.vboBindState.bindVertexIndex(0);
        }

        drawCall.render();
    }

}
