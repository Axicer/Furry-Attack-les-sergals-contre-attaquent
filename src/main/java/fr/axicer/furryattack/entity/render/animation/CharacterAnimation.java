package fr.axicer.furryattack.entity.render.animation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import fr.axicer.furryattack.render.shaders.AbstractShader;
import fr.axicer.furryattack.render.textures.Texture;
import fr.axicer.furryattack.util.config.Configuration;

public class CharacterAnimation extends Configuration{
	
	private Texture texture;
	private int actual, countX, countY, actualTick, tickCount;
	
	public CharacterAnimation(String AnimPath, String texturePath) {
		super(AnimPath);
		this.actual = 0;
		this.actualTick = 0;
		this.countX = getInt("counts.x", 0);
		this.countY = getInt("counts.y", 0);
		this.tickCount = getInt("interval", 0);
		this.texture = Texture.loadTexture(texturePath, GL12.GL_CLAMP_TO_EDGE, GL11.GL_LINEAR);
	}
	
	public Texture getTexture() {
		return this.texture;
	}
	
	public void updateState() {
		actualTick++;
		checkTicks();
	}
	
	private void checkTicks() {
		if(actualTick >= tickCount) {
			actual++;
			if(actual >= countX*countY)actual=0;
		}
		actualTick %= tickCount;
	}
	
	/**
	 * Get the normalized sprite's width to use in the shader
	 * @return float normalized sprite's width
	 */
	public float getNormalisedSpriteWidth() {
		return 1f/((float)countX);
	}
	/**
	 * Get the normalized sprite's height to use in the shader
	 * @return float normalized sprite's height
	 */
	public float getNormalisedSpriteHeight() {
		return 1f/((float)countY);
	}
	
	/**
	 * Set the offset on both axis
	 * THIS FUNCTION IS ASSUMING THE SHADER IS BIND 
	 */
	public void setShaderVariables(AbstractShader shader) {
		shader.setUniformf("offsetX", getActualFrame().getPosX());
		shader.setUniformf("offsetY", getActualFrame().getPosY());
	}
	
	/**
	 * @return {@link KeyFrame} the actual key frame used actually
	 */
	public KeyFrame getActualFrame() {
		KeyFrame frame = new KeyFrame(actual%countX, actual/countX);
		return frame;
	}
}
