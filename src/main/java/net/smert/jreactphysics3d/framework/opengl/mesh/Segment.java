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

import net.smert.jreactphysics3d.framework.opengl.renderable.Renderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.gl1.DrawCommands;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Segment {

    private int elementCount;
    private int maxIndex;
    private int minIndex;
    private int primitiveMode;
    private float[] colors;
    private float[] normals;
    private float[] texCoords;
    private float[] vertices;
    private DrawCommands drawCommands;
    private Material material;
    private String name;

    public Segment() {
        elementCount = 0;
        maxIndex = 0;
        minIndex = 0;
        primitiveMode = -1; // GL_POINTS = 0 :(
        colors = new float[0];
        normals = new float[0];
        texCoords = new float[0];
        vertices = new float[0];
        drawCommands = null;
        material = null;
        name = "";
    }

    public Segment(DrawCommands drawCommands) {
        this();
        this.drawCommands = drawCommands;
    }

    public float[] getColors() {
        return colors;
    }

    public void setColors(float[] colors) {
        this.colors = colors;
    }

    public float[] getNormals() {
        return normals;
    }

    public void setNormals(float[] normals) {
        this.normals = normals;
    }

    public float[] getTexCoords() {
        return texCoords;
    }

    public void setTexCoords(float[] texCoords) {
        this.texCoords = texCoords;
    }

    public float[] getVertices() {
        return vertices;
    }

    public void setVertices(float[] vertices) {
        this.vertices = vertices;
    }

    public int getElementCount() {
        return elementCount;
    }

    public void setElementCount(int elementCount) {
        this.elementCount = elementCount;
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

}
