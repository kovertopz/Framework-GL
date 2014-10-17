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

import java.nio.FloatBuffer;
import net.smert.frameworkgl.math.Vector4f;
import net.smert.frameworkgl.opengl.constants.LightModel;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class AmbientLight {

    private final Vector4f ambient;

    public AmbientLight() {
        ambient = new Vector4f();
        reset();
    }

    public Vector4f getAmbient() {
        return ambient;
    }

    public void setAmbient(Vector4f ambient) {
        this.ambient.set(ambient);
    }

    public final void reset() {
        ambient.set(.2f, .2f, .2f, 1f);
    }

    public void updateOpenGL(FloatBuffer lightFloatBuffer) {
        ambient.toFloatBuffer(lightFloatBuffer);
        lightFloatBuffer.flip();
        GL.o1.lightModel(LightModel.AMBIENT, lightFloatBuffer);
    }

}
