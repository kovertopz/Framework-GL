package net.smert.frameworkgl.opengl.pipeline;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public interface AdvancedRenderingPipeline extends RenderingPipeline {

    public void addAllGameObjectsToRender();

    public AdvancedPipelineConfig getPipelineConfig();

    public void performFrustumCulling();

    public void updateAabbs();

    public void updateCurrentShader();

    public void updateViewFrustumGameObjectWithCamera();

}
