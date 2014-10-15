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
package net.smert.frameworkgl.opengl;

import net.smert.frameworkgl.opengl.camera.factory.CameraFactory;
import net.smert.frameworkgl.opengl.factory.GLFactory;
import net.smert.frameworkgl.opengl.font.GLFontBuilder;
import net.smert.frameworkgl.opengl.helpers.BufferHelper;
import net.smert.frameworkgl.opengl.helpers.DisplayListHelper;
import net.smert.frameworkgl.opengl.helpers.FrameBufferObjectHelper;
import net.smert.frameworkgl.opengl.helpers.LegacyRenderHelper;
import net.smert.frameworkgl.opengl.helpers.RenderBufferObjectHelper;
import net.smert.frameworkgl.opengl.helpers.ShaderHelper;
import net.smert.frameworkgl.opengl.helpers.TextureHelper;
import net.smert.frameworkgl.opengl.helpers.VertexArrayHelper;
import net.smert.frameworkgl.opengl.helpers.VertexBufferObjectHelper;
import net.smert.frameworkgl.opengl.mesh.DynamicMeshBuilder;
import net.smert.frameworkgl.opengl.mesh.MeshReader;
import net.smert.frameworkgl.opengl.mesh.Tessellator;
import net.smert.frameworkgl.opengl.mesh.factory.MeshFactory;
import net.smert.frameworkgl.opengl.renderable.factory.RenderableFactoryGL1;
import net.smert.frameworkgl.opengl.renderable.factory.RenderableFactoryGL2;
import net.smert.frameworkgl.opengl.renderable.factory.RenderableFactoryGL3;
import net.smert.frameworkgl.opengl.renderer.GLFontRenderer;
import net.smert.frameworkgl.opengl.renderer.RendererGL1;
import net.smert.frameworkgl.opengl.renderer.RendererGL2;
import net.smert.frameworkgl.opengl.renderer.RendererGL3;
import net.smert.frameworkgl.opengl.shader.DefaultAttribLocations;
import net.smert.frameworkgl.opengl.shader.ShaderBuilder;
import net.smert.frameworkgl.opengl.texture.TextureBuilder;
import net.smert.frameworkgl.opengl.texture.TextureReader;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class GL {

    public static CameraFactory cameraFactory;
    public static BufferHelper bufferHelper;
    public static DefaultAttribLocations defaultAttribLocations;
    public static DisplayListHelper displayListHelper;
    public static DynamicMeshBuilder dynamicMeshBuilder;
    public static FrameBufferObjectHelper fboHelper;
    public static GLFactory glFactory;
    public static GLFontBuilder glFontBuilder;
    public static GLFontRenderer glFontRenderer;
    public static MeshFactory meshFactory;
    public static MeshReader meshReader;
    public static OpenGL1 o1;
    public static OpenGL2 o2;
    public static OpenGL3 o3;
    public static LegacyRenderHelper renderHelper;
    public static RenderableFactoryGL1 rf1;
    public static RenderableFactoryGL2 rf2;
    public static RenderableFactoryGL3 rf3;
    public static RendererGL1 renderer1;
    public static RendererGL2 renderer2;
    public static RendererGL3 renderer3;
    public static RenderBufferObjectHelper rboHelper;
    public static ShaderBuilder shaderBuilder;
    public static ShaderHelper shaderHelper;
    public static Tessellator tessellator;
    public static TextureBuilder textureBuilder;
    public static TextureHelper textureHelper;
    public static TextureReader textureReader;
    public static VertexArrayHelper vaHelper;
    public static VertexBufferObjectHelper vboHelper;

}
