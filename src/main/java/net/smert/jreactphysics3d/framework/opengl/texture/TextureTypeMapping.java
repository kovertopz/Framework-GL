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

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class TextureTypeMapping {

    private final int textureTypeID;
    private final int uniqueTextureID;

    public TextureTypeMapping(int textureTypeID, int uniqueTextureID) {
        this.textureTypeID = textureTypeID;
        this.uniqueTextureID = uniqueTextureID;
    }

    public int getTextureTypeID() {
        return textureTypeID;
    }

    public int getUniqueTextureID() {
        return uniqueTextureID;
    }

}
