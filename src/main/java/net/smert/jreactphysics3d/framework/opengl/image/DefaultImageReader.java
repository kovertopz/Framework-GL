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
package net.smert.jreactphysics3d.framework.opengl.image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import net.smert.jreactphysics3d.framework.Files;
import net.smert.jreactphysics3d.framework.Fw;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class DefaultImageReader implements ImageReader {

    @Override
    public boolean defaultFlipHorizontally() {
        return false;
    }

    @Override
    public boolean defaultFlipVertically() {
        // By default many image formats have the origin in the upper
        // left corner. We want a bottom left origin for OpenGL.
        return true;
    }

    @Override
    public BufferedImage load(String filename) throws IOException {
        BufferedImage bufferedImage;
        Files.FileAsset fileAsset = Fw.files.getTexture(filename);

        // Try to read the stream (without crossing it)
        try (InputStream is = fileAsset.openStream()) {
            bufferedImage = ImageIO.read(is);
        }
        return bufferedImage;
    }

}
