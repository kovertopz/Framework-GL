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
import net.smert.jreactphysics3d.framework.opengl.renderable.factory.RenderableFactory;
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

    private AbstractDrawCall drawCall;
    private final VertexArray[] vas;

    public VertexArrayRenderable() {
        vas = new VertexArray[5];
    }

    @Override
    public void create(Mesh mesh) {

        // Destroy existing vertex array
        destroy();

        RenderableFactory.vertexArrays.reset();

        // Create vertex arrays
        if ((mesh.hasColors()) || (mesh.hasNormals()) || (mesh.hasTexCoords()) || (mesh.hasVertices())) {
            RenderableFactory.vaBuilder.createNonInterleavedBufferData(mesh, RenderableFactory.vertexArrays);
        }

        ByteBuffer vertexIndexBuffer = null;

        // Save vertex arrays
        if (mesh.hasColors()) {
            vas[VA_COLOR] = RenderableFactory.vertexArrays.getColorVertexArray();
        }
        if (mesh.hasNormals()) {
            vas[VA_NORMAL] = RenderableFactory.vertexArrays.getNormalVertexArray();
        }
        if (mesh.hasTexCoords()) {
            vas[VA_TEXCOORD] = RenderableFactory.vertexArrays.getTexCoordVertexArray();
        }
        if (mesh.hasVertices()) {
            vas[VA_VERTEX] = RenderableFactory.vertexArrays.getVertexVertexArray();
        }
        if (mesh.hasIndexes()) {
            vas[VA_VERTEX_INDEX] = RenderableFactory.vertexArrays.getVertexIndexVertexArray();
            vertexIndexBuffer = vas[VA_VERTEX_INDEX].getByteBuffer();
        }

        // Create draw call
        drawCall = RenderableFactory.vaBuilder.createDrawCall(mesh, vertexIndexBuffer);
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

        // Bind each vertex array
        if (vaColor != null) {
            RenderableFactory.vaBindState.bindColor(vaColor.getByteBuffer());
        }
        if (vaNormal != null) {
            RenderableFactory.vaBindState.bindNormal(vaNormal.getByteBuffer());
        }
        if (vaTexCoord != null) {
            RenderableFactory.vaBindState.bindTextureCoordinate(vaTexCoord.getByteBuffer());
        }
        if (vaVertex != null) {
            RenderableFactory.vaBindState.bindVertex(vaVertex.getByteBuffer());
        }

        drawCall.render();
    }

}
