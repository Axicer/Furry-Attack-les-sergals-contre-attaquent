package fr.axicer.furryattack.entity.render.animation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

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
	
	public void checkTicks() {
		if(actualTick >= tickCount) {
			actual++;
			if(actual >= countX*countY)actual=0;
		}
		actualTick %= tickCount;
	}
	
	public float getNormalisedSpriteWidth() {
		return 1f/((float)countX);
	}
	public float getNormalisedSpriteHeight() {
		return 1f/((float)countY);
	}
	
	public KeyFrame getActualFrame() {
		KeyFrame frame = new KeyFrame(actual%countX, actual/countX);
		return frame;
	}
}
