package fr.axicer.furryattack.render.shader;

public class BorderShader extends AbstractShader{

	public BorderShader() {
		super("collision/border.vert", null, "collision/border.frag");
		linkAndValidate();
	}
}
