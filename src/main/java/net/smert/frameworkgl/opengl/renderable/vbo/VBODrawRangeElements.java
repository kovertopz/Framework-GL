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
package net.smert.frameworkgl.opengl.renderable.vbo;

import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.mesh.Segment;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderable.shared.AbstractRenderCall;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VBODrawRangeElements extends AbstractRenderCall {

    private int indexSize;
    private int indexType;

    public int getIndexSize() {
        return indexSize;
    }

    public void setIndexSize(int indexSize) {
        this.indexSize = indexSize;
    }

    public int getIndexType() {
        return indexType;
    }

    public void setIndexType(int indexType) {
        this.indexType = indexType;
    }

    @Override
    public void render() {
        for (int i = 0; i < segments.length; i++) {
            Segment segment = segments[i];
            int elementCount = segment.getElementCount();
            int firstElement = segment.getMinIndex();
            int maxIndex = segment.getMaxIndex();
            int minIndex = segment.getMinIndex();
            int primitiveMode = segment.getPrimitiveMode();
            Renderable.colorState.changeColor(segment);
            Renderable.textureBindState.bindTextures(segment);
            Renderable.shaderBindState.sendUniformsOncePerRenderCall(segment);
            GL.vboHelper.drawRangeElements(primitiveMode, minIndex, maxIndex, elementCount, indexType, firstElement * indexSize);
        }
    }

}
