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
package net.smert.frameworkgl;

import java.io.IOException;
import java.util.logging.SimpleFormatter;
import net.smert.frameworkgl.gameobjects.GameObject;
import net.smert.frameworkgl.gameobjects.factory.GameObjectFactory;
import net.smert.frameworkgl.helpers.KeyboardHelper;
import net.smert.frameworkgl.helpers.MouseHelper;
import net.smert.frameworkgl.opengl.AmbientLight;
import net.smert.frameworkgl.opengl.DisplayList;
import net.smert.frameworkgl.opengl.FrameBufferObject;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.GLLight;
import net.smert.frameworkgl.opengl.MaterialLight;
import net.smert.frameworkgl.opengl.OpenGL1;
import net.smert.frameworkgl.opengl.OpenGL2;
import net.smert.frameworkgl.opengl.OpenGL3;
import net.smert.frameworkgl.opengl.RenderBufferObject;
import net.smert.frameworkgl.opengl.Shader;
import net.smert.frameworkgl.opengl.Shadow;
import net.smert.frameworkgl.opengl.Texture;
import net.smert.frameworkgl.opengl.VertexArray;
import net.smert.frameworkgl.opengl.VertexArrayObject;
import net.smert.frameworkgl.opengl.VertexBufferObject;
import net.smert.frameworkgl.opengl.VertexBufferObjectInterleaved;
import net.smert.frameworkgl.opengl.camera.Camera;
import net.smert.frameworkgl.opengl.camera.CameraController;
import net.smert.frameworkgl.opengl.camera.FrustumCullingClipSpace;
import net.smert.frameworkgl.opengl.camera.FrustumCullingClipSpaceSymmetrical;
import net.smert.frameworkgl.opengl.camera.LegacyCamera;
import net.smert.frameworkgl.opengl.camera.LegacyCameraController;
import net.smert.frameworkgl.opengl.camera.factory.CameraFactory;
import net.smert.frameworkgl.opengl.factory.GLFactory;
import net.smert.frameworkgl.opengl.font.GLFontBuilder;
import net.smert.frameworkgl.opengl.helpers.BufferHelper;
import net.smert.frameworkgl.opengl.helpers.DisplayListHelper;
import net.smert.frameworkgl.opengl.helpers.FrameBufferObjectHelper;
import net.smert.frameworkgl.opengl.helpers.LegacyRenderHelper;
import net.smert.frameworkgl.opengl.helpers.MatrixHelper;
import net.smert.frameworkgl.opengl.helpers.RenderBufferObjectHelper;
import net.smert.frameworkgl.opengl.helpers.ShaderHelper;
import net.smert.frameworkgl.opengl.helpers.ShaderUniformHelper;
import net.smert.frameworkgl.opengl.helpers.TextureHelper;
import net.smert.frameworkgl.opengl.helpers.VertexArrayHelper;
import net.smert.frameworkgl.opengl.helpers.VertexBufferObjectHelper;
import net.smert.frameworkgl.opengl.image.bmp.BMPReader;
import net.smert.frameworkgl.opengl.image.gif.GIFReader;
import net.smert.frameworkgl.opengl.image.jpg.JPGReader;
import net.smert.frameworkgl.opengl.image.png.PNGReader;
import net.smert.frameworkgl.opengl.image.tga.TGAImage;
import net.smert.frameworkgl.opengl.image.tga.TGAReader;
import net.smert.frameworkgl.opengl.image.tiff.TIFFReader;
import net.smert.frameworkgl.opengl.mesh.DrawCommandsConversion;
import net.smert.frameworkgl.opengl.mesh.DynamicMeshBuilder;
import net.smert.frameworkgl.opengl.mesh.Material;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.mesh.MeshReader;
import net.smert.frameworkgl.opengl.mesh.Segment;
import net.smert.frameworkgl.opengl.mesh.Tessellator;
import net.smert.frameworkgl.opengl.mesh.dynamic.AABB;
import net.smert.frameworkgl.opengl.mesh.dynamic.CubeMap;
import net.smert.frameworkgl.opengl.mesh.dynamic.PrimitiveCapsule;
import net.smert.frameworkgl.opengl.mesh.dynamic.PrimitiveCone;
import net.smert.frameworkgl.opengl.mesh.dynamic.PrimitiveCube;
import net.smert.frameworkgl.opengl.mesh.dynamic.PrimitiveCylinder;
import net.smert.frameworkgl.opengl.mesh.dynamic.PrimitiveFrustum;
import net.smert.frameworkgl.opengl.mesh.dynamic.PrimitiveGrid;
import net.smert.frameworkgl.opengl.mesh.dynamic.PrimitivePyramid;
import net.smert.frameworkgl.opengl.mesh.dynamic.PrimitiveQuad;
import net.smert.frameworkgl.opengl.mesh.dynamic.PrimitiveSphere;
import net.smert.frameworkgl.opengl.mesh.dynamic.PrimitiveToriod;
import net.smert.frameworkgl.opengl.mesh.dynamic.SimpleOrientationAxis;
import net.smert.frameworkgl.opengl.mesh.dynamic.ViewFrustum;
import net.smert.frameworkgl.opengl.mesh.factory.MeshFactory;
import net.smert.frameworkgl.opengl.model.obj.MaterialReader;
import net.smert.frameworkgl.opengl.model.obj.ObjReader;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderable.RenderableConfiguration;
import net.smert.frameworkgl.opengl.renderable.factory.RenderableFactoryGL1;
import net.smert.frameworkgl.opengl.renderable.factory.RenderableFactoryGL2;
import net.smert.frameworkgl.opengl.renderable.factory.RenderableFactoryGL3;
import net.smert.frameworkgl.opengl.renderable.gl1.BindStateGL1;
import net.smert.frameworkgl.opengl.renderable.gl1.VertexArrayBindStrategyGL1;
import net.smert.frameworkgl.opengl.renderable.gl1.VertexBufferObjectBindStrategyGL1;
import net.smert.frameworkgl.opengl.renderable.gl2.BindStateGL2;
import net.smert.frameworkgl.opengl.renderable.gl2.VertexArrayBindStrategyGL2;
import net.smert.frameworkgl.opengl.renderable.gl2.VertexBufferObjectBindStrategyGL2;
import net.smert.frameworkgl.opengl.renderable.shared.DisplayListRenderable;
import net.smert.frameworkgl.opengl.renderable.shared.ImmediateModeRenderable;
import net.smert.frameworkgl.opengl.renderable.shared.RenderableConfigurationPool;
import net.smert.frameworkgl.opengl.renderable.shared.ShaderBindState;
import net.smert.frameworkgl.opengl.renderable.shared.ShaderPool;
import net.smert.frameworkgl.opengl.renderable.shared.TextureBindState;
import net.smert.frameworkgl.opengl.renderable.shared.TexturePool;
import net.smert.frameworkgl.opengl.renderable.shared.VertexArrayCreateStrategy;
import net.smert.frameworkgl.opengl.renderable.shared.VertexArrayRenderStrategy;
import net.smert.frameworkgl.opengl.renderable.shared.VertexArrayRenderable;
import net.smert.frameworkgl.opengl.renderable.shared.VertexArrayUpdateStrategy;
import net.smert.frameworkgl.opengl.renderable.shared.VertexBufferObjectDynamicInterleavedRenderable;
import net.smert.frameworkgl.opengl.renderable.shared.VertexBufferObjectDynamicNonInterleavedRenderable;
import net.smert.frameworkgl.opengl.renderable.shared.VertexBufferObjectInterleavedCreateStrategy;
import net.smert.frameworkgl.opengl.renderable.shared.VertexBufferObjectInterleavedRenderStrategy;
import net.smert.frameworkgl.opengl.renderable.shared.VertexBufferObjectInterleavedRenderable;
import net.smert.frameworkgl.opengl.renderable.shared.VertexBufferObjectInterleavedUpdateStrategy;
import net.smert.frameworkgl.opengl.renderable.shared.VertexBufferObjectNonInterleavedCreateStrategy;
import net.smert.frameworkgl.opengl.renderable.shared.VertexBufferObjectNonInterleavedRenderStrategy;
import net.smert.frameworkgl.opengl.renderable.shared.VertexBufferObjectNonInterleavedRenderable;
import net.smert.frameworkgl.opengl.renderable.shared.VertexBufferObjectNonInterleavedUpdateStrategy;
import net.smert.frameworkgl.opengl.renderable.va.VABuilder;
import net.smert.frameworkgl.opengl.renderable.va.VADrawArrays;
import net.smert.frameworkgl.opengl.renderable.va.VADrawElements;
import net.smert.frameworkgl.opengl.renderable.va.VertexArrays;
import net.smert.frameworkgl.opengl.renderable.va.factory.VADrawCallFactory;
import net.smert.frameworkgl.opengl.renderable.vbo.ByteBuffers;
import net.smert.frameworkgl.opengl.renderable.vbo.VBOBuilder;
import net.smert.frameworkgl.opengl.renderable.vbo.VBODrawArrays;
import net.smert.frameworkgl.opengl.renderable.vbo.VBODrawElements;
import net.smert.frameworkgl.opengl.renderable.vbo.VBODrawRangeElements;
import net.smert.frameworkgl.opengl.renderable.vbo.factory.VBODrawCallFactory;
import net.smert.frameworkgl.opengl.renderer.GLFontRenderer;
import net.smert.frameworkgl.opengl.renderer.RendererGL1;
import net.smert.frameworkgl.opengl.renderer.RendererGL2;
import net.smert.frameworkgl.opengl.renderer.RendererGL3;
import net.smert.frameworkgl.opengl.shader.DefaultAttribLocations;
import net.smert.frameworkgl.opengl.shader.ShaderBuilder;
import net.smert.frameworkgl.opengl.texture.TextureBuilder;
import net.smert.frameworkgl.opengl.texture.TextureReader;
import org.picocontainer.Characteristics;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;
import org.picocontainer.parameters.ConstantParameter;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class BootStrap {

    protected MutablePicoContainer cameraFactoryContainer;
    protected MutablePicoContainer gameObjectFactoryContainer;
    protected MutablePicoContainer glFactoryContainer;
    protected MutablePicoContainer meshFactoryContainer;
    protected MutablePicoContainer renderableFactoryGL1Container;
    protected MutablePicoContainer renderableFactoryGL2Container;
    protected MutablePicoContainer renderableFactoryGL3Container;
    protected MutablePicoContainer vaDrawCallFactoryContainer;
    protected MutablePicoContainer vboDrawCallFactoryContainer;

    private void addContainersForFactories(MutablePicoContainer parentContainer) {

        {
            // Container for GameObjectFactory
            gameObjectFactoryContainer = new PicoBuilder(parentContainer).withConstructorInjection().build(); // NO caching!

            // Framework multiple instance components
            gameObjectFactoryContainer.addComponent(GameObject.class);

            // Add container for GameObjectFactory
            parentContainer.addComponent("gameObjectFactoryContainer", gameObjectFactoryContainer);
        }

        {
            // Container for GLFactory
            glFactoryContainer = new PicoBuilder(parentContainer).withConstructorInjection().build(); // NO caching!

            // OpenGL multiple instance components
            glFactoryContainer.addComponent(AmbientLight.class);
            glFactoryContainer.addComponent(DisplayList.class);
            glFactoryContainer.addComponent(FrameBufferObject.class);
            glFactoryContainer.addComponent(GLLight.class);
            glFactoryContainer.addComponent(MaterialLight.class);
            glFactoryContainer.addComponent(RenderBufferObject.class);
            glFactoryContainer.addComponent(Shader.class);
            glFactoryContainer.addComponent(Shadow.class);
            glFactoryContainer.addComponent(Texture.class);
            glFactoryContainer.addComponent(VertexArray.class);
            glFactoryContainer.addComponent(VertexArrayObject.class);
            glFactoryContainer.addComponent(VertexBufferObject.class);
            glFactoryContainer.addComponent(VertexBufferObjectInterleaved.class);

            // Add container for GLFactory
            parentContainer.addComponent("glFactoryContainer", glFactoryContainer);
        }

        {
            // Container for CameraFactory
            cameraFactoryContainer = new PicoBuilder(parentContainer).withConstructorInjection().build(); // NO caching!

            // Camera
            cameraFactoryContainer.addComponent(Camera.class);
            cameraFactoryContainer.addComponent(CameraController.class);
            cameraFactoryContainer.addComponent(FrustumCullingClipSpace.class);
            cameraFactoryContainer.addComponent(FrustumCullingClipSpaceSymmetrical.class);
            cameraFactoryContainer.addComponent(LegacyCamera.class);
            cameraFactoryContainer.addComponent(LegacyCameraController.class);

            // Add container for CameraFactory
            parentContainer.addComponent("cameraFactoryContainer", cameraFactoryContainer);
        }

        {
            // Container for MeshFactory
            meshFactoryContainer = new PicoBuilder(parentContainer).withConstructorInjection().build(); // NO caching!

            // Mesh
            meshFactoryContainer.addComponent(Material.class);
            meshFactoryContainer.addComponent(Mesh.class);
            meshFactoryContainer.addComponent(RenderableConfiguration.class);
            meshFactoryContainer.addComponent(Segment.class);
            meshFactoryContainer.addComponent(AABB.class);
            meshFactoryContainer.addComponent(CubeMap.class);
            meshFactoryContainer.addComponent(PrimitiveCapsule.class);
            meshFactoryContainer.addComponent(PrimitiveCone.class);
            meshFactoryContainer.addComponent(PrimitiveCube.class);
            meshFactoryContainer.addComponent(PrimitiveCylinder.class);
            meshFactoryContainer.addComponent(PrimitiveFrustum.class);
            meshFactoryContainer.addComponent(PrimitiveGrid.class);
            meshFactoryContainer.addComponent(PrimitivePyramid.class);
            meshFactoryContainer.addComponent(PrimitiveQuad.class);
            meshFactoryContainer.addComponent(PrimitiveSphere.class);
            meshFactoryContainer.addComponent(PrimitiveToriod.class);
            meshFactoryContainer.addComponent(SimpleOrientationAxis.class);
            meshFactoryContainer.addComponent(ViewFrustum.class);
            meshFactoryContainer.addComponent(Tessellator.class);

            // Add container for MeshFactory
            parentContainer.addComponent("meshFactoryContainer", meshFactoryContainer);
        }

        {
            // Container for RenderableFactoryGL1
            renderableFactoryGL1Container = new PicoBuilder(parentContainer).withConstructorInjection().build(); // NO caching!

            // Renderable
            renderableFactoryGL1Container.addComponent(DisplayListRenderable.class);
            renderableFactoryGL1Container.addComponent(ImmediateModeRenderable.class);
            renderableFactoryGL1Container.addComponent(VertexArrayRenderable.class);
            renderableFactoryGL1Container.addComponent(VertexBufferObjectDynamicInterleavedRenderable.class);
            renderableFactoryGL1Container.addComponent(VertexBufferObjectDynamicNonInterleavedRenderable.class);
            renderableFactoryGL1Container.addComponent(VertexBufferObjectInterleavedRenderable.class);
            renderableFactoryGL1Container.addComponent(VertexBufferObjectNonInterleavedRenderable.class);

            // Add container for RenderableFactoryGL1
            parentContainer.addComponent("renderableFactoryGL1Container", renderableFactoryGL1Container);
        }

        {
            // Container for RenderableFactoryGL2
            renderableFactoryGL2Container = new PicoBuilder(parentContainer).withConstructorInjection().build(); // NO caching!

            // Renderable
            renderableFactoryGL2Container.addComponent(DisplayListRenderable.class);
            renderableFactoryGL2Container.addComponent(ImmediateModeRenderable.class);
            renderableFactoryGL2Container.addComponent(VertexArrayRenderable.class);
            renderableFactoryGL2Container.addComponent(VertexBufferObjectDynamicInterleavedRenderable.class);
            renderableFactoryGL2Container.addComponent(VertexBufferObjectDynamicNonInterleavedRenderable.class);
            renderableFactoryGL2Container.addComponent(VertexBufferObjectInterleavedRenderable.class);
            renderableFactoryGL2Container.addComponent(VertexBufferObjectNonInterleavedRenderable.class);

            // Add container for RenderableFactoryGL2
            parentContainer.addComponent("renderableFactoryGL2Container", renderableFactoryGL2Container);
        }

        {
            // Container for RenderableFactoryGL3
            renderableFactoryGL3Container = new PicoBuilder(parentContainer).withConstructorInjection().build(); // NO caching!

            // Add container for RenderableFactoryGL3
            parentContainer.addComponent("renderableFactoryGL3Container", renderableFactoryGL3Container);
        }

        {
            // Container for VADrawCallFactory
            vaDrawCallFactoryContainer = new PicoBuilder(parentContainer).withConstructorInjection().build(); // NO caching!

            // Renderable VA
            vaDrawCallFactoryContainer.addComponent(VADrawArrays.class);
            vaDrawCallFactoryContainer.addComponent(VADrawElements.class);

            // Add container for VADrawCallFactory
            parentContainer.addComponent("vaDrawCallFactoryContainer", vaDrawCallFactoryContainer);
        }

        {
            // Container for VBODrawCallFactory
            vboDrawCallFactoryContainer = new PicoBuilder(parentContainer).withConstructorInjection().build(); // NO caching!

            // Renderable VBO
            vboDrawCallFactoryContainer.addComponent(VBODrawArrays.class);
            vboDrawCallFactoryContainer.addComponent(VBODrawElements.class);
            vboDrawCallFactoryContainer.addComponent(VBODrawRangeElements.class);

            // Add container for VBODrawCallFactory
            parentContainer.addComponent("vboDrawCallFactoryContainer", vboDrawCallFactoryContainer);
        }
    }

    private MutablePicoContainer createDependencies(Class configClass, Class screenClass, String[] args) {

        // Create Pico Container
        MutablePicoContainer container = new PicoBuilder()
                .withCaching().withConstructorInjection().build(); // MUST CACHE INSTANCES!

        // Framework components
        container.addComponent(Configuration.class, configClass, new ConstantParameter(args));
        container.addComponent(Screen.class, screenClass, new ConstantParameter(args));
        container.addComponent(Application.class);
        container.addComponent(Audio.class);
        container.addComponent(Files.class);
        container.addComponent(Graphics.class);
        container.addComponent(Input.class);
        container.addComponent(Logging.class);
        container.addComponent(Network.class);
        container.addComponent(ThrowableHandler.class);
        container.addComponent(Timer.class);
        container.addComponent(Window.class);

        // Framework component dependencies
        container.addComponent(SimpleFormatter.class);

        // Framework factories
        container.as(Characteristics.USE_NAMES).addComponent(GameObjectFactory.class);

        // Framework helpers
        container.addComponent(KeyboardHelper.class);
        container.addComponent(MouseHelper.class);

        // OpenGL components
        container.addComponent(OpenGL1.class);
        container.addComponent(OpenGL2.class);
        container.addComponent(OpenGL3.class);

        // OpenGL camera factory
        container.as(Characteristics.USE_NAMES).addComponent(CameraFactory.class);

        // OpenGL factory
        container.as(Characteristics.USE_NAMES).addComponent(GLFactory.class);

        // OpenGL font
        container.addComponent(GLFontBuilder.class);

        // OpenGL helpers
        container.addComponent(BufferHelper.class);
        container.addComponent(DisplayListHelper.class);
        container.addComponent(FrameBufferObjectHelper.class);
        container.addComponent(LegacyRenderHelper.class);
        container.addComponent(MatrixHelper.class);
        container.addComponent(RenderBufferObjectHelper.class);
        container.addComponent(ShaderHelper.class);
        container.addComponent(ShaderUniformHelper.class);
        container.addComponent(TextureHelper.class);
        container.addComponent(VertexArrayHelper.class);
        container.addComponent(VertexBufferObjectHelper.class);

        // Image
        container.addComponent(BMPReader.class);
        container.addComponent(GIFReader.class);
        container.addComponent(JPGReader.class);
        container.addComponent(PNGReader.class);
        container.addComponent(TGAImage.class);
        container.addComponent(TGAReader.class);
        container.addComponent(TIFFReader.class);

        // Mesh
        container.addComponent(DrawCommandsConversion.class);
        container.addComponent(DynamicMeshBuilder.class);
        container.addComponent(MeshReader.class);
        container.addComponent(Tessellator.class);

        // Mesh factory
        container.as(Characteristics.USE_NAMES).addComponent(MeshFactory.class);

        // Model
        container.addComponent(MaterialReader.class);
        container.addComponent(ObjReader.class);

        // Renderable factory
        container.as(Characteristics.USE_NAMES).addComponent(RenderableFactoryGL1.class);
        container.as(Characteristics.USE_NAMES).addComponent(RenderableFactoryGL2.class);
        container.as(Characteristics.USE_NAMES).addComponent(RenderableFactoryGL3.class);

        // Renderable factory gl1
        container.addComponent(BindStateGL1.class);
        container.addComponent(VertexArrayBindStrategyGL1.class);
        container.addComponent(VertexBufferObjectBindStrategyGL1.class);

        // Renderable factory gl2
        container.addComponent(BindStateGL2.class);
        container.addComponent(VertexArrayBindStrategyGL2.class);
        container.addComponent(VertexBufferObjectBindStrategyGL2.class);

        // Renderable shared
        container.addComponent(RenderableConfigurationPool.class);
        container.addComponent(ShaderBindState.class);
        container.addComponent(ShaderPool.class);
        container.addComponent(TextureBindState.class);
        container.addComponent(TexturePool.class);
        container.addComponent(VertexArrayCreateStrategy.class);
        container.addComponent(VertexArrayRenderStrategy.class);
        container.addComponent(VertexArrayUpdateStrategy.class);
        container.addComponent(VertexBufferObjectInterleavedCreateStrategy.class);
        container.addComponent(VertexBufferObjectInterleavedRenderStrategy.class);
        container.addComponent(VertexBufferObjectInterleavedUpdateStrategy.class);
        container.addComponent(VertexBufferObjectNonInterleavedCreateStrategy.class);
        container.addComponent(VertexBufferObjectNonInterleavedRenderStrategy.class);
        container.addComponent(VertexBufferObjectNonInterleavedUpdateStrategy.class);

        // Renderable VA
        container.addComponent(VABuilder.class);
        container.addComponent(VertexArrays.class);

        // Renderable VA factory
        container.as(Characteristics.USE_NAMES).addComponent(VADrawCallFactory.class);

        // Renderable VBO
        container.addComponent(ByteBuffers.class);
        container.addComponent(VBOBuilder.class);

        // Renderable VBO factory
        container.as(Characteristics.USE_NAMES).addComponent(VBODrawCallFactory.class);

        // Renderers
        container.addComponent(GLFontRenderer.class);
        container.addComponent(RendererGL1.class);
        container.addComponent(RendererGL2.class);
        container.addComponent(RendererGL3.class);

        // Shader
        container.addComponent(DefaultAttribLocations.class);
        container.addComponent(ShaderBuilder.class);

        // Texture
        container.addComponent(TextureBuilder.class);
        container.addComponent(TextureReader.class);

        return container;
    }

    protected void createStaticFramework(MutablePicoContainer container) {
        Fw.app = container.getComponent(Application.class);
        Fw.audio = container.getComponent(Audio.class);
        Fw.config = container.getComponent(Configuration.class);
        Fw.files = container.getComponent(Files.class);
        Fw.gof = container.getComponent(GameObjectFactory.class);
        Fw.graphics = container.getComponent(Graphics.class);
        Fw.input = container.getComponent(Input.class);
        Fw.net = container.getComponent(Network.class);
        Fw.timer = container.getComponent(Timer.class);
        Fw.window = container.getComponent(Window.class);
    }

    protected void createStaticOpenGL(MutablePicoContainer container) {
        GL.bufferHelper = container.getComponent(BufferHelper.class);
        GL.cameraFactory = container.getComponent(CameraFactory.class);
        GL.defaultAttribLocations = container.getComponent(DefaultAttribLocations.class);
        GL.displayListHelper = container.getComponent(DisplayListHelper.class);
        GL.dynamicMeshBuilder = container.getComponent(DynamicMeshBuilder.class);
        GL.fboHelper = container.getComponent(FrameBufferObjectHelper.class);
        GL.glFactory = container.getComponent(GLFactory.class);
        GL.glFontBuilder = container.getComponent(GLFontBuilder.class);
        GL.glFontRenderer = container.getComponent(GLFontRenderer.class);
        GL.matrixHelper = container.getComponent(MatrixHelper.class);
        GL.meshReader = container.getComponent(MeshReader.class);
        GL.meshFactory = container.getComponent(MeshFactory.class);
        GL.o1 = container.getComponent(OpenGL1.class);
        GL.o2 = container.getComponent(OpenGL2.class);
        GL.o3 = container.getComponent(OpenGL3.class);
        GL.rf1 = container.getComponent(RenderableFactoryGL1.class);
        GL.rf2 = container.getComponent(RenderableFactoryGL2.class);
        GL.rf3 = container.getComponent(RenderableFactoryGL3.class);
        GL.renderer1 = container.getComponent(RendererGL1.class);
        GL.renderer2 = container.getComponent(RendererGL2.class);
        GL.renderer3 = container.getComponent(RendererGL3.class);
        GL.rboHelper = container.getComponent(RenderBufferObjectHelper.class);
        GL.renderHelper = container.getComponent(LegacyRenderHelper.class);
        GL.shaderBuilder = container.getComponent(ShaderBuilder.class);
        GL.shaderHelper = container.getComponent(ShaderHelper.class);
        GL.shaderUniformHelper = container.getComponent(ShaderUniformHelper.class);
        GL.tessellator = container.getComponent(Tessellator.class);
        GL.textureBuilder = container.getComponent(TextureBuilder.class);
        GL.textureHelper = container.getComponent(TextureHelper.class);
        GL.textureReader = container.getComponent(TextureReader.class);
        GL.vaHelper = container.getComponent(VertexArrayHelper.class);
        GL.vboHelper = container.getComponent(VertexBufferObjectHelper.class);
    }

    protected void createStaticRenderable(MutablePicoContainer container) {
        Renderable.bindState1 = container.getComponent(BindStateGL1.class);
        Renderable.bindState2 = container.getComponent(BindStateGL2.class);
        Renderable.byteBuffers = container.getComponent(ByteBuffers.class);
        Renderable.configPool = container.getComponent(RenderableConfigurationPool.class);
        Renderable.drawCommandsConversion = container.getComponent(DrawCommandsConversion.class);
        Renderable.shaderBindState = container.getComponent(ShaderBindState.class);
        Renderable.shaderPool = container.getComponent(ShaderPool.class);
        Renderable.textureBindState = container.getComponent(TextureBindState.class);
        Renderable.texturePool = container.getComponent(TexturePool.class);
        Renderable.vaBuilder = container.getComponent(VABuilder.class);
        Renderable.vboBuilder = container.getComponent(VBOBuilder.class);
        Renderable.vertexArrays = container.getComponent(VertexArrays.class);
    }

    protected void initialize(MutablePicoContainer container) {

        // Create instances
        DynamicMeshBuilder dynamicMeshBuilder = container.getComponent(DynamicMeshBuilder.class);
        MeshReader meshReader = container.getComponent(MeshReader.class);
        TextureReader textureReader = container.getComponent(TextureReader.class);

        // Register dynamic meshes
        dynamicMeshBuilder.register("aabb", GL.meshFactory.createDynamicAABB());
        dynamicMeshBuilder.register("capsule", GL.meshFactory.createDynamicPrimitiveCapsule());
        dynamicMeshBuilder.register("cone", GL.meshFactory.createDynamicPrimitiveCone());
        dynamicMeshBuilder.register("cube", GL.meshFactory.createDynamicPrimitiveCube());
        dynamicMeshBuilder.register("cube_map", GL.meshFactory.createDynamicCubeMap());
        dynamicMeshBuilder.register("cylinder", GL.meshFactory.createDynamicPrimitiveCylinder());
        dynamicMeshBuilder.register("frustum", GL.meshFactory.createDynamicPrimitiveFrustum());
        dynamicMeshBuilder.register("grid", GL.meshFactory.createDynamicPrimitiveGrid());
        dynamicMeshBuilder.register("pyramid", GL.meshFactory.createDynamicPrimitivePyramid());
        dynamicMeshBuilder.register("quad", GL.meshFactory.createDynamicPrimitiveQuad());
        dynamicMeshBuilder.register("simple_orientation_axis", GL.meshFactory.createDynamicSimpleOrientationAxis());
        dynamicMeshBuilder.register("sphere", GL.meshFactory.createDynamicPrimitiveSphere());
        dynamicMeshBuilder.register("toriod", GL.meshFactory.createDynamicPrimitiveToriod());
        dynamicMeshBuilder.register("view_frustum", GL.meshFactory.createDynamicViewFrustum());

        // Register extensions
        meshReader.registerExtension("obj", container.getComponent(ObjReader.class));
        textureReader.registerExtension("bmp", container.getComponent(BMPReader.class));
        textureReader.registerExtension("gif", container.getComponent(GIFReader.class));
        textureReader.registerExtension("jpeg", container.getComponent(JPGReader.class));
        textureReader.registerExtension("jpg", container.getComponent(JPGReader.class));
        textureReader.registerExtension("png", container.getComponent(PNGReader.class));
        textureReader.registerExtension("tga", container.getComponent(TGAReader.class));
        textureReader.registerExtension("tif", container.getComponent(TIFFReader.class));
        textureReader.registerExtension("tiff", container.getComponent(TIFFReader.class));
    }

    protected void modifyDependencies(MutablePicoContainer container) {
    }

    public void start(Class configClass, Class screenClass, String[] args) {

        // Container creation
        MutablePicoContainer container = createDependencies(configClass, screenClass, args);
        addContainersForFactories(container);

        // Classes may be instantiated past this point
        modifyDependencies(container);
        createStaticFramework(container);
        createStaticOpenGL(container);
        createStaticRenderable(container);
        initialize(container);

        // Start the application
        try {
            Fw.app.run(container.getComponent(Screen.class));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
