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
package net.smert.frameworkgl.opengl.mesh.dynamic;

import net.smert.frameworkgl.math.Vector2f;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.utils.Color;
import net.smert.frameworkgl.utils.HashMapIntGeneric;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class ConstructionInfo {

    boolean convertToTriangles;
    final HashMapIntGeneric<Color> colors;
    final HashMapIntGeneric<Object> customData;
    final Quality quality;
    final Vector2f texCoordMinMaxX;
    final Vector2f texCoordMinMaxY;
    final Vector2f texCoordMinMaxZ;
    final Vector3f localPosition;
    final Vector3f radius;
    final Vector3f size;

    public ConstructionInfo() {
        colors = new HashMapIntGeneric<>();
        customData = new HashMapIntGeneric<>();
        quality = new Quality();
        texCoordMinMaxX = new Vector2f();
        texCoordMinMaxY = new Vector2f();
        texCoordMinMaxZ = new Vector2f();
        localPosition = new Vector3f();
        radius = new Vector3f();
        size = new Vector3f();
        reset();
    }

    public boolean isConvertToTriangles() {
        return convertToTriangles;
    }

    public void setConvertToTriangles(boolean convertToTriangles) {
        this.convertToTriangles = convertToTriangles;
    }

    public Color getColor(int index) {
        Color color = colors.get(index);
        if (color == null) {
            color = new Color();
            colors.put(index, color);
        }
        return color;
    }

    public void setColor(int index, float r, float g, float b) {
        Color existingColor = getColor(index);
        existingColor.set(r, g, b, 1f);
    }

    public void setColor(int index, float r, float g, float b, float a) {
        Color existingColor = getColor(index);
        existingColor.set(r, g, b, a);
    }

    public void setColor(int index, Color color) {
        Color existingColor = getColor(index);
        existingColor.set(color);
    }

    public void setColor(int index, String colorName) {
        Color existingColor = getColor(index);
        existingColor.set(colorName);
    }

    public void setColorHex(int index, String hexCode) {
        Color existingColor = getColor(index);
        existingColor.setHex(hexCode);
    }

    public Object getCustomData(int index) {
        return customData.get(index);
    }

    public void setCustomData(int index, Object object) {
        customData.put(index, object);
    }

    public Quality getQuality() {
        return quality;
    }

    public void setQuality(int x, int y, int z) {
        quality.set(x, y, z);
    }

    public Vector2f getTexCoordMinMaxX() {
        return texCoordMinMaxX;
    }

    public void setTexCoordMinMaxX(float x, float y) {
        texCoordMinMaxX.set(x, y);
    }

    public Vector2f getTexCoordMinMaxY() {
        return texCoordMinMaxY;
    }

    public void setTexCoordMinMaxY(float x, float y) {
        texCoordMinMaxY.set(x, y);
    }

    public Vector2f getTexCoordMinMaxZ() {
        return texCoordMinMaxZ;
    }

    public void setTexCoordMinMaxZ(float x, float y) {
        texCoordMinMaxZ.set(x, y);
    }

    public Vector3f getLocalPosition() {
        return localPosition;
    }

    public void setLocalPosition(float x, float y, float z) {
        localPosition.set(x, y, z);
    }

    public Vector3f getRadius() {
        return radius;
    }

    public void setRadius(float x, float y, float z) {
        radius.set(x, y, z);
    }

    public Vector3f getSize() {
        return size;
    }

    public void setSize(float x, float y, float z) {
        size.set(x, y, z);
    }

    public final void reset() {
        convertToTriangles = true;
        colors.clear();
        colors.put(0, new Color());
        colors.put(1, new Color());
        colors.put(2, new Color());
        customData.clear();
        quality.set(1, 1, 1);
        texCoordMinMaxX.set(0.0f, 1.0f);
        texCoordMinMaxY.set(0.0f, 1.0f);
        texCoordMinMaxZ.set(0.0f, 1.0f);
        localPosition.zero();
        radius.set(1.0f, 1.0f, 1.0f);
        size.set(1.0f, 1.0f, 1.0f);
    }

}
