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
package net.smert.frameworkgl.opengl.font;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.Texture;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.utils.HashMapIntString;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class AngelCodeFontBuilder {

    private boolean useMipmap;
    private float lodBias;
    private AngelCodeFont angelCodeFont;
    private String fontFilename;

    public AngelCodeFontBuilder buildFont() throws IOException {

        angelCodeFont = new AngelCodeFont();
        angelCodeFont.load(fontFilename);

        Iterator<HashMapIntString.Entry> iterator = angelCodeFont.getPages().entrySet().iterator();
        while (iterator.hasNext()) {
            HashMapIntString.Entry entry = iterator.next();
            String textureFilename = entry.getValue();

            // Use texture builder to create texture
            BufferedImage image = GL.textureReader.getBufferedImage(textureFilename);
            GL.textureBuilder.load2D(image);
            if (useMipmap) {
                GL.textureBuilder.setLodBias(lodBias).setUseMipmaps(useMipmap).
                        setFilterMagLinear().setFilterMinLinearMipmapLinear();
            } else {
                GL.textureBuilder.setFilterMagLinear().setFilterMinLinear();
            }
            Texture texture = GL.textureBuilder.buildTexture().createTexture(true);

            // Remove texture from the pool if it exists. Add texture to the pool.
            Texture existingTexture = Renderable.texturePool.remove(textureFilename);
            if (existingTexture != null) {
                existingTexture.destroy();
            }
            Renderable.texturePool.add(textureFilename, texture);
        }

        return this;
    }

    public AngelCodeFont createFont(boolean reset) {
        AngelCodeFont temp = angelCodeFont;
        if (reset) {
            reset();
        }
        return temp;
    }

    public boolean isUseMipmap() {
        return useMipmap;
    }

    public AngelCodeFontBuilder setUseMipmap(boolean useMipmap) {
        this.useMipmap = useMipmap;
        return this;
    }

    public float getLodBias() {
        return lodBias;
    }

    public AngelCodeFontBuilder setLodBias(float lodBias) {
        this.lodBias = lodBias;
        return this;
    }

    public AngelCodeFontBuilder load(String fontFilename) {
        this.fontFilename = fontFilename;
        return this;
    }

    public AngelCodeFontBuilder reset() {
        useMipmap = false;
        lodBias = 0;
        angelCodeFont = null;
        fontFilename = null;
        return this;
    }

}
