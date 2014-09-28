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
package net.smert.jreactphysics3d.framework.opengl.image.tga;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import net.smert.jreactphysics3d.framework.Files;
import net.smert.jreactphysics3d.framework.Fw;
import net.smert.jreactphysics3d.framework.opengl.image.Conversion;
import net.smert.jreactphysics3d.framework.opengl.image.ImageReader;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class TGAReader implements ImageReader {

    private final static Logger log = LoggerFactory.getLogger(TGAReader.class);

    @Override
    public boolean defaultFlipHorizontally() {
        return false;
    }

    @Override
    public boolean defaultFlipVertically() {
        return false;
    }

    @Override
    public BufferedImage load(String filename) throws IOException {
        BufferedImage image;
        log.info("Loading TGA image: {}", filename);
        Files.FileAsset fileAsset = Fw.files.getTexture(filename);

        // Try to read the stream
        try (InputStream is = fileAsset.openStream()) {

            // Read TGA
            TGAImage tgaImage = TGAImage.read(is);
            int height = tgaImage.getHeight();
            int width = tgaImage.getWidth();

            // Create new BufferedImage
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            ByteBuffer tgaImageByteBuffer = tgaImage.getData();

            // Convert to byte array
            byte[] byteArray = new byte[tgaImageByteBuffer.limit()];
            tgaImageByteBuffer.get(byteArray);

            // Convert to int array
            int[] intArray;
            if (tgaImage.getGLFormat() == GL11.GL_RGB) {
                intArray = Conversion.ConvertByteArrayRGBToARGBIntArray(byteArray, width, height);
            } else {
                intArray = Conversion.ConvertByteArrayRGBAToARGBIntArray(byteArray, width, height);
            }
            image.setRGB(0, 0, width, height, intArray, 0, width);
        }
        return image;
    }

}
