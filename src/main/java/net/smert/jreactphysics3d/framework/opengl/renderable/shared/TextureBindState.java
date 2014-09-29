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
package net.smert.jreactphysics3d.framework.opengl.renderable.shared;

import net.smert.jreactphysics3d.framework.opengl.GL;
import net.smert.jreactphysics3d.framework.opengl.Texture;
import net.smert.jreactphysics3d.framework.opengl.constants.TextureTargets;
import net.smert.jreactphysics3d.framework.opengl.constants.TextureUnit;
import net.smert.jreactphysics3d.framework.opengl.renderable.factory.Renderable;
import net.smert.jreactphysics3d.framework.utils.HashMapIntInt;

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
        if (activeTextureUnit != textureUnit) {
            activeTextureUnit = textureUnit;
            GL.textureHelper.activeTexture(textureUnit);
        }
    }

    private void setBindTexture(Texture texture) {
        boolean bindChanged = false;
        boolean flagChanged = false;
        int textureID = texture.getTextureID();
        int textureTarget = texture.getTextureTarget();
        int activeTextureID = textureUnitToTextureID.get(activeTextureUnit);

        if ((activeTextureID != HashMapIntInt.NOT_FOUND) && (activeTextureID != textureID)) {
            textureUnitToTextureID.put(activeTextureUnit, textureID);
            bindChanged = true;
            if ((activeTextureUnit >= TextureUnit.TEXTURE0)
                    && (activeTextureUnit < TextureUnit.TEXTURE0 + maxModelTextureUnitsWithTextureFlag)) {
                flagChanged = true;
            }
        }

        if (bindChanged) {
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
                    Renderable.shaderBindState.setTextureFlag(1.0f);
                } else {
                    Renderable.shaderBindState.setTextureFlag(0.0f);
                }
            }
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

    public void setTexture(int textureUnit, Texture texture) {
        setActiveTexture(textureUnit);
        if (texture == null) {
            unbindCurrent();
        } else {
            setBindTexture(texture);
        }
    }

    public void setTexture0(Texture texture) {
        setTexture(TextureUnit.TEXTURE0, texture);
    }

    public void setTexture1(Texture texture) {
        setTexture(TextureUnit.TEXTURE1, texture);
    }

    public void setTexture2(Texture texture) {
        setTexture(TextureUnit.TEXTURE2, texture);
    }

    public void setTexture3(Texture texture) {
        setTexture(TextureUnit.TEXTURE3, texture);
    }

    public void setTexture4(Texture texture) {
        setTexture(TextureUnit.TEXTURE4, texture);
    }

    public void setTexture5(Texture texture) {
        setTexture(TextureUnit.TEXTURE5, texture);
    }

    public void setTexture6(Texture texture) {
        setTexture(TextureUnit.TEXTURE6, texture);
    }

    public void setTexture7(Texture texture) {
        setTexture(TextureUnit.TEXTURE7, texture);
    }

    public void setTexture8(Texture texture) {
        setTexture(TextureUnit.TEXTURE8, texture);
    }

    public void setTexture9(Texture texture) {
        setTexture(TextureUnit.TEXTURE9, texture);
    }

    public void setTexture10(Texture texture) {
        setTexture(TextureUnit.TEXTURE10, texture);
    }

    public void setTexture11(Texture texture) {
        setTexture(TextureUnit.TEXTURE11, texture);
    }

    public void setTexture12(Texture texture) {
        setTexture(TextureUnit.TEXTURE12, texture);
    }

    public void setTexture13(Texture texture) {
        setTexture(TextureUnit.TEXTURE13, texture);
    }

    public void setTexture14(Texture texture) {
        setTexture(TextureUnit.TEXTURE14, texture);
    }

    public void setTexture15(Texture texture) {
        setTexture(TextureUnit.TEXTURE15, texture);
    }

    public void unbindAll() {
        for (int i = 0; i < maxTextureUnits; i++) {
            setTexture(TextureUnit.TEXTURE0 + i, null);
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
            Renderable.shaderBindState.setTextureFlag(0.0f);
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
            setTexture(TextureUnit.TEXTURE0 + i, null);
        }
    }

    public void unbindShader() {
        for (int i = maxModelTextureUnits; i < maxShaderTextureUnits; i++) {
            setTexture(TextureUnit.TEXTURE0 + i, null);
        }
    }

}
