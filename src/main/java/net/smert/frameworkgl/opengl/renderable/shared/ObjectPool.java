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
    private final HashMapStringInt filenameToUniqueID;
    private final Map<String, T> filenameToObject;
    private ObjectDestroyer<T> objectDestroyer;

    public ObjectPool() {
        currentUniqueID = 1;
        uniqueIDToObject = new HashMapIntGeneric<>();
        filenameToUniqueID = new HashMapStringInt();
        filenameToObject = new HashMap<>();
    }

    private ObjectDestroyer<T> getObjectDestroyer() {
        ObjectDestroyer<T> od = objectDestroyer;
        return od != null ? od : (objectDestroyer = createObjectDestroyer());
    }

    protected ObjectDestroyer<T> createObjectDestroyer() {
        return new ObjectDestroyer<>();
    }

    public void add(String filename, T object) {
        if (filenameToObject.containsKey(filename)) {
            throw new IllegalArgumentException("Tried to add a " + object.getClass().getSimpleName()
                    + " that already exists to the pool: " + filename);
        }
        int uniqueID = currentUniqueID++;
        assert (uniqueID < Integer.MAX_VALUE); // Unique IDs must be positive
        filenameToUniqueID.put(filename, uniqueID);
        filenameToObject.put(filename, object);
        uniqueIDToObject.put(uniqueID, object);
    }

    public void destroy() {
        // Don't reset currentUniqueID
        uniqueIDToObject.clear();
        filenameToUniqueID.clear();
        ObjectDestroyer<T> od = getObjectDestroyer();
        Iterator<T> iterator = filenameToObject.values().iterator();
        while (iterator.hasNext()) {
            T object = iterator.next();
            od.destroy(object);
        }
        filenameToObject.clear();
    }

    public T get(int uniqueID) {
        T object = uniqueIDToObject.get(uniqueID);
        if (object == null) {
            throw new IllegalArgumentException("Tried to get a object that does not exist from the pool: " + uniqueID);
        }
        return object;
    }

    public T get(String filename) {
        T object = filenameToObject.get(filename);
        if (object == null) {
            throw new IllegalArgumentException("Tried to get a object that does not exist from the pool: " + filename);
        }
        return object;
    }

    public int getUniqueID(String filename) {
        int uniqueID = filenameToUniqueID.get(filename);
        if (uniqueID != HashMapStringInt.NOT_FOUND) {
            return uniqueID;
        }
        return -1;
    }

    public T remove(String filename) {
        T existingObject = filenameToObject.remove(filename);
        if (existingObject != null) {
            int uniqueID = filenameToUniqueID.remove(filename);
            uniqueIDToObject.remove(uniqueID);
        }
        return existingObject;
    }

    public static class ObjectDestroyer<T> {

        public void destroy(T object) {
        }

    }

}
