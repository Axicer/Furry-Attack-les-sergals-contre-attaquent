package fr.axicer.furryattack.entity.character.old;

import org.joml.Vector2f;

import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.util.Constants;

public class CharacterRenderer implements Renderable, Updateable{
	
	private CharacterOld character;
	private CharacterPart chest;
	//private CharacterPart legL;
	
	public CharacterRenderer(CharacterOld character) {
		this.character = character;
		this.chest = new CharacterPart(CharacterPartsType.CHEST, character.getRace(), new Vector2f(character.getPosition()).add(0, CharacterPartsType.LEG.getHeight()*Constants.HEIGHT));
		//this.legL = new CharacterPart(CharacterPartsType.LEG, character.getRace(), new Vector2f(character.getPosition()).add((CharacterPartsType.LEG.getWidth()/2f)*Constants.WIDTH, 0f));
	}
	
	@Override
	public void render() {
		chest.render();
		//legL.render();
	}
	
	@Override
	public void update() {
		chest.pos = new Vector2f(character.getPosition()).add(0, CharacterPartsType.LEG.getHeight()*Constants.HEIGHT);
		//legL.pos = new Vector2f(character.getPosition()).add((CharacterPartsType.LEG.getWidth()/2f)*Constants.WIDTH, 0f);
		chest.update();
		//legL.update();
	}
}
