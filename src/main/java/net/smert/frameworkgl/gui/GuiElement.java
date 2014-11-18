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
package net.smert.frameworkgl.gui;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class GuiElement {

    private boolean inherit;
    private boolean visible;
    private boolean visibleToMouse;
    private int zIndex;
    private final BoxDimensions boxDimensions;
    private Color backgroundColor;
    private Display display;
    private HAlign hAlign;
    private ImageRepeat imageRepeat;
    private final List<GuiElement> children;
    private Overflow overflow;
    private Position position;
    private String id;
    private String backgroundImage;
    private String controllerClass;
    private String renderClass;
    private String style;
    private VAlign vAlign;

    public GuiElement() {
        boxDimensions = new BoxDimensions();
        children = new ArrayList<>();
    }

    public static class BoxDimensions {

        private float height;
        private UnitType heightUnitType;
        private float marginBottom;
        private float marginLeft;
        private float marginRight;
        private float marginTop;
        private float paddingBottom;
        private float paddingLeft;
        private float paddingRight;
        private float paddingTop;
        private float width;
        private UnitType widthUnitType;
        private float x;
        private float y;

    }

    public static class Color {

        private float alpha;
        private float blue;
        private float green;
        private float red;

        public void parse(String hexString) {
            if ((hexString.length() != 5) && (hexString.length() != 9)) {
                throw new IllegalArgumentException("Hex string must be 5 or 9 characters long: " + hexString);
            }
            if (!hexString.startsWith("#")) {
                throw new IllegalArgumentException("Hex string must be in the format #RGBA or #RRGGBBAA: " + hexString);
            }
            hexString = hexString.substring(1);
        }

    }

    public enum Display {

        NONE,
        BLOCK

    }

    public enum HAlign {

        CENTER,
        LEFT,
        RIGHT

    }

    public enum ImageRepeat {

        NO_REPEAT,
        REPEAT,
        REPEAT_X,
        REPEAT_Y

    }

    public enum Overflow {

        AUTO,
        HIDDEN,
        SCROLL,
        VISIBLE

    }

    public enum Position {

        ABSOLUTE,
        RELATIVE

    }

    public enum UnitType {

        EM,
        PERCENT,
        PIXEL

    }

    public enum VAlign {

        BOTTOM,
        CENTER,
        TOP

    }

}
