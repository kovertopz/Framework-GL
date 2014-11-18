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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class GuiXmlElement {

    private GuiXmlElement parent;
    private final List<GuiXmlElement> children;
    private final Map<String, String> attributes;
    private String elementType;

    public GuiXmlElement() {
        children = new ArrayList<>();
        attributes = new HashMap<>();
    }

    void addChild(GuiXmlElement child) {
        children.add(child);
    }

    void loadAttributes(int attributeCount, XmlPullParser xmlPullParser) {
        for (int i = 0; i < attributeCount; i++) {
            String name = xmlPullParser.getAttributeName(i);
            String value = xmlPullParser.getAttributeValue(i);
            attributes.put(name, value);
        }
    }

    void setParent(GuiXmlElement parent) {
        this.parent = parent;
    }

    void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public GuiXmlElement getParent() {
        return parent;
    }

    public List<GuiXmlElement> getChildren() {
        return children;
    }

    public String getElementType() {
        return elementType;
    }

}
