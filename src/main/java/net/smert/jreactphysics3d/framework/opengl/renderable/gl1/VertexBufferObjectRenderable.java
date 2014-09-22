package net.smert.jreactphysics3d.framework.opengl.renderable.gl1;

import net.smert.jreactphysics3d.framework.opengl.GL;
import net.smert.jreactphysics3d.framework.opengl.VertexBufferObject;
import net.smert.jreactphysics3d.framework.opengl.constants.VertexBufferObjectTypes;
import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.renderable.AbstractRenderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.AbstractDrawCall;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.BindState;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.Builder;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.ByteBuffers;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.Configuration;

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

    private static BindState vboBindState;
    private static Builder vboBuilder;
    private static Configuration vboConfiguration;
    private AbstractDrawCall drawCall;
    private final VertexBufferObject[] vbos;

    public VertexBufferObjectRenderable(Mesh mesh) {
        super(mesh);
        vbos = new VertexBufferObject[5];
    }

    @Override
    public void create() {

        // Destroy existing VBOs
        destroy();

        ByteBuffers byteBuffers = new ByteBuffers();

        // Create VBOs
        if ((mesh.hasColors()) || (mesh.hasNormals()) || (mesh.hasTexCoords()) || (mesh.hasVertices())) {
            vboBuilder.createNonInterleavedBufferData(mesh, vboConfiguration, byteBuffers);
        }

        // Send byte buffer data for colors
        if (mesh.hasColors()) {
            vbos[VBO_COLOR] = new VertexBufferObject();
            VertexBufferObject vboColor = vbos[VBO_COLOR];
            vboColor.create();
            GL.vboHelper.setBufferData(vboColor.getVboID(), byteBuffers.color, VertexBufferObjectTypes.STATIC_DRAW);
        }

        // Send byte buffer data for normals
        if (mesh.hasNormals()) {
            vbos[VBO_NORMAL] = new VertexBufferObject();
            VertexBufferObject vboNormal = vbos[VBO_NORMAL];
            vboNormal.create();
            GL.vboHelper.setBufferData(vboNormal.getVboID(), byteBuffers.normal, VertexBufferObjectTypes.STATIC_DRAW);
        }

        // Send byte buffer data for texture coordinates
        if (mesh.hasTexCoords()) {
            vbos[VBO_TEXCOORD] = new VertexBufferObject();
            VertexBufferObject vboTexCoord = vbos[VBO_TEXCOORD];
            vboTexCoord.create();
            GL.vboHelper.setBufferData(vboTexCoord.getVboID(), byteBuffers.texCoord, VertexBufferObjectTypes.STATIC_DRAW);
        }

        // Send byte buffer data for vertices
        if (mesh.hasVertices()) {
            vbos[VBO_VERTEX] = new VertexBufferObject();
            VertexBufferObject vboVertex = vbos[VBO_TEXCOORD];
            vboVertex.create();
            GL.vboHelper.setBufferData(vboVertex.getVboID(), byteBuffers.vertex, VertexBufferObjectTypes.STATIC_DRAW);
        }

        // Create VBO for indexes
        if (mesh.hasIndexes()) {
            vbos[VBO_VERTEX_INDEX] = new VertexBufferObject();
            VertexBufferObject vboVertexIndex = vbos[VBO_VERTEX_INDEX];
            vboVertexIndex.create();
            vboBuilder.createIndexBufferData(mesh, vboConfiguration, byteBuffers);
            GL.vboHelper.setBufferElementData(vboVertexIndex.getVboID(), byteBuffers.index, VertexBufferObjectTypes.STATIC_DRAW);
        }

        // Create draw call
        drawCall = vboBuilder.createDrawCall(mesh);
    }

    @Override
    public void destroy() {

        VertexBufferObject vboColor = vbos[VBO_COLOR];
        VertexBufferObject vboNormal = vbos[VBO_NORMAL];
        VertexBufferObject vboTexCoord = vbos[VBO_TEXCOORD];
        VertexBufferObject vboVertex = vbos[VBO_TEXCOORD];
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
        VertexBufferObject vboVertex = vbos[VBO_TEXCOORD];
        VertexBufferObject vboVertexIndex = vbos[VBO_VERTEX_INDEX];

        if (vboColor != null) {
            vboBindState.bindColor(vboColor.getVboID(), 0, 0);
        }
        if (vboNormal != null) {
            vboBindState.bindNormal(vboNormal.getVboID(), 0, 0);
        }
        if (vboTexCoord != null) {
            vboBindState.bindTextureCoordinate(vboTexCoord.getVboID(), 0, 0);
        }
        if (vboVertex != null) {
            vboBindState.bindVertex(vboVertex.getVboID(), 0, 0);
        }
        if (vboVertexIndex != null) {
            vboBindState.bindVertexIndex(vboVertexIndex.getVboID());
        }
    }

    public static void SetVboBindState(BindState vboBindState) {
        VertexBufferObjectRenderable.vboBindState = vboBindState;
    }

    public static void SetVboBuilder(Builder vboBuilder) {
        VertexBufferObjectRenderable.vboBuilder = vboBuilder;
    }

    public static void SetVboConfiguration(Configuration vboConfiguration) {
        VertexBufferObjectRenderable.vboConfiguration = vboConfiguration;
        if (vboConfiguration.isImmutable() == false) {
            throw new RuntimeException("VBO configuration must be made immutable");
        }
    }

}
