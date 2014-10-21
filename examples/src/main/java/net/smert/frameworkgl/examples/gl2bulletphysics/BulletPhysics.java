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
package net.smert.frameworkgl.examples.gl2bulletphysics;

import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.Screen;
import net.smert.frameworkgl.examples.common.BulletGameObject;
import net.smert.frameworkgl.examples.common.BulletGameObjectFactory;
import net.smert.frameworkgl.examples.common.BulletWrapper;
import net.smert.frameworkgl.gameobjects.GameObject;
import net.smert.frameworkgl.helpers.Keyboard;
import net.smert.frameworkgl.math.Matrix3f;
import net.smert.frameworkgl.math.Transform4f;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.math.Vector4f;
import net.smert.frameworkgl.opengl.AmbientLight;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.GLLight;
import net.smert.frameworkgl.opengl.MaterialLight;
import net.smert.frameworkgl.opengl.camera.Camera;
import net.smert.frameworkgl.opengl.camera.CameraController;
import net.smert.frameworkgl.opengl.constants.GetString;
import net.smert.frameworkgl.opengl.shader.vertexlit.single.DiffusePointShader;
import net.smert.frameworkgl.utils.FpsTimer;
import net.smert.frameworkgl.utils.MemoryUsage;
import net.smert.frameworkgl.utils.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class BulletPhysics extends Screen {

    private final static Logger log = LoggerFactory.getLogger(BulletPhysics.class);

    private float spawnTimer;
    private AmbientLight ambientLight;
    private BulletWrapper bulletWrapper;
    private Camera camera;
    private CameraController cameraController;
    private DiffusePointShader vertexLitSingleDiffusePointShader;
    private FpsTimer fpsTimer;
    private GLLight glLight;
    private final List<GameObject> gameObjects;
    private final Map<Integer, String> randomObjects;
    private MaterialLight materialLight;
    private MemoryUsage memoryUsage;

    public BulletPhysics(String[] args) {
        gameObjects = new ArrayList<>();
        randomObjects = new HashMap<>();
    }

    private void handleInput() {
        if (Fw.input.isKeyDown(Keyboard.ESCAPE)) {
            Fw.app.stopRunning();
        }
        cameraController.update();
    }

    @Override
    public void destroy() {
        bulletWrapper.destroy();
        Fw.input.removeInputProcessor(cameraController);
        Fw.input.releaseMouseCursor();
    }

    @Override
    public void init() {

        // Switch renderer and factory to OpenGL 2
        Fw.graphics.switchRenderableFactoryAndRenderer(2);

        // Create timer
        fpsTimer = new FpsTimer();

        // Setup camera and controller
        camera = GL.cameraFactory.createCamera();
        camera.lookAt(new Vector3f(0f, 5f, 20f), new Vector3f(0f, 5f, -1f), Vector3f.WORLD_Y_AXIS);
        camera.setPerspectiveProjection(
                70f,
                (float) Fw.config.getCurrentWidth() / (float) Fw.config.getCurrentHeight(),
                .05f, 128f);
        cameraController = GL.cameraFactory.createCameraController();
        cameraController.setCamera(camera);

        // Memory usage
        memoryUsage = new MemoryUsage();

        // Initialize Bullet
        bulletWrapper = new BulletWrapper();
        bulletWrapper.init();

        // Create ambient light, glLight and material light
        ambientLight = GL.glFactory.createAmbientLight();
        glLight = GL.glFactory.createGLLight();
        glLight.setPosition(new Vector4f(0f, 15f, 10f, 1f));
        glLight.setRadius(256f); // Shader uses this value and OpenGL does not
        materialLight = GL.glFactory.createMaterialLight();

        // Create ground
        BulletGameObject groundGameObject
                = bulletWrapper.createBulletGameObject("ground", new Transform4f(new Matrix3f().identity(), new Vector3f()));
        groundGameObject.getBody().setCollisionFlags(groundGameObject.getBody().getCollisionFlags()
                | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
        groundGameObject.getBody().setActivationState(Collision.DISABLE_DEACTIVATION);
        gameObjects.add(groundGameObject);
        bulletWrapper.addDynamicsWorldRigidBody(groundGameObject);

        // Create a random object pool for all the constructors in the factory except "ground"
        int uniqueID = 0;
        BulletGameObjectFactory factory = bulletWrapper.getFactory();
        Iterator<String> iterator = factory.getNameToIndex().keySet().iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            if (!name.equals("ground")) {
                randomObjects.put(uniqueID++, name);
            }
        }

        // Build shaders
        try {
            vertexLitSingleDiffusePointShader = DiffusePointShader.Factory.Create();
            vertexLitSingleDiffusePointShader.init();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // OpenGL settings
        GL.o1.enableCulling();
        GL.o1.cullBackFaces();
        GL.o1.enableDepthTest();
        GL.o1.setDepthFuncLess();
        GL.o1.enableDepthMask();
        GL.o1.setClearDepth(1f);
        GL.o1.clear();

        log.info("OpenGL version: " + GL.o1.getString(GetString.VERSION));

        // Add camera controller to input
        Fw.input.addInputProcessor(cameraController);
        Fw.input.grabMouseCursor();
    }

    @Override
    public void pause() {
    }

    @Override
    public void render() {
        fpsTimer.update();
        memoryUsage.update();

        if (Fw.timer.isGameTick()) {
            // Do nothing
        }

        if (Fw.timer.isRenderTick()) {
            handleInput();

            // Update dynamics world
            float delta = Fw.timer.getDelta();
            bulletWrapper.stepSimulation(delta);

            // Spawn new object
            if ((spawnTimer -= delta) < 0f) {
                int random = RandomUtils.NextInt(0, randomObjects.size() - 1);
                String randomObject = randomObjects.get(random);
                BulletGameObject gameObject = bulletWrapper.createBulletGameObject(randomObject,
                        new Transform4f(new Matrix3f().identity(), new Vector3f(0f, 25f, 0f)));
                gameObjects.add(gameObject);
                bulletWrapper.addDynamicsWorldRigidBody(gameObject);
                spawnTimer += .5f;
            }

            // Clear screen
            GL.o1.clear();

            // Update camera
            Fw.graphics.setCamera(camera);

            // Bind shader
            Fw.graphics.switchShader(vertexLitSingleDiffusePointShader);

            // Update uniforms
            vertexLitSingleDiffusePointShader.getUniforms().setAmbientLight(ambientLight);
            vertexLitSingleDiffusePointShader.getUniforms().setLight(glLight);
            vertexLitSingleDiffusePointShader.getUniforms().setMaterialLight(materialLight);

            // Render directly
            Fw.graphics.render(gameObjects);

            // Unbind shader
            Fw.graphics.unbindShader();
        }
    }

    @Override
    public void resize(int width, int height) {
        GL.o1.setViewport(0, 0, width, height);
    }

    @Override
    public void resume() {
    }

}
