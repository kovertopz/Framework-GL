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
public class ViewFrustum extends AbstractDynamicMesh {

    @Override
    public void create(boolean reset, ConstructionInfo constructionInfo, Tessellator tessellator) {
        float aspectRatio = (float) constructionInfo.getCustomData(0);
        float fieldOfView = (float) constructionInfo.getCustomData(1);
        float zNear = (float) constructionInfo.getCustomData(2);
        float zFar = (float) constructionInfo.getCustomData(3);
        final Color color0 = constructionInfo.getColor(0);
        final Color color1 = constructionInfo.getColor(1);
        final Color color2 = constructionInfo.getColor(2);
        final Color color3 = constructionInfo.getColor(3);

        // Reset
        if (reset == true) {
            tessellator.setConvertToTriangles(constructionInfo.convertToTriangles);
            tessellator.reset();
        }
        tessellator.setLocalPosition(constructionInfo.localPosition);

        float tangent = MathHelper.Tan(fieldOfView / 2f * MathHelper.DEG_TO_RAD);
        float nearHalfHeight = zNear * tangent;
        float nearHalfWidth = nearHalfHeight * aspectRatio;
        float farHalfHeight = zFar * tangent;
        float farHalfWidth = farHalfHeight * aspectRatio;

        // Create vertices for each point of the frustum
        final Vector3f vertexFarBottomLeft = new Vector3f(-farHalfWidth, -farHalfHeight, -zFar);
        final Vector3f vertexFarBottomRight = new Vector3f(farHalfWidth, -farHalfHeight, -zFar);
        final Vector3f vertexFarTopLeft = new Vector3f(-farHalfWidth, farHalfHeight, -zFar);
        final Vector3f vertexFarTopRight = new Vector3f(farHalfWidth, farHalfHeight, -zFar);
        final Vector3f vertexNearBottomLeft = new Vector3f(-nearHalfWidth, -nearHalfHeight, -zNear);
        final Vector3f vertexNearBottomRight = new Vector3f(nearHalfWidth, -nearHalfHeight, -zNear);
        final Vector3f vertexNearTopLeft = new Vector3f(-nearHalfWidth, nearHalfHeight, -zNear);
        final Vector3f vertexNearTopRight = new Vector3f(nearHalfWidth, nearHalfHeight, -zNear);

        // Lines
        tessellator.start(Primitives.LINES);

        // Aspect ratio is 1 when we have an ortho camera
        if (aspectRatio != 1f) {
            tessellator.addColor(color0);
            tessellator.addNormal(vertexNearTopLeft, vertexNearTopRight, vertexFarTopRight);
            tessellator.addVertex(0.0f, 0.0f, 0.0f);
            tessellator.addColor(color0);
            tessellator.addNormalAgain();
            tessellator.addVertex(vertexFarTopRight);
            tessellator.addColor(color0);
            tessellator.addNormal(vertexFarTopLeft, vertexNearTopLeft, vertexNearTopRight);
            tessellator.addVertex(0.0f, 0.0f, 0.0f);
            tessellator.addColor(color0);
            tessellator.addNormalAgain();
            tessellator.addVertex(vertexFarTopLeft);
            tessellator.addColor(color0);
            tessellator.addNormal(vertexFarBottomLeft, vertexNearBottomLeft, vertexNearBottomRight);
            tessellator.addVertex(0.0f, 0.0f, 0.0f);
            tessellator.addColor(color0);
            tessellator.addNormalAgain();
            tessellator.addVertex(vertexFarBottomLeft);
            tessellator.addColor(color0);
            tessellator.addNormal(vertexNearBottomLeft, vertexNearBottomRight, vertexFarBottomRight);
            tessellator.addVertex(0.0f, 0.0f, 0.0f);
            tessellator.addColor(color0);
            tessellator.addNormalAgain();
            tessellator.addVertex(vertexFarBottomRight);
        } else {
            tessellator.addColor(color0);
            tessellator.addNormal(0f, 1f, 0f);
            tessellator.addVertex(vertexNearTopRight);
            tessellator.addColor(color0);
            tessellator.addNormalAgain();
            tessellator.addVertex(vertexFarTopRight);
            tessellator.addColor(color0);
            tessellator.addNormalAgain();
            tessellator.addVertex(vertexNearTopLeft);
            tessellator.addColor(color0);
            tessellator.addNormalAgain();
            tessellator.addVertex(vertexFarTopLeft);
            tessellator.addColor(color0);
            tessellator.addNormalAgain();
            tessellator.addVertex(vertexNearBottomLeft);
            tessellator.addColor(color0);
            tessellator.addNormalAgain();
            tessellator.addVertex(vertexFarBottomLeft);
            tessellator.addColor(color0);
            tessellator.addNormalAgain();
            tessellator.addVertex(vertexNearBottomRight);
            tessellator.addColor(color0);
            tessellator.addNormalAgain();
            tessellator.addVertex(vertexFarBottomRight);
        }

        // Near plane
        tessellator.addColor(color1);
        tessellator.addNormal(0f, 0f, 1f);
        tessellator.addVertex(vertexNearTopRight);
        tessellator.addColor(color1);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearTopLeft);
        tessellator.addColor(color1);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearTopLeft);
        tessellator.addColor(color1);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearBottomLeft);
        tessellator.addColor(color1);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearBottomLeft);
        tessellator.addColor(color1);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearBottomRight);
        tessellator.addColor(color1);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearBottomRight);
        tessellator.addColor(color1);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearTopRight);

        // Far plane
        tessellator.addColor(color2);
        tessellator.addNormal(0f, 0f, 1f);
        tessellator.addVertex(vertexNearTopRight);
        tessellator.addColor(color2);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearTopLeft);
        tessellator.addColor(color2);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearTopLeft);
        tessellator.addColor(color2);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearBottomLeft);
        tessellator.addColor(color2);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearBottomLeft);
        tessellator.addColor(color2);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearBottomRight);
        tessellator.addColor(color2);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearBottomRight);
        tessellator.addColor(color2);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearTopRight);

        tessellator.stop();
        tessellator.addSegment("View Frustum - Lines");

        tessellator.start(Primitives.QUADS);

        // Near - Outside
        tessellator.addColor(color3);
        tessellator.addNormal(vertexNearBottomLeft, vertexNearBottomRight, vertexNearTopRight);
        tessellator.addVertex(vertexNearTopRight);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearTopLeft);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearBottomLeft);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearBottomRight);

        // Far - Outside
        tessellator.addColor(color3);
        tessellator.addNormal(vertexFarBottomRight, vertexFarBottomLeft, vertexFarTopLeft);
        tessellator.addVertex(vertexFarTopLeft);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexFarTopRight);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexFarBottomRight);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexFarBottomLeft);

        // Left - Outside
        tessellator.addColor(color3);
        tessellator.addNormal(vertexFarBottomLeft, vertexNearBottomLeft, vertexNearTopLeft);
        tessellator.addVertex(vertexNearTopLeft);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexFarTopLeft);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexFarBottomLeft);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearBottomLeft);

        // Right - Outside
        tessellator.addColor(color3);
        tessellator.addNormal(vertexNearBottomRight, vertexFarBottomRight, vertexFarTopRight);
        tessellator.addVertex(vertexFarTopRight);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearTopRight);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearBottomRight);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexFarBottomRight);

        // Bottom - Outside
        tessellator.addColor(color3);
        tessellator.addNormal(vertexFarBottomLeft, vertexFarBottomRight, vertexNearBottomRight);
        tessellator.addVertex(vertexNearBottomRight);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearBottomLeft);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexFarBottomLeft);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexFarBottomRight);

        // Top - Outside
        tessellator.addColor(color3);
        tessellator.addNormal(vertexNearTopLeft, vertexNearTopRight, vertexFarTopRight);
        tessellator.addVertex(vertexFarTopRight);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexFarTopLeft);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearTopLeft);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearTopRight);

        // Near - Inside
        tessellator.addColor(color3);
        tessellator.addNormal(vertexNearBottomRight, vertexNearBottomLeft, vertexNearTopLeft);
        tessellator.addVertex(vertexNearTopLeft);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearTopRight);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearBottomRight);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearBottomLeft);

        // Far - Inside
        tessellator.addColor(color3);
        tessellator.addNormal(vertexFarBottomLeft, vertexFarBottomRight, vertexFarTopRight);
        tessellator.addVertex(vertexFarTopRight);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexFarTopLeft);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexFarBottomLeft);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexFarBottomRight);

        // Left - Inside
        tessellator.addColor(color3);
        tessellator.addNormal(vertexNearBottomLeft, vertexFarBottomLeft, vertexFarTopLeft);
        tessellator.addVertex(vertexFarTopLeft);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearTopLeft);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearBottomLeft);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexFarBottomLeft);

        // Right - Inside
        tessellator.addColor(color3);
        tessellator.addNormal(vertexFarBottomRight, vertexNearBottomRight, vertexNearTopRight);
        tessellator.addVertex(vertexNearTopRight);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexFarTopRight);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexFarBottomRight);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearBottomRight);

        // Bottom - Inside
        tessellator.addColor(color3);
        tessellator.addNormal(vertexNearBottomLeft, vertexNearBottomRight, vertexFarBottomRight);
        tessellator.addVertex(vertexFarBottomRight);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexFarBottomLeft);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearBottomLeft);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearBottomRight);

        // Top - Inside
        tessellator.addColor(color3);
        tessellator.addNormal(vertexFarTopLeft, vertexFarTopRight, vertexNearTopRight);
        tessellator.addVertex(vertexNearTopRight);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexNearTopLeft);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexFarTopLeft);
        tessellator.addColor(color3);
        tessellator.addNormalAgain();
        tessellator.addVertex(vertexFarTopRight);

        tessellator.stop();
        tessellator.addSegment("View Frustum - Planes");
    }

}
