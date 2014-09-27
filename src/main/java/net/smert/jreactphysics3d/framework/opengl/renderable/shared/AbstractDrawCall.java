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

import net.smert.jreactphysics3d.framework.opengl.renderable.factory.Configuration;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public abstract class AbstractDrawCall implements DrawCall {

    protected int[] elementCounts;
    protected int[] primitiveModes;
    protected static Configuration renderableConfig;

    public int[] getElementCounts() {
        return elementCounts;
    }

    public void setElementCounts(int[] elementCounts) {
        this.elementCounts = elementCounts;
    }

    public int[] getPrimitiveModes() {
        return primitiveModes;
    }

    public void setPrimitiveModes(int[] primitiveModes) {
        this.primitiveModes = primitiveModes;
    }

    public static void SetRenderableConfiguration(Configuration renderableConfig) {
        AbstractDrawCall.renderableConfig = renderableConfig;
        if (renderableConfig.isImmutable() == false) {
            throw new RuntimeException("Renderable configuration must be made immutable");
        }
    }

}
