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
import java.util.Iterator;
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
    private final Map<String, Map<String, String>> defaultAttributes;
    private final Map<String, String> defaultAttributesToLoad;
    private final Stack<GuiXmlElement> stack;
    private String rootElementType;
    private XmlPullParser xmlPullParser;

    public GuiXmlSchema() {
        defaultAttributes = new HashMap<>();
        defaultAttributesToLoad = new HashMap<>();
        stack = new Stack<>();

        defaultAttributesToLoad.put("element", "elementType");
        defaultAttributesToLoad.put("image", "imageType");
        defaultAttributesToLoad.put("screenElement", "screenElementType");
        defaultAttributesToLoad.put("text", "textType");
        setRootElementType("clumsyRoot");
    }

    private void parseAttributeNodeList(Map<String, String> attributes, NodeList attributeNodeList) {
        for (int i = 0; i < attributeNodeList.getLength(); i++) {
            Element element = (Element) attributeNodeList.item(i);

            // Skip if the tag has no attributes
            if (!element.hasAttributes()) {
                continue;
            }

            // Get the default value if the tag has one
            String defaultValue = element.getAttribute("default");
            String name = element.getAttribute("name");
            if (defaultValue.length() > 0) {
                attributes.put(name, defaultValue);
            }
        }
    }

    public void addDefaultAttributesToLoad(String key, String tag) {
        defaultAttributesToLoad.put(key, tag);
    }

    public void clearDefaultAttributesToLoad() {
        defaultAttributesToLoad.clear();
    }

    public GuiXmlElement getRoot() {
        return root;
    }

    public Map<String, Map<String, String>> getDefaultAttributes() {
        return defaultAttributes;
    }

    public String getRootElementType() {
        return rootElementType;
    }

    public final void setRootElementType(String rootElementType) {
        this.rootElementType = rootElementType;
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
                    GuiXmlElement guiXmlElement = UI.guiFactory.createXmlElement();

                    // Set element type
                    String elementType = xmlPullParser.getName();
                    guiXmlElement.setElementType(elementType);

                    // Set root element
                    if ((root == null) && (elementType.equals(rootElementType))) {
                        root = guiXmlElement;
                    }

                    // Get the parent element
                    GuiXmlElement parentGuiXmlElement;
                    try {
                        parentGuiXmlElement = stack.peek();
                    } catch (EmptyStackException ex) {
                        parentGuiXmlElement = null;
                    }

                    // Set the parent and add a child
                    if (parentGuiXmlElement != null) {
                        guiXmlElement.setParent(parentGuiXmlElement);
                        parentGuiXmlElement.addChild(guiXmlElement);
                    }

                    // Parse attributes for the tag
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

        if (root == null) {
            throw new RuntimeException("The root element was not found: " + rootElementType);
        }
    }

    public void loadDefaultAttributesFromXsd(InputStream isSchema) throws IOException, ParserConfigurationException,
            SAXException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(isSchema);
        NodeList complexTypeNodeList = document.getElementsByTagName("xs:complexType");

        Iterator<Map.Entry<String, String>> iterator = defaultAttributesToLoad.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String key = entry.getKey();
            String tag = entry.getValue();

            for (int i = 0; i < complexTypeNodeList.getLength(); i++) {
                Element element = (Element) complexTypeNodeList.item(i);

                // If the element has no attributes
                if (!element.hasAttributes()) {
                    continue;
                }

                // If the tag doesnt match
                String name = element.getAttribute("name");
                if (!tag.equals(name)) {
                    continue;
                }

                // Add a new hashmap for the key
                Map<String, String> attributes = new HashMap<>();
                defaultAttributes.put(key, attributes);

                // Find attributes with defaults
                NodeList attributeNodeList = element.getElementsByTagName("xs:attribute");
                parseAttributeNodeList(attributes, attributeNodeList);

                break;
            }
        }
    }

    public void printChildren(GuiXmlElement current, int depth) {

        // Create a string based on the depth
        StringBuilder sb = new StringBuilder();
        int count = depth;
        while (count > 0) {
            count--;
            sb.append("  ");
        }
        String indent = sb.toString();

        // Loop over all children and recurse
        List<GuiXmlElement> children = current.getChildren();
        Map<String, String> attributes = current.getAttributes();
        System.out.println(indent + "Element " + current.getElementType()
                + " has " + children.size() + " children and " + attributes.size() + " attributes.");
        for (GuiXmlElement child : children) {
            printChildren(child, depth + 1);
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
