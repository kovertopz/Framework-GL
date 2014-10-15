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
package net.smert.frameworkgl.opengl.renderable.factory;

import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;
import org.picocontainer.MutablePicoContainer;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class RenderableFactoryGL3 implements RenderableFactory {

    private final MutablePicoContainer container;

    public RenderableFactoryGL3(MutablePicoContainer renderableFactoryGL3Container) {
        container = renderableFactoryGL3Container;
    }

    @Override
    public AbstractRenderable createArrayRenderable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AbstractRenderable createDynamicInterleavedRenderable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AbstractRenderable createDynamicNonInterleavedRenderable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AbstractRenderable createInterleavedRenderable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AbstractRenderable createNonInterleavedRenderable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
