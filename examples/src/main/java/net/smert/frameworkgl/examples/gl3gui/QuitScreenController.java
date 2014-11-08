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

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import net.smert.frameworkgl.Fw;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class QuitScreenController implements ScreenController {

    private Nifty nifty;
    private Screen screen;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        System.out.println("QuitScreenController bind");
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onStartScreen() {
        System.out.println("QuitScreenController onStartScreen");
        Fw.app.stopRunning(); // Stop the application
    }

    @Override
    public void onEndScreen() {
        System.out.println("QuitScreenController onEndScreen");
    }

}
