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
package net.smert.frameworkgl.gui.builders;

import net.smert.frameworkgl.gui.GuiXmlElement;
import net.smert.frameworkgl.gui.GuiXmlElementType;
import net.smert.frameworkgl.gui.UI;
import net.smert.frameworkgl.gui.widgets.AbstractGuiControl;
import net.smert.frameworkgl.gui.widgets.GuiImage;
import net.smert.frameworkgl.gui.widgets.GuiPanel;
import net.smert.frameworkgl.gui.widgets.GuiText;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class GuiPanelBuilder {

    public GuiPanel create(GuiXmlElement panel) {
        GuiPanel guiPanel = UI.guiFactory.createWidgetPanel();
        GuiBuilder.BuildCommon(guiPanel, panel);

        // Create each child
        for (GuiXmlElement child : panel.getChildren()) {
            String elementType = child.getElementType();

            switch (elementType) {
                case GuiXmlElementType.CONTROL_TYPE:
                    AbstractGuiControl guiControl = UI.controlBuiler.create(child);
                    guiPanel.addChild(guiControl);
                    break;
                case GuiXmlElementType.IMAGE_TYPE:
                    GuiImage guiImage = UI.imageBuilder.create(child);
                    guiPanel.addChild(guiImage);
                    break;
                case GuiXmlElementType.PANEL_TYPE:
                    GuiPanel guiPanelChild = UI.panelBuilder.create(child);
                    guiPanel.addChild(guiPanelChild);
                    break;
                case GuiXmlElementType.TEXT_TYPE:
                    GuiText guiText = UI.textBuilder.create(child);
                    guiPanel.addChild(guiText);
                    break;

                default:
                    throw new IllegalArgumentException("Unknown element type: " + elementType);
            }
        }

        return guiPanel;
    }

}
