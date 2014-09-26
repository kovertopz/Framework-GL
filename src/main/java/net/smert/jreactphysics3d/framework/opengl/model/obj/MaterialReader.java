package net.smert.jreactphysics3d.framework.opengl.model.obj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import net.smert.jreactphysics3d.framework.Files;
import net.smert.jreactphysics3d.framework.Fw;
import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.model.ModelReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://paulbourke.net/dataformats/mtl/
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class MaterialReader implements ModelReader {

    private final static Logger log = LoggerFactory.getLogger(MaterialReader.class);

    private final List<Material> materials;
    private final List<String> comments;
    private Material currentMaterial;
    private String materialName;

    public MaterialReader() {
        materials = new ArrayList<>();
        comments = new ArrayList<>();
        reset();
    }

    private void addComment(StringTokenizer tokenizer) {
        String comment = getRemainingTokens(tokenizer);
        if (comment.length() > 0) {
            comments.add(comment);
        }
    }

    private Color createColor(StringTokenizer tokenizer) {
        int index = 0;
        Color color = new Color();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            switch (token) {
                case "spectral":
                    log.warn("Skipping color. Don't support spectral definitions.");
                    return null;

                case "xyz":
                    log.warn("Skipping color. Don't support xyz definitions.");
                    return null;
            }
            color.set(index++, Float.parseFloat(token));
        }
        assert (index == 3);
        return color;
    }

    private void createNewMaterial(StringTokenizer tokenizer) {
        materialName = getNextTokenOnly(tokenizer);
        if (currentMaterial != null) {
            materials.add(currentMaterial);
        }
        currentMaterial = new Material(materialName);
        log.debug("Creating a new material: {}", materialName);
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

                case "d":
                    // Ex: "d factor"
                    setDissolve(tokenizer);
                    break;

                case "illum":
                    // Ex: "illum illum_#"
                    setIlluminationMode(tokenizer);
                    break;

                case "Ka":
                    // Ex: "Ka 0.000000 0.000000 0.000000"
                    if (totalTokens == 4) {
                        setAmbient(tokenizer);
                    } else {
                        log.warn("Invalid ambient definition: {}", line);
                    }
                    break;

                case "Kd":
                    // Ex: "Kd 0.8 0.8 0.8"
                    if (totalTokens == 4) {
                        setDiffuse(tokenizer);
                    } else {
                        log.warn("Invalid diffuse definition: {}", line);
                    }
                    break;

                case "Ks":
                    // Ex: "Ks 0.8 0.8 0.8"
                    if (totalTokens == 4) {
                        setSpecular(tokenizer);
                    } else {
                        log.warn("Invalid specular definition: {}", line);
                    }
                    break;

                case "map_Ka":
                    if (totalTokens == 2) {
                        setAmbientMap(tokenizer);
                    } else {
                        log.warn("Don't support optional arguments. Skipping ambient map definition: {}", line);
                    }
                    break;

                case "map_Kd":
                    if (totalTokens == 2) {
                        setDiffuseMap(tokenizer);
                    } else {
                        log.warn("Don't support optional arguments. Skipping diffuse map definition: {}", line);
                    }
                    break;

                case "map_Ks":
                    if (totalTokens == 2) {
                        setSpecularMap(tokenizer);
                    } else {
                        log.warn("Don't support optional arguments. Skipping specular map definition: {}", line);
                    }
                    break;

                case "map_Ns":
                    if (totalTokens == 2) {
                        setSpecularExponentMap(tokenizer);
                    } else {
                        log.warn("Don't support optional arguments. Skipping specular exponent map definition: {}", line);
                    }
                    break;

                case "newmtl":
                    // Ex: "newmtl my_mtl"
                    createNewMaterial(tokenizer);
                    break;

                case "Ni":
                    // Ex: "Ni optical_density"
                    setOpticalDensity(tokenizer);
                    break;

                case "Ns":
                    // Ex: "Ns exponent"
                    setSpecularExponent(tokenizer);
                    break;

                default:
                    log.warn("Skipped line with an unsupported token: " + line);
            }
        }
    }

    private void read(String filename) throws IOException {
        Files.FileAsset fileAsset = Fw.files.getMesh(filename);

        InputStream is = fileAsset.openStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(isr);
        String line;

        while ((line = reader.readLine()) != null) {
            parse(line);
        }

        reader.close();
        isr.close();
        is.close();
    }

    private void setAmbient(StringTokenizer tokenizer) {
        if (currentMaterial != null) {
            Color color = createColor(tokenizer);
            if (color != null) {
                currentMaterial.setAmbient(color);
            }
        } else {
            log.warn("Current material was NULL. Skipping ambient.");
        }
    }

    private void setAmbientMap(StringTokenizer tokenizer) {
        if (currentMaterial != null) {
            String ambientMapFilename = getNextTokenOnly(tokenizer);
            currentMaterial.setAmbientMapFilename(ambientMapFilename);
        } else {
            log.warn("Current material was NULL. Skipping ambient map.");
        }
    }

    private void setDiffuse(StringTokenizer tokenizer) {
        if (currentMaterial != null) {
            Color color = createColor(tokenizer);
            if (color != null) {
                currentMaterial.setDiffuse(color);
            }
        } else {
            log.warn("Current material was NULL. Skipping diffuse.");
        }
    }

    private void setDiffuseMap(StringTokenizer tokenizer) {
        if (currentMaterial != null) {
            String diffuseMapFilename = getNextTokenOnly(tokenizer);
            currentMaterial.setDiffuseMapFilename(diffuseMapFilename);
        } else {
            log.warn("Current material was NULL. Skipping diffuse map.");
        }
    }

    private void setDissolve(StringTokenizer tokenizer) {
        if (currentMaterial != null) {
            String dissolve = getNextTokenOnly(tokenizer);
            currentMaterial.setDissolve(dissolve);
        } else {
            log.warn("Current material was NULL. Skipping dissolve.");
        }
    }

    private void setIlluminationMode(StringTokenizer tokenizer) {
        if (currentMaterial != null) {
            String illuminationMode = getNextTokenOnly(tokenizer);
            currentMaterial.setIlluminationMode(illuminationMode);
        } else {
            log.warn("Current material was NULL. Skipping illumination mode.");
        }
    }

    private void setOpticalDensity(StringTokenizer tokenizer) {
        if (currentMaterial != null) {
            String opticalDensity = getNextTokenOnly(tokenizer);
            currentMaterial.setOpticalDensity(opticalDensity);
        } else {
            log.warn("Current material was NULL. Skipping optical density.");
        }
    }

    private void setSpecular(StringTokenizer tokenizer) {
        if (currentMaterial != null) {
            Color color = createColor(tokenizer);
            if (color != null) {
                currentMaterial.setSpecular(color);
            }
        } else {
            log.warn("Current material was NULL. Skipping specular.");
        }
    }

    private void setSpecularExponent(StringTokenizer tokenizer) {
        if (currentMaterial != null) {
            String exponent = getNextTokenOnly(tokenizer);
            currentMaterial.setSpecularExponent(exponent);
        } else {
            log.warn("Current material was NULL. Skipping specular exponent.");
        }
    }

    private void setSpecularExponentMap(StringTokenizer tokenizer) {
        if (currentMaterial != null) {
            String specularExponentMapFilename = getNextTokenOnly(tokenizer);
            currentMaterial.setSpecularExponentMapFilename(specularExponentMapFilename);
        } else {
            log.warn("Current material was NULL. Skipping specular exponent map.");
        }
    }

    private void setSpecularMap(StringTokenizer tokenizer) {
        if (currentMaterial != null) {
            String specularMapFilename = getNextTokenOnly(tokenizer);
            currentMaterial.setSpecularMapFilename(specularMapFilename);
        } else {
            log.warn("Current material was NULL. Skipping specular map.");
        }
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public List<String> getComments() {
        return comments;
    }

    public String getMaterialName() {
        return materialName;
    }

    public final void reset() {
        materials.clear();
        comments.clear();
        currentMaterial = null;
        materialName = "";
    }

    @Override
    public void load(String filename, Mesh mesh) throws IOException {
        log.info("Loading MTL definition: {}", filename);
        reset();
        read(filename);

        // The last material is not added until here
        if (currentMaterial != null) {
            materials.add(currentMaterial);
        }
    }

    public static class Color {

        private float r;
        private float g;
        private float b;

        private Color() {
            r = 0;
            g = 0;
            b = 0;
        }

        public void set(int index, float value) {
            if (index == 0) {
                r = value;
            } else if (index == 1) {
                g = value;
            } else if (index == 2) {
                b = value;
            } else {
                throw new IllegalArgumentException("Unknown index: " + index);
            }
        }

        public void set(Color color) {
            r = color.r;
            g = color.g;
            b = color.b;
        }

    }

    public static class Material {

        private float dissolve;
        private float opticalDensity;
        private float specularExponent;
        private int illuminationMode;
        private final Color ambient;
        private final Color diffuse;
        private final Color specular;
        private String ambientMapFilename;
        private String diffuseMapFilename;
        private final String materialName;
        private String specularMapFilename;
        private String specularExponentMapFilename;

        private Material(String materialName) {
            dissolve = 1.0f;
            opticalDensity = 1.0f;
            specularExponent = 0;
            illuminationMode = 0;
            ambient = new Color();
            diffuse = new Color();
            specular = new Color();
            ambientMapFilename = "";
            diffuseMapFilename = "";
            this.materialName = materialName;
            specularMapFilename = "";
            specularExponentMapFilename = "";
        }

        public float getDissolve() {
            return dissolve;
        }

        public void setDissolve(String dissolve) {
            this.dissolve = Float.parseFloat(dissolve);
        }

        public float getOpticalDensity() {
            return opticalDensity;
        }

        public void setOpticalDensity(String opticalDensity) {
            this.opticalDensity = Float.parseFloat(opticalDensity);
        }

        public float getSpecularExponent() {
            return specularExponent;
        }

        public void setSpecularExponent(String specularExponent) {
            this.specularExponent = Float.parseFloat(specularExponent);
        }

        public int getIlluminationMode() {
            return illuminationMode;
        }

        public void setIlluminationMode(String illuminationMode) {
            this.illuminationMode = Integer.parseInt(illuminationMode);
        }

        public Color getAmbient() {
            return ambient;
        }

        public void setAmbient(Color ambient) {
            this.ambient.set(ambient);
        }

        public Color getDiffuse() {
            return diffuse;
        }

        public void setDiffuse(Color diffuse) {
            this.diffuse.set(diffuse);
        }

        public Color getSpecular() {
            return specular;
        }

        public void setSpecular(Color specular) {
            this.specular.set(specular);
        }

        public String getAmbientMapFilename() {
            return ambientMapFilename;
        }

        public void setAmbientMapFilename(String ambientMapFilename) {
            this.ambientMapFilename = ambientMapFilename;
        }

        public String getDiffuseMapFilename() {
            return diffuseMapFilename;
        }

        public void setDiffuseMapFilename(String diffuseMapFilename) {
            this.diffuseMapFilename = diffuseMapFilename;
        }

        public String getMaterialName() {
            return materialName;
        }

        public String getSpecularMapFilename() {
            return specularMapFilename;
        }

        public void setSpecularMapFilename(String specularMapFilename) {
            this.specularMapFilename = specularMapFilename;
        }

        public String getSpecularExponentMapFilename() {
            return specularExponentMapFilename;
        }

        public void setSpecularExponentMapFilename(String specularExponentMapFilename) {
            this.specularExponentMapFilename = specularExponentMapFilename;
        }

    }

}
