package net.smert.frameworkgl.opengl.pipeline;

import net.smert.frameworkgl.gameobjects.AABBGameObject;
import net.smert.frameworkgl.gameobjects.SimpleOrientationAxisGameObject;
import net.smert.frameworkgl.opengl.camera.Camera;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public interface PipelineRenderCallback {

    public void addAllGameObjectsToRender();

    public void addPipelineRenderDebugCallback(PipelineRenderDebugCallback pipelineRenderDebugCallback);

    public void performFrustumCulling(Camera camera);

    public void removePipelineRenderDebugCallback(PipelineRenderDebugCallback pipelineRenderDebugCallback);

    public void render();

    public void renderAabbs(AABBGameObject aabbGameObject);

    public void renderDebug();

    public void renderSimpleOrientationAxis(SimpleOrientationAxisGameObject simpleOrientationAxisGameObject);

    public void updateAabbs();

}
