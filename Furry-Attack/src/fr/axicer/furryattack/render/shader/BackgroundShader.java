package fr.axicer.furryattack.render.shader;

public class BackgroundShader extends AbstractShader{

	public BackgroundShader() {
		super("background/background.vs", null, "background/background.fs");
		linkAndValidate();
	}
	
	
}
