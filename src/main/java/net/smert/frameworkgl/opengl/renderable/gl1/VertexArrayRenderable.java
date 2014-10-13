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

import java.nio.ByteBuffer;
import net.smert.frameworkgl.opengl.VertexArray;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderable.RenderableConfiguration;
import net.smert.frameworkgl.opengl.renderable.shared.AbstractDrawCall;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VertexArrayRenderable extends AbstractRenderable {

    private int renderableConfigID;
    private AbstractDrawCall drawCall;
    private VertexArray vaColor;
    private VertexArray vaNormal;
    private VertexArray vaTexCoord;
    private VertexArray vaVertex;
    private VertexArray vaVertexIndex;

    public VertexArrayRenderable() {
        renderableConfigID = -1;
        drawCall = null;
    }

    @Override
    public void create(Mesh mesh) {

        // Get configuration
        renderableConfigID = mesh.getRenderableConfigID();
        RenderableConfiguration config = Renderable.configPool.get(renderableConfigID);

        // Destroy existing vertex array
        destroy();

        // Create vertex arrays
        if ((mesh.hasColors()) || (mesh.hasNormals()) || (mesh.hasTexCoords()) || (mesh.hasVertices())) {
            Renderable.vaBuilder.createNonInterleavedBufferData(mesh, Renderable.vertexArrays, config);
        }

        ByteBuffer vertexIndexBuffer = null;

        // Save vertex arrays
        if (mesh.hasColors()) {
            vaColor = Renderable.vertexArrays.getColorVertexArray();
        }
        if (mesh.hasNormals()) {
            vaNormal = Renderable.vertexArrays.getNormalVertexArray();
        }
        if (mesh.hasTexCoords()) {
            vaTexCoord = Renderable.vertexArrays.getTexCoordVertexArray();
        }
        if (mesh.hasVertices()) {
            vaVertex = Renderable.vertexArrays.getVertexVertexArray();
        }
        if (mesh.hasIndexes()) {
            vaVertexIndex = Renderable.vertexArrays.getVertexIndexVertexArray();
            vertexIndexBuffer = vaVertexIndex.getByteBuffer();
        }

        // Create draw call
        drawCall = Renderable.vaBuilder.createDrawCall(mesh, config, vertexIndexBuffer);
    }

    @Override
    public void destroy() {
        if (vaColor != null) {
            vaColor.destroy();
            vaColor = null;
        }
        if (vaNormal != null) {
            vaNormal.destroy();
            vaNormal = null;
        }
        if (vaTexCoord != null) {
            vaTexCoord.destroy();
            vaTexCoord = null;
        }
        if (vaVertex != null) {
            vaVertex.destroy();
            vaVertex = null;
        }
        if (vaVertexIndex != null) {
            vaVertexIndex.destroy();
            vaVertexIndex = null;
        }
    }

    @Override
    public void render() {

        // Switch the renderable configuration first
        Renderable.vaBindState.switchRenderableConfiguration(renderableConfigID);

        // Bind each vertex array
        if (vaColor != null) {
            Renderable.vaBindState.bindColor(vaColor.getByteBuffer());
        } else {
            Renderable.vaBindState.bindColor(null);
        }
        if (vaNormal != null) {
            Renderable.vaBindState.bindNormal(vaNormal.getByteBuffer());
        } else {
            Renderable.vaBindState.bindNormal(null);
        }
        if (vaTexCoord != null) {
            Renderable.vaBindState.bindTextureCoordinate(vaTexCoord.getByteBuffer());
        } else {
            Renderable.vaBindState.bindTextureCoordinate(null);
        }
        if (vaVertex != null) {
            Renderable.vaBindState.bindVertex(vaVertex.getByteBuffer());
        } else {
            Renderable.vaBindState.bindVertex(null);
        }

        drawCall.render();
    }

}
