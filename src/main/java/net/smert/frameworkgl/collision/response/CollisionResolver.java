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
package net.smert.frameworkgl.collision.response;

import net.smert.frameworkgl.collision.CollisionGameObject;
import net.smert.frameworkgl.collision.narrowphase.Contact;
import net.smert.frameworkgl.collision.narrowphase.ContactData;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.utils.ThreadLocalVars;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class CollisionResolver {

    private final CollisionResponseFilterCallback collisionResponseFilterCallback;

    public CollisionResolver(CollisionResponseFilterCallback collisionResponseFilterCallback) {
        this.collisionResponseFilterCallback = collisionResponseFilterCallback;
    }

    private void resolveCollision(Contact contact) {
        float penetration = contact.penetration;
        CollisionGameObject collisionGameObject0 = contact.collisionGameObject0;
        CollisionGameObject collisionGameObject1 = contact.collisionGameObject1;
        Vector3f normal = contact.normal;

        // From collision objects
        float restitution = Math.min(collisionGameObject0.getRestitution(), collisionGameObject1.getRestitution());
        float totalInverseMass = collisionGameObject0.getInverseMass() + collisionGameObject1.getInverseMass();
        Vector3f linearVelocity0 = collisionGameObject0.getLinearVelocity();
        Vector3f linearVelocity1 = collisionGameObject1.getLinearVelocity();

        // Temp vars from thread local storage
        ThreadLocalVars vars = ThreadLocalVars.Get();
        Vector3f impulse = vars.v3f0;
        Vector3f positionCorrection = vars.v3f1;
        Vector3f relativeVelocity = vars.v3f2;

        // Relative velocity from collisionGameObject0 to collisionGameObject1
        relativeVelocity.set(linearVelocity1).subtract(linearVelocity0);
        float velocityOnNormal = relativeVelocity.dot(normal);

        // Calculate impulse
        // j = -(1 + e) * ((Vb - Va) . N) / ( (1 / MASSa) + (1 / MASSb) )
        float j = -(1f + restitution) * velocityOnNormal / totalInverseMass;
        impulse.set(normal).multiply(j);

        // Apply impulse to both objects but do collisionGameObject1 first
        // since the impulse to collisionGameObject0 needs to be inverted.
        collisionGameObject1.applyImpulse(impulse);
        collisionGameObject0.applyImpulse(impulse.invert());

        // Correct position
        positionCorrection.set(normal).multiply(penetration / totalInverseMass);

        // Apply position correction to both objects
        collisionGameObject0.applyPositionCorrection(positionCorrection);
        collisionGameObject1.applyPositionCorrection(positionCorrection.invert());

        // Release vars instance
        vars.release();
    }

    public void processContacts(ContactData contactData) {
        Contact[] contacts = contactData.getContacts();

        for (int i = 0; i < contacts.length; i++) {
            Contact contact = contacts[i];

            // Skip free contact
            if (contact.index == ContactData.NULL) {
                continue;
            }

            // Resolve collision
            if (collisionResponseFilterCallback.needsCollisionResponse(contact.collisionGameObject0,
                    contact.collisionGameObject1)) {
                resolveCollision(contact);
            }

            // Free contact
            contactData.freeContact(contact);
        }
    }

}
