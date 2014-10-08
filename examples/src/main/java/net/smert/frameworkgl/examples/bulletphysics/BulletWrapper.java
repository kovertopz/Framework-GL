package net.smert.frameworkgl.examples.bulletphysics;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import net.smert.frameworkgl.math.Transform4f;
import net.smert.frameworkgl.math.Vector3f;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class BulletWrapper {

    private float fixedTimeStep = 1f / 60f;
    private btCollisionDispatcher collisionDispatcher;
    private btConstraintSolver constraintSolver;
    private btDbvtBroadphase broadphase;
    private btDefaultCollisionConfiguration collisionConfig;
    private btDynamicsWorld dynamicsWorld;
    private BulletGameObjectFactory factory;
    private final Vector3 gravity = new Vector3(0f, -10f, 0f);

    public BulletGameObject.Constructor addConstructor(String name, BulletGameObject.Constructor constructor) {
        return factory.add(name, constructor);
    }

    public void addDynamicsWorldRigidBody(BulletGameObject bulletGameObject) {
        btRigidBody rigidBody = bulletGameObject.getBody();
        dynamicsWorld.addRigidBody(rigidBody);
    }

    public BulletGameObject createBulletGameObject(String name, Transform4f worldTransform) {
        return factory.create(name, worldTransform);
    }

    public void destroy() {
        factory.destroy();
        dynamicsWorld.dispose();
        constraintSolver.dispose();
        broadphase.dispose();
        collisionDispatcher.dispose();
        collisionConfig.dispose();
    }

    public float getFixedTimeStep() {
        return fixedTimeStep;
    }

    public void setFixedTimeStep(float fixedTimeStep) {
        this.fixedTimeStep = fixedTimeStep;
    }

    public btCollisionDispatcher getCollisionDispatcher() {
        return collisionDispatcher;
    }

    public btConstraintSolver getConstraintSolver() {
        return constraintSolver;
    }

    public btDbvtBroadphase getBroadphase() {
        return broadphase;
    }

    public btDefaultCollisionConfiguration getCollisionConfig() {
        return collisionConfig;
    }

    public btDynamicsWorld getDynamicsWorld() {
        return dynamicsWorld;
    }

    public BulletGameObjectFactory getFactory() {
        return factory;
    }

    public Vector3 getGravity() {
        return gravity;
    }

    public void setGravity(Vector3f gravity) {
        this.gravity.set(gravity.getX(), gravity.getY(), gravity.getZ());
    }

    public void init() {

        // Initialize Bullet
        Bullet.init();

        // Setup factory
        factory = new BulletGameObjectFactory();
        factory.init();

        // Bullet world
        collisionConfig = new btDefaultCollisionConfiguration();
        collisionDispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        constraintSolver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(collisionDispatcher, broadphase, constraintSolver, collisionConfig);
        dynamicsWorld.setGravity(gravity);
    }

    public BulletGameObject.Constructor removeConstructor(String name) {
        return factory.remove(name);
    }

    public void removeDynamicsWorldRigidBody(BulletGameObject bulletGameObject) {
        btRigidBody rigidBody = bulletGameObject.getBody();
        dynamicsWorld.removeRigidBody(rigidBody);
    }

    public void stepSimulation(float delta) {
        dynamicsWorld.stepSimulation(delta, 5, fixedTimeStep);
    }

}
