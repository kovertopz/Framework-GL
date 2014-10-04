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
package net.smert.jreactphysics3d.framework;

import java.io.IOException;
import java.util.logging.SimpleFormatter;
import net.smert.jreactphysics3d.framework.factory.GameObjectFactory;
import net.smert.jreactphysics3d.framework.helpers.KeyboardHelper;
import net.smert.jreactphysics3d.framework.helpers.MouseHelper;
import net.smert.jreactphysics3d.framework.opengl.AmbientLight;
import net.smert.jreactphysics3d.framework.opengl.DisplayList;
import net.smert.jreactphysics3d.framework.opengl.FrameBufferObject;
import net.smert.jreactphysics3d.framework.opengl.GL;
import net.smert.jreactphysics3d.framework.opengl.Light;
import net.smert.jreactphysics3d.framework.opengl.MaterialLight;
import net.smert.jreactphysics3d.framework.opengl.OpenGL1;
import net.smert.jreactphysics3d.framework.opengl.OpenGL2;
import net.smert.jreactphysics3d.framework.opengl.OpenGL3;
import net.smert.jreactphysics3d.framework.opengl.RenderBufferObject;
import net.smert.jreactphysics3d.framework.opengl.Shader;
import net.smert.jreactphysics3d.framework.opengl.Shadow;
import net.smert.jreactphysics3d.framework.opengl.Texture;
import net.smert.jreactphysics3d.framework.opengl.VertexArray;
import net.smert.jreactphysics3d.framework.opengl.VertexArrayObject;
import net.smert.jreactphysics3d.framework.opengl.VertexBufferObject;
import net.smert.jreactphysics3d.framework.opengl.VertexBufferObjectInterleaved;
import net.smert.jreactphysics3d.framework.opengl.camera.Camera;
import net.smert.jreactphysics3d.framework.opengl.camera.LegacyCamera;
import net.smert.jreactphysics3d.framework.opengl.camera.LegacyCameraController;
import net.smert.jreactphysics3d.framework.opengl.camera.factory.CameraFactory;
import net.smert.jreactphysics3d.framework.opengl.factory.GLFactory;
import net.smert.jreactphysics3d.framework.opengl.helpers.BufferHelper;
import net.smert.jreactphysics3d.framework.opengl.helpers.DisplayListHelper;
import net.smert.jreactphysics3d.framework.opengl.helpers.FrameBufferObjectHelper;
import net.smert.jreactphysics3d.framework.opengl.helpers.LegacyRenderHelper;
import net.smert.jreactphysics3d.framework.opengl.helpers.RenderBufferObjectHelper;
import net.smert.jreactphysics3d.framework.opengl.helpers.ShaderHelper;
import net.smert.jreactphysics3d.framework.opengl.helpers.TextureHelper;
import net.smert.jreactphysics3d.framework.opengl.helpers.VertexArrayHelper;
import net.smert.jreactphysics3d.framework.opengl.helpers.VertexBufferObjectHelper;
import net.smert.jreactphysics3d.framework.opengl.image.bmp.BMPReader;
import net.smert.jreactphysics3d.framework.opengl.image.gif.GIFReader;
import net.smert.jreactphysics3d.framework.opengl.image.jpg.JPGReader;
import net.smert.jreactphysics3d.framework.opengl.image.png.PNGReader;
import net.smert.jreactphysics3d.framework.opengl.image.tga.TGAImage;
import net.smert.jreactphysics3d.framework.opengl.image.tga.TGAReader;
import net.smert.jreactphysics3d.framework.opengl.image.tiff.TIFFReader;
import net.smert.jreactphysics3d.framework.opengl.mesh.DrawCommandsConversion;
import net.smert.jreactphysics3d.framework.opengl.mesh.Material;
import net.smert.jreactphysics3d.framework.opengl.mesh.Mesh;
import net.smert.jreactphysics3d.framework.opengl.mesh.MeshReader;
import net.smert.jreactphysics3d.framework.opengl.mesh.Segment;
import net.smert.jreactphysics3d.framework.opengl.mesh.factory.MeshFactory;
import net.smert.jreactphysics3d.framework.opengl.model.obj.MaterialReader;
import net.smert.jreactphysics3d.framework.opengl.model.obj.ObjReader;
import net.smert.jreactphysics3d.framework.opengl.renderable.factory.Renderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.factory.RenderableConfiguration;
import net.smert.jreactphysics3d.framework.opengl.renderable.factory.RenderableFactoryGL1;
import net.smert.jreactphysics3d.framework.opengl.renderable.gl1.DisplayListRenderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.gl1.ImmediateModeRenderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.gl1.VertexArrayRenderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.gl1.VertexBufferObjectRenderable;
import net.smert.jreactphysics3d.framework.opengl.renderable.gl1.VertexBufferObjectRenderableInterleaved;
import net.smert.jreactphysics3d.framework.opengl.renderable.shared.ShaderBindState;
import net.smert.jreactphysics3d.framework.opengl.renderable.shared.ShaderPool;
import net.smert.jreactphysics3d.framework.opengl.renderable.shared.TextureBindState;
import net.smert.jreactphysics3d.framework.opengl.renderable.shared.TexturePool;
import net.smert.jreactphysics3d.framework.opengl.renderable.va.VABindState;
import net.smert.jreactphysics3d.framework.opengl.renderable.va.VABuilder;
import net.smert.jreactphysics3d.framework.opengl.renderable.va.VADrawArrays;
import net.smert.jreactphysics3d.framework.opengl.renderable.va.VADrawElements;
import net.smert.jreactphysics3d.framework.opengl.renderable.va.VertexArrays;
import net.smert.jreactphysics3d.framework.opengl.renderable.va.factory.VADrawCallFactory;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.ByteBuffers;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.VBOBindState;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.VBOBuilder;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.VBODrawArrays;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.VBODrawElements;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.VBODrawRangeElements;
import net.smert.jreactphysics3d.framework.opengl.renderable.vbo.factory.VBODrawCallFactory;
import net.smert.jreactphysics3d.framework.opengl.texture.TextureBuilder;
import net.smert.jreactphysics3d.framework.opengl.texture.TextureReader;
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
            glFactoryContainer.addComponent(Light.class);
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
            meshFactoryContainer.addComponent(Segment.class);

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
            renderableFactoryGL1Container.addComponent(VertexBufferObjectRenderable.class);
            renderableFactoryGL1Container.addComponent(VertexBufferObjectRenderableInterleaved.class);

            // Add container for RenderableFactoryGL1
            parentContainer.addComponent("renderableFactoryGL1Container", renderableFactoryGL1Container);
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

        // OpenGL helpers
        container.addComponent(BufferHelper.class);
        container.addComponent(DisplayListHelper.class);
        container.addComponent(FrameBufferObjectHelper.class);
        container.addComponent(LegacyRenderHelper.class);
        container.addComponent(RenderBufferObjectHelper.class);
        container.addComponent(ShaderHelper.class);
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
        container.addComponent(MeshReader.class);

        // Mesh factory
        container.as(Characteristics.USE_NAMES).addComponent(MeshFactory.class);

        // Model
        container.addComponent(MaterialReader.class);
        container.addComponent(ObjReader.class);

        // Renderable factory
        container.addComponent(RenderableConfiguration.class);
        container.as(Characteristics.USE_NAMES).addComponent(RenderableFactoryGL1.class);

        // Renderable shared
        container.addComponent(ShaderBindState.class);
        container.addComponent(ShaderPool.class);
        container.addComponent(TextureBindState.class);
        container.addComponent(TexturePool.class);

        // Renderable VA
        container.addComponent(VABindState.class);
        container.addComponent(VABuilder.class);
        container.addComponent(VertexArrays.class);

        // Renderable VA factory
        container.as(Characteristics.USE_NAMES).addComponent(VADrawCallFactory.class);

        // Renderable VBO
        container.addComponent(ByteBuffers.class);
        container.addComponent(VBOBindState.class);
        container.addComponent(VBOBuilder.class);

        // Renderable VBO factory
        container.as(Characteristics.USE_NAMES).addComponent(VBODrawCallFactory.class);

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
        GL.cf = container.getComponent(CameraFactory.class);
        GL.displayListHelper = container.getComponent(DisplayListHelper.class);
        GL.fboHelper = container.getComponent(FrameBufferObjectHelper.class);
        GL.glf = container.getComponent(GLFactory.class);
        GL.meshReader = container.getComponent(MeshReader.class);
        GL.mf = container.getComponent(MeshFactory.class);
        GL.o1 = container.getComponent(OpenGL1.class);
        GL.o2 = container.getComponent(OpenGL2.class);
        GL.o3 = container.getComponent(OpenGL3.class);
        GL.rf1 = container.getComponent(RenderableFactoryGL1.class);
        GL.rboHelper = container.getComponent(RenderBufferObjectHelper.class);
        GL.renderHelper = container.getComponent(LegacyRenderHelper.class);
        GL.shaderHelper = container.getComponent(ShaderHelper.class);
        GL.textureBuilder = container.getComponent(TextureBuilder.class);
        GL.textureHelper = container.getComponent(TextureHelper.class);
        GL.textureReader = container.getComponent(TextureReader.class);
        GL.vaHelper = container.getComponent(VertexArrayHelper.class);
        GL.vboHelper = container.getComponent(VertexBufferObjectHelper.class);
    }

    protected void createStaticRenderable(MutablePicoContainer container) {
        Renderable.byteBuffers = container.getComponent(ByteBuffers.class);
        Renderable.drawCommandsConversion = container.getComponent(DrawCommandsConversion.class);
        Renderable.config = container.getComponent(RenderableConfiguration.class);
        Renderable.shaderBindState = container.getComponent(ShaderBindState.class);
        Renderable.shaderPool = container.getComponent(ShaderPool.class);
        Renderable.textureBindState = container.getComponent(TextureBindState.class);
        Renderable.texturePool = container.getComponent(TexturePool.class);
        Renderable.vaBindState = container.getComponent(VABindState.class);
        Renderable.vaBuilder = container.getComponent(VABuilder.class);
        Renderable.vboBindState = container.getComponent(VBOBindState.class);
        Renderable.vboBuilder = container.getComponent(VBOBuilder.class);
        Renderable.vertexArrays = container.getComponent(VertexArrays.class);
    }

    protected void initialize(MutablePicoContainer container) {

        // Create instances
        MeshReader meshReader = container.getComponent(MeshReader.class);
        TextureReader textureReader = container.getComponent(TextureReader.class);

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
        initialize(container);
        createStaticFramework(container);
        createStaticOpenGL(container);
        createStaticRenderable(container);

        // Start the application
        try {
            Fw.app.run(container.getComponent(Screen.class));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
