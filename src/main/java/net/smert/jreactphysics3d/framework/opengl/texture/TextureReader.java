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
package net.smert.jreactphysics3d.framework.opengl.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.smert.jreactphysics3d.framework.Fw;
import net.smert.jreactphysics3d.framework.opengl.Texture;
import net.smert.jreactphysics3d.framework.opengl.image.ImageReader;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class TextureReader {

    private boolean useFlipDefaults;
    private final Map<String, ImageReader> imageReaders;
    private final TextureBuilder textureBuilder;

    public TextureReader(TextureBuilder textureBuilder) {
        useFlipDefaults = true;
        imageReaders = new HashMap<>();
        this.textureBuilder = textureBuilder;
    }

    public boolean isUseFlipDefaults() {
        return useFlipDefaults;
    }

    public void setUseFlipDefaults(boolean useFlipDefaults) {
        this.useFlipDefaults = useFlipDefaults;
    }

    public Texture load(String filename) throws IOException {

        // Trim slashes
        filename = Fw.files.trimLeftSlashes(filename);

        // Get the extension from the filename
        int posOfLastPeriod = filename.lastIndexOf(".");
        if (posOfLastPeriod == -1) {
            throw new IllegalArgumentException("The filename must have an extension: " + filename);
        }
        String extension = filename.substring(posOfLastPeriod + 1).toLowerCase();

        // Does the image reader for this extension exist?
        if (!imageReaders.containsKey(extension)) {
            throw new IllegalArgumentException("The extension has not been registered: " + extension);
        }

        // Load the filename using the image reader
        ImageReader imageReader = imageReaders.get(extension);
        BufferedImage bufferedImage = imageReader.load(filename);

        // Flip image if the format requires it
        if (useFlipDefaults) {
            textureBuilder.setLoadFlipHorizontally(imageReader.defaultFlipHorizontally());
            textureBuilder.setLoadFlipVertically(imageReader.defaultFlipVertically());
        }

        // Build the texture
        textureBuilder.load2D(bufferedImage).buildTexture();
        return textureBuilder.build(true); // Reset to defaults
    }

    public void registerExtension(String extension, ImageReader imageReader) {
        if (imageReaders.containsKey(extension)) {
            throw new IllegalArgumentException("The extension has already been registered: " + extension);
        }
        imageReaders.put(extension, imageReader);
    }

    public void unregisterExtension(String extension) {
        if (!imageReaders.containsKey(extension)) {
            throw new IllegalArgumentException("The extension has not been registered: " + extension);
        }
        imageReaders.remove(extension);
    }

}
