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
package net.smert.frameworkgl.opengl.renderable.gl2;

import net.smert.frameworkgl.opengl.VertexArray;
import net.smert.frameworkgl.opengl.renderable.Renderable;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VertexArrayBindStrategyGL2 implements net.smert.frameworkgl.opengl.renderable.shared.VertexArrayBindStrategy {

    @Override
    public void bind(int renderableConfigID, VertexArray vaColor, VertexArray vaNormal, VertexArray vaTexCoord,
            VertexArray vaVertex) {

        // Must unbind any VBOs
        Renderable.bindState2.unbindVBO();

        // Switch the renderable configuration first
        Renderable.bindState2.switchRenderableConfiguration(renderableConfigID);

        // Bind each vertex array
        if (vaColor != null) {
            Renderable.bindState2.bindColor(vaColor.getByteBuffer());
        } else {
            Renderable.bindState2.bindColor(null);
        }
        if (vaNormal != null) {
            Renderable.bindState2.bindNormal(vaNormal.getByteBuffer());
        } else {
            Renderable.bindState2.bindNormal(null);
        }
        if (vaTexCoord != null) {
            Renderable.bindState2.bindTexCoord(vaTexCoord.getByteBuffer());
        } else {
            Renderable.bindState2.bindTexCoord(null);
        }
        if (vaVertex != null) {
            Renderable.bindState2.bindVertex(vaVertex.getByteBuffer());
        } else {
            Renderable.bindState2.bindVertex(null);
        }
    }

}
