package net.smert.jreactphysics3d.framework.opengl.helpers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class ShaderHelper {

    public void attach(int programid, int shaderid) {
        GL20.glAttachShader(programid, shaderid);
    }

    public void bind(int programid) {
        GL20.glUseProgram(programid);
    }

    public void compile(int shaderid) {
        GL20.glCompileShader(shaderid);
    }

    public int createProgram() {
        return GL20.glCreateProgram();
    }

    public int createShader(int type) {
        return GL20.glCreateShader(type);
    }

    public void deleteProgram(int programid) {
        GL20.glDeleteProgram(programid);
    }

    public void deleteShader(int shaderid) {
        GL20.glDeleteShader(shaderid);
    }

    public boolean getCompileStatus(int shaderid) {
        return GL20.glGetShaderi(shaderid, GL20.GL_COMPILE_STATUS) != GL11.GL_FALSE;
    }

    public boolean getLinkStatus(int programid) {
        return GL20.glGetProgrami(programid, GL20.GL_LINK_STATUS) != GL11.GL_FALSE;
    }

    public boolean getValidateStatus(int programid) {
        return GL20.glGetProgrami(programid, GL20.GL_VALIDATE_STATUS) != GL11.GL_FALSE;
    }

    public int getUniformLocation(int programid, String uniformname) {
        return GL20.glGetUniformLocation(programid, uniformname);
    }

    public String getInfoLog(int objectid) {
        return GL20.glGetShaderInfoLog(objectid, 8192);
    }

    public void link(int programid) {
        GL20.glLinkProgram(programid);
    }

    public void setSource(int shaderid, String code) {
        GL20.glShaderSource(shaderid, code);
    }

    public void unbind() {
        GL20.glUseProgram(0);
    }

    public void validate(int program) {
        GL20.glValidateProgram(program);
    }

}