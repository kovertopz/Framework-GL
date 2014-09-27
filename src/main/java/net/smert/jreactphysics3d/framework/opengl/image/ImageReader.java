package net.smert.jreactphysics3d.framework.opengl.image;

import java.awt.Image;
import java.io.IOException;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public interface ImageReader {

    public Image load(String filename) throws IOException;

}
