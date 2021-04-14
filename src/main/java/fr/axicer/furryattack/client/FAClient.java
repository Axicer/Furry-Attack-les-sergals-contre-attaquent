package fr.axicer.furryattack.client;

import fr.axicer.furryattack.client.control.handler.KeyboardHandler;
import fr.axicer.furryattack.client.control.handler.MouseButtonHandler;
import fr.axicer.furryattack.client.control.handler.MouseHandler;
import fr.axicer.furryattack.client.render.ClientRenderer;
import fr.axicer.furryattack.client.render.Loader;
import fr.axicer.furryattack.client.render.texture.Texture;
import fr.axicer.furryattack.client.update.ClientUpdater;
import fr.axicer.furryattack.common.events.EventListener;
import fr.axicer.furryattack.common.events.EventManager;
import fr.axicer.furryattack.common.map.background.Background;
import fr.axicer.furryattack.common.map.frame.Frame;
import fr.axicer.furryattack.common.map.layer.Layer;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

public class FAClient implements EventListener {

    public static final Logger LOGGER = LoggerFactory.getLogger(FAClient.class);

    private static final EventManager eventManager = new EventManager();
    private static ClientRenderer renderer;
    private static ClientUpdater updater;

    private KeyboardHandler keyboardHandler;
    private MouseHandler mouseHandler;
    private MouseButtonHandler mouseButtonHandler;
    private Thread contextThread;
    private static double xPos, yPos;
    private boolean running;

    Frame frame;

    public FAClient() {
        running = false;
    }

    private void initGame(){
        frame = new Frame(Layer.getStoneFullMap());
        updater.addUpdatableItem(frame);
        renderer.addRenderableItem(frame);
    }

    private void cleanUp(){
        Texture.cleanUp();
        Background.cleanUp();
        Loader.cleanUp();
        renderer.remove();
        updater.remove();
    }

    public static Vector2f getCursorPos() {
        return new Vector2f((float) (xPos/(double)renderer.getWindowWidth()), (float) (yPos/(double)renderer.getWindowHeight()));
    }

    /**
     * Stop the client by stopping the loop
     */
    public void stop(){
        running = false;
    }

    /**
     * start the loop of update, render and poll event
     */
    public void start() {
        if(running){
            LOGGER.error("Attempted to run loop but loop is already started !");
            return;
        }
        running = true;

        initGame();

        long lastTimeTick = System.nanoTime();
        long lastRenderTime = System.nanoTime();

        double tickTime = 1000000000.0/20.0; //20 TPS
        double renderTime = 1000000000.0/120.0; //120 FPS

        int frames = 0;
        int ticks = 0;

        long timer = System.currentTimeMillis();

        double[] xPosArray = new double[1];
        double[] yPosArray = new double[1];
        while ( !glfwWindowShouldClose(renderer.getWindowID()) && running) {
            if(System.nanoTime() - lastTimeTick > tickTime){
                updater.update();
                lastTimeTick += tickTime;
                ticks++;
            }
            if(System.nanoTime() - lastRenderTime > renderTime){
                renderer.render();
                lastRenderTime += renderTime;
                frames++;
            }
            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                LOGGER.info("frames: "+frames+" FPS, ticks: "+ticks);
                frames = 0;
                ticks = 0;
                eventManager.update();
            }

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
            org.lwjgl.glfw.GLFW.glfwGetCursorPos(renderer.getWindowID(), xPosArray, yPosArray);
            xPos = xPosArray[0];
            yPos = yPosArray[0];
        }

        stop();
        cleanUp();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(renderer.getWindowID());
        glfwDestroyWindow(renderer.getWindowID());

        // Terminate GLFW and free the error callback
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    /**
     * Init renderer
     */
    public void init(int width, int height, boolean fullscreen, boolean vsync, int screenID) throws RuntimeException{
        renderer = new ClientRenderer();
        renderer.createFrame(width, height, fullscreen, vsync, screenID);
        renderer.setProjectionMatrix(width, height);
        renderer.setViewMatrix(new Matrix4f().identity());
        contextThread = Thread.currentThread();

        updater = new ClientUpdater();

        GL11.glClearColor(0,0,0,1);

        //init mouse and keyboard
        glfwSetKeyCallback(renderer.getWindowID(), keyboardHandler = new KeyboardHandler(this));
        glfwSetCursorPosCallback(renderer.getWindowID(), mouseHandler = new MouseHandler());
        glfwSetMouseButtonCallback(renderer.getWindowID(), mouseButtonHandler = new MouseButtonHandler(this));
    }

    public static ClientRenderer getRenderer() {
        return renderer;
    }

    public static ClientUpdater getUpdater() {
        return updater;
    }

    public KeyboardHandler getKeyboardHandler() {
        return keyboardHandler;
    }

    public MouseHandler getMouseHandler() {
        return mouseHandler;
    }

    public MouseButtonHandler getMouseButtonHandler() {
        return mouseButtonHandler;
    }

    public static EventManager getEventManager() {
        return eventManager;
    }

    /**
     * Get the thread where OpenGL is running
     * @return Thread
     */
    public Thread getContextThread() {
        return contextThread;
    }
}
