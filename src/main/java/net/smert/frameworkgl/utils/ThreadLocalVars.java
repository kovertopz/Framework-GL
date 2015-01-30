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

import net.smert.frameworkgl.math.AABB;
import net.smert.frameworkgl.math.Matrix3f;
import net.smert.frameworkgl.math.Matrix4f;
import net.smert.frameworkgl.math.Transform4f;
import net.smert.frameworkgl.math.Vector2f;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.math.Vector4f;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class ThreadLocalVars {

    private static final int STACK_SIZE = 5;
    private static final ThreadLocal<LocalStack> localStack;

    private boolean isUsed = false;
    public final AABB aabb0 = new AABB();
    public final AABB aabb1 = new AABB();
    public final Matrix3f m3f0 = new Matrix3f();
    public final Matrix3f m3f1 = new Matrix3f();
    public final Matrix4f m4f0 = new Matrix4f();
    public final Matrix4f m4f1 = new Matrix4f();
    public final Transform4f t0 = new Transform4f();
    public final Transform4f t1 = new Transform4f();
    public final Vector2f v2f0 = new Vector2f();
    public final Vector2f v2f1 = new Vector2f();
    public final Vector3f v3f0 = new Vector3f();
    public final Vector3f v3f1 = new Vector3f();
    public final Vector3f v3f2 = new Vector3f();
    public final Vector3f v3f3 = new Vector3f();
    public final Vector3f v3f4 = new Vector3f();
    public final Vector3f v3f5 = new Vector3f();
    public final Vector3f v3f6 = new Vector3f();
    public final Vector3f v3f7 = new Vector3f();
    public final Vector3f v3f8 = new Vector3f();
    public final Vector3f v3f9 = new Vector3f();
    public final Vector4f v4f0 = new Vector4f();
    public final Vector4f v4f1 = new Vector4f();

    private ThreadLocalVars() {
    }

    public void release() {
        if (!isUsed) {
            throw new IllegalStateException("This instance of ThreadLocalVars was already released");
        }

        // Free the instance
        isUsed = false;

        // Get stack from thread local storage
        LocalStack stack = localStack.get();

        // Return it to the stack
        stack.index--;

        // Make sure our instance matches where it's in the stack
        if (stack.vars[stack.index] != this) {
            throw new IllegalStateException("Another instance of ThreadLocalVars has been allocated but not released");
        }
    }

    public static ThreadLocalVars Get() {

        // Get stack from thread local storage
        LocalStack stack = localStack.get();
        ThreadLocalVars instance = stack.vars[stack.index];

        if (instance == null) {

            // Create new instance
            instance = new ThreadLocalVars();

            // Save instance
            stack.vars[stack.index] = instance;
        }

        // Increment stack and mark it in use
        stack.index++;
        instance.isUsed = true;

        return instance;
    }

    private static class LocalStack {

        int index = 0;
        ThreadLocalVars[] vars = new ThreadLocalVars[STACK_SIZE];

    }

    // Use static block to initialize class
    static {
        localStack = new ThreadLocal<LocalStack>() {

            @Override
            public LocalStack initialValue() {
                return new LocalStack();
            }

        };
    }

}
