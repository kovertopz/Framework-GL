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
public abstract class AbstractRenderingPipeline {

    protected boolean debug;
    protected boolean frustumCulling;
    protected boolean shadowsEnabled;
    protected boolean wireframe;
    protected Camera camera;

    public void destroy() {
    }

    public void init() throws IOException {
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isFrustumCulling() {
        return frustumCulling;
    }

    public void setFrustumCulling(boolean frustumCulling) {
        this.frustumCulling = frustumCulling;
    }

    public boolean isShadowsEnabled() {
        return shadowsEnabled;
    }

    public void setShadowsEnabled(boolean shadowsEnabled) {
        this.shadowsEnabled = shadowsEnabled;
    }

    public boolean isWireframe() {
        return wireframe;
    }

    public void setWireframe(boolean wireframe) {
        this.wireframe = wireframe;
    }

    public void render() {
    }

    public void reset() {
        debug = false;
        frustumCulling = true;
        shadowsEnabled = false;
        wireframe = false;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void switchPolygonFillMode() {
        if (wireframe) {
            GL.o1.setPolygonModeFrontAndBackLine();
        } else {
            GL.o1.setPolygonModeFrontAndBackFill();
        }
    }

}
