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
package net.smert.frameworkgl.collision;

import net.smert.frameworkgl.collision.broadphase.BroadphaseProxy;
import net.smert.frameworkgl.collision.shapes.ShapeType;
import net.smert.frameworkgl.gameobjects.GameObject;
import net.smert.frameworkgl.math.MathHelper;
import net.smert.frameworkgl.math.Vector3f;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class CollisionGameObject extends GameObject {

    private boolean awake;
    private boolean canSleep;
    private boolean enabled;
    private boolean hasContactResponse;
    private boolean staticObject;
    private float inverseMass;
    private float linearDamping;
    private float restitution;
    private int collisionGroup;
    private int collisionCollidesWith;
    private BroadphaseProxy broadphaseProxy;
    private ShapeType shapeType;
    private final Vector3f linearAcceleration;
    private final Vector3f linearForce;
    private final Vector3f linearVelocity;
    private final Vector3f position;

    public CollisionGameObject() {
        super();
        awake = true;
        canSleep = true;
        enabled = true;
        hasContactResponse = true;
        staticObject = false;
        inverseMass = 0f;
        linearDamping = .99f;
        restitution = .1f;
        collisionCollidesWith = 0;
        collisionGroup = 0;
        linearAcceleration = new Vector3f();
        linearForce = new Vector3f();
        linearVelocity = new Vector3f();
        position = getWorldPosition(); // From game object
    }

    public void applyForce(Vector3f force) {
        if (inverseMass == 0) {
            return;
        }

        linearForce.add(force);
        awake = true;
    }

    public void applyImpulse(Vector3f impulse) {
        if (inverseMass == 0) {
            return;
        }

        linearVelocity.addScaled(impulse, inverseMass);
        awake = true;
    }

    public void applyPositionCorrection(Vector3f delta) {
        if (inverseMass == 0) {
            return;
        }

        position.addScaled(delta, inverseMass);
        awake = true;
    }

    public boolean canSleep() {
        return canSleep;
    }

    public void setCanSleep(boolean canSleep) {
        this.canSleep = canSleep;
    }

    public void collidedWith(CollisionGameObject other) {
    }

    public float getInverseMass() {
        return inverseMass;
    }

    public float getLinearDamping() {
        return linearDamping;
    }

    public void setLinearDamping(float linearDamping) {
        this.linearDamping = linearDamping;
    }

    public float getMass() {
        if (inverseMass == 0f) {
            return Float.MAX_VALUE;
        } else {
            return 1f / inverseMass;
        }
    }

    public void setMass(float mass) {
        if (mass == 0f) {
            inverseMass = 0f;
        } else {
            inverseMass = 1f / mass;
        }
    }

    public float getRestitution() {
        return restitution;
    }

    public void setRestitution(float restitution) {
        this.restitution = restitution;
    }

    public int getCollisionGroup() {
        return collisionGroup;
    }

    public void setCollisionGroup(int collisionGroup) {
        this.collisionGroup = collisionGroup;
    }

    public int getCollisionCollidesWith() {
        return collisionCollidesWith;
    }

    public void setCollisionCollidesWith(int collisionCollidesWith) {
        this.collisionCollidesWith = collisionCollidesWith;
    }

    public BroadphaseProxy getBroadphaseProxy() {
        return broadphaseProxy;
    }

    public void setBroadphaseProxy(BroadphaseProxy broadphaseProxy) {
        this.broadphaseProxy = broadphaseProxy;
    }

    public ShapeType getShapeType() {
        return shapeType;
    }

    public void initSetShapeType(ShapeType shapeType) {
        this.shapeType = shapeType;
    }

    public Vector3f getLinearAcceleration() {
        return linearAcceleration;
    }

    public void setLinearAcceleration(float x, float y, float z) {
        this.linearAcceleration.set(x, y, z);
    }

    public void setLinearAcceleration(Vector3f linearAcceleration) {
        this.linearAcceleration.set(linearAcceleration);
    }

    public Vector3f getLinearVelocity() {
        return linearVelocity;
    }

    public void setLinearVelocity(float x, float y, float z) {
        this.linearVelocity.set(x, y, z);
    }

    public void setLinearVelocity(Vector3f linearVelocity) {
        this.linearVelocity.set(linearVelocity);
    }

    public boolean hasContactResponse() {
        return hasContactResponse;
    }

    public void setHasContactResponse(boolean hasContactResponse) {
        this.hasContactResponse = hasContactResponse;
    }

    public void integrateExplicitEuler(float delta) {
        if (!awake || inverseMass == 0) {
            return;
        }

        // Save
        float accX = linearAcceleration.getX();
        float accY = linearAcceleration.getY();
        float accZ = linearAcceleration.getZ();

        // Integrate
        linearVelocity.multiply(MathHelper.Pow(linearDamping, delta));
        position.addScaled(linearVelocity, delta);
        linearAcceleration.addScaled(linearForce, inverseMass);
        linearVelocity.addScaled(linearAcceleration, delta);

        // Restore
        linearAcceleration.set(accX, accY, accZ);

        // Clear forces
        linearForce.zero();
    }

    public void integrateSemiExplicitEuler(float delta) {
        if (!awake || inverseMass == 0) {
            return;
        }

        // Save
        float accX = linearAcceleration.getX();
        float accY = linearAcceleration.getY();
        float accZ = linearAcceleration.getZ();

        // Integrate
        linearAcceleration.addScaled(linearForce, inverseMass);
        linearVelocity.addScaled(linearAcceleration, delta);
        linearVelocity.multiply(MathHelper.Pow(linearDamping, delta));
        position.addScaled(linearVelocity, delta);

        // Restore
        linearAcceleration.set(accX, accY, accZ);

        // Clear forces
        linearForce.zero();
    }

    public boolean isAwake() {
        return awake;
    }

    public void setAwake(boolean awake) {
        this.awake = awake;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isStaticObject() {
        return staticObject;
    }

    public void setStaticObject(boolean staticObject) {
        this.staticObject = staticObject;
    }

}
