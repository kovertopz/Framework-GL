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

import java.io.IOException;
import java.io.InputStream;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import net.smert.frameworkgl.Fw;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class GuiXmlSchema {

    private GuiXmlElement root;
    private final Map<String, String> defaultAttributes;
    private final Stack<GuiXmlElement> stack;
    private XmlPullParser xmlPullParser;

    public GuiXmlSchema() {
        defaultAttributes = new HashMap<>();
        stack = new Stack<>();
    }

    private void parseAttributeNodeList(NodeList attributeNodeList) {
        for (int i = 0; i < attributeNodeList.getLength(); i++) {
            Element element = (Element) attributeNodeList.item(i);
            if (element.hasAttributes()) {
                String defaultValue = element.getAttribute("default");
                String name = element.getAttribute("name");
                if (defaultValue.length() > 0) {
                    defaultAttributes.put(name, defaultValue);
                }
            }
        }
    }

    public GuiXmlElement getRoot() {
        return root;
    }

    public Map<String, String> getDefaultAttributes() {
        return defaultAttributes;
    }

    public void init() {
        try {
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            xmlPullParserFactory.setNamespaceAware(true);
            xmlPullParserFactory.setValidating(false);
            xmlPullParser = xmlPullParserFactory.newPullParser();
        } catch (XmlPullParserException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void load(InputStream isXml) throws IOException {
        try {
            xmlPullParser.setInput(isXml, null);
            int eventType;
            while ((eventType = xmlPullParser.next()) != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.END_TAG) {
                    stack.pop();
                } else if (eventType == XmlPullParser.START_TAG) {
                    GuiXmlElement guiXmlElement = Fw.guiFactory.createGuiXmlElement();
                    String elementType = xmlPullParser.getName();
                    guiXmlElement.setElementType(elementType);
                    if (root == null) {
                        root = guiXmlElement;
                    }
                    GuiXmlElement parentGuiXmlElement;
                    try {
                        parentGuiXmlElement = stack.peek();
                    } catch (EmptyStackException ex) {
                        parentGuiXmlElement = null;
                    }
                    if (parentGuiXmlElement != null) {
                        guiXmlElement.setParent(parentGuiXmlElement);
                        parentGuiXmlElement.addChild(guiXmlElement);
                    }
                    int attributeCount = xmlPullParser.getAttributeCount();
                    if (attributeCount > 0) {
                        guiXmlElement.loadAttributes(attributeCount, xmlPullParser);
                    }
                    stack.push(guiXmlElement);
                }
            }
        } catch (XmlPullParserException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void loadDefaultAttributesFromXsd(InputStream isSchema) throws IOException, ParserConfigurationException,
            SAXException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(isSchema);
        NodeList complexTypeNodeList = document.getElementsByTagName("xs:complexType");

        for (int i = 0; i < complexTypeNodeList.getLength(); i++) {
            Element element = (Element) complexTypeNodeList.item(i);
            if (element.hasAttributes()) {
                String name = element.getAttribute("name");
                if ("elementType".equals(name)) {
                    NodeList attributeNodeList = element.getElementsByTagName("xs:attribute");
                    parseAttributeNodeList(attributeNodeList);
                    break;
                }
            }
        }
    }

    public void printChildren(GuiXmlElement current) {
        if (current != null) {
            List<GuiXmlElement> children = current.getChildren();
            System.out.println("Element " + current.getElementType() + " has " + children.size() + " children.");
            for (int i = 0; i < children.size(); i++) {
                System.out.println("Child #" + i + " of parent " + current.getElementType());
                GuiXmlElement child = children.get(i);
                printChildren(child);
            }
        }
    }

    public void validate(InputStream isSchema, InputStream isXml) throws IOException, ParserConfigurationException,
            SAXException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilderFactory.setValidating(false);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(isXml);
        DOMSource domSource = new DOMSource(document);
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Source schemaSource = new StreamSource(isSchema);
        Schema schema = schemaFactory.newSchema(schemaSource);
        Validator validator = schema.newValidator();
        validator.validate(domSource);
    }

}
