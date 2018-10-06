package fr.axicer.furryattack.render.shaders;

public class TextShader extends AbstractShader{

	public TextShader() {
		super("text.vert", null, "text.frag");
		linkAndValidate();
	}
	
	@Override
	public void fillShader() {
		// TODO Auto-generated method stub
		
	}
}
