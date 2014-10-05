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

import java.nio.FloatBuffer;
import net.smert.jreactphysics3d.framework.opengl.constants.BlendEquations;
import net.smert.jreactphysics3d.framework.opengl.constants.BlendFunctions;
import net.smert.jreactphysics3d.framework.opengl.constants.ClearBits;
import net.smert.jreactphysics3d.framework.opengl.constants.DepthFunctions;
import net.smert.jreactphysics3d.framework.opengl.constants.StencilFunctions;
import net.smert.jreactphysics3d.framework.opengl.constants.TextureTargets;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.util.glu.GLU;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class OpenGL1 {

    private boolean wireframeMode = false;
    private float defaultLineWidth = 1.0f;
    private int clearBits = ClearBits.COLOR_BUFFER_BIT | ClearBits.DEPTH_BUFFER_BIT;

    public OpenGL1 begin(int primitive) {
        GL.renderHelper.begin(primitive);
        return this;
    }

    public OpenGL1 clear() {
        GL11.glClear(clearBits);
        return this;
    }

    public OpenGL1 clearColorBuffer() {
        GL11.glClear(ClearBits.COLOR_BUFFER_BIT);
        return this;
    }

    public OpenGL1 clearDepthBuffer() {
        GL11.glClear(ClearBits.DEPTH_BUFFER_BIT);
        return this;
    }

    public OpenGL1 clearStencilBuffer() {
        GL11.glClear(ClearBits.STENCIL_BUFFER_BIT);
        return this;
    }

    public OpenGL1 color(float r, float g, float b) {
        GL.renderHelper.color(r, g, b);
        return this;
    }

    public OpenGL1 color(float r, float g, float b, float a) {
        GL.renderHelper.color(r, g, b, a);
        return this;
    }

    public OpenGL1 colorMaterial(int face, int material) {
        GL11.glColorMaterial(face, material);
        return this;
    }

    public OpenGL1 cullBackFaces() {
        GL11.glCullFace(GL11.GL_BACK);
        return this;
    }

    public OpenGL1 cullFrontFaces() {
        GL11.glCullFace(GL11.GL_FRONT);
        return this;
    }

    public OpenGL1 disableBlending() {
        GL11.glDisable(GL11.GL_BLEND);
        return this;
    }

    public OpenGL1 disableColorMask() {
        GL11.glColorMask(false, false, false, false);
        return this;
    }

    public OpenGL1 disableColorMaterial() {
        GL11.glDisable(GL11.GL_COLOR_MATERIAL);
        return this;
    }

    public OpenGL1 disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
        return this;
    }

    public OpenGL1 disableDepthMask() {
        GL11.glDepthMask(false);
        return this;
    }

    public OpenGL1 disableDepthTest() {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        return this;
    }

    public OpenGL1 disableLight0() {
        GL11.glDisable(GL11.GL_LIGHT0);
        return this;
    }

    public OpenGL1 disableLight1() {
        GL11.glDisable(GL11.GL_LIGHT1);
        return this;
    }

    public OpenGL1 disableLight2() {
        GL11.glDisable(GL11.GL_LIGHT2);
        return this;
    }

    public OpenGL1 disableLight3() {
        GL11.glDisable(GL11.GL_LIGHT3);
        return this;
    }

    public OpenGL1 disableLight4() {
        GL11.glDisable(GL11.GL_LIGHT4);
        return this;
    }

    public OpenGL1 disableLight5() {
        GL11.glDisable(GL11.GL_LIGHT5);
        return this;
    }

    public OpenGL1 disableLight6() {
        GL11.glDisable(GL11.GL_LIGHT6);
        return this;
    }

    public OpenGL1 disableLight7() {
        GL11.glDisable(GL11.GL_LIGHT7);
        return this;
    }

    public OpenGL1 disableLighting() {
        GL11.glDisable(GL11.GL_LIGHTING);
        return this;
    }

    public OpenGL1 disableLinePolygonFillMode() {
        GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_FILL);
        return this;
    }

    public OpenGL1 disableStencilTest() {
        GL11.glDisable(GL11.GL_STENCIL_TEST);
        return this;
    }

    public OpenGL1 disableTexture2D() {
        GL11.glDisable(TextureTargets.TEXTURE_2D);
        return this;
    }

    public OpenGL1 disableTexture3D() {
        GL11.glDisable(TextureTargets.TEXTURE_3D);
        return this;
    }

    public OpenGL1 disableTextureCubeMap() {
        GL11.glDisable(TextureTargets.TEXTURE_CUBE_MAP);
        return this;
    }

    public OpenGL1 disableWireframe() {
        wireframeMode = false;
        return this;
    }

    public OpenGL1 enableBlending() {
        GL11.glEnable(GL11.GL_BLEND);
        return this;
    }

    public OpenGL1 enableColorMask() {
        GL11.glColorMask(true, true, true, true);
        return this;
    }

    public OpenGL1 enableColorMaterial() {
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        return this;
    }

    public OpenGL1 enableCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        return this;
    }

    public OpenGL1 enableDepthMask() {
        GL11.glDepthMask(true);
        return this;
    }

    public OpenGL1 enableDepthTest() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        return this;
    }

    public OpenGL1 enableLight0() {
        GL11.glEnable(GL11.GL_LIGHT0);
        return this;
    }

    public OpenGL1 enableLight1() {
        GL11.glEnable(GL11.GL_LIGHT1);
        return this;
    }

    public OpenGL1 enableLight2() {
        GL11.glEnable(GL11.GL_LIGHT2);
        return this;
    }

    public OpenGL1 enableLight3() {
        GL11.glEnable(GL11.GL_LIGHT3);
        return this;
    }

    public OpenGL1 enableLight4() {
        GL11.glEnable(GL11.GL_LIGHT4);
        return this;
    }

    public OpenGL1 enableLight5() {
        GL11.glEnable(GL11.GL_LIGHT5);
        return this;
    }

    public OpenGL1 enableLight6() {
        GL11.glEnable(GL11.GL_LIGHT6);
        return this;
    }

    public OpenGL1 enableLight7() {
        GL11.glEnable(GL11.GL_LIGHT7);
        return this;
    }

    public OpenGL1 enableLighting() {
        GL11.glEnable(GL11.GL_LIGHTING);
        return this;
    }

    public OpenGL1 enableLinePolygonFillMode() {
        GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_LINE);
        return this;
    }

    public OpenGL1 enableStencilTest() {
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        return this;
    }

    public OpenGL1 enableTexture2D() {
        GL11.glEnable(TextureTargets.TEXTURE_2D);
        return this;
    }

    public OpenGL1 enableTexture3D() {
        GL11.glEnable(TextureTargets.TEXTURE_3D);
        return this;
    }

    public OpenGL1 enableTextureCubeMap() {
        GL11.glEnable(TextureTargets.TEXTURE_CUBE_MAP);
        return this;
    }

    public OpenGL1 enableWireframe() {
        wireframeMode = true;
        return this;
    }

    public OpenGL1 end() {
        GL.renderHelper.end();
        return this;
    }

    public float getDefaultLineWidth() {
        return defaultLineWidth;
    }

    public int getAvailableStencilBits() {
        return GL11.glGetInteger(GL11.GL_STENCIL_BITS);
    }

    public int getClearBits() {
        return clearBits;
    }

    public int getError() {
        return GL11.glGetError();
    }

    public String getString(int getstring) {
        return GL11.glGetString(getstring);
    }

    public OpenGL1 light(int lightnumber, int light, FloatBuffer fb) {
        GL11.glLight(lightnumber, light, fb);
        return this;
    }

    public OpenGL1 loadMatrix(FloatBuffer fb) {
        GL11.glLoadMatrix(fb);
        return this;
    }

    public OpenGL1 material(int face, int material, FloatBuffer fb) {
        GL11.glMaterial(face, material, fb);
        return this;
    }

    public OpenGL1 material(int face, int material, int value) {
        GL11.glMateriali(face, material, value);
        return this;
    }

    public OpenGL1 multiplyMatrix(FloatBuffer fb) {
        GL11.glMultMatrix(fb);
        return this;
    }

    public OpenGL1 normal(float x, float y, float z) {
        GL.renderHelper.normal(x, y, z);
        return this;
    }

    public OpenGL1 popMatrix() {
        GL11.glPopMatrix();
        return this;
    }

    public OpenGL1 pushMatrix() {
        GL11.glPushMatrix();
        return this;
    }

    public OpenGL1 rotate(float angle, float x, float y, float z) {
        GL11.glRotatef(angle, x, y, z);
        return this;
    }

    public OpenGL1 setBlendingFunctionOneAndOne() {
        GL11.glBlendFunc(BlendFunctions.ONE, BlendFunctions.ONE);
        return this;
    }

    public OpenGL1 setBlendingFunctionSrcAlphaAndOneMinusSrcAlpha() {
        GL11.glBlendFunc(BlendFunctions.SRC_ALPHA, BlendFunctions.ONE_MINUS_SRC_ALPHA);
        return this;
    }

    public OpenGL1 setBlendingEquationAdd() {
        GL14.glBlendEquation(BlendEquations.ADD);
        return this;
    }

    public OpenGL1 setBlendingEquationMax() {
        GL14.glBlendEquation(BlendEquations.MAX);
        return this;
    }

    public OpenGL1 setBlendingEquationMin() {
        GL14.glBlendEquation(BlendEquations.MIN);
        return this;
    }

    public OpenGL1 setBlendingEquationReverseSubtract() {
        GL14.glBlendEquation(BlendEquations.REVERSE_SUBTRACT);
        return this;
    }

    public OpenGL1 setBlendingEquationSubtract() {
        GL14.glBlendEquation(BlendEquations.SUBTRACT);
        return this;
    }

    public OpenGL1 setClearBits(int clearbits) {
        clearBits = clearbits;
        return this;
    }

    public OpenGL1 setClearColor(float red, float green, float blue, float alpha) {
        GL11.glClearColor(red, green, blue, alpha);
        return this;
    }

    public OpenGL1 setClearDepth(double depth) {
        GL11.glClearDepth(depth);
        return this;
    }

    public OpenGL1 setClearStencil(int index) {
        GL11.glClearStencil(index);
        return this;
    }

    public OpenGL1 setColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
        GL11.glColorMask(red, green, blue, alpha);
        return this;
    }

    public OpenGL1 setCullBackFace() {
        GL11.glCullFace(GL11.GL_BACK);
        return this;
    }

    public OpenGL1 setCullFrontFace() {
        GL11.glCullFace(GL11.GL_FRONT);
        return this;
    }

    public OpenGL1 setDefaultLineWidth(float linewidth) {
        defaultLineWidth = linewidth;
        return this;
    }

    public OpenGL1 setDepthFuncAlways() {
        GL11.glDepthFunc(DepthFunctions.ALWAYS);
        return this;
    }

    public OpenGL1 setDepthFuncEqual() {
        GL11.glDepthFunc(DepthFunctions.EQUAL);
        return this;
    }

    public OpenGL1 setDepthFuncGreater() {
        GL11.glDepthFunc(DepthFunctions.GREATER);
        return this;
    }

    public OpenGL1 setDepthFuncGreaterEqual() {
        GL11.glDepthFunc(DepthFunctions.GEQUAL);
        return this;
    }

    public OpenGL1 setDepthFuncLess() {
        GL11.glDepthFunc(DepthFunctions.LESS);
        return this;
    }

    public OpenGL1 setDepthFuncLessEqual() {
        GL11.glDepthFunc(DepthFunctions.LEQUAL);
        return this;
    }

    public OpenGL1 setDepthFuncNever() {
        GL11.glDepthFunc(DepthFunctions.NEVER);
        return this;
    }

    public OpenGL1 setDepthFuncNotEqual() {
        GL11.glDepthFunc(DepthFunctions.NOTEQUAL);
        return this;
    }

    public OpenGL1 setFrontFaceCCW() {
        GL11.glFrontFace(GL11.GL_CCW);
        return this;
    }

    public OpenGL1 setFrontFaceCW() {
        GL11.glFrontFace(GL11.GL_CW);
        return this;
    }

    public OpenGL1 setLineWidth(float linewidth) {
        GL11.glLineWidth(linewidth);
        return this;
    }

    public OpenGL1 setModelViewIdentity() {
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        return this;
    }

    public OpenGL1 setPolygonModeBackFill() {
        GL11.glPolygonMode(GL11.GL_BACK, GL11.GL_FILL);
        return this;
    }

    public OpenGL1 setPolygonModeBackLine() {
        GL11.glPolygonMode(GL11.GL_BACK, GL11.GL_LINE);
        return this;
    }

    public OpenGL1 setPolygonModeFrontAndBackFill() {
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        return this;
    }

    public OpenGL1 setPolygonModeFrontAndBackLine() {
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        return this;
    }

    public OpenGL1 setPolygonModeFrontFill() {
        GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_FILL);
        return this;
    }

    public OpenGL1 setPolygonModeFrontLine() {
        GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_LINE);
        return this;
    }

    public OpenGL1 setProjectionOrtho(double left, double right, double bottom, double top, double znear, double zfar) {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glOrtho(left, right, bottom, top, znear, zfar);
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
        return this;
    }

    public OpenGL1 setProjectionPerspective(float fovy, float aspectratio, float znear, float zfar) {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(
                fovy,
                aspectratio,
                znear,
                zfar);
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
        return this;
    }

    public OpenGL1 setSmoothLighting(boolean isSmooth) {
        if (isSmooth) {
            GL11.glShadeModel(GL11.GL_SMOOTH);
        } else {
            GL11.glShadeModel(GL11.GL_FLAT);
        }
        return this;
    }

    public OpenGL1 setStencilFuncAlways(int ref, int mask) {
        GL11.glStencilFunc(StencilFunctions.ALWAYS, ref, mask);
        return this;
    }

    public OpenGL1 setStencilFuncEqual(int ref, int mask) {
        GL11.glStencilFunc(StencilFunctions.EQUAL, ref, mask);
        return this;
    }

    public OpenGL1 setStencilFuncGreater(int ref, int mask) {
        GL11.glStencilFunc(StencilFunctions.GREATER, ref, mask);
        return this;
    }

    public OpenGL1 setStencilFuncGreaterEqual(int ref, int mask) {
        GL11.glStencilFunc(StencilFunctions.GEQUAL, ref, mask);
        return this;
    }

    public OpenGL1 setStencilFuncLess(int ref, int mask) {
        GL11.glStencilFunc(StencilFunctions.LESS, ref, mask);
        return this;
    }

    public OpenGL1 setStencilFuncLessEqual(int ref, int mask) {
        GL11.glStencilFunc(StencilFunctions.LEQUAL, ref, mask);
        return this;
    }

    public OpenGL1 setStencilFuncNever(int ref, int mask) {
        GL11.glStencilFunc(StencilFunctions.NEVER, ref, mask);
        return this;
    }

    public OpenGL1 setStencilFuncNotEqual(int ref, int mask) {
        GL11.glStencilFunc(StencilFunctions.NOTEQUAL, ref, mask);
        return this;
    }

    public OpenGL1 setViewport(int x, int y, int width, int height) {
        GL11.glViewport(x, y, width, height);
        return this;
    }

    public OpenGL1 switchModelView() {
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        return this;
    }

    public OpenGL1 switchPolygonFillMode() {
        if (wireframeMode) {
            GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_LINE);
        } else {
            GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_FILL);
        }
        return this;
    }

    public OpenGL1 switchProjection() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        return this;
    }

    public OpenGL1 texCoord(float s, float t) {
        GL.renderHelper.texCoord(s, t);
        return this;
    }

    public OpenGL1 texCoord(float s, float t, float r) {
        GL.renderHelper.texCoord(s, t, r);
        return this;
    }

    public OpenGL1 translate(float x, float y, float z) {
        GL11.glTranslatef(x, y, z);
        return this;
    }

    public OpenGL1 vertex(float x, float y, float z) {
        GL.renderHelper.vertex(x, y, z);
        return this;
    }

    public OpenGL1 vertex(float x, float y, float z, float w) {
        GL.renderHelper.vertex(x, y, z, w);
        return this;
    }

}
