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
package net.smert.frameworkgl.opengl.renderable.shared;

import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.Texture;
import net.smert.frameworkgl.opengl.constants.TextureTargets;
import net.smert.frameworkgl.opengl.constants.TextureUnit;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.texture.TextureTypeMapping;
import net.smert.frameworkgl.utils.HashMapIntInt;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class TextureBindState {

    private int activeTextureUnit;
    private int maxModelTextureUnits;
    private int maxModelTextureUnitsWithTextureFlag;
    private int maxShaderTextureUnits;
    private int maxTextureUnits;
    private final HashMapIntInt textureUnitToTextureID;

    public TextureBindState() {
        maxModelTextureUnits = 8;
        maxModelTextureUnitsWithTextureFlag = 4; // Must be less than maxModelTextureUnits
        maxShaderTextureUnits = 8;
        maxTextureUnits = 16;
        textureUnitToTextureID = new HashMapIntInt();
        reset();
    }

    private void setActiveTexture(int textureUnit) {
        if (activeTextureUnit == textureUnit) {
            return;
        }
        activeTextureUnit = textureUnit;
        GL.textureHelper.activeTexture(textureUnit);
    }

    private void setBindTexture(Texture texture) {
        int textureID = texture.getTextureID();
        int activeTextureID = textureUnitToTextureID.get(activeTextureUnit);

        if (activeTextureID == textureID) {
            return;
        }

        boolean flagChanged = false;
        textureUnitToTextureID.put(activeTextureUnit, textureID);
        if ((activeTextureUnit >= TextureUnit.TEXTURE0)
                && (activeTextureUnit < TextureUnit.TEXTURE0 + maxModelTextureUnitsWithTextureFlag)) {
            flagChanged = true;
        }

        int textureTarget = texture.getTextureTarget();
        switch (textureTarget) {
            case TextureTargets.TEXTURE_2D:
                GL.textureHelper.setTextureTarget2D();
                break;

            case TextureTargets.TEXTURE_3D:
                GL.textureHelper.setTextureTarget3D();
                break;

            case TextureTargets.TEXTURE_CUBE_MAP:
                GL.textureHelper.setTextureTargetCubeMap();
                break;
        }
        GL.textureHelper.bind(textureID);

        if (flagChanged) {
            if (textureID != 0) {
                Renderable.shaderBindState.sendUniformTextureFlag(1f);
            } else {
                Renderable.shaderBindState.sendUniformTextureFlag(0f);
            }
        }
    }

    public void bindTexture(int textureUnit, Texture texture) {
        setActiveTexture(textureUnit);
        if (texture == null) {
            unbindCurrent();
            return;
        }
        setBindTexture(texture);
    }

    public void bindTexture0(Texture texture) {
        bindTexture(TextureUnit.TEXTURE0, texture);
    }

    public void bindTexture1(Texture texture) {
        bindTexture(TextureUnit.TEXTURE1, texture);
    }

    public void bindTexture2(Texture texture) {
        bindTexture(TextureUnit.TEXTURE2, texture);
    }

    public void bindTexture3(Texture texture) {
        bindTexture(TextureUnit.TEXTURE3, texture);
    }

    public void bindTexture4(Texture texture) {
        bindTexture(TextureUnit.TEXTURE4, texture);
    }

    public void bindTexture5(Texture texture) {
        bindTexture(TextureUnit.TEXTURE5, texture);
    }

    public void bindTexture6(Texture texture) {
        bindTexture(TextureUnit.TEXTURE6, texture);
    }

    public void bindTexture7(Texture texture) {
        bindTexture(TextureUnit.TEXTURE7, texture);
    }

    public void bindTexture8(Texture texture) {
        bindTexture(TextureUnit.TEXTURE8, texture);
    }

    public void bindTexture9(Texture texture) {
        bindTexture(TextureUnit.TEXTURE9, texture);
    }

    public void bindTexture10(Texture texture) {
        bindTexture(TextureUnit.TEXTURE10, texture);
    }

    public void bindTexture11(Texture texture) {
        bindTexture(TextureUnit.TEXTURE11, texture);
    }

    public void bindTexture12(Texture texture) {
        bindTexture(TextureUnit.TEXTURE12, texture);
    }

    public void bindTexture13(Texture texture) {
        bindTexture(TextureUnit.TEXTURE13, texture);
    }

    public void bindTexture14(Texture texture) {
        bindTexture(TextureUnit.TEXTURE14, texture);
    }

    public void bindTexture15(Texture texture) {
        bindTexture(TextureUnit.TEXTURE15, texture);
    }

    public void bindTextures(TextureTypeMapping[] textures) {
        if (textures == null) {
            unbindModel();
            return;
        }
        for (TextureTypeMapping mapping : textures) {
            int textureUnit = Renderable.shaderBindState.getTextureUnit(mapping.getTextureTypeID());
            Texture texture = Renderable.texturePool.get(mapping.getUniqueTextureID());
            bindTexture(textureUnit, texture);
        }
    }

    public int getMaxModelTextureUnits() {
        return maxModelTextureUnits;
    }

    public void setMaxModelTextureUnits(int maxModelTextureUnits) {
        this.maxModelTextureUnits = maxModelTextureUnits;
    }

    public int getMaxModelTextureUnitsWithTextureFlag() {
        return maxModelTextureUnitsWithTextureFlag;
    }

    public void setMaxModelTextureUnitsWithTextureFlag(int maxModelTextureUnitsWithTextureFlag) {
        this.maxModelTextureUnitsWithTextureFlag = maxModelTextureUnitsWithTextureFlag;
    }

    public int getMaxShaderTextureUnits() {
        return maxShaderTextureUnits;
    }

    public void setMaxShaderTextureUnits(int maxShaderTextureUnits) {
        this.maxShaderTextureUnits = maxShaderTextureUnits;
    }

    public int getMaxTextureUnits() {
        return maxTextureUnits;
    }

    public void setMaxTextureUnits(int maxTextureUnits) {
        this.maxTextureUnits = maxTextureUnits;
    }

    public final void reset() {
        activeTextureUnit = -1;
    }

    public void unbindAll() {
        for (int i = 0; i < maxTextureUnits; i++) {
            bindTexture(TextureUnit.TEXTURE0 + i, null);
        }
    }

    public void unbindCurrent() {
        int activeTextureID = textureUnitToTextureID.get(activeTextureUnit);

        // Null key early out or if the active texture ID is already zero
        if ((activeTextureID == HashMapIntInt.NOT_FOUND) || (activeTextureID == 0)) {
            return;
        }

        // Active texture ID has changed
        textureUnitToTextureID.put(activeTextureUnit, 0);
        if ((activeTextureUnit >= TextureUnit.TEXTURE0)
                && (activeTextureUnit < TextureUnit.TEXTURE0 + maxModelTextureUnitsWithTextureFlag)) {
            Renderable.shaderBindState.sendUniformTextureFlag(0f);
        }

        // Unbind all texture targets
        GL.textureHelper.setTextureTarget2D();
        GL.textureHelper.unbind();
        GL.textureHelper.setTextureTarget3D();
        GL.textureHelper.unbind();
        GL.textureHelper.setTextureTargetCubeMap();
        GL.textureHelper.unbind();
    }

    public void unbindModel() {
        for (int i = 0; i < maxModelTextureUnits; i++) {
            bindTexture(TextureUnit.TEXTURE0 + i, null);
        }
    }

    public void unbindShader() {
        for (int i = maxModelTextureUnits; i < maxShaderTextureUnits; i++) {
            bindTexture(TextureUnit.TEXTURE0 + i, null);
        }
    }

}
