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
