package fr.axicer.furryattack.client.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import java.nio.ByteBuffer;

public class FrameBufferObject {

    private int frameBufferId;
    private int textureId;
    private int depthBufferId;
    private final int fboWidth, fboHeight;

    public FrameBufferObject(int width, int height) {
        fboWidth = width;
        fboHeight = height;
        init();
    }

    public int getFrameBufferId() {
        return frameBufferId;
    }

    public int getTextureId() {
        return textureId;
    }

    public int getDepthBufferId() {
        return depthBufferId;
    }

    /**
     * Destroy all inside the FBO
     */
    public void clean() {//call when closing the game
        GL30.glDeleteFramebuffers(frameBufferId);
        GL11.glDeleteTextures(textureId);
        GL30.glDeleteRenderbuffers(depthBufferId);
    }

    /**
     * Bind a given frameBuffer to OpenGL
     */
    public void bind(){
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);//To make sure the texture isn't bound
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBufferId);
        GL11.glViewport(0, 0, fboWidth, fboHeight);
    }

    /**
     * Unbind the current frameBuffer, switching to the default one
     * @param width viewport to use when rendering to default FBO
     * @param height viewport to use when rendering to default FBO
     */
    public void unbind(int width, int height) {//call to switch to default frame buffer
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glViewport(0, 0, width, height);
    }

    /**
     * Init the frame buffer
     */
    private void init() {
        frameBufferId = createFrameBuffer();
        textureId = createTextureAttachment(fboWidth, fboHeight);
        depthBufferId = createDepthBufferAttachment(fboWidth, fboHeight);
        unbind(fboWidth, fboHeight);
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
     * @param width texture width
     * @param height texture height
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
     * @param width depth buffer width
     * @param height depth buffer height
     * @return int depthBuffer id
     */
    private int createDepthBufferAttachment(int width, int height) {
        int depthBuffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width, height);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthBuffer);
        return depthBuffer;
    }
}
