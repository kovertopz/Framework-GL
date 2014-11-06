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

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public interface FontRenderer {

    public void drawString(String text, float x, float y, TextHelperRenderer renderer);

    public void drawString(String text, float x, float y, float sizeX, float sizeY, TextHelperRenderer renderer);

    public void drawString(String text, TextHelperRenderer renderer);

    public int getExtraFontAdvanceX();

    public void setExtraFontAdvanceX(int extraFontAdvanceX);

    public void newHalfLine(TextHelperRenderer renderer, Vector2f position);

    public void newLine(TextHelperRenderer renderer, Vector2f position);

    public void reset(TextHelperRenderer renderer, Vector2f position);

}
