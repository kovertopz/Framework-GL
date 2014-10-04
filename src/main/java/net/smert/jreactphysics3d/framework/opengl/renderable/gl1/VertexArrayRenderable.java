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

import java.nio.ByteBuffer;
import net.smert.jreactphysics3d.framework.opengl.VertexArray;
import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.renderable.AbstractRenderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.Renderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.RenderableConfiguration;
import net.smert.jreactphysics3d.framework.opengl.renderable.shared.AbstractDrawCall;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VertexArrayRenderable extends AbstractRenderable {

    private final static int VA_COLOR = 0;
    private final static int VA_NORMAL = 1;
    private final static int VA_TEXCOORD = 2;
    private final static int VA_VERTEX = 3;
    private final static int VA_VERTEX_INDEX = 4;

    private int renderableConfigID;
    private AbstractDrawCall drawCall;
    private final VertexArray[] vas;

    public VertexArrayRenderable() {
        renderableConfigID = -1;
        drawCall = null;
        vas = new VertexArray[5];
    }

    @Override
    public void create(Mesh mesh) {

        // Get configuration
        renderableConfigID = mesh.getRenderableConfigID();
        RenderableConfiguration config = Renderable.configPool.get(renderableConfigID);

        // Destroy existing vertex array
        destroy();

        Renderable.vertexArrays.reset();

        // Create vertex arrays
        if ((mesh.hasColors()) || (mesh.hasNormals()) || (mesh.hasTexCoords()) || (mesh.hasVertices())) {
            Renderable.vaBuilder.createNonInterleavedBufferData(mesh, Renderable.vertexArrays, config);
        }

        ByteBuffer vertexIndexBuffer = null;

        // Save vertex arrays
        if (mesh.hasColors()) {
            vas[VA_COLOR] = Renderable.vertexArrays.getColorVertexArray();
        }
        if (mesh.hasNormals()) {
            vas[VA_NORMAL] = Renderable.vertexArrays.getNormalVertexArray();
        }
        if (mesh.hasTexCoords()) {
            vas[VA_TEXCOORD] = Renderable.vertexArrays.getTexCoordVertexArray();
        }
        if (mesh.hasVertices()) {
            vas[VA_VERTEX] = Renderable.vertexArrays.getVertexVertexArray();
        }
        if (mesh.hasIndexes()) {
            vas[VA_VERTEX_INDEX] = Renderable.vertexArrays.getVertexIndexVertexArray();
            vertexIndexBuffer = vas[VA_VERTEX_INDEX].getByteBuffer();
        }

        // Create draw call
        drawCall = Renderable.vaBuilder.createDrawCall(mesh, config, vertexIndexBuffer);
    }

    @Override
    public void destroy() {

        VertexArray vaColor = vas[VA_COLOR];
        VertexArray vaNormal = vas[VA_NORMAL];
        VertexArray vaTexCoord = vas[VA_TEXCOORD];
        VertexArray vaVertex = vas[VA_VERTEX];
        VertexArray vaVertexIndex = vas[VA_VERTEX_INDEX];

        if (vaColor != null) {
            vaColor.destroy();
        }
        if (vaNormal != null) {
            vaNormal.destroy();
        }
        if (vaTexCoord != null) {
            vaTexCoord.destroy();
        }
        if (vaVertex != null) {
            vaVertex.destroy();
        }
        if (vaVertexIndex != null) {
            vaVertexIndex.destroy();
        }
        for (int i = 0; i < vas.length; i++) {
            vas[i] = null;
        }
    }

    @Override
    public void render() {

        VertexArray vaColor = vas[VA_COLOR];
        VertexArray vaNormal = vas[VA_NORMAL];
        VertexArray vaTexCoord = vas[VA_TEXCOORD];
        VertexArray vaVertex = vas[VA_VERTEX];

        // Switch the renderable configuration first
        Renderable.vaBindState.switchRenderableConfiguration(renderableConfigID);

        // Bind each vertex array
        if (vaColor != null) {
            Renderable.vaBindState.bindColor(vaColor.getByteBuffer());
        }
        if (vaNormal != null) {
            Renderable.vaBindState.bindNormal(vaNormal.getByteBuffer());
        }
        if (vaTexCoord != null) {
            Renderable.vaBindState.bindTextureCoordinate(vaTexCoord.getByteBuffer());
        }
        if (vaVertex != null) {
            Renderable.vaBindState.bindVertex(vaVertex.getByteBuffer());
        }

        drawCall.render();
    }

}
