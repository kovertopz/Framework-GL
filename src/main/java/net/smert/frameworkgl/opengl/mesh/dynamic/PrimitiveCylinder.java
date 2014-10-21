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
public class PrimitiveCylinder extends AbstractDynamicMesh {

    @Override
    public boolean canReducePrimitiveModes() {
        return false;
    }

    @Override
    public void create(boolean reset, ConstructionInfo constructionInfo, Tessellator tessellator) {
        float halfZ = constructionInfo.size.getZ() * .5f;
        float radiusX = constructionInfo.radius.getX();
        float radiusY = constructionInfo.radius.getY();
        int sides = constructionInfo.quality.getX() * 4;
        final Color color0 = constructionInfo.getColor(0);

        // Reset
        if (reset) {
            tessellator.setConvertToTriangles(constructionInfo.convertToTriangles);
            tessellator.reset();
        }
        tessellator.setLocalPosition(constructionInfo.localPosition);

        // Middle
        tessellator.start(Primitives.TRIANGLE_STRIP);

        float angle = MathHelper.TAU / sides;
        float cosAngle = MathHelper.Cos(angle);
        float sinAngle = MathHelper.Sin(angle);
        float t;
        final Vector3f pos1 = new Vector3f(0f, 0f, 0f);
        final Vector3f pos2 = new Vector3f(0f, 0f, 0f);
        final Vector3f pos3 = new Vector3f(0f, 0f, 0f);
        final Vector3f radius1 = new Vector3f(1f, 0f, 0f);

        for (int i = 0; i <= sides; i++) {
            pos1.set(radius1.getX() * radiusX, radius1.getY() * radiusY, halfZ);
            pos2.set(radius1.getX() * radiusX, radius1.getY() * radiusY, -halfZ);
            t = sinAngle * radius1.getX() + cosAngle * radius1.getY();
            radius1.setX(cosAngle * radius1.getX() - sinAngle * radius1.getY());
            radius1.setY(t);
            pos3.set(radius1.getX() * radiusX, radius1.getY() * radiusY, halfZ);
            tessellator.addColor(color0);
            tessellator.addNormal(pos1, pos2, pos3);
            tessellator.addVertex(pos1);
            tessellator.addColor(color0);
            tessellator.addNormalAgain();
            tessellator.addVertex(pos2);
        }

        tessellator.stop();
        tessellator.addSegment("Primitive Cylinder - Middle");

        // -Z
        tessellator.start(Primitives.TRIANGLE_FAN);

        pos1.zero();
        radius1.set(1f, 0f, -halfZ);
        tessellator.addColor(color0);
        tessellator.addNormal(new Vector3f(0f, 0f, -1f));
        tessellator.addVertex(new Vector3f(0f, 0f, -halfZ));

        for (int i = 0; i <= sides; i++) {
            pos1.set(radius1.getX() * radiusX, radius1.getY() * radiusY, -halfZ);
            tessellator.addColor(color0);
            tessellator.addNormalAgain();
            tessellator.addVertex(pos1);
            t = -sinAngle * radius1.getX() + cosAngle * radius1.getY();
            radius1.setX(cosAngle * radius1.getX() + sinAngle * radius1.getY());
            radius1.setY(t);
        }

        tessellator.stop();
        tessellator.addSegment("Primitive Cylinder - Cap -Z");

        // +Z
        tessellator.start(Primitives.TRIANGLE_FAN);

        pos1.zero();
        radius1.set(1f, 0f, halfZ);
        tessellator.addColor(color0);
        tessellator.addNormal(new Vector3f(0f, 0f, 1f));
        tessellator.addVertex(new Vector3f(0f, 0f, halfZ));

        for (int i = 0; i <= sides; i++) {
            pos1.set(radius1.getX() * radiusX, radius1.getY() * radiusY, halfZ);
            tessellator.addColor(color0);
            tessellator.addNormalAgain();
            tessellator.addVertex(pos1);
            t = sinAngle * radius1.getX() + cosAngle * radius1.getY();
            radius1.setX(cosAngle * radius1.getX() - sinAngle * radius1.getY());
            radius1.setY(t);
        }

        tessellator.stop();
        tessellator.addSegment("Primitive Cylinder - Cap +Z");
    }

}
