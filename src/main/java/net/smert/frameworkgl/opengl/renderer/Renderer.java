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
package net.smert.frameworkgl.opengl.renderer;

import java.util.List;
import net.smert.frameworkgl.gameobjects.GameObject;
import net.smert.frameworkgl.math.Transform4f;
import net.smert.frameworkgl.math.Vector3f;
import net.smert.frameworkgl.opengl.camera.Camera;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;
import net.smert.frameworkgl.opengl.shader.AbstractShader;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public interface Renderer {

    public void color(float r, float g, float b, float a);

    public void render(AbstractRenderable renderable, float x, float y, float z);

    public void render(AbstractRenderable renderable, Transform4f transform);

    public void render(AbstractRenderable renderable, Vector3f position);

    public void render(GameObject gameObject);

    public void render(List<GameObject> gameObjects);

    public void renderBlend(GameObject gameObject);

    public void renderBlend(List<GameObject> gameObjects);

    public void renderOpaque(GameObject gameObject);

    public void renderOpaque(List<GameObject> gameObjects);

    public void set2DMode();

    public void set2DMode(int width, int height);

    public void setCamera(Camera camera);

    public void switchShader(AbstractShader shader);

    public void unbindShader();

}
