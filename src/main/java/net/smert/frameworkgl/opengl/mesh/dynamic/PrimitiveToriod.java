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

import net.smert.frameworkgl.math.MathHelper;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.opengl.constants.Primitives;
import net.smert.frameworkgl.opengl.mesh.Tessellator;
import net.smert.frameworkgl.utils.Color;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class PrimitiveToriod extends AbstractDynamicMesh {

    @Override
    public boolean canReducePrimitiveModes() {
        return false;
    }

    @Override
    public void create(boolean reset, ConstructionInfo constructionInfo, Tessellator tessellator) {
        float radiusIn = constructionInfo.radius.getX();
        float radiusOut = constructionInfo.radius.getY();
        int rings = constructionInfo.quality.getX() * 4;
        int sides = constructionInfo.quality.getY() * 4;
        final Color color0 = constructionInfo.getColor(0);

        // Reset
        if (reset == true) {
            tessellator.setConvertToTriangles(constructionInfo.convertToTriangles);
            tessellator.reset();
        }
        tessellator.setLocalPosition(constructionInfo.localPosition);

        float cosTheta = 1f;
        float ringDelta = MathHelper.TAU / rings;
        float sideDelta = MathHelper.TAU / sides;
        float sinTheta = 0f;
        float theta = 0f;
        final Vector3f normal = new Vector3f();

        for (int i = rings - 1; i >= 0; i--) {
            float phi = 0f;
            float thetaNew = theta + ringDelta;
            float cosThetaNew = MathHelper.Cos(thetaNew);
            float sinThetaNew = MathHelper.Sin(thetaNew);

            tessellator.start(Primitives.QUAD_STRIP);

            for (int j = sides; j >= 0; j--) {
                phi += sideDelta;
                float cosPhi = MathHelper.Cos(phi);
                float sinPhi = MathHelper.Sin(phi);
                float dist = radiusOut + radiusIn * cosPhi;
                normal.set(cosThetaNew * cosPhi, -sinThetaNew * cosPhi, sinPhi);
                normal.normalize();
                tessellator.addColor(color0);
                tessellator.addNormal(normal);
                tessellator.addVertex(cosThetaNew * dist, -sinThetaNew * dist, radiusIn * sinPhi);
                normal.set(cosTheta * cosPhi, -sinTheta * cosPhi, sinPhi);
                normal.normalize();
                tessellator.addColor(color0);
                tessellator.addNormal(normal);
                tessellator.addVertex(cosTheta * dist, -sinTheta * dist, radiusIn * sinPhi);
            }

            tessellator.stop();
            tessellator.addSegment("Primitive Toriod - Ring");

            theta = thetaNew;
            cosTheta = cosThetaNew;
            sinTheta = sinThetaNew;
        }
    }

}
