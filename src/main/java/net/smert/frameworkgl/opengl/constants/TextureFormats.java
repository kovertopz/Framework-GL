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
package net.smert.frameworkgl.opengl.constants;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class TextureFormats {

    public final static int DEPTH_COMPONENT = GL11.GL_DEPTH_COMPONENT;
    public final static int DEPTH_STENCIL = GL30.GL_DEPTH_STENCIL;
    public final static int R = GL11.GL_RED;
    public final static int RG = GL30.GL_RG;
    public final static int RGB = GL11.GL_RGB;
    public final static int RGBA = GL11.GL_RGBA;

    private TextureFormats() {
    }

}
