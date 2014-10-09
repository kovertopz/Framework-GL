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
package net.smert.frameworkgl.opengl.mesh.factory;

import net.smert.frameworkgl.opengl.mesh.Material;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.mesh.Segment;
import net.smert.frameworkgl.opengl.mesh.Tessellator;
import net.smert.frameworkgl.opengl.mesh.dynamic.AbstractDynamicMesh;
import net.smert.frameworkgl.opengl.mesh.dynamic.PrimitiveBox;
import net.smert.frameworkgl.opengl.mesh.dynamic.PrimitiveCapsule;
import net.smert.frameworkgl.opengl.mesh.dynamic.PrimitiveCone;
import net.smert.frameworkgl.opengl.mesh.dynamic.PrimitiveCylinder;
import net.smert.frameworkgl.opengl.mesh.dynamic.PrimitiveFrustum;
import net.smert.frameworkgl.opengl.mesh.dynamic.PrimitiveGrid;
import net.smert.frameworkgl.opengl.mesh.dynamic.PrimitivePyramid;
import net.smert.frameworkgl.opengl.mesh.dynamic.PrimitiveQuad;
import net.smert.frameworkgl.opengl.mesh.dynamic.PrimitiveSphere;
import net.smert.frameworkgl.opengl.mesh.dynamic.PrimitiveToriod;
import net.smert.frameworkgl.opengl.renderable.RenderableConfiguration;
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

    public AbstractDynamicMesh createDynamicPrimitiveBox() {
        return container.getComponent(PrimitiveBox.class);
    }

    public AbstractDynamicMesh createDynamicPrimitiveCapsule() {
        return container.getComponent(PrimitiveCapsule.class);
    }

    public AbstractDynamicMesh createDynamicPrimitiveCone() {
        return container.getComponent(PrimitiveCone.class);
    }

    public AbstractDynamicMesh createDynamicPrimitiveCylinder() {
        return container.getComponent(PrimitiveCylinder.class);
    }

    public AbstractDynamicMesh createDynamicPrimitiveFrustum() {
        return container.getComponent(PrimitiveFrustum.class);
    }

    public AbstractDynamicMesh createDynamicPrimitiveGrid() {
        return container.getComponent(PrimitiveGrid.class);
    }

    public AbstractDynamicMesh createDynamicPrimitivePyramid() {
        return container.getComponent(PrimitivePyramid.class);
    }

    public AbstractDynamicMesh createDynamicPrimitiveQuad() {
        return container.getComponent(PrimitiveQuad.class);
    }

    public AbstractDynamicMesh createDynamicPrimitiveSphere() {
        return container.getComponent(PrimitiveSphere.class);
    }

    public AbstractDynamicMesh createDynamicPrimitiveToriod() {
        return container.getComponent(PrimitiveToriod.class);
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

    public Tessellator createTessellator() {
        return container.getComponent(Tessellator.class);
    }

}
