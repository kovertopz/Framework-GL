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

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL43;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class ShaderTypes {

    public final static int COMPUTE_SHADER = GL43.GL_COMPUTE_SHADER;
    public final static int FRAGMENT_SHADER = GL20.GL_FRAGMENT_SHADER;
    public final static int GEOMETRY_SHADER = GL32.GL_GEOMETRY_SHADER;
    public final static int TESS_CONTROL_SHADER = GL40.GL_TESS_CONTROL_SHADER;
    public final static int TESS_EVALUATION_SHADER = GL40.GL_TESS_EVALUATION_SHADER;
    public final static int VERTEX_SHADER = GL20.GL_VERTEX_SHADER;

    private ShaderTypes() {
    }

}
