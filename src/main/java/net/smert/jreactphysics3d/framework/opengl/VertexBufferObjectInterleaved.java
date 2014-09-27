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

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class VertexBufferObjectInterleaved extends VertexBufferObject {

    private int colorOffsetBytes;
    private int normalOffsetBytes;
    private int strideBytes;
    private int texCoordOffsetBytes;
    private int vertexOffsetBytes;

    public VertexBufferObjectInterleaved() {
        super();
        colorOffsetBytes = 0;
        normalOffsetBytes = 0;
        strideBytes = 0;
        texCoordOffsetBytes = 0;
        vertexOffsetBytes = 0;
    }

    public int getColorOffsetBytes() {
        return colorOffsetBytes;
    }

    public void setColorOffsetBytes(int colorOffsetBytes) {
        this.colorOffsetBytes = colorOffsetBytes;
    }

    public int getNormalOffsetBytes() {
        return normalOffsetBytes;
    }

    public void setNormalOffsetBytes(int normalOffsetBytes) {
        this.normalOffsetBytes = normalOffsetBytes;
    }

    public int getStrideBytes() {
        return strideBytes;
    }

    public void setStrideBytes(int strideBytes) {
        this.strideBytes = strideBytes;
    }

    public int getTexCoordOffsetBytes() {
        return texCoordOffsetBytes;
    }

    public void setTexCoordOffsetBytes(int texCoordOffsetBytes) {
        this.texCoordOffsetBytes = texCoordOffsetBytes;
    }

    public int getVertexOffsetBytes() {
        return vertexOffsetBytes;
    }

    public void setVertexOffsetBytes(int vertexOffsetBytes) {
        this.vertexOffsetBytes = vertexOffsetBytes;
    }

}
