package fr.axicer.furryattack.render.shader;

public class ButtonShader extends AbstractShader{

	public ButtonShader() {
		super("button/button.vert", "button/button.geom", "button/button.frag");
		linkAndValidate();
	}

}
