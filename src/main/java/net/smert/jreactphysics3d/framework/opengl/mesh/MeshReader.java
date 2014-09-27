package net.smert.jreactphysics3d.framework.opengl.mesh;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.smert.jreactphysics3d.framework.Fw;
import net.smert.jreactphysics3d.framework.opengl.model.ModelReader;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class MeshReader {

    private final Map<String, ModelReader> modelReaders;

    public MeshReader() {
        modelReaders = new HashMap<>();
    }

    public void load(String filename, Mesh mesh) throws IOException {

        // Trim slashes
        filename = Fw.files.trimLeftSlashes(filename);

        // Get the extension from the filename
        int posOfLastPeriod = filename.lastIndexOf(".");
        if (posOfLastPeriod == -1) {
            throw new IllegalArgumentException("The filename must have an extension: " + filename);
        }
        String extension = filename.substring(posOfLastPeriod + 1);

        // Does the model reader for this extension exist?
        if (modelReaders.containsKey(extension) == false) {
            throw new IllegalArgumentException("The extension has not been registered: " + extension);
        }

        // Load the filename using the model reader
        ModelReader modelReader = modelReaders.get(extension);
        modelReader.load(filename, mesh);
    }

    public void registerExtension(String extension, ModelReader modelReader) {
        if (modelReaders.containsKey(extension)) {
            throw new IllegalArgumentException("The extension has already been registered: " + extension);
        }
        modelReaders.put(extension, modelReader);
    }

    public void unregisterExtension(String extension) {
        if (modelReaders.containsKey(extension) == false) {
            throw new IllegalArgumentException("The extension has not been registered: " + extension);
        }
        modelReaders.remove(extension);
    }

}
