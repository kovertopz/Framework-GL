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
package net.smert.frameworkgl.opengl.renderable.va;

import java.nio.ByteBuffer;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.mesh.Segment;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderable.shared.AbstractRenderCall;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VADrawElements extends AbstractRenderCall {

    private int indexType;
    private ByteBuffer vertexIndexBuffer;

    public int getIndexType() {
        return indexType;
    }

    public void setIndexType(int indexType) {
        this.indexType = indexType;
    }

    public ByteBuffer getVertexIndexBuffer() {
        return vertexIndexBuffer;
    }

    public void setVertexIndexBuffer(ByteBuffer vertexIndexBuffer) {
        this.vertexIndexBuffer = vertexIndexBuffer;
    }

    @Override
    public void render() {
        for (int i = 0; i < segments.length; i++) {
            Segment segment = segments[i];
            int elementCount = segment.getElementCount();
            int primitiveMode = segment.getPrimitiveMode();
            Renderable.colorState.changeColor(segment);
            Renderable.textureBindState.bindTextures(segment);
            Renderable.shaderBindState.sendUniformsOncePerRenderCall(segment);
            GL.vaHelper.drawElements(primitiveMode, elementCount, vertexIndexBuffer);
        }
    }

}
