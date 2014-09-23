package net.smert.jreactphysics3d.framework.opengl.renderable.gl1;

import net.smert.jreactphysics3d.framework.opengl.DisplayList;
import net.smert.jreactphysics3d.framework.opengl.GL;
import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.renderable.AbstractRenderable;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class DisplayListRenderable extends AbstractRenderable {

    private DisplayList displayList;

    public DisplayListRenderable(Mesh mesh) {
        super(mesh);
    }

    @Override
    public void create() {

        // Destroy existing display list
        destroy();

        // Create display list
        displayList = new DisplayList();
        displayList.create();
        int displayListID = displayList.getDisplayListID();

        // Compile display list
        assert (displayListID != 0);
        GL.displayListHelper.begin(displayListID);
        DrawCommands drawCommands = mesh.getDrawCommands();
        drawCommands.execCommands(mesh);
        GL.displayListHelper.end();
    }

    @Override
    public void destroy() {
        if (displayList != null) {
            displayList.destroy();
            displayList = null;
        }
    }

    @Override
    public void render() {
        assert (displayList != null);
        int displayListID = displayList.getDisplayListID();
        assert (displayListID != 0);
        GL.displayListHelper.call(displayListID);
    }

}
