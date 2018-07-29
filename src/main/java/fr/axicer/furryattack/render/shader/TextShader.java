package fr.axicer.furryattack.render.shader;

public class TextShader extends AbstractShader{

	public TextShader() {
		super("text/text.vert", null, "text/text.frag");
		linkAndValidate();
	}
	
}
