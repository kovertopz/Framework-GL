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
import net.smert.jreactphysics3d.framework.opengl.constants.Primitives;
import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.mesh.Segment;
import net.smert.jreactphysics3d.framework.opengl.mesh.factory.MeshFactory;
import net.smert.jreactphysics3d.framework.opengl.model.ModelReader;
import net.smert.jreactphysics3d.framework.opengl.model.obj.MaterialReader.Color;
import net.smert.jreactphysics3d.framework.opengl.model.obj.MaterialReader.Material;
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
    private final MeshFactory meshFactory;
    private String groupName;
    private String materialLibrary;
    private String materialName;
    private String objectName;
    private String smoothingGroup;

    public ObjReader(MaterialReader materialReader, MeshFactory meshFactory) {
        resetOnFinish = true;
        faces = new ArrayList<>();
        comments = new ArrayList<>();
        texCoords = new ArrayList<>();
        normals = new ArrayList<>();
        vertices = new ArrayList<>();
        this.materialReader = materialReader;
        this.meshFactory = meshFactory;
        reset();
    }

    private void addComment(StringTokenizer tokenizer) {
        String comment = getRemainingTokens(tokenizer);
        if (comment.length() > 0) {
            comments.add(comment);
        }
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

    private void convertToMesh(Mesh mesh) {
        mesh.reset();

        // Map materials to their names for easy lookup
        List<Material> materials = materialReader.getMaterials();
        Map<String, Material> materialNameToMaterial = new HashMap<>();
        for (Material material : materials) {
            materialNameToMaterial.put(material.getMaterialName(), material);
        }

        // Map one material to one segment
        Map<String, Segment> materialNameToSegments = new HashMap<>();

        for (Face face : faces) {

            // Not sure how this would be even possible
            if (!face.hasVertices()) {
                log.error("The face had no vertices: {}", face);
                continue;
            }

            // Get material and segment by the material name
            Material material = materialNameToMaterial.get(face.getMaterialName());
            String segmentKey = (material == null) ? null : face.getMaterialName();
            Segment segment = materialNameToSegments.get(segmentKey);

            // Create segment if it doesn't exist
            if (segment == null) {
                segment = meshFactory.createSegment();
                segment.setPrimitiveMode(Primitives.TRIANGLES);
                segment.setName(segmentKey);
                materialNameToSegments.put(segmentKey, segment);
            }

            // Each segment has one render mode. Since face definitions for the same
            // material can be made up of triangles, quads and triangle fans we must
            // convert back into triangles.
            if (face.isTriangle()) {
                Vertex vertex1 = vertices.get(face.getVertexIndex().get(0));
                Vertex vertex2 = vertices.get(face.getVertexIndex().get(1));
                Vertex vertex3 = vertices.get(face.getVertexIndex().get(2));
                segment.addVertex(vertex1.getX(), vertex1.getY(), vertex1.getZ());
                segment.addVertex(vertex2.getX(), vertex2.getY(), vertex2.getZ());
                segment.addVertex(vertex3.getX(), vertex3.getY(), vertex3.getZ());

                if (face.hasNormals()) {
                    Vertex normal1 = normals.get(face.getNormalIndex().get(0));
                    Vertex normal2 = normals.get(face.getNormalIndex().get(1));
                    Vertex normal3 = normals.get(face.getNormalIndex().get(2));
                    segment.addNormal(normal1.getX(), normal1.getY(), normal1.getZ());
                    segment.addNormal(normal2.getX(), normal2.getY(), normal2.getZ());
                    segment.addNormal(normal3.getX(), normal3.getY(), normal3.getZ());
                }
                if (face.hasTexCoords()) {
                    TexCoord texCoord1 = texCoords.get(face.getTexIndex().get(0));
                    TexCoord texCoord2 = texCoords.get(face.getTexIndex().get(1));
                    TexCoord texCoord3 = texCoords.get(face.getTexIndex().get(2));

                    if (texCoord1.hasThree()) {
                        segment.addTexCoord(texCoord1.getU(), texCoord1.getV(), texCoord1.getW());
                        segment.addTexCoord(texCoord2.getU(), texCoord2.getV(), texCoord2.getW());
                        segment.addTexCoord(texCoord3.getU(), texCoord3.getV(), texCoord3.getW());
                    } else {
                        segment.addTexCoord(texCoord1.getU(), texCoord1.getV());
                        segment.addTexCoord(texCoord2.getU(), texCoord2.getV());
                        segment.addTexCoord(texCoord3.getU(), texCoord3.getV());
                    }
                }
            } else if (face.isQuad()) {
                Vertex vertex1 = vertices.get(face.getVertexIndex().get(0));
                Vertex vertex2 = vertices.get(face.getVertexIndex().get(1));
                Vertex vertex3 = vertices.get(face.getVertexIndex().get(2));
                Vertex vertex4 = vertices.get(face.getVertexIndex().get(3));
                segment.addVertex(vertex1.getX(), vertex1.getY(), vertex1.getZ());
                segment.addVertex(vertex2.getX(), vertex2.getY(), vertex2.getZ());
                segment.addVertex(vertex3.getX(), vertex3.getY(), vertex3.getZ());
                segment.addVertex(vertex1.getX(), vertex1.getY(), vertex1.getZ());
                segment.addVertex(vertex3.getX(), vertex3.getY(), vertex3.getZ());
                segment.addVertex(vertex4.getX(), vertex4.getY(), vertex4.getZ());

                if (face.hasNormals()) {
                    Vertex normal1 = normals.get(face.getNormalIndex().get(0));
                    Vertex normal2 = normals.get(face.getNormalIndex().get(1));
                    Vertex normal3 = normals.get(face.getNormalIndex().get(2));
                    Vertex normal4 = normals.get(face.getNormalIndex().get(3));
                    segment.addNormal(normal1.getX(), normal1.getY(), normal1.getZ());
                    segment.addNormal(normal2.getX(), normal2.getY(), normal2.getZ());
                    segment.addNormal(normal3.getX(), normal3.getY(), normal3.getZ());
                    segment.addNormal(normal1.getX(), normal1.getY(), normal1.getZ());
                    segment.addNormal(normal3.getX(), normal3.getY(), normal3.getZ());
                    segment.addNormal(normal4.getX(), normal4.getY(), normal4.getZ());
                }
                if (face.hasTexCoords()) {
                    TexCoord texCoord1 = texCoords.get(face.getTexIndex().get(0));
                    TexCoord texCoord2 = texCoords.get(face.getTexIndex().get(1));
                    TexCoord texCoord3 = texCoords.get(face.getTexIndex().get(2));
                    TexCoord texCoord4 = texCoords.get(face.getTexIndex().get(3));

                    if (texCoord1.hasThree()) {
                        segment.addTexCoord(texCoord1.getU(), texCoord1.getV(), texCoord1.getW());
                        segment.addTexCoord(texCoord2.getU(), texCoord2.getV(), texCoord2.getW());
                        segment.addTexCoord(texCoord3.getU(), texCoord3.getV(), texCoord3.getW());
                        segment.addTexCoord(texCoord1.getU(), texCoord1.getV(), texCoord1.getW());
                        segment.addTexCoord(texCoord3.getU(), texCoord3.getV(), texCoord3.getW());
                        segment.addTexCoord(texCoord4.getU(), texCoord4.getV(), texCoord4.getW());
                    } else {
                        segment.addTexCoord(texCoord1.getU(), texCoord1.getV());
                        segment.addTexCoord(texCoord2.getU(), texCoord2.getV());
                        segment.addTexCoord(texCoord3.getU(), texCoord3.getV());
                        segment.addTexCoord(texCoord1.getU(), texCoord1.getV());
                        segment.addTexCoord(texCoord3.getU(), texCoord3.getV());
                        segment.addTexCoord(texCoord4.getU(), texCoord4.getV());
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

                for (int count = 1, i = 1, max = face.getVertexIndex().size(); i < max; i++) {
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
                    if (count == 3) {

                        // Add triangle
                        segment.addVertex(vertex1.getX(), vertex1.getY(), vertex1.getZ());
                        segment.addVertex(vertexOld.getX(), vertexOld.getY(), vertexOld.getZ());
                        segment.addVertex(vertexNew.getX(), vertexNew.getY(), vertexNew.getZ());

                        // Add normals for the triangle
                        if (face.hasNormals()) {
                            segment.addNormal(normal1.getX(), normal1.getY(), normal1.getZ());
                            segment.addNormal(normalOld.getX(), normalOld.getY(), normalOld.getZ());
                            segment.addNormal(normalNew.getX(), normalNew.getY(), normalNew.getZ());
                        }

                        // Add texture coordinates for the triangle
                        if (face.hasTexCoords()) {
                            if (texCoord1.hasThree()) {
                                segment.addTexCoord(texCoord1.getU(), texCoord1.getV(), texCoord1.getW());
                                segment.addTexCoord(texCoordOld.getU(), texCoordOld.getV(), texCoordOld.getW());
                                segment.addTexCoord(texCoordNew.getU(), texCoordNew.getV(), texCoordNew.getW());
                            } else {
                                segment.addTexCoord(texCoord1.getU(), texCoord1.getV());
                                segment.addTexCoord(texCoordOld.getU(), texCoordOld.getV());
                                segment.addTexCoord(texCoordNew.getU(), texCoordNew.getV());
                            }
                        }

                        // Reset count
                        count = 2;
                    }
                }
            }
        }

        // Add all the segments to the mesh
        Iterator<Segment> segments = materialNameToSegments.values().iterator();
        while (segments.hasNext()) {
            Segment segment = segments.next();
            String name = segment.getName();

            if (name != null) {
                Material material = materialNameToMaterial.get(name);
                convertToMeshMaterial(segment, material);
            }

            assert (segment.getVertices().size() > 0);
            mesh.addSegment(segment);
        }
    }

    private void convertToMeshMaterial(Segment segment, Material material) {
        Color ambient = material.getAmbient();
        Color diffuse = material.getDiffuse();
        Color specular = material.getSpecular();

        net.smert.jreactphysics3d.framework.opengl.mesh.Material meshMaterial = meshFactory.createMaterial();

        // Lighting
        if (ambient.hasBeenSet()) {
            meshMaterial.setLighting(
                    "ambient", new Vector4f(ambient.getR(), ambient.getG(), ambient.getB(), 1.0f));
        }
        if (diffuse.hasBeenSet()) {
            meshMaterial.setLighting(
                    "diffuse", new Vector4f(diffuse.getR(), diffuse.getG(), diffuse.getB(), 1.0f));
        }
        if (specular.hasBeenSet()) {
            meshMaterial.setLighting(
                    "specular", new Vector4f(specular.getR(), specular.getG(), specular.getB(), 1.0f));
        }
        meshMaterial.setShininess(material.convertSpecularExponent());

        // Textures
        String filename;

        filename = material.getAmbientMapFilename();
        if ((filename != null) && (filename.length() > 0)) {
            meshMaterial.setTexture("ambient", filename);
        }
        filename = material.getDiffuseMapFilename();
        if ((filename != null) && (filename.length() > 0)) {
            meshMaterial.setTexture("diffuse", filename);
        }
        filename = material.getSpecularMapFilename();
        if ((filename != null) && (filename.length() > 0)) {
            meshMaterial.setTexture("specular", filename);
        }
        filename = material.getSpecularExponentMapFilename();
        if ((filename != null) && (filename.length() > 0)) {
            meshMaterial.setTexture("shininess", filename);
        }

        // Add material to mesh
        segment.setMaterial(meshMaterial);
    }

    private String getNextTokenOnly(StringTokenizer tokenizer) {
        String nextToken = "";
        if (tokenizer.hasMoreTokens()) {
            nextToken = tokenizer.nextToken();
        }
        return nextToken;
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
        int totalTokens = tokenizer.countTokens();

        if (tokenizer.hasMoreTokens()) {
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
        if (materialLibrary.length() > 0) {

            // Take objFilename and strip the filename portion from it
            String separator = Fw.files.INTERNAL_FILE_SEPARATOR;
            int lastSlash = objFilename.lastIndexOf(separator);
            String directory = objFilename.substring(0, lastSlash);
            String materialFilename = directory + separator + materialLibrary;
            materialReader.reset();
            materialReader.load(materialFilename, mesh);
        }
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
        }

        private void addIndex(List<Integer> indexes, String index) {
            if (index.length() > 0) {
                int idx = indexToArray(index);
                indexes.add(idx);
            }
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

    private static class TexCoord {

        private float u;
        private float v;
        private float w;

        private TexCoord() {
            u = -Float.MIN_VALUE;
            v = -Float.MIN_VALUE;
            w = -Float.MIN_VALUE;
        }

        public float getU() {
            return u;
        }

        public float getV() {
            return v;
        }

        public float getW() {
            return w;
        }

        public boolean hasTwo() {
            return (u != -Float.MIN_VALUE) && (v != -Float.MIN_VALUE) && (w == -Float.MIN_VALUE);
        }

        public boolean hasThree() {
            return (u != -Float.MIN_VALUE) && (v != -Float.MIN_VALUE) && (w != -Float.MIN_VALUE);
        }

        public void set(int index, float value) {
            if (index == 0) {
                u = value;
            } else if (index == 1) {
                v = value;
            } else if (index == 2) {
                w = value;
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
