package net.smert.jreactphysics3d.framework.opengl.renderable.vbo;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import net.smert.jreactphysics3d.framework.math.Vector3f;
import net.smert.jreactphysics3d.framework.math.Vector4f;
import net.smert.jreactphysics3d.framework.opengl.VertexBufferObjectInterleaved;
import net.smert.jreactphysics3d.framework.opengl.constants.GLTypes;
import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.mesh.Segment;
import net.smert.jreactphysics3d.framework.utils.ListUtils;
import org.lwjgl.BufferUtils;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Builder {

    private int convertGLTypeToByteSize(int glType) {

        int byteSize = 0;

        // Convert the type to the byte size of the type
        switch (glType) {
            case GLTypes.BYTE:
            case GLTypes.UNSIGNED_BYTE:
                byteSize = 1;
                break;

            case GLTypes.SHORT:
            case GLTypes.UNSIGNED_SHORT:
                byteSize = 2;
                break;

            case GLTypes.FLOAT:
            case GLTypes.INT:
            case GLTypes.UNSIGNED_INT:
                byteSize = 4;
                break;

            default:
                throw new IllegalArgumentException("Unknown GL type constant: " + glType);
        }

        return byteSize;
    }

    private void convertColorToByteBuffer(Vector4f vector, int size, int glType, ByteBuffer byteBuffer) {

        if ((size != 3) && (size != 4)) {
            throw new RuntimeException("Color size must be 3 or 4. Was given: " + size);
        }

        float x = vector.getX();
        float y = vector.getY();
        float z = vector.getZ();
        float w = vector.getW();

        assert (x >= 0.0f && x <= 1.0f);
        assert (y >= 0.0f && y <= 1.0f);
        assert (z >= 0.0f && z <= 1.0f);
        assert (w >= 0.0f && w <= 1.0f);

        // Depending on the GL type and size convert the data and put it into the byte buffer
        switch (glType) {
            case GLTypes.BYTE:
            case GLTypes.UNSIGNED_BYTE:
                byteBuffer.put((byte) ((x / 1.0f) * 255));
                byteBuffer.put((byte) ((y / 1.0f) * 255));
                byteBuffer.put((byte) ((z / 1.0f) * 255));
                if (size == 4) {
                    byteBuffer.put((byte) ((w / 1.0f) * 255));
                }
                break;

            case GLTypes.FLOAT:
                byteBuffer.putFloat(x);
                byteBuffer.putFloat(y);
                byteBuffer.putFloat(z);
                if (size == 4) {
                    byteBuffer.putFloat(w);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown GL type constant for color: " + glType);
        }
    }

    private void convertNormalToByteBuffer(Vector3f vector, int glType, ByteBuffer byteBuffer) {

        float x = vector.getX();
        float y = vector.getY();
        float z = vector.getZ();

        assert (x >= -1.0f && x <= 1.0f);
        assert (y >= -1.0f && y <= 1.0f);
        assert (z >= -1.0f && z <= 1.0f);

        // Depending on the GL type put it into the byte buffer
        switch (glType) {
            case GLTypes.FLOAT:
                byteBuffer.putFloat(x);
                byteBuffer.putFloat(y);
                byteBuffer.putFloat(z);
                break;

            default:
                throw new IllegalArgumentException("Unknown GL type constant for normal: " + glType);
        }
    }

    private void convertTexCoordToByteBuffer(Vector3f vector, int size, int glType, ByteBuffer byteBuffer) {

        if ((size != 2) && (size != 3)) {
            throw new RuntimeException("Texture coordinate size must be 2 or 3. Was given: " + size);
        }

        float x = vector.getX();
        float y = vector.getY();
        float z = vector.getZ();

        assert (x >= 0.0f && x <= 1.0f);
        assert (y >= 0.0f && y <= 1.0f);
        assert (z >= 0.0f && z <= 1.0f);

        // Depending on the GL type and size put it into the byte buffer
        switch (glType) {
            case GLTypes.FLOAT:
                byteBuffer.putFloat(x);
                byteBuffer.putFloat(y);
                if (size == 3) {
                    byteBuffer.putFloat(z);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown GL type constant for texture coordinate: " + glType);
        }
    }

    private void convertVertexToByteBuffer(Vector4f vector, int size, int glType, ByteBuffer byteBuffer) {

        if ((size != 3) && (size != 4)) {
            throw new RuntimeException("Vertex size must be 3 or 4. Was given: " + size);
        }

        float x = vector.getX();
        float y = vector.getY();
        float z = vector.getZ();
        float w = vector.getW();

        assert (x >= -1.0f && x <= 1.0f);
        assert (y >= -1.0f && y <= 1.0f);
        assert (z >= -1.0f && z <= 1.0f);
        assert (w >= -1.0f && w <= 1.0f);

        // Depending on the GL type and size put it into the byte buffer
        switch (glType) {
            case GLTypes.FLOAT:
                byteBuffer.putFloat(x);
                byteBuffer.putFloat(y);
                byteBuffer.putFloat(z);
                if (size == 4) {
                    byteBuffer.putFloat(w);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown GL type constant for vertex: " + glType);
        }
    }

    public void calculateOffsetsAndStride(
            Mesh mesh, Configuration vboConfiguration, VertexBufferObjectInterleaved vboInterleaved) {

        int total = 0;

        // Calculate byte size of each type and add to the total. Save the total as
        // the current offset before increasing it.
        if (mesh.hasColors()) {
            vboInterleaved.setColorOffsetBytes(total);
            int byteSize = convertGLTypeToByteSize(vboConfiguration.getColorType());
            total += vboConfiguration.getColorSize() * byteSize;
        }
        if (mesh.hasNormals()) {
            vboInterleaved.setNormalOffsetBytes(total);
            int byteSize = convertGLTypeToByteSize(vboConfiguration.getNormalType());
            total += 3 * byteSize;
        }
        if (mesh.hasTexCoords()) {
            vboInterleaved.setTexCoordOffsetBytes(total);
            int byteSize = convertGLTypeToByteSize(vboConfiguration.getTexCoordType());
            total += vboConfiguration.getTexCoordSize() * byteSize;
        }
        if (mesh.hasVertices()) {
            vboInterleaved.setVertexOffsetBytes(total);
            int byteSize = convertGLTypeToByteSize(vboConfiguration.getTexCoordType());
            total += vboConfiguration.getTexCoordSize() * byteSize;
        }

        vboInterleaved.setStrideBytes(total);
    }

    public ByteBuffer createBufferData(
            Mesh mesh, Configuration vboConfiguration, VertexBufferObjectInterleaved vboInterleaved) {

        // Create byte buffer to hold color, normal, texture coordinates and vertex data
        int bufferSize = vboInterleaved.getStrideBytes() * mesh.getTotalVerticies();
        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(bufferSize);

        // For each segment in the mesh
        for (int i = 0, max = mesh.getTotalSegments(); i < max; i++) {
            Segment segment = mesh.getSegment(i);

            // For each vertex in the segment
            for (int j = 0, max2 = segment.getVertices().size(); j < max2; j++) {

                // For each type convert the data and add to the byte buffer
                if (mesh.hasColors()) {
                    int colorSize = vboConfiguration.getColorSize();
                    int colorType = vboConfiguration.getColorType();
                    Vector4f color = segment.getColors().get(j);
                    convertColorToByteBuffer(color, colorSize, colorType, byteBuffer);
                }
                if (mesh.hasNormals()) {
                    Vector3f normal = segment.getNormals().get(j);
                    convertNormalToByteBuffer(normal, max, byteBuffer);
                }
                if (mesh.hasTexCoords()) {
                    int texCoordSize = vboConfiguration.getTexCoordSize();
                    int texCoordType = vboConfiguration.getTexCoordType();
                    Vector3f texCoord = segment.getTexCoords().get(j);
                    convertTexCoordToByteBuffer(texCoord, texCoordSize, texCoordType, byteBuffer);
                }
                if (mesh.hasVertices()) {
                    int vertexSize = vboConfiguration.getVertexSize();
                    int vertexType = vboConfiguration.getVertexType();
                    Vector4f vertex = segment.getVertices().get(j);
                    convertVertexToByteBuffer(vertex, vertexSize, vertexType, byteBuffer);
                }
            }
        }

        // Missy Elliott that shit
        byteBuffer.flip();

        return byteBuffer;
    }

    public IntBuffer createBufferElementData(Mesh mesh) {

        // Create int buffer
        int bufferSize = mesh.getIndexes().size();
        IntBuffer intBuffer = BufferUtils.createIntBuffer(bufferSize);

        // Convert list to primitive array
        int[] intArray = ListUtils.ToPrimitiveIntArray(mesh.getIndexes());
        intBuffer.put(intArray);
        intBuffer.flip();

        return intBuffer;
    }

    public AbstractDrawCall createDrawCall(Mesh mesh) {
        AbstractDrawCall drawCall;

        if (mesh.hasIndexes()) {
            if (mesh.canRenderRanged()) {
                drawCall = new DrawRangeElements();
            } else {
                drawCall = new DrawElements();
            }
        } else {
            drawCall = new DrawArrays();
        }

        return drawCall;
    }

}