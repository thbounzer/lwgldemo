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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import thebounzer.org.lwgldemo.glutils.GenericShader;


/**
 *
 * @author thbounzer
 */
public class Chapter5 extends Chapter {
    
    Matrix4f projMat = perspectiveMat(50.0f,1.0f,1.1f,1000.0f);
    float[] vertex = new float[]{
        -0.25f,  0.25f, -0.25f,
        -0.25f, -0.25f, -0.25f,
         0.25f, -0.25f, -0.25f,
         0.25f, -0.25f, -0.25f,
         0.25f,  0.25f, -0.25f,
        -0.25f,  0.25f, -0.25f,
        -0.25f,  0.25f, -0.25f,
         0.25f,  0.25f, -0.25f,
         0.25f,  0.25f,  0.25f,
         0.25f,  0.25f,  0.25f,
        -0.25f,  0.25f,  0.25f,
        -0.25f,  0.25f, -0.25f};    
    
        @Override
    public void configure() {
        shaders.add(new GenericShader("src/main/resources/shadersCap5/shader.vert",GL20.GL_VERTEX_SHADER));
        shaders.add(new GenericShader("src/main/resources/shadersCap5/shader.frag",GL20.GL_FRAGMENT_SHADER));
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);   
        GL11.glPointSize(5.0f);
        try {
            createAndBindVertexObject(vertex);
        } catch (IOException ex) {
            Logger.getLogger(Chapter5.class.getName()).log(Level.SEVERE, null, ex);
        }
        shaderSetup();   
        FloatBuffer matrixProjBuff = BufferUtils.createFloatBuffer(16);
        projMat.store(matrixProjBuff);
        matrixProjBuff.flip();        
    }
    
    
    @Override
    public void loopCycle() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        float f = (float) (time * Math.PI * 0.1f);
        Matrix4f modelViewM = new Matrix4f();
        Matrix4f mat = new Matrix4f();
        Matrix4f transOne = new Matrix4f();
        Matrix4f transTwo = new Matrix4f();
        Matrix4f rotaOne = new Matrix4f();
        Matrix4f rotaTwo = new Matrix4f();
        Matrix4f.translate(new Vector3f(0.0f, 0.0f, -0.4f),mat,transOne);
        Matrix4f.translate(new Vector3f(
                (float) Math.sin(2.1f * f) * 0.5f,
                (float) Math.cos(1.7f * f) * 0.5f, 
                (float) Math.sin(1.3f * f) * (float) Math.cos(1.5 * f) * 2.0f),mat,transTwo);
        Matrix4f.rotate(time * 45.0f, new Vector3f(0.0f,1.0f,0.0f), mat, rotaOne);
        Matrix4f.rotate(time * 81.0f, new Vector3f(1.0f, 0.0f, 0.0f), mat,rotaTwo);
        Matrix4f.mul(modelViewM, transOne, modelViewM);
        Matrix4f.mul(modelViewM, transTwo, modelViewM);
        Matrix4f.mul(modelViewM, rotaOne, modelViewM);
        Matrix4f.mul(modelViewM, rotaTwo, modelViewM);
        
        GL20.glUseProgram(program.getId());
        FloatBuffer matrixBuf = BufferUtils.createFloatBuffer(16);
        modelViewM.store(matrixBuf);
        matrixBuf.flip();
        int uniLoc = GL20.glGetUniformLocation(program.getId(), "mv_matrix");
        GL20.glUniformMatrix4(uniLoc, false, matrixBuf);
        int uniProjMatLoc = GL20.glGetUniformLocation(program.getId(), "proj_matrix");
        GL20.glUniformMatrix4(uniProjMatLoc, false, matrixBuf);
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);	                
        GL11.glDrawArrays(GL11.GL_POINTS,0,36);
        GL20.glUseProgram(0);
        time += 0.001f;
    }

    @Override
    public void destroy() {
        program.destroy();
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoId);          
    }
    
    private void createAndBindVertexObject(float[] data) throws IOException{
        FloatBuffer fBuffer = BufferUtils.createFloatBuffer(data.length);
        fBuffer.put(data);
        fBuffer.flip();
        int vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fBuffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
    
    private Matrix4f perspectiveMat(float fov, float aspect, float znear, float zfar){
        Matrix4f perspect = new Matrix4f();
        
        float xymax = znear * (float) Math.tan(fov * (Math.PI/360));
        float ymin = -xymax;
        float xmin = -xymax;

        float width = xymax - xmin;
        float height = xymax - ymin;

        float depth = zfar - znear;
        float q = -(zfar + znear) / depth;
        float qn = -2 * (zfar * znear) / depth;

        float w = 2 * znear / width;
        w = w / aspect;
        float h = 2 * znear / height;
        perspect.m00 = w;
        perspect.m01 = 0;
        perspect.m02 = 0;
        perspect.m03 = 0;
        perspect.m10 = 0;
        perspect.m11 = h;
        perspect.m12 = 0;
        perspect.m13 = 0;
        perspect.m20 = 0;
        perspect.m21 = 0;
        perspect.m22 = q;
        perspect.m23 = -1;
        perspect.m30 = 0;
        perspect.m31 = 0;
        perspect.m32= qn;
        perspect.m33 = 0;
        return perspect;
    }
}


//byte[] indices = {
//				0, 1, 2,
//				2, 3, 0
//		};
//		indicesCount = indices.length;
//		ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indicesCount);
//		indicesBuffer.put(indices);
//		indicesBuffer.flip();
//		
//		// Create a new Vertex Array Object in memory and select it (bind)
//		vaoId = GL30.glGenVertexArrays();
//		GL30.glBindVertexArray(vaoId);
//		
//		// Create a new Vertex Buffer Object in memory and select it (bind) - VERTICES
//		vboId = GL15.glGenBuffers();
//		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
//		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
//		GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, 0, 0);
//		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
//		
//		// Create a new VBO for the indices and select it (bind) - COLORS
//		vbocId = GL15.glGenBuffers();
//		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbocId);
//		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorsBuffer, GL15.GL_STATIC_DRAW);
//		GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 0, 0);
//		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
//		
//		// Deselect (bind to 0) the VAO
//		GL30.glBindVertexArray(0);