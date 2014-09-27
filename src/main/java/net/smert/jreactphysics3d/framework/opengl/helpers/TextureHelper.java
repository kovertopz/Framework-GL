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
package net.smert.jreactphysics3d.framework.opengl.helpers;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import net.smert.jreactphysics3d.framework.opengl.constants.TextureTargets;
import net.smert.jreactphysics3d.framework.opengl.constants.TextureTypes;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.glu.GLU;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class TextureHelper {

    private int textureTarget = TextureTargets.TEXTURE_2D;

    public void activeTexture(int textureunit) {
        GL13.glActiveTexture(textureunit);
    }

    public void bind(int textureid) {
        GL11.glBindTexture(textureTarget, textureid);
    }

    public int create() {
        return GL11.glGenTextures();
    }

    public void create(IntBuffer textureids) {
        GL11.glGenTextures(textureids);
    }

    public void delete(int textureid) {
        GL11.glDeleteTextures(textureid);
    }

    public void generateMipmap() {
        GL30.glGenerateMipmap(textureTarget);
    }

    public void setByteMipmaps(int internalformat, int width, int height, int format, ByteBuffer pixeldata) {
        GLU.gluBuild2DMipmaps(textureTarget, internalformat, width, height, format, TextureTypes.UNSIGNED_BYTE, pixeldata);
    }

    public void setClamping(int wrapr, int wraps, int wrapt) {
        GL11.glTexParameteri(textureTarget, GL11.GL_TEXTURE_WRAP_S, wrapr);
        GL11.glTexParameteri(textureTarget, GL11.GL_TEXTURE_WRAP_S, wraps);
        GL11.glTexParameteri(textureTarget, GL11.GL_TEXTURE_WRAP_T, wrapt);
    }

    public void setCompareFunction(int comparefunc) {
        GL11.glTexParameteri(textureTarget, GL14.GL_TEXTURE_COMPARE_FUNC, comparefunc);
    }

    public void setCompareMode(int comparemode) {
        GL11.glTexParameteri(textureTarget, GL14.GL_TEXTURE_COMPARE_MODE, comparemode);
    }

    public void setFilters(int magfilter, int minfilter) {
        GL11.glTexParameteri(textureTarget, GL11.GL_TEXTURE_MAG_FILTER, magfilter);
        GL11.glTexParameteri(textureTarget, GL11.GL_TEXTURE_MIN_FILTER, minfilter);
    }

    public void setImage2DBytePixelData(int internalformat, int width, int height, int format, ByteBuffer pixeldata) {
        GL11.glTexImage2D(textureTarget, 0, internalformat, width, height, 0, format, TextureTypes.UNSIGNED_BYTE, pixeldata);
    }

    public void setImage2DFloatPixelData(int internalformat, int width, int height, int format, FloatBuffer pixeldata) {
        GL11.glTexImage2D(textureTarget, 0, internalformat, width, height, 0, format, TextureTypes.FLOAT, pixeldata);
    }

    public void setImage2DInt1010102PixelData(int internalformat, int width, int height, int format, IntBuffer pixeldata) {
        GL11.glTexImage2D(textureTarget, 0, internalformat, width, height, 0, format, TextureTypes.UNSIGNED_INT_10_10_10_2, pixeldata);
    }

    public void setImage2DIntPixelData(int internalformat, int width, int height, int format, IntBuffer pixeldata) {
        GL11.glTexImage2D(textureTarget, 0, internalformat, width, height, 0, format, TextureTypes.UNSIGNED_INT, pixeldata);
    }

    public void setImage2DShortPixelData(int internalformat, int width, int height, int format, ShortBuffer pixeldata) {
        GL11.glTexImage2D(textureTarget, 0, internalformat, width, height, 0, format, TextureTypes.UNSIGNED_SHORT, pixeldata);
    }

    public void setImage2DStencilPixelData(int internalformat, int width, int height, int format, IntBuffer pixeldata) {
        GL11.glTexImage2D(textureTarget, 0, internalformat, width, height, 0, format, TextureTypes.UNSIGNED_INT_24_8, pixeldata);
    }

    public void setImage3DBytePixelData(int internalformat, int width, int height, int depth, int format, ByteBuffer pixeldata) {
        GL12.glTexImage3D(textureTarget, 0, internalformat, width, height, depth, 0, format, TextureTypes.UNSIGNED_BYTE, pixeldata);
    }

    public void setImage3DFloatPixelData(int internalformat, int width, int height, int depth, int format, FloatBuffer pixeldata) {
        GL12.glTexImage3D(textureTarget, 0, internalformat, width, height, depth, 0, format, TextureTypes.FLOAT, pixeldata);
    }

    public void setImage3DInt1010102PixelData(int internalformat, int width, int height, int depth, int format, IntBuffer pixeldata) {
        GL12.glTexImage3D(textureTarget, 0, internalformat, width, height, depth, 0, format, TextureTypes.UNSIGNED_INT_10_10_10_2, pixeldata);
    }

    public void setImage3DIntPixelData(int internalformat, int width, int height, int depth, int format, IntBuffer pixeldata) {
        GL12.glTexImage3D(textureTarget, 0, internalformat, width, height, depth, 0, format, TextureTypes.UNSIGNED_INT, pixeldata);
    }

    public void setImage3DShortPixelData(int internalformat, int width, int height, int depth, int format, ShortBuffer pixeldata) {
        GL12.glTexImage3D(textureTarget, 0, internalformat, width, height, depth, 0, format, TextureTypes.UNSIGNED_SHORT, pixeldata);
    }

    public void setImage3DStencilPixelData(int internalformat, int width, int height, int depth, int format, IntBuffer pixeldata) {
        GL12.glTexImage3D(textureTarget, 0, internalformat, width, height, depth, 0, format, TextureTypes.UNSIGNED_INT_24_8, pixeldata);
    }

    public void setTextureTarget2D() {
        textureTarget = TextureTargets.TEXTURE_2D;
    }

    public void setTextureTarget3D() {
        textureTarget = TextureTargets.TEXTURE_3D;
    }

    public void setTextureTargetCubeMap() {
        textureTarget = TextureTargets.TEXTURE_CUBE_MAP;
    }

    public void setTextureTargetCubeMapNegativeX() {
        textureTarget = TextureTargets.TEXTURE_CUBE_MAP_NEGATIVE_X;
    }

    public void setTextureTargetCubeMapPositiveX() {
        textureTarget = TextureTargets.TEXTURE_CUBE_MAP_POSITIVE_X;
    }

    public void setTextureTargetCubeMapNegativeY() {
        textureTarget = TextureTargets.TEXTURE_CUBE_MAP_NEGATIVE_Y;
    }

    public void setTextureTargetCubeMapPositiveY() {
        textureTarget = TextureTargets.TEXTURE_CUBE_MAP_POSITIVE_Y;
    }

    public void setTextureTargetCubeMapNegativeZ() {
        textureTarget = TextureTargets.TEXTURE_CUBE_MAP_NEGATIVE_Z;
    }

    public void setTextureTargetCubeMapPositiveZ() {
        textureTarget = TextureTargets.TEXTURE_CUBE_MAP_POSITIVE_Z;
    }

    public void unbind() {
        GL11.glBindTexture(textureTarget, 0);
    }

}
