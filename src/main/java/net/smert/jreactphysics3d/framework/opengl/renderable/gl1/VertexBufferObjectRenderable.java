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
import net.smert.jreactphysics3d.framework.opengl.constants.VertexBufferObjectTypes;
import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.renderable.AbstractRenderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.factory.RenderableFactory;
import net.smert.jreactphysics3d.framework.opengl.renderable.shared.AbstractDrawCall;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VertexBufferObjectRenderable extends AbstractRenderable {

    private final static int VBO_COLOR = 0;
    private final static int VBO_NORMAL = 1;
    private final static int VBO_TEXCOORD = 2;
    private final static int VBO_VERTEX = 3;
    private final static int VBO_VERTEX_INDEX = 4;

    private AbstractDrawCall drawCall;
    private final VertexBufferObject[] vbos;

    public VertexBufferObjectRenderable() {
        vbos = new VertexBufferObject[5];
    }

    @Override
    public void create(Mesh mesh) {

        // Destroy existing VBOs
        destroy();

        RenderableFactory.byteBuffers.reset();

        // Create VBOs
        if ((mesh.hasColors()) || (mesh.hasNormals()) || (mesh.hasTexCoords()) || (mesh.hasVertices())) {
            RenderableFactory.vboBuilder.createNonInterleavedBufferData(mesh, RenderableFactory.byteBuffers);
        }

        // Send byte buffer data for colors
        if (mesh.hasColors()) {
            vbos[VBO_COLOR] = GL.glf.createVertexBufferObject();
            VertexBufferObject vboColor = vbos[VBO_COLOR];
            vboColor.create();
            GL.vboHelper.setBufferData(vboColor.getVboID(), RenderableFactory.byteBuffers.getColor(),
                    VertexBufferObjectTypes.STATIC_DRAW);
        }

        // Send byte buffer data for normals
        if (mesh.hasNormals()) {
            vbos[VBO_NORMAL] = GL.glf.createVertexBufferObject();
            VertexBufferObject vboNormal = vbos[VBO_NORMAL];
            vboNormal.create();
            GL.vboHelper.setBufferData(vboNormal.getVboID(), RenderableFactory.byteBuffers.getNormal(),
                    VertexBufferObjectTypes.STATIC_DRAW);
        }

        // Send byte buffer data for texture coordinates
        if (mesh.hasTexCoords()) {
            vbos[VBO_TEXCOORD] = GL.glf.createVertexBufferObject();
            VertexBufferObject vboTexCoord = vbos[VBO_TEXCOORD];
            vboTexCoord.create();
            GL.vboHelper.setBufferData(vboTexCoord.getVboID(), RenderableFactory.byteBuffers.getTexCoord(),
                    VertexBufferObjectTypes.STATIC_DRAW);
        }

        // Send byte buffer data for vertices
        if (mesh.hasVertices()) {
            vbos[VBO_VERTEX] = GL.glf.createVertexBufferObject();
            VertexBufferObject vboVertex = vbos[VBO_VERTEX];
            vboVertex.create();
            GL.vboHelper.setBufferData(vboVertex.getVboID(), RenderableFactory.byteBuffers.getVertex(),
                    VertexBufferObjectTypes.STATIC_DRAW);
        }

        // Create VBO for indexes
        if (mesh.hasIndexes()) {
            vbos[VBO_VERTEX_INDEX] = GL.glf.createVertexBufferObject();
            VertexBufferObject vboVertexIndex = vbos[VBO_VERTEX_INDEX];
            vboVertexIndex.create();
            RenderableFactory.vboBuilder.createIndexBufferData(mesh, RenderableFactory.byteBuffers);
            GL.vboHelper.setBufferElementData(vboVertexIndex.getVboID(), RenderableFactory.byteBuffers.getVertexIndex(),
                    VertexBufferObjectTypes.STATIC_DRAW);
        }

        GL.vboHelper.unbind();

        // Create draw call
        drawCall = RenderableFactory.vboBuilder.createDrawCall(mesh);
    }

    @Override
    public void destroy() {

        VertexBufferObject vboColor = vbos[VBO_COLOR];
        VertexBufferObject vboNormal = vbos[VBO_NORMAL];
        VertexBufferObject vboTexCoord = vbos[VBO_TEXCOORD];
        VertexBufferObject vboVertex = vbos[VBO_VERTEX];
        VertexBufferObject vboVertexIndex = vbos[VBO_VERTEX_INDEX];

        if (vboColor != null) {
            vboColor.destroy();
        }
        if (vboNormal != null) {
            vboNormal.destroy();
        }
        if (vboTexCoord != null) {
            vboTexCoord.destroy();
        }
        if (vboVertex != null) {
            vboVertex.destroy();
        }
        if (vboVertexIndex != null) {
            vboVertexIndex.destroy();
        }
        for (int i = 0; i < vbos.length; i++) {
            vbos[i] = null;
        }
    }

    @Override
    public void render() {

        VertexBufferObject vboColor = vbos[VBO_COLOR];
        VertexBufferObject vboNormal = vbos[VBO_NORMAL];
        VertexBufferObject vboTexCoord = vbos[VBO_TEXCOORD];
        VertexBufferObject vboVertex = vbos[VBO_VERTEX];
        VertexBufferObject vboVertexIndex = vbos[VBO_VERTEX_INDEX];

        // Bind each VBO
        if (vboColor != null) {
            RenderableFactory.vboBindState.bindColor(vboColor.getVboID(), 0, 0);
        }
        if (vboNormal != null) {
            RenderableFactory.vboBindState.bindNormal(vboNormal.getVboID(), 0, 0);
        }
        if (vboTexCoord != null) {
            RenderableFactory.vboBindState.bindTextureCoordinate(vboTexCoord.getVboID(), 0, 0);
        }
        if (vboVertex != null) {
            RenderableFactory.vboBindState.bindVertex(vboVertex.getVboID(), 0, 0);
        }
        if (vboVertexIndex != null) {
            RenderableFactory.vboBindState.bindVertexIndex(vboVertexIndex.getVboID());
        }

        drawCall.render();
    }

}
