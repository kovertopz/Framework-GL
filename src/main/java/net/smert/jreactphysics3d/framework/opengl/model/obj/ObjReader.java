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
package net.smert.jreactphysics3d.framework.opengl.model.obj;

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
import net.smert.jreactphysics3d.framework.Files.FileAsset;
import net.smert.jreactphysics3d.framework.Fw;
import net.smert.jreactphysics3d.framework.math.Vector4f;
import net.smert.jreactphysics3d.framework.opengl.GL;
import net.smert.jreactphysics3d.framework.opengl.LightParameterType;
import net.smert.jreactphysics3d.framework.opengl.constants.Primitives;
import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.mesh.Segment;
import net.smert.jreactphysics3d.framework.opengl.model.ModelReader;
import net.smert.jreactphysics3d.framework.opengl.model.obj.MaterialReader.Color;
import net.smert.jreactphysics3d.framework.opengl.model.obj.MaterialReader.Material;
import net.smert.jreactphysics3d.framework.opengl.renderable.Renderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.RenderableConfiguration;
import net.smert.jreactphysics3d.framework.opengl.texture.TextureType;
import net.smert.jreactphysics3d.framework.utils.ListUtils;
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

    private net.smert.jreactphysics3d.framework.opengl.mesh.Material convertMaterialToMeshMaterial(Material material) {
        Color ambient = material.getAmbient();
        Color diffuse = material.getDiffuse();
        Color specular = material.getSpecular();

        net.smert.jreactphysics3d.framework.opengl.mesh.Material meshMaterial = GL.mf.createMaterial();

        // Lighting
        if (ambient.hasBeenSet()) {
            meshMaterial.setLighting(
                    LightParameterType.AMBIENT, new Vector4f(ambient.getR(), ambient.getG(), ambient.getB(), 1.0f));
        }
        if (diffuse.hasBeenSet()) {
            meshMaterial.setLighting(
                    LightParameterType.DIFFUSE, new Vector4f(diffuse.getR(), diffuse.getG(), diffuse.getB(), 1.0f));
        }
        if (specular.hasBeenSet()) {
            meshMaterial.setLighting(
                    LightParameterType.SPECULAR, new Vector4f(specular.getR(), specular.getG(), specular.getB(), 1.0f));
        }
        meshMaterial.setShininess(material.convertSpecularExponent());

        // Textures
        String filename;

        filename = material.getAmbientMapFilename();
        if ((filename != null) && (filename.length() > 0)) {
            meshMaterial.setTexture(TextureType.AMBIENT_OCCLUSION, filename);
        }
        filename = material.getDiffuseMapFilename();
        if ((filename != null) && (filename.length() > 0)) {
            meshMaterial.setTexture(TextureType.DIFFUSE, filename);
        }
        filename = material.getSpecularMapFilename();
        if ((filename != null) && (filename.length() > 0)) {
            meshMaterial.setTexture(TextureType.SPECULAR, filename);
        }
        filename = material.getSpecularExponentMapFilename();
        if ((filename != null) && (filename.length() > 0)) {
            meshMaterial.setTexture(TextureType.SPECULAR_EXPONENT, filename);
        }

        return meshMaterial;
    }

    private Segment convertModelDataToMeshSegment(ModelData modelData, Material material, int[] index) {

        // Create new segment and all parameters
        Segment segment = GL.mf.createSegment();
        segment.setElementCount(modelData.getElementCount());
        segment.setMinIndex(index[0]);
        index[0] += modelData.getElementCount();
        segment.setMaxIndex(index[0] - 1);
        segment.setName(modelData.getName());
        if (modelData.hasNormals()) {
            segment.setNormals(ListUtils.ToPrimitiveFloatArray(modelData.getNormals()));
        }
        segment.setPrimitiveMode(modelData.getPrimitiveMode());
        if (modelData.hasTexCoords()) {
            segment.setTexCoords(ListUtils.ToPrimitiveFloatArray(modelData.getTexCoords()));
        }
        if (modelData.hasVertices()) {
            segment.setVertices(ListUtils.ToPrimitiveFloatArray(modelData.getVertices()));
        }

        // Convert the material if it exists
        if (material != null) {
            net.smert.jreactphysics3d.framework.opengl.mesh.Material meshMaterial
                    = convertMaterialToMeshMaterial(material);
            segment.setMaterial(meshMaterial);
        }

        return segment;
    }

    private void convertToMesh(Mesh mesh) {
        mesh.reset();

        // Create a renderable configuration for the mesh. Set all parameters to match model capabilities.
        RenderableConfiguration config = GL.mf.createRenderableConfiguration();
        config.setColorSize(4);
        config.setColorTypeFloat();
        config.setIndexTypeUnsignedInt();
        config.setTexCoordSize(2); // Could change in 3 spots below
        config.setVertexSize(3);

        // Map materials to their names for easy lookup
        List<Material> materials = materialReader.getMaterials();
        Map<String, Material> materialNameToMaterial = new HashMap<>();
        for (Material material : materials) {
            materialNameToMaterial.put(material.getMaterialName(), material);
        }

        // Map one material to one model data
        Map<String, ModelData> materialNameToModelData = new HashMap<>();

        // Create model data for all faces
        for (Face face : faces) {

            // Not sure how this would be even possible
            if (!face.hasVertices()) {
                log.error("The face had no vertices: {}", face);
                continue;
            }

            // Get material and model data by the material name
            Material material = materialNameToMaterial.get(face.getMaterialName()); // Null key must not have a material
            String modelDataKey = (material == null) ? null : face.getMaterialName();
            ModelData modelData = materialNameToModelData.get(modelDataKey);

            // Create model data if it doesn't exist
            if (modelData == null) {
                modelData = new ModelData(Primitives.TRIANGLES);
                modelData.setName(modelDataKey);
                materialNameToModelData.put(modelDataKey, modelData);
            }

            // Each model data has one render mode. Since face definitions for the
            // same material can be made up of triangles, quads and triangle fans
            // we must convert back into triangles.
            if (face.isTriangle()) {
                Vertex vertex1 = vertices.get(face.getVertexIndex().get(0));
                Vertex vertex2 = vertices.get(face.getVertexIndex().get(1));
                Vertex vertex3 = vertices.get(face.getVertexIndex().get(2));
                modelData.addVertex(vertex1);
                modelData.addVertex(vertex2);
                modelData.addVertex(vertex3);

                if (face.hasNormals()) {
                    Vertex normal1 = normals.get(face.getNormalIndex().get(0));
                    Vertex normal2 = normals.get(face.getNormalIndex().get(1));
                    Vertex normal3 = normals.get(face.getNormalIndex().get(2));
                    modelData.addNormal(normal1);
                    modelData.addNormal(normal2);
                    modelData.addNormal(normal3);
                }
                if (face.hasTexCoords()) {
                    TexCoord texCoord1 = texCoords.get(face.getTexIndex().get(0));
                    TexCoord texCoord2 = texCoords.get(face.getTexIndex().get(1));
                    TexCoord texCoord3 = texCoords.get(face.getTexIndex().get(2));

                    if (texCoord1.hasThree()) {
                        modelData.addTexCoord(texCoord1);
                        modelData.addTexCoord(texCoord2);
                        modelData.addTexCoord(texCoord3);
                        config.setTexCoordSize(3);
                    } else {
                        modelData.addTexCoord(texCoord1.getS(), texCoord1.getT());
                        modelData.addTexCoord(texCoord2.getS(), texCoord2.getT());
                        modelData.addTexCoord(texCoord3.getS(), texCoord3.getT());
                    }
                }
            } else if (face.isQuad()) {
                Vertex vertex1 = vertices.get(face.getVertexIndex().get(0));
                Vertex vertex2 = vertices.get(face.getVertexIndex().get(1));
                Vertex vertex3 = vertices.get(face.getVertexIndex().get(2));
                Vertex vertex4 = vertices.get(face.getVertexIndex().get(3));
                modelData.addVertex(vertex1);
                modelData.addVertex(vertex2);
                modelData.addVertex(vertex3);
                modelData.addVertex(vertex1);
                modelData.addVertex(vertex3);
                modelData.addVertex(vertex4);

                if (face.hasNormals()) {
                    Vertex normal1 = normals.get(face.getNormalIndex().get(0));
                    Vertex normal2 = normals.get(face.getNormalIndex().get(1));
                    Vertex normal3 = normals.get(face.getNormalIndex().get(2));
                    Vertex normal4 = normals.get(face.getNormalIndex().get(3));
                    modelData.addNormal(normal1);
                    modelData.addNormal(normal2);
                    modelData.addNormal(normal3);
                    modelData.addNormal(normal1);
                    modelData.addNormal(normal3);
                    modelData.addNormal(normal4);
                }
                if (face.hasTexCoords()) {
                    TexCoord texCoord1 = texCoords.get(face.getTexIndex().get(0));
                    TexCoord texCoord2 = texCoords.get(face.getTexIndex().get(1));
                    TexCoord texCoord3 = texCoords.get(face.getTexIndex().get(2));
                    TexCoord texCoord4 = texCoords.get(face.getTexIndex().get(3));

                    if (texCoord1.hasThree()) {
                        modelData.addTexCoord(texCoord1);
                        modelData.addTexCoord(texCoord2);
                        modelData.addTexCoord(texCoord3);
                        modelData.addTexCoord(texCoord1);
                        modelData.addTexCoord(texCoord3);
                        modelData.addTexCoord(texCoord4);
                        config.setTexCoordSize(3);
                    } else {
                        modelData.addTexCoord(texCoord1.getS(), texCoord1.getT());
                        modelData.addTexCoord(texCoord2.getS(), texCoord2.getT());
                        modelData.addTexCoord(texCoord3.getS(), texCoord3.getT());
                        modelData.addTexCoord(texCoord1.getS(), texCoord1.getT());
                        modelData.addTexCoord(texCoord3.getS(), texCoord3.getT());
                        modelData.addTexCoord(texCoord4.getS(), texCoord4.getT());
                    }
                }
            } else if (face.isTriangleFan()) {

                // Save the first vertex
                Vertex vertex1 = vertices.get(face.getVertexIndex().get(0));
                Vertex vertexNew = new Vertex();
                Vertex vertexOld;

                // Save the first normal
                Vertex normal1 = new Vertex();
                if (face.hasNormals()) {
                    normal1 = normals.get(face.getNormalIndex().get(0));
                }
                Vertex normalNew = new Vertex();
                Vertex normalOld = new Vertex();

                // Save the first texture coordinate
                TexCoord texCoord1 = new TexCoord();
                if (face.hasTexCoords()) {
                    texCoord1 = texCoords.get(face.getTexIndex().get(0));
                }
                TexCoord texCoordNew = new TexCoord();
                TexCoord texCoordOld = new TexCoord();

                for (int count = 1, i = 1; i < face.getVertexIndex().size(); i++) {
                    vertexOld = vertexNew;
                    vertexNew = vertices.get(face.getVertexIndex().get(i));
                    count++;

                    if (face.hasNormals()) {
                        normalOld = normalNew;
                        normalNew = normals.get(face.getNormalIndex().get(i));
                    }
                    if (face.hasTexCoords()) {
                        texCoordOld = texCoordNew;
                        texCoordNew = texCoords.get(face.getTexIndex().get(i));
                    }

                    // Create a new triangle using the first index, the old index and the new. We reset the count back
                    // to two so the next loop will add another vertex and we will have a count of three again.
                    if (count != 3) {
                        continue;
                    }

                    // Add triangle
                    modelData.addVertex(vertex1);
                    modelData.addVertex(vertexOld);
                    modelData.addVertex(vertexNew);

                    // Add normals for the triangle
                    if (face.hasNormals()) {
                        modelData.addNormal(normal1);
                        modelData.addNormal(normalOld);
                        modelData.addNormal(normalNew);
                    }

                    // Add texture coordinates for the triangle
                    if (face.hasTexCoords()) {
                        if (texCoord1.hasThree()) {
                            modelData.addTexCoord(texCoord1);
                            modelData.addTexCoord(texCoordOld);
                            modelData.addTexCoord(texCoordNew);
                            config.setTexCoordSize(3);
                        } else {
                            modelData.addTexCoord(texCoord1.getS(), texCoord1.getT());
                            modelData.addTexCoord(texCoordOld.getS(), texCoordOld.getT());
                            modelData.addTexCoord(texCoordNew.getS(), texCoordNew.getT());
                        }
                    }

                    // Reset count
                    count = 2;
                }
            }
        }

        // Check to see if a renderable configuration exists before adding it
        int renderableConfigID = Renderable.configPool.getOrAdd(config);
        mesh.setRenderableConfigID(renderableConfigID);

        // Add all the model data to the mesh
        int[] index = new int[]{0};
        Iterator<ModelData> modelData = materialNameToModelData.values().iterator();
        while (modelData.hasNext()) {
            ModelData data = modelData.next();
            assert (data.getVertices().size() > 0);
            String name = data.getName();
            Material material = materialNameToMaterial.get(name);
            Segment segment = convertModelDataToMeshSegment(data, material, index);
            mesh.addSegment(segment);
        }

        // Generate indexes for the model
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
                log.warn("Skipped line with an unsupported token: " + line);
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

    private static class ModelData {

        private int elementCount;
        private final int primitiveMode;
        private final List<Float> normals;
        private final List<Float> texCoords;
        private final List<Float> vertices;
        private String name;

        public ModelData(int primitiveMode) {
            elementCount = 0;
            this.primitiveMode = primitiveMode;
            normals = new ArrayList<>();
            texCoords = new ArrayList<>();
            vertices = new ArrayList<>();
        }

        public void addNormal(Vertex vertex) {
            normals.add(vertex.getX());
            normals.add(vertex.getY());
            normals.add(vertex.getZ());
        }

        public void addTexCoord(float s, float t) {
            texCoords.add(s);
            texCoords.add(t);
        }

        public void addTexCoord(TexCoord texCoord) {
            texCoords.add(texCoord.getS());
            texCoords.add(texCoord.getT());
            texCoords.add(texCoord.getR());
        }

        public void addVertex(Vertex vertex) {
            vertices.add(vertex.getX());
            vertices.add(vertex.getY());
            vertices.add(vertex.getZ());
            elementCount++;
        }

        public int getElementCount() {
            return elementCount;
        }

        public int getPrimitiveMode() {
            return primitiveMode;
        }

        public List<Float> getNormals() {
            return normals;
        }

        public List<Float> getTexCoords() {
            return texCoords;
        }

        public List<Float> getVertices() {
            return vertices;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean hasNormals() {
            return (normals.size() > 0);
        }

        public boolean hasTexCoords() {
            return (texCoords.size() > 0);
        }

        public boolean hasVertices() {
            return (vertices.size() > 0);
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
            x = 0;
            y = 0;
            z = 0;
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
