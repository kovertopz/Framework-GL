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
package net.smert.frameworkgl.opengl.pipeline;

import java.util.ArrayList;
import java.util.List;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.gameobjects.AABBGameObject;
import net.smert.frameworkgl.gameobjects.GameObject;
import net.smert.frameworkgl.gameobjects.SimpleOrientationAxisGameObject;
import net.smert.frameworkgl.math.AABB;
import net.smert.frameworkgl.opengl.camera.Camera;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class DefaultPipelineRenderCallback implements PipelineRenderCallback {

    private final List<PipelineRenderDebugCallback> pipelineRenderDebugCallbacks;
    private final List<GameObject> entityGameObjects;
    private final List<GameObject> entityGameObjectsToRender;
    private final List<GameObject> nonOpaqueGameObjects;
    private final List<GameObject> nonOpaqueGameObjectsToRender;
    private final List<GameObject> worldGameObjects;
    private final List<GameObject> worldGameObjectsToRender;

    public DefaultPipelineRenderCallback() {
        pipelineRenderDebugCallbacks = new ArrayList<>();
        entityGameObjects = new ArrayList<>();
        entityGameObjectsToRender = new ArrayList<>();
        nonOpaqueGameObjects = new ArrayList<>();
        nonOpaqueGameObjectsToRender = new ArrayList<>();
        worldGameObjects = new ArrayList<>();
        worldGameObjectsToRender = new ArrayList<>();
    }

    private void renderAabbs(List<GameObject> gameObjects, AABBGameObject aabbGameObject) {
        for (GameObject gameObject : gameObjects) {
            AABB worldAabb = gameObject.getWorldAabb();
            // Updating AABBs this way is costly
            aabbGameObject.update(worldAabb);
            // AABB is already in world coordinates so we don't translate
            Fw.graphics.render(aabbGameObject.getRenderable(), 0f, 0f, 0f);
        }
    }

    private void renderSimpleOrientationAxis(List<GameObject> gameObjects, SimpleOrientationAxisGameObject simpleOrientationAxisGameObject) {
        for (GameObject gameObject : gameObjects) {
            simpleOrientationAxisGameObject.setWorldTransform(gameObject.getWorldTransform());
            Fw.graphics.render(simpleOrientationAxisGameObject);
        }
    }

    private void updateGameObjectsToRender(List<GameObject> gameObjectsToRender, List<GameObject> gameObjects) {
        gameObjectsToRender.clear();
        for (GameObject gameObject : gameObjects) {
            if (gameObject.getRenderableState().isInFrustum()) {
                gameObjectsToRender.add(gameObject);
            }
        }
    }

    public void addEntityGameObject(GameObject entityGameObject) {
        this.entityGameObjects.add(entityGameObject);
    }

    public void addEntityGameObjects(List<GameObject> entityGameObjects) {
        for (GameObject gameObject : entityGameObjects) {
            this.entityGameObjects.add(gameObject);
        }
    }

    public void addNonOpaqueGameObject(GameObject nonOpaqueGameObject) {
        this.nonOpaqueGameObjects.add(nonOpaqueGameObject);
    }

    public void addNonOpaqueGameObjects(List<GameObject> nonOpaqueGameObjects) {
        for (GameObject gameObject : nonOpaqueGameObjects) {
            this.nonOpaqueGameObjects.add(gameObject);
        }
    }

    public void addWorldGameObject(GameObject worldGameObject) {
        this.worldGameObjects.add(worldGameObject);
    }

    public void addWorldGameObjects(List<GameObject> worldGameObjects) {
        for (GameObject gameObject : worldGameObjects) {
            this.worldGameObjects.add(gameObject);
        }
    }

    public List<GameObject> getEntityGameObjects() {
        return entityGameObjects;
    }

    public List<GameObject> getNonOpaqueGameObjects() {
        return nonOpaqueGameObjects;
    }

    public List<GameObject> getWorldGameObjects() {
        return worldGameObjects;
    }

    public void removeEntityGameObject(GameObject entityGameObject) {
        this.entityGameObjects.remove(entityGameObject);
    }

    public void removeEntityGameObjects(List<GameObject> entityGameObjects) {
        for (GameObject gameObject : entityGameObjects) {
            this.entityGameObjects.remove(gameObject);
        }
    }

    public void removeNonOpaqueGameObject(GameObject nonOpaqueGameObject) {
        this.nonOpaqueGameObjects.remove(nonOpaqueGameObject);
    }

    public void removeNonOpaqueGameObjects(List<GameObject> nonOpaqueGameObjects) {
        for (GameObject gameObject : nonOpaqueGameObjects) {
            this.nonOpaqueGameObjects.remove(gameObject);
        }
    }

    public void removeWorldGameObject(GameObject worldGameObject) {
        this.worldGameObjects.remove(worldGameObject);
    }

    public void removeWorldGameObjects(List<GameObject> worldGameObjects) {
        for (GameObject gameObject : worldGameObjects) {
            this.worldGameObjects.remove(gameObject);
        }
    }

    public void setAllEntityGameObjects(List<GameObject> entityGameObjects) {
        this.entityGameObjects.clear();
        this.entityGameObjects.addAll(entityGameObjects);
    }

    public void setAllNonOpaqueGameObjects(List<GameObject> nonOpaqueGameObjects) {
        this.nonOpaqueGameObjects.clear();
        this.nonOpaqueGameObjects.addAll(nonOpaqueGameObjects);
    }

    public void setAllWorldGameObjects(List<GameObject> worldGameObjects) {
        this.worldGameObjects.clear();
        this.worldGameObjects.addAll(worldGameObjects);
    }

    @Override
    public void addAllGameObjectsToRender() {
        worldGameObjectsToRender.clear();
        entityGameObjectsToRender.clear();
        nonOpaqueGameObjectsToRender.clear();
        worldGameObjectsToRender.addAll(worldGameObjects);
        entityGameObjectsToRender.addAll(entityGameObjects);
        nonOpaqueGameObjectsToRender.addAll(nonOpaqueGameObjects);
    }

    @Override
    public void addPipelineRenderDebugCallback(PipelineRenderDebugCallback pipelineRenderDebugCallback) {
        pipelineRenderDebugCallbacks.add(pipelineRenderDebugCallback);
    }

    @Override
    public void performFrustumCulling(Camera camera) {
        Fw.graphics.performCulling(camera, worldGameObjects);
        Fw.graphics.performCulling(camera, entityGameObjects);
        Fw.graphics.performCulling(camera, nonOpaqueGameObjects);
        updateGameObjectsToRender(worldGameObjectsToRender, worldGameObjects);
        updateGameObjectsToRender(entityGameObjectsToRender, entityGameObjects);
        updateGameObjectsToRender(nonOpaqueGameObjectsToRender, nonOpaqueGameObjects);
    }

    @Override
    public void removePipelineRenderDebugCallback(PipelineRenderDebugCallback pipelineRenderDebugCallback) {
        pipelineRenderDebugCallbacks.remove(pipelineRenderDebugCallback);
    }

    @Override
    public void render() {
        Fw.graphics.render(worldGameObjectsToRender);
        Fw.graphics.render(entityGameObjectsToRender);
        Fw.graphics.renderBlend(nonOpaqueGameObjectsToRender);
    }

    @Override
    public void renderAabbs(AABBGameObject aabbGameObject) {
        renderAabbs(worldGameObjectsToRender, aabbGameObject);
        renderAabbs(entityGameObjectsToRender, aabbGameObject);
        renderAabbs(nonOpaqueGameObjectsToRender, aabbGameObject);
    }

    @Override
    public void renderDebug() {
        for (PipelineRenderDebugCallback callback : pipelineRenderDebugCallbacks) {
            callback.render();
        }
    }

    @Override
    public void renderSimpleOrientationAxis(SimpleOrientationAxisGameObject simpleOrientationAxisGameObject) {
        renderSimpleOrientationAxis(worldGameObjectsToRender, simpleOrientationAxisGameObject);
        renderSimpleOrientationAxis(entityGameObjectsToRender, simpleOrientationAxisGameObject);
        renderSimpleOrientationAxis(nonOpaqueGameObjectsToRender, simpleOrientationAxisGameObject);
    }

    @Override
    public void updateAabbs() {
        Fw.graphics.updateAabb(worldGameObjects);
        Fw.graphics.updateAabb(entityGameObjects);
        Fw.graphics.updateAabb(nonOpaqueGameObjects);
    }

}
