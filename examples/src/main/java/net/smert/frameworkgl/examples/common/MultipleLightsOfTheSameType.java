package net.smert.frameworkgl.examples.common;

import java.util.ArrayList;
import java.util.List;
import net.smert.frameworkgl.math.Vector4f;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.GLLight;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class MultipleLightsOfTheSameType {

    private final List<GLLight> directionalLights;
    private final List<GLLight> pointLights;
    private final List<GLLight> spotLights;

    public MultipleLightsOfTheSameType() {
        directionalLights = new ArrayList<>();
        pointLights = new ArrayList<>();
        spotLights = new ArrayList<>();
    }

    private void createDirectionalLights() {
        GLLight directionalLight;

        directionalLight = GL.glFactory.createGLLight();
        directionalLight.setDiffuse(new Vector4f(1f, 0f, 0f, 1f));
        directionalLight.setSpecular(new Vector4f(1f, 0f, 0f, 1f));
        directionalLight.setPosition(new Vector4f(0f, 15f, 15f, 0f));
        directionalLight.setRadius(32f);
        directionalLights.add(directionalLight);

        directionalLight = GL.glFactory.createGLLight();
        directionalLight.setDiffuse(new Vector4f(0f, 1f, 0f, 1f));
        directionalLight.setSpecular(new Vector4f(0f, 1f, 0f, 1f));
        directionalLight.setPosition(new Vector4f(-15f, 15f, 0f, 0f));
        directionalLight.setRadius(32f);
        directionalLights.add(directionalLight);

        directionalLight = GL.glFactory.createGLLight();
        directionalLight.setDiffuse(new Vector4f(0f, 0f, 1f, 1f));
        directionalLight.setSpecular(new Vector4f(0f, 0f, 1f, 1f));
        directionalLight.setPosition(new Vector4f(0f, 15f, -15f, 0f));
        directionalLight.setRadius(32f);
        directionalLights.add(directionalLight);

        directionalLight = GL.glFactory.createGLLight();
        directionalLight.setDiffuse(new Vector4f(1f, 1f, 0f, 1f));
        directionalLight.setSpecular(new Vector4f(1f, 1f, 0f, 1f));
        directionalLight.setPosition(new Vector4f(15f, 15f, 0f, 0f));
        directionalLight.setRadius(32f);
        directionalLights.add(directionalLight);

        directionalLight = GL.glFactory.createGLLight();
        directionalLight.setDiffuse(new Vector4f(0f, 1f, 1f, 1f));
        directionalLight.setSpecular(new Vector4f(0f, 1f, 1f, 1f));
        directionalLight.setPosition(new Vector4f(15f, 15f, 15f, 0f));
        directionalLight.setRadius(32f);
        directionalLights.add(directionalLight);

        directionalLight = GL.glFactory.createGLLight();
        directionalLight.setDiffuse(new Vector4f(1f, 0f, 1f, 1f));
        directionalLight.setSpecular(new Vector4f(1f, 0f, 1f, 1f));
        directionalLight.setPosition(new Vector4f(-15f, 15f, 15f, 0f));
        directionalLight.setRadius(32f);
        directionalLights.add(directionalLight);

        directionalLight = GL.glFactory.createGLLight();
        directionalLight.setDiffuse(new Vector4f(.5f, .5f, 0f, 1f));
        directionalLight.setSpecular(new Vector4f(.5f, .5f, 0f, 1f));
        directionalLight.setPosition(new Vector4f(-15f, 15f, -15f, 0f));
        directionalLight.setRadius(32f);
        directionalLights.add(directionalLight);

        directionalLight = GL.glFactory.createGLLight();
        directionalLight.setDiffuse(new Vector4f(0f, .5f, .5f, 1f));
        directionalLight.setSpecular(new Vector4f(0f, .5f, .5f, 1f));
        directionalLight.setPosition(new Vector4f(15f, 15f, -15f, 0f));
        directionalLight.setRadius(32f);
        directionalLights.add(directionalLight);
    }

    private void createPointLights() {
        GLLight pointLight;

        pointLight = GL.glFactory.createGLLight();
        pointLight.setDiffuse(new Vector4f(1f, 0f, 0f, 1f));
        pointLight.setSpecular(new Vector4f(1f, 0f, 0f, 1f));
        pointLight.setPosition(new Vector4f(0f, 15f, 15f, 1f));
        pointLight.setRadius(32f);
        pointLights.add(pointLight);

        pointLight = GL.glFactory.createGLLight();
        pointLight.setDiffuse(new Vector4f(0f, 1f, 0f, 1f));
        pointLight.setSpecular(new Vector4f(0f, 1f, 0f, 1f));
        pointLight.setPosition(new Vector4f(-15f, 15f, 0f, 1f));
        pointLight.setRadius(32f);
        pointLights.add(pointLight);

        pointLight = GL.glFactory.createGLLight();
        pointLight.setDiffuse(new Vector4f(0f, 0f, 1f, 1f));
        pointLight.setSpecular(new Vector4f(0f, 0f, 1f, 1f));
        pointLight.setPosition(new Vector4f(0f, 15f, -15f, 1f));
        pointLight.setRadius(32f);
        pointLights.add(pointLight);

        pointLight = GL.glFactory.createGLLight();
        pointLight.setDiffuse(new Vector4f(1f, 1f, 0f, 1f));
        pointLight.setSpecular(new Vector4f(1f, 1f, 0f, 1f));
        pointLight.setPosition(new Vector4f(15f, 15f, 0f, 1f));
        pointLight.setRadius(32f);
        pointLights.add(pointLight);

        pointLight = GL.glFactory.createGLLight();
        pointLight.setDiffuse(new Vector4f(0f, 1f, 1f, 1f));
        pointLight.setSpecular(new Vector4f(0f, 1f, 1f, 1f));
        pointLight.setPosition(new Vector4f(15f, 15f, 15f, 1f));
        pointLight.setRadius(32f);
        pointLights.add(pointLight);

        pointLight = GL.glFactory.createGLLight();
        pointLight.setDiffuse(new Vector4f(1f, 0f, 1f, 1f));
        pointLight.setSpecular(new Vector4f(1f, 0f, 1f, 1f));
        pointLight.setPosition(new Vector4f(-15f, 15f, 15f, 1f));
        pointLight.setRadius(32f);
        pointLights.add(pointLight);

        pointLight = GL.glFactory.createGLLight();
        pointLight.setDiffuse(new Vector4f(.5f, .5f, 0f, 1f));
        pointLight.setSpecular(new Vector4f(.5f, .5f, 0f, 1f));
        pointLight.setPosition(new Vector4f(-15f, 15f, -15f, 1f));
        pointLight.setRadius(32f);
        pointLights.add(pointLight);

        pointLight = GL.glFactory.createGLLight();
        pointLight.setDiffuse(new Vector4f(0f, .5f, .5f, 1f));
        pointLight.setSpecular(new Vector4f(0f, .5f, .5f, 1f));
        pointLight.setPosition(new Vector4f(15f, 15f, -15f, 1f));
        pointLight.setRadius(32f);
        pointLights.add(pointLight);
    }

    private void createSpotLights() {
        float spotInnerCutoff = 180f;
        float spotOuterCutoff = 180f;
        GLLight spotLight;

        spotLight = GL.glFactory.createGLLight();
        spotLight.setDiffuse(new Vector4f(1f, 0f, 0f, 1f));
        spotLight.setSpecular(new Vector4f(1f, 0f, 0f, 1f));
        spotLight.setPosition(new Vector4f(0f, 15f, 15f, 1f));
        spotLight.setRadius(32f);
        spotLight.setSpotInnerCutoff(spotInnerCutoff);
        spotLight.setSpotOuterCutoff(spotOuterCutoff);
        spotLight.setSpotDirection(new Vector4f(0f, -15f, -15f, 1f));
        spotLights.add(spotLight);

        spotLight = GL.glFactory.createGLLight();
        spotLight.setDiffuse(new Vector4f(0f, 1f, 0f, 1f));
        spotLight.setSpecular(new Vector4f(0f, 1f, 0f, 1f));
        spotLight.setPosition(new Vector4f(-15f, 15f, 0f, 1f));
        spotLight.setRadius(32f);
        spotLight.setSpotInnerCutoff(spotInnerCutoff);
        spotLight.setSpotOuterCutoff(spotOuterCutoff);
        spotLight.setSpotDirection(new Vector4f(15f, -15f, 0f, 1f));
        spotLights.add(spotLight);

        spotLight = GL.glFactory.createGLLight();
        spotLight.setDiffuse(new Vector4f(0f, 0f, 1f, 1f));
        spotLight.setSpecular(new Vector4f(0f, 0f, 1f, 1f));
        spotLight.setPosition(new Vector4f(0f, 15f, -15f, 1f));
        spotLight.setRadius(32f);
        spotLight.setSpotInnerCutoff(spotInnerCutoff);
        spotLight.setSpotOuterCutoff(spotOuterCutoff);
        spotLight.setSpotDirection(new Vector4f(0f, -15f, 15f, 1f));
        spotLights.add(spotLight);

        spotLight = GL.glFactory.createGLLight();
        spotLight.setDiffuse(new Vector4f(1f, 1f, 0f, 1f));
        spotLight.setSpecular(new Vector4f(1f, 1f, 0f, 1f));
        spotLight.setPosition(new Vector4f(15f, 15f, 0f, 1f));
        spotLight.setRadius(32f);
        spotLight.setSpotInnerCutoff(spotInnerCutoff);
        spotLight.setSpotOuterCutoff(spotOuterCutoff);
        spotLight.setSpotDirection(new Vector4f(-15f, -15f, 0f, 1f));
        spotLights.add(spotLight);

        spotLight = GL.glFactory.createGLLight();
        spotLight.setDiffuse(new Vector4f(0f, 1f, 1f, 1f));
        spotLight.setSpecular(new Vector4f(0f, 1f, 1f, 1f));
        spotLight.setPosition(new Vector4f(15f, 15f, 15f, 1f));
        spotLight.setRadius(32f);
        spotLight.setSpotInnerCutoff(spotInnerCutoff);
        spotLight.setSpotOuterCutoff(spotOuterCutoff);
        spotLight.setSpotDirection(new Vector4f(-15f, -15f, -15f, 1f));
        spotLights.add(spotLight);

        spotLight = GL.glFactory.createGLLight();
        spotLight.setDiffuse(new Vector4f(1f, 0f, 1f, 1f));
        spotLight.setSpecular(new Vector4f(1f, 0f, 1f, 1f));
        spotLight.setPosition(new Vector4f(-15f, 15f, 15f, 1f));
        spotLight.setRadius(32f);
        spotLight.setSpotInnerCutoff(spotInnerCutoff);
        spotLight.setSpotOuterCutoff(spotOuterCutoff);
        spotLight.setSpotDirection(new Vector4f(15f, -15f, -15f, 1f));
        spotLights.add(spotLight);

        spotLight = GL.glFactory.createGLLight();
        spotLight.setDiffuse(new Vector4f(.5f, .5f, 0f, 1f));
        spotLight.setSpecular(new Vector4f(.5f, .5f, 0f, 1f));
        spotLight.setPosition(new Vector4f(-15f, 15f, -15f, 1f));
        spotLight.setRadius(32f);
        spotLight.setSpotInnerCutoff(spotInnerCutoff);
        spotLight.setSpotOuterCutoff(spotOuterCutoff);
        spotLight.setSpotDirection(new Vector4f(15f, -15f, 15f, 1f));
        spotLights.add(spotLight);

        spotLight = GL.glFactory.createGLLight();
        spotLight.setDiffuse(new Vector4f(0f, .5f, .5f, 1f));
        spotLight.setSpecular(new Vector4f(0f, .5f, .5f, 1f));
        spotLight.setPosition(new Vector4f(15f, 15f, -15f, 1f));
        spotLight.setRadius(32f);
        spotLight.setSpotInnerCutoff(spotInnerCutoff);
        spotLight.setSpotOuterCutoff(spotOuterCutoff);
        spotLight.setSpotDirection(new Vector4f(-15f, -15f, 15f, 1f));
        spotLights.add(spotLight);
    }

    public List<GLLight> getDirectionalLights() {
        return directionalLights;
    }

    public List<GLLight> getPointLights() {
        return pointLights;
    }

    public List<GLLight> getSpotLights() {
        return spotLights;
    }

    public void init() {
        createDirectionalLights();
        createPointLights();
        createSpotLights();
    }

}
