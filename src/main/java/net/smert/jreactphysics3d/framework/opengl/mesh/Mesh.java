package net.smert.jreactphysics3d.framework.opengl.mesh;

import java.util.ArrayList;
import java.util.List;
import net.smert.jreactphysics3d.framework.opengl.renderable.gl1.DrawCommands;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Mesh {

    private boolean canRenderRanged;
    private boolean hasColors;
    private boolean hasIndexes;
    private boolean hasNormals;
    private boolean hasTexCoords;
    private boolean hasVertices;
    private int totalVertices;
    private DrawCommands drawCommands;
    private final List<Integer> firstIndexes;
    private final List<Integer> indexes;
    private final List<Segment> segments;

    public Mesh() {
        firstIndexes = new ArrayList<>();
        indexes = new ArrayList<>();
        segments = new ArrayList<>();
        reset();
    }

    public Mesh(DrawCommands drawCommands) {
        this();
        this.drawCommands = drawCommands;
    }

    public void addSegment(Segment segment) {
        if (segments.contains(segment)) {
            throw new IllegalArgumentException("The segment already exists");
        }
        firstIndexes.add(totalVertices);
        segments.add(segment);
        totalVertices += segment.getVertices().size();
        canRenderRanged |= ((segment.getMaxIndex() - segment.getMinIndex()) > 1);
        hasColors |= (segment.getColors().size() > 0);
        hasNormals |= (segment.getNormals().size() > 0);
        hasTexCoords |= (segment.getTexCoords().size() > 0);
        hasVertices |= (segment.getVertices().size() > 0);
        hasIndexes |= (getIndexes().size() > 0);
    }

    public boolean canRenderRanged() {
        return canRenderRanged;
    }

    public int getTotalSegments() {
        return segments.size();
    }

    public int getTotalVerticies() {
        return totalVertices;
    }

    public List<Integer> getIndexes() {
        return indexes;
    }

    public Segment getSegment(int index) {
        return segments.get(index);
    }

    public boolean hasColors() {
        return hasColors;
    }

    public boolean hasIndexes() {
        return hasIndexes;
    }

    public boolean hasNormals() {
        return hasNormals;
    }

    public boolean hasTexCoords() {
        return hasTexCoords;
    }

    public boolean hasVertices() {
        return hasVertices;
    }

    public DrawCommands getDrawCommands() {
        return drawCommands;
    }

    public void setDrawCommands(DrawCommands drawCommands) {
        if (drawCommands == null) {
            throw new IllegalArgumentException("Draw commands cannot be null");
        }
        this.drawCommands = drawCommands;
    }

    public void removeSegment(Segment segment) {
        int index = segments.indexOf(segment);
        if (index == -1) {
            throw new RuntimeException("The segment was not found");
        }
        firstIndexes.remove(index);
        segments.remove(segment);
        totalVertices -= segment.getVertices().size();
    }

    public final void reset() {
        canRenderRanged = false;
        hasColors = false;
        hasIndexes = false;
        hasNormals = false;
        hasTexCoords = false;
        hasVertices = false;
        totalVertices = 0;
        firstIndexes.clear();
        indexes.clear();
        segments.clear();
    }

}