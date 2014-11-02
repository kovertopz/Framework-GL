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
package net.smert.frameworkgl.openal.factory;

import net.smert.frameworkgl.openal.OpenALBuffer;
import net.smert.frameworkgl.openal.OpenALListener;
import net.smert.frameworkgl.openal.OpenALSource;
import org.picocontainer.MutablePicoContainer;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class ALFactory {

    private final MutablePicoContainer container;

    public ALFactory(MutablePicoContainer alFactoryContainer) {
        container = alFactoryContainer;
    }

    public OpenALBuffer createOpenALBuffer() {
        return container.getComponent(OpenALBuffer.class);
    }

    public OpenALListener createOpenALListener() {
        return container.getComponent(OpenALListener.class);
    }

    public OpenALSource createOpenALSource() {
        return container.getComponent(OpenALSource.class);
    }

}
