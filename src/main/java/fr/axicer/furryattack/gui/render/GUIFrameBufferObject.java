package fr.axicer.furryattack.gui.render;

import static org.lwjgl.opengl.GL11.*;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import fr.axicer.furryattack.util.Constants;

/**
 * Contains the FBO's information relative to the GUI
 * @author Axicer
 *
 */
public class GUIFrameBufferObject {
	
	private int GuiFrameBufferId;
    private int GuiTextureId;
    private int GuiDepthBufferId;
    
    /**
     * Create and initialize the GUI's FBO
     */
    public GUIFrameBufferObject() {
    	initializeGuiFrameBuffer();
    }
    
    /**
     * Get the frame buffer ID
     * @return int
     */
    public int getGuiFrameBufferId() {
		return GuiFrameBufferId;
	}

    /**
     * Get the Texture ID
     * @return int
     */
	public int getGuiTextureId() {
		return GuiTextureId;
	}

	/**
	 * Get the DepthBuffer ID
	 * @return int
	 */
	public int getGuiDepthBufferId() {
		return GuiDepthBufferId;
	}

	/**
	 * Bind the GUI frameBuffer
	 */
	public void bindGuiFrameBuffer() {//call before rendering to this FBO
        bindFrameBuffer(GuiFrameBufferId,Constants.WIDTH,Constants.HEIGHT);
    }
	
	/**
	 * Bind a given frameBuffer to OpenGL
	 * @param frameBuffer
	 * @param width
	 * @param height
	 */
	private void bindFrameBuffer(int frameBuffer, int width, int height){
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);//To make sure the texture isn't bound
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
        GL11.glViewport(0, 0, width, height);
    }
	
	/**
	 * Unbind the current frameBuffer, switching to the default one
	 */
	public void unbindCurrentFrameBuffer() {//call to switch to default frame buffer
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glViewport(0, 0, Constants.WIDTH, Constants.HEIGHT);
    }
	
	/**
	 * Clear the buffer
	 */
	public void clearBuffer() {
		bindGuiFrameBuffer();
		GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		unbindCurrentFrameBuffer();
	}
    
	/**
	 * Initialize the Frame Buffer to the default width and height
	 */
    private void initializeGuiFrameBuffer() {
    	GuiFrameBufferId = createFrameBuffer();
    	GuiTextureId = createTextureAttachment(Constants.WIDTH,Constants.HEIGHT);
    	GuiDepthBufferId = createDepthBufferAttachment(Constants.WIDTH,Constants.HEIGHT);
        unbindCurrentFrameBuffer();
    }
    
    /**
     * Get a new FrameBuffer and bind it
     * @return int id
     */
    private int createFrameBuffer() {
        int frameBuffer = GL30.glGenFramebuffers();
        //generate name for frame buffer
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
        //create the framebuffer
        GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
        //indicate that we will always render to color attachment 0
        return frameBuffer;
    }
    
    /**
     * Create a new empty texture and bind it
     * @param width
     * @param height
     * @return int texture id
     */
    private int createTextureAttachment( int width, int height) {
        int texture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, texture, 0);
        return texture;
    }
    
    /**
     * Create a new depthBuffer and bind it
     * @param width
     * @param height
     * @return int depthBuffer id
     */
    private int createDepthBufferAttachment(int width, int height) {
        int depthBuffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width, height);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthBuffer);
        return depthBuffer;
    }
    
    /**
     * Destroy all inside the FBO
     */
    public void cleanUp() {//call when closing the game
        GL30.glDeleteFramebuffers(GuiFrameBufferId);
        GL11.glDeleteTextures(GuiTextureId);
        GL30.glDeleteRenderbuffers(GuiDepthBufferId);
    }
}
