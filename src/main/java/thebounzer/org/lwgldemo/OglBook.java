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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.Util;

import thebounzer.org.lwgldemo.glutils.OpenGLDisplay;
import thebounzer.org.lwgldemo.glutils.ShaderProgram;


/**
 *
 * @author thbounzer
 */
public class OglBook {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;
    private static final String TITLE = "OGL BOOK EXAMPLES";
    private static final Logger logger = Logger.getLogger(ShaderProgram.class.getName());
    
    
    public OglBook(){
        // Initialize OpenGL (Display)
        this.setupOpenGL();
        this.printOpenglInfo();
        Util.checkGLError();
        Chapter chapter = new Chapter3();
        chapter.shaderSetup();
        // Setup background color
        float[] color = new float[]{0.0f,0.0f,0.0f,1.0f};
        GL11.glClearColor(color[0],color[1],color[2],color[3]);
        while (!Display.isCloseRequested()) {
                // Do a single loop (logic/render)
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
                chapter.loopCycle();
                // Force a maximum FPS of about 60
                Display.sync(60);
                // Let the CPU synchronize with the GPU if GPU is tagging behind
                Display.update();
        }

        // Destroy OpenGL (Display)
        chapter.destroy();
        this.destroyDisplay();
        
    }

    public void setupOpenGL() {
        OpenGLDisplay.init(WIDTH, HEIGHT, TITLE);
        // Map the internal OpenGL coordinate system to the entire screen
        GL11.glViewport(0, 0, WIDTH, HEIGHT);
    }
    
    
    public void destroyDisplay() {
        Display.destroy();
    }
    
    private void printOpenglInfo(){
        StringBuffer info = new StringBuffer();
        String newline = "\n";
        info.append(GL11.glGetString(GL11.GL_VERSION).concat(newline));
        info.append(GL11.glGetString(GL11.GL_VENDOR).concat(newline));
        info.append(GL11.glGetString(GL11.GL_RENDERER).concat(newline));
        int extensions = GL11.glGetInteger(GL30.GL_NUM_EXTENSIONS);
        info.append(getSupportedExtensions(extensions));
        logger.log(Level.INFO, "OpenGL version info: {0}", info);
    }
        
    private String getSupportedExtensions(int extensionsQty){
        int i;
        StringBuffer supportedExtensions = new StringBuffer("Supported extensions are:\n");
        for (i=0;i<extensionsQty;i++){
            supportedExtensions.append(GL30.glGetStringi(GL11.GL_EXTENSIONS, i).concat("\n"));
        }
        return supportedExtensions.toString();
    }    
}
