/**
 * Copyright 2014 Jason Sorensen (sorensenj@smert.net)
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
package net.smert.frameworkgl.examples.gl3gui;

import java.io.IOException;
import java.net.URISyntaxException;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.Screen;
import net.smert.frameworkgl.gui.SimpleDebugGuiScreen;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.shader.basic.DiffuseTextureShader;
import net.smert.frameworkgl.utils.FpsTimer;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Gui extends Screen {

    private DiffuseTextureShader diffuseTextureShader;
    private FpsTimer fpsTimer;
    private SimpleDebugGuiScreen guiScreen;

    public Gui(String[] args) {
    }

    @Override
    public void destroy() {
        System.out.println("destroy");
        System.out.println(Fw.timer);
        diffuseTextureShader.destroy();
    }

    @Override
    public void init() {
        System.out.println("init");

        // Register assets
        try {
            Fw.files.registerAssets("/net/smert/frameworkgl/examples/assets", true);
        } catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }

        // Switch renderer and factory to OpenGL 3
        Fw.graphics.switchOpenGLVersion(3);

        // Initialize GUI
        Fw.gui.init();

        // Create timer
        fpsTimer = new FpsTimer();

        // Create GUI screen
        guiScreen = new SimpleDebugGuiScreen();
        guiScreen.init(Fw.graphics.getRenderer());

        // Switch GUI screen
        Fw.gui.setScreen(guiScreen);

        // Build shaders
        try {
            diffuseTextureShader = DiffuseTextureShader.Factory.Create();
            diffuseTextureShader.init();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        GL.o1.clear();
    }

    @Override
    public void pause() {
        System.out.println("pause");
    }

    @Override
    public void render() {
        fpsTimer.update();
        GL.o1.clear();
        GL.o1.setBlendingFunctionSrcAlphaAndOneMinusSrcAlpha();
        GL.o1.enableBlending();
        Fw.graphics.switchShader(diffuseTextureShader);
        Fw.graphics.set2DMode();
        Fw.gui.update();
        Fw.gui.render();
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("resize");
    }

    @Override
    public void resume() {
        System.out.println("resume");
    }

}
