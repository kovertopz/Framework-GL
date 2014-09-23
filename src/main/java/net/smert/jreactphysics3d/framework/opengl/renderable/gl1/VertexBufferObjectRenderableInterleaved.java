package net.smert.jreactphysics3d.framework.opengl.renderable.gl1;

import net.smert.jreactphysics3d.framework.opengl.GL;
import net.smert.jreactphysics3d.framework.opengl.VertexBufferObject;
import net.smert.jreactphysics3d.framework.opengl.VertexBufferObjectInterleaved;
import net.smert.jreactphysics3d.framework.opengl.constants.VertexBufferObjectTypes;
import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.renderable.AbstractRenderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.factory.Configuration;
import net.smert.jreactphysics3d.framework.opengl.renderable.shared.AbstractDrawCall;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.VBOBindState;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.VBOBuilder;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.ByteBuffers;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VertexBufferObjectRenderableInterleaved extends AbstractRenderable {

    private static Configuration renderableConfig;
    private static VBOBindState vboBindState;
    private static VBOBuilder vboBuilder;
    private AbstractDrawCall drawCall;
    private VertexBufferObject vboVertexIndex;
    private VertexBufferObjectInterleaved vboInterleaved;

    public VertexBufferObjectRenderableInterleaved(Mesh mesh) {
        super(mesh);
    }

    @Override
    public void create() {

        // Destroy existing VBOs
        destroy();

        ByteBuffers byteBuffers = new ByteBuffers();

        // Create VBO
        if ((mesh.hasColors()) || (mesh.hasNormals()) || (mesh.hasTexCoords()) || (mesh.hasVertices())) {
            vboInterleaved = new VertexBufferObjectInterleaved();
            vboInterleaved.create();
            vboBuilder.calculateOffsetsAndStride(mesh, renderableConfig, vboInterleaved);
            vboBuilder.createInterleavedBufferData(mesh, renderableConfig, vboInterleaved.getStrideBytes(), byteBuffers);
            GL.vboHelper.setBufferData(
                    vboInterleaved.getVboID(), byteBuffers.getInterleaved(), VertexBufferObjectTypes.STATIC_DRAW);
        }

        // Create VBO for indexes
        if (mesh.hasIndexes()) {
            vboVertexIndex = new VertexBufferObject();
            vboVertexIndex.create();
            vboBuilder.createIndexBufferData(mesh, renderableConfig, byteBuffers);
            GL.vboHelper.setBufferElementData(
                    vboVertexIndex.getVboID(), byteBuffers.getVertexIndex(), VertexBufferObjectTypes.STATIC_DRAW);
        }

        GL.vboHelper.unbind();

        // Create draw call
        drawCall = vboBuilder.createDrawCall(mesh);
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
    }

    @Override
    public void render() {

        int strideBytes = vboInterleaved.getStrideBytes();

        // Bind VBO for each type
        if (mesh.hasColors()) {
            vboBindState.bindColor(vboInterleaved.getVboID(), strideBytes, vboInterleaved.getColorOffsetBytes());
        }
        if (mesh.hasNormals()) {
            vboBindState.bindNormal(vboInterleaved.getVboID(), strideBytes, vboInterleaved.getNormalOffsetBytes());
        }
        if (mesh.hasTexCoords()) {
            vboBindState.bindTextureCoordinate(
                    vboInterleaved.getVboID(), strideBytes, vboInterleaved.getTexCoordOffsetBytes());
        }
        if (mesh.hasVertices()) {
            vboBindState.bindVertex(vboInterleaved.getVboID(), strideBytes, vboInterleaved.getVertexOffsetBytes());
        }
        if (mesh.hasIndexes()) {
            vboBindState.bindVertexIndex(vboInterleaved.getVboID());
        }

        drawCall.render();
    }

    public static void SetRenderableConfiguration(Configuration renderableConfig) {
        VertexBufferObjectRenderableInterleaved.renderableConfig = renderableConfig;
        if (renderableConfig.isImmutable() == false) {
            throw new RuntimeException("Renderable configuration must be made immutable");
        }
    }

    public static void SetVboBindState(VBOBindState vboBindState) {
        VertexBufferObjectRenderableInterleaved.vboBindState = vboBindState;
    }

    public static void SetVboBuilder(VBOBuilder vboBuilder) {
        VertexBufferObjectRenderableInterleaved.vboBuilder = vboBuilder;
    }

}
