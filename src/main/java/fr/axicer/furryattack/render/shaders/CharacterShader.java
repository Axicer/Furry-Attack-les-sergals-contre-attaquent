package fr.axicer.furryattack.render.shaders;

public class CharacterShader extends AbstractShader{

	public CharacterShader() {
		super("character.vert", "character.geom", "character.frag");
		linkAndValidate();
	}

}
