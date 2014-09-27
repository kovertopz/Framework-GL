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
package net.smert.jreactphysics3d.framework.opengl.constants;

import org.lwjgl.opengl.GL15;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VertexBufferObjectTypes {

    public final static int DYNAMIC_DRAW = GL15.GL_DYNAMIC_DRAW;
    public final static int STATIC_DRAW = GL15.GL_STATIC_DRAW;
    public final static int STREAM_DRAW = GL15.GL_STREAM_DRAW;

    private VertexBufferObjectTypes() {
    }

}
