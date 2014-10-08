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
package net.smert.frameworkgl.opengl.mesh;

import net.smert.frameworkgl.math.MathHelper;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.constants.GLTypes;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderable.RenderableConfiguration;
import net.smert.frameworkgl.opengl.renderable.gl1.DrawCommands;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class DrawCommandsConversion implements DrawCommands {

    private byte convertFloatToByte(float value) {
        return (byte) (MathHelper.Clamp(value, 0f, 1f) * 255);
    }

    @Override
    public void execCommands(Mesh mesh) {
        assert (mesh != null);

        RenderableConfiguration config = Renderable.configPool.get(mesh.getRenderableConfigID());

        // For each segment in the mesh
        for (int i = 0; i < mesh.getTotalSegments(); i++) {
            Segment segment = mesh.getSegment(i);

            // Begin
            GL.renderHelper.begin(segment.getPrimitiveMode());

            float[] colors = segment.getColors();
            float[] normals = segment.getNormals();
            float[] texCoords = segment.getTexCoords();
            float[] vertices = segment.getVertices();

            // For each vertex in the segment
            for (int j = 0; j < segment.getElementCount(); j++) {

                // For each type call the render helper
                if (mesh.hasColors()) {
                    int offset = config.getColorSize() * j;
                    switch (config.getColorSize()) {
                        case 3:
                            switch (config.getColorType()) {
                                case GLTypes.BYTE:
                                case GLTypes.UNSIGNED_BYTE:
                                    byte r = convertFloatToByte(colors[offset + 0]);
                                    byte g = convertFloatToByte(colors[offset + 1]);
                                    byte b = convertFloatToByte(colors[offset + 2]);
                                    GL.renderHelper.color(r, g, b);
                                    break;
                                case GLTypes.FLOAT:
                                    GL.renderHelper.color(
                                            colors[offset + 0], colors[offset + 1], colors[offset + 2]);
                                    break;
                                default:
                                    throw new IllegalArgumentException(
                                            "Unknown GL type constant for color: " + config.getColorType());
                            }
                            break;

                        case 4:
                            switch (config.getColorType()) {
                                case GLTypes.BYTE:
                                case GLTypes.UNSIGNED_BYTE:
                                    byte r = convertFloatToByte(colors[offset + 0]);
                                    byte g = convertFloatToByte(colors[offset + 1]);
                                    byte b = convertFloatToByte(colors[offset + 2]);
                                    byte a = convertFloatToByte(colors[offset + 3]);
                                    GL.renderHelper.color(r, g, b, a);
                                    break;
                                case GLTypes.FLOAT:
                                    GL.renderHelper.color(
                                            colors[offset + 0], colors[offset + 1], colors[offset + 2], colors[offset + 3]);
                                    break;
                                default:
                                    throw new IllegalArgumentException(
                                            "Unknown GL type constant for color: " + config.getColorType());
                            }
                            break;

                        default:
                            throw new IllegalArgumentException("Unknown color size: " + config.getColorSize());
                    }
                }
                if (mesh.hasNormals()) {
                    int offset = config.getNormalSize() * j;
                    GL.renderHelper.normal(normals[offset + 0], normals[offset + 1], normals[offset + 2]);
                }
                if (mesh.hasTexCoords()) {
                    int offset = config.getTexCoordSize() * j;
                    GL.renderHelper.texCoord(texCoords[offset + 0], texCoords[offset + 1], texCoords[offset + 2]);
                }
                if (mesh.hasVertices()) {
                    int offset = config.getVertexSize() * j;
                    GL.renderHelper.vertex(vertices[offset + 0], vertices[offset + 1], vertices[offset + 2]);
                }
            }

            // End
            GL.renderHelper.end();
        }
    }

}
