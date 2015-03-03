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
package net.smert.frameworkgl.opengl.pipeline;

import java.io.IOException;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.camera.Camera;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public abstract class AbstractRenderingPipeline implements RenderingPipeline {

    protected boolean debug;
    protected boolean frustumCulling;
    protected boolean shadowsEnabled;
    protected boolean wireframe;
    protected Camera camera;

    @Override
    public void destroy() {
    }

    @Override
    public void init() throws IOException {
    }

    @Override
    public void render() {
    }

    @Override
    public void reset() {
        debug = false;
        frustumCulling = true;
        shadowsEnabled = false;
        wireframe = false;
    }

    @Override
    public void switchPolygonFillMode() {
        if (wireframe) {
            GL.o1.setPolygonModeFrontAndBackLine();
        } else {
            GL.o1.setPolygonModeFrontAndBackFill();

        }
    }

    public class Config implements PipelineConfig {

        @Override
        public Camera getCamera() {
            return AbstractRenderingPipeline.this.camera;
        }

        @Override
        public void setCamera(Camera camera) {
            AbstractRenderingPipeline.this.camera = camera;
        }

        @Override
        public boolean isDebug() {
            return debug;
        }

        @Override
        public void setDebug(boolean debug) {
            AbstractRenderingPipeline.this.debug = debug;
        }

        @Override
        public boolean isFrustumCulling() {
            return frustumCulling;
        }

        @Override
        public void setFrustumCulling(boolean frustumCulling) {
            AbstractRenderingPipeline.this.frustumCulling = frustumCulling;
        }

        @Override
        public boolean isShadowsEnabled() {
            return shadowsEnabled;
        }

        @Override
        public void setShadowsEnabled(boolean shadowsEnabled) {
            AbstractRenderingPipeline.this.shadowsEnabled = shadowsEnabled;
        }

        @Override
        public boolean isWireframe() {
            return wireframe;
        }

        @Override
        public void setWireframe(boolean wireframe) {
            AbstractRenderingPipeline.this.wireframe = wireframe;
        }

    }

}
