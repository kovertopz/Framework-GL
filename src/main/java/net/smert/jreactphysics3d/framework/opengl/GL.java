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
package net.smert.jreactphysics3d.framework.opengl;

import net.smert.jreactphysics3d.framework.opengl.camera.factory.CameraFactory;
import net.smert.jreactphysics3d.framework.opengl.factory.GLFactory;
import net.smert.jreactphysics3d.framework.opengl.helpers.BufferHelper;
import net.smert.jreactphysics3d.framework.opengl.helpers.DisplayListHelper;
import net.smert.jreactphysics3d.framework.opengl.helpers.FrameBufferObjectHelper;
import net.smert.jreactphysics3d.framework.opengl.helpers.LegacyRenderHelper;
import net.smert.jreactphysics3d.framework.opengl.helpers.RenderBufferObjectHelper;
import net.smert.jreactphysics3d.framework.opengl.helpers.ShaderHelper;
import net.smert.jreactphysics3d.framework.opengl.helpers.TextureHelper;
import net.smert.jreactphysics3d.framework.opengl.helpers.VertexArrayHelper;
import net.smert.jreactphysics3d.framework.opengl.helpers.VertexBufferObjectHelper;
import net.smert.jreactphysics3d.framework.opengl.mesh.MeshReader;
import net.smert.jreactphysics3d.framework.opengl.mesh.factory.MeshFactory;
import net.smert.jreactphysics3d.framework.opengl.renderable.factory.RenderableFactoryGL1;
import net.smert.jreactphysics3d.framework.opengl.texture.TextureBuilder;
import net.smert.jreactphysics3d.framework.opengl.texture.TextureReader;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class GL {

    public static CameraFactory cf;
    public static BufferHelper bufferHelper;
    public static DisplayListHelper displayListHelper;
    public static FrameBufferObjectHelper fboHelper;
    public static GLFactory glf;
    public static MeshFactory mf;
    public static MeshReader meshReader;
    public static OpenGL1 o1;
    public static OpenGL2 o2;
    public static OpenGL3 o3;
    public static LegacyRenderHelper renderHelper;
    public static RenderableFactoryGL1 rf1;
    public static RenderBufferObjectHelper rboHelper;
    public static ShaderHelper shaderHelper;
    public static TextureBuilder textureBuilder;
    public static TextureHelper textureHelper;
    public static TextureReader textureReader;
    public static VertexArrayHelper vaHelper;
    public static VertexBufferObjectHelper vboHelper;

}
