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
package net.smert.jreactphysics3d.framework.utils;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class HashMapStringInt {

    private static int DEFAULT_INITIAL_CAPACITY = 16;
    private static int MAXIMUM_CAPACITY = 1 << 30;
    private static int NULL_BUCKET = 0;
    private static float DEFAULT_LOAD_FACTOR = 0.75f;
    public static int NOT_FOUND = -Integer.MAX_VALUE;

    private float loadFactor;
    private int modCount;
    private int size;
    private int threshold;
    private Entry[] table;
    private EntrySet entrySet;
    private KeySet keySet;

    public HashMapStringInt() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public HashMapStringInt(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public HashMapStringInt(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }

        if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }

        int capacity = 1;
        while (capacity < initialCapacity) {
            capacity <<= 1;
        }

        this.loadFactor = loadFactor;
        modCount = 0;
        size = 0;
        threshold = (int) Math.min(capacity * loadFactor, MAXIMUM_CAPACITY + 1);
        table = new Entry[capacity];
        entrySet = null;
        keySet = null;
    }

    private void addEntry(int hash, String key, int value, int bucketIndex) {
        if ((size >= threshold) && (table[bucketIndex] != null)) {
            resize(2 * table.length);
            bucketIndex = indexFor(hash, table.length);
        }
        Entry e = table[bucketIndex];
        table[bucketIndex] = new Entry(hash, key, value, e);
        size++;
    }

    private void copyTable(Entry[] newTable) {
        int newCapacity = newTable.length;
        for (Entry e : table) {
            while (e != null) {
                Entry next = e.next;
                int i = indexFor(e.hash, newCapacity);
                e.next = newTable[i];
                newTable[i] = e;
                e = next;
            }
        }
    }

    private Entry getEntry(String key) {
        if (size == 0) {
            return null;
        }
        int hash = hash(key);
        int i = indexFor(hash, table.length);
        for (Entry e = table[i]; e != null; e = e.next) {
            if (e.hash == hash && e.key.equals(key)) {
                return e;
            }
        }
        return null;
    }

    private int getForNullKey() {
        if (size == 0) {
            return NOT_FOUND;
        }
        for (Entry e = table[NULL_BUCKET]; e != null; e = e.next) {
            if (e.key == null) {
                return e.value;
            }
        }
        return NOT_FOUND;
    }

    private int hash(String k) {
        int h = k.hashCode();
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private Iterator< Entry> newEntryIterator() {
        return new EntryIterator();
    }

    private Iterator<String> newKeyIterator() {
        return new KeyIterator();
    }

    private Iterator<Integer> newValueIterator() {
        return new ValueIterator();
    }

    private int putForNullKey(int value) {
        for (Entry e = table[NULL_BUCKET]; e != null; e = e.next) {
            if (e.key == null) {
                int oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }

        modCount++;
        addEntry(0, null, value, 0);
        return NOT_FOUND;
    }

    private Entry removeEntryForKey(String key) {
        if (size == 0) {
            return null;
        }

        int hash = (key == null) ? 0 : hash(key);
        int i = indexFor(hash, table.length);
        Entry prev = table[i];
        Entry e = prev;

        while (e != null) {
            Entry next = e.next;
            if (e.hash == hash && e.key.equals(key)) {
                modCount++;
                size--;
                if (prev == e) {
                    table[i] = next;
                } else {
                    prev.next = next;
                }
                return e;
            }
            prev = e;
            e = next;
        }

        return null;
    }

    private Entry removeEntry(Entry entry) {
        if (size == 0) {
            return null;
        }

        String key = entry.getKey();
        int hash = (key == null) ? 0 : hash(key);
        int i = indexFor(hash, table.length);
        Entry prev = table[i];
        Entry e = prev;

        while (e != null) {
            Entry next = e.next;
            if (e.hash == hash && e.equals(entry)) {
                modCount++;
                size--;
                if (prev == e) {
                    table[i] = next;
                } else {
                    prev.next = next;
                }
                return e;
            }
            prev = e;
            e = next;
        }

        return null;
    }

    private void resize(int newCapacity) {
        if (table.length == MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        }

        Entry[] newTable = new Entry[newCapacity];
        copyTable(newTable);
        table = newTable;
        threshold = (int) Math.min(newCapacity * loadFactor, MAXIMUM_CAPACITY + 1);
    }

    public void clear() {
        modCount++;
        Arrays.fill(table, null);
        size = 0;
    }

    public boolean containsKey(String key) {
        return getEntry(key) != null;
    }

    public boolean containsValue(int value) {
        for (Entry e : table) {
            for (Entry ie = e; ie != null; ie = ie.next) {
                if (ie.value == value) {
                    return true;
                }
            }
        }
        return false;
    }

    public Set<Entry> entrySet() {
        Set<Entry> es = entrySet;
        return es != null ? es : (entrySet = new EntrySet());
    }

    public int get(String key) {
        if (key == null) {
            return getForNullKey();
        }
        Entry e = getEntry(key);

        return (e == null) ? NOT_FOUND : e.getValue();
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Set<String> keySet() {
        Set<String> ks = keySet;
        return (ks != null ? ks : (keySet = new KeySet()));
    }

    public int put(String key, int value) {
        if (key == null) {
            return putForNullKey(value);
        }

        int hash = hash(key);
        int i = indexFor(hash, table.length);
        for (Entry e = table[i]; e != null; e = e.next) {
            if (e.hash == hash && e.key.equals(key)) {
                int oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }

        modCount++;
        addEntry(hash, key, value, i);
        return NOT_FOUND;
    }

    public int remove(String key) {
        Entry e = removeEntryForKey(key);
        return (e == null) ? NOT_FOUND : e.value;
    }

    public int size() {
        return size;
    }

    public static class Entry {

        private int hash;
        private int value;
        private Entry next;
        private String key;

        public Entry(int h, String k, int v, Entry n) {
            hash = h;
            value = v;
            next = n;
            key = k;
        }

        public int getValue() {
            return value;
        }

        public String getKey() {
            return key;
        }

        public int setValue(int newValue) {
            int oldValue = value;
            value = newValue;
            return oldValue;
        }

        @Override
        public int hashCode() {
            int hc = 7;
            hc = 89 * hc + Objects.hashCode(this.key);
            hc = 89 * hc + this.value;
            return hc;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            Entry other = (Entry) obj;
            if (!Objects.equals(this.key, other.key)) {
                return false;
            }
            return this.value == other.value;
        }

        @Override
        public String toString() {
            return getKey() + "=" + getValue();
        }

    }

    private class EntryIterator extends HashIterator<Entry> {

        @Override
        public Entry next() {
            return nextEntry();
        }

    }

    private class EntrySet extends AbstractSet<Entry> {

        @Override
        public void clear() {
            HashMapStringInt.this.clear();
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Entry)) {
                return false;
            }
            Entry e = (Entry) o;
            Entry candidate = getEntry(e.getKey());
            return candidate != null && candidate.equals(e);
        }

        @Override
        public Iterator<Entry> iterator() {
            return newEntryIterator();
        }

        @Override
        public boolean remove(Object o) {
            if (!(o instanceof Entry)) {
                return false;
            }
            return removeEntry((Entry) o) != null;
        }

        @Override
        public int size() {
            return size;
        }

    }

    private abstract class HashIterator<E> implements Iterator<E> {

        private int expectedModCount;
        private int index;
        private Entry current;
        private Entry next;

        public HashIterator() {
            expectedModCount = modCount;
            if (size > 0) {
                while (index < table.length && (next = table[index++]) == null);
            }
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        public Entry nextEntry() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            Entry e = next;
            if (e == null) {
                throw new NoSuchElementException();
            }
            if ((next = e.next) == null) {
                while (index < table.length && (next = table[index++]) == null);
            }
            current = e;
            return e;
        }

        @Override
        public void remove() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (current == null) {
                throw new IllegalStateException();
            }
            String k = current.key;
            current = null;
            HashMapStringInt.this.removeEntryForKey(k);
            expectedModCount = modCount;
        }

    }

    private class KeyIterator extends HashIterator<String> {

        @Override
        public String next() {
            return nextEntry().getKey();
        }

    }

    private class KeySet extends AbstractSet<String> {

        @Override
        public void clear() {
            HashMapStringInt.this.clear();
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof String)) {
                return false;
            }
            return containsKey((String) o);
        }

        @Override
        public Iterator<String> iterator() {
            return newKeyIterator();
        }

        @Override
        public boolean remove(Object o) {
            if (!(o instanceof String)) {
                return false;
            }
            return HashMapStringInt.this.removeEntryForKey((String) o) != null;
        }

        @Override
        public int size() {
            return size;
        }

    }

    private class ValueIterator extends HashIterator<Integer> {

        @Override
        public Integer next() {
            return nextEntry().value;
        }

    }

}
