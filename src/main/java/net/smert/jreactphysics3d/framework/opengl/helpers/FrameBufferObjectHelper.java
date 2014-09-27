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

import java.nio.IntBuffer;
import net.smert.jreactphysics3d.framework.opengl.constants.TextureTargets;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class FrameBufferObjectHelper {

    private int textureTarget = TextureTargets.TEXTURE_2D;

    public void attachRenderBuffer(int attachment, int rboid) {
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, attachment, GL30.GL_RENDERBUFFER, rboid);
    }

    public void attachTexture(int attachment, int textureid) {
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, attachment, textureTarget, textureid, 0);
    }

    public void bind(int fboid) {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fboid);
    }

    public void blit(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {
        GL30.glBlitFramebuffer(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
    }

    public int checkStatus() {
        return GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);
    }

    public int create() {
        return GL30.glGenFramebuffers();
    }

    public void delete(int fboid) {
        GL30.glDeleteFramebuffers(fboid);
    }

    public void disableBuffers() {
        GL11.glDrawBuffer(GL11.GL_NONE);
        GL11.glReadBuffer(GL11.GL_NONE);
    }

    public void disableDrawBuffer() {
        GL11.glDrawBuffer(GL11.GL_NONE);
    }

    public void disableReadBuffer() {
        GL11.glReadBuffer(GL11.GL_NONE);
    }

    public void drawBuffer(int mode) {
        GL11.glDrawBuffer(mode);
    }

    public void drawBuffer(IntBuffer intbuffer) {
        GL20.glDrawBuffers(intbuffer);
    }

    public void generateMipmap(int textarget) {
        GL30.glGenerateMipmap(textarget);
    }

    public void readBuffer(int mode) {
        GL11.glReadBuffer(mode);
    }

    public void setBlitTargets(int sourcefboid, int destinationfboid) {
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, destinationfboid);
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, sourcefboid);
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
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    public void unbindBlitTargets() {
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, 0);
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
    }

}
