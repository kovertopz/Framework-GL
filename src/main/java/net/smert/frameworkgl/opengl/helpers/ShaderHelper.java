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
package net.smert.frameworkgl.opengl.helpers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class ShaderHelper {

    public void attach(int programID, int shaderID) {
        GL20.glAttachShader(programID, shaderID);
    }

    public void bind(int programID) {
        GL20.glUseProgram(programID);
    }

    public void bindAttribLocation(int programID, int index, CharSequence chars) {
        GL20.glBindAttribLocation(programID, index, chars);
    }

    public void compile(int shaderID) {
        GL20.glCompileShader(shaderID);
    }

    public int createProgram() {
        return GL20.glCreateProgram();
    }

    public int createShader(int type) {
        return GL20.glCreateShader(type);
    }

    public void deleteProgram(int programID) {
        GL20.glDeleteProgram(programID);
    }

    public void deleteShader(int shaderID) {
        GL20.glDeleteShader(shaderID);
    }

    public void detach(int programID, int shaderID) {
        GL20.glDetachShader(programID, shaderID);
    }

    public boolean getCompileStatus(int shaderID) {
        return GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) != GL11.GL_FALSE;
    }

    public boolean getLinkStatus(int programID) {
        return GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) != GL11.GL_FALSE;
    }

    public boolean getValidateStatus(int programID) {
        return GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) != GL11.GL_FALSE;
    }

    public int getUniformLocation(int programID, String uniformName) {
        return GL20.glGetUniformLocation(programID, uniformName);
    }

    public String getInfoLog(int objectID) {
        return GL20.glGetShaderInfoLog(objectID, 8192);
    }

    public void link(int programID) {
        GL20.glLinkProgram(programID);
    }

    public void setSource(int shaderID, String code) {
        GL20.glShaderSource(shaderID, code);
    }

    public void unbind() {
        GL20.glUseProgram(0);
    }

    public void validate(int programID) {
        GL20.glValidateProgram(programID);
    }

}
