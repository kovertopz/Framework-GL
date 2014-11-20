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

import net.smert.frameworkgl.gui.ClumsyGui;
import net.smert.frameworkgl.gui.GuiScreen;
import net.smert.frameworkgl.gui.GuiXmlElement;
import net.smert.frameworkgl.gui.GuiXmlSchema;
import org.picocontainer.MutablePicoContainer;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class GuiFactory {

    private final MutablePicoContainer container;

    public GuiFactory(MutablePicoContainer guiFactoryContainer) {
        container = guiFactoryContainer;
    }

    public ClumsyGui createClumsyGui() {
        return container.getComponent(ClumsyGui.class);
    }

    public GuiScreen createGuiScreen() {
        return container.getComponent(GuiScreen.class);
    }

    public GuiXmlElement createGuiXmlElement() {
        return container.getComponent(GuiXmlElement.class);
    }

    public GuiXmlSchema createGuiXmlSchema() {
        return container.getComponent(GuiXmlSchema.class);
    }

}
