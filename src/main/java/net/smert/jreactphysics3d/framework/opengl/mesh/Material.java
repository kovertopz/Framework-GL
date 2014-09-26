package net.smert.jreactphysics3d.framework.opengl.mesh;

import net.smert.jreactphysics3d.framework.math.Vector4f;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Material {

    private boolean isOpaque;
    private int shininess;
    private final Vector4f ambient;
    private final Vector4f diffuse;
    private final Vector4f emissive;
    private final Vector4f specular;

    public Material() {

        // Same as glMaterial defaults
        isOpaque = true;
        shininess = 0;
        ambient = new Vector4f(0.2f, 0.2f, 0.2f, 1.0f);
        diffuse = new Vector4f(0.8f, 0.8f, 0.8f, 1.0f);
        emissive = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
        specular = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public boolean isIsOpaque() {
        return isOpaque;
    }

    public void setIsOpaque(boolean isOpaque) {
        this.isOpaque = isOpaque;
    }

    public int getShininess() {
        return shininess;
    }

    public void setShininess(int shininess) {
        this.shininess = shininess;
    }

    public Vector4f getAmbient() {
        return ambient;
    }

    public void setAmbient(Vector4f ambient) {
        this.ambient.set(ambient);
    }

    public Vector4f getDiffuse() {
        return diffuse;
    }

    public void setDiffuse(Vector4f diffuse) {
        this.diffuse.set(diffuse);
    }

    public Vector4f getEmissive() {
        return emissive;
    }

    public void setEmissive(Vector4f emissive) {
        this.emissive.set(emissive);
    }

    public Vector4f getSpecular() {
        return specular;
    }

    public void setSpecular(Vector4f specular) {
        this.specular.set(specular);
    }

}
