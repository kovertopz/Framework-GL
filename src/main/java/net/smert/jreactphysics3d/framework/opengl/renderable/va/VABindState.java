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
package net.smert.jreactphysics3d.framework.opengl.renderable.va;

import java.nio.ByteBuffer;
import net.smert.jreactphysics3d.framework.opengl.GL;
import net.smert.jreactphysics3d.framework.opengl.renderable.factory.Renderable;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VABindState {

    private boolean colorEnabled;
    private boolean normalEnabled;
    private boolean texCoordEnabled;
    private boolean vertexEnabled;

    public VABindState() {
        reset();
    }

    private void setColorEnabled(boolean enabled) {
        if (colorEnabled == enabled) {
            return;
        }
        colorEnabled = enabled;
        if (enabled) {
            GL.vaHelper.enableColors();
        } else {
            GL.vaHelper.disableColors();
        }
    }

    private void setNormalEnabled(boolean enabled) {
        if (normalEnabled == enabled) {
            return;
        }
        normalEnabled = enabled;
        if (enabled) {
            GL.vaHelper.enableNormals();
        } else {
            GL.vaHelper.disableNormals();
        }
    }

    private void setTextureCoordinateEnabled(boolean enabled) {
        if (texCoordEnabled == enabled) {
            return;
        }
        texCoordEnabled = enabled;
        if (enabled) {
            GL.vaHelper.enableTextureCoordinates();
        } else {
            GL.vaHelper.disableTextureCoordinates();
        }
    }

    private void setVertexEnabled(boolean enabled) {
        if (vertexEnabled == enabled) {
            return;
        }
        vertexEnabled = enabled;
        if (enabled) {
            GL.vaHelper.enableVertices();
        } else {
            GL.vaHelper.disableVertices();
        }
    }

    public void bindColor(ByteBuffer colorByteBuffer) {
        if (colorByteBuffer == null) {
            setColorEnabled(false);
            return;
        }
        int colorSize = Renderable.config.getColorSize();
        int colorType = Renderable.config.getColorType();
        setColorEnabled(true);
        GL.vaHelper.bindColors(colorSize, colorType, colorByteBuffer);
    }

    public void bindNormal(ByteBuffer normalByteBuffer) {
        if (normalByteBuffer == null) {
            setNormalEnabled(false);
            return;
        }
        int normalType = Renderable.config.getNormalType();
        setNormalEnabled(true);
        GL.vaHelper.bindNormals(normalType, normalByteBuffer);
    }

    public void bindTextureCoordinate(ByteBuffer texCoordByteBuffer) {
        if (texCoordByteBuffer == null) {
            setTextureCoordinateEnabled(false);
            return;
        }
        int texCoordSize = Renderable.config.getTexCoordSize();
        int texCoordType = Renderable.config.getTexCoordType();
        setTextureCoordinateEnabled(true);
        GL.vaHelper.bindTextureCoordinates(texCoordSize, texCoordType, texCoordByteBuffer);
    }

    public void bindVertex(ByteBuffer vertexByteBuffer) {
        if (vertexByteBuffer == null) {
            setVertexEnabled(false);
            return;
        }
        int vertexSize = Renderable.config.getVertexSize();
        int vertexType = Renderable.config.getVertexType();
        setVertexEnabled(true);
        GL.vaHelper.bindVertices(vertexSize, vertexType, vertexByteBuffer);
    }

    public final void reset() {
        colorEnabled = false;
        normalEnabled = false;
        texCoordEnabled = false;
        vertexEnabled = false;
    }

    public void unbind() {
        bindColor(null);
        bindNormal(null);
        bindTextureCoordinate(null);
        bindVertex(null);
    }

}
