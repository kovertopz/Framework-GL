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

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VertexBufferObjectNonInterleavedData {

    public int bufferUsage;
    public int renderableConfigID;
    public AbstractDrawCall drawCall;
    public VertexBufferObject vboColor;
    public VertexBufferObject vboNormal;
    public VertexBufferObject vboTexCoord;
    public VertexBufferObject vboVertex;
    public VertexBufferObject vboVertexIndex;

    public VertexBufferObjectNonInterleavedData() {
        bufferUsage = -1;
        renderableConfigID = -1;
        drawCall = null;
        vboColor = null;
        vboNormal = null;
        vboTexCoord = null;
        vboVertex = null;
        vboVertexIndex = null;
    }

    public void destroy() {
        if (vboColor != null) {
            vboColor.destroy();
            vboColor = null;
        }
        if (vboNormal != null) {
            vboNormal.destroy();
            vboNormal = null;
        }
        if (vboTexCoord != null) {
            vboTexCoord.destroy();
            vboTexCoord = null;
        }
        if (vboVertex != null) {
            vboVertex.destroy();
            vboVertex = null;
        }
        if (vboVertexIndex != null) {
            vboVertexIndex.destroy();
            vboVertexIndex = null;
        }
    }

}
