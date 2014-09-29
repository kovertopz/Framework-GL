package net.smert.jreactphysics3d.framework.opengl.renderable.shared;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.smert.jreactphysics3d.framework.Fw;
import net.smert.jreactphysics3d.framework.opengl.Texture;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class TexturePool {

    private final Map<String, Texture> textures;

    public TexturePool() {
        textures = new HashMap<>();
    }

    public void add(String filename, Texture texture) {

        // Trim slashes
        filename = Fw.files.trimLeftSlashes(filename);

        if (!textures.containsKey(filename)) {
            textures.put(filename, texture);
        } else {
            throw new IllegalArgumentException("Tried to add a Texture that already exists to the pool: " + filename);
        }
    }

    public void destroy() {
        Iterator<Texture> iterator = textures.values().iterator();
        while (iterator.hasNext()) {
            Texture texture = iterator.next();
            texture.destroy();
        }
        textures.clear();
    }

    public Texture get(String filename) {

        // Trim slashes
        filename = Fw.files.trimLeftSlashes(filename);

        Texture texture = textures.get(filename);
        if (texture == null) {
            throw new IllegalArgumentException("Tried to get a Texture that does not exist from the pool: " + filename);
        }
        return texture;
    }

    public void remove(String filename) {

        // Trim slashes
        filename = Fw.files.trimLeftSlashes(filename);

        if (textures.remove(filename) == null) {
            throw new IllegalArgumentException("Did not find an instance of the Texture: " + filename);
        }
    }

}
