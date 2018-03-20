package fr.axicer.furryattack.render.shader;

public class TextShader extends AbstractShader{

	public TextShader() {
		super("text/text.vs", null, "text/text.fs");
		linkAndValidate();
	}
	
}
