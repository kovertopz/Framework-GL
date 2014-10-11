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
package net.smert.frameworkgl.opengl.renderer;

import java.nio.FloatBuffer;
import java.util.List;
import net.smert.frameworkgl.gameobjects.GameObject;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderable.gl1.DisplayListRenderable;
import net.smert.frameworkgl.opengl.renderable.gl1.DynamicVertexBufferObjectRenderable;
import net.smert.frameworkgl.opengl.renderable.gl1.DynamicVertexBufferObjectRenderableInterleaved;
import net.smert.frameworkgl.opengl.renderable.gl1.ImmediateModeRenderable;
import net.smert.frameworkgl.opengl.renderable.gl1.VertexArrayRenderable;
import net.smert.frameworkgl.opengl.renderable.gl1.VertexBufferObjectRenderable;
import net.smert.frameworkgl.opengl.renderable.gl1.VertexBufferObjectRenderableInterleaved;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class LegacyRenderer {

    public DisplayListRenderable createDisplayListRenderable() {
        return GL.rf1.createDisplayList();
    }

    public DynamicVertexBufferObjectRenderable createDynamicVertexBufferObjectRenderable() {
        return GL.rf1.createDynamicVertexBufferObject();
    }

    public DynamicVertexBufferObjectRenderableInterleaved createDynamicVertexBufferObjectInterleavedRenderable() {
        return GL.rf1.createDynamicVertexBufferObjectInterleaved();
    }

    public ImmediateModeRenderable createImmediateModeRenderable() {
        return GL.rf1.createImmediateMode();
    }

    public VertexArrayRenderable createVertexArrayRenderable() {
        return GL.rf1.createVertexArray();
    }

    public VertexBufferObjectRenderable createVertexBufferObjectRenderable() {
        return GL.rf1.createVertexBufferObject();
    }

    public VertexBufferObjectRenderableInterleaved createVertexBufferObjectInterleavedRenderable() {
        return GL.rf1.createVertexBufferObjectInterleaved();
    }

    public void destroy() {
        Renderable.vaBindState.reset();
        Renderable.vboBindState.reset();
    }

    public void render(AbstractRenderable renderable, float x, float y, float z) {
        GL.o1.pushMatrix();
        GL.o1.translate(x, y, z);
        renderable.render();
        GL.o1.popMatrix();
    }

    public void render(AbstractRenderable renderable, FloatBuffer transformWorldFloatBuffer) {
        GL.o1.pushMatrix();
        GL.o1.multiplyMatrix(transformWorldFloatBuffer);
        renderable.render();
        GL.o1.popMatrix();
    }

    public void render(AbstractRenderable renderable, Vector3f position) {
        GL.o1.pushMatrix();
        GL.o1.translate(position.getX(), position.getY(), position.getZ());
        renderable.render();
        GL.o1.popMatrix();
    }

    public void render(GameObject gameObject, FloatBuffer transformWorldFloatBuffer) {
        gameObject.getWorldTransform().toFloatBuffer(transformWorldFloatBuffer);
        transformWorldFloatBuffer.flip();
        render(gameObject.getRenderable(), transformWorldFloatBuffer);
    }

    public void render(List<GameObject> gameObjects, FloatBuffer transformWorldFloatBuffer) {
        for (GameObject gameObject : gameObjects) {
            render(gameObject, transformWorldFloatBuffer);
        }
    }

    public void renderBlend(GameObject gameObject, FloatBuffer transformWorldFloatBuffer) {
        if (gameObject.getRenderableState().isOpaque()) {
            return;
        }
        GL.o1.enableBlending();
        gameObject.getWorldTransform().toFloatBuffer(transformWorldFloatBuffer);
        transformWorldFloatBuffer.flip();
        render(gameObject.getRenderable(), transformWorldFloatBuffer);
        GL.o1.disableBlending();
    }

    public void renderBlend(List<GameObject> gameObjects, FloatBuffer transformWorldFloatBuffer) {
        GL.o1.enableBlending();
        for (GameObject gameObject : gameObjects) {
            if (gameObject.getRenderableState().isOpaque()) {
                continue;
            }
            gameObject.getWorldTransform().toFloatBuffer(transformWorldFloatBuffer);
            transformWorldFloatBuffer.flip();
            render(gameObject.getRenderable(), transformWorldFloatBuffer);
        }
        GL.o1.disableBlending();
    }

    public void renderOpaque(GameObject gameObject, FloatBuffer transformWorldFloatBuffer) {
        if (!gameObject.getRenderableState().isOpaque()) {
            return;
        }
        gameObject.getWorldTransform().toFloatBuffer(transformWorldFloatBuffer);
        transformWorldFloatBuffer.flip();
        render(gameObject.getRenderable(), transformWorldFloatBuffer);
    }

    public void renderOpaque(List<GameObject> gameObjects, FloatBuffer transformWorldFloatBuffer) {
        for (GameObject gameObject : gameObjects) {
            if (!gameObject.getRenderableState().isOpaque()) {
                continue;
            }
            gameObject.getWorldTransform().toFloatBuffer(transformWorldFloatBuffer);
            transformWorldFloatBuffer.flip();
            render(gameObject.getRenderable(), transformWorldFloatBuffer);
        }
    }

}
