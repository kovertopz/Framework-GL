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
package net.smert.frameworkgl.opengl.shader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.smert.frameworkgl.Files;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.Shader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class ShaderBuilder {

    private final static Logger log = LoggerFactory.getLogger(ShaderBuilder.class);

    private final List<Integer> shaderIDs;
    private final Map<Integer, List<ShaderNameAndSource>> shaderTypeToShaderNameAndSources;
    private Shader shader;

    public ShaderBuilder() {
        shaderIDs = new ArrayList<>();
        shaderTypeToShaderNameAndSources = new HashMap<>();
        shader = null;
    }

    private int compileShader(String shaderName, String shaderSource, int shaderType) {
        int shaderID = GL.shaderHelper.createShader(shaderType);

        log.debug("Created a new shader with ID: {}", shaderID);

        if (shaderID == 0) {
            throw new RuntimeException("Unable to create shader: Name: " + shaderName + " Type: " + shaderType);
        }

        GL.shaderHelper.setSource(shaderID, shaderSource);
        GL.shaderHelper.compile(shaderID);

        if (!GL.shaderHelper.getCompileStatus(shaderID)) {
            log.error("Shader souce code error: Name: {} Info Log:\n{}",
                    shaderName, GL.shaderHelper.getInfoLog(shaderID));
            throw new RuntimeException("Shader \"" + shaderName + "\" had compile errors");
        }

        return shaderID;
    }

    private String read(String filename) throws IOException {
        Files.FileAsset fileAsset = Fw.files.getShader(filename);
        StringBuilder shaderSource = new StringBuilder(8192);

        try (InputStream is = fileAsset.openStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(isr)) {
            String line;

            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
        }

        return shaderSource.toString();
    }

    public ShaderBuilder buildProgram(String programName) {
        return buildProgram(programName, GL.defaultAttribLocations.getAttribLocations());
    }

    public ShaderBuilder buildProgram(String programName, Map<Integer, String> attribLocations) {

        shader = GL.glFactory.createShader();
        shader.create();

        int programID = shader.getProgramID();
        if (programID == 0) {
            throw new RuntimeException("Unable to create program: Name: " + programName);
        }

        // Attach all shaders
        for (int shaderID : shaderIDs) {
            GL.shaderHelper.attach(programID, shaderID);
        }

        // Bind all attribute locations
        if (attribLocations != null) {
            Iterator<Map.Entry<Integer, String>> iterator = attribLocations.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, String> entry = iterator.next();
                GL.shaderHelper.bindAttribLocation(programID, entry.getKey(), entry.getValue());
            }
        }

        // Link shaders
        GL.shaderHelper.link(programID);
        if (!GL.shaderHelper.getLinkStatus(programID)) {
            log.error("Program linking errors: Name: {} Info Log:\n{}",
                    programName, GL.shaderHelper.getInfoLog(programID));
            throw new RuntimeException("Program \"" + programName + "\" had linking errors");
        }

        // Validate program
        if (Fw.config.isValidateShaders()) {
            GL.shaderHelper.validate(programID);
            if (!GL.shaderHelper.getValidateStatus(programID)) {
                log.error("Program validate errors: Name: {} Info Log:\n{}",
                        programName, GL.shaderHelper.getInfoLog(programID));
                throw new RuntimeException("Program \"" + programName + "\" had validate errors");
            }
        }

        log.info("Loaded shader program: {}", programName);

        // Delete attached shaders
        for (int shaderID : shaderIDs) {
            GL.shaderHelper.detach(programID, shaderID);
            GL.shaderHelper.deleteShader(shaderID);
        }

        return this;
    }

    public ShaderBuilder compileShaders() {
        Iterator<Map.Entry<Integer, List<ShaderNameAndSource>>> iterator
                = shaderTypeToShaderNameAndSources.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, List<ShaderNameAndSource>> entry = iterator.next();
            int shaderType = entry.getKey();
            List<ShaderNameAndSource> shaderNamesAndSources = entry.getValue();
            for (ShaderNameAndSource shaderNameAndSource : shaderNamesAndSources) {
                String shaderName = shaderNameAndSource.getName();
                String shaderSource = shaderNameAndSource.getSource();
                int shaderID = compileShader(shaderName, shaderSource, shaderType);
                shaderIDs.add(shaderID);
            }
        }
        return this;
    }

    public Shader createShader(boolean reset) {
        Shader temp = shader;
        if (reset) {
            reset();
        }
        return temp;
    }

    public ShaderBuilder load(String shaderName, int shaderType) throws IOException {
        String shaderSource = read(shaderName);
        List<ShaderNameAndSource> shaderNamesAndSources = shaderTypeToShaderNameAndSources.get(shaderType);
        if (shaderNamesAndSources == null) {
            shaderNamesAndSources = new ArrayList<>();
            shaderTypeToShaderNameAndSources.put(shaderType, shaderNamesAndSources);
        }
        shaderNamesAndSources.add(new ShaderNameAndSource(shaderName, shaderSource));
        return this;
    }

    public final void reset() {
        shaderIDs.clear();
        shaderTypeToShaderNameAndSources.clear();
        shader = null;
    }

    private static class ShaderNameAndSource {

        private final String name;
        private final String source;

        public ShaderNameAndSource(String name, String source) {
            this.name = name;
            this.source = source;
        }

        public String getName() {
            return name;
        }

        public String getSource() {
            return source;
        }

    }

}
