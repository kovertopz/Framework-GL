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
package net.smert.frameworkgl.gameobjects;

import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.TextureType;
import net.smert.frameworkgl.opengl.mesh.Material;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class SkyboxGameObject extends GameObject {

    public void init(String skyboxTexture) {

        // Create cube
        Mesh mesh = GL.meshFactory.createMesh();
        GL.dynamicMeshBuilder.
                setColor(0, "white").
                setQuality(1, 1, 1).
                setSize(1.0f, 1.0f, 1.0f).
                build("cube_map").
                createMesh(true, mesh);

        // Create material
        Material material = mesh.getSegment(0).getMaterial();
        if (material == null) {
            material = GL.meshFactory.createMaterial();
            mesh.getSegment(0).setMaterial(material);
        }

        // Set the environment texture 
        mesh.getSegment(0).getMaterial().setTexture(TextureType.ENVIRONMENT, skyboxTexture);

        // Create renderable from mesh
        AbstractRenderable renderable = Fw.graphics.createInterleavedRenderable();
        renderable.create(mesh); // Texture must be loaded before renderable is created

        // Attach to game object
        setMesh(mesh);
        setRenderable(renderable);
    }

}
