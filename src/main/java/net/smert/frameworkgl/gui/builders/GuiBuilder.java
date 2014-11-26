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

import java.util.Map;
import net.smert.frameworkgl.gui.GuiXmlElement;
import net.smert.frameworkgl.gui.GuiXmlElementAttribute;
import net.smert.frameworkgl.gui.widgets.AbstractGuiWidget;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class GuiBuilder {

    private static String RemovePixel(String string) {
        return string.replaceAll("px", "");
    }

    private static String[] SplitSpace(String string) {
        return string.split("\\s+");
    }

    public static void BuildCommon(AbstractGuiWidget widget, GuiXmlElement xmlElement) {
        Map<String, String> attributes = xmlElement.getAttributes();
        widget.setDefaultAttributes(attributes);

        String padding = attributes.get(GuiXmlElementAttribute.PADDING);
        if (padding != null) {

            // Ex: 5px 10px 15px 20px
            padding = RemovePixel(padding); // Ex: 5 10 15 20
            String[] split = SplitSpace(padding);
            assert (split.length == 4); // XSD validates this properly
            int top = Integer.parseInt(split[0]);
            int right = Integer.parseInt(split[1]);
            int bottom = Integer.parseInt(split[2]);
            int left = Integer.parseInt(split[3]);
            widget.setPaddingBottom(bottom);
            widget.setPaddingLeft(left);
            widget.setPaddingRight(right);
            widget.setPaddingTop(top);
        }

        String paddingBottom = attributes.get(GuiXmlElementAttribute.PADDING_BOTTOM);
        if (paddingBottom != null) {
            paddingBottom = RemovePixel(paddingBottom);
            int bottom = Integer.parseInt(paddingBottom);
            widget.setPaddingBottom(bottom);
        }
        String paddingLeft = attributes.get(GuiXmlElementAttribute.PADDING_LEFT);
        if (paddingLeft != null) {
            paddingLeft = RemovePixel(paddingLeft);
            int left = Integer.parseInt(paddingLeft);
            widget.setPaddingLeft(left);
        }
        String paddingRight = attributes.get(GuiXmlElementAttribute.PADDING_RIGHT);
        if (paddingRight != null) {
            paddingRight = RemovePixel(paddingRight);
            int right = Integer.parseInt(paddingRight);
            widget.setPaddingRight(right);
        }
        String paddingTop = attributes.get(GuiXmlElementAttribute.PADDING_TOP);
        if (paddingTop != null) {
            paddingTop = RemovePixel(paddingTop);
            int top = Integer.parseInt(paddingTop);
            widget.setPaddingTop(top);
        }

        String margin = attributes.get(GuiXmlElementAttribute.MARGIN);
        if (margin != null) {

            // Ex: 5px 10px 15px 20px
            margin = RemovePixel(margin); // Ex: 5 10 15 20
            String[] split = SplitSpace(margin);
            assert (split.length == 4); // XSD validates this properly
            int top = Integer.parseInt(split[0]);
            int right = Integer.parseInt(split[1]);
            int bottom = Integer.parseInt(split[2]);
            int left = Integer.parseInt(split[3]);
            widget.setMarginBottom(bottom);
            widget.setMarginLeft(left);
            widget.setMarginRight(right);
            widget.setMarginTop(top);
        }

        String marginBottom = attributes.get(GuiXmlElementAttribute.MARGIN_BOTTOM);
        if (marginBottom != null) {
            marginBottom = RemovePixel(marginBottom);
            int bottom = Integer.parseInt(marginBottom);
            widget.setMarginBottom(bottom);
        }
        String marginLeft = attributes.get(GuiXmlElementAttribute.MARGIN_LEFT);
        if (marginLeft != null) {
            marginLeft = RemovePixel(marginLeft);
            int left = Integer.parseInt(marginLeft);
            widget.setMarginLeft(left);
        }
        String marginRight = attributes.get(GuiXmlElementAttribute.MARGIN_RIGHT);
        if (marginRight != null) {
            marginRight = RemovePixel(marginRight);
            int right = Integer.parseInt(marginRight);
            widget.setMarginRight(right);
        }
        String marginTop = attributes.get(GuiXmlElementAttribute.MARGIN_TOP);
        if (marginTop != null) {
            marginTop = RemovePixel(marginTop);
            int top = Integer.parseInt(marginTop);
            widget.setMarginTop(top);
        }
    }

}
