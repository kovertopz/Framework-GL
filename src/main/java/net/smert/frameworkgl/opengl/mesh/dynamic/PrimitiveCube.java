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
package net.smert.frameworkgl.opengl.mesh.dynamic;

import net.smert.frameworkgl.opengl.constants.Primitives;
import net.smert.frameworkgl.opengl.mesh.Tessellator;
import net.smert.frameworkgl.utils.Color;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class PrimitiveCube extends AbstractDynamicMesh {

    @Override
    public void create(boolean reset, ConstructionInfo constructionInfo, Tessellator tessellator) {
        float halfX = constructionInfo.size.getX() * .5f;
        float halfY = constructionInfo.size.getY() * .5f;
        float halfZ = constructionInfo.size.getZ() * .5f;
        float sizeX = constructionInfo.size.getX();
        float sizeY = constructionInfo.size.getY();
        float sizeZ = constructionInfo.size.getZ();
        int qualityX = constructionInfo.quality.getX();
        int qualityY = constructionInfo.quality.getY();
        int qualityZ = constructionInfo.quality.getZ();
        float stepX = 1f / qualityX;
        float stepY = 1f / qualityY;
        float stepZ = 1f / qualityZ;
        final Color color0 = constructionInfo.getColor(0);

        // Reset
        if (reset) {
            tessellator.setConvertToTriangles(constructionInfo.convertToTriangles);
            tessellator.reset();
        }
        tessellator.setLocalPosition(constructionInfo.localPosition);

        tessellator.start(Primitives.QUADS);

        // Face +Z
        for (int x = 0; x < qualityX; x++) {
            for (int y = 0; y < qualityY; y++) {
                float startX = stepX * x;
                float startY = stepY * y;
                float xMin = -halfX + (startX * sizeX);
                float xMax = -halfX + ((startX + stepX) * sizeX);
                float yMin = -halfY + (startY * sizeY);
                float yMax = -halfY + ((startY + stepY) * sizeY);
                tessellator.addColor(color0);
                tessellator.addNormal(0f, 0f, 1f);
                tessellator.addVertex(xMax, yMax, halfZ);
                tessellator.addColor(color0);
                tessellator.addNormal(0f, 0f, 1f);
                tessellator.addVertex(xMin, yMax, halfZ);
                tessellator.addColor(color0);
                tessellator.addNormal(0f, 0f, 1f);
                tessellator.addVertex(xMin, yMin, halfZ);
                tessellator.addColor(color0);
                tessellator.addNormal(0f, 0f, 1f);
                tessellator.addVertex(xMax, yMin, halfZ);
            }
        }

        // Face +X
        for (int y = 0; y < qualityY; y++) {
            for (int z = 0; z < qualityZ; z++) {
                float startY = stepY * y;
                float startZ = stepZ * z;
                float yMin = -halfY + (startY * sizeY);
                float yMax = -halfY + ((startY + stepY) * sizeY);
                float zMin = -halfZ + (startZ * sizeZ);
                float zMax = -halfZ + ((startZ + stepZ) * sizeZ);
                tessellator.addColor(color0);
                tessellator.addNormal(1f, 0f, 0f);
                tessellator.addVertex(halfX, yMax, zMin);
                tessellator.addColor(color0);
                tessellator.addNormal(1f, 0f, 0f);
                tessellator.addVertex(halfX, yMax, zMax);
                tessellator.addColor(color0);
                tessellator.addNormal(1f, 0f, 0f);
                tessellator.addVertex(halfX, yMin, zMax);
                tessellator.addColor(color0);
                tessellator.addNormal(1f, 0f, 0f);
                tessellator.addVertex(halfX, yMin, zMin);
            }
        }

        // Face -Z
        for (int x = 0; x < qualityX; x++) {
            for (int y = 0; y < qualityY; y++) {
                float startX = stepX * x;
                float startY = stepY * y;
                float xMin = -halfX + (startX * sizeX);
                float xMax = -halfX + ((startX + stepX) * sizeX);
                float yMin = -halfY + (startY * sizeY);
                float yMax = -halfY + ((startY + stepY) * sizeY);
                tessellator.addColor(color0);
                tessellator.addNormal(0f, 0f, -1f);
                tessellator.addVertex(xMin, yMax, -halfZ);
                tessellator.addColor(color0);
                tessellator.addNormal(0f, 0f, -1f);
                tessellator.addVertex(xMax, yMax, -halfZ);
                tessellator.addColor(color0);
                tessellator.addNormal(0f, 0f, -1f);
                tessellator.addVertex(xMax, yMin, -halfZ);
                tessellator.addColor(color0);
                tessellator.addNormal(0f, 0f, -1f);
                tessellator.addVertex(xMin, yMin, -halfZ);
            }
        }

        // Face -X
        for (int y = 0; y < qualityY; y++) {
            for (int z = 0; z < qualityZ; z++) {
                float startY = stepY * y;
                float startZ = stepZ * z;
                float yMin = -halfY + (startY * sizeY);
                float yMax = -halfY + ((startY + stepY) * sizeY);
                float zMin = -halfZ + (startZ * sizeZ);
                float zMax = -halfZ + ((startZ + stepZ) * sizeZ);
                tessellator.addColor(color0);
                tessellator.addNormal(-1f, 0f, 0f);
                tessellator.addVertex(-halfX, yMax, zMax);
                tessellator.addColor(color0);
                tessellator.addNormal(-1f, 0f, 0f);
                tessellator.addVertex(-halfX, yMax, zMin);
                tessellator.addColor(color0);
                tessellator.addNormal(-1f, 0f, 0f);
                tessellator.addVertex(-halfX, yMin, zMin);
                tessellator.addColor(color0);
                tessellator.addNormal(-1f, 0f, 0f);
                tessellator.addVertex(-halfX, yMin, zMax);
            }
        }

        // Face +Y
        for (int x = 0; x < qualityX; x++) {
            for (int z = 0; z < qualityZ; z++) {
                float startX = stepX * x;
                float startZ = stepZ * z;
                float xMin = -halfX + (startX * sizeX);
                float xMax = -halfX + ((startX + stepX) * sizeX);
                float zMin = -halfZ + (startZ * sizeZ);
                float zMax = -halfZ + ((startZ + stepZ) * sizeZ);
                tessellator.addColor(color0);
                tessellator.addNormal(0f, 1f, 0f);
                tessellator.addVertex(xMax, halfY, zMin);
                tessellator.addColor(color0);
                tessellator.addNormal(0f, 1f, 0f);
                tessellator.addVertex(xMin, halfY, zMin);
                tessellator.addColor(color0);
                tessellator.addNormal(0f, 1f, 0f);
                tessellator.addVertex(xMin, halfY, zMax);
                tessellator.addColor(color0);
                tessellator.addNormal(0f, 1f, 0f);
                tessellator.addVertex(xMax, halfY, zMax);
            }
        }

        // Face -Y
        for (int x = 0; x < qualityX; x++) {
            for (int z = 0; z < qualityZ; z++) {
                float startX = stepX * x;
                float startZ = stepZ * z;
                float xMin = -halfX + (startX * sizeX);
                float xMax = -halfX + ((startX + stepX) * sizeX);
                float zMin = -halfZ + (startZ * sizeZ);
                float zMax = -halfZ + ((startZ + stepZ) * sizeZ);
                tessellator.addColor(color0);
                tessellator.addNormal(0f, -1f, 0f);
                tessellator.addVertex(xMax, -halfY, zMax);
                tessellator.addColor(color0);
                tessellator.addNormal(0f, -1f, 0f);
                tessellator.addVertex(xMin, -halfY, zMax);
                tessellator.addColor(color0);
                tessellator.addNormal(0f, -1f, 0f);
                tessellator.addVertex(xMin, -halfY, zMin);
                tessellator.addColor(color0);
                tessellator.addNormal(0f, -1f, 0f);
                tessellator.addVertex(xMax, -halfY, zMin);
            }
        }

        tessellator.stop();
        tessellator.addSegment("Primitive Box");
    }

}
