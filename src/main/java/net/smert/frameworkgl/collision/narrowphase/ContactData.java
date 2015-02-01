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
package net.smert.frameworkgl.collision.narrowphase;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class ContactData {

    public final static int NULL = -1;

    private int capacity;
    private int free;
    private int size;
    private Contact[] contacts;

    public ContactData() {
        capacity = 16;
        free = 0;
        size = 0;
        contacts = new Contact[capacity];
        createContacts(free);
    }

    private void createContacts(int index) {
        assert (index >= 0);
        assert (index < capacity);

        // Create all contacts except the last one
        for (int i = index; i < capacity - 1; i++) {
            Contact contact = new Contact();
            contact.index = NULL; // Mark not in use
            contact.next = i + 1; // Next free contact
            contacts[i] = contact;
        }

        // Create last contact
        Contact contact = new Contact();
        contact.index = NULL; // Mark not in use
        contact.next = NULL; // Last free contact
        contacts[capacity - 1] = contact;
    }

    public Contact allocateContact() {

        // Expand contacts array
        if (free == NULL) {
            assert (size == capacity);
            capacity *= 2;
            free = size;
            Contact[] newContacts = new Contact[capacity];
            System.arraycopy(contacts, 0, newContacts, 0, size);
            contacts = newContacts;
            createContacts(free);
        }

        // Allocate contact
        int index = free;
        Contact contact = contacts[index];
        free = contact.next; // Next free contact
        size++;

        // Defaults
        contact.index = index; // Mark contact in use and also used to free contact
        contact.next = NULL; // Last free contact

        return contact;
    }

    public void freeContact(Contact contact) {
        contact.next = free; // Next free contact
        free = contact.index;
        size--;
        contact.index = NULL; // Mark contact free
    }

    public Contact[] getContacts() {
        return contacts;
    }

}
