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

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Light {

    public final static int AMBIENT = GL11.GL_AMBIENT;
    public final static int CONSTANT_ATTENUATION = GL11.GL_CONSTANT_ATTENUATION;
    public final static int DIFFUSE = GL11.GL_DIFFUSE;
    public final static int LIGHT0 = GL11.GL_LIGHT0;
    public final static int LIGHT1 = GL11.GL_LIGHT1;
    public final static int LIGHT2 = GL11.GL_LIGHT2;
    public final static int LIGHT3 = GL11.GL_LIGHT3;
    public final static int LIGHT4 = GL11.GL_LIGHT4;
    public final static int LIGHT5 = GL11.GL_LIGHT5;
    public final static int LIGHT6 = GL11.GL_LIGHT6;
    public final static int LIGHT7 = GL11.GL_LIGHT7;
    public final static int LINEAR_ATTENUATION = GL11.GL_LINEAR_ATTENUATION;
    public final static int POSITION = GL11.GL_POSITION;
    public final static int QUADRATIC_ATTENUATION = GL11.GL_QUADRATIC_ATTENUATION;
    public final static int SPECULAR = GL11.GL_SPECULAR;
    public final static int SPOT_CUTOFF = GL11.GL_SPOT_CUTOFF;
    public final static int SPOT_DIRECTION = GL11.GL_SPOT_DIRECTION;
    public final static int SPOT_EXPONENT = GL11.GL_SPOT_EXPONENT;

    private Light() {
    }

}
