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

import net.smert.frameworkgl.math.Vector2f;
import net.smert.frameworkgl.opengl.renderable.AbstractRenderable;
import net.smert.frameworkgl.utils.Color;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public interface TextHelperRenderer {

    public void colorText(Color color);

    public AbstractRenderable createGlyphRenderable();

    public float getTextDefaultX();

    public float getTextDefaultY();

    public Color getTextColor();

    public Vector2f getTextPosition();

    public void popMatrix();

    public void pushMatrix();

    public void renderGlyph(AbstractRenderable renderable);

    public void scaleText(float x, float y);

    public void translateText(float x, float y);

}
