package net.smert.frameworkgl.examples.bulletphysics;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import net.smert.frameworkgl.GameObject;
import net.smert.frameworkgl.math.Transform4f;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class BulletGameObject extends GameObject {

    public final btRigidBody body;
    public final MotionState motionState;
    public final String name;

    public BulletGameObject(AbstractRenderable renderable, Transform4f scaling,
            MotionState motionState, String name, btRigidBody.btRigidBodyConstructionInfo constructionInfo) {
        setRenderable(renderable); // Attach to game object
        setScalingTransform(scaling); // Attach to game object
        this.motionState = motionState;
        this.name = name;
        body = new btRigidBody(constructionInfo);
        body.setMotionState(motionState);
        body.proceedToTransform(motionState.worldTransform);
        setCollisionShape(constructionInfo.getCollisionShape()); // Attach to game object
        setRigidBody(body); // Attach to game object
    }

    @Override
    public void destroy() {
        body.dispose();
        motionState.dispose();
        super.destroy();
    }

    public btRigidBody getBody() {
        return body;
    }

    public MotionState getMotionState() {
        return motionState;
    }

    public String getName() {
        return name;
    }

    public static class Constructor {

        private final static Vector3 LOCAL_INERTIA = new Vector3();

        private final AbstractRenderable renderable;
        private final btCollisionShape shape;
        private final btRigidBody.btRigidBodyConstructionInfo constructionInfo;
        private final String name;
        private final Transform4f scaling;

        public Constructor(float mass, AbstractRenderable renderable, btCollisionShape collisionShape, String name,
                Transform4f scaling) {
            this.renderable = renderable;
            this.shape = collisionShape;
            this.name = name;
            this.scaling = scaling;
            if (mass > 0f) {
                collisionShape.calculateLocalInertia(mass, LOCAL_INERTIA);
            } else {
                LOCAL_INERTIA.set(0f, 0f, 0f);
            }
            this.constructionInfo
                    = new btRigidBody.btRigidBodyConstructionInfo(mass, null, collisionShape, LOCAL_INERTIA);
        }

        public BulletGameObject create(Matrix4 worldTransform) {
            MotionState motionState = new MotionState(worldTransform);
            BulletGameObject bulletGameObject
                    = new BulletGameObject(renderable, scaling, motionState, name, constructionInfo);
            motionState.setGameObject(bulletGameObject);
            motionState.setWorldTransform(worldTransform); // Game object transform and scaling is applied
            return bulletGameObject;
        }

        public void destroy() {
            renderable.destroy();
            constructionInfo.dispose();
            shape.dispose();
        }

    }

    public static Transform4f CreateBoxScaling(Vector3f size) {
        Vector3f halfSize = new Vector3f(size).multiply(.5f);
        Transform4f scaling = new Transform4f();
        scaling.getRotation().setDiagonal(halfSize);
        return scaling;
    }

    public static Transform4f CreateCapsuleScaling(float height, float radius) {
        Vector3f size = new Vector3f(radius, (height + 2f * radius) / 3f, radius);
        Transform4f scaling = new Transform4f();
        scaling.getRotation().setDiagonal(size);
        return scaling;
    }

    public static Transform4f CreateConeScaling(float height, float radius) {
        Vector3f size = new Vector3f(radius, height, radius);
        Transform4f scaling = new Transform4f();
        scaling.getRotation().setDiagonal(size);
        return scaling;
    }

    public static Transform4f CreateCylinderScaling(float height, float radius) {
        Vector3f size = new Vector3f(radius, height, radius);
        Transform4f scaling = new Transform4f();
        scaling.getRotation().setDiagonal(size);
        return scaling;
    }

    public static Transform4f CreateDefaultScaling() {
        return new Transform4f();
    }

    public static Transform4f CreateSphereScaling(float radius) {
        Vector3f size = new Vector3f(radius, radius, radius);
        Transform4f scaling = new Transform4f();
        scaling.getRotation().setDiagonal(size);
        return scaling;
    }

    public static class MotionState extends btMotionState {

        private GameObject gameObject;
        private final Matrix4 worldTransform;

        public MotionState(Matrix4 worldTransform) {
            this.worldTransform = worldTransform;
        }

        public void setGameObject(GameObject gameObject) {
            this.gameObject = gameObject;
        }

        @Override
        public void getWorldTransform(Matrix4 worldTransform) {
            worldTransform.set(this.worldTransform);
        }

        @Override
        public final void setWorldTransform(Matrix4 worldTransform) {
            this.worldTransform.set(worldTransform);
            gameObject.getWorldTransform().fromOpenGLArray(worldTransform.getValues());
            gameObject.getWorldTransform().multiply(gameObject.getScalingTransform());
        }

    }

}
