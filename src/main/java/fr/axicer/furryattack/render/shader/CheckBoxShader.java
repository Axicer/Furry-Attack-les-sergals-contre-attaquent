package fr.axicer.furryattack.render.shader;

public class CheckBoxShader extends AbstractShader{

	public CheckBoxShader() {
		super("checkbox/checkbox.vs", "checkbox/checkbox.gs", "checkbox/checkbox.fs");
		linkAndValidate();
	}

}
