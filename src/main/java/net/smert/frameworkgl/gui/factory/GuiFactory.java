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

import net.smert.frameworkgl.gui.GuiScreen;
import net.smert.frameworkgl.gui.GuiXmlElement;
import net.smert.frameworkgl.gui.GuiXmlSchema;
import net.smert.frameworkgl.gui.widgets.GuiImage;
import net.smert.frameworkgl.gui.widgets.GuiLayer;
import net.smert.frameworkgl.gui.widgets.GuiPanel;
import net.smert.frameworkgl.gui.widgets.GuiRoot;
import net.smert.frameworkgl.gui.widgets.GuiScrollBar;
import net.smert.frameworkgl.gui.widgets.GuiScrollContainer;
import net.smert.frameworkgl.gui.widgets.GuiText;
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

    public GuiScreen createScreen() {
        return container.getComponent(GuiScreen.class);
    }

    public GuiImage createWidgetImage() {
        return container.getComponent(GuiImage.class);
    }

    public GuiLayer createWidgetLayer() {
        return container.getComponent(GuiLayer.class);
    }

    public GuiPanel createWidgetPanel() {
        return container.getComponent(GuiPanel.class);
    }

    public GuiRoot createWidgetRoot() {
        return container.getComponent(GuiRoot.class);
    }

    public GuiScrollBar createWidgetScrollBar() {
        return container.getComponent(GuiScrollBar.class);
    }

    public GuiScrollContainer createWidgetScrollContainer() {
        return container.getComponent(GuiScrollContainer.class);
    }

    public GuiText createWidgetText() {
        return container.getComponent(GuiText.class);
    }

    public GuiXmlElement createXmlElement() {
        return container.getComponent(GuiXmlElement.class);
    }

    public GuiXmlSchema createXmlSchema() {
        return container.getComponent(GuiXmlSchema.class);
    }

}
