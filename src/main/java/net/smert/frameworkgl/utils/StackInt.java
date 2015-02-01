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
package net.smert.frameworkgl.utils;

import java.util.EmptyStackException;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class StackInt {

    private int capacity;
    private int size;
    private int[] elements;

    public StackInt() {
        capacity = 16;
        size = 0;
        elements = new int[capacity];
    }

    public void clear() {
        size = 0;
    }

    public boolean empty() {
        return size == 0;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;

        // Create new array with capacity
        int[] newElements = new int[capacity];
        if (size > 0) {
            int minLength = Math.min(size, capacity);
            System.arraycopy(elements, 0, newElements, 0, minLength);
        }
        elements = newElements;
    }

    public int getSize() {
        return size;
    }

    public int peek() {
        if (size == 0) {
            throw new EmptyStackException();
        }
        return elements[size - 1];
    }

    public int pop() {
        int value = peek();
        size--;
        return value;
    }

    public void push(int value) {

        // Expand array
        if (size == capacity) {
            capacity *= 2;
            int[] newElements = new int[capacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }

        // Push value
        elements[size++] = value;
    }

}
