package fr.axicer.furryattack.render.shader;

public class CharacterShader extends AbstractShader{

	public CharacterShader() {
		super("character/character.vert", "character/character.geom", "character/character.frag");
		linkAndValidate();
	}

}
