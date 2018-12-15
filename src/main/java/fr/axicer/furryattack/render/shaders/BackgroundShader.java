package fr.axicer.furryattack.render.shaders;

public class BackgroundShader extends AbstractShader{

	public BackgroundShader() {
		super("background.vert", "background.geom", "background.frag");
		linkAndValidate();
	}	
}
