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
import net.smert.frameworkgl.math.Vector4f;
import net.smert.frameworkgl.opengl.LightParameterType;
import net.smert.frameworkgl.opengl.texture.TextureType;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Material {

    private boolean isOpaque;
    private int shininess;
    private String shader;
    private final Map<TextureType, String> textures;
    private final Map<LightParameterType, Vector4f> lighting;

    public Material() {
        isOpaque = true;
        shininess = 0;
        textures = new HashMap<>();
        lighting = new HashMap<>();
    }

    public boolean isIsOpaque() {
        return isOpaque;
    }

    public void setIsOpaque(boolean isOpaque) {
        this.isOpaque = isOpaque;
    }

    public int getShininess() {
        return shininess;
    }

    public void setShininess(int shininess) {
        this.shininess = shininess;
    }

    public String getShader() {
        return shader;
    }

    public void setShader(String shader) {
        this.shader = shader;
    }

    public String getTexture(TextureType textureType) {
        return textures.get(textureType);
    }

    public String setTexture(TextureType textureType, String filename) {
        return textures.put(textureType, filename);
    }

    public Map<TextureType, String> getTextures() {
        return textures;
    }

    public Map<LightParameterType, Vector4f> getLighting() {
        return lighting;
    }

    public Vector4f getLighting(LightParameterType lightParameterType) {
        return lighting.get(lightParameterType);
    }

    public Vector4f setLighting(LightParameterType lightParameterType, Vector4f value) {
        return lighting.put(lightParameterType, value);
    }

}
