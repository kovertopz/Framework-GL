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

import net.smert.frameworkgl.opengl.VertexBufferObject;
import net.smert.frameworkgl.opengl.VertexBufferObjectInterleaved;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public interface VertexBufferObjectBindStrategy {

    public void bind(int renderableConfigID, VertexBufferObject vboColor, VertexBufferObject vboNormal,
            VertexBufferObject vboTexCoord, VertexBufferObject vboVertex, VertexBufferObject vboVertexIndex);

    public void bindInterleaved(int renderableConfigID, boolean hasColors, boolean hasNormals, boolean hasTexCoords,
            boolean hasVertices, boolean hasIndexes, VertexBufferObjectInterleaved vboInterleaved,
            VertexBufferObject vboVertexIndex);

}
