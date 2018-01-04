package fr.axicer.furryattack;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import fr.axicer.furryattack.render.Background;
import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.unused.MouseHandler;
import fr.axicer.furryattack.util.Constants;
import fr.axicer.furryattack.util.KeyboardHandler;

public class FurryAttack implements Renderable, Updateable{
	
	// The window handle
	private long window;
	private boolean running = true;
	@SuppressWarnings("unused")
	private KeyboardHandler keyhandler;
	@SuppressWarnings("unused")
	private MouseHandler mousehandler;
	@SuppressWarnings("unused")
	private GLFWMouseButtonCallback mousebuttoncallback;
	@SuppressWarnings("unused")
	private GLFWWindowSizeCallback windowSizeCallback;
	
	//public boolean MouseGrabbed = false;
	public Matrix4f projection;
	public Background back;
	
	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		init();
		loop();
		exit();
	}

	private void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

		// Create the window
		window = glfwCreateWindow(Constants.WIDTH, Constants.HEIGHT, Constants.TITLE, NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);
		// Make the window visible
		glfwShowWindow(window);
		
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		// Set the clear color
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		
		projection = new Matrix4f().ortho(-Constants.WIDTH/2, Constants.WIDTH/2, -Constants.HEIGHT/2, Constants.HEIGHT/2, 0.1f, 1000.0f);
		back = new Background();
		
		glfwSetKeyCallback(window, keyhandler = new KeyboardHandler());
		glfwSetCursorPosCallback(window, mousehandler = new MouseHandler());
		/*glfwSetMouseButtonCallback(window, mousebuttoncallback = new GLFWMouseButtonCallback() {
			@Override
			public void invoke(long window, int button, int action, int mods) {
				if(!MouseGrabbed && action == GLFW.GLFW_PRESS) {
					MouseGrabbed = true;
					glfwSetCursorPos(window, Constants.WIDTH/2, Constants.HEIGHT/2);
					MouseHandler.mouseX = Constants.WIDTH/2;
					MouseHandler.mouseY = Constants.HEIGHT/2;
					glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
					MouseHandler.getDX();
					MouseHandler.getDY();
				}
			}
		});*/
		glfwSetWindowSizeCallback(window, windowSizeCallback = new GLFWWindowSizeCallback(){
            @Override
            public void invoke(long window, int width, int height){
            	Constants.WIDTH = width;
            	Constants.HEIGHT = height;
            	back.recalculate(width, height);
            	GL11.glViewport(0, 0, width, height);
            	projection = new Matrix4f().ortho(-width/2, width/2, -height/2, height/2, 0.1f, 1000.0f);
            }
        });
	}

	private void loop() {
		long lastTimeTick = System.nanoTime();
		long lastRenderTime = System.nanoTime();
		
		double tickTime = 1000000000.0/60.0;
		double renderTime = 1000000000.0/Constants.FPS;
		
		int frames = 0;
		int ticks = 0;
		
		long timer = System.currentTimeMillis();
		
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( !glfwWindowShouldClose(window) && running) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
			
			if(System.nanoTime() - lastTimeTick > tickTime){
				update();
				lastTimeTick += tickTime;
				ticks++;
			}
			if(System.nanoTime() - lastRenderTime > renderTime){
				render();
				lastRenderTime += renderTime;
				frames++;
			}
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				System.out.println("frames: "+frames+" FPS, ticks: "+ticks);
				frames = 0;
				ticks = 0;
				updatesec();
			}else{
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			glfwSwapBuffers(window); // swap the color buffers
			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
		}
	}
	
	public void exit() {
		back.destroy();
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	public void updatesec() {
		
	}
	
	@Override
	public void update() {
		/*if(KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
			MouseGrabbed = false;
			 glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		}
		if(MouseGrabbed) {
			glfwSetCursorPos(window, Constants.WIDTH/2, Constants.HEIGHT/2);
		}*/
		/*double[] x = new double[1];
		double[] y = new double[1];
		glfwGetCursorPos(window, x, y);
		System.out.println(x[0]+","+y[0]);*/
	}

	@Override
	public void render() {
		back.render();
	}
	
	
	public static FurryAttack instance;

	public static FurryAttack getInstance() {
		return instance;
	}
	
	public static void main(String[] args) {
		instance = new FurryAttack();
		instance.run();
	}
}
