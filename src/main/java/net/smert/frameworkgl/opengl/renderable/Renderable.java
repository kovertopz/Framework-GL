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
package net.smert.frameworkgl.opengl.renderable;

import net.smert.frameworkgl.opengl.renderable.displaylist.DisplayListRenderCallBuilder;
import net.smert.frameworkgl.opengl.renderable.gl1.BindStateGL1;
import net.smert.frameworkgl.opengl.renderable.gl2.BindStateGL2;
import net.smert.frameworkgl.opengl.renderable.gl3.BindStateGL3;
import net.smert.frameworkgl.opengl.renderable.immediatemode.ImmediateModeRenderCallBuilder;
import net.smert.frameworkgl.opengl.renderable.shared.DrawCommands;
import net.smert.frameworkgl.opengl.renderable.shared.RenderableBuilder;
import net.smert.frameworkgl.opengl.renderable.shared.RenderableConfigurationPool;
import net.smert.frameworkgl.opengl.renderable.shared.ShaderBindState;
import net.smert.frameworkgl.opengl.renderable.shared.ShaderPool;
import net.smert.frameworkgl.opengl.renderable.shared.TextureBindState;
import net.smert.frameworkgl.opengl.renderable.shared.TexturePool;
import net.smert.frameworkgl.opengl.renderable.va.VADrawCallBuilder;
import net.smert.frameworkgl.opengl.renderable.va.VertexArrays;
import net.smert.frameworkgl.opengl.renderable.vbo.ByteBuffers;
import net.smert.frameworkgl.opengl.renderable.vbo.VBODrawCallBuilder;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Renderable {

    public static BindStateGL1 bindState1;
    public static BindStateGL2 bindState2;
    public static BindStateGL3 bindState3;
    public static ByteBuffers byteBuffers;
    public static DisplayListRenderCallBuilder displayListRenderCallBuilder;
    public static DrawCommands drawCommandsConversion;
    public static ImmediateModeRenderCallBuilder immediateModeRenderCallBuilder;
    public static RenderableBuilder renderableBuilder;
    public static RenderableConfigurationPool configPool;
    public static ShaderBindState shaderBindState;
    public static ShaderPool shaderPool;
    public static TextureBindState textureBindState;
    public static TexturePool texturePool;
    public static VADrawCallBuilder vaDrawCallBuilder;
    public static VBODrawCallBuilder vboDrawCallBuilder;
    public static VertexArrays vertexArrays;

}
