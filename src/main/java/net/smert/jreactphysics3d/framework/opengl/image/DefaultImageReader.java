package net.smert.jreactphysics3d.framework.opengl.image;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import net.smert.jreactphysics3d.framework.Files;
import net.smert.jreactphysics3d.framework.Fw;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class DefaultImageReader implements ImageReader {

    @Override
    public Image load(String filename) throws IOException {
        Files.FileAsset fileAsset = Fw.files.getTexture(filename);
        InputStream is = fileAsset.openStream();
        return ImageIO.read(is);
    }

}
