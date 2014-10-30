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
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.RenderableConfiguration;
import net.smert.frameworkgl.opengl.renderable.shared.AbstractRenderCall;
import net.smert.frameworkgl.opengl.renderable.shared.AbstractRenderCallBuilder;
import net.smert.frameworkgl.opengl.renderable.va.factory.VADrawCallFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VADrawCallBuilder extends AbstractRenderCallBuilder {

    private final VADrawCallFactory vaDrawCallFactory;

    public VADrawCallBuilder(VADrawCallFactory vaDrawCallFactory) {
        this.vaDrawCallFactory = vaDrawCallFactory;
    }

    public AbstractRenderCall createRenderCall(Mesh mesh, RenderableConfiguration config, ByteBuffer vertexIndexBuffer) {
        AbstractRenderCall renderCall;

        if (mesh.hasIndexes()) {

            assert (vertexIndexBuffer != null);

            // Create concrete class and set specific data
            VADrawElements drawElements = vaDrawCallFactory.createDrawElements();
            drawElements.setIndexType(config.getIndexType());
            drawElements.setVertexIndexBuffer(vertexIndexBuffer);

            // Make sure we set the abstract class
            renderCall = drawElements;
        } else {

            // Create concrete class
            VADrawArrays drawArrays = vaDrawCallFactory.createDrawArrays();

            // Make sure we set the abstract class
            renderCall = drawArrays;
        }

        // Attach segments to the render call
        super.createRenderCall(mesh, renderCall);

        return renderCall;
    }

}
