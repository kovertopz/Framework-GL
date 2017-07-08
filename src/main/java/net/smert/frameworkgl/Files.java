/**
 * Copyright 2012 Jason Sorensen (sorensenj@smert.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package net.smert.frameworkgl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Files {

    private final static Logger log = LoggerFactory.getLogger(Files.class);
    private final static String AUDIO_LOCATION = "audio";
    private final static String FONT_LOCATION = "fonts";
    private final static String GUI_LOCATION = "gui";
    private final static String MATERIAL_LOCATION = "materials";
    private final static String MESH_LOCATION = "meshes";
    private final static String SHADER_LOCATION = "shaders";
    private final static String TEXTURE_LOCATION = "textures";
    public final String DEFAULT_ASSETS_LOCATION = "/net/smert/frameworkgl/assets";
    public final String INTERNAL_FILE_SEPARATOR = "/";

    private boolean foundAsset;
    private boolean isInternal;
    private boolean useGlslPrefix;
    private FileType fileType;
    private final Map<String, FileAsset> filenameToFileAsset;

    public Files() {
        useGlslPrefix = true;
        filenameToFileAsset = new HashMap<>();
        try {
            registerAssets(DEFAULT_ASSETS_LOCATION, true);
        } catch (IOException | URISyntaxException ex) {
            log.error("There was an problem loading the default assets location: " + DEFAULT_ASSETS_LOCATION, ex);
            System.exit(-1);
        }
    }

    private void register(String fullPath, String path, String separator) {
        int firstForwardSlash = path.indexOf(separator);
        String directory = path.substring(0, firstForwardSlash);
        String filename = path.substring(firstForwardSlash + 1);

        if (directory.isEmpty()) {
            throw new FilesException("The directory length was not greater than zero: " + path);
        }
        if (filename.isEmpty()) {
            throw new FilesException("The filename length was not greater than zero: " + path);
        }
        if (isInternal && (fileType == FileType.ZIP)) {
            throw new FilesException("The full path cannot be a zip file for internal assets: " + fullPath);
        }

        log.debug("Registering asset: File Type: {} Type: {} Filename: {} Full Path: {}",
                fileType, directory, filename, fullPath);

        // Create new file asset and key
        FileAsset fileAsset = new FileAsset(fileType, directory, filename, fullPath, separator);
        String key = directory + INTERNAL_FILE_SEPARATOR + filename.replace(separator, INTERNAL_FILE_SEPARATOR);

        // Save the file asset
        FileAsset oldFileAsset = filenameToFileAsset.put(key, fileAsset);

        if (oldFileAsset != null) {
            log.warn("Overwrote a entry in the hash table for the Key: {} New File Asset: {} Old File Asset: {}",
                    key, fileAsset, oldFileAsset);
        }

        // We found an asset
        foundAsset = true;
    }

    private void registerExternalAssets(String fullPath) throws IOException {
        File file = new File(fullPath);
        registerFileAssets(file);
    }

    private void registerDirectoryAssets(String fullPath, String path) throws IOException {
        File directory = new File(path);
        File[] files = directory.listFiles();

        // Add a trailing slash to this path
        String fullPathWithTrailingSlash = fullPath;
        if (!fullPath.endsWith(File.separator)) {
            fullPathWithTrailingSlash = fullPath + File.separator;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                registerDirectoryAssets(fullPath, file.getAbsolutePath());
                continue;
            }

            // Strip relativeFullPath from beginning of entry name
            String relativePathToBaseDirectory = file.getAbsolutePath().replace(fullPathWithTrailingSlash, "");

            // If we are to register an asset then it must be inside a directory. We need the directory
            // name to determine what type of asset it belongs to.
            if (!relativePathToBaseDirectory.contains(File.separator)) {
                continue;
            }

            register(fullPath, relativePathToBaseDirectory, File.separator);
        }
    }

    private void registerFileAssets(File file) throws IOException {

        // Set the file type
        fileType = FileType.FILE;

        String absolutePath = file.getAbsolutePath();
        if (file.isDirectory()) {
            registerDirectoryAssets(absolutePath, absolutePath);
            return;
        }
        registerZipFileAssets(file, absolutePath);
    }

    private void registerInternalAssets(String fullPath) throws IOException, URISyntaxException {
        URL url = this.getClass().getResource(fullPath);

        if (url == null) {
            throw new FileNotFoundException("The file or path could not be found: " + fullPath);
        }

        switch (url.getProtocol()) {

            case "file":
                log.info("Found an internal FILE resource: {}", fullPath);

                // Try to convert the URL into a URI
                File file = new File(url.toURI());
                registerFileAssets(file);
                break;

            case "jar":
                log.info("Found an internal JAR resource: {}", fullPath);

                // The url.getPath() method returns something like "file:C:\Program Files\Something\somejar.jar"
                String jarPath = url.getPath().substring(5, url.getPath().indexOf("!"));
                registerJarAssets(fullPath, jarPath);
                break;

            default:
                throw new FilesException("Unknown protocol: " + url.getProtocol());
        }
    }

    private void registerJarAssets(String fullPath, String jarPath) throws IOException {

        // Set the file type
        fileType = FileType.JAR;

        // Strip all leading forward slashes (if any)
        String relativeFullPath = fullPath.replaceFirst("^/+(?!$)", "");

        // Add a trailing slash to this path
        String relativeFullPathWithTrailingSlash = relativeFullPath;
        if (!relativeFullPath.endsWith(INTERNAL_FILE_SEPARATOR)) {
            relativeFullPathWithTrailingSlash = relativeFullPath + INTERNAL_FILE_SEPARATOR;
        }

        // Try to open the JAR file.
        try (JarFile jar = new JarFile(jarPath)) {
            boolean firstEntry = true;
            Enumeration<JarEntry> entries = jar.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName(); // Name should never start with a forward slash

                // If the path or file matches the curent entry
                if (!name.startsWith(relativeFullPath)) {
                    continue;
                }

                if (firstEntry) {
                    // If the first entry is not a directory then we have a problem and won't be able to
                    // register assets
                    if (!entry.isDirectory()) {
                        throw new FilesException(
                                "The full path was found inside a JAR file and must be a directory: "
                                + fullPath);
                    }
                    firstEntry = false;
                    continue;
                }

                // Skip entries that are just directories
                if (entry.isDirectory()) {
                    continue;
                }

                // Strip relativeFullPath from beginning of entry name
                String relativePathToBaseDirectory = name.replace(relativeFullPathWithTrailingSlash, "");

                // If we are to register an asset then it must be inside a directory. We need the directory
                // name to determine what type of asset it belongs to.
                if (!relativePathToBaseDirectory.contains(INTERNAL_FILE_SEPARATOR)) {
                    continue;
                }

                register(fullPath, relativePathToBaseDirectory, INTERNAL_FILE_SEPARATOR);
            }
        }
    }

    private void registerZipFileAssets(File file, String fullPath) throws IOException {

        // Set the file type
        fileType = FileType.ZIP;

        // Try to open the file
        try (FileInputStream fis = new FileInputStream(file);
                ZipInputStream zip = new ZipInputStream(fis)) {

            while (true) {
                ZipEntry entry = zip.getNextEntry();

                // If there are no more entries
                if (entry == null) {
                    break;
                }

                // We don't do anything with directories
                if (entry.isDirectory()) {
                    continue;
                }

                String name = entry.getName(); // Name should never start with a forward slash

                // If we are to register an asset then it must be inside a directory. We need the directory
                // name to determine what type of asset it belongs to.
                if (!name.contains(INTERNAL_FILE_SEPARATOR)) {
                    continue;
                }

                register(fullPath, name, INTERNAL_FILE_SEPARATOR);
            }
        }
    }

    public boolean isUseGlslPrefix() {
        return useGlslPrefix;
    }

    public void setUseGlslPrefix(boolean useGlslPrefix) {
        this.useGlslPrefix = useGlslPrefix;
    }

    public FileAsset get(String resourceType, String filename) {
        String key = resourceType + INTERNAL_FILE_SEPARATOR + filename;
        if (!filenameToFileAsset.containsKey(key)) {
            throw new IllegalArgumentException("Unable to find asset for type: " + resourceType + " and path: " + filename);
        }
        return filenameToFileAsset.get(key);
    }

    public FileAsset getAudio(String filename) {
        return get(AUDIO_LOCATION, filename);
    }

    public FileAsset getFont(String filename) {
        return get(FONT_LOCATION, filename);
    }

    public FileAsset getGui(String filename) {
        return get(GUI_LOCATION, filename);
    }

    public FileAsset getMaterial(String filename) {
        return get(MATERIAL_LOCATION, filename);
    }

    public FileAsset getMesh(String filename) {
        return get(MESH_LOCATION, filename);
    }

    public FileAsset getShader(String filename) {
        if (useGlslPrefix) {
            filename = "v" + Fw.config.glslVersion + INTERNAL_FILE_SEPARATOR + filename;
        }
        return get(SHADER_LOCATION, filename);
    }

    public FileAsset getTexture(String filename) {
        return get(TEXTURE_LOCATION, filename);
    }

    public final void registerAssets(String fullPath, boolean useInternalPath) throws IOException, URISyntaxException {

        // Remove trailing slash
        while (fullPath.endsWith(INTERNAL_FILE_SEPARATOR) && (fullPath.length() > 0)) {
            fullPath = fullPath.substring(0, fullPath.length() - 1);
        }

        foundAsset = false;
        isInternal = useInternalPath;

        if (isInternal) {
            registerInternalAssets(fullPath);
        } else {
            registerExternalAssets(fullPath);
        }

        if (!foundAsset) {
            throw new FileNotFoundException("Unable to find asset for the given file or path: " + fullPath);
        }
    }

    public String trimLeftSlashes(String stringToTrim) {
        // I don't like this function here but since it is a one off thing I'll leave it for now.
        while (stringToTrim.startsWith(INTERNAL_FILE_SEPARATOR)) {
            stringToTrim = stringToTrim.substring(1);
        }
        return stringToTrim;
    }

    public void unregisterAssets(String fullPath) {
        Iterator<FileAsset> fileAssetIterator = filenameToFileAsset.values().iterator();

        while (fileAssetIterator.hasNext()) {
            FileAsset fileAsset = fileAssetIterator.next();
            String registeredFullPath = fileAsset.getRegisteredFullPath();
            if (!registeredFullPath.equals(fullPath)) {
                continue;
            }
            log.debug("Unregistering asset: {}", fileAsset);
            fileAssetIterator.remove();
        }
    }

    private static enum FileType {

        FILE,
        JAR,
        ZIP

    }

    public static class FileAsset {

        private final FileType fileType;
        private final String fullPathToFile;
        private final String registeredFullPath;
        private final String relativePath;

        private FileAsset(FileType fileType,
                String directory, String filename, String fullPath, String separator) {
            this.fileType = fileType;
            this.registeredFullPath = fullPath;

            switch (fileType) {
                case FILE: // Fall through
                case JAR:
                    this.fullPathToFile = fullPath + separator + directory + separator + filename;
                    this.relativePath = null;
                    break;
                case ZIP:
                    this.fullPathToFile = fullPath;
                    this.relativePath = directory + separator + filename;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown file type: " + fileType);
            }
        }

        private void advanceStreamToEntryInZipFile(ZipInputStream zip, String path) throws IOException {

            boolean entryFound = false;

            // Advance the input stream to the entry of the asset
            while (true) {
                ZipEntry entry = zip.getNextEntry();

                // If there are no more entries
                if (entry == null) {
                    break;
                }

                // We don't do anything with directories
                if (entry.isDirectory()) {
                    continue;
                }

                String name = entry.getName(); // Name should never start with a forward slash

                if (name.equals(path)) {
                    entryFound = true;
                    break;
                }
            }

            if (!entryFound) {
                throw new FileNotFoundException("The path requested: " + path + " in the zip file: "
                        + this.fullPathToFile + " does not exist");
            }
        }

        public String getFullPathToFile() {
            return fullPathToFile;
        }

        public String getRegisteredFullPath() {
            return registeredFullPath;
        }

        public String getRelativePath() {
            return relativePath;
        }

        public InputStream openStream() throws IOException {
            switch (fileType) {
                case FILE:
                    File file = new File(fullPathToFile);
                    return new FileInputStream(file);

                case JAR:
                    return this.getClass().getResourceAsStream(fullPathToFile);

                case ZIP:
                    File zipFile = new File(fullPathToFile);
                    FileInputStream fis = new FileInputStream(zipFile);
                    ZipInputStream zip = new ZipInputStream(fis);
                    advanceStreamToEntryInZipFile(zip, relativePath);
                    return zip;

                default:
                    throw new IllegalArgumentException("Unknown file type: " + fileType);
            }
        }

        public URL toURL() throws MalformedURLException {
            switch (fileType) {
                case FILE:
                    File file = new File(fullPathToFile);
                    return file.toURI().toURL();

                case JAR:
                    return this.getClass().getResource(fullPathToFile);

                case ZIP:
                    throw new UnsupportedOperationException("Not supported");

                default:
                    throw new IllegalArgumentException("Unknown file type: " + fileType);
            }
        }

        @Override
        public String toString() {
            return "(fileType: " + fileType + " fullPathToFile: " + fullPathToFile
                    + " registeredFullPath: " + registeredFullPath + " relativePath: " + relativePath + ")";
        }

    }

}
