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
package net.smert.frameworkgl.opengl.fbo;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import net.smert.frameworkgl.opengl.FrameBufferObject;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.Texture;
import net.smert.frameworkgl.opengl.constants.FrameBufferObjectAttachments;
import net.smert.frameworkgl.opengl.constants.TextureFormats;
import net.smert.frameworkgl.opengl.constants.TextureInternalFormats;
import net.smert.frameworkgl.opengl.constants.TextureTargets;
import net.smert.frameworkgl.utils.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason
 */
public class FrameBufferObjectBuilder {

    private final static Logger log = LoggerFactory.getLogger(FrameBufferObjectBuilder.class);

    private int fboHeight;
    private int fboWidth;
    private int maxColorAttachments;
    private int maxDepthAttachments;
    private int maxStencilAttachments;
    private int numberOfColorAttachments;
    private int numberOfDepthAttachments;
    private int numberOfStencilAttachments;
    private int readBufferAttachment;
    private FrameBufferObject frameBufferObject;
    private IntBuffer multipleRenderTarget;
    private final List<Texture> textures;

    public FrameBufferObjectBuilder() {
        textures = new ArrayList<>();
        reset();
    }

    private void checkAttachmentForErrors(int attachment) {
        if ((attachment >= FrameBufferObjectAttachments.COLOR_ATTACHMENT0)
                && (attachment <= FrameBufferObjectAttachments.COLOR_ATTACHMENT15)) {
            if (numberOfColorAttachments == maxColorAttachments) {
                throw new IllegalStateException("Max color attachments exceeded");
            }
            numberOfColorAttachments++;
        } else if (attachment == FrameBufferObjectAttachments.DEPTH_ATTACHMENT) {
            if (numberOfDepthAttachments == maxDepthAttachments) {
                throw new IllegalStateException("Max depth attachments exceeded");
            }
            numberOfDepthAttachments++;
        } else if (attachment == FrameBufferObjectAttachments.STENCIL_ATTACHMENT) {
            if (numberOfDepthAttachments == maxDepthAttachments) {
                throw new IllegalStateException("Max depth attachments exceeded");
            }
            if (numberOfStencilAttachments == maxStencilAttachments) {
                throw new IllegalStateException("Max stencil attachments exceeded");
            }
            numberOfDepthAttachments++;
            numberOfStencilAttachments++;
        }
    }

    public FrameBufferObjectBuilder attachNewTexture() {
        int attachment = FrameBufferObjectAttachments.COLOR_ATTACHMENT0 + textures.size();

        // Select an attachment based on the texture internal format. Also configure
        // the format and GL Type of the texture.
        switch (GL.textureBuilder.getTextureInternalFormat()) {
            case TextureInternalFormats.DEPTH_COMPONENT16:
                attachment = FrameBufferObjectAttachments.DEPTH_ATTACHMENT;
                GL.textureBuilder.setFormatDepthComponent();
                GL.textureBuilder.setPrimitiveShort();
                break;

            case TextureInternalFormats.DEPTH_COMPONENT24:
            case TextureInternalFormats.DEPTH_COMPONENT32:
                attachment = FrameBufferObjectAttachments.DEPTH_ATTACHMENT;
                GL.textureBuilder.setFormatDepthComponent();
                GL.textureBuilder.setPrimitiveInt();
                break;

            case TextureInternalFormats.DEPTH24_STENCIL8:
                attachment = FrameBufferObjectAttachments.STENCIL_ATTACHMENT;
                GL.textureBuilder.setFormatDepthStencil();
                GL.textureBuilder.setPrimitiveStencil();
                break;

            case TextureInternalFormats.R16F:
            case TextureInternalFormats.R32F:
                GL.textureBuilder.setFormatR();
                GL.textureBuilder.setPrimitiveFloat();
                break;

            case TextureInternalFormats.RG16F:
            case TextureInternalFormats.RG32F:
                GL.textureBuilder.setFormatRG();
                GL.textureBuilder.setPrimitiveFloat();
                break;

            case TextureInternalFormats.RGB10_A2:
                GL.textureBuilder.setFormatRGBA();
                GL.textureBuilder.setPrimitiveInt1010102();
                break;

            case TextureInternalFormats.RGB16F:
            case TextureInternalFormats.RGB32F:
                GL.textureBuilder.setFormatRGB();
                GL.textureBuilder.setPrimitiveFloat();
                break;

            case TextureInternalFormats.RGB8:
                GL.textureBuilder.setFormatRGB();
                GL.textureBuilder.setPrimitiveByte();
                break;

            case TextureInternalFormats.RGBA16F:
            case TextureInternalFormats.RGBA32F:
                GL.textureBuilder.setFormatRGBA();
                GL.textureBuilder.setPrimitiveFloat();
                break;

            case TextureInternalFormats.RGBA8:
                GL.textureBuilder.setFormatRGBA();
                GL.textureBuilder.setPrimitiveByte();
                break;

            default:
                throw new IllegalArgumentException("Unknown texture internal format: "
                        + GL.textureBuilder.getTextureInternalFormat());
        }

        checkAttachmentForErrors(attachment);

        // Switch texture target of the FBO
        switch (GL.textureBuilder.getTextureType()) {
            case TextureTargets.TEXTURE_2D:
                GL.fboHelper.setTextureTarget2D();
                break;

            case TextureTargets.TEXTURE_3D:
                GL.fboHelper.setTextureTarget3D();
                break;

            case TextureTargets.TEXTURE_CUBE_MAP:
                break;
        }

        // Build texture
        GL.textureBuilder.
                setHeight(fboHeight).
                setWidth(fboWidth).
                setClampingWrapRClampToEdge().
                setClampingWrapSClampToEdge().
                setClampingWrapTClampToEdge().
                setNullData(true).
                buildTexture();

        // Create texture
        Texture texture = GL.textureBuilder.createTexture(true);

        // Add texture to list of textures
        textures.add(texture);

        // Attach texture to FBO
        if (attachment == FrameBufferObjectAttachments.STENCIL_ATTACHMENT) {
            GL.fboHelper.attachTexture(FrameBufferObjectAttachments.DEPTH_ATTACHMENT, texture.getTextureID());
        }
        GL.fboHelper.attachTexture(attachment, texture.getTextureID());

        return this;
    }

    public FrameBufferObjectBuilder attachTexture(Texture texture) {
        int attachment;

        // Select an attachment based on the texture format
        switch (GL.textureBuilder.getTextureFormat()) {
            case TextureFormats.DEPTH_COMPONENT:
                attachment = FrameBufferObjectAttachments.DEPTH_ATTACHMENT;
                break;

            case TextureFormats.DEPTH_STENCIL:
                attachment = FrameBufferObjectAttachments.STENCIL_ATTACHMENT;
                break;

            case TextureFormats.R:
            case TextureFormats.RG:
            case TextureFormats.RGB:
            case TextureFormats.RGBA:
                attachment = FrameBufferObjectAttachments.COLOR_ATTACHMENT0 + textures.size();
                break;

            default:
                throw new IllegalArgumentException("Unknown texture format: " + GL.textureBuilder.getTextureFormat());
        }

        checkAttachmentForErrors(attachment);

        // Switch texture target of the FBO
        switch (GL.textureBuilder.getTextureType()) {
            case TextureTargets.TEXTURE_2D:
                GL.fboHelper.setTextureTarget2D();
                break;

            case TextureTargets.TEXTURE_3D:
                GL.fboHelper.setTextureTarget3D();
                break;

            case TextureTargets.TEXTURE_CUBE_MAP:
                break;
        }

        // Reset texture builder
        GL.textureBuilder.reset();

        // Add texture to list of textures
        textures.add(texture);

        // Attach texture
        if (attachment == FrameBufferObjectAttachments.STENCIL_ATTACHMENT) {
            GL.fboHelper.attachTexture(FrameBufferObjectAttachments.DEPTH_ATTACHMENT, texture.getTextureID());
        }
        GL.fboHelper.attachTexture(attachment, texture.getTextureID());

        return this;
    }

    public FrameBufferObjectBuilder buildBegin() {

        // Create new FBO
        frameBufferObject = GL.glFactory.createFrameBufferObject();
        frameBufferObject.create();

        // Bind FBO
        GL.fboHelper.bind(frameBufferObject.getFboID());
        return this;
    }

    public FrameBufferObjectBuilder buildEnd(String name) {

        // Set FBO draw and read buffers
        if (numberOfColorAttachments == 0) {
            log.debug("Frame buffer object has no color buffers.");
            GL.fboHelper.disableBuffers();
        } else {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < numberOfColorAttachments; i++) {
                list.add(FrameBufferObjectAttachments.COLOR_ATTACHMENT0 + i);
            }

            int[] intArray = ListUtils.ToPrimitiveIntArray(list);
            multipleRenderTarget = GL.bufferHelper.createIntBuffer(intArray.length);
            multipleRenderTarget.put(intArray).flip();

            GL.fboHelper.drawBuffer(multipleRenderTarget);
            GL.fboHelper.readBuffer(readBufferAttachment);
        }

        // Check to see if the FBO is complete (built successfully)
        int frameBufferObjectStatus = GL.fboHelper.checkStatus();
        if (frameBufferObjectStatus != FrameBufferObjectAttachments.FRAMEBUFFER_COMPLETE) {
            throw new RuntimeException("Unable to create frame buffer object: " + frameBufferObjectStatus);
        }

        // Unbind FBO
        GL.fboHelper.unbind();

        log.info("Frame buffer object \"{}\" was successfully built.", name);

        return this;
    }

    public FrameBufferObject createFrameBufferObject(boolean reset) {
        FrameBufferObject temp = frameBufferObject;
        if (reset == true) {
            reset();
            GL.textureBuilder.reset();
        }
        return temp;
    }

    public void init() {
        maxColorAttachments = GL.o3.getMaxColorAttachments();
        maxDepthAttachments = 1;
        maxStencilAttachments = 1;
    }

    public final FrameBufferObjectBuilder reset() {
        fboHeight = 0;
        fboWidth = 0;
        numberOfColorAttachments = 0;
        numberOfDepthAttachments = 0;
        numberOfStencilAttachments = 0;
        readBufferAttachment = FrameBufferObjectAttachments.NONE;
        frameBufferObject = null;
        multipleRenderTarget = null;
        textures.clear();
        return this;
    }

    public FrameBufferObjectBuilder setFilterMagLinear() {
        GL.textureBuilder.setFilterMagLinear();
        return this;
    }

    public FrameBufferObjectBuilder setFilterMagNearest() {
        GL.textureBuilder.setFilterMagNearest();
        return this;
    }

    public FrameBufferObjectBuilder setFilterMinLinear() {
        GL.textureBuilder.setFilterMinLinear();
        return this;
    }

    public FrameBufferObjectBuilder setFilterMinLinearMipmapLinear() {
        GL.textureBuilder.setFilterMinLinearMipmapLinear();
        return this;
    }

    public FrameBufferObjectBuilder setFilterMinLinearMipmapNearest() {
        GL.textureBuilder.setFilterMinLinearMipmapNearest();
        return this;
    }

    public FrameBufferObjectBuilder setFilterMinNearest() {
        GL.textureBuilder.setFilterMinNearest();
        return this;
    }

    public FrameBufferObjectBuilder setFilterMinNearestMipmapLinear() {
        GL.textureBuilder.setFilterMinNearestMipmapLinear();
        return this;
    }

    public FrameBufferObjectBuilder setFilterMinNearestMipmapNearest() {
        GL.textureBuilder.setFilterMinNearestMipmapNearest();
        return this;
    }

    public FrameBufferObjectBuilder setFormatDepthComponent() {
        GL.textureBuilder.setFormatDepthComponent();
        return this;
    }

    public FrameBufferObjectBuilder setFormatDepthStencil() {
        GL.textureBuilder.setFormatDepthStencil();
        return this;
    }

    public FrameBufferObjectBuilder setFormatR() {
        GL.textureBuilder.setFormatR();
        return this;
    }

    public FrameBufferObjectBuilder setFormatRG() {
        GL.textureBuilder.setFormatRG();
        return this;
    }

    public FrameBufferObjectBuilder setFormatRGB() {
        GL.textureBuilder.setFormatRGB();
        return this;
    }

    public FrameBufferObjectBuilder setFormatRGBA() {
        GL.textureBuilder.setFormatRGBA();
        return this;
    }

    public FrameBufferObjectBuilder setHeight(int height) {
        this.fboHeight = height;
        return this;
    }

    public FrameBufferObjectBuilder setInternalFormatDepthComponent16() {
        GL.textureBuilder.setInternalFormatDepthComponent16();
        return this;
    }

    public FrameBufferObjectBuilder setInternalFormatDepthComponent24() {
        GL.textureBuilder.setInternalFormatDepthComponent24();
        return this;
    }

    public FrameBufferObjectBuilder setInternalFormatDepthComponent32() {
        GL.textureBuilder.setInternalFormatDepthComponent32();
        return this;
    }

    public FrameBufferObjectBuilder setInternalFormatDepth24Stencil8() {
        GL.textureBuilder.setInternalFormatDepth24Stencil8();
        return this;
    }

    public FrameBufferObjectBuilder setInternalFormatR16F() {
        GL.textureBuilder.setInternalFormatR16F();
        return this;
    }

    public FrameBufferObjectBuilder setInternalFormatR32F() {
        GL.textureBuilder.setInternalFormatR32F();
        return this;
    }

    public FrameBufferObjectBuilder setInternalFormatRG16F() {
        GL.textureBuilder.setInternalFormatRG16F();
        return this;
    }

    public FrameBufferObjectBuilder setInternalFormatRG32F() {
        GL.textureBuilder.setInternalFormatRG32F();
        return this;
    }

    public FrameBufferObjectBuilder setInternalFormatRGB10_A2() {
        GL.textureBuilder.setInternalFormatRGB10_A2();
        return this;
    }

    public FrameBufferObjectBuilder setInternalFormatRGB16F() {
        GL.textureBuilder.setInternalFormatRGB16F();
        return this;
    }

    public FrameBufferObjectBuilder setInternalFormatRGB32F() {
        GL.textureBuilder.setInternalFormatRGB32F();
        return this;
    }

    public FrameBufferObjectBuilder setInternalFormatRGB8() {
        GL.textureBuilder.setInternalFormatRGB8();
        return this;
    }

    public FrameBufferObjectBuilder setInternalFormatRGBA16F() {
        GL.textureBuilder.setInternalFormatRGBA16F();
        return this;
    }

    public FrameBufferObjectBuilder setInternalFormatRGBA32F() {
        GL.textureBuilder.setInternalFormatRGBA32F();
        return this;
    }

    public FrameBufferObjectBuilder setInternalFormatRGBA8() {
        GL.textureBuilder.setInternalFormatRGBA8();
        return this;
    }

    public FrameBufferObjectBuilder setReadBufferAttachmentColorAttachment0() {
        readBufferAttachment = FrameBufferObjectAttachments.COLOR_ATTACHMENT0;
        return this;
    }

    public FrameBufferObjectBuilder setReadBufferAttachmentNone() {
        readBufferAttachment = FrameBufferObjectAttachments.NONE;
        return this;
    }

    public FrameBufferObjectBuilder setTextureTarget2D() {
        GL.fboHelper.setTextureTarget2D();
        GL.textureBuilder.setTextureTarget2D();
        return this;
    }

    public FrameBufferObjectBuilder setTextureTarget3D() {
        GL.fboHelper.setTextureTarget3D();
        GL.textureBuilder.setTextureTarget3D();
        return this;
    }

    public FrameBufferObjectBuilder setTextureTargetCubeMap() {
        GL.fboHelper.setTextureTargetCubeMap();
        GL.textureBuilder.setTextureTargetCubeMap();
        return this;
    }

    public FrameBufferObjectBuilder setTextureTargetCubeMapNegativeX() {
        GL.fboHelper.setTextureTargetCubeMapNegativeX();
        return this;
    }

    public FrameBufferObjectBuilder setTextureTargetCubeMapPositiveX() {
        GL.fboHelper.setTextureTargetCubeMapPositiveX();
        return this;
    }

    public FrameBufferObjectBuilder setTextureTargetCubeMapNegativeY() {
        GL.fboHelper.setTextureTargetCubeMapNegativeY();
        return this;
    }

    public FrameBufferObjectBuilder setTextureTargetCubeMapPositiveY() {
        GL.fboHelper.setTextureTargetCubeMapPositiveY();
        return this;
    }

    public FrameBufferObjectBuilder setTextureTargetCubeMapNegativeZ() {
        GL.fboHelper.setTextureTargetCubeMapNegativeZ();
        return this;
    }

    public FrameBufferObjectBuilder setTextureTargetCubeMapPositiveZ() {
        GL.fboHelper.setTextureTargetCubeMapPositiveZ();
        return this;
    }

    public FrameBufferObjectBuilder setUseMipmaps(boolean enabled) {
        GL.textureBuilder.setUseMipmaps(enabled);
        return this;
    }

    public FrameBufferObjectBuilder setWidth(int width) {
        this.fboWidth = width;
        return this;
    }

}
