package net.smert.frameworkgl.opengl.pipeline;

import net.smert.frameworkgl.gameobjects.AABBGameObject;
import net.smert.frameworkgl.gameobjects.SimpleOrientationAxisGameObject;
import net.smert.frameworkgl.gameobjects.SkyboxGameObject;
import net.smert.frameworkgl.gameobjects.ViewFrustumGameObject;
import net.smert.frameworkgl.opengl.renderer.GuiRenderer;
import net.smert.frameworkgl.opengl.shader.AbstractShader;
import net.smert.frameworkgl.utils.Color;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public interface AdvancedPipelineConfig extends PipelineConfig {

    public AABBGameObject getAabbGameObject();

    public void setAabbGameObject(AABBGameObject aabbGameObject);

    public AbstractShader getDefaultShader();

    public void setDefaultShader(AbstractShader defaultShader);

    public AbstractShader getDefaultShaderWithShadows();

    public void setDefaultShaderWithShadows(AbstractShader defaultShaderWithShadows);

    public Color getSkyboxColor();

    public GuiRenderer getGuiRenderer();

    public void setGuiRenderer(GuiRenderer guiRenderer);

    public PipelineRenderCallback getPipelineRenderCallback();

    public void setPipelineRenderCallback(PipelineRenderCallback pipelineRenderCallback);

    public SimpleOrientationAxisGameObject getSimpleOrientationAxisGameObject();

    public void setSimpleOrientationAxisGameObject(SimpleOrientationAxisGameObject simpleOrientationAxisGameObject);

    public SkyboxGameObject getSkyboxGameObject();

    public void setSkyboxGameObject(SkyboxGameObject skyboxGameObject);

    public ViewFrustumGameObject getViewFrustumGameObject();

    public void setViewFrustumGameObject(ViewFrustumGameObject viewFrustumGameObject);

    public boolean isRenderAabbs();

    public void setRenderAabbs(boolean renderAabbs);

    public boolean isRenderDebug();

    public void setRenderDebug(boolean renderDebug);

    public boolean isRenderSimpleOrientationAxis();

    public void setRenderSimpleOrientationAxis(boolean renderSimpleOrientationAxis);

    public boolean isRenderViewFrustum();

    public void setRenderViewFrustum(boolean renderViewFrustum);

    public boolean isUpdateAabbs();

    public void setUpdateAabbs(boolean updateAabbs);

}
