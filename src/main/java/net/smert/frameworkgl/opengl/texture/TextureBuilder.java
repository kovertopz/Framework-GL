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
package net.smert.frameworkgl.opengl.texture;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.Texture;
import net.smert.frameworkgl.opengl.constants.TextureClamping;
import net.smert.frameworkgl.opengl.constants.TextureCompareFunction;
import net.smert.frameworkgl.opengl.constants.TextureCompareMode;
import net.smert.frameworkgl.opengl.constants.TextureFilters;
import net.smert.frameworkgl.opengl.constants.TextureFormats;
import net.smert.frameworkgl.opengl.constants.TextureInternalFormats;
import net.smert.frameworkgl.opengl.constants.TextureTargets;
import net.smert.frameworkgl.opengl.constants.TextureTypes;
import net.smert.frameworkgl.opengl.image.Conversion;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class TextureBuilder {

    private boolean convertToRGBA;
    private boolean flipHorizontally;
    private boolean flipVertically;
    private boolean nullData;
    private boolean useMipmap;
    private byte[] pixelByteArray;
    private float lodBias;
    private float maxLod;
    private float minLod;
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

        // Set pixel data
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

        if (!useMipmap) {
            return;
        }

        // Generate mipmaps
        switch (texturePrimitive) {
            case TextureTypes.UNSIGNED_BYTE:
            case TextureTypes.FLOAT:
            case TextureTypes.UNSIGNED_INT_10_10_10_2:
            case TextureTypes.UNSIGNED_INT:
            case TextureTypes.UNSIGNED_SHORT:
            case TextureTypes.UNSIGNED_INT_24_8:
                throw new IllegalArgumentException("Unsupported texture primitive for mipmap data: " + texturePrimitive);
        }
    }

    private void buildTexture2DNullData() {

        // Set pixel data
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

        if (!useMipmap) {
            return;
        }

        // Generate mipmaps
        switch (texturePrimitive) {
            case TextureTypes.UNSIGNED_BYTE:
            case TextureTypes.FLOAT:
            case TextureTypes.UNSIGNED_INT_10_10_10_2:
            case TextureTypes.UNSIGNED_INT:
            case TextureTypes.UNSIGNED_SHORT:
            case TextureTypes.UNSIGNED_INT_24_8:
                throw new IllegalArgumentException("Unsupported texture primitive for mipmap data: " + texturePrimitive);
        }
    }

    private void buildTexture3D() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void buildTextureCubeMap() {
        if (nullData) {
            buildTextureCubeMapNullData();
        } else {
            buildTextureCubeMapByteBuffer();
        }
        GL.textureHelper.setTextureTargetCubeMap();
    }

    private void buildTextureCubeMapByteBuffer() {

        // Set pixel data
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

        if (!useMipmap) {
            return;
        }

        // Generate mipmaps
        switch (texturePrimitive) {
            case TextureTypes.FLOAT:
            case TextureTypes.UNSIGNED_BYTE:
            case TextureTypes.UNSIGNED_INT_10_10_10_2:
            case TextureTypes.UNSIGNED_INT:
            case TextureTypes.UNSIGNED_SHORT:
            case TextureTypes.UNSIGNED_INT_24_8:
                throw new IllegalArgumentException("Unsupported texture primitive for mipmap data: " + texturePrimitive);
        }
    }

    private void buildTextureCubeMapNullData() {

        // Set pixel data
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

        if (!useMipmap) {
            return;
        }

        // Generate mipmaps
        switch (texturePrimitive) {
            case TextureTypes.UNSIGNED_BYTE:
            case TextureTypes.FLOAT:
            case TextureTypes.UNSIGNED_INT_10_10_10_2:
            case TextureTypes.UNSIGNED_INT:
            case TextureTypes.UNSIGNED_SHORT:
            case TextureTypes.UNSIGNED_INT_24_8:
                throw new IllegalArgumentException("Unsupported texture primitive for mipmap data: " + texturePrimitive);
        }
    }

    private ByteBuffer createBytePixelDataBuffer(byte[] pixelarray) {
        ByteBuffer pixeldata = GL.bufferHelper.createByteBuffer(pixelarray.length);

        for (int i = 0; i < pixelarray.length; i++) {
            byte temp = pixelarray[i];
            int index = pixelarray.length - i - 1;
            pixelarray[i] = pixelarray[index];
            pixelarray[index] = temp;
        }

        pixeldata.put(pixelarray);
        pixeldata.flip();
        return pixeldata;
    }

    public TextureBuilder buildTexture() {

        texture = GL.glFactory.createTexture();
        texture.create();
        texture.setTextureTarget(textureType);
        texture.setWidthAndHeight(textureWidth, textureHeight);

        switch (textureType) {
            case TextureTargets.TEXTURE_2D:
                GL.textureHelper.setTextureTarget2D();
                break;

            case TextureTargets.TEXTURE_3D:
                GL.textureHelper.setTextureTarget3D();
                break;

            case TextureTargets.TEXTURE_CUBE_MAP:
                GL.textureHelper.setTextureTargetCubeMap();
                break;
        }

        GL.textureHelper.bind(texture.getTextureID());
        GL.textureHelper.setClamping(textureWrapR, textureWrapS, textureWrapT);
        GL.textureHelper.setCompareFunction(textureCompareFunction);
        GL.textureHelper.setCompareMode(textureCompareMode);
        GL.textureHelper.setFilters(textureFilterMag, textureFilterMin);
        GL.textureHelper.setLodBias(lodBias);
        GL.textureHelper.setMaxLod(maxLod);
        GL.textureHelper.setMinLod(minLod);

        switch (textureType) {
            case TextureTargets.TEXTURE_2D:
                buildTexture2D();
                break;

            case TextureTargets.TEXTURE_3D:
                buildTexture3D();
                break;

            case TextureTargets.TEXTURE_CUBE_MAP:
                buildTextureCubeMap();
                break;
        }

        GL.textureHelper.unbind();

        return this;
    }

    public TextureBuilder convertImageARGBToBGRABytePixelArray() {
        pixelByteArray = Conversion.ConvertImageARGBToBGRAByteArray(bufferedImage);
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

    public Texture createTexture(boolean reset) {
        Texture temp = texture;
        if (reset) {
            reset();
        }
        return temp;
    }

    public byte[] getPixelByteArray() {
        return pixelByteArray;
    }

    public float getLodBias() {
        return lodBias;
    }

    public float getMaxLod() {
        return maxLod;
    }

    public float getMinLod() {
        return minLod;
    }

    public int getTextureCompareFunction() {
        return textureCompareFunction;
    }

    public int getTextureCompareMode() {
        return textureCompareMode;
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

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public ByteBuffer getPixelByteBuffer() {
        return pixelByteBuffer;
    }

    public ByteBuffer getPixelByteBufferXNeg() {
        return pixelByteBufferXNeg;
    }

    public ByteBuffer getPixelByteBufferXPos() {
        return pixelByteBufferXPos;
    }

    public ByteBuffer getPixelByteBufferYNeg() {
        return pixelByteBufferYNeg;
    }

    public ByteBuffer getPixelByteBufferYPos() {
        return pixelByteBufferYPos;
    }

    public ByteBuffer getPixelByteBufferZNeg() {
        return pixelByteBufferZNeg;
    }

    public ByteBuffer getPixelByteBufferZPos() {
        return pixelByteBufferZPos;
    }

    public boolean isFlipHorizontally() {
        return flipHorizontally;
    }

    public boolean isFlipVertically() {
        return flipVertically;
    }

    public boolean isNullData() {
        return nullData;
    }

    public boolean isUseMipmap() {
        return useMipmap;
    }

    public TextureBuilder load2D(BufferedImage textureImage) {

        setImage(textureImage);
        setHeightAndWidthFromImage();
        if (convertToRGBA) {
            convertImageARGBToRGBABytePixelArray();
        } else {
            convertImageARGBToBGRABytePixelArray();
        }
        if (flipHorizontally) {
            pixelByteArray = Conversion.FlipHorizontally(pixelByteArray, textureImage.getWidth(),
                    textureImage.getHeight());
        }
        if (flipVertically) {
            pixelByteArray = Conversion.FlipVertically(pixelByteArray, textureImage.getWidth(),
                    textureImage.getHeight());
        }
        createBytePixelDataBuffer();

        return this;
    }

    public TextureBuilder loadCube(BufferedImage textureImage, int cubeSize) {

        setImage(textureImage);
        setHeightAndWidthFromImage();
        if (convertToRGBA) {
            convertImageARGBToRGBABytePixelArray();
        } else {
            convertImageARGBToBGRABytePixelArray();
        }
        if (flipHorizontally) {
            pixelByteArray = Conversion.FlipHorizontally(pixelByteArray, textureImage.getWidth(),
                    textureImage.getHeight());
        }
        if (flipVertically) {
            pixelByteArray = Conversion.FlipVertically(pixelByteArray, textureImage.getWidth(),
                    textureImage.getHeight());
        }

        switch (cubeSize) {
            case TextureTargets.TEXTURE_CUBE_MAP_POSITIVE_X:
                createBytePixelDataBufferXPos();
                break;

            case TextureTargets.TEXTURE_CUBE_MAP_NEGATIVE_X:
                createBytePixelDataBufferXNeg();
                break;

            case TextureTargets.TEXTURE_CUBE_MAP_POSITIVE_Y:
                createBytePixelDataBufferYPos();
                break;

            case TextureTargets.TEXTURE_CUBE_MAP_NEGATIVE_Y:
                createBytePixelDataBufferYNeg();
                break;

            case TextureTargets.TEXTURE_CUBE_MAP_POSITIVE_Z:
                createBytePixelDataBufferZPos();
                break;

            case TextureTargets.TEXTURE_CUBE_MAP_NEGATIVE_Z:
                createBytePixelDataBufferZNeg();
                break;
        }

        return this;
    }

    public final TextureBuilder reset() {

        convertToRGBA = true;
        flipHorizontally = false;
        flipVertically = false;
        nullData = false;
        useMipmap = false;
        pixelByteArray = null;
        lodBias = 0;
        maxLod = 1000f;
        minLod = -1000f;
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
        textureWrapR = TextureClamping.CLAMP;
        return this;
    }

    public TextureBuilder setClampingWrapRClampToEdge() {
        textureWrapR = TextureClamping.CLAMP_TO_EDGE;
        return this;
    }

    public TextureBuilder setClampingWrapRRepeat() {
        textureWrapR = TextureClamping.REPEAT;
        return this;
    }

    public TextureBuilder setClampingWrapSClamp() {
        textureWrapS = TextureClamping.CLAMP;
        return this;
    }

    public TextureBuilder setClampingWrapSClampToEdge() {
        textureWrapS = TextureClamping.CLAMP_TO_EDGE;
        return this;
    }

    public TextureBuilder setClampingWrapSRepeat() {
        textureWrapS = TextureClamping.REPEAT;
        return this;
    }

    public TextureBuilder setClampingWrapTClamp() {
        textureWrapT = TextureClamping.CLAMP;
        return this;
    }

    public TextureBuilder setClampingWrapTClampToEdge() {
        textureWrapT = TextureClamping.CLAMP_TO_EDGE;
        return this;
    }

    public TextureBuilder setClampingWrapTRepeat() {
        textureWrapT = TextureClamping.REPEAT;
        return this;
    }

    public TextureBuilder setCompareFunctionAlways() {
        textureCompareFunction = TextureCompareFunction.ALWAYS;
        return this;
    }

    public TextureBuilder setCompareFunctionEqual() {
        textureCompareFunction = TextureCompareFunction.EQUAL;
        return this;
    }

    public TextureBuilder setCompareFunctionGreater() {
        textureCompareFunction = TextureCompareFunction.GREATER;
        return this;
    }

    public TextureBuilder setCompareFunctionGreaterEqual() {
        textureCompareFunction = TextureCompareFunction.GEQUAL;
        return this;
    }

    public TextureBuilder setCompareFunctionLess() {
        textureCompareFunction = TextureCompareFunction.LESS;
        return this;
    }

    public TextureBuilder setCompareFunctionLessEqual() {
        textureCompareFunction = TextureCompareFunction.LEQUAL;
        return this;
    }

    public TextureBuilder setCompareFunctionNever() {
        textureCompareFunction = TextureCompareFunction.NEVER;
        return this;
    }

    public TextureBuilder setCompareFunctionNotEqual() {
        textureCompareFunction = TextureCompareFunction.NOTEQUAL;
        return this;
    }

    public TextureBuilder setCompareModeCompareRToTexture() {
        textureCompareMode = TextureCompareMode.COMPARE_R_TO_TEXTURE;
        return this;
    }

    public TextureBuilder setCompareModeNone() {
        textureCompareMode = TextureCompareMode.NONE;
        return this;
    }

    /**
     * Converts the image to RGBA to be used by OpenGL. If false the format will
     * be BGRA which is useful for LWJGL mouse cursors. The documentation for
     * LWJGL Cursor says "Cursor images are in ARGB format, but only one bit
     * transparency is guaranteed to be supported." but converting to the format
     * swaps red and blue colors leading me to believe there is an error with
     * the documentation.
     *
     * @param enabled
     * @return
     */
    public TextureBuilder setConvertToRGBA(boolean enabled) {
        convertToRGBA = enabled;
        return this;
    }

    public TextureBuilder setFilterMagLinear() {
        textureFilterMag = TextureFilters.LINEAR;
        return this;
    }

    public TextureBuilder setFilterMagNearest() {
        textureFilterMag = TextureFilters.NEAREST;
        return this;
    }

    public TextureBuilder setFilterMinLinear() {
        textureFilterMin = TextureFilters.LINEAR;
        return this;
    }

    public TextureBuilder setFilterMinLinearMipmapLinear() {
        textureFilterMin = TextureFilters.LINEAR_MIPMAP_LINEAR;
        return this;
    }

    public TextureBuilder setFilterMinLinearMipmapNearest() {
        textureFilterMin = TextureFilters.LINEAR_MIPMAP_NEAREST;
        return this;
    }

    public TextureBuilder setFilterMinNearest() {
        textureFilterMin = TextureFilters.NEAREST;
        return this;
    }

    public TextureBuilder setFilterMinNearestMipmapLinear() {
        textureFilterMin = TextureFilters.NEAREST_MIPMAP_LINEAR;
        return this;
    }

    public TextureBuilder setFilterMinNearestMipmapNearest() {
        textureFilterMin = TextureFilters.NEAREST_MIPMAP_NEAREST;
        return this;
    }

    public TextureBuilder setFormatDepthComponent() {
        textureFormat = TextureFormats.DEPTH_COMPONENT;
        return this;
    }

    public TextureBuilder setFormatDepthStencil() {
        textureFormat = TextureFormats.DEPTH_STENCIL;
        return this;
    }

    public TextureBuilder setFormatR() {
        textureFormat = TextureFormats.R;
        return this;
    }

    public TextureBuilder setFormatRG() {
        textureFormat = TextureFormats.RG;
        return this;
    }

    public TextureBuilder setFormatRGB() {
        textureFormat = TextureFormats.RGB;
        return this;
    }

    public TextureBuilder setFormatRGBA() {
        textureFormat = TextureFormats.RGBA;
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
        textureInternalFormat = TextureInternalFormats.DEPTH_COMPONENT16;
        return this;
    }

    public TextureBuilder setInternalFormatDepthComponent24() {
        textureInternalFormat = TextureInternalFormats.DEPTH_COMPONENT24;
        return this;
    }

    public TextureBuilder setInternalFormatDepthComponent32() {
        textureInternalFormat = TextureInternalFormats.DEPTH_COMPONENT32;
        return this;
    }

    public TextureBuilder setInternalFormatDepth24Stencil8() {
        textureInternalFormat = TextureInternalFormats.DEPTH24_STENCIL8;
        return this;
    }

    public TextureBuilder setInternalFormatR16F() {
        textureInternalFormat = TextureInternalFormats.R16F;
        return this;
    }

    public TextureBuilder setInternalFormatR32F() {
        textureInternalFormat = TextureInternalFormats.R32F;
        return this;
    }

    public TextureBuilder setInternalFormatRG16F() {
        textureInternalFormat = TextureInternalFormats.RG16F;
        return this;
    }

    public TextureBuilder setInternalFormatRG32F() {
        textureInternalFormat = TextureInternalFormats.RG32F;
        return this;
    }

    public TextureBuilder setInternalFormatRGB10_A2() {
        textureInternalFormat = TextureInternalFormats.RGB10_A2;
        return this;
    }

    public TextureBuilder setInternalFormatRGB16F() {
        textureInternalFormat = TextureInternalFormats.RGB16F;
        return this;
    }

    public TextureBuilder setInternalFormatRGB32F() {
        textureInternalFormat = TextureInternalFormats.RGB32F;
        return this;
    }

    public TextureBuilder setInternalFormatRGB8() {
        textureInternalFormat = TextureInternalFormats.RGB8;
        return this;
    }

    public TextureBuilder setInternalFormatRGBA16F() {
        textureInternalFormat = TextureInternalFormats.RGBA16F;
        return this;
    }

    public TextureBuilder setInternalFormatRGBA32F() {
        textureInternalFormat = TextureInternalFormats.RGBA32F;
        return this;
    }

    public TextureBuilder setInternalFormatRGBA8() {
        textureInternalFormat = TextureInternalFormats.RGBA8;
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

    public TextureBuilder setLodBias(float lodBias) {
        this.lodBias = lodBias;
        return this;
    }

    public TextureBuilder setMaxLod(float maxLod) {
        this.maxLod = maxLod;
        return this;
    }

    public TextureBuilder setMinLod(float minLod) {
        this.minLod = minLod;
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
        textureType = TextureTargets.TEXTURE_2D;
        return this;
    }

    public TextureBuilder setTextureTarget3D() {
        textureType = TextureTargets.TEXTURE_3D;
        return this;
    }

    public TextureBuilder setTextureTargetCubeMap() {
        textureType = TextureTargets.TEXTURE_CUBE_MAP;
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
