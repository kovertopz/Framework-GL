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
package net.smert.frameworkgl.opengl.camera;

import net.smert.frameworkgl.math.Matrix4f;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class FrustumCullingClipSpace extends AbstractFrustumCulling {

    private final Matrix4f clippingMatrix;

    public FrustumCullingClipSpace() {
        clippingMatrix = new Matrix4f();
    }

    @Override
    public void updatePlanes(Matrix4f projectionMatrix, Matrix4f viewMatrix) {
        projectionMatrix.projectionMultiplyViewOut(viewMatrix, clippingMatrix);
        clippingMatrix.extractPlanes(clipPlanes);
    }

}
