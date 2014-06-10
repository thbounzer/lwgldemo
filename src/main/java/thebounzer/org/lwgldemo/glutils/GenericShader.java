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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL20;

/**
 *
 * @author thbounzer
 */
public class GenericShader {
    
    private File shaderFile;
    private StringBuffer shaderSource = new StringBuffer();
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private boolean isCompiled = false;
    private int shaderId;
    private int programId;
    private String sourceFileName;
    
    public GenericShader(String shader, int type){
        shaderFile = new File(shader);
        sourceFileName = shaderFile.getName();
        BufferedReader reader = null;
        shaderId = GL20.glCreateShader(type);
        try {
            reader = new BufferedReader(new FileReader(shaderFile));
            String line;
            while ((line = reader.readLine()) != null) {
                    shaderSource.append(line).append("\n");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to load shader from:{0}", shaderFile.getAbsolutePath());
        } finally{
            IOUtils.closeQuietly(reader);
        }
    }

    public boolean isIsCompiled() {
        return isCompiled;
    }

    public void setIsCompiled(boolean isCompiled) {
        this.isCompiled = isCompiled;
    }

    public int getShaderId() {
        return shaderId;
    }

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }
    
    public StringBuffer getSource(){
        return shaderSource;
    }

    public String getSourceFileName() {
        return sourceFileName;
    }
    
    
}
