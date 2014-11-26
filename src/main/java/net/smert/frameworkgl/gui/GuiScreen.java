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
package net.smert.frameworkgl.gui;

import net.smert.frameworkgl.gui.widgets.AbstractGuiWidget;
import net.smert.frameworkgl.gui.widgets.GuiRoot;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class GuiScreen {

    private GuiRoot root;

    public GuiScreen() {
    }

    public GuiRoot getRoot() {
        return root;
    }

    public void setRoot(GuiRoot root) {
        this.root = root;
    }

    public void addChild(AbstractGuiWidget widget) {
        root.addChild(widget);
    }

    public boolean removeChild(AbstractGuiWidget widget) {
        return root.removeChild(widget);
    }

    public void render() {
    }

    public void update() {
    }

}
