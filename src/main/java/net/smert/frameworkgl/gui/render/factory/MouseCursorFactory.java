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
package net.smert.frameworkgl.gui.render.factory;

import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import net.smert.frameworkgl.opengl.GL;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class MouseCursorFactory implements de.lessvoid.nifty.render.batch.spi.MouseCursorFactory {

    @Override
    public MouseCursor create(String filename, int hotspotX, int hotspotY, NiftyResourceLoader resourceLoader)
            throws IOException {
        net.smert.frameworkgl.gui.render.MouseCursor cursor = new net.smert.frameworkgl.gui.render.MouseCursor();

        // Get image reader and load the image
        BufferedImage bufferedImage = GL.textureReader.getBufferedImage(filename);

        // Load the texture which creates a byte buffer in ARGB format
        GL.textureBuilder.setConvertToRGBA(false).load2D(bufferedImage);

        // Extract data from texture builder
        int height = GL.textureBuilder.getTextureHeight();
        int width = GL.textureBuilder.getTextureWidth();
        ByteBuffer pixelData = GL.textureBuilder.getPixelByteBuffer();

        // Reset the builder
        GL.textureBuilder.reset();

        // Create the cursor
        cursor.create(width, height, hotspotX, height - hotspotY - 1, 1, pixelData.asIntBuffer(), null);

        return cursor;
    }

}
