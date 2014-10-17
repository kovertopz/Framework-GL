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
import net.smert.frameworkgl.opengl.constants.Face;
import net.smert.frameworkgl.opengl.constants.Material;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class MaterialLight {

    private int face;
    private int shininess;
    private final Vector4f ambient;
    private final Vector4f diffuse;
    private final Vector4f emission;
    private final Vector4f specular;

    public MaterialLight() {
        ambient = new Vector4f();
        diffuse = new Vector4f();
        emission = new Vector4f();
        specular = new Vector4f();
        reset();
    }

    public int getFace() {
        return face;
    }

    public void setFace(int face) {
        this.face = face;
    }

    public int getShininess() {
        return shininess;
    }

    public void setShininess(int shininess) {
        this.shininess = shininess;
    }

    public Vector4f getAmbient() {
        return ambient;
    }

    public void setAmbient(Vector4f ambient) {
        this.ambient.set(ambient);
    }

    public Vector4f getDiffuse() {
        return diffuse;
    }

    public void setDiffuse(Vector4f diffuse) {
        this.diffuse.set(diffuse);
    }

    public Vector4f getEmission() {
        return emission;
    }

    public void setEmission(Vector4f emission) {
        this.emission.set(emission);
    }

    public Vector4f getSpecular() {
        return specular;
    }

    public void setSpecular(Vector4f specular) {
        this.specular.set(specular);
    }

    public final void reset() {
        face = Face.FRONT_AND_BACK;
        shininess = 0;
        ambient.set(.2f, .2f, .2f, 1f);
        diffuse.set(.8f, .8f, .8f, 1f);
        emission.set(0f, 0f, 0f, 1f);
        specular.set(0f, 0f, 0f, 1f);
    }

    public void updateOpenGL(FloatBuffer lightFloatBuffer) {
        GL.o1.material(face, Material.SHININESS, shininess);
        ambient.toFloatBuffer(lightFloatBuffer);
        lightFloatBuffer.flip();
        GL.o1.material(face, Material.AMBIENT, lightFloatBuffer);
        diffuse.toFloatBuffer(lightFloatBuffer);
        lightFloatBuffer.flip();
        GL.o1.material(face, Material.DIFFUSE, lightFloatBuffer);
        emission.toFloatBuffer(lightFloatBuffer);
        lightFloatBuffer.flip();
        GL.o1.material(face, Material.EMISSION, lightFloatBuffer);
        specular.toFloatBuffer(lightFloatBuffer);
        lightFloatBuffer.flip();
        GL.o1.material(face, Material.SPECULAR, lightFloatBuffer);
    }

}
