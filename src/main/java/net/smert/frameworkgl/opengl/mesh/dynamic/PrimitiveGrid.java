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
import net.smert.frameworkgl.utils.Color;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class PrimitiveGrid extends AbstractDynamicMesh {

    @Override
    public void create(boolean reset, ConstructionInfo constructionInfo) {
        float numLinesX = constructionInfo.size.getX() * .5f;
        float numLinesZ = constructionInfo.size.getZ() * .5f;
        float stepX = 1f / constructionInfo.quality.getX();
        float stepZ = 1f / constructionInfo.quality.getZ();
        final Color color0 = constructionInfo.getColor(0);
        final Color color1 = constructionInfo.getColor(1);
        final Color color2 = constructionInfo.getColor(2);

        // Reset
        if (reset == true) {
            GL.tessellator.setConvertToTriangles(constructionInfo.convertToTriangles);
            GL.tessellator.reset();
        }
        GL.tessellator.setLocalPosition(constructionInfo.localPosition);

        GL.tessellator.start(Primitives.LINES);

        for (float i = 1; i <= numLinesX; i++) {
            GL.tessellator.addColor(color0);
            GL.tessellator.addNormal(0f, 1f, 0f);
            GL.tessellator.addVertex(-numLinesX, 0f, i * stepX);
            GL.tessellator.addColor(color0);
            GL.tessellator.addNormalAgain();
            GL.tessellator.addVertex(numLinesX, 0f, i * stepX);
            GL.tessellator.addColor(color0);
            GL.tessellator.addNormalAgain();
            GL.tessellator.addVertex(-numLinesX, 0f, -i * stepX);
            GL.tessellator.addColor(color0);
            GL.tessellator.addNormalAgain();
            GL.tessellator.addVertex(numLinesX, 0f, -i * stepX);
        }

        for (float i = 1; i <= numLinesZ; i++) {
            GL.tessellator.addColor(color0);
            GL.tessellator.addNormalAgain();
            GL.tessellator.addVertex(i * stepZ, 0f, -numLinesZ);
            GL.tessellator.addColor(color0);
            GL.tessellator.addNormalAgain();
            GL.tessellator.addVertex(i * stepZ, 0f, numLinesZ);
            GL.tessellator.addColor(color0);
            GL.tessellator.addNormalAgain();
            GL.tessellator.addVertex(-i * stepZ, 0f, -numLinesZ);
            GL.tessellator.addColor(color0);
            GL.tessellator.addNormalAgain();
            GL.tessellator.addVertex(-i * stepZ, 0f, numLinesZ);
        }

        // Middle X
        GL.tessellator.addColor(color1);
        GL.tessellator.addNormalAgain();
        GL.tessellator.addVertex(-numLinesX, 0f, 0f);
        GL.tessellator.addColor(color1);
        GL.tessellator.addNormalAgain();
        GL.tessellator.addVertex(numLinesX, 0f, 0f);
        // Middle Z
        GL.tessellator.addColor(color2);
        GL.tessellator.addNormalAgain();
        GL.tessellator.addVertex(0f, 0f, -numLinesZ);
        GL.tessellator.addColor(color2);
        GL.tessellator.addNormalAgain();
        GL.tessellator.addVertex(0f, 0f, numLinesZ);

        GL.tessellator.stop();
        GL.tessellator.addSegment("Primitive Grid");
    }

}
