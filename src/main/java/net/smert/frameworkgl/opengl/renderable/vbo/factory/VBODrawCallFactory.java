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
package net.smert.frameworkgl.opengl.renderable.vbo.factory;

import net.smert.frameworkgl.opengl.renderable.vbo.VBODrawArrays;
import net.smert.frameworkgl.opengl.renderable.vbo.VBODrawElements;
import net.smert.frameworkgl.opengl.renderable.vbo.VBODrawRangeElements;
import org.picocontainer.MutablePicoContainer;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VBODrawCallFactory {

    private final MutablePicoContainer container;

    public VBODrawCallFactory(MutablePicoContainer vboDrawCallFactoryContainer) {
        container = vboDrawCallFactoryContainer;
    }

    public VBODrawArrays createDrawArrays() {
        return container.getComponent(VBODrawArrays.class);
    }

    public VBODrawElements createDrawElements() {
        return container.getComponent(VBODrawElements.class);
    }

    public VBODrawRangeElements createDrawRangeElements() {
        return container.getComponent(VBODrawRangeElements.class);
    }

}
