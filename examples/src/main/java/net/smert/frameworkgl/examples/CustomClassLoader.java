/**
 * Copyright 2014 Jason Sorensen (sorensenj@smert.net)
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
package net.smert.frameworkgl.examples;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.security.SecureClassLoader;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * http://docstore.mik.ua/orelly/java-ent/security/ch03_04.htm
 * http://www.javaworld.com/article/2077009/core-java/security-and-the-class-loader-architecture.html
 * http://www.securingjava.com/chapter-three/chapter-three-7.html
 * http://stackoverflow.com/questions/320542/how-to-get-the-path-of-a-running-jar-file
 * http://stackoverflow.com/questions/1771679/difference-between-threads-context-class-loader-and-normal-classloader
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class CustomClassLoader extends SecureClassLoader {

    private final static Logger log = Logger.getLogger(CustomClassLoader.class.getName());
    private final static String TMP_DIR = "JavaCustomClassLoader";

    private int maxDepthForJarFiles;
    private final ByteBuffer classByteCodeBuffer;
    private final Map<String, Class> loadedClasses;
    private final Map<String, String> loadedNatives;
    private final Map<String, FileEntryInJar> fileEntriesInJar;
    private final List<JarFileEntry> jarFileEntries;
    private final ProtectionDomain rootProtectionDomain;

    public CustomClassLoader() {
        this(ClassLoader.getSystemClassLoader());
    }

    public CustomClassLoader(ClassLoader parent) {
        super(parent);

        maxDepthForJarFiles = 3;
        // I thought the max was 64K but I've seen .class files with bigger. Maybe I'm confusing method length?
        // Either case it doesn't matter just let the JVM go up in flames. Hopefully we don't throw a
        // BufferOverflowException while reading a class.
        classByteCodeBuffer = ByteBuffer.allocateDirect(1048576);
        loadedClasses = new HashMap<>();
        loadedNatives = new HashMap<>();
        fileEntriesInJar = new HashMap<>();
        jarFileEntries = new ArrayList<>();
        rootProtectionDomain = getClass().getProtectionDomain();

        URL location = getClassLocation();
        loadViaProtocol(location);
    }

    private File createTempFile(String name) throws IOException {
        // Directory
        String tmpDirName = System.getProperty("java.io.tmpdir");
        File tmpDir = new File(tmpDirName, TMP_DIR);
        if (!tmpDir.exists()) {
            tmpDir.mkdir();
        }
        // Chmod 777
        tmpDir.setReadable(true, false);
        tmpDir.setWritable(true, false);
        tmpDir.setExecutable(true, false);
        if ((!tmpDir.exists()) || (!tmpDir.isDirectory())) {
            throw new RuntimeException("Unable to create a temporary directory: " + tmpDirName);
        }
        // File
        File tmpFile = File.createTempFile(name, null, tmpDir);
        tmpFile.deleteOnExit(); // Only works when there is no lock on the file
        // Chmod 777
        tmpFile.setReadable(true, false);
        tmpFile.setWritable(true, false);
        tmpFile.setExecutable(true, false);
        if ((!tmpFile.exists()) || (tmpFile.isDirectory())) {
            throw new RuntimeException("Unable to create a temporary file: " + tmpFile.getAbsolutePath());
        }
        return tmpFile;
    }

    private void defineNewPackage(String className, FileEntryInJar fileEntryInJar) {
        int lastPeriod = className.lastIndexOf('.');
        if (lastPeriod != -1) {
            JarFileEntry jarFileEntry = fileEntryInJar.getJarFileEntry();
            Manifest manifest = jarFileEntry.getManifest();
            String packageName = className.substring(0, lastPeriod);
            URL sealBase = jarFileEntry.getSealBase(packageName);

            Package pkg = getPackage(packageName);
            if (pkg != null) {
                if (pkg.isSealed()) {
                    // The package is already defined and the new class needs to be verified to match the seal
                    if (!pkg.isSealed(sealBase)) {
                        throw new SecurityException("Sealing violation: package " + packageName
                                + " is sealed but new class " + className + " is not defined in the same JAR");
                    }
                } else {
                    // The package is already defined and sealing was disabled. We need to make sure the new class
                    // being defined does not have sealing enabled. We cannot redefine a package.
                    if (manifest != null) {
                        String sealed = jarFileEntry.getSealed(packageName);
                        if ("true".equalsIgnoreCase(sealed)) {
                            throw new SecurityException("Sealing violation: package " + packageName
                                    + " is not sealed but new class " + className + " has sealing enabled");
                        }
                    }
                }
                return; // Return if there are no errors
            }

            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkPackageDefinition(packageName);
            }

            // Define a new package for the class
            if (manifest != null) {
                String implTitle = jarFileEntry.getImplementationTitle(packageName);
                String implVendor = jarFileEntry.getImplementationVendor(packageName);
                String implVersion = jarFileEntry.getImplementationVersion(packageName);
                String specTitle = jarFileEntry.getSpecificationTitle(packageName);
                String specVendor = jarFileEntry.getSpecificationVendor(packageName);
                String specVersion = jarFileEntry.getSpecificationVersion(packageName);
                definePackage(packageName, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor, sealBase);
            } else {
                definePackage(packageName, null, null, null, null, null, null, null);
            }
        }
    }

    private void extractFileInformationFromJarFileEntries() throws IOException {
        for (JarFileEntry jarFileEntry : jarFileEntries) {
            int currentDepth = jarFileEntry.getDepth();
            JarFile jarFile = jarFileEntry.getJarFile();
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String name = jarEntry.getName(); // Name should never start with a forward slash

                // Remove trailing slash from directories. A directory resource won't be found since
                // findResource() never has a trailing slash
                if (jarEntry.isDirectory()) {
                    while (name.endsWith("/")) {
                        name = name.substring(0, name.length() - 1);
                    }
                }

                // Skip jar entries since we already have them all
                if (name.endsWith(".jar")) {
                    continue;
                }

                log.log(Level.FINER, "CustomClassLoader found a file: {0} in the JAR file: {1}",
                        new Object[]{name, jarFileEntry.getUrl().toString()});

                // Get the existing entry and overwrite it if we are allowed. Default implementation only allows
                // overrides with a higher depth level than the current. Another file with the same depth will not
                // override the existing entry. This may have security consequences if the package is sealed.
                FileEntryInJar existingFileEntryInJar = fileEntriesInJar.get(name);
                FileEntryInJar newFileEntryInJar = new FileEntryInJar(currentDepth, jarEntry, jarFileEntry, name);
                if (existingFileEntryInJar != null) {
                    if (!allowedToOverwriteFileEntryInJar(existingFileEntryInJar, newFileEntryInJar)) {
                        continue;
                    }
                }
                fileEntriesInJar.put(name, newFileEntryInJar);
            }
        }
    }

    private File extractFileToTempDirectory(JarFile jarFile, JarEntry entry, String name)
            throws IOException {
        File tmpFile = createTempFile(name);
        int totalSize = 0;
        try (InputStream is = jarFile.getInputStream(entry);
                BufferedInputStream bis = new BufferedInputStream(is);
                FileOutputStream fos = new FileOutputStream(tmpFile);
                BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            byte[] bytesRead = new byte[1024 * 1024];
            int len;
            while ((len = bis.read(bytesRead)) != -1) {
                bos.write(bytesRead, 0, len);
                totalSize += len;
            }
        }
        log.log(Level.FINER, "CustomClassLoader extracted a file with a size of: {0}", totalSize);
        return tmpFile;
    }

    private void extractJarFilesInsideRootJar(URL url, int depth) throws IOException {
        if (depth > maxDepthForJarFiles) {
            log.log(Level.WARNING, "CustomClassLoader has reached a max depth and JAR: {0} will be ignored", url);
            return;
        }

        // Open file
        File file;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
        JarFile jarFile = new JarFile(file);

        // Get or create a manifest
        Manifest jarManifest = jarFile.getManifest();
        if (jarManifest == null) {
            jarManifest = new Manifest();
        }

        // Create a new code source for this JAR
        CodeSource rootCodeSource = rootProtectionDomain.getCodeSource();
        Certificate[] rootCertificates = rootCodeSource.getCertificates();
        CodeSource newCodeSource = (rootCertificates == null)
                ? new CodeSource(url, rootCodeSource.getCodeSigners())
                : new CodeSource(url, rootCertificates);

        // Create an entry for this JAR file. We will be searching all JAR file entries later
        JarFileEntry jarFileEntry = new JarFileEntry(depth, newCodeSource, jarFile, jarManifest, url);
        jarFileEntries.add(jarFileEntry);

        // Loop over jar entries and find jar files
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();

            // We don't do anything with directories
            if (jarEntry.isDirectory()) {
                continue;
            }

            String name = jarEntry.getName(); // Name should never start with a forward slash

            // Skip non-jar entries
            if (!name.endsWith(".jar")) {
                continue;
            }

            log.log(Level.FINE, "CustomClassLoader found a JAR file: {0}", name);

            File tmpFile = extractFileToTempDirectory(jarFile, jarEntry, name);
            extractJarFilesInsideRootJar(tmpFile.toURI().toURL(), depth + 1);
        }
    }

    private byte[] getClassBytes(BufferedInputStream bis) throws IOException {
        classByteCodeBuffer.clear();
        byte[] bytesRead = new byte[32 * 1024];
        int len, totalSize = 0;
        while ((len = bis.read(bytesRead)) != -1) {
            classByteCodeBuffer.put(bytesRead, 0, len);
            totalSize += len;
        }
        classByteCodeBuffer.flip();
        byte[] classBytes = new byte[classByteCodeBuffer.remaining()];
        classByteCodeBuffer.get(classBytes, 0, classBytes.length);
        log.log(Level.FINER, "CustomClassLoader loaded a class with a size of: {0}", totalSize);
        return classBytes;
    }

    private URL getClassLocation() {
        return getClass().getResource(CustomClassLoader.class.getSimpleName() + ".class");
    }

    private URL getClassLoadedFromLocation() {
        return getClass().getProtectionDomain().getCodeSource().getLocation();
    }

    private void loadViaProtocol(URL classLocation) {
        String protocol = classLocation.getProtocol();

        log.log(Level.FINE, "CustomClassLoader found protocol of current class: {0}", protocol);

        switch (protocol) {
            case "file":
                try {
                    URI uri = classLocation.toURI();
                    String path = uri.getPath();
                    log.log(Level.FINE, "CustomClassLoader found path of current class: {0}", path);
                } catch (URISyntaxException e) {
                    log.log(Level.SEVERE, "Invalid URI syntax", e);
                    System.exit(-1);
                }
                // Nothing to do since we weren't launched from inside a JAR file
                log.log(Level.FINE, "CustomClassLoader is exiting since protocol wasn't a JAR file");
                return; // RETURN

            case "jar":
                // Fall through
                break;

            default:
                throw new RuntimeException("CustomClassLoader doesn't work for the protocol: " + protocol);
        }

        // Since this is a JAR protocol we have a JAR file
        try {
            URL classLoadedFromURL = getClassLoadedFromLocation();
            extractJarFilesInsideRootJar(classLoadedFromURL, 0);
            extractFileInformationFromJarFileEntries();
        } catch (IOException e) {
            log.log(Level.SEVERE, null, e);
            System.exit(-1);
        }
    }

    private String mapLibraryName(String libraryName) {
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            return "lib" + libraryName + ".jnilib";
        }
        return System.mapLibraryName(libraryName);
    }

    private String mapLibraryNameAlternate(String libraryName) {
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            return "lib" + libraryName + ".dylib";
        }
        return System.mapLibraryName(libraryName);
    }

    protected boolean allowedToOverwriteFileEntryInJar(FileEntryInJar existingFileEntryInJar, FileEntryInJar newFileEntryInJar) {
        return (existingFileEntryInJar.getDepth() < newFileEntryInJar.getDepth());
    }

    /**
     * Finds the class with the specified <a href="#name">binary name</a>. This method should be overridden by class
     * loader implementations that follow the delegation model for loading classes, and will be invoked by the
     * {@link #loadClass <tt>loadClass</tt>} method after checking the parent class loader for the requested class. The
     * default implementation throws a <tt>ClassNotFoundException</tt>.
     *
     * @param className The <a href="#name">binary name</a> of the class
     *
     * @return The resulting <tt>Class</tt> object
     *
     * @throws ClassNotFoundException If the class could not be found
     *
     * @since 1.2
     */
    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        log.log(Level.FINER, "CustomClassLoader finding class: {0}", className);

        Class clazz = loadedClasses.get(className);
        if (clazz != null) {
            log.log(Level.FINEST, "CustomClassLoader found an already defined class: {0}", clazz);
            return clazz;
        }

        String filename = className.replace('.', '/').concat(".class");
        FileEntryInJar fileEntryInJar = fileEntriesInJar.get(filename);
        if (fileEntryInJar == null) {
            log.log(Level.FINEST, "CustomClassLoader no file entry for filename: {0}", filename);
            throw new ClassNotFoundException(className);
        }

        // Define a package for this class
        defineNewPackage(className, fileEntryInJar);

        try {
            // Read the bytes of the class and then define it
            try (InputStream jarInputStream = fileEntryInJar.openStream();
                    BufferedInputStream bis = new BufferedInputStream(jarInputStream)) {
                byte[] classBytes = getClassBytes(bis);
                CodeSource codeSource = fileEntryInJar.getJarFileEntry().getCodeSource();
                clazz = defineClass(className, classBytes, 0, classBytes.length, codeSource);
                loadedClasses.put(className, clazz);
                log.log(Level.FINEST, "CustomClassLoader defined the class: {0}", clazz);
                return clazz;
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Unable to open JarInputStream for fileEntryInJar", e);
            System.exit(-1);
        }

        // Something really bad has happened for us to reach this
        throw new ClassNotFoundException(className);
    }

    /**
     * Returns the absolute path name of a native library. The VM invokes this method to locate the native libraries
     * that belong to classes loaded with this class loader. If this method returns <tt>null</tt>, the VM searches the
     * library along the path specified as the "<tt>java.library.path</tt>" property.
     *
     * @param libraryName The library name
     *
     * @return The absolute path of the native library
     *
     * @see System#loadLibrary(String)
     * @see System#mapLibraryName(String)
     *
     * @since 1.2
     */
    @Override
    protected String findLibrary(String libraryName) {
        log.log(Level.FINER, "CustomClassLoader finding native library: {0}", libraryName);

        libraryName = mapLibraryName(libraryName);

        String fullPathToNativeLibrary = loadedNatives.get(libraryName);
        if (fullPathToNativeLibrary != null) {
            log.log(Level.FINEST, "CustomClassLoader found an already extracted native: {0}", fullPathToNativeLibrary);
            return fullPathToNativeLibrary;
        }

        log.log(Level.FINER, "CustomClassLoader mapped library name: {0}", libraryName);
        FileEntryInJar fileEntryInJar = fileEntriesInJar.get(libraryName);
        if (fileEntryInJar == null) {
            log.log(Level.FINEST, "CustomClassLoader did not find native library: {0}", libraryName);

            libraryName = mapLibraryNameAlternate(libraryName);

            fullPathToNativeLibrary = loadedNatives.get(libraryName);
            if (fullPathToNativeLibrary != null) {
                log.log(Level.FINEST, "CustomClassLoader found an already extracted native: {0}", fullPathToNativeLibrary);
                return fullPathToNativeLibrary;
            }

            log.log(Level.FINER, "CustomClassLoader mapped alternate library name: {0}", libraryName);
            fileEntryInJar = fileEntriesInJar.get(libraryName);
            if (fileEntryInJar == null) {
                log.log(Level.FINEST, "CustomClassLoader did not find native library: {0}", libraryName);
                return super.findLibrary(libraryName);
            }
        }

        try {
            // Read the bytes of the native library and save it to a temp file
            JarFileEntry jarFileEntry = fileEntryInJar.getJarFileEntry();
            JarFile jarFile = jarFileEntry.getJarFile();
            JarEntry jarEntry = fileEntryInJar.getJarEntry();
            File tmpFile = extractFileToTempDirectory(jarFile, jarEntry, fileEntryInJar.getName());
            fullPathToNativeLibrary = tmpFile.getAbsolutePath();
            loadedNatives.put(libraryName, fullPathToNativeLibrary);
            log.log(Level.FINEST, "CustomClassLoader loaded the native library: {0}", fullPathToNativeLibrary);
            return fullPathToNativeLibrary;
        } catch (IOException e) {
            log.log(Level.SEVERE, "Unable to open JarInputStream for fileEntryInJar", e);
            System.exit(-1);
        }

        return null;
    }

    /**
     * Finds the resource with the given name. Class loader implementations should override this method to specify where
     * to find resources.
     *
     * @param resourceName The resource name
     *
     * @return A <tt>URL</tt> object for reading the resource, or
     * <tt>null</tt> if the resource could not be found
     *
     * @since 1.2
     */
    @Override
    protected URL findResource(String resourceName) {
        log.log(Level.FINER, "CustomClassLoader finding resource: {0}", resourceName);

        // If resourceName is a directory it will not have the trailing slash
        FileEntryInJar fileEntryInJar = fileEntriesInJar.get(resourceName);
        if (fileEntryInJar == null) {
            log.log(Level.FINEST, "CustomClassLoader did not find resource: {0}", resourceName);
            return super.findResource(resourceName);
        }

        URL url = fileEntryInJar.getUrl();
        log.log(Level.FINEST, "CustomClassLoader found resource: {0}", resourceName);
        return url;
    }

    /**
     * Returns an enumeration of {@link java.net.URL <tt>URL</tt>} objects representing all the resources with the given
     * name. Class loader implementations should override this method to specify where to load resources from.
     *
     * @param resourceName The resource name
     *
     * @return An enumeration of {@link java.net.URL <tt>URL</tt>} objects for the resources
     *
     * @throws IOException If I/O errors occur
     *
     * @since 1.2
     */
    @Override
    protected Enumeration<URL> findResources(String resourceName) throws IOException {
        log.log(Level.FINER, "CustomClassLoader finding resources: {0}", resourceName);

        Iterator<FileEntryInJar> iterator = fileEntriesInJar.values().iterator();
        List<URL> urls = new ArrayList<>();
        while (iterator.hasNext()) {
            FileEntryInJar fileEntryInJar = iterator.next();
            String name = fileEntryInJar.getName();
            if (name.startsWith(resourceName)) {
                URL url = fileEntryInJar.getUrl();
                urls.add(url);
                log.log(Level.FINEST, "CustomClassLoader found resources: {0}", url);
            }
        }

        if (!urls.isEmpty()) {
            return Collections.enumeration(urls);
        }

        log.log(Level.FINEST, "CustomClassLoader did not find resources: {0}", resourceName);
        return super.findResources(resourceName);
    }

    /**
     * Loads the class with the specified <a href="#name">binary name</a>. The default implementation of this method
     * searches for classes in the following order:
     *
     * <p>
     * <ol>
     *
     * <li><p>
     * Invoke {@link #findLoadedClass(String)} to check if the class has already been loaded.  </p></li>
     *
     * <li><p>
     * Invoke the {@link #loadClass(String) <tt>loadClass</tt>} method on the parent class loader. If the parent is
     * <tt>null</tt> the class loader built-in to the virtual machine is used, instead.  </p></li>
     *
     * <li><p>
     * Invoke the {@link #findClass(String)} method to find the class.  </p></li>
     *
     * </ol>
     * <p>
     *
     * If the class was found using the above steps, and the
     * <tt>resolve</tt> flag is true, this method will then invoke the {@link
     * #resolveClass(Class)} method on the resulting <tt>Class</tt> object.
     *
     * <p>
     * Subclasses of <tt>ClassLoader</tt> are encouraged to override {@link
     * #findClass(String)}, rather than this method.  </p>
     *
     * <p>
     * Unless overridden, this method synchronizes on the result of
     * {@link #getClassLoadingLock <tt>getClassLoadingLock</tt>} method during the entire class loading process.
     *
     * @param className The <a href="#name">binary name</a> of the class
     *
     * @param resolve If <tt>true</tt> then resolve the class
     *
     * @return The resulting <tt>Class</tt> object
     *
     * @throws ClassNotFoundException If the class could not be found
     */
    @Override
    protected synchronized Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        log.log(Level.FINER, "CustomClassLoader loading class: {0}", className);

        // We return null when we can't find a class instead of throwing an exception
        Class clazz = null;
        try {
            clazz = findClass(className);
        } catch (ClassNotFoundException e) {
            // We can't find the class so the parent needs to handle it
        }
        if (clazz == null) {
            // Parent will throw an exception when it can't load the class and will call our
            // findClass() method again. This time we will let the exception buble.
            ClassLoader parent = getParent();
            clazz = parent.loadClass(className);
        }
        if (resolve) {
            resolveClass(clazz);
        }
        return clazz;
    }

    public static class FileEntryInJar {

        private final int depth;
        private final JarEntry jarEntry;
        private final JarFileEntry jarFileEntry;
        private final String name;

        public FileEntryInJar(int depth, JarEntry jarEntry, JarFileEntry jarFileEntry, String name) {
            this.depth = depth;
            this.jarEntry = jarEntry;
            this.jarFileEntry = jarFileEntry;
            this.name = name;
        }

        public int getDepth() {
            return depth;
        }

        public JarEntry getJarEntry() {
            return jarEntry;
        }

        public JarFileEntry getJarFileEntry() {
            return jarFileEntry;
        }

        public String getName() {
            return name;
        }

        public URL getUrl() {
            try {
                return new URL("jar:" + jarFileEntry.getUrl() + "!/" + name);
            } catch (MalformedURLException e) {
                return null;
            }
        }

        public InputStream openStream() throws IOException {
            return jarFileEntry.getJarFile().getInputStream(jarEntry);
        }

        @Override
        public String toString() {
            return "Name: " + name + " Depth: " + depth + " JarFileEntry: " + jarFileEntry;
        }

    }

    public static class JarFileEntry {

        private final int depth;
        private final CodeSource codeSource;
        private final JarFile jarFile;
        private final Manifest manifest;
        private final URL url;

        public JarFileEntry(int depth, CodeSource codeSource, JarFile jarFile, Manifest manifest, URL url) {
            this.depth = depth;
            this.codeSource = codeSource;
            this.jarFile = jarFile;
            this.manifest = manifest;
            this.url = url;
        }

        private String getAttributeFromPackage(String packageName, Name attrName) {
            String path = packageName.replace('.', '/').concat("/");
            Attributes attr = manifest.getAttributes(path);
            if (attr != null) {
                String attrString = attr.getValue(attrName);
                if (attrString != null) {
                    return attrString;
                }
            }
            return manifest.getMainAttributes().getValue(attrName);
        }

        public boolean deleteOnExit() {
            return (depth > 0);
        }

        public int getDepth() {
            return depth;
        }

        public CodeSource getCodeSource() {
            return codeSource;
        }

        public JarFile getJarFile() {
            return jarFile;
        }

        public Manifest getManifest() {
            return manifest;
        }

        public String getImplementationTitle(String packageName) {
            return getAttributeFromPackage(packageName, Name.IMPLEMENTATION_TITLE);
        }

        public String getImplementationVendor(String packageName) {
            return getAttributeFromPackage(packageName, Name.IMPLEMENTATION_VENDOR);
        }

        public String getImplementationVersion(String packageName) {
            return getAttributeFromPackage(packageName, Name.IMPLEMENTATION_VERSION);
        }

        public String getSealed(String packageName) {
            return getAttributeFromPackage(packageName, Name.SEALED);
        }

        public String getSpecificationTitle(String packageName) {
            return getAttributeFromPackage(packageName, Name.SPECIFICATION_TITLE);
        }

        public String getSpecificationVendor(String packageName) {
            return getAttributeFromPackage(packageName, Name.SPECIFICATION_VENDOR);
        }

        public String getSpecificationVersion(String packageName) {
            return getAttributeFromPackage(packageName, Name.SPECIFICATION_VERSION);
        }

        public URL getSealBase(String packageName) {
            String sealed = getAttributeFromPackage(packageName, Name.SEALED);
            if ("true".equalsIgnoreCase(sealed)) {
                return url;
            }
            return null;
        }

        public URL getUrl() {
            return url;
        }

        @Override
        public String toString() {
            return "URL: " + url + " Depth: " + depth + " Manifest: " + manifest;
        }

    }

}
