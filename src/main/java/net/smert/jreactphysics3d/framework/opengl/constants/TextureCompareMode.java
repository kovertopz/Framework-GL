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
package net.smert.jreactphysics3d.framework.opengl.constants;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class TextureCompareMode {

    public final static int COMPARE_R_TO_TEXTURE = GL14.GL_COMPARE_R_TO_TEXTURE;
    public final static int NONE = GL11.GL_NONE;

    private TextureCompareMode() {
    }

}
