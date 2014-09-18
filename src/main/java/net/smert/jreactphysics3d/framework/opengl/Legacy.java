package net.smert.jreactphysics3d.framework.opengl;

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
public class Legacy {

    private boolean wireframeMode = false;
    private float defaultLineWidth = 1.0f;
    private int clearBits = ClearBits.COLOR_BUFFER_BIT | ClearBits.DEPTH_BUFFER_BIT;

    public Legacy begin(int primitive) {
        GL11.glBegin(primitive);
        return this;
    }

    public Legacy clear() {
        GL11.glClear(clearBits);
        return this;
    }

    public Legacy clearColorBuffer() {
        GL11.glClear(ClearBits.COLOR_BUFFER_BIT);
        return this;
    }

    public Legacy clearDepthBuffer() {
        GL11.glClear(ClearBits.DEPTH_BUFFER_BIT);
        return this;
    }

    public Legacy clearStencilBuffer() {
        GL11.glClear(ClearBits.STENCIL_BUFFER_BIT);
        return this;
    }

    public Legacy color(float r, float g, float b, float a) {
        GL11.glColor4f(r, g, b, a);
        return this;
    }

    public Legacy cullBackFaces() {
        GL11.glCullFace(GL11.GL_BACK);
        return this;
    }

    public Legacy cullFrontFaces() {
        GL11.glCullFace(GL11.GL_FRONT);
        return this;
    }

    public Legacy disableBlending() {
        GL11.glDisable(GL11.GL_BLEND);
        return this;
    }

    public Legacy disableColorMask() {
        GL11.glColorMask(false, false, false, false);
        return this;
    }

    public Legacy disableColorMaterial() {
        GL11.glDisable(GL11.GL_COLOR_MATERIAL);
        return this;
    }

    public Legacy disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
        return this;
    }

    public Legacy disableDepthMask() {
        GL11.glDepthMask(false);
        return this;
    }

    public Legacy disableDepthTest() {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        return this;
    }

    public Legacy disableLight0() {
        GL11.glDisable(GL11.GL_LIGHT0);
        return this;
    }

    public Legacy disableLight1() {
        GL11.glDisable(GL11.GL_LIGHT1);
        return this;
    }

    public Legacy disableLight2() {
        GL11.glDisable(GL11.GL_LIGHT2);
        return this;
    }

    public Legacy disableLight3() {
        GL11.glDisable(GL11.GL_LIGHT3);
        return this;
    }

    public Legacy disableLight4() {
        GL11.glDisable(GL11.GL_LIGHT4);
        return this;
    }

    public Legacy disableLight5() {
        GL11.glDisable(GL11.GL_LIGHT5);
        return this;
    }

    public Legacy disableLight6() {
        GL11.glDisable(GL11.GL_LIGHT6);
        return this;
    }

    public Legacy disableLight7() {
        GL11.glDisable(GL11.GL_LIGHT7);
        return this;
    }

    public Legacy disableLighting() {
        GL11.glDisable(GL11.GL_LIGHTING);
        return this;
    }

    public Legacy disableLinePolygonFillMode() {
        GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_FILL);
        return this;
    }

    public Legacy disableStencilTest() {
        GL11.glDisable(GL11.GL_STENCIL_TEST);
        return this;
    }

    public Legacy disableTexture2D() {
        GL11.glDisable(TextureTargets.TEXTURE_2D);
        return this;
    }

    public Legacy disableTexture3D() {
        GL11.glDisable(TextureTargets.TEXTURE_3D);
        return this;
    }

    public Legacy disableTextureCubeMap() {
        GL11.glDisable(TextureTargets.TEXTURE_CUBE_MAP);
        return this;
    }

    public Legacy disableWireframe() {
        wireframeMode = false;
        return this;
    }

    public Legacy enableBlending() {
        GL11.glEnable(GL11.GL_BLEND);
        return this;
    }

    public Legacy enableColorMask() {
        GL11.glColorMask(true, true, true, true);
        return this;
    }

    public Legacy enableColorMaterial() {
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        return this;
    }

    public Legacy enableCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        return this;
    }

    public Legacy enableDepthMask() {
        GL11.glDepthMask(true);
        return this;
    }

    public Legacy enableDepthTest() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        return this;
    }

    public Legacy enableLight0() {
        GL11.glEnable(GL11.GL_LIGHT0);
        return this;
    }

    public Legacy enableLight1() {
        GL11.glEnable(GL11.GL_LIGHT1);
        return this;
    }

    public Legacy enableLight2() {
        GL11.glEnable(GL11.GL_LIGHT2);
        return this;
    }

    public Legacy enableLight3() {
        GL11.glEnable(GL11.GL_LIGHT3);
        return this;
    }

    public Legacy enableLight4() {
        GL11.glEnable(GL11.GL_LIGHT4);
        return this;
    }

    public Legacy enableLight5() {
        GL11.glEnable(GL11.GL_LIGHT5);
        return this;
    }

    public Legacy enableLight6() {
        GL11.glEnable(GL11.GL_LIGHT6);
        return this;
    }

    public Legacy enableLight7() {
        GL11.glEnable(GL11.GL_LIGHT7);
        return this;
    }

    public Legacy enableLighting() {
        GL11.glEnable(GL11.GL_LIGHTING);
        return this;
    }

    public Legacy enableLinePolygonFillMode() {
        GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_LINE);
        return this;
    }

    public Legacy enableStencilTest() {
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        return this;
    }

    public Legacy enableTexture2D() {
        GL11.glEnable(TextureTargets.TEXTURE_2D);
        return this;
    }

    public Legacy enableTexture3D() {
        GL11.glEnable(TextureTargets.TEXTURE_3D);
        return this;
    }

    public Legacy enableTextureCubeMap() {
        GL11.glEnable(TextureTargets.TEXTURE_CUBE_MAP);
        return this;
    }

    public Legacy enableWireframe() {
        wireframeMode = true;
        return this;
    }

    public Legacy end() {
        GL11.glEnd();
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

    public Legacy normal(float x, float y, float z) {
        GL11.glNormal3f(x, y, z);
        return this;
    }

    public Legacy popMatrix() {
        GL11.glPopMatrix();
        return this;
    }

    public Legacy pushMatrix() {
        GL11.glPushMatrix();
        return this;
    }

    public Legacy rotate(float angle, float x, float y, float z) {
        GL11.glRotatef(angle, x, y, z);
        return this;
    }

    public Legacy setBlendingFunctionOneAndOne() {
        GL11.glBlendFunc(BlendFunctions.ONE, BlendFunctions.ONE);
        return this;
    }

    public Legacy setBlendingFunctionSrcAlphaAndOneMinusSrcAlpha() {
        GL11.glBlendFunc(BlendFunctions.SRC_ALPHA, BlendFunctions.ONE_MINUS_SRC_ALPHA);
        return this;
    }

    public Legacy setBlendingEquationAdd() {
        GL14.glBlendEquation(BlendEquations.ADD);
        return this;
    }

    public Legacy setBlendingEquationMax() {
        GL14.glBlendEquation(BlendEquations.MAX);
        return this;
    }

    public Legacy setBlendingEquationMin() {
        GL14.glBlendEquation(BlendEquations.MIN);
        return this;
    }

    public Legacy setBlendingEquationReverseSubtract() {
        GL14.glBlendEquation(BlendEquations.REVERSE_SUBTRACT);
        return this;
    }

    public Legacy setBlendingEquationSubtract() {
        GL14.glBlendEquation(BlendEquations.SUBTRACT);
        return this;
    }

    public Legacy setClearBits(int clearbits) {
        clearBits = clearbits;
        return this;
    }

    public Legacy setClearColor(float red, float green, float blue, float alpha) {
        GL11.glClearColor(red, green, blue, alpha);
        return this;
    }

    public Legacy setClearDepth(double depth) {
        GL11.glClearDepth(depth);
        return this;
    }

    public Legacy setClearStencil(int s) {
        GL11.glClearStencil(s);
        return this;
    }

    public Legacy setColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
        GL11.glColorMask(red, green, blue, alpha);
        return this;
    }

    public Legacy setCullBackFace() {
        GL11.glCullFace(GL11.GL_BACK);
        return this;
    }

    public Legacy setCullFrontFace() {
        GL11.glCullFace(GL11.GL_FRONT);
        return this;
    }

    public Legacy setDefaultLineWidth(float linewidth) {
        defaultLineWidth = linewidth;
        return this;
    }

    public Legacy setDepthFuncAlways() {
        GL11.glDepthFunc(DepthFunctions.ALWAYS);
        return this;
    }

    public Legacy setDepthFuncEqual() {
        GL11.glDepthFunc(DepthFunctions.EQUAL);
        return this;
    }

    public Legacy setDepthFuncGreater() {
        GL11.glDepthFunc(DepthFunctions.GREATER);
        return this;
    }

    public Legacy setDepthFuncGreaterEqual() {
        GL11.glDepthFunc(DepthFunctions.GEQUAL);
        return this;
    }

    public Legacy setDepthFuncLess() {
        GL11.glDepthFunc(DepthFunctions.LESS);
        return this;
    }

    public Legacy setDepthFuncLessEqual() {
        GL11.glDepthFunc(DepthFunctions.LEQUAL);
        return this;
    }

    public Legacy setDepthFuncNever() {
        GL11.glDepthFunc(DepthFunctions.NEVER);
        return this;
    }

    public Legacy setDepthFuncNotEqual() {
        GL11.glDepthFunc(DepthFunctions.NOTEQUAL);
        return this;
    }

    public Legacy setFrontFaceCCW() {
        GL11.glFrontFace(GL11.GL_CCW);
        return this;
    }

    public Legacy setFrontFaceCW() {
        GL11.glFrontFace(GL11.GL_CW);
        return this;
    }

    public Legacy setLineWidth(float linewidth) {
        GL11.glLineWidth(linewidth);
        return this;
    }

    public Legacy setModelViewIdentity() {
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        return this;
    }

    public Legacy setPolygonModeBackFill() {
        GL11.glPolygonMode(GL11.GL_BACK, GL11.GL_FILL);
        return this;
    }

    public Legacy setPolygonModeBackLine() {
        GL11.glPolygonMode(GL11.GL_BACK, GL11.GL_LINE);
        return this;
    }

    public Legacy setPolygonModeFrontAndBackFill() {
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        return this;
    }

    public Legacy setPolygonModeFrontAndBackLine() {
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        return this;
    }

    public Legacy setPolygonModeFrontFill() {
        GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_FILL);
        return this;
    }

    public Legacy setPolygonModeFrontLine() {
        GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_LINE);
        return this;
    }

    public Legacy setProjectionOrtho(double left, double right, double bottom, double top, double znear, double zfar) {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glOrtho(left, right, bottom, top, znear, zfar);
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
        return this;
    }

    public Legacy setProjectionPerspective(float fovy, float aspectratio, float znear, float zfar) {
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

    public Legacy setSmoothLighting(boolean isSmooth) {
        if (isSmooth) {
            GL11.glShadeModel(GL11.GL_SMOOTH);
        } else {
            GL11.glShadeModel(GL11.GL_FLAT);
        }
        return this;
    }

    public Legacy setStencilFuncAlways(int ref, int mask) {
        GL11.glStencilFunc(StencilFunctions.ALWAYS, ref, mask);
        return this;
    }

    public Legacy setStencilFuncEqual(int ref, int mask) {
        GL11.glStencilFunc(StencilFunctions.EQUAL, ref, mask);
        return this;
    }

    public Legacy setStencilFuncGreater(int ref, int mask) {
        GL11.glStencilFunc(StencilFunctions.GREATER, ref, mask);
        return this;
    }

    public Legacy setStencilFuncGreaterEqual(int ref, int mask) {
        GL11.glStencilFunc(StencilFunctions.GEQUAL, ref, mask);
        return this;
    }

    public Legacy setStencilFuncLess(int ref, int mask) {
        GL11.glStencilFunc(StencilFunctions.LESS, ref, mask);
        return this;
    }

    public Legacy setStencilFuncLessEqual(int ref, int mask) {
        GL11.glStencilFunc(StencilFunctions.LEQUAL, ref, mask);
        return this;
    }

    public Legacy setStencilFuncNever(int ref, int mask) {
        GL11.glStencilFunc(StencilFunctions.NEVER, ref, mask);
        return this;
    }

    public Legacy setStencilFuncNotEqual(int ref, int mask) {
        GL11.glStencilFunc(StencilFunctions.NOTEQUAL, ref, mask);
        return this;
    }

    public Legacy setViewport(int x, int y, int width, int height) {
        GL11.glViewport(x, y, width, height);
        return this;
    }

    public Legacy switchModelView() {
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        return this;
    }

    public Legacy switchPolygonFillMode() {
        if (wireframeMode == true) {
            GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_LINE);
        } else {
            GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_FILL);
        }
        return this;
    }

    public Legacy switchProjection() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        return this;
    }

    public Legacy texCoord(float s, float t) {
        GL11.glTexCoord2f(s, t);
        return this;
    }

    public Legacy translate(float x, float y, float z) {
        GL11.glTranslatef(x, y, z);
        return this;
    }

    public Legacy vertex(float x, float y, float z) {
        GL11.glVertex3f(x, y, z);
        return this;
    }

}
