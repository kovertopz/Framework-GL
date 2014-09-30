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
package net.smert.jreactphysics3d.framework.opengl.renderable.factory;

import net.smert.jreactphysics3d.framework.opengl.renderable.gl1.DrawCommands;
import net.smert.jreactphysics3d.framework.opengl.renderable.shared.ShaderBindState;
import net.smert.jreactphysics3d.framework.opengl.renderable.shared.ShaderPool;
import net.smert.jreactphysics3d.framework.opengl.renderable.shared.TextureBindState;
import net.smert.jreactphysics3d.framework.opengl.renderable.shared.TexturePool;
import net.smert.jreactphysics3d.framework.opengl.renderable.va.VABindState;
import net.smert.jreactphysics3d.framework.opengl.renderable.va.VABuilder;
import net.smert.jreactphysics3d.framework.opengl.renderable.va.VertexArrays;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.ByteBuffers;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.VBOBindState;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.VBOBuilder;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Renderable {

    public static ByteBuffers byteBuffers;
    public static DrawCommands drawCommandsConversion;
    public static RenderableConfiguration config;
    public static ShaderBindState shaderBindState;
    public static ShaderPool shaderPool;
    public static TextureBindState textureBindState;
    public static TexturePool texturePool;
    public static VABindState vaBindState;
    public static VABuilder vaBuilder;
    public static VBOBindState vboBindState;
    public static VBOBuilder vboBuilder;
    public static VertexArrays vertexArrays;

}
