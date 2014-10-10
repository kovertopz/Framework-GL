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
package net.smert.frameworkgl.opengl.mesh;

import java.util.HashMap;
import java.util.Map;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.mesh.dynamic.AbstractDynamicMesh;
import net.smert.frameworkgl.opengl.mesh.dynamic.ConstructionInfo;
import net.smert.frameworkgl.utils.Color;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class DynamicMeshBuilder {

    private final ConstructionInfo constructionInfo;
    private final Map<String, AbstractDynamicMesh> dynamicMeshes;

    public DynamicMeshBuilder() {
        constructionInfo = new ConstructionInfo();
        dynamicMeshes = new HashMap<>();
    }

    public DynamicMeshBuilder append(String name) {
        AbstractDynamicMesh dynamicMesh = dynamicMeshes.get(name);
        if (dynamicMesh == null) {
            throw new IllegalArgumentException("The requested dynamic mesh: " + name + " does not exist");
        }
        dynamicMesh.create(false, constructionInfo, GL.tessellator);
        return this;
    }

    public DynamicMeshBuilder build(String name) {
        AbstractDynamicMesh dynamicMesh = dynamicMeshes.get(name);
        if (dynamicMesh == null) {
            throw new IllegalArgumentException("The requested dynamic mesh: " + name + " does not exist");
        }
        dynamicMesh.create(true, constructionInfo, GL.tessellator);
        return this;
    }

    public Mesh createMesh(boolean reset) {
        Mesh mesh = GL.meshFactory.createMesh();
        return createMesh(reset, mesh);
    }

    public Mesh createMesh(boolean reset, Mesh mesh) {
        Fw.graphics.createMesh(GL.tessellator, mesh);
        if (reset) {
            reset();
        }
        return mesh;
    }

    public Vector3f getSize() {
        return constructionInfo.getSize();
    }

    public AbstractDynamicMesh register(String name, AbstractDynamicMesh dynamicMesh) {
        return dynamicMeshes.put(name, dynamicMesh);
    }

    public final DynamicMeshBuilder reset() {
        constructionInfo.reset();
        GL.tessellator.reset();
        return this;
    }

    public DynamicMeshBuilder setConvertToTriangles(boolean convertToTriangles) {
        constructionInfo.setConvertToTriangles(convertToTriangles);
        return this;
    }

    public DynamicMeshBuilder setColor(int index, float r, float g, float b) {
        constructionInfo.setColor(index, r, g, b);
        return this;
    }

    public DynamicMeshBuilder setColor(int index, float r, float g, float b, float a) {
        constructionInfo.setColor(index, r, g, b, a);
        return this;
    }

    public DynamicMeshBuilder setColor(int index, Color color) {
        constructionInfo.setColor(index, color);
        return this;
    }

    public DynamicMeshBuilder setColor(int index, String colorName) {
        constructionInfo.setColor(index, colorName);
        return this;
    }

    public DynamicMeshBuilder setColorHex(int index, String hexCode) {
        constructionInfo.setColorHex(index, hexCode);
        return this;
    }

    public DynamicMeshBuilder setLocalPosition(float x, float y, float z) {
        constructionInfo.setLocalPosition(x, y, z);
        return this;
    }

    public DynamicMeshBuilder setQuality(int x, int y, int z) {
        constructionInfo.setQuality(x, y, z);
        return this;
    }

    public DynamicMeshBuilder setTexCoordMinMaxX(float x, float y) {
        constructionInfo.setTexCoordMinMaxX(x, y);
        return this;
    }

    public DynamicMeshBuilder setTexCoordMinMaxY(float x, float y) {
        constructionInfo.setTexCoordMinMaxY(x, y);
        return this;
    }

    public DynamicMeshBuilder setTexCoordMinMaxZ(float x, float y) {
        constructionInfo.setTexCoordMinMaxZ(x, y);
        return this;
    }

    public DynamicMeshBuilder setRadius(float x, float y, float z) {
        constructionInfo.setRadius(x, y, z);
        return this;
    }

    public DynamicMeshBuilder setSize(float x, float y, float z) {
        constructionInfo.setSize(x, y, z);
        return this;
    }

    public AbstractDynamicMesh unregister(String name) {
        return dynamicMeshes.remove(name);
    }

}
