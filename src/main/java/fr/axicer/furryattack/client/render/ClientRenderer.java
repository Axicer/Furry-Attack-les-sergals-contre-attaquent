package fr.axicer.furryattack.client.render;

import fr.axicer.furryattack.client.FAClient;
import fr.axicer.furryattack.client.control.KeyPressedEvent;
import fr.axicer.furryattack.common.entity.Removable;
import fr.axicer.furryattack.common.entity.Renderable;
import fr.axicer.furryattack.common.events.EventListener;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

public class ClientRenderer implements Renderable, Removable, EventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientRenderer.class);

    private boolean frameCreated;
    private long windowID;
    private int windowWidth, windowHeight;
    private Matrix4f projectionMatrix, viewMatrix;
    private boolean wireframeMode = false;

    private final FAClient client;
    private final Set<Renderable> renderableSet;

    public ClientRenderer(FAClient client) {
        frameCreated = false;
        renderableSet = new HashSet<>();
        this.client = client;
        client.getEventManager().addListener(this);
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    public void setProjectionMatrix(int width, int height) {
        projectionMatrix = new Matrix4f().ortho(-width / 2, width / 2, -height / 2, height / 2, 0.1f, 1000.0f);
    }

    public void setViewMatrix(Matrix4f viewMatrix) {
        this.viewMatrix = viewMatrix;
    }

    /**
     * Create the frame
     */
    public void createFrame(int width, int height, boolean fullscreen, boolean vsync, int screenID) throws RuntimeException {
        if (frameCreated) {
            LOGGER.error("Frame is already created !");
            return;
        }

        initGL();
        createContext(width, height, fullscreen, screenID, vsync);

        frameCreated = true;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    private void initGL() throws IllegalStateException {
        //because OSX don't like awt fonts ><
        System.setProperty("java.awt.headless", "false");

        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        LOGGER.info("Initializing LWJGL...");
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");
        LOGGER.info("LWJGL initialized !");
    }

    private void createContext(int width, int height, boolean fullscreen, int screenID, boolean vsync) throws RuntimeException {
        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will not be resizable
        glfwWindowHint(GLFW_DECORATED, fullscreen ? GLFW_FALSE : GLFW_TRUE); //remove decoration

        long screen = glfwGetMonitors().get(screenID);

        // Create the window
        windowID = glfwCreateWindow(width, height, "Awesome AAA incoming", fullscreen ? screen : NULL, NULL);
        if (windowID == NULL)
            throw new RuntimeException("Failed to create the GLFW window");
        windowWidth = width;
        windowHeight = height;

        // Get the thread stack and push a new frame
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(windowID, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(screen);

            // Center the window
            glfwSetWindowPos(
                    windowID,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowID);
        // Enable v-sync
        glfwSwapInterval(vsync ? 1 : 0);
        // Make the window visible
        glfwShowWindow(windowID);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public long getWindowID() {
        return windowID;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    /**
     * Add an item to render
     *
     * @param toRender {@link Renderable} item to render
     * @return true if added false otherwise
     */
    public boolean addRenderableItem(Renderable toRender) {
        return renderableSet.add(toRender);
    }

    /**
     * Remove an item from rendering
     *
     * @param toRemove {@link Renderable} item to remove
     * @return Renderable item if removed null otherwise
     */
    public Renderable removeRenderableItem(Renderable toRemove) {
        return renderableSet.remove(toRemove) ? toRemove : null;
    }

    @Override
    public void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        if(wireframeMode){
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        }else{
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        }
        renderableSet.forEach(Renderable::render);
        glfwSwapBuffers(windowID); // swap the color buffers
    }

    @Override
    public void remove() {
        renderableSet.stream()
                .filter(renderable -> renderable.getClass().isAssignableFrom(Removable.class))
                .forEach(renderable -> ((Removable) renderable).remove());
    }

    public void onKeyPressed(KeyPressedEvent ev){
        if(ev.getKey() == GLFW_KEY_F){
            wireframeMode = !wireframeMode;
        }
    }
}
