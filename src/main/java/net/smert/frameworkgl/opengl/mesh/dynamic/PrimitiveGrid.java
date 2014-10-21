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
public class PrimitiveGrid extends AbstractDynamicMesh {

    @Override
    public void create(boolean reset, ConstructionInfo constructionInfo, Tessellator tessellator) {
        float numLinesX = constructionInfo.size.getX() * .5f;
        float numLinesZ = constructionInfo.size.getZ() * .5f;
        float stepX = 1f / constructionInfo.quality.getX();
        float stepZ = 1f / constructionInfo.quality.getZ();
        final Color color0 = constructionInfo.getColor(0);
        final Color color1 = constructionInfo.getColor(1);
        final Color color2 = constructionInfo.getColor(2);

        // Reset
        if (reset) {
            tessellator.setConvertToTriangles(constructionInfo.convertToTriangles);
            tessellator.reset();
        }
        tessellator.setLocalPosition(constructionInfo.localPosition);

        tessellator.start(Primitives.LINES);

        for (float i = 1; i <= numLinesX; i++) {
            tessellator.addColor(color0);
            tessellator.addNormal(0f, 1f, 0f);
            tessellator.addVertex(-numLinesX, 0f, i * stepX);
            tessellator.addColor(color0);
            tessellator.addNormalAgain();
            tessellator.addVertex(numLinesX, 0f, i * stepX);
            tessellator.addColor(color0);
            tessellator.addNormalAgain();
            tessellator.addVertex(-numLinesX, 0f, -i * stepX);
            tessellator.addColor(color0);
            tessellator.addNormalAgain();
            tessellator.addVertex(numLinesX, 0f, -i * stepX);
        }

        for (float i = 1; i <= numLinesZ; i++) {
            tessellator.addColor(color0);
            tessellator.addNormalAgain();
            tessellator.addVertex(i * stepZ, 0f, -numLinesZ);
            tessellator.addColor(color0);
            tessellator.addNormalAgain();
            tessellator.addVertex(i * stepZ, 0f, numLinesZ);
            tessellator.addColor(color0);
            tessellator.addNormalAgain();
            tessellator.addVertex(-i * stepZ, 0f, -numLinesZ);
            tessellator.addColor(color0);
            tessellator.addNormalAgain();
            tessellator.addVertex(-i * stepZ, 0f, numLinesZ);
        }

        // Middle X
        tessellator.addColor(color1);
        tessellator.addNormalAgain();
        tessellator.addVertex(-numLinesX, 0f, 0f);
        tessellator.addColor(color1);
        tessellator.addNormalAgain();
        tessellator.addVertex(numLinesX, 0f, 0f);
        // Middle Z
        tessellator.addColor(color2);
        tessellator.addNormalAgain();
        tessellator.addVertex(0f, 0f, -numLinesZ);
        tessellator.addColor(color2);
        tessellator.addNormalAgain();
        tessellator.addVertex(0f, 0f, numLinesZ);

        tessellator.stop();
        tessellator.addSegment("Primitive Grid");
    }

}
