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
package net.smert.frameworkgl.opengl.mesh;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.smert.frameworkgl.opengl.model.ModelReader;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class MeshReader {

    private final Map<String, ModelReader> modelReaders;

    public MeshReader() {
        modelReaders = new HashMap<>();
    }

    public ModelReader getModelReader(String filename) {

        // Get the extension from the filename
        int posOfLastPeriod = filename.lastIndexOf(".");
        if (posOfLastPeriod == -1) {
            throw new IllegalArgumentException("The filename must have an extension: " + filename);
        }
        String extension = filename.substring(posOfLastPeriod + 1).toLowerCase();

        // Does the model reader for this extension exist?
        if (!modelReaders.containsKey(extension)) {
            throw new IllegalArgumentException("The extension has not been registered: " + extension);
        }

        // Load the filename using the model reader
        return modelReaders.get(extension);
    }

    public void load(String filename, Mesh mesh) throws IOException {
        ModelReader modelReader = getModelReader(filename);
        modelReader.load(filename, mesh);
    }

    public void registerExtension(String extension, ModelReader modelReader) {
        extension = extension.toLowerCase();
        if (modelReaders.containsKey(extension)) {
            throw new IllegalArgumentException("The extension has already been registered: " + extension);
        }
        modelReaders.put(extension, modelReader);
    }

    public void unregisterExtension(String extension) {
        extension = extension.toLowerCase();
        if (!modelReaders.containsKey(extension)) {
            throw new IllegalArgumentException("The extension has not been registered: " + extension);
        }
        modelReaders.remove(extension);
    }

}
