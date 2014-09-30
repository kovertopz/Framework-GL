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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.smert.jreactphysics3d.framework.Fw;
import net.smert.jreactphysics3d.framework.utils.HashMapIntGeneric;
import net.smert.jreactphysics3d.framework.utils.HashMapStringInt;

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

        // Trim slashes
        filename = Fw.files.trimLeftSlashes(filename);

        if (objectPool.containsKey(filename)) {
            throw new IllegalArgumentException("Tried to add a " + object.getClass().getSimpleName()
                    + " that already exists to the pool: " + filename);
        }

        int uniqueID = currentUniqueID++;
        assert (uniqueID != HashMapStringInt.NOT_FOUND);
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

        // Trim slashes
        filename = Fw.files.trimLeftSlashes(filename);

        T object = objectPool.get(filename);
        if (object == null) {
            throw new IllegalArgumentException("Tried to get a object that does not exist from the pool: " + filename);
        }
        return object;
    }

    public int getUniqueID(String filename) {

        // Trim slashes
        filename = Fw.files.trimLeftSlashes(filename);

        int uniqueID = filenameToUniqueID.get(filename);
        if (uniqueID == HashMapStringInt.NOT_FOUND) {
            throw new IllegalArgumentException("Tried to get a object that does not exist from the pool: " + filename);
        }
        return uniqueID;
    }

    public void remove(String filename) {

        // Trim slashes
        filename = Fw.files.trimLeftSlashes(filename);

        if (objectPool.remove(filename) == null) {
            throw new IllegalArgumentException("Did not find an instance of the object: " + filename);
        }
    }

    public static class ObjectDestroyer<T> {

        public void destroy(T object) {
        }

    }

}
