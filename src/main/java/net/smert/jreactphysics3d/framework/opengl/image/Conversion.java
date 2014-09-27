package net.smert.jreactphysics3d.framework.opengl.image;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Conversion {

    public byte[] convertImageARGBToRGBAByteArray(BufferedImage image) {
        int ARGB[] = new int[image.getHeight() * image.getWidth()];
        byte RGBA[] = new byte[image.getHeight() * image.getWidth() * 4];

        image.getRGB(0, 0, image.getWidth(), image.getHeight(), ARGB, 0, image.getWidth());

        for (int i = 0, max = ARGB.length; i < max; i++) {
            int alpha = ARGB[i] >> 24 & 0xff;
            int red = ARGB[i] >> 16 & 0xff;
            int green = ARGB[i] >> 8 & 0xff;
            int blue = ARGB[i] & 0xff;

            RGBA[i * 4 + 0] = (byte) red;
            RGBA[i * 4 + 1] = (byte) green;
            RGBA[i * 4 + 2] = (byte) blue;
            RGBA[i * 4 + 3] = (byte) alpha;
        }

        return RGBA;
    }

    public BufferedImage flipHorizontally(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();

        BufferedImage flippedimage = new BufferedImage(w, h, image.getType());
        Graphics2D g = flippedimage.createGraphics();

        g.drawImage(image, 0, 0, w, h, w, 0, 0, h, null);
        g.dispose();

        return flippedimage;
    }

    public BufferedImage flipVertically(BufferedImage image) {
        int h = image.getHeight();
        int w = image.getWidth();

        BufferedImage flippedimage = new BufferedImage(w, h, image.getType());
        Graphics2D g = flippedimage.createGraphics();

        g.drawImage(image, 0, 0, w, h, 0, h, w, 0, null);
        g.dispose();

        return flippedimage;
    }

}