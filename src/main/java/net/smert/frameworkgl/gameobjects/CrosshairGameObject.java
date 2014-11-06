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
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.mesh.SegmentMaterial;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class CrosshairGameObject extends GameObject {

    public void init(int width, int height, String textureFilename) {

        // Create quad
        Mesh mesh = GL.meshFactory.createMesh();
        GL.dynamicMeshBuilder.
                setQuality(1, 1, 1).
                setSize(width, height, 1.0f).
                build("quad").
                createMesh(true, mesh);

        // Create segment material
        SegmentMaterial segmentMaterial = mesh.getSegment(0).getMaterial();
        if (segmentMaterial == null) {
            segmentMaterial = GL.meshFactory.createSegmentMaterial();
            mesh.getSegment(0).setMaterial(segmentMaterial);
        }

        // Set the diffuse texture 
        mesh.getSegment(0).getMaterial().setTexture(TextureType.DIFFUSE, textureFilename);

        // Create renderable from mesh
        AbstractRenderable renderable = Fw.graphics.createInterleavedRenderable();
        renderable.create(mesh); // Texture must be loaded before renderable is created

        // Attach to game object
        setMesh(mesh);
        setRenderable(renderable);
    }

    @Override
    public void update() {
        getWorldTransform().setPosition(Fw.config.getCurrentWidth() / 2f, Fw.config.getCurrentHeight() / 2f, 0f);
    }

}
