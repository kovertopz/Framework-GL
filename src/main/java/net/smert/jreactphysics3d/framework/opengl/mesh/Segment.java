package net.smert.jreactphysics3d.framework.opengl.mesh;

import java.util.ArrayList;
import java.util.List;
import net.smert.jreactphysics3d.framework.math.Vector3f;
import net.smert.jreactphysics3d.framework.math.Vector4f;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Segment {

    private int maxIndex;
    private int minIndex;
    private final int primitiveMode;
    private final List<Vector4f> colors;
    private final List<Vector3f> normals;
    private final List<Vector3f> texCoords;
    private final List<Vector4f> vertices;

    public Segment(int primitiveMode) {
        maxIndex = 0;
        minIndex = 0;
        this.primitiveMode = primitiveMode;
        colors = new ArrayList<>();
        normals = new ArrayList<>();
        texCoords = new ArrayList<>();
        vertices = new ArrayList<>();
    }

    public void addColor(float x, float y, float z) {
        colors.add(new Vector4f(x, y, z, 1.0f));
    }

    public void addColor(float x, float y, float z, float w) {
        colors.add(new Vector4f(x, y, z, w));
    }

    public void addColor(Vector4f vector) {
        colors.add(vector);
    }

    public void addNormal(float x, float y, float z) {
        normals.add(new Vector3f(x, y, z));
    }

    public void addNormal(Vector3f vector) {
        normals.add(vector);
    }

    public void addTexCoord(float x, float y) {
        texCoords.add(new Vector3f(x, y, 0));
    }

    public void addTexCoord(float x, float y, float z) {
        texCoords.add(new Vector3f(x, y, z));
    }

    public void addTexCoord(Vector3f vector) {
        texCoords.add(vector);
    }

    public void addVertex(float x, float y, float z) {
        vertices.add(new Vector4f(x, y, z, 1.0f));
    }

    public void addVertex(float x, float y, float z, float w) {
        vertices.add(new Vector4f(x, y, z, w));
    }

    public void addVertex(Vector4f vector) {
        vertices.add(vector);
    }

    public int getMaxIndex() {
        return maxIndex;
    }

    public void setMaxIndex(int maxIndex) {
        this.maxIndex = maxIndex;
    }

    public int getMinIndex() {
        return minIndex;
    }

    public void setMinIndex(int minIndex) {
        this.minIndex = minIndex;
    }

    public int getPrimitiveMode() {
        return primitiveMode;
    }

    public List<Vector4f> getColors() {
        return colors;
    }

    public List<Vector3f> getNormals() {
        return normals;
    }

    public List<Vector3f> getTexCoords() {
        return texCoords;
    }

    public List<Vector4f> getVertices() {
        return vertices;
    }

}
