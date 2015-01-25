/**
 * Copyright 2014 Jason Sorensen (sorensenj@smert.net)
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
package net.smert.frameworkgl.examples.common;

import net.smert.frameworkgl.gui.SimpleDebugGuiScreen;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class HybridPixelOrVertexLitGuiScreen extends SimpleDebugGuiScreen {

    private boolean isPixelLit;
    private float spotInnerCutoff;
    private float spotOuterCutoff;
    private int lightsIndex;
    private int shaderIndex;

    public HybridPixelOrVertexLitGuiScreen() {
        isPixelLit = false;
        lightsIndex = 0;
        shaderIndex = 1;
    }

    private void renderCurrentLightsName() {
        String lightsName = "";

        switch (lightsIndex) {
            case 0:
                lightsName = "Directional Lights";
                break;
            case 1:
                lightsName = "Point Lights";
                break;
            case 2:
                lightsName = "Spot Lights";
                break;
        }

        textRenderer.setTextColor("lime");
        textRenderer.drawString(lightsName);
        textRenderer.textNewLine();
        if (lightsIndex == 2) {
            textRenderer.drawString("Spot outer cutoff: " + spotOuterCutoff);
            textRenderer.textNewLine();
            if (isPixelLit) {
                textRenderer.drawString("Spot inner cutoff: " + spotInnerCutoff);
                textRenderer.textNewLine();
            }
        }
    }

    private void renderCurrentShaderName() {
        String shaderName = "";

        switch (shaderIndex) {
            case 0:
                shaderName = "BlinnPhongSpecularHybridShader";
                break;
            case 1:
                shaderName = "DiffuseHybridShader";
                break;
            case 2:
                shaderName = "PhongSpecularHybridShader";
                break;
        }

        textRenderer.setTextColor("yellow");
        textRenderer.drawString(shaderName);
        textRenderer.textNewLine();
    }

    public void decrementLightIndex() {
        if (--lightsIndex < 0) {
            lightsIndex += 3;
        }
    }

    public void decrementShaderIndex() {
        if (--shaderIndex < 0) {
            shaderIndex += 3;
        }
    }

    public float getSpotInnerCutoff() {
        return spotInnerCutoff;
    }

    public void setSpotInnerCutoff(float spotInnerCutoff) {
        this.spotInnerCutoff = spotInnerCutoff;
    }

    public float getSpotOuterCutoff() {
        return spotOuterCutoff;
    }

    public void setSpotOuterCutoff(float spotOuterCutoff) {
        this.spotOuterCutoff = spotOuterCutoff;
    }

    public int getLightsIndex() {
        return lightsIndex;
    }

    public int getShaderIndex() {
        return shaderIndex;
    }

    public void incrementLightIndex() {
        lightsIndex = ++lightsIndex % 3;
    }

    public void incrementShaderIndex() {
        shaderIndex = ++shaderIndex % 3;
    }

    public void setIsPixelLit(boolean isPixelLit) {
        this.isPixelLit = isPixelLit;
    }

    @Override
    public void onEnd() {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void render() {
        super.render();
        renderCurrentShaderName();
        renderCurrentLightsName();
    }

    @Override
    public void update() {
        super.update();
    }

}
