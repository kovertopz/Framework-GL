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
    private final Map<String, T> objectPool;
    private final HashMapIntGeneric<T> uniqueIDToObjectPool;
    private final HashMapStringInt filenameToUniqueID;
    private ObjectDestroyer<T> objectDestroyer;

    public ObjectPool() {
        currentUniqueID = 1;
        objectPool = new HashMap<>();
        uniqueIDToObjectPool = new HashMapIntGeneric<>();
        filenameToUniqueID = new HashMapStringInt();
    }

    private ObjectDestroyer<T> getObjectDestroyer() {
        ObjectDestroyer<T> od = objectDestroyer;
        return od != null ? od : (objectDestroyer = createObjectDestroyer());
    }

    protected ObjectDestroyer<T> createObjectDestroyer() {
        return new ObjectDestroyer<>();
    }

    public void add(String filename, T object) {
        if (objectPool.containsKey(filename)) {
            throw new IllegalArgumentException("Tried to add a " + object.getClass().getSimpleName()
                    + " that already exists to the pool: " + filename);
        }
        int uniqueID = currentUniqueID++;
        assert (uniqueID < Integer.MAX_VALUE); // Unique IDs must be positive
        filenameToUniqueID.put(filename, uniqueID);
        objectPool.put(filename, object);
        uniqueIDToObjectPool.put(uniqueID, object);
    }

    public void destroy() {
        ObjectDestroyer<T> od = getObjectDestroyer();
        Iterator<T> iterator = objectPool.values().iterator();
        while (iterator.hasNext()) {
            T object = iterator.next();
            od.destroy(object);
        }
        objectPool.clear();
    }

    public T get(int uniqueID) {
        T object = uniqueIDToObjectPool.get(uniqueID);
        if (object == null) {
            throw new IllegalArgumentException("Tried to get a object that does not exist from the pool: " + uniqueID);
        }
        return object;
    }

    public T get(String filename) {
        T object = objectPool.get(filename);
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

    public void remove(String filename) {
        if (objectPool.remove(filename) == null) {
            throw new IllegalArgumentException("Tried to remove a object that does not exist from the pool: " + filename);
        }
        int uniqueID = filenameToUniqueID.remove(filename);
        uniqueIDToObjectPool.remove(uniqueID);
    }

    public static class ObjectDestroyer<T> {

        public void destroy(T object) {
        }

    }

}
