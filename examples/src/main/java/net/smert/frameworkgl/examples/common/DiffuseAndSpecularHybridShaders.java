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

import java.io.IOException;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class DiffuseAndSpecularHybridShaders {

    private net.smert.frameworkgl.opengl.shader.pixellit.multi.BlinnPhongSpecularHybridShader pixelLitMultiBlinnPhongSpecularHybridShader;
    private net.smert.frameworkgl.opengl.shader.pixellit.multi.DiffuseHybridShader pixelLitMultiDiffuseHybridShader;
    private net.smert.frameworkgl.opengl.shader.pixellit.multi.PhongSpecularHybridShader pixelLitMultiPhongSpecularHybridShader;
    private net.smert.frameworkgl.opengl.shader.vertexlit.multi.BlinnPhongSpecularHybridShader vertexLitMultiBlinnPhongSpecularHybridShader;
    private net.smert.frameworkgl.opengl.shader.vertexlit.multi.DiffuseHybridShader vertexLitMultiDiffuseHybridShader;
    private net.smert.frameworkgl.opengl.shader.vertexlit.multi.PhongSpecularHybridShader vertexLitMultiPhongSpecularHybridShader;

    public void destroy() {
        pixelLitMultiBlinnPhongSpecularHybridShader.destroy();
        pixelLitMultiDiffuseHybridShader.destroy();
        pixelLitMultiPhongSpecularHybridShader.destroy();
        vertexLitMultiBlinnPhongSpecularHybridShader.destroy();
        vertexLitMultiDiffuseHybridShader.destroy();
        vertexLitMultiPhongSpecularHybridShader.destroy();
    }

    public net.smert.frameworkgl.opengl.shader.pixellit.multi.BlinnPhongSpecularHybridShader
            getPixelLitMultiBlinnPhongSpecularHybridShader() {
        return pixelLitMultiBlinnPhongSpecularHybridShader;
    }

    public net.smert.frameworkgl.opengl.shader.pixellit.multi.DiffuseHybridShader
            getPixelLitMultiDiffuseHybridShader() {
        return pixelLitMultiDiffuseHybridShader;
    }

    public net.smert.frameworkgl.opengl.shader.pixellit.multi.PhongSpecularHybridShader
            getPixelLitMultiPhongSpecularHybridShader() {
        return pixelLitMultiPhongSpecularHybridShader;
    }

    public net.smert.frameworkgl.opengl.shader.vertexlit.multi.BlinnPhongSpecularHybridShader
            getVertexLitMultiBlinnPhongSpecularHybridShader() {
        return vertexLitMultiBlinnPhongSpecularHybridShader;
    }

    public net.smert.frameworkgl.opengl.shader.vertexlit.multi.DiffuseHybridShader
            getVertexLitMultiDiffuseHybridShader() {
        return vertexLitMultiDiffuseHybridShader;
    }

    public net.smert.frameworkgl.opengl.shader.vertexlit.multi.PhongSpecularHybridShader
            getVertexLitMultiPhongSpecularHybridShader() {
        return vertexLitMultiPhongSpecularHybridShader;
    }

    public void init() throws IOException {
        pixelLitMultiBlinnPhongSpecularHybridShader
                = net.smert.frameworkgl.opengl.shader.pixellit.multi.BlinnPhongSpecularHybridShader.Factory.Create();
        pixelLitMultiBlinnPhongSpecularHybridShader.init();
        pixelLitMultiDiffuseHybridShader
                = net.smert.frameworkgl.opengl.shader.pixellit.multi.DiffuseHybridShader.Factory.Create();
        pixelLitMultiDiffuseHybridShader.init();
        pixelLitMultiPhongSpecularHybridShader
                = net.smert.frameworkgl.opengl.shader.pixellit.multi.PhongSpecularHybridShader.Factory.Create();
        pixelLitMultiPhongSpecularHybridShader.init();
        vertexLitMultiBlinnPhongSpecularHybridShader
                = net.smert.frameworkgl.opengl.shader.vertexlit.multi.BlinnPhongSpecularHybridShader.Factory.Create();
        vertexLitMultiBlinnPhongSpecularHybridShader.init();
        vertexLitMultiDiffuseHybridShader
                = net.smert.frameworkgl.opengl.shader.vertexlit.multi.DiffuseHybridShader.Factory.Create();
        vertexLitMultiDiffuseHybridShader.init();
        vertexLitMultiPhongSpecularHybridShader
                = net.smert.frameworkgl.opengl.shader.vertexlit.multi.PhongSpecularHybridShader.Factory.Create();
        vertexLitMultiPhongSpecularHybridShader.init();
    }

}
