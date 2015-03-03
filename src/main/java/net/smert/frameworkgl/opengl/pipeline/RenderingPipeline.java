package net.smert.frameworkgl.opengl.pipeline;

import java.io.IOException;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public interface RenderingPipeline {

    public void destroy();

    public void init() throws IOException;

    public void render();

    public void reset();

    public void switchPolygonFillMode();

}
