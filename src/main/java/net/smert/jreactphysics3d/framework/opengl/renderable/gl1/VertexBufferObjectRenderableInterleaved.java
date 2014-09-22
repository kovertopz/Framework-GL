package net.smert.jreactphysics3d.framework.opengl.renderable.gl1;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import net.smert.jreactphysics3d.framework.opengl.VertexBufferObject;
import net.smert.jreactphysics3d.framework.opengl.VertexBufferObjectInterleaved;
import net.smert.jreactphysics3d.framework.opengl.constants.VertexBufferObjectTypes;
import net.smert.jreactphysics3d.framework.opengl.helpers.VertexBufferObjectHelper;
import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.renderable.AbstractRenderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.AbstractDrawCall;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.BindState;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.Builder;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.Configuration;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VertexBufferObjectRenderableInterleaved extends AbstractRenderable {

    private static BindState vboBindState;
    private static Builder vboBuilder;
    private static Configuration vboConfiguration;
    private static VertexBufferObjectHelper vboHelper;
    private AbstractDrawCall drawCall;
    private VertexBufferObject vboIndex;
    private VertexBufferObjectInterleaved vboInterleaved;

    public VertexBufferObjectRenderableInterleaved(Mesh mesh) {
        super(mesh);
    }

    @Override
    public void create() {

        // Destroy existing VBOs
        destroy();

        // Create VBO
        if ((mesh.hasColors()) || (mesh.hasNormals()) || (mesh.hasTexCoords()) || (mesh.hasVertices())) {
            vboInterleaved = new VertexBufferObjectInterleaved();
            vboInterleaved.create();
            vboBuilder.calculateOffsetsAndStride(mesh, vboConfiguration, vboInterleaved);
            ByteBuffer byteBuffer = vboBuilder.createBufferData(mesh, vboConfiguration, vboInterleaved);
            vboHelper.setBufferData(vboInterleaved.getVboID(), byteBuffer, VertexBufferObjectTypes.STATIC_DRAW);
        }

        // Create VBO for indexes
        if (mesh.hasIndexes()) {
            vboIndex = new VertexBufferObject();
            vboIndex.create();
            IntBuffer intBuffer = vboBuilder.createBufferElementData(mesh);
            vboHelper.setBufferElementData(vboIndex.getVboID(), intBuffer, VertexBufferObjectTypes.STATIC_DRAW);
        }

        // Create draw call
        drawCall = vboBuilder.createDrawCall(mesh);
    }

    @Override
    public void destroy() {
        if (vboInterleaved != null) {
            vboInterleaved.destroy();
            vboInterleaved = null;
        }
        if (vboIndex != null) {
            vboIndex.destroy();
            vboIndex = null;
        }
    }

    @Override
    public void render() {

        int strideBytes = vboInterleaved.getStrideBytes();

        if (mesh.hasColors()) {
            vboBindState.bindColor(vboInterleaved.getVboID(), strideBytes, vboInterleaved.getColorOffsetBytes());
        }
        if (mesh.hasNormals()) {
            vboBindState.bindNormal(vboInterleaved.getVboID(), strideBytes, vboInterleaved.getNormalOffsetBytes());
        }
        if (mesh.hasTexCoords()) {
            vboBindState.bindTextureCoordinate(vboInterleaved.getVboID(), strideBytes, vboInterleaved.getTexCoordOffsetBytes());
        }
        if (mesh.hasVertices()) {
            vboBindState.bindVertex(vboInterleaved.getVboID(), strideBytes, vboInterleaved.getVertexOffsetBytes());
        }
        if (mesh.hasIndexes()) {
            vboBindState.bindVertexIndex(vboInterleaved.getVboID());
        }
    }

    public static void SetVboBindState(BindState vboBindState) {
        VertexBufferObjectRenderableInterleaved.vboBindState = vboBindState;
    }

    public static void SetVboBuilder(Builder vboBuilder) {
        VertexBufferObjectRenderableInterleaved.vboBuilder = vboBuilder;
    }

    public static void SetVboConfiguration(Configuration vboConfiguration) {
        VertexBufferObjectRenderableInterleaved.vboConfiguration = vboConfiguration;
        if (vboConfiguration.isImmutable() == false) {
            throw new RuntimeException("VBO configuration must be made immutable");
        }
    }

    public static void SetVboHelper(VertexBufferObjectHelper vboHelper) {
        VertexBufferObjectRenderableInterleaved.vboHelper = vboHelper;
    }

}
