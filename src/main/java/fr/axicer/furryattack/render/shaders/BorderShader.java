package fr.axicer.furryattack.render.shaders;

public class BorderShader extends AbstractShader{

	public BorderShader() {
		super("border.vert", null, "border.frag");
		linkAndValidate();
	}
}
