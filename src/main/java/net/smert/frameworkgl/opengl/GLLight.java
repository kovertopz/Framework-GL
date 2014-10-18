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
import net.smert.frameworkgl.math.MathHelper;
import net.smert.frameworkgl.math.Vector4f;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class GLLight {

    private boolean enabled;
    private boolean shadowCasting;
    private float constantAttenuation;
    private float linearAttenuation;
    private float quadraticAttenuation;
    private float radius;
    private float spotInnerCutoff;
    private float spotOuterCutoff;
    private float spotInnerCutoffCos;
    private float spotOuterCutoffCos;
    private int lightNumber;
    private int spotExponent;
    private final Vector4f ambient;
    private final Vector4f diffuse;
    private final Vector4f position;
    private final Vector4f specular;
    private final Vector4f spotDirection;

    public GLLight() {
        ambient = new Vector4f();
        diffuse = new Vector4f();
        position = new Vector4f();
        specular = new Vector4f();
        spotDirection = new Vector4f();
        reset();
    }

    public float getConstantAttenuation() {
        return constantAttenuation;
    }

    public void setConstantAttenuation(float constantAttenuation) {
        this.constantAttenuation = constantAttenuation;
    }

    public float getLinearAttenuation() {
        return linearAttenuation;
    }

    public void setLinearAttenuation(float linearAttenuation) {
        this.linearAttenuation = linearAttenuation;
    }

    public float getQuadraticAttenuation() {
        return quadraticAttenuation;
    }

    public void setQuadraticAttenuation(float quadraticAttenuation) {
        this.quadraticAttenuation = quadraticAttenuation;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        if (radius < 0.01f) {
            radius = 0.01f; // 10CM
        }
        this.radius = radius;
    }

    public float getSpotCutoff() {
        return spotOuterCutoff;
    }

    public void setSpotCutoff(float spotCutoff) {
        setSpotOuterCutoff(spotCutoff);
    }

    public float getSpotInnerCutoff() {
        return spotInnerCutoff;
    }

    public void setSpotInnerCutoff(float spotInnerCutoff) {
        this.spotInnerCutoff = spotInnerCutoff;
        spotInnerCutoffCos = MathHelper.Cos(spotInnerCutoff * MathHelper.DEG_TO_RAD);
    }

    public float getSpotOuterCutoff() {
        return spotOuterCutoff;
    }

    public void setSpotOuterCutoff(float spotOuterCutoff) {
        this.spotOuterCutoff = spotOuterCutoff;
        spotOuterCutoffCos = MathHelper.Cos(spotOuterCutoff * MathHelper.DEG_TO_RAD);
    }

    public float getSpotInnerCutoffCos() {
        return spotInnerCutoffCos;
    }

    public float getSpotOuterCutoffCos() {
        return spotOuterCutoffCos;
    }

    public int getLightNumber() {
        return lightNumber;
    }

    public void setLightNumber(int lightNumber) {
        this.lightNumber = lightNumber;
    }

    public int getSpotExponent() {
        return spotExponent;
    }

    public void setSpotExponent(int spotExponent) {
        this.spotExponent = spotExponent;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isShadowCasting() {
        return shadowCasting;
    }

    public void setShadowCasting(boolean shadowCasting) {
        this.shadowCasting = shadowCasting;
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

    public Vector4f getPosition() {
        return position;
    }

    public void setPosition(Vector4f position) {
        this.position.set(position);
    }

    public Vector4f getSpecular() {
        return specular;
    }

    public void setSpecular(Vector4f specular) {
        this.specular.set(specular);
    }

    public Vector4f getSpotDirection() {
        return spotDirection;
    }

    public void setSpotDirection(Vector4f spotDirection) {
        this.spotDirection.set(spotDirection);
    }

    public final void reset() {
        enabled = true;
        shadowCasting = false;
        constantAttenuation = 1f;
        linearAttenuation = 0f;
        quadraticAttenuation = 0f;
        radius = 0f;
        spotInnerCutoff = 180f;
        spotOuterCutoff = 180f;
        spotInnerCutoffCos = MathHelper.Cos(spotInnerCutoff * MathHelper.DEG_TO_RAD);
        spotOuterCutoffCos = MathHelper.Cos(spotOuterCutoff * MathHelper.DEG_TO_RAD);
        lightNumber = net.smert.frameworkgl.opengl.constants.Light.LIGHT0;
        spotExponent = 0;
        ambient.set(0f, 0f, 0f, 1f);
        diffuse.set(1f, 1f, 1f, 1f);
        position.set(0f, 0f, 1f, 0f);
        specular.set(1f, 1f, 1f, 1f);
        spotDirection.set(0f, 0f, -1f, 0f);
    }

    public void update() {
    }

    public void updateOpenGL(FloatBuffer lightFloatBuffer) {
        GL.o1.light(lightNumber, net.smert.frameworkgl.opengl.constants.Light.CONSTANT_ATTENUATION, constantAttenuation);
        GL.o1.light(lightNumber, net.smert.frameworkgl.opengl.constants.Light.LINEAR_ATTENUATION, linearAttenuation);
        GL.o1.light(lightNumber, net.smert.frameworkgl.opengl.constants.Light.QUADRATIC_ATTENUATION, quadraticAttenuation);
        GL.o1.light(lightNumber, net.smert.frameworkgl.opengl.constants.Light.SPOT_CUTOFF, spotOuterCutoff);
        GL.o1.light(lightNumber, net.smert.frameworkgl.opengl.constants.Light.SPOT_EXPONENT, spotExponent);
        ambient.toFloatBuffer(lightFloatBuffer);
        lightFloatBuffer.flip();
        GL.o1.light(lightNumber, net.smert.frameworkgl.opengl.constants.Light.AMBIENT, lightFloatBuffer);
        diffuse.toFloatBuffer(lightFloatBuffer);
        lightFloatBuffer.flip();
        GL.o1.light(lightNumber, net.smert.frameworkgl.opengl.constants.Light.DIFFUSE, lightFloatBuffer);
        position.toFloatBuffer(lightFloatBuffer);
        lightFloatBuffer.flip();
        GL.o1.light(lightNumber, net.smert.frameworkgl.opengl.constants.Light.POSITION, lightFloatBuffer);
        specular.toFloatBuffer(lightFloatBuffer);
        lightFloatBuffer.flip();
        GL.o1.light(lightNumber, net.smert.frameworkgl.opengl.constants.Light.SPECULAR, lightFloatBuffer);
        spotDirection.toFloatBuffer(lightFloatBuffer);
        lightFloatBuffer.flip();
        GL.o1.light(lightNumber, net.smert.frameworkgl.opengl.constants.Light.SPOT_DIRECTION, lightFloatBuffer);
    }

}
