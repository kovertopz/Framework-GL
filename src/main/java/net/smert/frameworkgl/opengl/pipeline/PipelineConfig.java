package net.smert.frameworkgl.opengl.pipeline;

import net.smert.frameworkgl.opengl.camera.Camera;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public interface PipelineConfig {

    public Camera getCamera();

    public void setCamera(Camera camera);

    public boolean isDebug();

    public void setDebug(boolean debug);

    public boolean isFrustumCulling();

    public void setFrustumCulling(boolean frustumCulling);

    public boolean isShadowsEnabled();

    public void setShadowsEnabled(boolean shadowsEnabled);

    public boolean isWireframe();

    public void setWireframe(boolean wireframe);

}
