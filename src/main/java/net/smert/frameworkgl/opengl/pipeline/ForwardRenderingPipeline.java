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
package net.smert.frameworkgl.opengl.pipeline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.gameobjects.AABBGameObject;
import net.smert.frameworkgl.gameobjects.GameObject;
import net.smert.frameworkgl.gameobjects.SimpleOrientationAxisGameObject;
import net.smert.frameworkgl.gameobjects.SkyboxGameObject;
import net.smert.frameworkgl.gameobjects.ViewFrustumGameObject;
import net.smert.frameworkgl.math.AABB;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.renderer.GuiRenderer;
import net.smert.frameworkgl.opengl.shader.AbstractShader;
import net.smert.frameworkgl.opengl.shader.basic.DiffuseTextureShader;
import net.smert.frameworkgl.opengl.shader.basic.SkyboxShader;
import net.smert.frameworkgl.utils.Color;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class ForwardRenderingPipeline extends AbstractRenderingPipeline {

    private boolean renderAabbs;
    private boolean renderDebugRenderCallbacks;
    private boolean renderViewFrustum;
    private boolean renderSimpleOrientationAxis;
    private AABBGameObject aabbGameObject;
    private AbstractShader currentDefaultShader;
    private AbstractShader defaultShader;
    private AbstractShader defaultShaderWithShadows;
    private final Color skyboxColor;
    private final Config config;
    private DiffuseTextureShader diffuseTextureShader;
    private GuiRenderer guiRenderer;
    private List<GameObject> entityGameObjects;
    private final List<GameObject> entityGameObjectsToRender;
    private List<GameObject> nonOpaqueGameObjects;
    private final List<GameObject> nonOpaqueGameObjectsToRender;
    private List<GameObject> worldGameObjects;
    private final List<GameObject> worldGameObjectsToRender;
    private SimpleOrientationAxisGameObject simpleOrientationAxisGameObject;
    private SkyboxGameObject skyboxGameObject;
    private SkyboxShader skyboxShader;
    private ViewFrustumGameObject viewFrustumGameObject;

    public ForwardRenderingPipeline() {
        skyboxColor = new Color();
        config = new Config();
        entityGameObjects = new ArrayList<>();
        entityGameObjectsToRender = new ArrayList<>();
        nonOpaqueGameObjects = new ArrayList<>();
        nonOpaqueGameObjectsToRender = new ArrayList<>();
        worldGameObjects = new ArrayList<>();
        worldGameObjectsToRender = new ArrayList<>();
        reset();
    }

    protected void renderAabbs(List<GameObject> gameObjects) {
        for (GameObject gameObject : gameObjects) {
            AABB worldAabb = gameObject.getWorldAabb();
            // Updating AABBs this way is costly
            aabbGameObject.update(worldAabb);
            // AABB is already in world coordinates so we don't translate
            Fw.graphics.render(aabbGameObject.getRenderable(), 0f, 0f, 0f);
        }
    }

    protected void renderSimpleOrientationAxis(List<GameObject> gameObjects) {
        for (GameObject gameObject : gameObjects) {
            simpleOrientationAxisGameObject.setWorldTransform(gameObject.getWorldTransform());
            Fw.graphics.render(simpleOrientationAxisGameObject);
        }
    }

    protected void updateGameObjectsToRender(List<GameObject> gameObjectsToRender, List<GameObject> gameObjects) {
        gameObjectsToRender.clear();
        for (GameObject gameObject : gameObjects) {
            if (gameObject.getRenderableState().isInFrustum()) {
                gameObjectsToRender.add(gameObject);
            }
        }
    }

    public void addAllGameObjectsToRender() {
        worldGameObjectsToRender.clear();
        entityGameObjectsToRender.clear();
        nonOpaqueGameObjectsToRender.clear();
        worldGameObjectsToRender.addAll(worldGameObjects);
        entityGameObjectsToRender.addAll(entityGameObjects);
        nonOpaqueGameObjectsToRender.addAll(nonOpaqueGameObjects);
    }

    public Config getConfig() {
        return config;
    }

    public void performFrustumCulling() {
        camera.updatePlanes();
        Fw.graphics.updateAabb(worldGameObjects);
        Fw.graphics.updateAabb(entityGameObjects);
        Fw.graphics.updateAabb(nonOpaqueGameObjects);
        Fw.graphics.performCulling(camera, worldGameObjects);
        Fw.graphics.performCulling(camera, entityGameObjects);
        Fw.graphics.performCulling(camera, nonOpaqueGameObjects);
        updateGameObjectsToRender(worldGameObjectsToRender, worldGameObjects);
        updateGameObjectsToRender(entityGameObjectsToRender, entityGameObjects);
        updateGameObjectsToRender(nonOpaqueGameObjectsToRender, nonOpaqueGameObjects);
    }

    public void updateAabbs() {
        Fw.graphics.updateAabb(worldGameObjects);
        Fw.graphics.updateAabb(entityGameObjects);
        Fw.graphics.updateAabb(nonOpaqueGameObjects);
    }

    public void updateCurrentShader() {
        if (shadowsEnabled) {
            currentDefaultShader = defaultShaderWithShadows;
        } else {
            currentDefaultShader = defaultShader;
        }
    }

    public void updateViewFrustumGameObjectWithCamera() {
        viewFrustumGameObject.getWorldTransform().getRotation().set(camera.getRotationMatrix());
        viewFrustumGameObject.setWorldPosition(camera.getPosition());
        viewFrustumGameObject.update(camera.getAspectRatio(), camera.getFieldOfView(), camera.getZNear(),
                camera.getZFar());
        Fw.graphics.updateAabb(viewFrustumGameObject);
    }

    @Override
    public void destroy() {
        aabbGameObject.destroy();
        simpleOrientationAxisGameObject.destroy();
        viewFrustumGameObject.destroy();
        diffuseTextureShader.destroy();
        skyboxShader.destroy();
    }

    @Override
    public void init() throws IOException {

        // AABB game object
        aabbGameObject = new AABBGameObject();
        aabbGameObject.getColor0().set("yellow");
        aabbGameObject.init(new AABB()); // Empty AABB

        // Simple axis game object
        simpleOrientationAxisGameObject = new SimpleOrientationAxisGameObject();
        simpleOrientationAxisGameObject.getColor0().set("red");
        simpleOrientationAxisGameObject.getColor1().set("green");
        simpleOrientationAxisGameObject.getColor2().set("blue");
        simpleOrientationAxisGameObject.init();

        // View frustum game object
        viewFrustumGameObject = new ViewFrustumGameObject();
        viewFrustumGameObject.getColor0().set("black");
        viewFrustumGameObject.getColor1().set("yellow");
        viewFrustumGameObject.getColor2().set("yellow");
        viewFrustumGameObject.getColor3().set("white");
        viewFrustumGameObject.getColor3().setA(.4f);
        viewFrustumGameObject.init(camera.getAspectRatio(), camera.getFieldOfView(), camera.getZNear(),
                camera.getZFar());

        // Build shaders
        diffuseTextureShader = DiffuseTextureShader.Factory.Create();
        diffuseTextureShader.init();
        skyboxShader = SkyboxShader.Factory.Create();
        skyboxShader.init();

        updateCurrentShader();
    }

    @Override
    public void render() {

        // Reset state and clear
        GL.o1.disableBlending();
        GL.o1.enableColorMask();
        GL.o1.enableCulling();
        GL.o1.enableDepthMask();
        GL.o1.enableDepthTest();
        GL.o1.setDepthFuncLess();
        GL.o1.setPolygonModeFrontAndBackFill();
        GL.o1.clear();

        // Update camera
        Fw.graphics.setCamera(camera);

        // Frustum culling
        if (frustumCulling) {
            performFrustumCulling();
        }

        // Render 3D
        Fw.graphics.switchShader(skyboxShader);
        Fw.graphics.color(skyboxColor.getR(), skyboxColor.getG(), skyboxColor.getB(), skyboxColor.getA());
        skyboxGameObject.getWorldTransform().setPosition(camera.getPosition());
        GL.o1.disableCulling();
        GL.o1.disableDepthTest();
        Fw.graphics.render(skyboxGameObject);
        GL.o1.enableDepthTest();
        GL.o1.enableCulling();
        Fw.graphics.unbindShader();

        if (shadowsEnabled) {

        }

        switchPolygonFillMode();
        Fw.graphics.switchShader(currentDefaultShader);
        Fw.graphics.render(worldGameObjectsToRender);
        Fw.graphics.render(entityGameObjectsToRender);
        Fw.graphics.renderBlend(nonOpaqueGameObjectsToRender);
        Fw.graphics.unbindShader();

        if (debug) {
            Fw.graphics.switchShader(diffuseTextureShader); // No lighting

            // View frustum
            if (renderViewFrustum) {
                Fw.graphics.renderBlend(viewFrustumGameObject);
            }

            // AABBs
            if (renderAabbs) {
                renderAabbs(worldGameObjectsToRender);
                renderAabbs(entityGameObjectsToRender);
                renderAabbs(nonOpaqueGameObjectsToRender);
            }

            // Orientation axis
            if (renderSimpleOrientationAxis) {
                GL.o1.disableDepthTest();
                renderSimpleOrientationAxis(worldGameObjectsToRender);
                renderSimpleOrientationAxis(entityGameObjectsToRender);
                renderSimpleOrientationAxis(nonOpaqueGameObjectsToRender);
                GL.o1.enableDepthTest();
            }

            // Debug render callbacks
            if (renderDebugRenderCallbacks) {
                for (DebugRenderCallback callback : debugRenderCallbacks) {
                    callback.render();
                }
            }

            Fw.graphics.unbindShader();
        }

        // Render 2D
        GL.o1.setBlendingFunctionSrcAlphaAndOneMinusSrcAlpha();
        GL.o1.enableBlending();
        GL.o1.disableDepthTest();
        GL.o1.setPolygonModeFrontAndBackFill();
        Fw.graphics.switchShader(diffuseTextureShader);
        Fw.graphics.set2DMode();
        guiRenderer.render();
        Fw.graphics.unbindShader();
        GL.o1.enableDepthTest();
        GL.o1.disableBlending();
    }

    @Override
    public final void reset() {
        super.reset();
        renderAabbs = false;
        renderSimpleOrientationAxis = false;
        renderViewFrustum = false;
        shadowsEnabled = false;
        wireframe = false;
        skyboxColor.setWhite();
    }

    public class Config extends AbstractRenderingPipeline.Config {

        public AABBGameObject getAabbGameObject() {
            return aabbGameObject;
        }

        public void setAabbGameObject(AABBGameObject aabbGameObject) {
            ForwardRenderingPipeline.this.aabbGameObject = aabbGameObject;
        }

        public AbstractShader getDefaultShader() {
            return defaultShader;
        }

        public void setDefaultShader(AbstractShader defaultShader) {
            ForwardRenderingPipeline.this.defaultShader = defaultShader;
        }

        public AbstractShader getDefaultShaderWithShadows() {
            return defaultShaderWithShadows;
        }

        public void setDefaultShaderWithShadows(AbstractShader defaultShaderWithShadows) {
            ForwardRenderingPipeline.this.defaultShaderWithShadows = defaultShaderWithShadows;
        }

        public Color getSkyboxColor() {
            return skyboxColor;
        }

        public GuiRenderer getGuiRenderer() {
            return guiRenderer;
        }

        public void setGuiRenderer(GuiRenderer guiRenderer) {
            ForwardRenderingPipeline.this.guiRenderer = guiRenderer;
        }

        public List<GameObject> getEntityGameObjects() {
            return entityGameObjects;
        }

        public void setEntityGameObjects(List<GameObject> entityGameObjects) {
            ForwardRenderingPipeline.this.entityGameObjects = entityGameObjects;
        }

        public List<GameObject> getNonOpaqueGameObjects() {
            return nonOpaqueGameObjects;
        }

        public void setNonOpaqueGameObjects(List<GameObject> nonOpaqueGameObjects) {
            ForwardRenderingPipeline.this.nonOpaqueGameObjects = nonOpaqueGameObjects;
        }

        public List<GameObject> getWorldGameObjects() {
            return worldGameObjects;
        }

        public void setWorldGameObjects(List<GameObject> worldGameObjects) {
            ForwardRenderingPipeline.this.worldGameObjects = worldGameObjects;
        }

        public SimpleOrientationAxisGameObject getSimpleOrientationAxisGameObject() {
            return simpleOrientationAxisGameObject;
        }

        public void setSimpleOrientationAxisGameObject(SimpleOrientationAxisGameObject simpleOrientationAxisGameObject) {
            ForwardRenderingPipeline.this.simpleOrientationAxisGameObject = simpleOrientationAxisGameObject;
        }

        public SkyboxGameObject getSkyboxGameObject() {
            return skyboxGameObject;
        }

        public void setSkyboxGameObject(SkyboxGameObject skyboxGameObject) {
            ForwardRenderingPipeline.this.skyboxGameObject = skyboxGameObject;
        }

        public ViewFrustumGameObject getViewFrustumGameObject() {
            return viewFrustumGameObject;
        }

        public void setViewFrustumGameObject(ViewFrustumGameObject viewFrustumGameObject) {
            ForwardRenderingPipeline.this.viewFrustumGameObject = viewFrustumGameObject;
        }

        public boolean isRenderAabbs() {
            return renderAabbs;
        }

        public void setRenderAabbs(boolean renderAabbs) {
            ForwardRenderingPipeline.this.renderAabbs = renderAabbs;
        }

        public boolean isRenderViewFrustum() {
            return renderViewFrustum;
        }

        public void setRenderViewFrustum(boolean renderViewFrustum) {
            ForwardRenderingPipeline.this.renderViewFrustum = renderViewFrustum;
        }

        public boolean isRenderSimpleOrientationAxis() {
            return renderSimpleOrientationAxis;
        }

        public void setRenderSimpleOrientationAxis(boolean renderSimpleOrientationAxis) {
            ForwardRenderingPipeline.this.renderSimpleOrientationAxis = renderSimpleOrientationAxis;
        }

        @Override
        public void setShadowsEnabled(boolean shadowsEnabled) {
            super.setShadowsEnabled(shadowsEnabled);
            updateCurrentShader();
        }

    }

}
