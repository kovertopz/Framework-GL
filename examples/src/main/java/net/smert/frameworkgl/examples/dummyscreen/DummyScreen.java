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
package net.smert.frameworkgl.examples.dummyscreen;

import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.Screen;
import net.smert.frameworkgl.utils.FpsTimer;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class DummyScreen extends Screen {

    private FpsTimer fpsTimer;

    public DummyScreen(String[] args) {
    }

    @Override
    public void destroy() {
        System.out.println("destroy");
        System.out.println(Fw.timer);
    }

    @Override
    public void init() {
        System.out.println("init");
        fpsTimer = new FpsTimer();
    }

    @Override
    public void pause() {
        System.out.println("pause");
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
    }

}
