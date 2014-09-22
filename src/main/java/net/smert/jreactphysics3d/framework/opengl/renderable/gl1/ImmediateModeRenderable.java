package net.smert.jreactphysics3d.framework.opengl.renderable.gl1;

import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.renderable.AbstractRenderable;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class ImmediateModeRenderable extends AbstractRenderable {

    private DrawCommands drawCommands;

    public ImmediateModeRenderable(Mesh mesh) {
        super(mesh);
    }

    @Override
    public void create() {
        drawCommands = mesh.getDrawCommands();
    }

    @Override
    public void destroy() {
    }

    @Override
    public void render() {
        drawCommands.execCommands();
    }

}
