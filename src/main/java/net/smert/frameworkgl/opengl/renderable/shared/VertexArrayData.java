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

import net.smert.frameworkgl.opengl.VertexArray;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VertexArrayData {

    public int renderableConfigID;
    public AbstractDrawCall drawCall;
    public VertexArray vaColor;
    public VertexArray vaNormal;
    public VertexArray vaTexCoord;
    public VertexArray vaVertex;
    public VertexArray vaVertexIndex;

    public VertexArrayData() {
        renderableConfigID = -1;
        drawCall = null;
        vaColor = null;
        vaNormal = null;
        vaTexCoord = null;
        vaVertex = null;
        vaVertexIndex = null;
    }

    public void destroy() {
        if (vaColor != null) {
            vaColor.destroy();
            vaColor = null;
        }
        if (vaNormal != null) {
            vaNormal.destroy();
            vaNormal = null;
        }
        if (vaTexCoord != null) {
            vaTexCoord.destroy();
            vaTexCoord = null;
        }
        if (vaVertex != null) {
            vaVertex.destroy();
            vaVertex = null;
        }
        if (vaVertexIndex != null) {
            vaVertexIndex.destroy();
            vaVertexIndex = null;
        }
    }

}
