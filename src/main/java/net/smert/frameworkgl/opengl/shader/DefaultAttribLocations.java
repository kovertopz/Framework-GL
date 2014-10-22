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
package net.smert.frameworkgl.opengl.shader;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class DefaultAttribLocations {

    private final Map<Integer, String> indexToAttributeLocation;
    private final Map<String, Integer> nameToIndex;

    public DefaultAttribLocations() {
        indexToAttributeLocation = new HashMap<>();
        nameToIndex = new HashMap<>();
        reset();
    }

    public int getIndex(String name) {
        return nameToIndex.get(name);
    }

    public void setIndex(String name, int index) {
        nameToIndex.put(name, index);
    }

    public Map<Integer, String> getAttribLocations() {
        return indexToAttributeLocation;
    }

    public String getAttribLocation(int index) {
        return indexToAttributeLocation.get(index);
    }

    public void setAttribLocations(int index, String name) {
        indexToAttributeLocation.put(index, name);
    }

    public final void reset() {

        // Indexes
        nameToIndex.clear();
        nameToIndex.put("vertex", 0);
        // 1 Unused
        nameToIndex.put("normal", 2);
        nameToIndex.put("color", 3);
        nameToIndex.put("color2", 4); // Could reuse
        // 5 gl_FogCoord
        // 6 Unused
        // 7 Unused
        nameToIndex.put("texCoord0", 8);
        nameToIndex.put("texCoord1", 9);
        nameToIndex.put("texCoord2", 10);
        nameToIndex.put("texCoord3", 11);
        nameToIndex.put("texCoord4", 12); // Could reuse
        nameToIndex.put("texCoord5", 13); // Could reuse
        nameToIndex.put("binormal", 14);
        nameToIndex.put("tangent", 15);

        // Attribute locations
        indexToAttributeLocation.clear();
        indexToAttributeLocation.put(nameToIndex.get("vertex"), "in_Vertex");
        indexToAttributeLocation.put(nameToIndex.get("normal"), "in_Normal");
        indexToAttributeLocation.put(nameToIndex.get("color"), "in_Color");
        indexToAttributeLocation.put(nameToIndex.get("color2"), "in_Color2");
        indexToAttributeLocation.put(nameToIndex.get("texCoord0"), "in_TexCoord0");
        indexToAttributeLocation.put(nameToIndex.get("texCoord1"), "in_TexCoord1");
        indexToAttributeLocation.put(nameToIndex.get("texCoord2"), "in_TexCoord2");
        indexToAttributeLocation.put(nameToIndex.get("texCoord3"), "in_TexCoord3");
        indexToAttributeLocation.put(nameToIndex.get("texCoord4"), "in_TexCoord4");
        indexToAttributeLocation.put(nameToIndex.get("texCoord5"), "in_TexCoord5");
        indexToAttributeLocation.put(nameToIndex.get("binormal"), "in_Binormal");
        indexToAttributeLocation.put(nameToIndex.get("tangent"), "in_Tangent");
    }

}
