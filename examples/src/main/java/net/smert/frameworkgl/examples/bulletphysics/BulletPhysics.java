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
package net.smert.frameworkgl.examples.bulletphysics;

import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.GameObject;
import net.smert.frameworkgl.Screen;
import net.smert.frameworkgl.helpers.Keyboard;
import net.smert.frameworkgl.math.Matrix3f;
import net.smert.frameworkgl.math.Transform4f;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.camera.LegacyCamera;
import net.smert.frameworkgl.opengl.camera.LegacyCameraController;
import net.smert.frameworkgl.opengl.constants.GetString;
import net.smert.frameworkgl.opengl.constants.Light;
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
    private BulletWrapper bulletWrapper;
    private FloatBuffer lightFloatBuffer;
    private FloatBuffer transformWorldFloatBuffer;
    private FpsTimer fpsTimer;
    private LegacyCamera camera;
    private LegacyCameraController cameraController;
    private final List<GameObject> gameObjects;
    private final Map<Integer, String> randomObjects;
    private MemoryUsage memoryUsage;

    public BulletPhysics(String[] args) {
        gameObjects = new ArrayList<>();
        randomObjects = new HashMap<>();
    }

    private void handleInput() {
        if (Fw.input.isKeyDown(Keyboard.ESCAPE) == true) {
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

        // Initialize Bullet
        bulletWrapper = new BulletWrapper();
        bulletWrapper.init();

        // Create timer
        fpsTimer = new FpsTimer();

        // Setup camera and controller
        camera = new LegacyCamera();
        camera.setPosition(0f, 5f, 20f);
        cameraController = new LegacyCameraController(camera);

        // Memory usage
        memoryUsage = new MemoryUsage();

        // Float buffer for light and matrices
        lightFloatBuffer = GL.bufferHelper.createFloatBuffer(4);
        transformWorldFloatBuffer = GL.bufferHelper.createFloatBuffer(16);

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

        GL.o1.enableCulling();
        GL.o1.cullBackFaces();
        GL.o1.enableDepthTest();
        GL.o1.setDepthFuncLess();
        GL.o1.enableDepthMask();
        GL.o1.setClearDepth(1f);
        GL.o1.enableColorMaterial();
        GL.o1.enableLight0();
        GL.o1.enableLighting();
        GL.o1.setSmoothLighting(true);
        GL.o1.clear();

        GL.o1.setProjectionPerspective(
                70f,
                (float) Fw.config.getCurrentWidth() / (float) Fw.config.getCurrentHeight(),
                .05f, 128f);
        GL.o1.setModelViewIdentity();

        // Light position
        lightFloatBuffer.put(0f);
        lightFloatBuffer.put(15f);
        lightFloatBuffer.put(10f);
        lightFloatBuffer.put(1f);
        lightFloatBuffer.flip();

        log.info("OpenGL version: " + GL.o1.getString(GetString.VERSION));

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

            float delta = Fw.timer.getDelta();
            bulletWrapper.stepSimulation(delta);

            if ((spawnTimer -= delta) < 0f) {
                int random = RandomUtils.NextInt(0, randomObjects.size() - 1);
                String randomObject = randomObjects.get(random);
                BulletGameObject gameObject = bulletWrapper.createBulletGameObject(randomObject,
                        new Transform4f(new Matrix3f().identity(), new Vector3f(0f, 25f, 0f)));
                gameObjects.add(gameObject);
                bulletWrapper.addDynamicsWorldRigidBody(gameObject);
                spawnTimer += .5f;
            }

            // Clear screen and reset modelview matrix
            GL.o1.clear();
            GL.o1.setModelViewIdentity();

            camera.updateOpenGL();

            GL.o1.light(Light.LIGHT0, Light.POSITION, lightFloatBuffer);

            // Render directly
            for (GameObject gameObject : gameObjects) {
                Fw.graphics.render(gameObject, transformWorldFloatBuffer);
            }
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
