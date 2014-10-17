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

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL20;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class ShaderUniformHelper {

    public void getUniform(int programID, int uniformID, FloatBuffer values) {
        GL20.glGetUniform(programID, uniformID, values);
    }

    public void getUniform(int programID, int uniformID, IntBuffer values) {
        GL20.glGetUniform(programID, uniformID, values);
    }

    public int getUniformLocation(int programID, String uniformName) {
        return GL20.glGetUniformLocation(programID, uniformName);
    }

    public void setUniform(int uniformID, float value1) {
        GL20.glUniform1f(uniformID, value1);
    }

    public void setUniform(int uniformID, int value1) {
        GL20.glUniform1i(uniformID, value1);
    }

    public void setUniform(int uniformID, float value1, float value2) {
        GL20.glUniform2f(uniformID, value1, value2);
    }

    public void setUniform(int uniformID, int value1, int value2) {
        GL20.glUniform2f(uniformID, value1, value2);
    }

    public void setUniform(int uniformID, float value1, float value2, float value3) {
        GL20.glUniform3f(uniformID, value1, value2, value3);
    }

    public void setUniform(int uniformID, int value1, int value2, int value3) {
        GL20.glUniform3f(uniformID, value1, value2, value3);
    }

    public void setUniform(int uniformID, float value1, float value2, float value3, float value4) {
        GL20.glUniform4f(uniformID, value1, value2, value3, value4);
    }

    public void setUniform(int uniformID, int value1, int value2, int value3, int value4) {
        GL20.glUniform4f(uniformID, value1, value2, value3, value4);
    }

    public void setUniform(int uniformID, FloatBuffer values) {
        GL20.glUniform1(uniformID, values);
    }

    public void setUniform(int uniformID, IntBuffer values) {
        GL20.glUniform1(uniformID, values);
    }

    public void setUniformMatrix2(int uniformID, boolean transpose, FloatBuffer matrix) {
        GL20.glUniformMatrix2(uniformID, transpose, matrix);
    }

    public void setUniformMatrix3(int uniformID, boolean transpose, FloatBuffer matrix) {
        GL20.glUniformMatrix3(uniformID, transpose, matrix);
    }

    public void setUniformMatrix4(int uniformID, boolean transpose, FloatBuffer matrix) {
        GL20.glUniformMatrix4(uniformID, transpose, matrix);
    }

}
