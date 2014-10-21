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

import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.opengl.constants.Primitives;
import net.smert.frameworkgl.opengl.mesh.Tessellator;
import net.smert.frameworkgl.utils.Color;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class PrimitivePyramid extends AbstractDynamicMesh {

    @Override
    public void create(boolean reset, ConstructionInfo constructionInfo, Tessellator tessellator) {
        float halfX = constructionInfo.size.getX() * .5f;
        float halfY = constructionInfo.size.getY() * .5f;
        float halfZ = constructionInfo.size.getZ() * .5f;
        final Color color0 = constructionInfo.getColor(0);

        // Reset
        if (reset) {
            tessellator.setConvertToTriangles(constructionInfo.convertToTriangles);
            tessellator.reset();
        }
        tessellator.setLocalPosition(constructionInfo.localPosition);

        tessellator.start(Primitives.TRIANGLES);

        // Face +Z
        final Vector3f pos1 = new Vector3f(halfX, -halfY, halfZ);
        final Vector3f pos2 = new Vector3f(0f, halfY, 0f);
        final Vector3f pos3 = new Vector3f(-halfX, -halfY, halfZ);
        tessellator.addColor(color0);
        tessellator.addNormal(pos1, pos2, pos3);
        tessellator.addVertex(pos1);
        tessellator.addColor(color0);
        tessellator.addNormalAgain();
        tessellator.addVertex(pos2);
        tessellator.addColor(color0);
        tessellator.addNormalAgain();
        tessellator.addVertex(pos3);

        // Face +X
        pos1.set(halfX, -halfY, -halfZ);
        pos2.set(0f, halfY, 0f);
        pos3.set(halfX, -halfY, halfZ);
        tessellator.addColor(color0);
        tessellator.addNormal(pos1, pos2, pos3);
        tessellator.addVertex(pos1);
        tessellator.addColor(color0);
        tessellator.addNormalAgain();
        tessellator.addVertex(pos2);
        tessellator.addColor(color0);
        tessellator.addNormalAgain();
        tessellator.addVertex(pos3);

        // Face -Z
        pos1.set(-halfX, -halfY, -halfZ);
        pos2.set(0f, halfY, 0f);
        pos3.set(halfX, -halfY, -halfZ);
        tessellator.addColor(color0);
        tessellator.addNormal(pos1, pos2, pos3);
        tessellator.addVertex(pos1);
        tessellator.addColor(color0);
        tessellator.addNormalAgain();
        tessellator.addVertex(pos2);
        tessellator.addColor(color0);
        tessellator.addNormalAgain();
        tessellator.addVertex(pos3);

        // Face -X
        pos1.set(-halfX, -halfY, halfZ);
        pos2.set(0f, halfY, 0f);
        pos3.set(-halfX, -halfY, -halfZ);
        tessellator.addColor(color0);
        tessellator.addNormal(pos1, pos2, pos3);
        tessellator.addVertex(pos1);
        tessellator.addColor(color0);
        tessellator.addNormalAgain();
        tessellator.addVertex(pos2);
        tessellator.addColor(color0);
        tessellator.addNormalAgain();
        tessellator.addVertex(pos3);

        // Face Bottom
        tessellator.addColor(color0);
        tessellator.addNormal(0f, -1f, 0f);
        tessellator.addVertex(halfX, -halfY, halfZ);
        tessellator.addColor(color0);
        tessellator.addNormalAgain();
        tessellator.addVertex(-halfX, -halfY, halfZ);
        tessellator.addColor(color0);
        tessellator.addNormalAgain();
        tessellator.addVertex(-halfX, -halfY, -halfZ);
        tessellator.addColor(color0);
        tessellator.addNormalAgain();
        tessellator.addVertex(-halfX, -halfY, -halfZ);
        tessellator.addColor(color0);
        tessellator.addNormalAgain();
        tessellator.addVertex(halfX, -halfY, -halfZ);
        tessellator.addColor(color0);
        tessellator.addNormalAgain();
        tessellator.addVertex(halfX, -halfY, halfZ);

        tessellator.stop();
        tessellator.addSegment("Primitive Pyramid");
    }

}
