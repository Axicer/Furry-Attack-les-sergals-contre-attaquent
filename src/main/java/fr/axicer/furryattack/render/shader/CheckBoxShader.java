package fr.axicer.furryattack.render.shader;

public class CheckBoxShader extends AbstractShader{

	public CheckBoxShader() {
		super("checkbox/checkbox.vert", "checkbox/checkbox.geom", "checkbox/checkbox.frag");
		linkAndValidate();
	}

}
