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
package net.smert.frameworkgl.opengl.helpers;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import net.smert.frameworkgl.opengl.constants.TextureTargets;
import net.smert.frameworkgl.opengl.constants.TextureTypes;
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

    public void bind(int textureID) {
        GL11.glBindTexture(textureTarget, textureID);
    }

    public int create() {
        return GL11.glGenTextures();
    }

    public void create(IntBuffer textureIDs) {
        GL11.glGenTextures(textureIDs);
    }

    public void delete(int textureID) {
        GL11.glDeleteTextures(textureID);
    }

    public void generateMipmap() {
        GL30.glGenerateMipmap(textureTarget);
    }

    public void setByteMipmaps(int internalFormat, int width, int height, int format, ByteBuffer pixelData) {
        GLU.gluBuild2DMipmaps(textureTarget, internalFormat, width, height, format, TextureTypes.UNSIGNED_BYTE, pixelData);
    }

    public void setClamping(int wrapR, int wrapS, int wrapT) {
        GL11.glTexParameteri(textureTarget, GL12.GL_TEXTURE_WRAP_R, wrapR);
        GL11.glTexParameteri(textureTarget, GL11.GL_TEXTURE_WRAP_S, wrapS);
        GL11.glTexParameteri(textureTarget, GL11.GL_TEXTURE_WRAP_T, wrapT);
    }

    public void setCompareFunction(int compareFunc) {
        GL11.glTexParameteri(textureTarget, GL14.GL_TEXTURE_COMPARE_FUNC, compareFunc);
    }

    public void setCompareMode(int compareMode) {
        GL11.glTexParameteri(textureTarget, GL14.GL_TEXTURE_COMPARE_MODE, compareMode);
    }

    public void setFilters(int magFilter, int minFilter) {
        GL11.glTexParameteri(textureTarget, GL11.GL_TEXTURE_MAG_FILTER, magFilter);
        GL11.glTexParameteri(textureTarget, GL11.GL_TEXTURE_MIN_FILTER, minFilter);
    }

    public void setImage2DBytePixelData(int internalFormat, int width, int height, int format, ByteBuffer pixelData) {
        GL11.glTexImage2D(textureTarget, 0, internalFormat, width, height, 0, format, TextureTypes.UNSIGNED_BYTE, pixelData);
    }

    public void setImage2DFloatPixelData(int internalFormat, int width, int height, int format, FloatBuffer pixelData) {
        GL11.glTexImage2D(textureTarget, 0, internalFormat, width, height, 0, format, TextureTypes.FLOAT, pixelData);
    }

    public void setImage2DInt1010102PixelData(int internalFormat, int width, int height, int format, IntBuffer pixelData) {
        GL11.glTexImage2D(textureTarget, 0, internalFormat, width, height, 0, format, TextureTypes.UNSIGNED_INT_10_10_10_2, pixelData);
    }

    public void setImage2DIntPixelData(int internalFormat, int width, int height, int format, IntBuffer pixelData) {
        GL11.glTexImage2D(textureTarget, 0, internalFormat, width, height, 0, format, TextureTypes.UNSIGNED_INT, pixelData);
    }

    public void setImage2DShortPixelData(int internalFormat, int width, int height, int format, ShortBuffer pixelData) {
        GL11.glTexImage2D(textureTarget, 0, internalFormat, width, height, 0, format, TextureTypes.UNSIGNED_SHORT, pixelData);
    }

    public void setImage2DStencilPixelData(int internalFormat, int width, int height, int format, IntBuffer pixelData) {
        GL11.glTexImage2D(textureTarget, 0, internalFormat, width, height, 0, format, TextureTypes.UNSIGNED_INT_24_8, pixelData);
    }

    public void setImage3DBytePixelData(int internalFormat, int width, int height, int depth, int format, ByteBuffer pixelData) {
        GL12.glTexImage3D(textureTarget, 0, internalFormat, width, height, depth, 0, format, TextureTypes.UNSIGNED_BYTE, pixelData);
    }

    public void setImage3DFloatPixelData(int internalFormat, int width, int height, int depth, int format, FloatBuffer pixelData) {
        GL12.glTexImage3D(textureTarget, 0, internalFormat, width, height, depth, 0, format, TextureTypes.FLOAT, pixelData);
    }

    public void setImage3DInt1010102PixelData(int internalFormat, int width, int height, int depth, int format, IntBuffer pixelData) {
        GL12.glTexImage3D(textureTarget, 0, internalFormat, width, height, depth, 0, format, TextureTypes.UNSIGNED_INT_10_10_10_2, pixelData);
    }

    public void setImage3DIntPixelData(int internalFormat, int width, int height, int depth, int format, IntBuffer pixelData) {
        GL12.glTexImage3D(textureTarget, 0, internalFormat, width, height, depth, 0, format, TextureTypes.UNSIGNED_INT, pixelData);
    }

    public void setImage3DShortPixelData(int internalFormat, int width, int height, int depth, int format, ShortBuffer pixelData) {
        GL12.glTexImage3D(textureTarget, 0, internalFormat, width, height, depth, 0, format, TextureTypes.UNSIGNED_SHORT, pixelData);
    }

    public void setImage3DStencilPixelData(int internalFormat, int width, int height, int depth, int format, IntBuffer pixelData) {
        GL12.glTexImage3D(textureTarget, 0, internalFormat, width, height, depth, 0, format, TextureTypes.UNSIGNED_INT_24_8, pixelData);
    }

    public void setLodBias(float lodBias) {
        GL11.glTexParameterf(textureTarget, GL14.GL_TEXTURE_LOD_BIAS, lodBias);
    }

    public void setMaxLod(float maxLod) {
        GL11.glTexParameterf(textureTarget, GL12.GL_TEXTURE_MAX_LOD, maxLod);
    }

    public void setMinLod(float minLod) {
        GL11.glTexParameterf(textureTarget, GL12.GL_TEXTURE_MIN_LOD, minLod);
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
