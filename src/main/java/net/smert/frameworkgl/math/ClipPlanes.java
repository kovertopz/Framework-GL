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
package net.smert.frameworkgl.math;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class ClipPlanes {

    // Bottom
    float bpW;
    float bpX;
    float bpY;
    float bpZ;

    // Far
    float fpW;
    float fpX;
    float fpY;
    float fpZ;

    // Left
    float lpW;
    float lpX;
    float lpY;
    float lpZ;

    // Near
    float npW;
    float npX;
    float npY;
    float npZ;

    // Right
    float rpW;
    float rpX;
    float rpY;
    float rpZ;

    // Top
    float tpW;
    float tpX;
    float tpY;
    float tpZ;

    // Thresholds
    float bpThreshold;
    float fpThreshold;
    float lpThreshold;
    float npThreshold;
    float rpThreshold;
    float tpThreshold;

    public ClipPlanes() {
        reset();
    }

    public boolean planeAABBEquation(Vector3f aabbMin, Vector3f aabbMax) {

        // This will fail if the AABB is larger than the frustum
        // Vertex minX minY minZ (0)
        float npXMinX = npX * aabbMin.x;
        float npYMinY = npY * aabbMin.y;
        float npZMinZ = npZ * aabbMin.z;
        float fpXMinX = fpX * aabbMin.x;
        float fpYMinY = fpY * aabbMin.y;
        float fpZMinZ = fpZ * aabbMin.z;
        float lpXMinX = lpX * aabbMin.x;
        float lpYMinY = lpY * aabbMin.y;
        float lpZMinZ = lpZ * aabbMin.z;
        float rpXMinX = rpX * aabbMin.x;
        float rpYMinY = rpY * aabbMin.y;
        float rpZMinZ = rpZ * aabbMin.z;
        float bpXMinX = bpX * aabbMin.x;
        float bpYMinY = bpY * aabbMin.y;
        float bpZMinZ = bpZ * aabbMin.z;
        float tpXMinX = tpX * aabbMin.x;
        float tpYMinY = tpY * aabbMin.y;
        float tpZMinZ = tpZ * aabbMin.z;
        if (((npXMinX + npYMinY + npZMinZ + npW) > npThreshold)
                && ((fpXMinX + fpYMinY + fpZMinZ + fpW) > fpThreshold)
                && ((lpXMinX + lpYMinY + lpZMinZ + lpW) > lpThreshold)
                && ((rpXMinX + rpYMinY + rpZMinZ + rpW) > rpThreshold)
                && ((bpXMinX + bpYMinY + bpZMinZ + bpW) > bpThreshold)
                && ((tpXMinX + tpYMinY + tpZMinZ + tpW) > tpThreshold)) {
            return true;
        }

        // Vertex minX minY maxZ (1)
        float npZMaxZ = npZ * aabbMax.z;
        float fpZMaxZ = fpZ * aabbMax.z;
        float lpZMaxZ = lpZ * aabbMax.z;
        float rpZMaxZ = rpZ * aabbMax.z;
        float bpZMaxZ = bpZ * aabbMax.z;
        float tpZMaxZ = tpZ * aabbMax.z;
        if (((npXMinX + npYMinY + npZMaxZ + npW) > npThreshold)
                && ((fpXMinX + fpYMinY + fpZMaxZ + fpW) > fpThreshold)
                && ((lpXMinX + lpYMinY + lpZMaxZ + lpW) > lpThreshold)
                && ((rpXMinX + rpYMinY + rpZMaxZ + rpW) > rpThreshold)
                && ((bpXMinX + bpYMinY + bpZMaxZ + bpW) > bpThreshold)
                && ((tpXMinX + tpYMinY + tpZMaxZ + tpW) > tpThreshold)) {
            return true;
        }

        // Vertex minX maxY minZ (2)
        float npYMaxY = npY * aabbMax.y;
        float fpYMaxY = fpY * aabbMax.y;
        float lpYMaxY = lpY * aabbMax.y;
        float rpYMaxY = rpY * aabbMax.y;
        float bpYMaxY = bpY * aabbMax.y;
        float tpYMaxY = tpY * aabbMax.y;
        if (((npXMinX + npYMaxY + npZMinZ + npW) > npThreshold)
                && ((fpXMinX + fpYMaxY + fpZMinZ + fpW) > fpThreshold)
                && ((lpXMinX + lpYMaxY + lpZMinZ + lpW) > lpThreshold)
                && ((rpXMinX + rpYMaxY + rpZMinZ + rpW) > rpThreshold)
                && ((bpXMinX + bpYMaxY + bpZMinZ + bpW) > bpThreshold)
                && ((tpXMinX + tpYMaxY + tpZMinZ + tpW) > tpThreshold)) {
            return true;
        }

        // Vertex minX maxY maxZ (3)
        if (((npXMinX + npYMaxY + npZMaxZ + npW) > npThreshold)
                && ((fpXMinX + fpYMaxY + fpZMaxZ + fpW) > fpThreshold)
                && ((lpXMinX + lpYMaxY + lpZMaxZ + lpW) > lpThreshold)
                && ((rpXMinX + rpYMaxY + rpZMaxZ + rpW) > rpThreshold)
                && ((bpXMinX + bpYMaxY + bpZMaxZ + bpW) > bpThreshold)
                && ((tpXMinX + tpYMaxY + tpZMaxZ + tpW) > tpThreshold)) {
            return true;
        }

        // Vertex maxX minY minZ (4)
        float npXMaxX = npX * aabbMax.x;
        float fpXMaxX = fpX * aabbMax.x;
        float lpXMaxX = lpX * aabbMax.x;
        float rpXMaxX = rpX * aabbMax.x;
        float bpXMaxX = bpX * aabbMax.x;
        float tpXMaxX = tpX * aabbMax.x;
        if (((npXMaxX + npYMinY + npZMinZ + npW) > npThreshold)
                && ((fpXMaxX + fpYMinY + fpZMinZ + fpW) > fpThreshold)
                && ((lpXMaxX + lpYMinY + lpZMinZ + lpW) > lpThreshold)
                && ((rpXMaxX + rpYMinY + rpZMinZ + rpW) > rpThreshold)
                && ((bpXMaxX + bpYMinY + bpZMinZ + bpW) > bpThreshold)
                && ((tpXMaxX + tpYMinY + tpZMinZ + tpW) > tpThreshold)) {
            return true;
        }

        // Vertex maxX minY maxZ (5)
        if (((npXMaxX + npYMinY + npZMaxZ + npW) > npThreshold)
                && ((fpXMaxX + fpYMinY + fpZMaxZ + fpW) > fpThreshold)
                && ((lpXMaxX + lpYMinY + lpZMaxZ + lpW) > lpThreshold)
                && ((rpXMaxX + rpYMinY + rpZMaxZ + rpW) > rpThreshold)
                && ((bpXMaxX + bpYMinY + bpZMaxZ + bpW) > bpThreshold)
                && ((tpXMaxX + tpYMinY + tpZMaxZ + tpW) > tpThreshold)) {
            return true;
        }

        // Vertex maxX maxY minZ (6)
        if (((npXMaxX + npYMaxY + npZMinZ + npW) > npThreshold)
                && ((fpXMaxX + fpYMaxY + fpZMinZ + fpW) > fpThreshold)
                && ((lpXMaxX + lpYMaxY + lpZMinZ + lpW) > lpThreshold)
                && ((rpXMaxX + rpYMaxY + rpZMinZ + rpW) > rpThreshold)
                && ((bpXMaxX + bpYMaxY + bpZMinZ + bpW) > bpThreshold)
                && ((tpXMaxX + tpYMaxY + tpZMinZ + tpW) > tpThreshold)) {
            return true;
        }

        // Vertex maxX maxY maxZ
        if (((npXMaxX + npYMaxY + npZMaxZ + npW) > npThreshold)
                && ((fpXMaxX + fpYMaxY + fpZMaxZ + fpW) > fpThreshold)
                && ((lpXMaxX + lpYMaxY + lpZMaxZ + lpW) > lpThreshold)
                && ((rpXMaxX + rpYMaxY + rpZMaxZ + rpW) > rpThreshold)
                && ((bpXMaxX + bpYMaxY + bpZMaxZ + bpW) > bpThreshold)
                && ((tpXMaxX + tpYMaxY + tpZMaxZ + tpW) > tpThreshold)) {
            return true;
        }

        // AABB is not in the frustum or it is bigger than so we do a proximity
        // test. This gives us false positives but does not ruin the user
        // experience by having objects that pop in and out of the frustum.
        Vector3f center = new Vector3f(aabbMin).add(aabbMax).multiply(.5f);
        Vector3f extent = new Vector3f(aabbMax).subtract(aabbMin).multiply(.5f);
        float maxExtent = MathHelper.Sqrt(extent.dot(extent));
        return planePointEquation(center, maxExtent);
    }

    public boolean planePointEquation(float x, float y, float z, float threshold) {
        boolean result;
        result = ((npX * x + npY * y + npZ * z + npW) > (-threshold + npThreshold));
        if (!result) {
            return result;
        }
        result = ((fpX * x + fpY * y + fpZ * z + fpW) > (-threshold + fpThreshold));
        if (!result) {
            return result;
        }
        result = ((lpX * x + lpY * y + lpZ * z + lpW) > (-threshold + lpThreshold));
        if (!result) {
            return result;
        }
        result = ((rpX * x + rpY * y + rpZ * z + rpW) > (-threshold + rpThreshold));
        if (!result) {
            return result;
        }
        result = ((bpX * x + bpY * y + bpZ * z + bpW) > (-threshold + bpThreshold));
        if (!result) {
            return result;
        }
        result = ((tpX * x + tpY * y + tpZ * z + tpW) > (-threshold + tpThreshold));
        return result;
    }

    public boolean planePointEquation(Vector3f v, float threshold) {
        return planePointEquation(v.x, v.y, v.z, threshold);
    }

    public void normalize() {
        float invMag;
        // W component must NOT be a part of the inverse magnitude
        invMag = 1f / MathHelper.Sqrt(npX * npX + npY * npY + npZ * npZ);
        npW *= invMag; // We still normalize it though
        npX *= invMag;
        npY *= invMag;
        npZ *= invMag;
        invMag = 1f / MathHelper.Sqrt(fpX * fpX + fpY * fpY + fpZ * fpZ);
        fpW *= invMag;
        fpX *= invMag;
        fpY *= invMag;
        fpZ *= invMag;
        invMag = 1f / MathHelper.Sqrt(lpX * lpX + lpY * lpY + lpZ * lpZ);
        lpW *= invMag;
        lpX *= invMag;
        lpY *= invMag;
        lpZ *= invMag;
        invMag = 1f / MathHelper.Sqrt(rpX * rpX + rpY * rpY + rpZ * rpZ);
        rpW *= invMag;
        rpX *= invMag;
        rpY *= invMag;
        rpZ *= invMag;
        invMag = 1f / MathHelper.Sqrt(bpX * bpX + bpY * bpY + bpZ * bpZ);
        bpW *= invMag;
        bpX *= invMag;
        bpY *= invMag;
        bpZ *= invMag;
        invMag = 1f / MathHelper.Sqrt(tpX * tpX + tpY * tpY + tpZ * tpZ);
        tpW *= invMag;
        tpX *= invMag;
        tpY *= invMag;
        tpZ *= invMag;
    }

    public final void reset() {
        bpW = 0f;
        bpX = 0f;
        bpY = 0f;
        bpZ = 0f;
        fpW = 0f;
        fpX = 0f;
        fpY = 0f;
        fpZ = 0f;
        lpW = 0f;
        lpX = 0f;
        lpY = 0f;
        lpZ = 0f;
        npW = 0f;
        npX = 0f;
        npY = 0f;
        npZ = 0f;
        rpW = 0f;
        rpX = 0f;
        rpY = 0f;
        rpZ = 0f;
        tpW = 0f;
        tpX = 0f;
        tpY = 0f;
        tpZ = 0f;
        bpThreshold = 0f;
        fpThreshold = 0f;
        lpThreshold = 0f;
        npThreshold = 0f;
        rpThreshold = 0f;
        tpThreshold = 0f;
    }

}
