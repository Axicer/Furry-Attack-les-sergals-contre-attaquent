package fr.axicer.furryattack.render.textures;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

public class Texture {
	public int ID, width, height;
	
	public Texture(int id, int width, int height) {
		this.ID = id;
		this.width = width;
		this.height = height;
	}
	
	public void bind(int sampler){
		glActiveTexture(GL_TEXTURE0+sampler);
		glBindTexture(GL_TEXTURE_2D, ID);
	}
	
	public static void unbind(){
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	private final static int BYTES_PER_PIXEL = 4;

	public static Texture loadTexture(BufferedImage image){
		
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
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
	
	    //Setup texture scaling filtering
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	    
	    //Send texel data to OpenGL
	    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
	    glBindTexture(GL_TEXTURE_2D, 0);
	    //Return the texture ID so we can bind it later again
	    return new Texture(textureID, image.getWidth(), image.getHeight());
	}
}
