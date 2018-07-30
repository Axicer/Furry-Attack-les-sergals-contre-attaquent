package fr.axicer.furryattack.entity.animation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import fr.axicer.furryattack.entity.Species;
import fr.axicer.furryattack.render.Destroyable;
import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.render.textures.Texture;
import fr.axicer.furryattack.util.config.Configuration;

public class SpecifiedAnimation implements Updateable, Destroyable{

	private Texture texture;
	private Configuration config;
	protected int countX, countY, tickCount, actualTick, actual;
	protected AnimationsType type;
	protected Species race;
	
	public SpecifiedAnimation(Species race, AnimationsType type) {
		this.type = type;
		this.race = race;
		this.texture = Texture.loadTexture("/img/species/"+race.toString().toLowerCase()+type.getPathName()+".png", GL12.GL_CLAMP_TO_EDGE, GL11.GL_LINEAR);
		this.config = new Configuration("/anim/species/"+race.toString().toLowerCase()+type.getPathName()+".anim");
		this.countX = config.getInt("counts.x", 0);
		this.countY = config.getInt("counts.y", 0);
		this.tickCount = config.getInt("interval", 0);
		this.actual = 0;
		this.actualTick = 0;
	}
	
	public Texture getTexture() {
		return this.texture;
	}

	@Override
	public void destroy() {
		texture.delete();
	}

	@Override
	public void update() {
		actualTick++;
		if(actualTick >= tickCount) {
			actual++;
			if(actual >= countX*countY)actual=0;
		}
		actualTick %= tickCount;
	}
	
	@Override
	public String toString() {
		return race.toString()+type.getPathName();
	}
}
