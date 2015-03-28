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
package net.smert.frameworkgl.opengl.renderable.shared;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.smert.frameworkgl.utils.HashMapIntGeneric;
import net.smert.frameworkgl.utils.HashMapStringInt;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 * @param <T>
 */
public class ObjectPool<T> {

    private int currentUniqueID;
    private final HashMapIntGeneric<T> uniqueIDToObject;
    private final HashMapStringInt nameToUniqueID;
    private final Map<String, T> nameToObject;
    private ObjectDestroyer<T> objectDestroyer;

    public ObjectPool() {
        currentUniqueID = 1;
        uniqueIDToObject = new HashMapIntGeneric<>();
        nameToUniqueID = new HashMapStringInt();
        nameToObject = new HashMap<>();
    }

    private ObjectDestroyer<T> getObjectDestroyer() {
        ObjectDestroyer<T> od = objectDestroyer;
        return od != null ? od : (objectDestroyer = createObjectDestroyer());
    }

    protected ObjectDestroyer<T> createObjectDestroyer() {
        return new ObjectDestroyer<>();
    }

    public void add(String name, T object) {
        if (nameToObject.containsKey(name)) {
            throw new IllegalArgumentException("Tried to add a " + object.getClass().getSimpleName()
                    + " that already exists to the pool: " + name);
        }
        int uniqueID = currentUniqueID++;
        assert (uniqueID < Integer.MAX_VALUE); // Unique IDs must be positive
        nameToUniqueID.put(name, uniqueID);
        nameToObject.put(name, object);
        uniqueIDToObject.put(uniqueID, object);
    }

    public void destroy() {
        // Don't reset currentUniqueID
        uniqueIDToObject.clear();
        nameToUniqueID.clear();
        ObjectDestroyer<T> od = getObjectDestroyer();
        Iterator<T> iterator = nameToObject.values().iterator();
        while (iterator.hasNext()) {
            T object = iterator.next();
            od.destroy(object);
        }
        nameToObject.clear();
    }

    public boolean exists(int uniqueID) {
        T object = uniqueIDToObject.get(uniqueID);
        return (object != null);
    }

    public boolean exists(String name) {
        T object = nameToObject.get(name);
        return (object != null);
    }

    public T get(int uniqueID) {
        T object = uniqueIDToObject.get(uniqueID);
        if (object == null) {
            throw new IllegalArgumentException("Tried to get a object that does not exist from the pool: " + uniqueID);
        }
        return object;
    }

    public T get(String name) {
        T object = nameToObject.get(name);
        if (object == null) {
            throw new IllegalArgumentException("Tried to get a object that does not exist from the pool: " + name);
        }
        return object;
    }

    public int getUniqueID(String name) {
        int uniqueID = nameToUniqueID.get(name);
        if (uniqueID != HashMapStringInt.NOT_FOUND) {
            return uniqueID;
        }
        return -1;
    }

    public T remove(String name) {
        T existingObject = nameToObject.remove(name);
        if (existingObject != null) {
            int uniqueID = nameToUniqueID.remove(name);
            uniqueIDToObject.remove(uniqueID);
        }
        return existingObject;
    }

    public static class ObjectDestroyer<T> {

        public void destroy(T object) {
        }

    }

}
