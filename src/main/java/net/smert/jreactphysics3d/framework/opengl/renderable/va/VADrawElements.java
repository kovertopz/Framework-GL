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
package net.smert.jreactphysics3d.framework.opengl.renderable.va;

import java.nio.ByteBuffer;
import net.smert.jreactphysics3d.framework.opengl.GL;
import net.smert.jreactphysics3d.framework.opengl.renderable.Renderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.shared.AbstractDrawCall;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VADrawElements extends AbstractDrawCall {

    private ByteBuffer vertexIndexBuffer;

    public ByteBuffer getVertexIndexBuffer() {
        return vertexIndexBuffer;
    }

    public void setVertexIndexBuffer(ByteBuffer vertexIndexBuffer) {
        this.vertexIndexBuffer = vertexIndexBuffer;
    }

    @Override
    public void render() {
        for (int i = 0; i < primitiveModes.length; i++) {
            Renderable.shaderBindState.bindShader(shaders[i]);
            Renderable.textureBindState.bindTextures(textureTypeMappings[i]);
            GL.vaHelper.drawElements(
                    primitiveModes[i], elementCounts[i], Renderable.config.getIndexType(), vertexIndexBuffer);
        }
    }

}
