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
package net.smert.frameworkgl.gui.render;

import net.smert.frameworkgl.opengl.Texture;
import net.smert.frameworkgl.opengl.renderable.Renderable;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class RenderImage implements de.lessvoid.nifty.spi.render.RenderImage {

    private final String filename;
    private final Texture texture;

    public RenderImage(String filename, Texture texture) {
        this.filename = filename;
        this.texture = texture;
    }

    public String getFilename() {
        return filename;
    }

    public Texture getTexture() {
        return texture;
    }

    @Override
    public int getWidth() {
        return texture.getWidth();
    }

    @Override
    public int getHeight() {
        return texture.getHeight();
    }

    @Override
    public void dispose() {
        texture.destroy();
        Renderable.texturePool.remove(filename);
    }

}
