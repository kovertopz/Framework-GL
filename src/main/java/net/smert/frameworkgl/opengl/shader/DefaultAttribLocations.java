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

    private final Map<Integer, String> attribLocations;
    private final Map<String, Integer> indexes;

    public DefaultAttribLocations() {
        attribLocations = new HashMap<>();
        indexes = new HashMap<>();
        reset();
    }

    public int getIndex(String name) {
        return indexes.get(name);
    }

    public Map<Integer, String> getAttribLocations() {
        return attribLocations;
    }

    public String getAttribLocation(int index) {
        return attribLocations.get(index);
    }

    public final void reset() {

        // Indexes
        indexes.clear();
        indexes.put("vertex", 0);
        // 1 Unused
        indexes.put("normal", 2);
        indexes.put("color", 3);
        indexes.put("color2", 4); // Could reuse
        // 5 gl_FogCoord
        // 6 Unused
        // 7 Unused
        indexes.put("texCoord0", 8);
        indexes.put("texCoord1", 9);
        indexes.put("texCoord2", 10);
        indexes.put("texCoord3", 11);
        indexes.put("texCoord4", 12); // Could reuse
        indexes.put("texCoord5", 13); // Could reuse
        indexes.put("binormal", 14);
        indexes.put("tangent", 15);

        // Attribute locations
        attribLocations.clear();
        attribLocations.put(indexes.get("vertex"), "in_Vertex");
        attribLocations.put(indexes.get("normal"), "in_Normal");
        attribLocations.put(indexes.get("color"), "in_Color");
        attribLocations.put(indexes.get("color2"), "in_Color2");
        attribLocations.put(indexes.get("texCoord0"), "in_TexCoord0");
        attribLocations.put(indexes.get("texCoord1"), "in_TexCoord1");
        attribLocations.put(indexes.get("texCoord2"), "in_TexCoord2");
        attribLocations.put(indexes.get("texCoord3"), "in_TexCoord3");
        attribLocations.put(indexes.get("texCoord4"), "in_TexCoord4");
        attribLocations.put(indexes.get("texCoord5"), "in_TexCoord5");
        attribLocations.put(indexes.get("binormal"), "in_Binormal");
        attribLocations.put(indexes.get("tangent"), "in_Tangent");
    }

    public void setAttribLocations(int index, String name) {
        attribLocations.put(index, name);
    }

    public void setIndex(String name, int index) {
        indexes.put(name, index);
    }

}
