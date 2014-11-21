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

import java.io.IOException;
import net.smert.frameworkgl.gui.ClumsyGui;
import net.smert.frameworkgl.gui.UI;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Gui {

    private boolean initialized;
    private ClumsyGui clumsyGui;

    public Gui() {
        initialized = false;
    }

    public boolean isValidateXml() {
        return clumsyGui.isValidateXml();
    }

    public void setValidateXml(boolean validateXml) {
        clumsyGui.setValidateXml(validateXml);
    }

    public String getSchemaFilename() {
        return clumsyGui.getSchemaFilename();
    }

    public void setSchemaFilename(String schemaFilename) {
        clumsyGui.setSchemaFilename(schemaFilename);
    }

    public void init() {
        if (initialized) {
            return;
        }

        // Initialize audio
        Fw.audio.init();

        // Initialize clumsy
        clumsyGui = UI.gui;
        initialized = true;
    }

    public void loadXml(String filename) throws IOException {
        clumsyGui.loadXml(filename);
    }

    public void loadXml(String filename, String startScreenID) throws IOException {
        loadXml(filename);
        switchScreen(startScreenID);
    }

    public void render() {
        clumsyGui.render();
    }

    public void switchScreen(String screenID) {
        clumsyGui.switchScreen(screenID);
    }

    public void update() {
        clumsyGui.update();
    }

}
