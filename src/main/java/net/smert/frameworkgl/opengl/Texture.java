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
package net.smert.frameworkgl.opengl;

import net.smert.frameworkgl.opengl.constants.TextureTargets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Texture {

    private final static Logger log = LoggerFactory.getLogger(Texture.class);

    private int height;
    private int textureID;
    private int textureTarget;
    private int width;

    public Texture() {
        textureID = 0;
        textureTarget = TextureTargets.TEXTURE_2D;
    }

    public void create() {
        destroy();
        textureID = GL.textureHelper.create();
        log.debug("Created a new texture with ID: {}", textureID);
    }

    public void destroy() {
        if (textureID == 0) {
            return;
        }
        GL.textureHelper.delete(textureID);
        log.debug("Deleted a texture with ID: {}", textureID);
        textureID = 0;
    }

    public int getHeight() {
        return height;
    }

    public int getTextureID() {
        return textureID;
    }

    public int getTextureTarget() {
        return textureTarget;
    }

    public void setTextureTarget(int textureTarget) {
        this.textureTarget = textureTarget;
    }

    public int getWidth() {
        return width;
    }

    public void setWidthAndHeight(int width, int height) {
        this.height = height;
        this.width = width;
    }

}
