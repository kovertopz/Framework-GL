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
public class AABB extends AbstractDynamicMesh {

    @Override
    public void create(boolean reset, ConstructionInfo constructionInfo, Tessellator tessellator) {
        final net.smert.frameworkgl.math.AABB aabb
                = (net.smert.frameworkgl.math.AABB) constructionInfo.getCustomData(0);
        final Color color0 = constructionInfo.getColor(0);

        // Reset
        if (reset) {
            tessellator.setConvertToTriangles(constructionInfo.convertToTriangles);
            tessellator.reset();
        }
        tessellator.setLocalPosition(constructionInfo.localPosition);

        tessellator.start(Primitives.LINES);

        final Vector3f max = aabb.getMax();
        final Vector3f min = aabb.getMin();

        // Four middle lines
        tessellator.addColor(color0);
        tessellator.addVertex(max.getX(), max.getY(), min.getZ());
        tessellator.addColor(color0);
        tessellator.addVertex(max.getX(), max.getY(), max.getZ());
        tessellator.addColor(color0);
        tessellator.addVertex(min.getX(), max.getY(), min.getZ());
        tessellator.addColor(color0);
        tessellator.addVertex(min.getX(), max.getY(), max.getZ());
        tessellator.addColor(color0);
        tessellator.addVertex(min.getX(), min.getY(), min.getZ());
        tessellator.addColor(color0);
        tessellator.addVertex(min.getX(), min.getY(), max.getZ());
        tessellator.addColor(color0);
        tessellator.addVertex(max.getX(), min.getY(), min.getZ());
        tessellator.addColor(color0);
        tessellator.addVertex(max.getX(), min.getY(), max.getZ());

        // Near lines that form a box (+Z)
        tessellator.addColor(color0);
        tessellator.addVertex(max.getX(), max.getY(), min.getZ());
        tessellator.addColor(color0);
        tessellator.addVertex(min.getX(), max.getY(), min.getZ());
        tessellator.addColor(color0);
        tessellator.addVertex(min.getX(), max.getY(), min.getZ());
        tessellator.addColor(color0);
        tessellator.addVertex(min.getX(), min.getY(), min.getZ());
        tessellator.addColor(color0);
        tessellator.addVertex(min.getX(), min.getY(), min.getZ());
        tessellator.addColor(color0);
        tessellator.addVertex(max.getX(), min.getY(), min.getZ());
        tessellator.addColor(color0);
        tessellator.addVertex(max.getX(), min.getY(), min.getZ());
        tessellator.addColor(color0);
        tessellator.addVertex(max.getX(), max.getY(), min.getZ());

        // Far lines that form a box (-Z)
        tessellator.addColor(color0);
        tessellator.addVertex(max.getX(), max.getY(), max.getZ());
        tessellator.addColor(color0);
        tessellator.addVertex(min.getX(), max.getY(), max.getZ());
        tessellator.addColor(color0);
        tessellator.addVertex(min.getX(), max.getY(), max.getZ());
        tessellator.addColor(color0);
        tessellator.addVertex(min.getX(), min.getY(), max.getZ());
        tessellator.addColor(color0);
        tessellator.addVertex(min.getX(), min.getY(), max.getZ());
        tessellator.addColor(color0);
        tessellator.addVertex(max.getX(), min.getY(), max.getZ());
        tessellator.addColor(color0);
        tessellator.addVertex(max.getX(), min.getY(), max.getZ());
        tessellator.addColor(color0);
        tessellator.addVertex(max.getX(), max.getY(), max.getZ());

        tessellator.stop();
        tessellator.addSegment("AABB");
    }

}
