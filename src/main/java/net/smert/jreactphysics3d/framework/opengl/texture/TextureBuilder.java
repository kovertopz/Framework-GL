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
package net.smert.jreactphysics3d.framework.opengl.texture;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import net.smert.jreactphysics3d.framework.opengl.GL;
import net.smert.jreactphysics3d.framework.opengl.Texture;
import net.smert.jreactphysics3d.framework.opengl.constants.TextureClamping;
import net.smert.jreactphysics3d.framework.opengl.constants.TextureCompareFunction;
import net.smert.jreactphysics3d.framework.opengl.constants.TextureCompareMode;
import net.smert.jreactphysics3d.framework.opengl.constants.TextureFilters;
import net.smert.jreactphysics3d.framework.opengl.constants.TextureFormats;
import net.smert.jreactphysics3d.framework.opengl.constants.TextureInternalFormats;
import net.smert.jreactphysics3d.framework.opengl.constants.TextureTargets;
import net.smert.jreactphysics3d.framework.opengl.constants.TextureTypes;
import net.smert.jreactphysics3d.framework.opengl.image.Conversion;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class TextureBuilder {

    private final static int ALWAYS = TextureCompareFunction.ALWAYS;
    private final static int CLAMP = TextureClamping.CLAMP;
    private final static int CLAMP_TO_EDGE = TextureClamping.CLAMP_TO_EDGE;
    private final static int COMPARE_R_TO_TEXTURE = TextureCompareMode.COMPARE_R_TO_TEXTURE;
    private final static int DEPTH_COMPONENT = TextureFormats.DEPTH_COMPONENT;
    private final static int DEPTH_COMPONENT16 = TextureInternalFormats.DEPTH_COMPONENT16;
    private final static int DEPTH_COMPONENT24 = TextureInternalFormats.DEPTH_COMPONENT24;
    private final static int DEPTH_COMPONENT32 = TextureInternalFormats.DEPTH_COMPONENT32;
    private final static int DEPTH_STENCIL = TextureFormats.DEPTH_STENCIL;
    private final static int DEPTH24_STENCIL8 = TextureInternalFormats.DEPTH24_STENCIL8;
    private final static int EQUAL = TextureCompareFunction.EQUAL;
    private final static int GEQUAL = TextureCompareFunction.GEQUAL;
    private final static int GREATER = TextureCompareFunction.GREATER;
    private final static int LEQUAL = TextureCompareFunction.LEQUAL;
    private final static int LESS = TextureCompareFunction.LESS;
    private final static int LINEAR = TextureFilters.LINEAR;
    private final static int LINEAR_MIPMAP_LINEAR = TextureFilters.LINEAR_MIPMAP_LINEAR;
    private final static int LINEAR_MIPMAP_NEAREST = TextureFilters.LINEAR_MIPMAP_NEAREST;
    private final static int NEAREST = TextureFilters.NEAREST;
    private final static int NEAREST_MIPMAP_LINEAR = TextureFilters.NEAREST_MIPMAP_LINEAR;
    private final static int NEAREST_MIPMAP_NEAREST = TextureFilters.NEAREST_MIPMAP_NEAREST;
    private final static int NEVER = TextureCompareFunction.NEVER;
    private final static int NONE = TextureCompareMode.NONE;
    private final static int NOTEQUAL = TextureCompareFunction.NOTEQUAL;
    private final static int REPEAT = TextureClamping.REPEAT;
    private final static int R = TextureFormats.R;
    private final static int R16F = TextureInternalFormats.R16F;
    private final static int R32F = TextureInternalFormats.R32F;
    private final static int RG = TextureFormats.RG;
    private final static int RG16F = TextureInternalFormats.RG16F;
    private final static int RG32F = TextureInternalFormats.RG32F;
    private final static int RGB = TextureFormats.RGB;
    private final static int RGB10_A2 = TextureInternalFormats.RGB10_A2;
    private final static int RGB16F = TextureInternalFormats.RGB16F;
    private final static int RGB32F = TextureInternalFormats.RGB32F;
    private final static int RGB8 = TextureInternalFormats.RGB8;
    private final static int RGBA = TextureFormats.RGBA;
    private final static int RGBA16F = TextureInternalFormats.RGBA16F;
    private final static int RGBA32F = TextureInternalFormats.RGBA32F;
    private final static int RGBA8 = TextureInternalFormats.RGBA8;
    private final static int TEXTURE_2D = TextureTargets.TEXTURE_2D;
    private final static int TEXTURE_3D = TextureTargets.TEXTURE_3D;
    private final static int TEXTURE_CUBE_MAP = TextureTargets.TEXTURE_CUBE_MAP;
    private final static int TEXTURE_CUBE_MAP_NEGATIVE_X = TextureTargets.TEXTURE_CUBE_MAP_NEGATIVE_X;
    private final static int TEXTURE_CUBE_MAP_POSITIVE_X = TextureTargets.TEXTURE_CUBE_MAP_POSITIVE_X;
    private final static int TEXTURE_CUBE_MAP_NEGATIVE_Y = TextureTargets.TEXTURE_CUBE_MAP_NEGATIVE_Y;
    private final static int TEXTURE_CUBE_MAP_POSITIVE_Y = TextureTargets.TEXTURE_CUBE_MAP_POSITIVE_Y;
    private final static int TEXTURE_CUBE_MAP_NEGATIVE_Z = TextureTargets.TEXTURE_CUBE_MAP_NEGATIVE_Z;
    private final static int TEXTURE_CUBE_MAP_POSITIVE_Z = TextureTargets.TEXTURE_CUBE_MAP_POSITIVE_Z;

    private boolean flipHorizontally;
    private boolean flipVertically;
    private boolean nullData;
    private boolean useMipmap;
    private byte[] pixelByteArray;
    private int textureCompareFunction;
    private int textureCompareMode;
    private int textureFilterMag;
    private int textureFilterMin;
    private int textureFormat;
    private int textureHeight;
    private int textureInternalFormat;
    private int texturePrimitive;
    private int textureType;
    private int textureWidth;
    private int textureWrapR;
    private int textureWrapS;
    private int textureWrapT;
    private BufferedImage bufferedImage;
    private ByteBuffer pixelByteBuffer;
    private ByteBuffer pixelByteBufferXNeg;
    private ByteBuffer pixelByteBufferXPos;
    private ByteBuffer pixelByteBufferYNeg;
    private ByteBuffer pixelByteBufferYPos;
    private ByteBuffer pixelByteBufferZNeg;
    private ByteBuffer pixelByteBufferZPos;
    private Texture texture;

    public TextureBuilder() {
        reset();
    }

    private void buildTexture2D() {
        if (nullData) {
            buildTexture2DNullData();
        } else {
            buildTexture2DByteBuffer();
        }
    }

    private void buildTexture2DByteBuffer() {

        switch (texturePrimitive) {
            case TextureTypes.UNSIGNED_BYTE:
                GL.textureHelper.setImage2DBytePixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, pixelByteBuffer);
                break;

            case TextureTypes.FLOAT:
            case TextureTypes.UNSIGNED_INT_10_10_10_2:
            case TextureTypes.UNSIGNED_INT:
            case TextureTypes.UNSIGNED_SHORT:
            case TextureTypes.UNSIGNED_INT_24_8:
                throw new IllegalArgumentException("Unsupported texture primitive for pixel data: " + texturePrimitive);
        }

        if (useMipmap == true) {
            switch (texturePrimitive) {
                case TextureTypes.UNSIGNED_BYTE:
                    GL.textureHelper.setByteMipmaps(textureInternalFormat, textureWidth, textureHeight, textureFormat, pixelByteBuffer);
                    break;

                case TextureTypes.FLOAT:
                case TextureTypes.UNSIGNED_INT_10_10_10_2:
                case TextureTypes.UNSIGNED_INT:
                case TextureTypes.UNSIGNED_SHORT:
                case TextureTypes.UNSIGNED_INT_24_8:
                    throw new IllegalArgumentException("Unsupported texture primitive for mipmap data: " + texturePrimitive);
            }
        }
    }

    private void buildTexture2DNullData() {

        switch (texturePrimitive) {
            case TextureTypes.UNSIGNED_BYTE:
                GL.textureHelper.setImage2DBytePixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                break;

            case TextureTypes.FLOAT:
                GL.textureHelper.setImage2DFloatPixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                break;

            case TextureTypes.UNSIGNED_INT_10_10_10_2:
                GL.textureHelper.setImage2DInt1010102PixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                break;

            case TextureTypes.UNSIGNED_INT:
                GL.textureHelper.setImage2DIntPixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                break;

            case TextureTypes.UNSIGNED_SHORT:
                GL.textureHelper.setImage2DShortPixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                break;

            case TextureTypes.UNSIGNED_INT_24_8:
                GL.textureHelper.setImage2DStencilPixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                break;
        }

        if (useMipmap == true) {
            switch (texturePrimitive) {
                case TextureTypes.UNSIGNED_BYTE:
                case TextureTypes.FLOAT:
                case TextureTypes.UNSIGNED_INT_10_10_10_2:
                case TextureTypes.UNSIGNED_INT:
                case TextureTypes.UNSIGNED_SHORT:
                case TextureTypes.UNSIGNED_INT_24_8:
                    GL.fboHelper.generateMipmap(TEXTURE_2D);
            }
        }
    }

    private void buildTexture3D() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void buildTextureCubeMap() {
        if (nullData == true) {
            buildTextureCubeMapNullData();
        } else {
            buildTextureCubeMapByteBuffer();
        }
        GL.textureHelper.setTextureTargetCubeMap();
    }

    private void buildTextureCubeMapByteBuffer() {

        switch (texturePrimitive) {
            case TextureTypes.UNSIGNED_BYTE:
                GL.textureHelper.setTextureTargetCubeMapPositiveX();
                GL.textureHelper.setImage2DBytePixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, pixelByteBufferXPos);
                GL.textureHelper.setTextureTargetCubeMapNegativeX();
                GL.textureHelper.setImage2DBytePixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, pixelByteBufferXNeg);
                GL.textureHelper.setTextureTargetCubeMapPositiveY();
                GL.textureHelper.setImage2DBytePixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, pixelByteBufferYPos);
                GL.textureHelper.setTextureTargetCubeMapNegativeY();
                GL.textureHelper.setImage2DBytePixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, pixelByteBufferYNeg);
                GL.textureHelper.setTextureTargetCubeMapPositiveZ();
                GL.textureHelper.setImage2DBytePixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, pixelByteBufferZPos);
                GL.textureHelper.setTextureTargetCubeMapNegativeZ();
                GL.textureHelper.setImage2DBytePixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, pixelByteBufferZNeg);
                break;

            case TextureTypes.FLOAT:
            case TextureTypes.UNSIGNED_INT_10_10_10_2:
            case TextureTypes.UNSIGNED_INT:
            case TextureTypes.UNSIGNED_SHORT:
            case TextureTypes.UNSIGNED_INT_24_8:
                throw new IllegalArgumentException("Unknown texture primitive for pixel data: " + texturePrimitive);
        }

        if (useMipmap == true) {
            switch (texturePrimitive) {
                case TextureTypes.FLOAT:
                case TextureTypes.UNSIGNED_BYTE:
                case TextureTypes.UNSIGNED_INT_10_10_10_2:
                case TextureTypes.UNSIGNED_INT:
                case TextureTypes.UNSIGNED_SHORT:
                case TextureTypes.UNSIGNED_INT_24_8:
                    throw new UnsupportedOperationException("Not supported yet.");
            }
        }
    }

    private void buildTextureCubeMapNullData() {

        switch (texturePrimitive) {
            case TextureTypes.UNSIGNED_BYTE:
                GL.textureHelper.setTextureTargetCubeMapPositiveX();
                GL.textureHelper.setImage2DBytePixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapNegativeX();
                GL.textureHelper.setImage2DBytePixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapPositiveY();
                GL.textureHelper.setImage2DBytePixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapNegativeY();
                GL.textureHelper.setImage2DBytePixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapPositiveZ();
                GL.textureHelper.setImage2DBytePixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapNegativeZ();
                GL.textureHelper.setImage2DBytePixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                break;

            case TextureTypes.FLOAT:
                GL.textureHelper.setTextureTargetCubeMapPositiveX();
                GL.textureHelper.setImage2DFloatPixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapNegativeX();
                GL.textureHelper.setImage2DFloatPixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapPositiveY();
                GL.textureHelper.setImage2DFloatPixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapNegativeY();
                GL.textureHelper.setImage2DFloatPixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapPositiveZ();
                GL.textureHelper.setImage2DFloatPixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapNegativeZ();
                GL.textureHelper.setImage2DFloatPixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                break;

            case TextureTypes.UNSIGNED_INT_10_10_10_2:
                GL.textureHelper.setTextureTargetCubeMapPositiveX();
                GL.textureHelper.setImage2DInt1010102PixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapNegativeX();
                GL.textureHelper.setImage2DInt1010102PixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapPositiveY();
                GL.textureHelper.setImage2DInt1010102PixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapNegativeY();
                GL.textureHelper.setImage2DInt1010102PixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapPositiveZ();
                GL.textureHelper.setImage2DInt1010102PixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapNegativeZ();
                GL.textureHelper.setImage2DInt1010102PixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                break;

            case TextureTypes.UNSIGNED_INT:
                GL.textureHelper.setTextureTargetCubeMapPositiveX();
                GL.textureHelper.setImage2DIntPixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapNegativeX();
                GL.textureHelper.setImage2DIntPixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapPositiveY();
                GL.textureHelper.setImage2DIntPixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapNegativeY();
                GL.textureHelper.setImage2DIntPixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapPositiveZ();
                GL.textureHelper.setImage2DIntPixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapNegativeZ();
                GL.textureHelper.setImage2DIntPixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                break;

            case TextureTypes.UNSIGNED_SHORT:
                GL.textureHelper.setTextureTargetCubeMapPositiveX();
                GL.textureHelper.setImage2DShortPixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapNegativeX();
                GL.textureHelper.setImage2DShortPixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapPositiveY();
                GL.textureHelper.setImage2DShortPixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapNegativeY();
                GL.textureHelper.setImage2DShortPixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapPositiveZ();
                GL.textureHelper.setImage2DShortPixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapNegativeZ();
                GL.textureHelper.setImage2DShortPixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                break;

            case TextureTypes.UNSIGNED_INT_24_8:
                GL.textureHelper.setTextureTargetCubeMapPositiveX();
                GL.textureHelper.setImage2DStencilPixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapNegativeX();
                GL.textureHelper.setImage2DStencilPixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapPositiveY();
                GL.textureHelper.setImage2DStencilPixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapNegativeY();
                GL.textureHelper.setImage2DStencilPixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapPositiveZ();
                GL.textureHelper.setImage2DStencilPixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                GL.textureHelper.setTextureTargetCubeMapNegativeZ();
                GL.textureHelper.setImage2DStencilPixelData(textureInternalFormat, textureWidth, textureHeight, textureFormat, null);
                break;
        }

        if (useMipmap == true) {
            switch (texturePrimitive) {
                case TextureTypes.UNSIGNED_BYTE:
                case TextureTypes.FLOAT:
                case TextureTypes.UNSIGNED_INT_10_10_10_2:
                case TextureTypes.UNSIGNED_INT:
                case TextureTypes.UNSIGNED_SHORT:
                case TextureTypes.UNSIGNED_INT_24_8:
                    throw new UnsupportedOperationException("Not supported yet.");
            }
        }
    }

    private ByteBuffer createBytePixelDataBuffer(byte[] pixelarray) {
        ByteBuffer pixeldata = GL.bufferHelper.createByteBuffer(pixelarray.length);

        for (int i = 0, max = pixelarray.length; i < max; i++) {
            byte temp = pixelarray[i];
            int index = pixelarray.length - i - 1;
            pixelarray[i] = pixelarray[index];
            pixelarray[index] = temp;
        }

        pixeldata.put(pixelarray);
        pixeldata.flip();
        return pixeldata;
    }

    public Texture build(boolean reset) {
        Texture temp = texture;
        if (reset == true) {
            reset();
        }
        return temp;
    }

    public TextureBuilder buildTexture() {

        texture = GL.glf.createTexture();
        texture.create();

        switch (textureType) {
            case TEXTURE_2D:
                GL.textureHelper.setTextureTarget2D();
                break;

            case TEXTURE_3D:
                GL.textureHelper.setTextureTarget3D();
                break;

            case TEXTURE_CUBE_MAP:
                GL.textureHelper.setTextureTargetCubeMap();
                break;
        }

        GL.textureHelper.bind(texture.getTextureID());
        GL.textureHelper.setClamping(textureWrapR, textureWrapS, textureWrapT);
        GL.textureHelper.setCompareFunction(textureCompareFunction);
        GL.textureHelper.setCompareMode(textureCompareMode);
        GL.textureHelper.setFilters(textureFilterMag, textureFilterMin);

        switch (textureType) {
            case TEXTURE_2D:
                buildTexture2D();
                break;

            case TEXTURE_3D:
                buildTexture3D();
                break;

            case TEXTURE_CUBE_MAP:
                buildTextureCubeMap();
                break;
        }

        GL.textureHelper.unbind();

        return this;
    }

    public TextureBuilder convertImageARGBToRGBABytePixelArray() {
        pixelByteArray = Conversion.ConvertImageARGBToRGBAByteArray(bufferedImage);
        return this;
    }

    public TextureBuilder createBytePixelDataBuffer() {
        pixelByteBuffer = createBytePixelDataBuffer(pixelByteArray);
        return this;
    }

    public TextureBuilder createBytePixelDataBufferXNeg() {
        pixelByteBufferXNeg = createBytePixelDataBuffer(pixelByteArray);
        return this;
    }

    public TextureBuilder createBytePixelDataBufferXPos() {
        pixelByteBufferXPos = createBytePixelDataBuffer(pixelByteArray);
        return this;
    }

    public TextureBuilder createBytePixelDataBufferYNeg() {
        pixelByteBufferYNeg = createBytePixelDataBuffer(pixelByteArray);
        return this;
    }

    public TextureBuilder createBytePixelDataBufferYPos() {
        pixelByteBufferYPos = createBytePixelDataBuffer(pixelByteArray);
        return this;
    }

    public TextureBuilder createBytePixelDataBufferZNeg() {
        pixelByteBufferZNeg = createBytePixelDataBuffer(pixelByteArray);
        return this;
    }

    public TextureBuilder createBytePixelDataBufferZPos() {
        pixelByteBufferZPos = createBytePixelDataBuffer(pixelByteArray);
        return this;
    }

    public int getTextureFilterMag() {
        return textureFilterMag;
    }

    public int getTextureFilterMin() {
        return textureFilterMin;
    }

    public int getTextureFormat() {
        return textureFormat;
    }

    public int getTextureHeight() {
        return textureHeight;
    }

    public int getTextureInternalFormat() {
        return textureInternalFormat;
    }

    public int getTexturePrimitive() {
        return texturePrimitive;
    }

    public int getTextureType() {
        return textureType;
    }

    public int getTextureWidth() {
        return textureWidth;
    }

    public int getTextureWrapR() {
        return textureWrapR;
    }

    public int getTextureWrapS() {
        return textureWrapS;
    }

    public int getTextureWrapT() {
        return textureWrapT;
    }

    public TextureBuilder load2D(BufferedImage textureImage) {

        if (flipHorizontally == true) {
            textureImage = Conversion.FlipHorizontally(textureImage);
        }
        if (flipVertically == true) {
            textureImage = Conversion.FlipVertically(textureImage);
        }
        setImage(textureImage);
        setHeightAndWidthFromImage();
        convertImageARGBToRGBABytePixelArray();
        createBytePixelDataBuffer();

        return this;
    }

    public TextureBuilder loadCube(BufferedImage textureImage, int cubeSize) {

        if (flipHorizontally == true) {
            textureImage = Conversion.FlipHorizontally(textureImage);
        }
        if (flipVertically == true) {
            textureImage = Conversion.FlipVertically(textureImage);
        }
        setImage(textureImage);
        setHeightAndWidthFromImage();
        convertImageARGBToRGBABytePixelArray();

        switch (cubeSize) {
            case TEXTURE_CUBE_MAP_POSITIVE_X:
                createBytePixelDataBufferXPos();
                break;

            case TEXTURE_CUBE_MAP_NEGATIVE_X:
                createBytePixelDataBufferXNeg();
                break;

            case TEXTURE_CUBE_MAP_POSITIVE_Y:
                createBytePixelDataBufferYPos();
                break;

            case TEXTURE_CUBE_MAP_NEGATIVE_Y:
                createBytePixelDataBufferYNeg();
                break;

            case TEXTURE_CUBE_MAP_POSITIVE_Z:
                createBytePixelDataBufferZPos();
                break;

            case TEXTURE_CUBE_MAP_NEGATIVE_Z:
                createBytePixelDataBufferZNeg();
                break;
        }

        return this;
    }

    public final TextureBuilder reset() {

        flipHorizontally = false;
        flipVertically = false;
        nullData = false;
        useMipmap = false;
        pixelByteArray = null;
        setCompareFunctionLessEqual();
        setCompareModeNone();
        setFilterMagNearest();
        setFilterMinNearest();
        setFormatRGBA();
        textureHeight = 0;
        setInternalFormatRGBA8();
        setPrimitiveByte();
        setTextureTarget2D();
        textureWidth = 0;
        setClampingWrapRRepeat();
        setClampingWrapSRepeat();
        setClampingWrapTRepeat();
        bufferedImage = null;
        pixelByteBuffer = null;
        pixelByteBufferXNeg = null;
        pixelByteBufferXPos = null;
        pixelByteBufferYNeg = null;
        pixelByteBufferYPos = null;
        pixelByteBufferZNeg = null;
        pixelByteBufferZPos = null;
        texture = null;
        return this;
    }

    public TextureBuilder setBytePixelArray(byte[] pixelarray) {
        pixelByteArray = pixelarray;
        return this;
    }

    public TextureBuilder setClampingWrapRClamp() {
        textureWrapR = CLAMP;
        return this;
    }

    public TextureBuilder setClampingWrapRClampToEdge() {
        textureWrapR = CLAMP_TO_EDGE;
        return this;
    }

    public TextureBuilder setClampingWrapRRepeat() {
        textureWrapR = REPEAT;
        return this;
    }

    public TextureBuilder setClampingWrapSClamp() {
        textureWrapS = CLAMP;
        return this;
    }

    public TextureBuilder setClampingWrapSClampToEdge() {
        textureWrapS = CLAMP_TO_EDGE;
        return this;
    }

    public TextureBuilder setClampingWrapSRepeat() {
        textureWrapS = REPEAT;
        return this;
    }

    public TextureBuilder setClampingWrapTClamp() {
        textureWrapT = CLAMP;
        return this;
    }

    public TextureBuilder setClampingWrapTClampToEdge() {
        textureWrapT = CLAMP_TO_EDGE;
        return this;
    }

    public TextureBuilder setClampingWrapTRepeat() {
        textureWrapT = REPEAT;
        return this;
    }

    public TextureBuilder setCompareFunctionAlways() {
        textureCompareFunction = ALWAYS;
        return this;
    }

    public TextureBuilder setCompareFunctionEqual() {
        textureCompareFunction = EQUAL;
        return this;
    }

    public TextureBuilder setCompareFunctionGreater() {
        textureCompareFunction = GREATER;
        return this;
    }

    public TextureBuilder setCompareFunctionGreaterEqual() {
        textureCompareFunction = GEQUAL;
        return this;
    }

    public TextureBuilder setCompareFunctionLess() {
        textureCompareFunction = LESS;
        return this;
    }

    public TextureBuilder setCompareFunctionLessEqual() {
        textureCompareFunction = LEQUAL;
        return this;
    }

    public TextureBuilder setCompareFunctionNever() {
        textureCompareFunction = NEVER;
        return this;
    }

    public TextureBuilder setCompareFunctionNotEqual() {
        textureCompareFunction = NOTEQUAL;
        return this;
    }

    public TextureBuilder setCompareModeCompareRToTexture() {
        textureCompareMode = COMPARE_R_TO_TEXTURE;
        return this;
    }

    public TextureBuilder setCompareModeNone() {
        textureCompareMode = NONE;
        return this;
    }

    public TextureBuilder setFilterMagLinear() {
        textureFilterMag = LINEAR;
        return this;
    }

    public TextureBuilder setFilterMagNearest() {
        textureFilterMag = NEAREST;
        return this;
    }

    public TextureBuilder setFilterMinLinear() {
        textureFilterMin = LINEAR;
        return this;
    }

    public TextureBuilder setFilterMinLinearMipmapLinear() {
        textureFilterMin = LINEAR_MIPMAP_LINEAR;
        return this;
    }

    public TextureBuilder setFilterMinLinearMipmapNearest() {
        textureFilterMin = LINEAR_MIPMAP_NEAREST;
        return this;
    }

    public TextureBuilder setFilterMinNearest() {
        textureFilterMin = NEAREST;
        return this;
    }

    public TextureBuilder setFilterMinNearestMipmapLinear() {
        textureFilterMin = NEAREST_MIPMAP_LINEAR;
        return this;
    }

    public TextureBuilder setFilterMinNearestMipmapNearest() {
        textureFilterMin = NEAREST_MIPMAP_NEAREST;
        return this;
    }

    public TextureBuilder setFormatDepthComponent() {
        textureFormat = DEPTH_COMPONENT;
        return this;
    }

    public TextureBuilder setFormatDepthStencil() {
        textureFormat = DEPTH_STENCIL;
        return this;
    }

    public TextureBuilder setFormatR() {
        textureFormat = R;
        return this;
    }

    public TextureBuilder setFormatRG() {
        textureFormat = RG;
        return this;
    }

    public TextureBuilder setFormatRGB() {
        textureFormat = RGB;
        return this;
    }

    public TextureBuilder setFormatRGBA() {
        textureFormat = RGBA;
        return this;
    }

    public TextureBuilder setHeight(int height) {
        this.textureHeight = height;
        return this;
    }

    public TextureBuilder setHeightAndWidthFromImage() {
        textureHeight = bufferedImage.getHeight();
        textureWidth = bufferedImage.getWidth();
        return this;
    }

    public TextureBuilder setImage(BufferedImage image) {
        bufferedImage = image;
        return this;
    }

    public TextureBuilder setInternalFormatDepthComponent16() {
        textureInternalFormat = DEPTH_COMPONENT16;
        return this;
    }

    public TextureBuilder setInternalFormatDepthComponent24() {
        textureInternalFormat = DEPTH_COMPONENT24;
        return this;
    }

    public TextureBuilder setInternalFormatDepthComponent32() {
        textureInternalFormat = DEPTH_COMPONENT32;
        return this;
    }

    public TextureBuilder setInternalFormatDepth24Stencil8() {
        textureInternalFormat = DEPTH24_STENCIL8;
        return this;
    }

    public TextureBuilder setInternalFormatR16F() {
        textureInternalFormat = R16F;
        return this;
    }

    public TextureBuilder setInternalFormatR32F() {
        textureInternalFormat = R32F;
        return this;
    }

    public TextureBuilder setInternalFormatRG16F() {
        textureInternalFormat = RG16F;
        return this;
    }

    public TextureBuilder setInternalFormatRG32F() {
        textureInternalFormat = RG32F;
        return this;
    }

    public TextureBuilder setInternalFormatRGB10_A2() {
        textureInternalFormat = RGB10_A2;
        return this;
    }

    public TextureBuilder setInternalFormatRGB16F() {
        textureInternalFormat = RGB16F;
        return this;
    }

    public TextureBuilder setInternalFormatRGB32F() {
        textureInternalFormat = RGB32F;
        return this;
    }

    public TextureBuilder setInternalFormatRGB8() {
        textureInternalFormat = RGB8;
        return this;
    }

    public TextureBuilder setInternalFormatRGBA16F() {
        textureInternalFormat = RGBA16F;
        return this;
    }

    public TextureBuilder setInternalFormatRGBA32F() {
        textureInternalFormat = RGBA32F;
        return this;
    }

    public TextureBuilder setInternalFormatRGBA8() {
        textureInternalFormat = RGBA8;
        return this;
    }

    public TextureBuilder setLoadFlipHorizontally(boolean enabled) {
        flipHorizontally = enabled;
        return this;
    }

    public TextureBuilder setLoadFlipVertically(boolean enabled) {
        flipVertically = enabled;
        return this;
    }

    public TextureBuilder setNullData(boolean enabled) {
        nullData = enabled;
        return this;
    }

    public TextureBuilder setPrimitiveByte() {
        texturePrimitive = TextureTypes.UNSIGNED_BYTE;
        return this;
    }

    public TextureBuilder setPrimitiveFloat() {
        texturePrimitive = TextureTypes.FLOAT;
        return this;
    }

    public TextureBuilder setPrimitiveInt1010102() {
        texturePrimitive = TextureTypes.UNSIGNED_INT_10_10_10_2;
        return this;
    }

    public TextureBuilder setPrimitiveInt() {
        texturePrimitive = TextureTypes.UNSIGNED_INT;
        return this;
    }

    public TextureBuilder setPrimitiveShort() {
        texturePrimitive = TextureTypes.UNSIGNED_SHORT;
        return this;
    }

    public TextureBuilder setPrimitiveStencil() {
        texturePrimitive = TextureTypes.UNSIGNED_INT_24_8;
        return this;
    }

    public TextureBuilder setTextureTarget2D() {
        textureType = TEXTURE_2D;
        return this;
    }

    public TextureBuilder setTextureTarget3D() {
        textureType = TEXTURE_3D;
        return this;
    }

    public TextureBuilder setTextureTargetCubeMap() {
        textureType = TEXTURE_CUBE_MAP;
        return this;
    }

    public TextureBuilder setUseMipmaps(boolean enabled) {
        useMipmap = enabled;
        return this;
    }

    public TextureBuilder setWidth(int width) {
        this.textureWidth = width;
        return this;
    }

}
