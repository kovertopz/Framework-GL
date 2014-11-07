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
package net.smert.frameworkgl.opengl.pipeline.factory;

import net.smert.frameworkgl.opengl.pipeline.DeferredLightingPipeline;
import net.smert.frameworkgl.opengl.pipeline.DeferredRenderingPipeline;
import net.smert.frameworkgl.opengl.pipeline.ForwardRenderingPipeline;
import org.picocontainer.MutablePicoContainer;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class RenderingPipelineFactory {

    private final MutablePicoContainer container;

    public RenderingPipelineFactory(MutablePicoContainer renderingPipelineFactoryContainer) {
        container = renderingPipelineFactoryContainer;
    }

    public DeferredLightingPipeline createDeferredLightingPipeline() {
        return container.getComponent(DeferredLightingPipeline.class);
    }

    public DeferredRenderingPipeline createDeferredRenderingPipeline() {
        return container.getComponent(DeferredRenderingPipeline.class);
    }

    public ForwardRenderingPipeline createForwardRenderingPipeline() {
        return container.getComponent(ForwardRenderingPipeline.class);
    }

}
