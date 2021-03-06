package thebounzer.org.lwgldemo;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL43;
import thebounzer.org.lwgldemo.glutils.GenericShader;
import thebounzer.org.lwgldemo.glutils.OpenGLDisplay;
import thebounzer.org.lwgldemo.glutils.ShaderProgram;


public class Main {

// Entry point for the application
	public static void main(String[] args) throws IOException {
		new OglBook();
	}
	
	// Setup variables
	private final String WINDOW_TITLE = "The Quad: colored";
	private final int WIDTH = 320;
	private final int HEIGHT = 240;
	// Quad variables
	private int vaoId = 0;
	private int vboId = 0;
	private int vbocId = 0;
	private int vboiId = 0;
	private int indicesCount = 0;
	// Shader variables
	private ShaderProgram program;
	
	public Main() throws IOException {
		// Initialize OpenGL (Display)
		this.setupOpenGL();
		
		//this.setupQuad();
		this.setupShaders();
		
		while (!Display.isCloseRequested()) {
			// Do a single loop (logic/render)
			this.loopCycle();
			
			// Force a maximum FPS of about 60
			Display.sync(60);
			// Let the CPU synchronize with the GPU if GPU is tagging behind
			Display.update();
		}
		
		// Destroy OpenGL (Display)
		this.destroyOpenGL();
	}

	public void setupOpenGL() {
                OpenGLDisplay.init(WIDTH, HEIGHT, WINDOW_TITLE);
		
		// Setup an XNA like background color
		GL11.glClearColor(0.4f, 0.6f, 0.9f, 0.5f);
		
		// Map the internal OpenGL coordinate system to the entire screen
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
	}
	
	public void setupQuad() {
		// Vertices, the order is not important. XYZW instead of XYZ
		float[] vertices = {
				-0.5f, 0.5f, 0f, 1f,
				-0.5f, -0.5f, 0f, 1f,
				0.5f, -0.5f, 0f, 1f,
		};
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
		verticesBuffer.put(vertices);
		verticesBuffer.flip();
                
		
		float[] colors = {
				1f, 0f, 0f, 1f,
				0f, 1f, 0f, 1f,
				0f, 0f, 1f, 1f,
		};
		FloatBuffer colorsBuffer = BufferUtils.createFloatBuffer(colors.length);
		colorsBuffer.put(colors);
		colorsBuffer.flip();
		
		// OpenGL expects to draw vertices in counter clockwise order by default
		byte[] indices = {
				0, 1, 2,
				2, 3, 0
		};
		indicesCount = indices.length;
		ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indicesCount);
		indicesBuffer.put(indices);
		indicesBuffer.flip();
		
		// Create a new Vertex Array Object in memory and select it (bind)
		vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoId);
		
		// Create a new Vertex Buffer Object in memory and select it (bind) - VERTICES
		vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		// Create a new VBO for the indices and select it (bind) - COLORS
		vbocId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbocId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorsBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		// Deselect (bind to 0) the VAO
		GL30.glBindVertexArray(0);
		
		// Create a new VBO for the indices and select it (bind) - INDICES
		vboiId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	private void setupShaders() throws IOException {
		
		// Load the vertex shader
                GenericShader vertex = new GenericShader("src/main/resources/shaders/screen.vert",GL20.GL_VERTEX_SHADER); 
		// Load the fragment shader
                GenericShader fragment = new GenericShader("src/main/resources/shaders/screen.frag",GL20.GL_FRAGMENT_SHADER);
                
                ArrayList<GenericShader> shaders = new ArrayList<GenericShader>();
                shaders.add(vertex);
                shaders.add(fragment);
		HashMap<Integer,String> attributes = new HashMap<Integer, String>();
                attributes.put(0, "in_Position");
                attributes.put(1, "in_Color");
		// Create a new shader program that links both shaders
		program = new ShaderProgram();
                program.compile(shaders, attributes);
	}
	
	public void loopCycle() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		GL20.glUseProgram(program.getId());
                
                vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoId);		
                
		// Bind to the VAO that has all the information about the vertices
//		GL30.glBindVertexArray(vaoId);
//		GL20.glEnableVertexAttribArray(0);
//		GL20.glEnableVertexAttribArray(1);
		
		// Bind to the index VBO that has all the information about the order of the vertices
//		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
		
		// Draw the vertices
                //GL11.glPointSize(10.0f);
		//GL11.glDrawElements(GL11.GL_POINTS, indicesCount, GL11.GL_UNSIGNED_BYTE, 0);
		GL11.glDrawArrays(GL11.GL_LINE_LOOP, 0, 3);
		// Put everything back to default (deselect)
//		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
//		GL20.glDisableVertexAttribArray(0);
//		GL20.glDisableVertexAttribArray(1);
//		GL30.glBindVertexArray(0);
		GL20.glUseProgram(0);
	}
	
	public void destroyOpenGL() {
		// Delete the shaders
                program.destroy();
		
//		// Select the VAO
//		GL30.glBindVertexArray(vaoId);
//		
//		// Disable the VBO index from the VAO attributes list
//		GL20.glDisableVertexAttribArray(0);
//		GL20.glDisableVertexAttribArray(1);
//		
//		// Delete the vertex VBO
//		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
//		GL15.glDeleteBuffers(vboId);
//		
//		// Delete the color VBO
//		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
//		GL15.glDeleteBuffers(vbocId);
//		
//		// Delete the index VBO
//		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
//		GL15.glDeleteBuffers(vboiId);
		
		// Delete the VAO
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(vaoId);
		
		Display.destroy();
	}
	
}