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

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Material {

    public final static int AMBIENT = GL11.GL_AMBIENT;
    public final static int AMBIENT_AND_DIFFUSE = GL11.GL_AMBIENT_AND_DIFFUSE;
    public final static int DIFFUSE = GL11.GL_DIFFUSE;
    public final static int EMISSION = GL11.GL_EMISSION;
    public final static int SHININESS = GL11.GL_SHININESS;
    public final static int SPECULAR = GL11.GL_SPECULAR;

    private Material() {
    }

}
