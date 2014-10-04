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
package net.smert.jreactphysics3d.framework.opengl.renderable.shared;

import java.util.Iterator;
import net.smert.jreactphysics3d.framework.opengl.renderable.RenderableConfiguration;
import net.smert.jreactphysics3d.framework.utils.HashMapIntGeneric;
import net.smert.jreactphysics3d.framework.utils.HashMapIntGeneric.Entry;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class RenderableConfigurationPool {

    private int currentUniqueID;
    private final HashMapIntGeneric<RenderableConfiguration> uniqueIDToRenderableConfiguration;

    public RenderableConfigurationPool() {
        currentUniqueID = 1;
        uniqueIDToRenderableConfiguration = new HashMapIntGeneric<>();
    }

    public int add(RenderableConfiguration config) {
        if (uniqueIDToRenderableConfiguration.containsValue(config)) {
            throw new IllegalArgumentException(
                    "Tried to add a RenderableConfiguration that already exists to the pool");
        }
        int uniqueID = currentUniqueID++;
        assert (uniqueID < Integer.MAX_VALUE); // Unique IDs must be positive
        uniqueIDToRenderableConfiguration.put(uniqueID, config);
        return uniqueID;
    }

    public int getOrAdd(RenderableConfiguration config) {
        int uniqueID = getUniqueID(config);
        if (uniqueID == -1) {
            uniqueID = add(config);
        }
        return uniqueID;
    }

    public RenderableConfiguration get(int uniqueID) {
        RenderableConfiguration config = uniqueIDToRenderableConfiguration.get(uniqueID);
        if (config == null) {
            throw new IllegalArgumentException(
                    "Tried to get a RenderableConfiguration that does not exist from the pool: " + uniqueID);
        }
        return config;
    }

    public int getUniqueID(RenderableConfiguration config) {
        Iterator<Entry<RenderableConfiguration>> iterator = uniqueIDToRenderableConfiguration.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<RenderableConfiguration> entry = iterator.next();
            if (entry.getValue().equals(config)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public void remove(int uniqueID) {
        if (uniqueIDToRenderableConfiguration.remove(uniqueID) == null) {
            throw new IllegalArgumentException(
                    "Tried to remove a RenderableConfiguration that does not exist from the pool: " + uniqueID);
        }
    }

}
