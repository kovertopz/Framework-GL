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
package net.smert.frameworkgl;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.render.batch.BatchRenderDevice;
import de.lessvoid.nifty.render.batch.core.BatchRenderBackendCoreProfileInternal;
import de.lessvoid.nifty.render.batch.spi.BatchRenderBackend;
import java.io.IOException;
import net.smert.frameworkgl.gui.InputSystem;
import net.smert.frameworkgl.gui.RenderDevice;
import net.smert.frameworkgl.gui.SoundDevice;
import net.smert.frameworkgl.gui.TimeProvider;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class GUI {

    private boolean clearScreen;
    private boolean initialized;
    private BatchRenderBackend batchRenderBackend;
    private BatchRenderDevice batchRenderDevice;
    private InputSystem inputSystem;
    private RenderDevice renderDevice;
    private SoundDevice soundDevice;
    private TimeProvider timeProvider;
    private Nifty nifty;

    public GUI() {
        clearScreen = false;
        initialized = false;
    }

    public void destroy() {
        if (nifty != null) {
            nifty.exit();
        }
    }

    public void init() {
        if (initialized) {
            return;
        }

        batchRenderBackend = new BatchRenderBackendCoreProfileInternal(
                Fw.guiFactory.createGLCore(),
                Fw.guiFactory.createBufferFactory(),
                Fw.guiFactory.createImageFactory(),
                Fw.guiFactory.createMouseCursorFactory());
        batchRenderDevice = new BatchRenderDevice(batchRenderBackend);
        inputSystem = Fw.guiFactory.createInputSystem();
        renderDevice = Fw.guiFactory.createRenderDevice();
        soundDevice = Fw.guiFactory.createSoundDevice();
        timeProvider = Fw.guiFactory.createTimeProvider();
        nifty = new Nifty(batchRenderDevice, soundDevice, inputSystem, timeProvider);
        initialized = true;
    }

    public void gotoScreen(String screenId) {
        nifty.gotoScreen(screenId);
    }

    public boolean isActive(String filename, String screenId) {
        return nifty.isActive(filename, screenId);
    }

    public boolean isClearScreen() {
        return clearScreen;
    }

    public void setClearScreen(boolean clearScreen) {
        this.clearScreen = clearScreen;
    }

    public void loadGuiFromXml(String filename, String startScreen) throws IOException {
        Files.FileAsset fileAsset = Fw.files.getGui(filename);
        nifty.fromXml(fileAsset.getFullPathToFile(), fileAsset.openStream(), startScreen);
    }

    public void render() {
        nifty.render(clearScreen);
    }

    public boolean update() {
        return nifty.update();
    }

}
