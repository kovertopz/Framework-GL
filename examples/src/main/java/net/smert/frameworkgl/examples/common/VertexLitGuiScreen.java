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
public class VertexLitGuiScreen extends SimpleDebugGuiScreen {

    private float spotOuterCutoff;
    private int shaderIndex;

    public VertexLitGuiScreen() {
        shaderIndex = 4;
    }

    private void renderCurrentShaderName() {
        String shaderName = "";

        switch (shaderIndex) {
            case 0:
                shaderName = "BlinnPhongSpecularDirectionalShader";
                break;
            case 1:
                shaderName = "BlinnPhongSpecularPointShader";
                break;
            case 2:
                shaderName = "BlinnPhongSpecularSpotShader";
                break;
            case 3:
                shaderName = "DiffuseDirectionalShader";
                break;
            case 4:
                shaderName = "DiffusePointShader";
                break;
            case 5:
                shaderName = "DiffuseSpotShader";
                break;
            case 6:
                shaderName = "PhongSpecularDirectionalShader";
                break;
            case 7:
                shaderName = "PhongSpecularPointShader";
                break;
            case 8:
                shaderName = "PhongSpecularSpotShader";
                break;
        }

        textRenderer.setTextColor("yellow");
        textRenderer.drawString(shaderName);
        textRenderer.textNewLine();
        if ((shaderIndex == 2) || (shaderIndex == 5) || (shaderIndex == 8)) {
            textRenderer.setTextColor("lime");
            textRenderer.drawString("Spot cutoff: " + spotOuterCutoff);
            textRenderer.textNewLine();
        }
    }

    public void decrementShaderIndex() {
        if (--shaderIndex < 0) {
            shaderIndex += 9;
        }
    }

    public float getSpotOuterCutoff() {
        return spotOuterCutoff;
    }

    public void setSpotOuterCutoff(float spotOuterCutoff) {
        this.spotOuterCutoff = spotOuterCutoff;
    }

    public int getShaderIndex() {
        return shaderIndex;
    }

    public void incrementShaderIndex() {
        shaderIndex = ++shaderIndex % 9;
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
    }

    @Override
    public void update() {
        super.update();
    }

}
