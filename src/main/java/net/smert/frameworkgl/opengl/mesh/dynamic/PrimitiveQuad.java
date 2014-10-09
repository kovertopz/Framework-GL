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

import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.constants.Primitives;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class PrimitiveQuad extends AbstractDynamicMesh {

    @Override
    public void create(boolean reset, ConstructionInfo constructionInfo) {
        float halfX = constructionInfo.size.getX() * .5f;
        float halfY = constructionInfo.size.getY() * .5f;
        float sizeX = constructionInfo.size.getX();
        float sizeY = constructionInfo.size.getY();
        float texCoordMinX = constructionInfo.texCoordMinMaxX.getX();
        float texCoordMinY = constructionInfo.texCoordMinMaxY.getX();
        float texCoordMaxX = constructionInfo.texCoordMinMaxX.getY();
        float texCoordMaxY = constructionInfo.texCoordMinMaxY.getY();
        int qualityX = constructionInfo.quality.getX();
        int qualityY = constructionInfo.quality.getY();
        float stepX = 1f / qualityX;
        float stepY = 1f / qualityY;
        float texCoordSizeX = texCoordMaxX - texCoordMinX;
        float texCoordSizeY = texCoordMaxY - texCoordMinY;

        // Reset
        if (reset == true) {
            GL.tessellator.setConvertToTriangles(constructionInfo.convertToTriangles);
            GL.tessellator.reset();
        }
        GL.tessellator.setLocalPosition(constructionInfo.localPosition);

        GL.tessellator.start(Primitives.QUADS);

        // Face +Z
        for (int x = 0; x < qualityX; x++) {
            for (int y = 0; y < qualityY; y++) {
                float startX = stepX * x;
                float startY = stepY * y;
                float texCoordMinNewX = texCoordMinX + (startX * texCoordSizeX);
                float texCoordMaxNewX = texCoordMinX + ((startX + stepX) * texCoordSizeX);
                float texCoordMinNewY = texCoordMaxY - ((startY + stepY) * texCoordSizeY);
                float texCoordMaxNewY = texCoordMaxY - (startY * texCoordSizeY);
                float xMin = -halfX + (startX * sizeX);
                float xMax = -halfX + ((startX + stepX) * sizeX);
                float yMin = -halfY + (startY * sizeY);
                float yMax = -halfY + ((startY + stepY) * sizeY);
                GL.tessellator.addTexCoord(texCoordMaxNewX, texCoordMaxNewY);
                GL.tessellator.addVertex(xMax, yMax, 0f);
                GL.tessellator.addTexCoord(texCoordMinNewX, texCoordMaxNewY);
                GL.tessellator.addVertex(xMin, yMax, 0f);
                GL.tessellator.addTexCoord(texCoordMinNewX, texCoordMinNewY);
                GL.tessellator.addVertex(xMin, yMin, 0f);
                GL.tessellator.addTexCoord(texCoordMaxNewX, texCoordMinNewY);
                GL.tessellator.addVertex(xMax, yMin, 0f);
            }
        }

        GL.tessellator.stop();
        GL.tessellator.addSegment("Primitive Quad");
    }

}
