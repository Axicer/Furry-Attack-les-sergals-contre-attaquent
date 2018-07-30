package fr.axicer.furryattack.render.shaders;

public class StandardShader extends AbstractShader{

	public StandardShader() {
		super("standard.vert", null, "standard.frag");
		linkAndValidate();
	}

}
