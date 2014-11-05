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
package net.smert.frameworkgl.opengl.model.obj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import net.smert.frameworkgl.Files.FileAsset;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.math.Vector4f;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.MaterialLight;
import net.smert.frameworkgl.opengl.TextureType;
import net.smert.frameworkgl.opengl.constants.Primitives;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.mesh.Segment;
import net.smert.frameworkgl.opengl.mesh.SegmentMaterial;
import net.smert.frameworkgl.opengl.mesh.Tessellator;
import net.smert.frameworkgl.opengl.model.ModelReader;
import net.smert.frameworkgl.opengl.model.obj.MaterialReader.Color;
import net.smert.frameworkgl.opengl.model.obj.MaterialReader.Material;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderable.RenderableConfiguration;
import net.smert.frameworkgl.utils.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://www.martinreddy.net/gfx/3d/OBJ.spec
 *
 * http://en.wikipedia.org/wiki/Wavefront_.obj_file
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class ObjReader implements ModelReader {

    private final static Logger log = LoggerFactory.getLogger(ObjReader.class);

    private boolean resetOnFinish;
    private final List<Face> faces;
    private final List<String> comments;
    private final List<TexCoord> texCoords;
    private final List<Vertex> normals;
    private final List<Vertex> vertices;
    private final MaterialReader materialReader;
    private String groupName;
    private String materialLibrary;
    private String materialName;
    private String objectName;
    private String smoothingGroup;

    public ObjReader(MaterialReader materialReader) {
        resetOnFinish = true;
        faces = new ArrayList<>();
        comments = new ArrayList<>();
        texCoords = new ArrayList<>();
        normals = new ArrayList<>();
        vertices = new ArrayList<>();
        this.materialReader = materialReader;
        reset();
    }

    private void addComment(StringTokenizer tokenizer) {
        String comment = getRemainingTokens(tokenizer);
        if (comment.length() <= 0) {
            return;
        }
        comments.add(comment);
    }

    private void addFace(StringTokenizer tokenizer) {

        // Create a new Face
        Face face = new Face(materialName);

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            String[] geometricTextureNormal = token.split("/"); // v/vt/vn
            int totalGeometricTextureNormal = geometricTextureNormal.length;

            if ((totalGeometricTextureNormal < 1) || (totalGeometricTextureNormal > 3)) {
                log.warn("Invalid face definition: {}", token);
                return;
            }

            // Add each type
            if (totalGeometricTextureNormal == 3) {
                face.addNormalIndex(geometricTextureNormal[2]);
            }
            if (totalGeometricTextureNormal >= 2) {
                face.addTexIndex(geometricTextureNormal[1]);
            }
            face.addVertexIndex(geometricTextureNormal[0]);
        }

        // Was the face valid?
        if (!face.isValid()) {
            log.warn("Invalid face: {}", face);
            return;
        }

        // Add the face
        faces.add(face);
    }

    private void addNormalOrVertex(StringTokenizer tokenizer, List<Vertex> normalsOrVertices) {
        int index = 0;
        Vertex normalOrVertex = new Vertex();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            normalOrVertex.set(index++, Float.parseFloat(token));
        }
        assert (index == 3);
        normalsOrVertices.add(normalOrVertex);
    }

    private void addNormal(StringTokenizer tokenizer) {
        addNormalOrVertex(tokenizer, normals);
    }

    private void addTexCoord(StringTokenizer tokenizer) {
        int index = 0;
        TexCoord texCoord = new TexCoord();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            texCoord.set(index++, Float.parseFloat(token));
        }
        assert ((index == 2) || (index == 3));
        texCoords.add(texCoord);
    }

    private void addVertex(StringTokenizer tokenizer) {
        addNormalOrVertex(tokenizer, vertices);
    }

    private SegmentMaterial convertMaterialToSegmentMaterial(Material material) {
        int specularExponent = material.convertSpecularExponent();
        Color ambient = material.getAmbient();
        Color diffuse = material.getDiffuse();
        Color specular = material.getSpecular();

        SegmentMaterial segmentMaterial = GL.meshFactory.createSegmentMaterial();
        MaterialLight materialLight = GL.glFactory.createMaterialLight();

        // Lighting
        if (ambient.hasBeenSet()) {
            materialLight.setAmbient(new Vector4f(ambient.getR(), ambient.getG(), ambient.getB(), 1f));
        }
        if (diffuse.hasBeenSet()) {
            materialLight.setDiffuse(new Vector4f(diffuse.getR(), diffuse.getG(), diffuse.getB(), 1f));
        }
        if (specular.hasBeenSet()) {
            materialLight.setSpecular(new Vector4f(specular.getR(), specular.getG(), specular.getB(), 1f));
        }
        materialLight.setShininess(specularExponent);

        // Save the material name and add the material to the pool
        String materialLightName = "materialLight_" + materialLight.hashCode();
        segmentMaterial.setMaterialLightName(materialLightName);
        if (Renderable.materialLightPool.getUniqueID(materialLightName) == -1) {
            Renderable.materialLightPool.add(materialLightName, materialLight);
        }

        // Textures
        String filename;
        filename = material.getAmbientMapFilename();
        if ((filename != null) && (filename.length() > 0)) {
            segmentMaterial.setTexture(TextureType.AMBIENT_OCCLUSION, filename);
        }
        filename = material.getDiffuseMapFilename();
        if ((filename != null) && (filename.length() > 0)) {
            segmentMaterial.setTexture(TextureType.DIFFUSE, filename);
        }
        filename = material.getSpecularMapFilename();
        if ((filename != null) && (filename.length() > 0)) {
            segmentMaterial.setTexture(TextureType.SPECULAR, filename);
        }
        filename = material.getSpecularExponentMapFilename();
        if ((filename != null) && (filename.length() > 0)) {
            segmentMaterial.setTexture(TextureType.SPECULAR_EXPONENT, filename);
        }

        return segmentMaterial;
    }

    private void convertToMesh(Mesh mesh) {
        mesh.reset();

        // Create a renderable configuration for the mesh. Set all parameters to match OBJ capabilities.
        RenderableConfiguration config = GL.meshFactory.createRenderableConfiguration();
        config.setColorSize(4);
        config.setColorTypeFloat();
        config.setIndexTypeUnsignedInt();
        config.setTexCoordSize(2); // Could change in 2 spots below
        config.setVertexSize(3);

        // Reset texture coordinate state
        MeshConversion.hasThree = false;
        MeshConversion.isLocked = false;

        // Create a conversion state for each expected type
        Tessellator.ConversionState conversionStateQuads
                = Tessellator.CreateConversionState(Primitives.QUADS);
        Tessellator.ConversionState conversionStateTriangleFan
                = Tessellator.CreateConversionState(Primitives.TRIANGLE_FAN);
        Tessellator.ConversionState conversionStateTriangles
                = Tessellator.CreateConversionState(Primitives.TRIANGLES);

        // Map materials to their names for easy lookup
        List<Material> materials = materialReader.getMaterials();
        Map<String, Material> materialNameToMaterial = new HashMap<>();
        for (Material material : materials) {
            materialNameToMaterial.put(material.getMaterialName(), material);
        }

        // Map one material to a tessellator
        Map<String, Tessellator> materialNameToTessellator = new HashMap<>();

        // Add each face to the correct tessellator
        for (Face face : faces) {

            // Not sure how this would be even possible
            if (!face.hasVertices()) {
                log.error("The face had no vertices: {}", face);
                continue;
            }

            // Get material and tessellator by the material name
            Material material = materialNameToMaterial.get(face.getMaterialName()); // getMaterialName() is never null
            String tessellatorKey = (material == null) ? null : face.getMaterialName();
            // All faces which did not have a matching material get mapped to the NULL key
            Tessellator tessellator = materialNameToTessellator.get(tessellatorKey);

            // Create model data if it doesn't exist
            if (tessellator == null) {
                tessellator = GL.meshFactory.createTessellator();
                tessellator.setConfig(config);
                tessellator.setConvertToTriangles(true); // Don't rely on defaults
                tessellator.start(Primitives.TRIANGLES, true); // Force conversion
                materialNameToTessellator.put(tessellatorKey, tessellator);
            }

            // Face definitions for the same material can be made up of triangles, quads
            // and triangle fans so we must convert back into triangles.
            if ((face.isTriangle()) || (face.isQuad())) {

                // Determine what type of conversion state is needed
                Tessellator.ConversionState conversionState;
                if (face.isTriangle()) {
                    conversionState = conversionStateTriangles;
                } else {
                    conversionState = conversionStateQuads;
                }

                // Reset conversion state
                conversionState.reset();

                if (face.hasNormals()) {
                    Vertex normal1 = normals.get(face.getNormalIndex().get(0));
                    Vertex normal2 = normals.get(face.getNormalIndex().get(1));
                    Vertex normal3 = normals.get(face.getNormalIndex().get(2));
                    MeshConversion.addNormal(conversionState, normal1);
                    MeshConversion.addNormal(conversionState, normal2);
                    MeshConversion.addNormal(conversionState, normal3);
                    if (face.isQuad()) {
                        Vertex normal4 = normals.get(face.getNormalIndex().get(3));
                        MeshConversion.addNormal(conversionState, normal4);
                    }
                }
                if (face.hasTexCoords()) {
                    TexCoord texCoord1 = texCoords.get(face.getTexIndex().get(0));
                    TexCoord texCoord2 = texCoords.get(face.getTexIndex().get(1));
                    TexCoord texCoord3 = texCoords.get(face.getTexIndex().get(2));
                    MeshConversion.checkTexCoord(texCoord1, config);
                    MeshConversion.addTexCoord(conversionState, texCoord1);
                    MeshConversion.addTexCoord(conversionState, texCoord2);
                    MeshConversion.addTexCoord(conversionState, texCoord3);
                    if (face.isQuad()) {
                        TexCoord texCoord4 = texCoords.get(face.getTexIndex().get(3));
                        MeshConversion.addTexCoord(conversionState, texCoord4);
                    }
                }
                Vertex vertex1 = vertices.get(face.getVertexIndex().get(0));
                Vertex vertex2 = vertices.get(face.getVertexIndex().get(1));
                Vertex vertex3 = vertices.get(face.getVertexIndex().get(2));
                MeshConversion.addVertex(conversionState, vertex1);
                MeshConversion.addVertex(conversionState, vertex2);
                MeshConversion.addVertex(conversionState, vertex3);
                if (face.isQuad()) {
                    Vertex vertex4 = vertices.get(face.getVertexIndex().get(3));
                    MeshConversion.addVertex(conversionState, vertex4);
                }

                // Add the triangle or quad to the tessellator. If it's a quad it will be converted.
                if (face.isTriangle()) {
                    conversionState.addTriangle(tessellator);
                } else {
                    conversionState.addQuad(tessellator);
                }
            } else if (face.isTriangleFan()) {

                // Reset conversion state
                conversionStateTriangleFan.reset();

                for (int i = 0; i < face.getVertexIndex().size(); i++) {

                    if (face.hasNormals()) {
                        Vertex normal = normals.get(face.getNormalIndex().get(i));
                        MeshConversion.addNormal(conversionStateTriangleFan, normal);
                    }
                    if (face.hasTexCoords()) {
                        TexCoord texCoord = texCoords.get(face.getTexIndex().get(i));
                        MeshConversion.checkTexCoord(texCoord, config);
                        MeshConversion.addTexCoord(conversionStateTriangleFan, texCoord);
                    }
                    Vertex vertex = vertices.get(face.getVertexIndex().get(i));
                    MeshConversion.addVertex(conversionStateTriangleFan, vertex);

                    conversionStateTriangleFan.convert(tessellator);
                }
            }
        }

        // Check to see if a renderable configuration exists before adding it
        int renderableConfigID = Renderable.configPool.getOrAdd(config);
        mesh.setRenderableConfigID(renderableConfigID);

        // Add all the model data to the mesh
        Iterator<Map.Entry<String, Tessellator>> tessellators = materialNameToTessellator.entrySet().iterator();
        while (tessellators.hasNext()) {
            Map.Entry<String, Tessellator> entry = tessellators.next();
            String name = entry.getKey();
            Tessellator tessellator = entry.getValue();
            assert (tessellator.getElementCount() > 0);

            // Stop the primitive mode of the tessellator
            tessellator.stop();

            // Calculate normals if there were none
            if (tessellator.getNormalsCount() == 0) {
                tessellator.calculateNormals();
            }

            // Convert model data into a mesh segment
            Segment segment = tessellator.createSegment(name);

            // Convert the material if it exists
            Material material = materialNameToMaterial.get(name);
            if (material != null) {
                SegmentMaterial segmentMaterial = convertMaterialToSegmentMaterial(material);
                segment.setMaterial(segmentMaterial);
            }

            // Add the segment to the mesh
            mesh.addSegment(segment);
        }

        // Generate indexes for the model
        int[] index = new int[]{0};
        for (int i = 0; i < mesh.getTotalSegments(); i++) {
            Segment segment = mesh.getSegment(i);
            segment.setMinIndex(index[0]);
            index[0] += segment.getElementCount();
            segment.setMaxIndex(index[0] - 1);
        }
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < index[0]; i++) {
            indexes.add(i);
        }
        mesh.setIndexes(ListUtils.ToPrimitiveIntArray(indexes));
    }

    private String getNextTokenOnly(StringTokenizer tokenizer) {
        if (!tokenizer.hasMoreTokens()) {
            return "";
        }
        return tokenizer.nextToken();
    }

    private String getRemainingTokens(StringTokenizer tokenizer) {
        StringBuilder remainingTokens = new StringBuilder(64);

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            remainingTokens.append(token).append(" ");
        }

        if (remainingTokens.length() > 0) {
            int count = Character.charCount(remainingTokens.codePointAt(remainingTokens.length() - 1));
            while (count > 0) {
                remainingTokens.deleteCharAt(remainingTokens.length() - 1);
                count--;
            }
        }

        return remainingTokens.toString();
    }

    private void parse(String line) {
        StringTokenizer tokenizer = new StringTokenizer(line);

        if (!tokenizer.hasMoreTokens()) {
            return;
        }

        int totalTokens = tokenizer.countTokens();
        String token = tokenizer.nextToken();

        switch (token) {
            case "#":
                // Ex: "# Some random comment"
                addComment(tokenizer);
                break;

            case "f":
                // Ex: "f v1 v2 v3 ... vN"
                // Ex: "f v1/vt1 v2/vt2 v3/vt3 ... vN/vtN"
                // Ex: "f v1/vt1/vn1 v2/vt2/vn2 v3/vt3/vn3 ... vN/vtN/vnN"
                // Ex: "f v1//vn1 v2//vn2 v3//vn3 ... vN//vnN"
                // Ex: "f v1// v2// v3// ... vN//"
                if (totalTokens >= 4) {
                    addFace(tokenizer);
                } else {
                    log.warn("Invalid face definition: {}", line);
                }
                break;

            case "g":
                // Blender replaces spaces with underscores. Seems to group faces.
                // Ex: "g some_group_name"
                // Spec says multiple groups can be applied at the same time. Only supporting one.
                // Not using groups in our implementation
                groupName = getNextTokenOnly(tokenizer);
                break;

            case "mtllib":
                // Format supports multiple but we only use the first one. The name could have spaces in it so
                // hopefully there is only one material.
                // Ex: "mtllib some material filename with spaces.mtl"
                materialLibrary = getRemainingTokens(tokenizer);
                break;

            case "o":
                // Blender replaces spaces with underscores. Seems to group vertices.
                // Ex: "o some_object_name"
                objectName = getNextTokenOnly(tokenizer);
                break;

            case "s":
                // Not using smoothing groups in our implementation
                smoothingGroup = getNextTokenOnly(tokenizer);
                break;

            case "usemtl":
                // Blender replaces spaces with underscores. Material applies to face definitions.
                // Ex: "usemtl Material_Name"
                materialName = getNextTokenOnly(tokenizer);
                break;

            case "v":
                // Ex: "v vX vY vZ"
                if (totalTokens == 4) {
                    addVertex(tokenizer);
                } else {
                    log.warn("Invalid vertex definition: {}", line);
                }
                break;

            case "vn":
                // Ex: "vn nX nY nZ"
                if (totalTokens == 4) {
                    addNormal(tokenizer);
                } else {
                    log.warn("Invalid vertex definition: {}", line);
                }
                break;

            case "vt":
                // Spec says we support one texture coordinate but ignoring
                // Ex: "vt tU tV"
                // Ex: "vt tU tV tW"
                if ((totalTokens == 3) || (totalTokens == 4)) {
                    addTexCoord(tokenizer);
                } else {
                    log.warn("Invalid texture definition: {}", line);
                }
                break;

            default:
                log.warn("Skipped line with an unsupported token: {}", line);
        }
    }

    private void read(String filename) throws IOException {
        FileAsset fileAsset = Fw.files.getMesh(filename);

        try (InputStream is = fileAsset.openStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(isr)) {
            String line;

            while ((line = reader.readLine()) != null) {
                parse(line);
            }
        }
    }

    private void readMaterial(String objFilename, Mesh mesh) throws IOException {
        if (materialLibrary.length() <= 0) {
            return;
        }

        // Take objFilename and strip the filename portion from it
        String materialFilename;
        String separator = Fw.files.INTERNAL_FILE_SEPARATOR;
        int lastSlash = objFilename.lastIndexOf(separator);
        if (lastSlash != -1) {
            String directory = objFilename.substring(0, lastSlash);
            materialFilename = directory + separator + materialLibrary;
        } else {
            materialFilename = materialLibrary;
        }
        materialReader.reset();
        materialReader.load(materialFilename, mesh);
    }

    private void reset() {
        faces.clear();
        comments.clear();
        texCoords.clear();
        normals.clear();
        vertices.clear();
        groupName = "";
        materialLibrary = "";
        materialName = "";
        objectName = "";
        smoothingGroup = "";
    }

    public boolean isResetOnFinish() {
        return resetOnFinish;
    }

    public void setResetOnFinish(boolean resetOnFinish) {
        this.resetOnFinish = resetOnFinish;
    }

    @Override
    public void load(String filename, Mesh mesh) throws IOException {
        log.info("Loading OBJ model: {}", filename);
        reset();
        read(filename);
        readMaterial(filename, mesh);
        convertToMesh(mesh);
        if (resetOnFinish) {
            reset();
            materialReader.reset();
        }
    }

    private static class Face {

        private final List<Integer> normalIndex;
        private final List<Integer> texIndex;
        private final List<Integer> vertexIndex;
        private final String materialName;

        private Face(String materialName) {
            normalIndex = new ArrayList<>();
            texIndex = new ArrayList<>();
            vertexIndex = new ArrayList<>();
            this.materialName = materialName;
            assert (materialName != null);
        }

        private void addIndex(List<Integer> indexes, String index) {
            if (index.length() <= 0) {
                return;
            }
            int idx = indexToArray(index);
            indexes.add(idx);
        }

        private int indexToArray(String index) {
            return Integer.parseInt(index) - 1;
        }

        public void addNormalIndex(String index) {
            addIndex(normalIndex, index);
        }

        public void addTexIndex(String index) {
            addIndex(texIndex, index);
        }

        public void addVertexIndex(String index) {
            addIndex(vertexIndex, index);
        }

        public boolean hasNormals() {
            return (normalIndex.size() > 0);
        }

        public boolean hasTexCoords() {
            return (texIndex.size() > 0);
        }

        public boolean hasVertices() {
            return (vertexIndex.size() > 0);
        }

        public List<Integer> getNormalIndex() {
            return normalIndex;
        }

        public List<Integer> getTexIndex() {
            return texIndex;
        }

        public List<Integer> getVertexIndex() {
            return vertexIndex;
        }

        public String getMaterialName() {
            return materialName;
        }

        public boolean isQuad() {
            return (vertexIndex.size() == 4);
        }

        public boolean isTriangle() {
            return (vertexIndex.size() == 3);
        }

        public boolean isTriangleFan() {
            return (vertexIndex.size() >= 5);
        }

        public boolean isValid() {
            return (normalIndex.size() == normalIndex.size() == vertexIndex.size() >= 3);
        }

        @Override
        public String toString() {
            return "(normal indexes: " + normalIndex.size()
                    + " texture indexes: " + texIndex.size() + " vertex indexes: " + vertexIndex.size() + ")";
        }

    }

    private static class MeshConversion {

        public static boolean hasThree;
        public static boolean isLocked;

        public static void addNormal(Tessellator.ConversionState conversionState, Vertex vertex) {
            conversionState.getNormal().set(vertex.getX(), vertex.getY(), vertex.getZ());
            conversionState.addNormalConversion(conversionState.getNormal());
        }

        public static void addTexCoord(Tessellator.ConversionState conversionState, TexCoord texCoord) {
            conversionState.getTexCoord().set(texCoord.getS(), texCoord.getT(), texCoord.getR());
            conversionState.addTexCoordConversion(conversionState.getTexCoord());
        }

        public static void addVertex(Tessellator.ConversionState conversionState, Vertex vertex) {
            conversionState.getVertex().set(vertex.getX(), vertex.getY(), vertex.getZ(), 1f);
            conversionState.addVertexConversion(conversionState.getVertex());
        }

        public static void checkTexCoord(TexCoord texCoord, RenderableConfiguration config) {
            if (texCoord.hasThree()) {
                if (!isLocked || (isLocked && hasThree)) {
                    config.setTexCoordSize(3);
                    hasThree = true;
                    isLocked = true;
                } else {
                    throw new RuntimeException("You cannot switch from 2 texture coords to 3");
                }
            } else {
                if (!isLocked || (isLocked && !hasThree)) {
                    hasThree = false;
                    isLocked = true;
                } else {
                    throw new RuntimeException("You cannot switch from 3 texture coords to 2");
                }
            }
        }

    }

    private static class TexCoord {

        private float s;
        private float t;
        private float r;

        private TexCoord() {
            s = -Float.MIN_VALUE;
            t = -Float.MIN_VALUE;
            r = -Float.MIN_VALUE;
        }

        public float getS() {
            return s;
        }

        public float getT() {
            return t;
        }

        public float getR() {
            return r;
        }

        public boolean hasTwo() {
            return (s != -Float.MIN_VALUE) && (t != -Float.MIN_VALUE) && (r == -Float.MIN_VALUE);
        }

        public boolean hasThree() {
            return (s != -Float.MIN_VALUE) && (t != -Float.MIN_VALUE) && (r != -Float.MIN_VALUE);
        }

        public void set(int index, float value) {
            if (index == 0) {
                s = value;
            } else if (index == 1) {
                t = value;
            } else if (index == 2) {
                r = value;
            } else {
                throw new IllegalArgumentException("Unknown index: " + index);
            }
        }

    }

    private static class Vertex {

        private float x;
        private float y;
        private float z;

        private Vertex() {
            x = 0f;
            y = 0f;
            z = 0f;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getZ() {
            return z;
        }

        public void set(int index, float value) {
            if (index == 0) {
                x = value;
            } else if (index == 1) {
                y = value;
            } else if (index == 2) {
                z = value;
            } else {
                throw new IllegalArgumentException("Unknown index: " + index);
            }
        }

    }

}
