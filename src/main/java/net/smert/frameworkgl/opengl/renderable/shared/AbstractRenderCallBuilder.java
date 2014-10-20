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
package net.smert.frameworkgl.opengl.renderable.shared;

import java.util.Iterator;
import java.util.Map;
import net.smert.frameworkgl.opengl.TextureType;
import net.smert.frameworkgl.opengl.mesh.Material;
import net.smert.frameworkgl.opengl.mesh.Mesh;
import net.smert.frameworkgl.opengl.renderable.Renderable;
import net.smert.frameworkgl.opengl.texture.TextureTypeMapping;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public abstract class AbstractRenderCallBuilder {

    public void createRenderCall(Mesh mesh, AbstractRenderCall renderCall) {

        int totalSegments = mesh.getTotalSegments();

        // Convert textures for each segment
        TextureTypeMapping[][] textureTypeMappings = new TextureTypeMapping[totalSegments][];
        for (int i = 0; i < textureTypeMappings.length; i++) {

            Material material = mesh.getSegment(i).getMaterial();
            if (material == null) {
                continue;
            }

            int j = 0;
            Map<TextureType, String> textures = material.getTextures();
            textureTypeMappings[i] = new TextureTypeMapping[textures.size()];

            Iterator<Map.Entry<TextureType, String>> entries = textures.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<TextureType, String> entry = entries.next();
                TextureType textureType = entry.getKey();
                String filename = entry.getValue();
                int textureTypeID = textureType.ordinal();
                int uniqueTextureID = Renderable.texturePool.getUniqueID(filename);
                textureTypeMappings[i][j++] = new TextureTypeMapping(textureTypeID, uniqueTextureID);
            }
        }

        // Convert shaders for each segment
        int[] uniqueShaderIDs = new int[totalSegments];
        for (int i = 0; i < uniqueShaderIDs.length; i++) {
            uniqueShaderIDs[i] = -1;

            Material material = mesh.getSegment(i).getMaterial();
            if (material == null) {
                continue;
            }
            String shader = material.getShader();
            if (shader == null) {
                continue;
            }

            uniqueShaderIDs[i] = Renderable.shaderPool.getUniqueID(shader);
        }

        renderCall.setTextureTypeMappings(textureTypeMappings);
        renderCall.setUniqueShaderIDs(uniqueShaderIDs);
    }

}
