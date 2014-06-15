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
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL32;


/**
 *
 * @author thbounzer
 */
public class Chapter5 extends Chapter {

    
    @Override
    public void loopCycle() {
        
    }

    @Override
    public void shaderSetup() {
        
    }

    @Override
    public void destroy() {
    }
    
    private void oglBufferAllocateFloatArray(int target, float[] data, int usage) throws IOException{
        checkCorrectUsage(usage);
        int buffer = GL15.glGenBuffers();
        GL15.glBindBuffer(target, buffer);
        FloatBuffer fBuffer = BufferUtils.createFloatBuffer(data.length);
        fBuffer.flip();
        fBuffer.put(data);
        GL15.glBufferData(target, fBuffer, usage);
    }
    
    private void checkCorrectUsage(int usage) throws IOException{
        if( (usage != GL15.GL_STREAM_READ ) ||
            (usage != GL15.GL_STREAM_COPY ) ||
            (usage != GL15.GL_STREAM_DRAW ) ||               
            (usage != GL15.GL_STATIC_COPY ) ||
            (usage != GL15.GL_STATIC_DRAW ) ||
            (usage != GL15.GL_STATIC_READ ) ||
            (usage != GL15.GL_DYNAMIC_COPY ) ||               
            (usage != GL15.GL_DYNAMIC_DRAW ) ||
            (usage != GL15.GL_DYNAMIC_READ )){
            throw new IOException("Invalid usage enum specifier:"+usage);
        }
            
        
    }

    @Override
    public void configure() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
