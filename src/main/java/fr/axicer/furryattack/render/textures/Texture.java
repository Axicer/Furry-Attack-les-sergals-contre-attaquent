package fr.axicer.furryattack.render.textures;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

public class Texture {
	
	public int ID, width, height, wrapMode, filtermode;
	
	public Texture(int id, int width, int height, int wrapMode, int filterMode) {
		this.ID = id;
		this.width = width;
		this.height = height;
		this.wrapMode = wrapMode;
		this.filtermode = filterMode;
	}
	
	public void bind(int sampler){
		glActiveTexture(GL_TEXTURE0+sampler);
		glBindTexture(GL_TEXTURE_2D, ID);
	}
	
	public static void unbind(){
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void delete() {
		glDeleteTextures(ID);
	}
	
	private final static int BYTES_PER_PIXEL = 4;

	public static Texture loadTexture(String path, int wrapMode, int filterMode) {
		return loadTexture(Texture.class.getResourceAsStream(path), wrapMode, filterMode);
	}
	
	public static Texture loadTexture(InputStream stream, int wrapMode, int filterMode){
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	    int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
	
	    ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL); //4 for RGBA, 3 for RGB
	    
	    for(int y = 0; y < image.getHeight(); y++){
	       for(int x = 0; x < image.getWidth(); x++){
	           int pixel = pixels[y * image.getWidth() + x];
	           buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
	           buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
	           buffer.put((byte) (pixel & 0xFF));               // Blue component
	           buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
	       }
	    }
	
	    buffer.flip(); //FOR THE LOVE OF GOD DO NOT FORGET THIS
	
	    // You now have a ByteBuffer filled with the color data of each pixel.
	    // Now just create a texture ID and bind it. Then you can load it using 
	    // whatever OpenGL method you want, for example:
	    int textureID = glGenTextures(); //Generate texture ID
	    glBindTexture(GL_TEXTURE_2D, textureID);
	    //Setup wrap mode
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrapMode);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrapMode);
	
	    //Setup texture scaling filtering
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filterMode);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filterMode);
	    
	    //Send texel data to OpenGL
	    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
	    glBindTexture(GL_TEXTURE_2D, 0);
	    //Return the texture ID so we can bind it later again
	    try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return new Texture(textureID, image.getWidth(), image.getHeight(), wrapMode, filterMode);
	}
}