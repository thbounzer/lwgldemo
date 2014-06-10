/*
 * The MIT License
 *
 * Copyright 2014 thbounzer.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package thebounzer.org.lwgldemo.glutils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.GLU;

/**
 *
 * @author thbounzer
 */
public class ShaderProgram {
    
    private static final Logger logger = Logger.getLogger(ShaderProgram.class.getName());
    private int pId;
    ArrayList<GenericShader> programShaders = new ArrayList<GenericShader>();
    
    public void compile(ArrayList<GenericShader> shaders, HashMap<Integer,String> attributes) throws IOException{
        programShaders = shaders;
        int programId = allocateProgram();
        for(GenericShader shader : shaders){
            compileShader(programId, shader);
        }
        setAttributes(programId, attributes);
        if (!validateProgram(programId)){
            throw new IOException("Unable to compile the shader program.");
        }
        pId = programId;
    }
    
    public void compile(ArrayList<GenericShader> shaders) throws IOException{
        programShaders = shaders;
        int programId = allocateProgram();
        for(GenericShader shader : shaders){
            compileShader(programId, shader);
        }
        if (!validateProgram(programId)){
            throw new IOException("Unable to compile the shader program.");
        }
        pId = programId;
    }    
    
    
    public int getId(){
        return pId;
    }
    
    public void compile(GenericShader shader, HashMap<Integer,String> attributes) throws IOException{
        programShaders.add(shader);
        int programId = allocateProgram();
        compileShader(programId, shader);
        setAttributes(programId, attributes);
        if (!validateProgram(programId)){
            destroy();
            throw new IOException("Unable to compile the shader program.");
        }        
        pId = programId;
    }
    
    
    private void compileShader(int programId, GenericShader shader) throws IOException{
        GL20.glShaderSource(shader.getShaderId(), shader.getSource());
        GL20.glCompileShader(shader.getShaderId());
        if (!validateCompilation(shader)){
            throw new IOException("Shader compilation failed, see log!");
        }
        GL20.glAttachShader(programId, shader.getShaderId());        
    }
    
    private int allocateProgram(){
        return GL20.glCreateProgram();
    }
    
    private void setAttributes(int programId, HashMap<Integer,String> attributes){
        for(Entry e : attributes.entrySet()){
            int n = (Integer) e.getKey();
            String s = (String) e.getValue();
            GL20.glBindAttribLocation(programId, n, s);   
        }
    }
    
    private boolean validateProgram(int programId){
        GL20.glLinkProgram(programId);
        GL20.glValidateProgram(programId);
        boolean result = true;
        int errorCheckValue = GL11.glGetError();
        if (errorCheckValue != GL11.GL_NO_ERROR) {
            logger.log(Level.SEVERE, "ERROR - Could not create the shaders:{0}", GLU.gluErrorString(errorCheckValue));
            result = false;
        }
        return result;
    }
    
    private boolean validateCompilation(GenericShader shader){
        boolean retValue = true;
        int isCompiled = GL20.glGetShaderi(shader.getShaderId(), GL20.GL_COMPILE_STATUS);
        if (isCompiled == GL11.GL_FALSE){
            int logLen = GL20.glGetShaderi(shader.getShaderId(),GL20.GL_INFO_LOG_LENGTH);
            String log = GL20.glGetShaderInfoLog(shader.getShaderId(),logLen);
            logger.log(Level.SEVERE, "Error compiling shader: {0}", shader.getSourceFileName());
            logger.log(Level.SEVERE, log);
            retValue = false;
        }
        return retValue;
    }
    
    public void destroy(){
        GL20.glUseProgram(0);
        for(GenericShader shader: programShaders)
            deleteShader(shader);
        GL20.glDeleteProgram(pId);        
    }
    
    private void deleteShader(GenericShader shader){
        GL20.glDetachShader(pId, shader.getShaderId());
        GL20.glDeleteShader(shader.getShaderId());   
    }
}
