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
package net.smert.frameworkgl.opengl.renderable.shared;

import java.nio.ByteBuffer;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public interface MultipleBuffers {

    public void createColor(int bufferSize);

    public void createInterleaved(int bufferSize);

    public void createNormal(int bufferSize);

    public void createTexCoord(int bufferSize);

    public void createVertex(int bufferSize);

    public void createVertexIndex(int bufferSize);

    public ByteBuffer getColor();

    public ByteBuffer getInterleaved();

    public ByteBuffer getNormal();

    public ByteBuffer getTexCoord();

    public ByteBuffer getVertex();

    public ByteBuffer getVertexIndex();

}
