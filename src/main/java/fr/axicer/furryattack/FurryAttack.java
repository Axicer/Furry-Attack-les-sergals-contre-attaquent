package fr.axicer.furryattack;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_DECORATED;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetMonitors;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.axicer.furryattack.entity.Character;
import fr.axicer.furryattack.entity.Species;
import fr.axicer.furryattack.entity.control.Controller;
import fr.axicer.furryattack.entity.render.animation.CharacterAnimation;
import fr.axicer.furryattack.map.MapManager;
import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Renderer;
import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.util.Color;
import fr.axicer.furryattack.util.Constants;
import fr.axicer.furryattack.util.Util;
import fr.axicer.furryattack.util.config.FileManager;
import fr.axicer.furryattack.util.control.KeyboardHandler;
import fr.axicer.furryattack.util.control.MouseButtonHandler;
import fr.axicer.furryattack.util.control.MouseHandler;
import fr.axicer.furryattack.util.debug.Debugger;
import fr.axicer.furryattack.util.events.EventManager;
import fr.axicer.furryattack.util.font.FontType;
import fr.axicer.furryattack.util.lang.LanguageManager;

public class FurryAttack implements Renderable, Updateable{
	
	String[] args;
	
	// The window handle
	public long window = -1;
	public boolean running = true;
	public static int screenid;
	@SuppressWarnings("unused")
	private KeyboardHandler keyhandler;
	@SuppressWarnings("unused")
	private MouseHandler mousehandler;
	@SuppressWarnings("unused")
	private GLFWMouseButtonCallback mousebuttoncallback;
	public Matrix4f projectionMatrix;
	public Matrix4f viewMatrix;

	private Renderer renderer;
	private EventManager eventManager;
	private LanguageManager langManager;
	private MapManager mapManager;
	private Controller controller;
	
	private Logger logger = LoggerFactory.getLogger(FurryAttack.class);
	
	public void run() {
		logger.info("LWJGL " + Version.getVersion() + "!");
		logger.info("Initializing frame...");
		Debugger.startCounting();
		initFrame();
		logger.info("Done");
		Debugger.endCounting();
		logger.info("Setup game...");
		Debugger.startCounting();
		setupGame();
		logger.info("Done");
		Debugger.endCounting();
		
		loop();
		exit();
	}

	private void setupGame() {
		projectionMatrix = new Matrix4f().ortho(-Constants.WIDTH/2, Constants.WIDTH/2, -Constants.HEIGHT/2, Constants.HEIGHT/2, 0.1f, 1000.0f);
		viewMatrix = new Matrix4f().identity();

		eventManager = new EventManager();
		langManager = new LanguageManager();
		renderer = new Renderer();
		mapManager = new MapManager();
		
		controller = new Controller(new Character(Species.FOX, Color.WHITE, Color.BLACK, "", new CharacterAnimation("/anim/wolf_head_normal.anim", "/img/human_walk_texture.png")));
		
		//only show GUI at first launch
		renderer.getGUIRenderer().setActivated(false);
		renderer.getMapRenderer().setActivated(true);
	}
	
	private void initFrame() {
		//because OSX don't like awt fonts ><
		System.setProperty("java.awt.headless", "false");
		
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		logger.info("Initializing LWJGL...");
		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");
		logger.info("LWJGL initialized !");
		
		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will not be resizable
		glfwWindowHint(GLFW_DECORATED, Constants.FULLSCREEN ? GLFW_FALSE : GLFW_TRUE); //remove decoration
		
		long screen = glfwGetMonitors().get(screenid);
		
		// Create the window
		window = glfwCreateWindow(Constants.WIDTH, Constants.HEIGHT, Constants.TITLE, Constants.FULLSCREEN ? screen : NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		// Get the thread stack and push a new frame
		try ( MemoryStack stack = MemoryStack.stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(screen);

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
		glfwSwapInterval(Constants.V_SYNC ? 1 : 0);
		// Make the window visible
		glfwShowWindow(window);
		
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		// Set the clear color
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		FontType.initalize(Util.contains(args, "-forcechars"));
		
		glfwSetKeyCallback(window, keyhandler = new KeyboardHandler());
		glfwSetCursorPosCallback(window, mousehandler = new MouseHandler());
		glfwSetMouseButtonCallback(window, mousebuttoncallback = new MouseButtonHandler());
	}

	private void loop() {
		long lastTimeTick = System.nanoTime();
		long lastRenderTime = System.nanoTime();
		
		double tickTime = 1000000000.0/60.0;
		double renderTime = 1000000000.0/Constants.FPS;
		
		int frames = 0;
		int ticks = 0;
		
		long timer = System.currentTimeMillis();
		
		while ( !glfwWindowShouldClose(window) && running) {
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
				logger.info("frames: "+frames+" FPS, ticks: "+ticks);
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
			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
		}
	}
	
	public void exit() {
		//renderer.destroy();
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	public void updatesec() {}
	
	@Override
	public void update() {
		renderer.update();
		controller.update();
	}

	@Override
	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
		renderer.render();
		controller.render();
		glfwSwapBuffers(window); // swap the color buffers
	}
	
	public Renderer getRenderer() {
		return renderer;
	}
	
	public EventManager getEventManager() {
		return eventManager;
	}
	
	public LanguageManager getLangManager() {
		return langManager;
	}
	
	public MapManager getMapManager() {
		return mapManager;
	}
	
	//******************** Launch ********************//

	private static FurryAttack instance = null;
	private FurryAttack() {}
	public static FurryAttack getInstance() {
		if(instance == null)instance = new FurryAttack();
		return instance;
	}
	
	public static void main(String[] args) {
		getInstance().args = args;
		FileManager.initialize();
		Constants.initialize(args);
		screenid = Constants.MAIN_CONFIG.getInt("screenid",0);
		getInstance().run();
	}
}
