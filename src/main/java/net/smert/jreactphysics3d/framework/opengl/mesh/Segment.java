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
package net.smert.jreactphysics3d.framework.opengl.mesh;

import java.util.ArrayList;
import java.util.List;
import net.smert.jreactphysics3d.framework.math.Vector3f;
import net.smert.jreactphysics3d.framework.math.Vector4f;
import net.smert.jreactphysics3d.framework.opengl.renderable.Renderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.gl1.DrawCommands;
import net.smert.jreactphysics3d.framework.utils.Color;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Segment {

    private int maxIndex;
    private int minIndex;
    private int primitiveMode;
    private DrawCommands drawCommands;
    private final List<Color> colors;
    private final List<Vector3f> normals;
    private final List<Vector3f> texCoords;
    private final List<Vector4f> vertices;
    private Material material;
    private String name;

    public Segment() {
        maxIndex = 0;
        minIndex = 0;
        drawCommands = null;
        primitiveMode = -1; // GL_POINTS = 0 :(
        colors = new ArrayList<>();
        normals = new ArrayList<>();
        texCoords = new ArrayList<>();
        vertices = new ArrayList<>();
        name = "";
    }

    public Segment(DrawCommands drawCommands) {
        this();
        this.drawCommands = drawCommands;
    }

    public Segment(int primitiveMode) {
        this();
        this.primitiveMode = primitiveMode;
    }

    public void addColor(float r, float g, float b) {
        colors.add(new Color(r, g, b, 1.0f));
    }

    public void addColor(float r, float g, float b, float a) {
        colors.add(new Color(r, g, b, a));
    }

    public void addColor(Color color) {
        colors.add(color);
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

    public void setPrimitiveMode(int primitiveMode) {
        this.primitiveMode = primitiveMode;
    }

    public DrawCommands getDrawCommands() {
        return (drawCommands == null) ? Renderable.drawCommandsConversion : drawCommands;
    }

    public void setDrawCommands(DrawCommands drawCommands) {
        if (drawCommands == null) {
            throw new IllegalArgumentException("Draw commands cannot be null");
        }
        this.drawCommands = drawCommands;
    }

    public List<Color> getColors() {
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

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasDrawCommands() {
        return (drawCommands != null);
    }

    public void setAllColors(float r, float g, float b) {
        colors.clear();
        while (colors.size() != vertices.size()) {
            colors.add(new Color(r, g, b, 1.0f));
        }
    }

    public void setAllColors(float r, float g, float b, float a) {
        colors.clear();
        while (colors.size() != vertices.size()) {
            colors.add(new Color(r, g, b, a));
        }
    }

    public void setAllColors(Color color) {
        colors.clear();
        while (colors.size() != vertices.size()) {
            colors.add(color);
        }
    }

}
