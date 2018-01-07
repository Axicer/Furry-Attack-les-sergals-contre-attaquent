package fr.axicer.furryattack.render.shader;

public class CharacterShader extends AbstractShader{

	public CharacterShader() {
		super("character/character.vs", null, "character/character.fs");
		linkAndValidate();
	}

}
