package fr.axicer.furryattack.render.shader;

public class CharacterShader extends AbstractShader{

	public CharacterShader() {
		super("character/character.vs", "character/character.gs", "character/character.fs");
		linkAndValidate();
	}

}
