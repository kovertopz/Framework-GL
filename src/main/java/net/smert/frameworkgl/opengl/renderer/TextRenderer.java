package net.smert.frameworkgl.opengl.renderer;

import net.smert.frameworkgl.opengl.font.GLFont;
import net.smert.frameworkgl.utils.Color;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public interface TextRenderer {

    public void drawString(String text, float x, float y);

    public void drawString(String text, float x, float y, GLFont font);

    public void drawString(String text);

    public void drawString(String text, GLFont font);

    public void resetTextRendering();

    public void setTextColor(float r, float g, float b, float a);

    public void setTextColor(Color color);

    public void setTextColor(String colorName);

    public void setTextColorHex(String hexCode);

    public void textNewHalfLine();

    public void textNewHalfLine(GLFont font);

    public void textNewLine();

    public void textNewLine(GLFont font);

}
