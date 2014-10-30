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

import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.RenderableConfiguration;
import net.smert.frameworkgl.opengl.renderable.shared.AbstractRenderCall;
import net.smert.frameworkgl.opengl.renderable.shared.AbstractRenderCallBuilder;
import net.smert.frameworkgl.opengl.renderable.vbo.factory.VBODrawCallFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VBODrawCallBuilder extends AbstractRenderCallBuilder {

    private boolean canRenderRanged;
    private final VBODrawCallFactory vboDrawCallFactory;

    public VBODrawCallBuilder(VBODrawCallFactory vboDrawCallFactory) {
        canRenderRanged = false;
        this.vboDrawCallFactory = vboDrawCallFactory;
    }

    public AbstractRenderCall createRenderCall(Mesh mesh, RenderableConfiguration config) {
        AbstractRenderCall renderCall;

        if (mesh.hasIndexes()) {
            if (canRenderRanged) {

                // Create concrete class and set specific data
                VBODrawRangeElements drawRangedElements = vboDrawCallFactory.createDrawRangeElements();
                drawRangedElements.setIndexType(config.getIndexType());

                // Make sure we set the abstract class
                renderCall = drawRangedElements;
            } else {

                // Create concrete class and set specific data
                VBODrawElements drawElements = vboDrawCallFactory.createDrawElements();
                drawElements.setIndexType(config.getIndexType());

                // Make sure we set the abstract class
                renderCall = drawElements;
            }
        } else {

            // Create concrete class
            VBODrawArrays drawArrays = vboDrawCallFactory.createDrawArrays();

            // Make sure we set the abstract class
            renderCall = drawArrays;
        }

        // Attach segments to the render call
        super.createRenderCall(mesh, renderCall);

        return renderCall;
    }

    public boolean isCanRenderRanged() {
        return canRenderRanged;
    }

    public void setCanRenderRanged(boolean canRenderRanged) {
        this.canRenderRanged = canRenderRanged;
    }

}
