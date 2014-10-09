package net.smert.frameworkgl.examples.common;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import net.smert.frameworkgl.Fw;
import net.smert.frameworkgl.GameObject;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.opengl.GL;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class DynamicMeshWorld {

    private FloatBuffer transformWorldFloatBuffer;
    private final List<GameObject> gameObjects;

    public DynamicMeshWorld() {
        gameObjects = new ArrayList<>();
    }

    private GameObject createGameObject(Mesh mesh) {
        GameObject gameObject = new GameObject();
        AbstractRenderable renderable = Fw.graphics.createVertexBufferObjectRenderable();
        renderable.create(mesh);
        gameObject.setMesh(mesh); // Attach to game object
        gameObject.setRenderable(renderable); // Attach to game object
        gameObjects.add(gameObject);
        return gameObject;
    }

    public void destroy() {
        for (GameObject gameObject : gameObjects) {
            gameObject.destroy();
        }
    }

    public void init() {

        // Float buffer for matrices
        transformWorldFloatBuffer = GL.bufferHelper.createFloatBuffer(16);

        // Build floor
        final Mesh baseFloor = GL.dynamicMeshBuilder.
                setColor(0, "silver").
                setQuality(4, 1, 4).
                setSize(64f, 3f, 64f).
                build("box").
                createMesh(false);
        final Mesh baseFloorGrid = GL.dynamicMeshBuilder.
                setColor(0, "dark_gray").
                setColor(1, "red").
                setColor(2, "blue").
                setQuality(1, 1, 1).
                setSize(64f, 0f, 64f).
                build("grid").
                createMesh(false);
        final Mesh baseWallNegX = GL.dynamicMeshBuilder.
                setColor(0, "silver").
                setQuality(1, 1, 8).
                setSize(.5f, 2f, 64f).
                build("box").
                createMesh(false);
        final Mesh baseWallPosX = GL.dynamicMeshBuilder.
                build("box").
                createMesh(false);
        final Mesh baseWallNegZ = GL.dynamicMeshBuilder.
                setQuality(8, 1, 1).
                setSize(64f, 2f, .5f).
                build("box").
                createMesh(false);
        final Mesh baseWallPosZ = GL.dynamicMeshBuilder.
                build("box").
                createMesh(false);

        createGameObject(baseFloor).setWorldPosition(0f, -1.51f, 0f);
        createGameObject(baseFloorGrid).setWorldPosition(0f, 0f, 0f);
        createGameObject(baseWallNegX).setWorldPosition(-32.25f, 1f, 0f);
        createGameObject(baseWallPosX).setWorldPosition(32.25f, 1f, 0f);
        createGameObject(baseWallNegZ).setWorldPosition(0f, 1f, -32.25f);
        createGameObject(baseWallPosZ).setWorldPosition(0f, 1f, 32.25f);

        // Build big slope and wall
        final Mesh bigLadder = GL.dynamicMeshBuilder.
                setColor(0, "yellow").
                setQuality(1, 5, 1).
                setSize(.25f, 9.999f, 1f).
                build("box").
                createMesh(false);
        final Mesh bigSlope = GL.dynamicMeshBuilder.
                setColor(0, "crimson").
                setQuality(3, 2, 13).
                setSize(6f, 4f, 26f).
                build("box").
                createMesh(false);
        final Mesh bigWall = GL.dynamicMeshBuilder.
                setQuality(1, 5, 8).
                setSize(2f, 10f, 16f).
                build("box").
                createMesh(false);

        createGameObject(bigLadder).setWorldPosition(-20f, 4.9995f, -8f);
        createGameObject(bigSlope).setWorldPosition(-17f, 3f, -12f).setWorldRotation(Vector3f.WORLD_X_AXIS, 25f);
        createGameObject(bigWall).setWorldPosition(-21f, 5f, -14f);

        // Build slope with varying inclines
        final Mesh slopewithvaryinginclines1 = GL.dynamicMeshBuilder.
                setColor(0, "midnight_blue").
                setQuality(2, 1, 2).
                setSize(4f, .5f, 5f).
                build("box").
                createMesh(false);
        final Mesh slopewithvaryinginclines2 = GL.dynamicMeshBuilder.
                build("box").
                createMesh(false);
        final Mesh slopewithvaryinginclines3 = GL.dynamicMeshBuilder.
                build("box").
                createMesh(false);
        final Mesh slopewithvaryinginclines4 = GL.dynamicMeshBuilder.
                build("box").
                createMesh(false);
        final Mesh slopewithvaryinginclines5 = GL.dynamicMeshBuilder.
                build("box").
                createMesh(false);
        final Mesh slopewithvaryinginclines6 = GL.dynamicMeshBuilder.
                build("box").
                createMesh(false);

        createGameObject(slopewithvaryinginclines1).setWorldPosition(-8f, 0f, -3f).setWorldRotation(Vector3f.WORLD_X_AXIS, 10f);
        createGameObject(slopewithvaryinginclines2).setWorldPosition(-8f, 1f, -7.5f).setWorldRotation(Vector3f.WORLD_X_AXIS, 20f);
        createGameObject(slopewithvaryinginclines3).setWorldPosition(-8f, 3f, -12f).setWorldRotation(Vector3f.WORLD_X_AXIS, 35f);
        createGameObject(slopewithvaryinginclines4).setWorldPosition(-8f, 6.2f, -15.75f).setWorldRotation(Vector3f.WORLD_X_AXIS, 50f);
        createGameObject(slopewithvaryinginclines5).setWorldPosition(-8f, 10f, -18.25f).setWorldRotation(Vector3f.WORLD_X_AXIS, 65f);
        createGameObject(slopewithvaryinginclines6).setWorldPosition(-8f, 14.7f, -19.75f).setWorldRotation(Vector3f.WORLD_X_AXIS, 80f);

        // Build stairs with varying sizes
        final Mesh stairsWithVaryingSizes1 = GL.dynamicMeshBuilder.
                setColor(0, "lime_green").
                setQuality(1, 1, 1).
                setSize(2f, .1f, 2f).
                build("box").
                createMesh(false);
        final Mesh stairsWithVaryingSizes2 = GL.dynamicMeshBuilder.
                setSize(2f, .2f, 2f).
                build("box").
                createMesh(false);
        final Mesh stairsWithVaryingSizes3 = GL.dynamicMeshBuilder.
                setSize(2f, .3f, 2f).
                build("box").
                createMesh(false);
        final Mesh stairsWithVaryingSizes4 = GL.dynamicMeshBuilder.
                setSize(2f, .4f, 2f).
                build("box").
                createMesh(false);
        final Mesh stairsWithVaryingSizes5 = GL.dynamicMeshBuilder.
                setSize(2f, .5f, 2f).
                build("box").
                createMesh(false);
        final Mesh stairsWithVaryingSizes6 = GL.dynamicMeshBuilder.
                setSize(2f, .7f, 2f).
                build("box").
                createMesh(false);
        final Mesh stairsWithVaryingSizes7 = GL.dynamicMeshBuilder.
                setSize(2f, .9f, 2f).
                build("box").
                createMesh(false);
        final Mesh stairsWithVaryingSizes8 = GL.dynamicMeshBuilder.
                setSize(2f, 1.1f, 2f).
                build("box").
                createMesh(false);
        final Mesh stairsWithVaryingSizes9 = GL.dynamicMeshBuilder.
                setSize(2f, 1.4f, 2f).
                build("box").
                createMesh(false);
        final Mesh stairsWithVaryingSizes10 = GL.dynamicMeshBuilder.
                setSize(2f, 1.7f, 2f).
                build("box").
                createMesh(false);

        createGameObject(stairsWithVaryingSizes1).setWorldPosition(0f, .1f / 2f, -2f);
        createGameObject(stairsWithVaryingSizes2).setWorldPosition(0f, .2f / 2f + .1f, -4f);
        createGameObject(stairsWithVaryingSizes3).setWorldPosition(0f, .3f / 2f + .3f, -6f);
        createGameObject(stairsWithVaryingSizes4).setWorldPosition(0f, .4f / 2f + .6f, -8f);
        createGameObject(stairsWithVaryingSizes5).setWorldPosition(0f, .5f / 2f + 1.0f, -10f);
        createGameObject(stairsWithVaryingSizes6).setWorldPosition(0f, .7f / 2f + 1.5f, -12f);
        createGameObject(stairsWithVaryingSizes7).setWorldPosition(0f, .9f / 2f + 2.2f, -14f);
        createGameObject(stairsWithVaryingSizes8).setWorldPosition(0f, 1.1f / 2f + 3.1f, -16f);
        createGameObject(stairsWithVaryingSizes9).setWorldPosition(0f, 1.4f / 2f + 4.2f, -18f);
        createGameObject(stairsWithVaryingSizes10).setWorldPosition(0f, 1.7f / 2f + 5.6f, -20f);

        // Build bridge with varying slopes
        final Mesh bridgeWithVaryingSlopes1 = GL.dynamicMeshBuilder.
                setColor(0, "yellow").
                setQuality(2, 1, 2).
                setSize(4f, .5f, 5f).
                build("box").
                createMesh(false);
        final Mesh bridgeWithVaryingSlopes2 = GL.dynamicMeshBuilder.
                build("box").
                createMesh(false);
        final Mesh bridgeWithVaryingSlopes3 = GL.dynamicMeshBuilder.
                build("box").
                createMesh(false);
        final Mesh bridgeWithVaryingSlopes4 = GL.dynamicMeshBuilder.
                build("box").
                createMesh(false);
        final Mesh bridgeWithVaryingSlopes5 = GL.dynamicMeshBuilder.
                build("box").
                createMesh(false);
        final Mesh bridgeWithVaryingSlopes6 = GL.dynamicMeshBuilder.
                build("box").
                createMesh(false);

        createGameObject(bridgeWithVaryingSlopes1).setWorldPosition(8f, .68f, -3.6f).setWorldRotation(Vector3f.WORLD_X_AXIS, 20f);
        createGameObject(bridgeWithVaryingSlopes2).setWorldPosition(8f, 2.9f, -7.9f).setWorldRotation(Vector3f.WORLD_X_AXIS, 35f);
        createGameObject(bridgeWithVaryingSlopes3).setWorldPosition(8f, 4.65f, -12.2f).setWorldRotation(Vector3f.WORLD_X_AXIS, 10f);
        createGameObject(bridgeWithVaryingSlopes4).setWorldPosition(8f, 4.65f, -17f).setWorldRotation(Vector3f.WORLD_X_AXIS, -10f);
        createGameObject(bridgeWithVaryingSlopes5).setWorldPosition(8f, 2.9f, -21.3f).setWorldRotation(Vector3f.WORLD_X_AXIS, -35f);
        createGameObject(bridgeWithVaryingSlopes6).setWorldPosition(8f, .68f, -25.6f).setWorldRotation(Vector3f.WORLD_X_AXIS, -20f);

        // Build double vertical slope
        final Mesh doubleVerticalSlope1 = GL.dynamicMeshBuilder.
                setColor(0, "cyan").
                setQuality(2, 1, 12).
                setSize(4f, 1f, 24f).
                build("box").
                createMesh(false);
        final Mesh doubleVerticalSlope2 = GL.dynamicMeshBuilder.
                build("box").
                createMesh(false);

        createGameObject(doubleVerticalSlope1).setWorldPosition(16f, 5f, -12f).setWorldRotation(Vector3f.WORLD_X_AXIS, 25f);
        createGameObject(doubleVerticalSlope2).setWorldPosition(16f, 5f, -12f).setWorldRotation(Vector3f.WORLD_X_AXIS, -25f);

        // Build double horizontal slope
        final Mesh doubleHorizontalSlope1 = GL.dynamicMeshBuilder.
                setColor(0, "steel_blue").
                setQuality(3, 2, 4).
                setSize(6f, 5f, 8f).
                build("box").
                createMesh(false);
        final Mesh doubleHorizontalSlope2 = GL.dynamicMeshBuilder.
                setQuality(2, 4, 2).
                setSize(4f, 8f, 4f).
                build("box").
                createMesh(false);

        createGameObject(doubleHorizontalSlope1).setWorldPosition(25f, 2.5f, -8f);
        createGameObject(doubleHorizontalSlope2).setWorldPosition(25f, 4f, -14f).setWorldRotation(Vector3f.WORLD_Y_AXIS, 45f);

        // Build platform next to the double vertical slope and platform next to vertical moving platform
        final Mesh platformNextToDoubleVerticalSlope = GL.dynamicMeshBuilder.
                setColor(0, "deep_pink").
                setQuality(2, 1, 2).
                setSize(4f, .5f, 4f).
                build("box").
                createMesh(false);
        final Mesh platformNextToVerticalMovingPlatform = GL.dynamicMeshBuilder.
                build("box").
                createMesh(false);

        createGameObject(platformNextToDoubleVerticalSlope).setWorldPosition(16f, 0f, 2f);
        createGameObject(platformNextToVerticalMovingPlatform).setWorldPosition(10f, 1f, 0f);

        // Build crush slope for horizontal moving platform
        final Mesh crushSlopeForHorizontalMovingPlatform = GL.dynamicMeshBuilder.
                setColor(0, "orange").
                setQuality(2, 1, 2).
                setSize(4f, .5f, 5f).
                build("box").
                createMesh(false);

        createGameObject(crushSlopeForHorizontalMovingPlatform).setWorldPosition(10f, 1f, 14.5f).setWorldRotation(Vector3f.WORLD_X_AXIS, -65f);

        // Build capsules
        final Mesh capsule1 = GL.dynamicMeshBuilder.
                setColor(0, "maroon").
                setQuality(2, 1, 1).
                setRadius(.5f, .5f, .5f).
                setSize(0f, 0f, .5f).
                build("capsule").
                createMesh(false);
        final Mesh capsule2 = GL.dynamicMeshBuilder.
                setQuality(4, 1, 1).
                setRadius(1f, 1f, 1f).
                setSize(0f, 0f, 1f).
                build("capsule").
                createMesh(false);
        final Mesh capsule3 = GL.dynamicMeshBuilder.
                setQuality(8, 1, 1).
                setRadius(2f, 2f, 2f).
                setSize(0f, 0f, 2f).
                build("capsule").
                createMesh(false);

        createGameObject(capsule1).setWorldPosition(1.5f, .5f, 10f);
        createGameObject(capsule2).setWorldPosition(0f, 1f, 10f);
        createGameObject(capsule3).setWorldPosition(-3f, 1f, 10f);

        // Build cones
        final Mesh cone1 = GL.dynamicMeshBuilder.
                setColor(0, "coral").
                setQuality(1, 1, 1).
                setRadius(.25f, 0f, .25f).
                setSize(0f, 1f, 0f).
                build("cone").
                createMesh(false);
        final Mesh cone2 = GL.dynamicMeshBuilder.
                setQuality(4, 1, 1).
                setRadius(1f, 0f, 1f).
                setSize(0f, 2f, 0f).
                build("cone").
                createMesh(false);
        final Mesh cone3 = GL.dynamicMeshBuilder.
                setQuality(8, 1, 1).
                setRadius(2f, 0f, 2f).
                setSize(0f, 3f, 0f).
                build("cone").
                createMesh(false);

        createGameObject(cone1).setWorldPosition(1.5f, .5f, 17.5f);
        createGameObject(cone2).setWorldPosition(0f, 1f, 18f);
        createGameObject(cone3).setWorldPosition(-3f, 1.5f, 18f);

        // Build boxes
        final Mesh box1 = GL.dynamicMeshBuilder.
                setColor(0, "azure").
                setQuality(1, 1, 1).
                setSize(1f, 1f, 1f).
                build("box").
                createMesh(false);
        final Mesh box2 = GL.dynamicMeshBuilder.
                setQuality(4, 4, 4).
                setSize(2f, 2f, 2f).
                build("box").
                createMesh(false);
        final Mesh box3 = GL.dynamicMeshBuilder.
                setQuality(8, 8, 8).
                setSize(4f, 4f, 4f).
                build("box").
                createMesh(false);

        createGameObject(box1).setWorldPosition(1.5f, .5f, 18.5f);
        createGameObject(box2).setWorldPosition(1.5f, 1f, 20f);
        createGameObject(box3).setWorldPosition(1.5f, 2f, 23f);

        // Build spheres
        final Mesh sphere1 = GL.dynamicMeshBuilder.
                setColor(0, "medium_purple").
                setQuality(2, 1, 1).
                setRadius(.5f, .5f, .5f).
                setSize(0f, 0f, 0f).
                build("sphere").
                createMesh(false);
        final Mesh sphere2 = GL.dynamicMeshBuilder.
                setQuality(4, 1, 1).
                setRadius(1f, 1f, 1f).
                setSize(0f, 0f, 0f).
                build("sphere").
                createMesh(false);
        final Mesh sphere3 = GL.dynamicMeshBuilder.
                setQuality(8, 1, 1).
                setRadius(2f, 2f, 2f).
                setSize(0f, 0f, 0f).
                build("sphere").
                createMesh(false);

        createGameObject(sphere1).setWorldPosition(-10.5f, .5f, 10f);
        createGameObject(sphere2).setWorldPosition(-12f, 1f, 10f);
        createGameObject(sphere3).setWorldPosition(-15f, 1f, 10f);

        // Build pyramids
        final Mesh pyramid1 = GL.dynamicMeshBuilder.
                setColor(0, "orange_red").
                setQuality(1, 1, 1).
                setSize(1f, 1f, 1f).
                build("pyramid").
                createMesh(false);
        final Mesh pyramid2 = GL.dynamicMeshBuilder.
                setSize(2f, 2f, 2f).
                build("pyramid").
                createMesh(false);
        final Mesh pyramid3 = GL.dynamicMeshBuilder.
                setSize(4f, 4f, 4f).
                build("pyramid").
                createMesh(false);

        createGameObject(pyramid1).setWorldPosition(-10.5f, .5f, 18.5f);
        createGameObject(pyramid2).setWorldPosition(-10.5f, 1f, 20f);
        createGameObject(pyramid3).setWorldPosition(-10.5f, 2f, 23f);

        // Build cylinders
        final Mesh cylinder1 = GL.dynamicMeshBuilder.
                setColor(0, "gold").
                setQuality(1, 1, 1).
                setRadius(.5f, .5f, 0f).
                setSize(0f, 0f, 1f).
                build("cylinder").
                createMesh(false);
        final Mesh cylinder2 = GL.dynamicMeshBuilder.
                setQuality(4, 1, 1).
                setRadius(1f, 1f, 0f).
                setSize(0f, 0f, 2f).
                build("cylinder").
                createMesh(false);
        final Mesh cylinder3 = GL.dynamicMeshBuilder.
                setQuality(8, 1, 1).
                setRadius(2f, 2f, 0f).
                setSize(0f, 0f, 4f).
                build("cylinder").
                createMesh(false);

        createGameObject(cylinder1).setWorldPosition(-15.5f, .5f, 18f);
        createGameObject(cylinder2).setWorldPosition(-15.5f, 1f, 21f);
        createGameObject(cylinder3).setWorldPosition(-15.5f, 2f, 25f);

        // Build frustums
        final Mesh frustum1 = GL.dynamicMeshBuilder.
                setColor(0, "violet").
                setQuality(1, 1, 1).
                setRadius(.25f, .25f, .25f).
                setSize(1f, 1f, 1f).
                build("frustum").
                createMesh(false);
        final Mesh frustum2 = GL.dynamicMeshBuilder.
                setQuality(1, 1, 1).
                setRadius(.25f, .25f, .25f).
                setSize(2f, 2f, 2f).
                build("frustum").
                createMesh(false);
        final Mesh frustum3 = GL.dynamicMeshBuilder.
                setQuality(1, 1, 1).
                setRadius(.25f, .25f, .25f).
                setSize(4f, 4f, 4f).
                build("frustum").
                createMesh(false);

        createGameObject(frustum1).setWorldPosition(-20.5f, .5f, 18f);
        createGameObject(frustum2).setWorldPosition(-20.5f, 1f, 21f);
        createGameObject(frustum3).setWorldPosition(-20.5f, 2f, 25f);

        // Build toriods
        final Mesh toriod1 = GL.dynamicMeshBuilder.
                setColor(0, "aquamarine").
                setQuality(2, 4, 1).
                setRadius(.5f, 1f, 0f).
                setSize(0f, 0f, 0f).
                build("toriod").
                createMesh(false);
        final Mesh toriod2 = GL.dynamicMeshBuilder.
                setQuality(4, 8, 1).
                setRadius(1f, 2f, 0f).
                setSize(0f, 0f, 0f).
                build("toriod").
                createMesh(false);
        final Mesh toriod3 = GL.dynamicMeshBuilder.
                setQuality(8, 16, 1).
                setRadius(1.5f, 3f, 0f).
                setSize(0f, 0f, 0f).
                build("toriod").
                createMesh(false);

        createGameObject(toriod1).setWorldPosition(-23.5f, 1.5f, 5.5f);
        createGameObject(toriod2).setWorldPosition(-23.5f, 3f, 9f);
        createGameObject(toriod3).setWorldPosition(-23.5f, 4.5f, 13.5f);

        // Reset builder
        GL.dynamicMeshBuilder.reset();
    }

    public void render() {
        for (GameObject gameObject : gameObjects) {
            Fw.graphics.render(gameObject, transformWorldFloatBuffer);
        }
    }

}
