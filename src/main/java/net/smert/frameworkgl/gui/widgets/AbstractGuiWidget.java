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
package net.smert.frameworkgl.gui.widgets;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public abstract class AbstractGuiWidget {

    private int borderBottom;
    private int borderLeft;
    private int borderRight;
    private int borderTop;
    private int marginBottom;
    private int marginLeft;
    private int marginRight;
    private int marginTop;
    private int paddingBottom;
    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    private int positionX;
    private int positionY;
    private int sizeX;
    private int sizeY;

    public abstract void render();

}
