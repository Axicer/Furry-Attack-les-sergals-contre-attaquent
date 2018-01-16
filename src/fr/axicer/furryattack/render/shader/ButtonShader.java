package fr.axicer.furryattack.render.shader;

public class ButtonShader extends AbstractShader{

	public ButtonShader() {
		super("button/button.vs", "button/button.gs", "button/button.fs");
		linkAndValidate();
	}

}
