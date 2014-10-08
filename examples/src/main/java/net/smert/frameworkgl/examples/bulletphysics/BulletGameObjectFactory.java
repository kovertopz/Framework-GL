package net.smert.frameworkgl.examples.bulletphysics;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btConeShape;
import com.badlogic.gdx.physics.bullet.collision.btCylinderShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.math.Quaternion4f;
import net.smert.frameworkgl.math.Transform4f;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class BulletGameObjectFactory {

    private int uniqueIndex;
    private AbstractRenderable renderableBox;
    private AbstractRenderable renderableCone;
    private AbstractRenderable renderableCapsule;
    private AbstractRenderable renderableCylinder;
    private AbstractRenderable renderableGround;
    private AbstractRenderable renderableSphere;
    private final List<BulletGameObject.Constructor> constructors;
    private final Map<String, Integer> nameToIndex;
    private Mesh meshBox;
    private Mesh meshCone;
    private Mesh meshCapsule;
    private Mesh meshCylinder;
    private Mesh meshGround;
    private Mesh meshSphere;

    public BulletGameObjectFactory() {
        uniqueIndex = 0;
        constructors = new ArrayList<>();
        nameToIndex = new HashMap<>();
    }

    public BulletGameObject.Constructor add(String name, BulletGameObject.Constructor constructor) {
        Integer existingIndex = nameToIndex.get(name);
        if (existingIndex == null) {
            nameToIndex.put(name, uniqueIndex++);
            constructors.add(constructor);
            return null;
        }
        BulletGameObject.Constructor existingConstructor = constructors.get(existingIndex);
        constructors.add(existingIndex, constructor);
        return existingConstructor;
    }

    public BulletGameObject create(String name, Transform4f worldTransform) {
        Integer existingIndex = nameToIndex.get(name);
        if (existingIndex == null) {
            return null;
        }
        BulletGameObject.Constructor constructor = constructors.get(existingIndex);
        Quaternion4f rotation = new Quaternion4f(worldTransform.getRotation());
        Vector3f position = worldTransform.getPosition();
        Quaternion newRotation = new Quaternion(
                rotation.getX(), rotation.getY(), rotation.getZ(), rotation.getW());
        Vector3 newPosition = new Vector3(position.getX(), position.getY(), position.getZ());
        Vector3 newScale = new Vector3(1f, 1f, 1f);
        return constructor.create(new Matrix4(newPosition, newRotation, newScale));
    }

    public void destroy() {
        for (BulletGameObject.Constructor constructor : constructors) {
            constructor.destroy();
        }
    }

    public List<BulletGameObject.Constructor> getConstructors() {
        return constructors;
    }

    public Map<String, Integer> getNameToIndex() {
        return nameToIndex;
    }

    public void init() {

        // Create meshes
        meshBox = GL.mf.createMesh();
        meshCapsule = GL.mf.createMesh();
        meshCone = GL.mf.createMesh();
        meshCylinder = GL.mf.createMesh();
        meshGround = GL.mf.createMesh();
        meshSphere = GL.mf.createMesh();

        // Load the meshes from obj models
        try {
            Fw.graphics.loadMesh("primitives/cube.obj", meshBox);
            meshBox.setAllColors(0f, .5f, 1f, 1f);
            meshBox.updateBooleansFromSegment();
            Fw.graphics.loadMesh("primitives/capsule.obj", meshCapsule);
            meshCapsule.setAllColors(0f, .5f, 1f, 1f);
            meshCapsule.updateBooleansFromSegment();
            Fw.graphics.loadMesh("primitives/cone.obj", meshCone);
            meshCone.setAllColors(0f, .5f, 1f, 1f);
            meshCone.updateBooleansFromSegment();
            Fw.graphics.loadMesh("primitives/cylinder.obj", meshCylinder);
            meshCylinder.setAllColors(0f, .5f, 1f, 1f);
            meshCylinder.updateBooleansFromSegment();
            Fw.graphics.loadMesh("primitives/cube.obj", meshGround);
            meshGround.setAllColors(.3f, .3f, .3f, 1f);
            meshGround.updateBooleansFromSegment();
            Fw.graphics.loadMesh("primitives/uvsphere.obj", meshSphere);
            meshSphere.setAllColors(0f, .5f, 1f, 1f);
            meshSphere.updateBooleansFromSegment();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // Create vertex buffer object renderables
        renderableBox = Fw.graphics.createVertexBufferObjectRenderable();
        renderableBox.create(meshBox);
        renderableCapsule = Fw.graphics.createVertexBufferObjectRenderable();
        renderableCapsule.create(meshCapsule);
        renderableCone = Fw.graphics.createVertexBufferObjectRenderable();
        renderableCone.create(meshCone);
        renderableCylinder = Fw.graphics.createVertexBufferObjectRenderable();
        renderableCylinder.create(meshCylinder);
        renderableGround = Fw.graphics.createVertexBufferObjectRenderable();
        renderableGround.create(meshGround);
        renderableSphere = Fw.graphics.createVertexBufferObjectRenderable();
        renderableSphere.create(meshSphere);

        // Scaling transforms
        Transform4f boxScaling = BulletGameObject.CreateBoxScaling(new Vector3f(1f, 1f, 1f));
        Transform4f capsuleScaling = BulletGameObject.CreateCapsuleScaling(1f, 1f);
        Transform4f coneScaling = BulletGameObject.CreateConeScaling(3f, 2f);
        Transform4f cylinderScaling = BulletGameObject.CreateCylinderScaling(5f, 1f);
        Transform4f groundScaling = BulletGameObject.CreateBoxScaling(new Vector3f(50f, 1f, 50f));
        Transform4f sphereScaling = BulletGameObject.CreateSphereScaling(1f);

        // Collision shapes
        btBoxShape boxShape = new btBoxShape(new Vector3(.5f, .5f, .5f));
        btCapsuleShape capsuleShape = new btCapsuleShape(1f, 1f);
        btConeShape coneShape = new btConeShape(2f, 3f);
        btCylinderShape cylinderShape = new btCylinderShape(new Vector3(1f, 2.5f, 1f));
        btBoxShape groundShape = new btBoxShape(new Vector3(25f, .5f, 25f));
        btSphereShape sphereShape = new btSphereShape(1f);

        // Add constructors
        add("box", new BulletGameObject.Constructor(1f, renderableBox, boxShape, "box", boxScaling));
        add("capsule", new BulletGameObject.Constructor(1f, renderableCapsule, capsuleShape, "capsule", capsuleScaling));
        add("cone", new BulletGameObject.Constructor(1f, renderableCone, coneShape, "cone", coneScaling));
        add("cylinder", new BulletGameObject.Constructor(1f, renderableCylinder, cylinderShape, "cylinder", cylinderScaling));
        add("ground", new BulletGameObject.Constructor(0f, renderableGround, groundShape, "ground", groundScaling));
        add("sphere", new BulletGameObject.Constructor(1f, renderableSphere, sphereShape, "sphere", sphereScaling));
    }

    public BulletGameObject.Constructor remove(String name) {
        Integer existingIndex = nameToIndex.get(name);
        if (existingIndex == null) {
            return null;
        }
        nameToIndex.remove(name);
        return constructors.remove((int) existingIndex);
    }

}
