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
public class CubeMap extends AbstractDynamicMesh {

    @Override
    public void create(boolean reset, ConstructionInfo constructionInfo, Tessellator tessellator) {
        final Color color0 = constructionInfo.getColor(0);

        // Reset
        if (reset == true) {
            tessellator.setConvertToTriangles(constructionInfo.convertToTriangles);
            tessellator.reset();
        }
        tessellator.getRenderableConfiguration().setTexCoordSize(3);
        tessellator.setLocalPosition(constructionInfo.localPosition);

        tessellator.start(Primitives.QUADS);

        // Face +Z
        tessellator.addColor(color0);
        tessellator.addTexCoord(1f, 1f, 1f);
        tessellator.addVertex(1f, 1f, 1f);
        tessellator.addColor(color0);
        tessellator.addTexCoord(-1f, 1f, 1f);
        tessellator.addVertex(-1f, 1f, 1f);
        tessellator.addColor(color0);
        tessellator.addTexCoord(-1f, -1f, 1f);
        tessellator.addVertex(-1f, -1f, 1f);
        tessellator.addColor(color0);
        tessellator.addTexCoord(1f, -1f, 1f);
        tessellator.addVertex(1f, -1f, 1f);

        // Face +X
        tessellator.addColor(color0);
        tessellator.addTexCoord(1f, 1f, -1f);
        tessellator.addVertex(1f, 1f, -1f);
        tessellator.addColor(color0);
        tessellator.addTexCoord(1f, 1f, 1f);
        tessellator.addVertex(1f, 1f, 1f);
        tessellator.addColor(color0);
        tessellator.addTexCoord(1f, -1f, 1f);
        tessellator.addVertex(1f, -1f, 1f);
        tessellator.addColor(color0);
        tessellator.addTexCoord(1f, -1f, -1f);
        tessellator.addVertex(1f, -1f, -1f);

        // Face -Z
        tessellator.addColor(color0);
        tessellator.addTexCoord(-1f, 1f, -1f);
        tessellator.addVertex(-1f, 1f, -1f);
        tessellator.addColor(color0);
        tessellator.addTexCoord(1f, 1f, -1f);
        tessellator.addVertex(1f, 1f, -1f);
        tessellator.addColor(color0);
        tessellator.addTexCoord(1f, -1f, -1f);
        tessellator.addVertex(1f, -1f, -1f);
        tessellator.addColor(color0);
        tessellator.addTexCoord(-1f, -1f, -1f);
        tessellator.addVertex(-1f, -1f, -1f);

        // Face -X
        tessellator.addColor(color0);
        tessellator.addTexCoord(-1f, 1f, 1f);
        tessellator.addVertex(-1f, 1f, 1f);
        tessellator.addColor(color0);
        tessellator.addTexCoord(-1f, 1f, -1f);
        tessellator.addVertex(-1f, 1f, -1f);
        tessellator.addColor(color0);
        tessellator.addTexCoord(-1f, -1f, -1f);
        tessellator.addVertex(-1f, -1f, -1f);
        tessellator.addColor(color0);
        tessellator.addTexCoord(-1f, -1f, 1f);
        tessellator.addVertex(-1f, -1f, 1f);

        // Face +Y
        tessellator.addColor(color0);
        tessellator.addTexCoord(1f, 1f, -1f);
        tessellator.addVertex(1f, 1f, -1f);
        tessellator.addColor(color0);
        tessellator.addTexCoord(-1f, 1f, -1f);
        tessellator.addVertex(-1f, 1f, -1f);
        tessellator.addColor(color0);
        tessellator.addTexCoord(-1f, 1f, 1f);
        tessellator.addVertex(-1f, 1f, 1f);
        tessellator.addColor(color0);
        tessellator.addTexCoord(1f, 1f, 1f);
        tessellator.addVertex(1f, 1f, 1f);

        // Face -Y
        tessellator.addColor(color0);
        tessellator.addTexCoord(1f, -1f, 1f);
        tessellator.addVertex(1f, -1f, 1f);
        tessellator.addColor(color0);
        tessellator.addTexCoord(-1f, -1f, 1f);
        tessellator.addVertex(-1f, -1f, 1f);
        tessellator.addColor(color0);
        tessellator.addTexCoord(-1f, -1f, -1f);
        tessellator.addVertex(-1f, -1f, -1f);
        tessellator.addColor(color0);
        tessellator.addTexCoord(1f, -1f, -1f);
        tessellator.addVertex(1f, -1f, -1f);

        tessellator.stop();
        tessellator.addSegment("CubeMap");
    }

}
