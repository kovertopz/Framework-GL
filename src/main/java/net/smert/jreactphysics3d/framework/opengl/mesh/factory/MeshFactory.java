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
package net.smert.jreactphysics3d.framework.opengl.mesh.factory;

import net.smert.jreactphysics3d.framework.opengl.mesh.Material;
import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.mesh.Segment;
import net.smert.jreactphysics3d.framework.opengl.renderable.RenderableConfiguration;
import org.picocontainer.MutablePicoContainer;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class MeshFactory {

    private final MutablePicoContainer container;

    public MeshFactory(MutablePicoContainer meshFactoryContainer) {
        container = meshFactoryContainer;
    }

    public Material createMaterial() {
        return container.getComponent(Material.class);
    }

    public Mesh createMesh() {
        return container.getComponent(Mesh.class);
    }

    public RenderableConfiguration createRenderableConfiguration() {
        return container.getComponent(RenderableConfiguration.class);
    }

    public Segment createSegment() {
        return container.getComponent(Segment.class);
    }

}
