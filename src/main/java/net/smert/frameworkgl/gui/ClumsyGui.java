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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import net.smert.frameworkgl.Files;
import net.smert.frameworkgl.Fw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class ClumsyGui {

    private final static Logger log = LoggerFactory.getLogger(ClumsyGui.class);
    private final String ID_ATTRIBUTE = "id";
    private final String SCREEN_ELEMENT_TYPE = "screen";

    private boolean validateXml;
    private GuiScreen currentScreen;
    private final Map<String, Map<String, String>> defaultAttributes;
    private final Map<String, GuiScreen> screens;
    private String schemaFilename;

    public ClumsyGui() {
        validateXml = true;
        defaultAttributes = new HashMap<>();
        screens = new HashMap<>();
        schemaFilename = "xsd/clumsyGui.xsd";
    }

    private void createScreens(GuiXmlSchema guiXmlSchema) {

        // Make sure we had a root element from the schema
        GuiXmlElement root = guiXmlSchema.getRoot();
        if (root == null) {
            throw new RuntimeException("The XML file had no root element");
        }

        List<GuiXmlElement> children = root.getChildren();
        for (GuiXmlElement child : children) {
            String type = child.getElementType();

            // If we are not a screen element
            if (!SCREEN_ELEMENT_TYPE.equals(type)) {
                continue;
            }

            // Make sure the screen element has an ID attribute
            String screenID = child.getAttributes().get(ID_ATTRIBUTE);
            if (screenID == null) {
                throw new RuntimeException("XML element screen is missing the \"" + ID_ATTRIBUTE + "\" attribute");
            }

            // Create and initialize screen
            GuiScreen screen = UI.guiFactory.createGuiScreen();
            screen.init(child);

            // Save screen
            GuiScreen oldScreen = screens.put(screenID, screen);
            if (oldScreen != null) {
                log.warn("The screen with ID {} was overwritten", screenID);
            }
        }
    }

    private void updateDefaultAttributes(GuiXmlSchema guiXmlSchema) {

        // This is essentially what putAll does but we can't complain when we overwrite an entry
        Map<String, Map<String, String>> attributes = guiXmlSchema.getDefaultAttributes();
        for (Map.Entry<String, Map<String, String>> entry : attributes.entrySet()) {
            String key = entry.getKey();
            Map<String, String> value = entry.getValue();
            Map<String, String> oldValue = defaultAttributes.put(key, value);
            if (oldValue != null) {
                log.warn("The default attributes for {} was overwritten", key);
            }
        }
    }

    public boolean isValidateXml() {
        return validateXml;
    }

    public void setValidateXml(boolean validateXml) {
        this.validateXml = validateXml;
    }

    public String getSchemaFilename() {
        return schemaFilename;
    }

    public void setSchemaFilename(String schemaFilename) {
        this.schemaFilename = schemaFilename;
    }

    public void loadXml(String filename) throws IOException {
        log.info("Loading GUI XML file: {}", filename);
        Files.FileAsset fileAsset = Fw.files.getGui(filename);
        Files.FileAsset schemaFileAsset = Fw.files.getGui(schemaFilename);

        // Create schema
        GuiXmlSchema guiXmlSchema = UI.guiFactory.createGuiXmlSchema();
        guiXmlSchema.init();

        // Validate if necessary
        if (validateXml) {
            try (InputStream isSchema = schemaFileAsset.openStream();
                    InputStream isXml = fileAsset.openStream()) {
                guiXmlSchema.validate(isSchema, isXml);
            } catch (ParserConfigurationException | SAXException ex) {
                throw new RuntimeException(ex);
            }
        }

        // Parse the XML file
        try (InputStream isXml = fileAsset.openStream()) {
            guiXmlSchema.load(isXml);
        }

        // Extract default attributes from the XSD
        try (InputStream isSchema = schemaFileAsset.openStream()) {
            guiXmlSchema.loadDefaultAttributesFromXsd(isSchema);
        } catch (ParserConfigurationException | SAXException ex) {
            throw new RuntimeException(ex);
        }

        // Build everything from the XML schema
        updateDefaultAttributes(guiXmlSchema);
        createScreens(guiXmlSchema);
        guiXmlSchema.printChildren(guiXmlSchema.getRoot(), 0);
    }

    public void render() {
    }

    public void switchScreen(String screenID) {
        GuiScreen screen = screens.get(screenID);
        if (screen == null) {
            throw new RuntimeException("There is no screen defined for ID: " + screenID);
        }
        currentScreen = screen;
    }

    public void update() {
    }

}
