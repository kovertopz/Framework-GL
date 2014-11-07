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
import net.smert.frameworkgl.gui.InputSystem;
import net.smert.frameworkgl.gui.RenderDevice;
import net.smert.frameworkgl.gui.SoundDevice;
import net.smert.frameworkgl.gui.TimeProvider;
import net.smert.frameworkgl.gui.factory.GUIFactory;
import net.smert.frameworkgl.helpers.KeyboardHelper;
import net.smert.frameworkgl.helpers.MouseHelper;
import net.smert.frameworkgl.openal.AL;
import net.smert.frameworkgl.openal.OpenAL;
import net.smert.frameworkgl.openal.OpenALBuffer;
import net.smert.frameworkgl.openal.OpenALListener;
import net.smert.frameworkgl.openal.OpenALSource;
import net.smert.frameworkgl.openal.codecs.midi.MIDICodec;
import net.smert.frameworkgl.openal.codecs.mp3.MP3Codec;
import net.smert.frameworkgl.openal.codecs.ogg.OGGCodec;
import net.smert.frameworkgl.openal.codecs.wav.WAVCodec;
import net.smert.frameworkgl.openal.factory.ALFactory;
import net.smert.frameworkgl.openal.helpers.ALBufferHelper;
import net.smert.frameworkgl.openal.helpers.ALListenerHelper;
import net.smert.frameworkgl.openal.helpers.ALSourceHelper;
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
import net.smert.frameworkgl.opengl.fbo.FrameBufferObjectBuilder;
import net.smert.frameworkgl.opengl.font.AngelCodeFontBuilder;
import net.smert.frameworkgl.opengl.font.AwtFontBuilder;
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
import net.smert.frameworkgl.opengl.helpers.VertexArrayObjectHelper;
import net.smert.frameworkgl.opengl.helpers.VertexBufferObjectHelper;
import net.smert.frameworkgl.opengl.image.bmp.BMPReader;
import net.smert.frameworkgl.opengl.image.gif.GIFReader;
import net.smert.frameworkgl.opengl.image.jpg.JPGReader;
import net.smert.frameworkgl.opengl.image.png.PNGReader;
import net.smert.frameworkgl.opengl.image.tga.TGAReader;
import net.smert.frameworkgl.opengl.image.tiff.TIFFReader;
import net.smert.frameworkgl.opengl.mesh.DrawCommandsConversion;
import net.smert.frameworkgl.opengl.mesh.DynamicMeshBuilder;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.mesh.MeshMaterial;
import net.smert.frameworkgl.opengl.mesh.MeshReader;
import net.smert.frameworkgl.opengl.mesh.Segment;
import net.smert.frameworkgl.opengl.mesh.SegmentMaterial;
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
import net.smert.frameworkgl.opengl.pipeline.DeferredLightingPipeline;
import net.smert.frameworkgl.opengl.pipeline.DeferredRenderingPipeline;
import net.smert.frameworkgl.opengl.pipeline.ForwardRenderingPipeline;
import net.smert.frameworkgl.opengl.pipeline.factory.RenderingPipelineFactory;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.renderable.RenderableConfiguration;
import net.smert.frameworkgl.opengl.renderable.displaylist.DisplayListRenderCall;
import net.smert.frameworkgl.opengl.renderable.displaylist.DisplayListRenderCallBuilder;
import net.smert.frameworkgl.opengl.renderable.displaylist.factory.DisplayListRenderCallFactory;
import net.smert.frameworkgl.opengl.renderable.factory.RenderableFactoryGL1;
import net.smert.frameworkgl.opengl.renderable.factory.RenderableFactoryGL2;
import net.smert.frameworkgl.opengl.renderable.factory.RenderableFactoryGL3;
import net.smert.frameworkgl.opengl.renderable.gl1.DisplayListGL1Renderable;
import net.smert.frameworkgl.opengl.renderable.gl1.DynamicVertexArrayGL1Renderable;
import net.smert.frameworkgl.opengl.renderable.gl1.DynamicVertexBufferObjectInterleavedGL1Renderable;
import net.smert.frameworkgl.opengl.renderable.gl1.DynamicVertexBufferObjectNonInterleavedGL1Renderable;
import net.smert.frameworkgl.opengl.renderable.gl1.ImmediateModeGL1Renderable;
import net.smert.frameworkgl.opengl.renderable.gl1.VertexArrayGL1Renderable;
import net.smert.frameworkgl.opengl.renderable.gl1.VertexBufferObjectInterleavedGL1Renderable;
import net.smert.frameworkgl.opengl.renderable.gl1.VertexBufferObjectNonInterleavedGL1Renderable;
import net.smert.frameworkgl.opengl.renderable.gl2.DynamicVertexArrayGL2Renderable;
import net.smert.frameworkgl.opengl.renderable.gl2.DynamicVertexBufferObjectInterleavedGL2Renderable;
import net.smert.frameworkgl.opengl.renderable.gl2.DynamicVertexBufferObjectNonInterleavedGL2Renderable;
import net.smert.frameworkgl.opengl.renderable.gl2.VertexArrayGL2Renderable;
import net.smert.frameworkgl.opengl.renderable.gl2.VertexBufferObjectInterleavedGL2Renderable;
import net.smert.frameworkgl.opengl.renderable.gl2.VertexBufferObjectNonInterleavedGL2Renderable;
import net.smert.frameworkgl.opengl.renderable.gl3.DynamicVertexArrayObjectInterleavedGL3Renderable;
import net.smert.frameworkgl.opengl.renderable.gl3.DynamicVertexArrayObjectNonInterleavedGL3Renderable;
import net.smert.frameworkgl.opengl.renderable.gl3.VertexArrayObjectInterleavedGL3Renderable;
import net.smert.frameworkgl.opengl.renderable.gl3.VertexArrayObjectNonInterleavedGL3Renderable;
import net.smert.frameworkgl.opengl.renderable.immediatemode.ImmediateModeRenderCall;
import net.smert.frameworkgl.opengl.renderable.immediatemode.ImmediateModeRenderCallBuilder;
import net.smert.frameworkgl.opengl.renderable.immediatemode.factory.ImmediateModeRenderCallFactory;
import net.smert.frameworkgl.opengl.renderable.shared.BindState;
import net.smert.frameworkgl.opengl.renderable.shared.MaterialLightPool;
import net.smert.frameworkgl.opengl.renderable.shared.RenderableBuilder;
import net.smert.frameworkgl.opengl.renderable.shared.RenderableConfigurationPool;
import net.smert.frameworkgl.opengl.renderable.shared.ShaderBindState;
import net.smert.frameworkgl.opengl.renderable.shared.ShaderPool;
import net.smert.frameworkgl.opengl.renderable.shared.TextureBindState;
import net.smert.frameworkgl.opengl.renderable.shared.TexturePool;
import net.smert.frameworkgl.opengl.renderable.va.VADrawArrays;
import net.smert.frameworkgl.opengl.renderable.va.VADrawCallBuilder;
import net.smert.frameworkgl.opengl.renderable.va.VADrawElements;
import net.smert.frameworkgl.opengl.renderable.va.VertexArrays;
import net.smert.frameworkgl.opengl.renderable.va.factory.VADrawCallFactory;
import net.smert.frameworkgl.opengl.renderable.vbo.ByteBuffers;
import net.smert.frameworkgl.opengl.renderable.vbo.VBODrawArrays;
import net.smert.frameworkgl.opengl.renderable.vbo.VBODrawCallBuilder;
import net.smert.frameworkgl.opengl.renderable.vbo.VBODrawElements;
import net.smert.frameworkgl.opengl.renderable.vbo.VBODrawRangeElements;
import net.smert.frameworkgl.opengl.renderable.vbo.factory.VBODrawCallFactory;
import net.smert.frameworkgl.opengl.renderer.AngelCodeFontRenderer;
import net.smert.frameworkgl.opengl.renderer.AwtFontRenderer;
import net.smert.frameworkgl.opengl.renderer.NiftyGuiRenderer;
import net.smert.frameworkgl.opengl.renderer.RendererGL1;
import net.smert.frameworkgl.opengl.renderer.RendererGL2;
import net.smert.frameworkgl.opengl.renderer.RendererGL3;
import net.smert.frameworkgl.opengl.renderer.factory.RendererFactory;
import net.smert.frameworkgl.opengl.shader.DefaultAttribLocations;
import net.smert.frameworkgl.opengl.shader.ShaderBuilder;
import net.smert.frameworkgl.opengl.shader.UniformVariables;
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

    protected MutablePicoContainer alFactoryContainer;
    protected MutablePicoContainer cameraFactoryContainer;
    protected MutablePicoContainer gameObjectFactoryContainer;
    protected MutablePicoContainer displayListRenderCallFactoryContainer;
    protected MutablePicoContainer glFactoryContainer;
    protected MutablePicoContainer guiFactoryContainer;
    protected MutablePicoContainer immediateModeRenderCallFactoryContainer;
    protected MutablePicoContainer meshFactoryContainer;
    protected MutablePicoContainer renderableFactoryGL1Container;
    protected MutablePicoContainer renderableFactoryGL2Container;
    protected MutablePicoContainer renderableFactoryGL3Container;
    protected MutablePicoContainer rendererFactoryContainer;
    protected MutablePicoContainer renderingPipelineFactoryContainer;
    protected MutablePicoContainer vaDrawCallFactoryContainer;
    protected MutablePicoContainer vboDrawCallFactoryContainer;

    private void addContainersForFactories(MutablePicoContainer parentContainer) {

        {
            // Container for GameObjectFactory
            gameObjectFactoryContainer = new PicoBuilder(parentContainer)
                    .withConstructorInjection().build(); // NO caching!

            // Framework multiple instance components
            gameObjectFactoryContainer.addComponent(GameObject.class);

            // Add container for GameObjectFactory
            parentContainer.addComponent("gameObjectFactoryContainer", gameObjectFactoryContainer);
        }

        {
            // Container for GUIFactory
            guiFactoryContainer = new PicoBuilder(parentContainer)
                    .withConstructorInjection().build(); // NO caching!

            // GUI
            guiFactoryContainer.addComponent(InputSystem.class);
            guiFactoryContainer.addComponent(RenderDevice.class);
            guiFactoryContainer.addComponent(SoundDevice.class);
            guiFactoryContainer.addComponent(TimeProvider.class);

            // Add container for CameraFactory
            parentContainer.addComponent("guiFactoryContainer", guiFactoryContainer);
        }

        {
            // Container for ALFactory
            alFactoryContainer = new PicoBuilder(parentContainer)
                    .withConstructorInjection().build(); // NO caching!

            // OpenAL multiple instance components
            alFactoryContainer.addComponent(OpenALBuffer.class);
            alFactoryContainer.addComponent(OpenALListener.class);
            alFactoryContainer.addComponent(OpenALSource.class);

            // Add container for ALFactory
            parentContainer.addComponent("alFactoryContainer", alFactoryContainer);
        }

        {
            // Container for GLFactory
            glFactoryContainer = new PicoBuilder(parentContainer)
                    .withConstructorInjection().build(); // NO caching!

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
            cameraFactoryContainer = new PicoBuilder(parentContainer)
                    .withConstructorInjection().build(); // NO caching!

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
            meshFactoryContainer = new PicoBuilder(parentContainer)
                    .withConstructorInjection().build(); // NO caching!

            // Mesh
            meshFactoryContainer.addComponent(Mesh.class);
            meshFactoryContainer.addComponent(MeshMaterial.class);
            meshFactoryContainer.addComponent(RenderableConfiguration.class);
            meshFactoryContainer.addComponent(Segment.class);
            meshFactoryContainer.addComponent(SegmentMaterial.class);
            meshFactoryContainer.addComponent(Tessellator.class);

            // Mesh dynamic
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

            // Add container for MeshFactory
            parentContainer.addComponent("meshFactoryContainer", meshFactoryContainer);
        }

        {
            // Container for RenderingPipelineFactory
            renderingPipelineFactoryContainer = new PicoBuilder(parentContainer)
                    .withConstructorInjection().build(); // NO caching!

            // Pipeline
            renderingPipelineFactoryContainer.addComponent(DeferredLightingPipeline.class);
            renderingPipelineFactoryContainer.addComponent(DeferredRenderingPipeline.class);
            renderingPipelineFactoryContainer.addComponent(ForwardRenderingPipeline.class);

            // Add container for RenderingPipelineFactory
            parentContainer.addComponent("renderingPipelineFactoryContainer", renderingPipelineFactoryContainer);
        }

        {
            // Container for RenderableFactoryGL1
            renderableFactoryGL1Container = new PicoBuilder(parentContainer)
                    .withConstructorInjection().build(); // NO caching!

            // Renderable
            renderableFactoryGL1Container.addComponent(DisplayListGL1Renderable.class);
            renderableFactoryGL1Container.addComponent(DynamicVertexArrayGL1Renderable.class);
            renderableFactoryGL1Container.addComponent(DynamicVertexBufferObjectInterleavedGL1Renderable.class);
            renderableFactoryGL1Container.addComponent(DynamicVertexBufferObjectNonInterleavedGL1Renderable.class);
            renderableFactoryGL1Container.addComponent(ImmediateModeGL1Renderable.class);
            renderableFactoryGL1Container.addComponent(VertexArrayGL1Renderable.class);
            renderableFactoryGL1Container.addComponent(VertexBufferObjectInterleavedGL1Renderable.class);
            renderableFactoryGL1Container.addComponent(VertexBufferObjectNonInterleavedGL1Renderable.class);

            // Add container for RenderableFactoryGL1
            parentContainer.addComponent("renderableFactoryGL1Container", renderableFactoryGL1Container);
        }

        {
            // Container for RenderableFactoryGL2
            renderableFactoryGL2Container = new PicoBuilder(parentContainer)
                    .withConstructorInjection().build(); // NO caching!

            // Renderable
            renderableFactoryGL2Container.addComponent(DynamicVertexArrayGL2Renderable.class);
            renderableFactoryGL2Container.addComponent(DynamicVertexBufferObjectInterleavedGL2Renderable.class);
            renderableFactoryGL2Container.addComponent(DynamicVertexBufferObjectNonInterleavedGL2Renderable.class);
            renderableFactoryGL2Container.addComponent(VertexArrayGL2Renderable.class);
            renderableFactoryGL2Container.addComponent(VertexBufferObjectInterleavedGL2Renderable.class);
            renderableFactoryGL2Container.addComponent(VertexBufferObjectNonInterleavedGL2Renderable.class);

            // Add container for RenderableFactoryGL2
            parentContainer.addComponent("renderableFactoryGL2Container", renderableFactoryGL2Container);
        }

        {
            // Container for RenderableFactoryGL3
            renderableFactoryGL3Container = new PicoBuilder(parentContainer)
                    .withConstructorInjection().build(); // NO caching!

            // Renderable
            renderableFactoryGL3Container.addComponent(DynamicVertexArrayObjectInterleavedGL3Renderable.class);
            renderableFactoryGL3Container.addComponent(DynamicVertexArrayObjectNonInterleavedGL3Renderable.class);
            renderableFactoryGL3Container.addComponent(VertexArrayObjectInterleavedGL3Renderable.class);
            renderableFactoryGL3Container.addComponent(VertexArrayObjectNonInterleavedGL3Renderable.class);

            // Add container for RenderableFactoryGL3
            parentContainer.addComponent("renderableFactoryGL3Container", renderableFactoryGL3Container);
        }

        {
            // Container for RendererFactory
            rendererFactoryContainer = new PicoBuilder(parentContainer)
                    .withConstructorInjection().build(); // NO caching!

            // Renderer
            rendererFactoryContainer.addComponent(AngelCodeFontRenderer.class);
            rendererFactoryContainer.addComponent(AwtFontRenderer.class);
            rendererFactoryContainer.addComponent(NiftyGuiRenderer.class);

            // Add container for RendererFactory
            parentContainer.addComponent("rendererFactoryContainer", rendererFactoryContainer);
        }

        {
            // Container for DisplayListRenderCallFactory
            displayListRenderCallFactoryContainer = new PicoBuilder(parentContainer)
                    .withConstructorInjection().build(); // NO caching!

            // Renderable display list
            displayListRenderCallFactoryContainer.addComponent(DisplayListRenderCall.class);

            // Add container for DisplayListRenderCallFactory
            parentContainer.addComponent("displayListRenderCallFactoryContainer",
                    displayListRenderCallFactoryContainer);
        }

        {
            // Container for ImmediateModeRenderCallFactory
            immediateModeRenderCallFactoryContainer = new PicoBuilder(parentContainer)
                    .withConstructorInjection().build(); // NO caching!

            // Renderable immediate mode
            immediateModeRenderCallFactoryContainer.addComponent(ImmediateModeRenderCall.class);

            // Add container for ImmediateModeRenderCallFactory
            parentContainer.addComponent("immediateModeRenderCallFactoryContainer",
                    immediateModeRenderCallFactoryContainer);
        }

        {
            // Container for VADrawCallFactory
            vaDrawCallFactoryContainer = new PicoBuilder(parentContainer)
                    .withConstructorInjection().build(); // NO caching!

            // Renderable VA
            vaDrawCallFactoryContainer.addComponent(VADrawArrays.class);
            vaDrawCallFactoryContainer.addComponent(VADrawElements.class);

            // Add container for VADrawCallFactory
            parentContainer.addComponent("vaDrawCallFactoryContainer", vaDrawCallFactoryContainer);
        }

        {
            // Container for VBODrawCallFactory
            vboDrawCallFactoryContainer = new PicoBuilder(parentContainer)
                    .withConstructorInjection().build(); // NO caching!

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
        container.addComponent(GUI.class);
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
        container.as(Characteristics.USE_NAMES).addComponent(GUIFactory.class);

        // Framework helpers
        container.addComponent(KeyboardHelper.class);
        container.addComponent(MouseHelper.class);

        // OpenAL components
        container.addComponent(OpenAL.class);

        // Codecs
        container.addComponent(MIDICodec.class);
        container.addComponent(MP3Codec.class);
        container.addComponent(OGGCodec.class);
        container.addComponent(WAVCodec.class);

        // Factory
        container.as(Characteristics.USE_NAMES).addComponent(ALFactory.class);

        // Helpers
        container.addComponent(ALBufferHelper.class);
        container.addComponent(ALListenerHelper.class);
        container.addComponent(ALSourceHelper.class);

        // OpenGL Components
        container.addComponent(OpenGL1.class);
        container.addComponent(OpenGL2.class);
        container.addComponent(OpenGL3.class);

        // Camera factory
        container.as(Characteristics.USE_NAMES).addComponent(CameraFactory.class);

        // Factory
        container.as(Characteristics.USE_NAMES).addComponent(GLFactory.class);

        // FBO
        container.addComponent(FrameBufferObjectBuilder.class);

        // Font
        container.addComponent(AngelCodeFontBuilder.class);
        container.addComponent(AwtFontBuilder.class);

        // Helpers
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
        container.addComponent(VertexArrayObjectHelper.class);
        container.addComponent(VertexBufferObjectHelper.class);

        // Image
        container.addComponent(BMPReader.class);
        container.addComponent(GIFReader.class);
        container.addComponent(JPGReader.class);
        container.addComponent(PNGReader.class);
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

        // Pipeline factory
        container.as(Characteristics.USE_NAMES).addComponent(RenderingPipelineFactory.class);

        // Renderable factory
        container.as(Characteristics.USE_NAMES).addComponent(RenderableFactoryGL1.class);
        container.as(Characteristics.USE_NAMES).addComponent(RenderableFactoryGL2.class);
        container.as(Characteristics.USE_NAMES).addComponent(RenderableFactoryGL3.class);

        // Renderable display list
        container.addComponent(DisplayListRenderCallBuilder.class);

        // Renderable display list factory
        container.as(Characteristics.USE_NAMES).addComponent(DisplayListRenderCallFactory.class);

        // Renderable immediate mode
        container.addComponent(ImmediateModeRenderCallBuilder.class);

        // Renderable immediate mode factory
        container.as(Characteristics.USE_NAMES).addComponent(ImmediateModeRenderCallFactory.class);

        // Renderable shared
        container.addComponent(BindState.class);
        container.addComponent(MaterialLightPool.class);
        container.addComponent(RenderableBuilder.class);
        container.addComponent(RenderableConfigurationPool.class);
        container.addComponent(ShaderBindState.class);
        container.addComponent(ShaderPool.class);
        container.addComponent(TextureBindState.class);
        container.addComponent(TexturePool.class);

        // Renderable VA
        container.addComponent(VADrawCallBuilder.class);
        container.addComponent(VertexArrays.class);

        // Renderable VA factory
        container.as(Characteristics.USE_NAMES).addComponent(VADrawCallFactory.class);

        // Renderable VBO
        container.addComponent(ByteBuffers.class);
        container.addComponent(VBODrawCallBuilder.class);

        // Renderable VBO factory
        container.as(Characteristics.USE_NAMES).addComponent(VBODrawCallFactory.class);

        // Renderers
        container.addComponent(RendererGL1.class);
        container.addComponent(RendererGL2.class);
        container.addComponent(RendererGL3.class);

        // Renderer factory
        container.as(Characteristics.USE_NAMES).addComponent(RendererFactory.class);

        // Shader
        container.addComponent(DefaultAttribLocations.class);
        container.addComponent(ShaderBuilder.class);
        container.addComponent(UniformVariables.class);

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
        Fw.gui = container.getComponent(GUI.class);
        Fw.guiFactory = container.getComponent(GUIFactory.class);
        Fw.input = container.getComponent(Input.class);
        Fw.net = container.getComponent(Network.class);
        Fw.timer = container.getComponent(Timer.class);
        Fw.window = container.getComponent(Window.class);
    }

    protected void createStaticOpenAL(MutablePicoContainer container) {
        AL.alFactory = container.getComponent(ALFactory.class);
        AL.bufferHelper = container.getComponent(ALBufferHelper.class);
        AL.listenerHelper = container.getComponent(ALListenerHelper.class);
        AL.openal = container.getComponent(OpenAL.class);
        AL.sourceHelper = container.getComponent(ALSourceHelper.class);
    }

    protected void createStaticOpenGL(MutablePicoContainer container) {
        GL.angelCodeFontBuilder = container.getComponent(AngelCodeFontBuilder.class);
        GL.awtFontBuilder = container.getComponent(AwtFontBuilder.class);
        GL.bufferHelper = container.getComponent(BufferHelper.class);
        GL.cameraFactory = container.getComponent(CameraFactory.class);
        GL.defaultAttribLocations = container.getComponent(DefaultAttribLocations.class);
        GL.displayListHelper = container.getComponent(DisplayListHelper.class);
        GL.dynamicMeshBuilder = container.getComponent(DynamicMeshBuilder.class);
        GL.fboBuilder = container.getComponent(FrameBufferObjectBuilder.class);
        GL.fboHelper = container.getComponent(FrameBufferObjectHelper.class);
        GL.glFactory = container.getComponent(GLFactory.class);
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
        GL.rendererFactory = container.getComponent(RendererFactory.class);
        GL.rboHelper = container.getComponent(RenderBufferObjectHelper.class);
        GL.renderHelper = container.getComponent(LegacyRenderHelper.class);
        GL.rpFactory = container.getComponent(RenderingPipelineFactory.class);
        GL.shaderBuilder = container.getComponent(ShaderBuilder.class);
        GL.shaderHelper = container.getComponent(ShaderHelper.class);
        GL.shaderUniformHelper = container.getComponent(ShaderUniformHelper.class);
        GL.tessellator = container.getComponent(Tessellator.class);
        GL.textureBuilder = container.getComponent(TextureBuilder.class);
        GL.textureHelper = container.getComponent(TextureHelper.class);
        GL.textureReader = container.getComponent(TextureReader.class);
        GL.uniformVariables = container.getComponent(UniformVariables.class);
        GL.vaHelper = container.getComponent(VertexArrayHelper.class);
        GL.vaoHelper = container.getComponent(VertexArrayObjectHelper.class);
        GL.vboHelper = container.getComponent(VertexBufferObjectHelper.class);
    }

    protected void createStaticRenderable(MutablePicoContainer container) {
        Renderable.bindState = container.getComponent(BindState.class);
        Renderable.byteBuffers = container.getComponent(ByteBuffers.class);
        Renderable.configPool = container.getComponent(RenderableConfigurationPool.class);
        Renderable.displayListRenderCallBuilder = container.getComponent(DisplayListRenderCallBuilder.class);
        Renderable.drawCommandsConversion = container.getComponent(DrawCommandsConversion.class);
        Renderable.immediateModeRenderCallBuilder = container.getComponent(ImmediateModeRenderCallBuilder.class);
        Renderable.materialLightPool = container.getComponent(MaterialLightPool.class);
        Renderable.renderableBuilder = container.getComponent(RenderableBuilder.class);
        Renderable.shaderBindState = container.getComponent(ShaderBindState.class);
        Renderable.shaderPool = container.getComponent(ShaderPool.class);
        Renderable.textureBindState = container.getComponent(TextureBindState.class);
        Renderable.texturePool = container.getComponent(TexturePool.class);
        Renderable.vaDrawCallBuilder = container.getComponent(VADrawCallBuilder.class);
        Renderable.vboDrawCallBuilder = container.getComponent(VBODrawCallBuilder.class);
        Renderable.vertexArrays = container.getComponent(VertexArrays.class);
    }

    protected void initialize(MutablePicoContainer container) {

        // Create instances
        DynamicMeshBuilder dynamicMeshBuilder = container.getComponent(DynamicMeshBuilder.class);
        MeshReader meshReader = container.getComponent(MeshReader.class);
        OpenAL openal = container.getComponent(OpenAL.class);
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

        // Register codecs and set listener
        openal.registerCodec("mid", container.getComponent(MIDICodec.class));
        openal.registerCodec("mp3", container.getComponent(MP3Codec.class));
        openal.registerCodec("ogg", container.getComponent(OGGCodec.class));
        openal.registerCodec("wav", container.getComponent(WAVCodec.class));
        openal.setListener(alFactoryContainer.getComponent(OpenALListener.class));

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
        createStaticOpenAL(container);
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
