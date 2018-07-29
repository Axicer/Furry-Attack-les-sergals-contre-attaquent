package fr.axicer.furryattack.render.shader;

public class BackgroundShader extends AbstractShader{

	public BackgroundShader() {
		super("background/background.vert", "background/background.geom", "background/background.frag");
		linkAndValidate();
	}
	
	
}
