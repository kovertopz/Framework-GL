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
package net.smert.frameworkgl.gui.factory;

import net.smert.frameworkgl.gui.InputSystem;
import net.smert.frameworkgl.gui.RenderDevice;
import net.smert.frameworkgl.gui.SoundDevice;
import net.smert.frameworkgl.gui.TimeProvider;
import net.smert.frameworkgl.gui.render.GLCompatibility;
import net.smert.frameworkgl.gui.render.GLCore;
import net.smert.frameworkgl.gui.render.factory.BufferFactory;
import net.smert.frameworkgl.gui.render.factory.ImageFactory;
import net.smert.frameworkgl.gui.render.factory.MouseCursorFactory;
import org.picocontainer.MutablePicoContainer;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class GUIFactory {

    private final MutablePicoContainer container;

    public GUIFactory(MutablePicoContainer guiFactoryContainer) {
        container = guiFactoryContainer;
    }

    public BufferFactory createBufferFactory() {
        return container.getComponent(BufferFactory.class);
    }

    public GLCompatibility createGLCompatibility() {
        return container.getComponent(GLCompatibility.class);
    }

    public GLCore createGLCore() {
        return container.getComponent(GLCore.class);
    }

    public ImageFactory createImageFactory() {
        return container.getComponent(ImageFactory.class);
    }

    public InputSystem createInputSystem() {
        return container.getComponent(InputSystem.class);
    }

    public MouseCursorFactory createMouseCursorFactory() {
        return container.getComponent(MouseCursorFactory.class);
    }

    public RenderDevice createRenderDevice() {
        return container.getComponent(RenderDevice.class);
    }

    public SoundDevice createSoundDevice() {
        return container.getComponent(SoundDevice.class);
    }

    public TimeProvider createTimeProvider() {
        return container.getComponent(TimeProvider.class);
    }

}
