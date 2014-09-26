package net.smert.jreactphysics3d.framework.opengl.model;

import java.io.IOException;
import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public interface ModelReader {

    public void load(String filename, Mesh mesh) throws IOException;

}
