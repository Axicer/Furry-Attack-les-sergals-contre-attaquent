package fr.axicer.furryattack.render.shader;

public class BackgroundShader extends AbstractShader{

	public BackgroundShader() {
		super("background/background.vs", "background/background.gs", "background/background.fs");
		linkAndValidate();
	}
	
	
}
