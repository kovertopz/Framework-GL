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
package net.smert.frameworkgl.opengl.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.Texture;
import net.smert.frameworkgl.opengl.image.ImageReader;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class TextureReader {

    private boolean useFlipDefaults;
    private final Map<String, ImageReader> extensionToImageReader;

    public TextureReader() {
        useFlipDefaults = true;
        extensionToImageReader = new HashMap<>();
    }

    public ImageReader getImageReader(String filename) {

        // Get the extension from the filename
        int posOfLastPeriod = filename.lastIndexOf(".");
        if (posOfLastPeriod == -1) {
            throw new IllegalArgumentException("The filename must have an extension: " + filename);
        }
        String extension = filename.substring(posOfLastPeriod + 1).toLowerCase();

        // Does the image reader for this extension exist?
        if (!extensionToImageReader.containsKey(extension)) {
            throw new IllegalArgumentException("The extension has not been registered: " + extension);
        }

        // Load the filename using the image reader
        return extensionToImageReader.get(extension);
    }

    public boolean isUseFlipDefaults() {
        return useFlipDefaults;
    }

    public void setUseFlipDefaults(boolean useFlipDefaults) {
        this.useFlipDefaults = useFlipDefaults;
    }

    public Texture load(String filename) throws IOException {

        // Get image reader and load the image
        ImageReader imageReader = getImageReader(filename);
        BufferedImage bufferedImage = imageReader.load(filename);

        // Flip image if the format requires it
        if (useFlipDefaults) {
            GL.textureBuilder.setLoadFlipHorizontally(imageReader.defaultFlipHorizontally());
            GL.textureBuilder.setLoadFlipVertically(imageReader.defaultFlipVertically());
        }

        // Build the texture
        GL.textureBuilder.load2D(bufferedImage).buildTexture();
        return GL.textureBuilder.createTexture(true); // Reset to defaults
    }

    public void registerExtension(String extension, ImageReader imageReader) {
        extension = extension.toLowerCase();
        if (extensionToImageReader.containsKey(extension)) {
            throw new IllegalArgumentException("The extension has already been registered: " + extension);
        }
        extensionToImageReader.put(extension, imageReader);
    }

    public void unregisterExtension(String extension) {
        extension = extension.toLowerCase();
        if (!extensionToImageReader.containsKey(extension)) {
            throw new IllegalArgumentException("The extension has not been registered: " + extension);
        }
        extensionToImageReader.remove(extension);
    }

}
