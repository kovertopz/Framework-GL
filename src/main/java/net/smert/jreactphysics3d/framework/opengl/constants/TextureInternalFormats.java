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

import org.lwjgl.opengl.ARBTextureFloat;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class TextureInternalFormats {

    public final static int DEPTH_COMPONENT16 = GL14.GL_DEPTH_COMPONENT16;
    public final static int DEPTH_COMPONENT24 = GL14.GL_DEPTH_COMPONENT24;
    public final static int DEPTH_COMPONENT32 = GL14.GL_DEPTH_COMPONENT32;
    public final static int DEPTH24_STENCIL8 = GL30.GL_DEPTH24_STENCIL8;
    public final static int R16F = GL30.GL_R16F;
    public final static int R32F = GL30.GL_R32F;
    public final static int RG16F = GL30.GL_RG16F;
    public final static int RG32F = GL30.GL_RG32F;
    public final static int RGB10_A2 = GL11.GL_RGB10_A2;
    public final static int RGB16F = ARBTextureFloat.GL_RGB16F_ARB;
    public final static int RGB32F = ARBTextureFloat.GL_RGB32F_ARB;
    public final static int RGB8 = GL11.GL_RGB8;
    public final static int RGBA16F = ARBTextureFloat.GL_RGBA16F_ARB;
    public final static int RGBA32F = ARBTextureFloat.GL_RGBA32F_ARB;
    public final static int RGBA8 = GL11.GL_RGBA8;

    private TextureInternalFormats() {
    }

}
