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

import java.util.HashMap;
import java.util.Map;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.MaterialLight;
import net.smert.frameworkgl.opengl.TextureType;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class SegmentMaterial {

    private boolean isOpaque;
    private float textureFlag;
    private final Map<TextureType, String> textureTypeToFilename;
    private MaterialLight materialLight;

    public SegmentMaterial() {
        isOpaque = true;
        textureFlag = 1f;
        textureTypeToFilename = new HashMap<>();
        materialLight = GL.glFactory.createMaterialLight();
    }

    public float getTextureFlag() {
        return textureFlag;
    }

    public void setTextureFlag(float textureFlag) {
        this.textureFlag = textureFlag;
    }

    public MaterialLight getMaterialLight() {
        return materialLight;
    }

    public void setMaterialLight(MaterialLight materialLight) {
        this.materialLight = materialLight;
    }

    public String getTexture(TextureType textureType) {
        return textureTypeToFilename.get(textureType);
    }

    public String setTexture(TextureType textureType, String filename) {
        return textureTypeToFilename.put(textureType, filename);
    }

    public Map<TextureType, String> getTextures() {
        return textureTypeToFilename;
    }

    public boolean isIsOpaque() {
        return isOpaque;
    }

    public void setIsOpaque(boolean isOpaque) {
        this.isOpaque = isOpaque;
    }

}
