package fr.axicer.furryattack.render.shader;

public class BorderShader extends AbstractShader{

	public BorderShader() {
		super("collision/border.vs", null, "collision/border.fs");
		linkAndValidate();
	}
}
