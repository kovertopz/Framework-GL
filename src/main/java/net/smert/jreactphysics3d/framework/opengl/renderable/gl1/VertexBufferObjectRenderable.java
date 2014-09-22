package net.smert.jreactphysics3d.framework.opengl.renderable.gl1;

import net.smert.jreactphysics3d.framework.opengl.VertexBufferObject;
import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.renderable.AbstractRenderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.BindState;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.Builder;

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
    private final VertexBufferObject[] vbos;

    public VertexBufferObjectRenderable(Mesh mesh) {
        super(mesh);
        vbos = new VertexBufferObject[5];
    }

    @Override
    public void create() {

        // Destroy existing VBOs
        destroy();

        // Create VBOs
        if (mesh.hasColors()) {
            vbos[VBO_COLOR] = new VertexBufferObject();
        }
        if (mesh.hasNormals()) {
            vbos[VBO_NORMAL] = new VertexBufferObject();
        }
        if (mesh.hasTexCoords()) {
            vbos[VBO_TEXCOORD] = new VertexBufferObject();
        }
        if (mesh.hasVertices()) {
            vbos[VBO_VERTEX] = new VertexBufferObject();
        }
        if (mesh.hasIndexes()) {
            vbos[VBO_VERTEX_INDEX] = new VertexBufferObject();
        }
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

}
