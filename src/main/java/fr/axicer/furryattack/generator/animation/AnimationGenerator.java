package fr.axicer.furryattack.generator.animation;

import fr.axicer.furryattack.entity.Species;
import fr.axicer.furryattack.entity.animation.Animation;
import fr.axicer.furryattack.generator.AbstractGenerator;

public class AnimationGenerator extends AbstractGenerator{
	
	private Species race;
	
	public AnimationGenerator(Species race) {
		this.race = race;
	}
	
	public Animation generate() {
		return new Animation(race);
	}
}
