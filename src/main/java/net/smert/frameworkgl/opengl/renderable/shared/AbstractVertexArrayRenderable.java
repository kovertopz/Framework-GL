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
package net.smert.frameworkgl.opengl.renderable.shared;

import java.nio.ByteBuffer;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.VertexArray;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderable.RenderableConfiguration;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public abstract class AbstractVertexArrayRenderable extends AbstractRenderable {

    protected int renderableConfigID;
    protected RenderCall renderCall;
    protected VertexArray vaColor;
    protected VertexArray vaNormal;
    protected VertexArray vaTexCoord;
    protected VertexArray vaVertex;
    protected VertexArray vaVertexIndex;

    public AbstractVertexArrayRenderable() {
        renderableConfigID = -1;
        renderCall = null;
        vaColor = null;
        vaNormal = null;
        vaTexCoord = null;
        vaVertex = null;
        vaVertexIndex = null;
    }

    protected void bindGL1() {

        // Must unbind any VBOs
        Renderable.bindState.unbindVBO();

        // Switch the renderable configuration first
        Renderable.bindState.switchRenderableConfiguration(renderableConfigID);
        RenderableConfiguration config = Renderable.bindState.getConfig();

        // Bind each vertex array
        if (vaColor != null) {
            GL.vaHelper.enableColors();
            GL.vaHelper.bindColors(config.getColorSize(), config.getColorType(), vaColor.getByteBuffer());
        } else {
            GL.vaHelper.disableColors();
        }
        if (vaNormal != null) {
            GL.vaHelper.enableNormals();
            GL.vaHelper.bindNormals(config.getNormalType(), vaNormal.getByteBuffer());
        } else {
            GL.vaHelper.disableNormals();
        }
        if (vaTexCoord != null) {
            GL.vaHelper.enableTexCoords();
            GL.vaHelper.bindTexCoords(config.getTexCoordSize(), config.getTexCoordType(), vaTexCoord.getByteBuffer());
        } else {
            GL.vaHelper.disableTexCoords();
        }
        if (vaVertex != null) {
            GL.vaHelper.enableVertices();
            GL.vaHelper.bindVertices(config.getVertexSize(), config.getVertexType(), vaVertex.getByteBuffer());
        } else {
            GL.vaHelper.disableVertices();
        }
    }

    protected void bindGL2() {

        // Must unbind any VBOs
        Renderable.bindState.unbindVBO();

        // Switch the renderable configuration first
        Renderable.bindState.switchRenderableConfiguration(renderableConfigID);
        RenderableConfiguration config = Renderable.bindState.getConfig();

        // Bind each vertex array
        if (vaColor != null) {
            GL.vaHelper.enableVertexAttribArray(Renderable.bindState.getColorIndex());
            GL.vaHelper.bindVertexAttrib(Renderable.bindState.getColorIndex(), config.getColorSize(),
                    config.getColorType(), vaColor.getByteBuffer());
        } else {
            GL.vaHelper.disableVertexAttribArray(Renderable.bindState.getColorIndex());
        }
        if (vaNormal != null) {
            GL.vaHelper.enableVertexAttribArray(Renderable.bindState.getNormalIndex());
            GL.vaHelper.bindVertexAttrib(Renderable.bindState.getNormalIndex(), config.getNormalSize(),
                    config.getNormalType(), vaNormal.getByteBuffer());
        } else {
            GL.vaHelper.disableVertexAttribArray(Renderable.bindState.getNormalIndex());
        }
        if (vaTexCoord != null) {
            GL.vaHelper.enableVertexAttribArray(Renderable.bindState.getTexCoord0Index());
            GL.vaHelper.bindVertexAttrib(Renderable.bindState.getTexCoord0Index(), config.getTexCoordSize(),
                    config.getTexCoordType(), vaTexCoord.getByteBuffer());
        } else {
            GL.vaHelper.disableVertexAttribArray(Renderable.bindState.getTexCoord0Index());
        }
        if (vaVertex != null) {
            GL.vaHelper.enableVertexAttribArray(Renderable.bindState.getVertexIndex());
            GL.vaHelper.bindVertexAttrib(Renderable.bindState.getVertexIndex(), config.getVertexSize(),
                    config.getVertexType(), vaVertex.getByteBuffer());
        } else {
            GL.vaHelper.disableVertexAttribArray(Renderable.bindState.getVertexIndex());
        }
    }

    protected void createGL1AndGL2(Mesh mesh) {

        // Get configuration
        renderableConfigID = mesh.getRenderableConfigID();
        RenderableConfiguration config = Renderable.configPool.get(renderableConfigID);

        // Destroy existing vertex array
        destroy();

        // Create vertex arrays
        if (mesh.hasColors() || mesh.hasNormals() || mesh.hasTexCoords() || mesh.hasVertices()) {
            Renderable.renderableBuilder.createNonInterleavedBufferData(mesh, Renderable.vertexArrays, config);
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
            Renderable.renderableBuilder.createIndexBufferData(mesh, Renderable.vertexArrays, config);
            vaVertexIndex = Renderable.vertexArrays.getVertexIndexVertexArray();
            vertexIndexBuffer = vaVertexIndex.getByteBuffer();
        }

        // Create render call
        renderCall = Renderable.vaDrawCallBuilder.createRenderCall(mesh, config, vertexIndexBuffer);
    }

    protected void renderGL1() {
        bindGL1();
        renderCall.render();
    }

    protected void renderGL2() {
        bindGL2();
        renderCall.render();
    }

    protected void updateGL1AndGL2(Mesh mesh) {

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

}
