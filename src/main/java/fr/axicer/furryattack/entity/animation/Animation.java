package fr.axicer.furryattack.entity.animation;

import fr.axicer.furryattack.entity.Species;
import fr.axicer.furryattack.render.Destroyable;
import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.render.shaders.AbstractShader;
import fr.axicer.furryattack.render.textures.Texture;

public class Animation implements Updateable, Destroyable{
	
	private SpecifiedAnimation stay;
	private SpecifiedAnimation walk;
	private SpecifiedAnimation jump;
	private SpecifiedAnimation shift;
	
	private SpecifiedAnimation actualAnimation;
	
	public Animation(Species race) {
		this.stay = new SpecifiedAnimation(race, AnimationsType.STAY);
		this.walk = new SpecifiedAnimation(race, AnimationsType.WALK);
		this.jump = new SpecifiedAnimation(race, AnimationsType.JUMP);
		this.shift = new SpecifiedAnimation(race, AnimationsType.SHIFT);
		
		this.actualAnimation = this.stay;
	}
	
	public void setAnimationType(AnimationsType type) {
		//if it's the same don't do anything
		if(this.actualAnimation.type == type)return;
		
		//reset old animation ticks
		//this.actualAnimation.actualTick = 0;
		//this.actualAnimation.actual = 0;
		
		//change to the new animation
		switch(type){
			case STAY:
				this.actualAnimation = stay;
				break;
			case WALK:
				this.actualAnimation = walk;
				break;
			case JUMP:
				this.actualAnimation = jump;
				break;
			case SHIFT:
				this.actualAnimation = shift;
				break;
			default:
				break;
		}
	}
	
	public AnimationsType getActualAnimationType() {
		return this.actualAnimation.type;
	}
	
	public Texture getTexture() {
		return this.actualAnimation.getTexture();
	}
	
	/**
	 * Get the normalized sprite's width to use in the shader
	 * @return float normalized sprite's width
	 */
	public float getNormalisedSpriteWidth() {
		return 1f/((float)actualAnimation.countX);
	}
	/**
	 * Get the normalized sprite's height to use in the shader
	 * @return float normalized sprite's height
	 */
	public float getNormalisedSpriteHeight() {
		return 1f/((float)actualAnimation.countY);
	}
	
	/**
	 * Set the offset on both axis
	 * THIS FUNCTION IS ASSUMING THE SHADER IS BOUND 
	 */
	public void setShaderVariables(AbstractShader shader) {
		shader.setUniformf("offsetX", getActualFrame().getPosX());
		shader.setUniformf("offsetY", getActualFrame().getPosY());
	}
	
	/**
	 * @return {@link KeyFrame} the actual key frame used actually
	 */
	public KeyFrame getActualFrame() {
		KeyFrame frame = new KeyFrame(actualAnimation.actual%actualAnimation.countX, actualAnimation.actual/actualAnimation.countX);
		return frame;
	}

	@Override
	public void update() {
		actualAnimation.update();
	}

	@Override
	public void destroy() {
		stay.destroy();
		walk.destroy();
		jump.destroy();
		shift.destroy();
	}
}
