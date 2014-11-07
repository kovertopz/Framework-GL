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
package net.smert.frameworkgl.examples.audiowav;

import java.io.IOException;
import java.net.URISyntaxException;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.Screen;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.utils.FpsTimer;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class AudioWav extends Screen {

    private int sourceID;
    private FpsTimer fpsTimer;

    public AudioWav(String[] args) {
    }

    @Override
    public void destroy() {
        System.out.println("destroy");
        System.out.println(Fw.timer);
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

        fpsTimer = new FpsTimer();
        Fw.audio.init();
        try {
            sourceID = Fw.audio.playMusic("237729__flathill__rain-and-thunder-4.wav", true);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        GL.o1.clear();
    }

    @Override
    public void pause() {
        System.out.println("pause");
        Fw.audio.pause(sourceID);
    }

    @Override
    public void render() {
        fpsTimer.update();
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("resize");
    }

    @Override
    public void resume() {
        System.out.println("resume");
        Fw.audio.resume(sourceID);
    }

}
