package net.smert.jreactphysics3d.framework.opengl.mesh;

import net.smert.jreactphysics3d.framework.math.Vector3f;
import net.smert.jreactphysics3d.framework.math.Vector4f;
import net.smert.jreactphysics3d.framework.opengl.GL;
import net.smert.jreactphysics3d.framework.opengl.renderable.gl1.DrawCommands;
import net.smert.jreactphysics3d.framework.utils.Color;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class DrawCommandsConversion implements DrawCommands {

    @Override
    public void execCommands(Mesh mesh) {
        assert (mesh != null);

        // For each segment in the mesh
        for (int i = 0, max = mesh.getTotalSegments(); i < max; i++) {
            Segment segment = mesh.getSegment(i);

            // Begin
            GL.renderHelper.begin(segment.getPrimitiveMode());

            // For each vertex in the segment
            for (int j = 0, max2 = segment.getVertices().size(); j < max2; j++) {

                // For each type call the render helper
                if (mesh.hasColors()) {
                    Color color = segment.getColors().get(j);
                    GL.renderHelper.color(color.getR(), color.getG(), color.getB(), color.getA());
                }
                if (mesh.hasNormals()) {
                    Vector3f normal = segment.getNormals().get(j);
                    GL.renderHelper.normal(normal.getX(), normal.getY(), normal.getZ());
                }
                if (mesh.hasTexCoords()) {
                    Vector3f texCoord = segment.getTexCoords().get(j);
                    GL.renderHelper.texCoord(texCoord.getX(), texCoord.getY(), texCoord.getZ());
                }
                if (mesh.hasVertices()) {
                    Vector4f vertex = segment.getVertices().get(j);
                    GL.renderHelper.vertex(vertex.getX(), vertex.getY(), vertex.getZ(), vertex.getW());
                }
            }

            // End
            GL.renderHelper.end();
        }
    }

}
