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

package thebounzer.org.lwgldemo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import thebounzer.org.lwgldemo.glutils.GenericShader;
import thebounzer.org.lwgldemo.glutils.ShaderProgram;

/**
 *
 * @author thbounzer
 */
public class Chapter3 extends Chapter{
    
    private ShaderProgram program;
    private int vaoId;
    private float time = 0.01f;
    
    
    @Override
    public void loopCycle() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        GL20.glUseProgram(program.getId());
        
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);	        
        attributesBind(time+=0.01f);
        GL11.glDrawArrays(GL11.GL_LINE_LOOP, 0, 3);
        GL20.glUseProgram(0);
    }
    
    private void attributesBind(float time){
        float[] offsetPos = new float[]{(float)Math.sin(time),(float)Math.cos(time),0.0f,1.0f};
        GL20.glVertexAttrib4f(0, offsetPos[0],offsetPos[1],offsetPos[2],offsetPos[3]);
    }

    @Override
    public void shaderSetup() {
        GenericShader vertex = new GenericShader("src/main/resources/shadersCap3/shader.vert",GL20.GL_VERTEX_SHADER); 
        // Load the fragment shader
        GenericShader fragment = new GenericShader("src/main/resources/shaders/screen.frag",GL20.GL_FRAGMENT_SHADER);

        ArrayList<GenericShader> shaders = new ArrayList<GenericShader>();
        shaders.add(vertex);
        shaders.add(fragment);
        // Create a new shader program that links both shaders
        program = new ShaderProgram();
        try {        
            program.compile(shaders);
        } catch (IOException ex) {
            Logger.getLogger(Chapter3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void destroy() {
        program.destroy();
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoId);        
    }
    
    
    
}
