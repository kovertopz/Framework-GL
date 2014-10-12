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
package net.smert.frameworkgl;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.smert.frameworkgl.gameobjects.GameObject;
import net.smert.frameworkgl.math.AABB;
import net.smert.frameworkgl.math.AABBUtilities;
import net.smert.frameworkgl.math.Transform4f;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.Texture;
import net.smert.frameworkgl.opengl.camera.Camera;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.mesh.Segment;
import net.smert.frameworkgl.opengl.mesh.Tessellator;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderable.RenderableConfiguration;
import net.smert.frameworkgl.opengl.renderable.gl1.DrawCommands;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Graphics {

    private static RenderableComparison renderableComparison = new RenderableComparison();

    public Mesh createMesh(DrawCommands drawCommands) {

        // Create new segment with the draw commands
        Segment segment = GL.meshFactory.createSegment();
        segment.setDrawCommands(drawCommands);

        // Check to see if a renderable configuration exists before adding it
        RenderableConfiguration config = GL.meshFactory.createRenderableConfiguration();
        int renderableConfigID = Renderable.configPool.getOrAdd(config);

        // Create mesh and set config ID and add segment
        Mesh mesh = GL.meshFactory.createMesh();
        mesh.setRenderableConfigID(renderableConfigID);
        mesh.addSegment(segment);
        return mesh;
    }

    public Mesh createMesh(Tessellator tessellator) {
        Mesh mesh = GL.meshFactory.createMesh();
        return createMesh(tessellator, mesh);
    }

    public Mesh createMesh(Tessellator tessellator, Mesh mesh) {

        // Reset the mesh
        mesh.reset();

        // Save AABB
        tessellator.getAABB(mesh.getAabb());

        // Check to see if a renderable configuration exists before adding it
        RenderableConfiguration config = tessellator.getRenderableConfiguration();
        int renderableConfigID = Renderable.configPool.getOrAdd(config);

        // Set config ID
        mesh.setRenderableConfigID(renderableConfigID);

        // Add segments
        List<Segment> segments = tessellator.getSegments();
        for (Segment segment : segments) {
            mesh.addSegment(segment);
        }

        return mesh;
    }

    /**
     * This method should be called just before the Display is destroyed. This is automatically called in Application
     * during the normal shutdown process.
     */
    public void destroy() {
        GL.legacyRenderer.destroy();
        Renderable.shaderBindState.reset();
        Renderable.textureBindState.reset();
        GL.fboHelper.unbind();
        GL.textureHelper.unbind();
        GL.vboHelper.unbind();
        Renderable.shaderPool.destroy();
        Renderable.texturePool.destroy();
    }

    public Comparator<GameObject> getRenderableComparison() {
        return renderableComparison;
    }

    public void setRenderableComparison(RenderableComparison renderableComparison) {
        Graphics.renderableComparison = renderableComparison;
    }

    public Texture getTexture(String filename) {
        return Renderable.texturePool.get(filename);
    }

    public void loadMesh(String filename, Mesh mesh) throws IOException {
        GL.meshReader.load(filename, mesh);
    }

    public void loadTexture(String filename) throws IOException {
        Texture texture = GL.textureReader.load(filename);
        Renderable.texturePool.add(filename, texture);
    }

    public void loadTextures(Mesh mesh) throws IOException {
        List<String> textures = mesh.getTextures();
        for (String filename : textures) {
            Texture texture = GL.textureReader.load(filename);
            Renderable.texturePool.add(filename, texture);
        }
    }

    public void performCulling(Camera camera, GameObject gameObject) {
        boolean inFrustum = camera.getFrustumCulling().isAABBInFrustum(gameObject.getWorldAabb());
        gameObject.getRenderableState().setInFrustum(inFrustum);
    }

    public void performCulling(Camera camera, List<GameObject> gameObjects) {
        for (GameObject gameObject : gameObjects) {
            performCulling(camera, gameObject);
        }
    }

    public void sort(List<GameObject> gameObjects, Vector3f cameraPosition) {
        sort(gameObjects, cameraPosition, renderableComparison);
    }

    public void sort(List<GameObject> gameObjects, Vector3f cameraPosition, RenderableComparison renderableComparison) {
        renderableComparison.setCameraPosition(cameraPosition);
        Collections.sort(gameObjects, renderableComparison);
    }

    public void updateAabb(GameObject gameObject) {
        AABB localAabb = gameObject.getMesh().getAabb();
        AABB worldAabb = gameObject.getWorldAabb();
        Transform4f worldTransform = gameObject.getWorldTransform();
        AABBUtilities.Transform(localAabb, worldTransform, worldAabb);
    }

    public void updateAabb(GameObject gameObject, float margin) {
        AABB localAabb = gameObject.getMesh().getAabb();
        AABB worldAabb = gameObject.getWorldAabb();
        Transform4f worldTransform = gameObject.getWorldTransform();
        AABBUtilities.Transform(localAabb, margin, worldTransform, worldAabb);
    }

    public void updateAabb(List<GameObject> gameObjects) {
        for (GameObject gameObject : gameObjects) {
            updateAabb(gameObject);
        }
    }

    public void updateAabb(List<GameObject> gameObjects, float margin) {
        for (GameObject gameObject : gameObjects) {
            updateAabb(gameObject, margin);
        }
    }

    public static class RenderableComparison implements Comparator<GameObject> {

        private Vector3f cameraPosition;

        public void setCameraPosition(Vector3f cameraPosition) {
            this.cameraPosition = cameraPosition;
        }

        @Override
        public int compare(GameObject o1, GameObject o2) {
            boolean o1Opaque = o1.getRenderableState().isOpaque();
            boolean o2Opaque = o2.getRenderableState().isOpaque();
            if ((o1Opaque == o2Opaque) && (o1Opaque)) {
                return 0;
            }
            if ((o1Opaque != o2Opaque) && (!o1Opaque)) {
                return 1;
            }
            if ((o1Opaque != o2Opaque) && (o1Opaque)) {
                return -1;
            }
            // Both objects are not opaque
            Vector3f o1Position = o1.getWorldTransform().getPosition();
            Vector3f o2Position = o2.getWorldTransform().getPosition();
            return (int) cameraPosition.distanceSquared(o2Position)
                    - (int) cameraPosition.distanceSquared(o1Position);
        }

    }

}
