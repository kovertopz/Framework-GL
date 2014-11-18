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

    private boolean validateXml;
    private String schemaFilename;

    public ClumsyGui() {
        validateXml = true;
        schemaFilename = "xsd/clumsyGui.xsd";
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

        GuiXmlSchema guiXmlSchema = Fw.guiFactory.createGuiXmlSchema();
        guiXmlSchema.init();
        if (validateXml) {
            try (InputStream isSchema = schemaFileAsset.openStream();
                    InputStream isXml = fileAsset.openStream()) {
                guiXmlSchema.validate(isSchema, isXml);
            } catch (ParserConfigurationException | SAXException ex) {
                throw new RuntimeException(ex);
            }
        }
        try (InputStream isXml = fileAsset.openStream()) {
            guiXmlSchema.load(isXml);
        }
        try (InputStream isSchema = schemaFileAsset.openStream()) {
            guiXmlSchema.loadDefaultAttributesFromXsd(isSchema);
        } catch (ParserConfigurationException | SAXException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void render() {
    }

    public void switchScreen(String screenID) {
    }

    public void update() {
    }

}
