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
    private final int primitive;
    private final List<Vector4f> colors;
    private final List<Vector3f> normals;
    private final List<Vector3f> texCoords;
    private final List<Vector4f> vertices;

    public Segment(int primitive) {
        maxIndex = 0;
        minIndex = 0;
        this.primitive = primitive;
        colors = new ArrayList<>();
        normals = new ArrayList<>();
        texCoords = new ArrayList<>();
        vertices = new ArrayList<>();
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

    public int getPrimitive() {
        return primitive;
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
