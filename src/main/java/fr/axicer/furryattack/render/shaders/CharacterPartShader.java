package fr.axicer.furryattack.render.shaders;

import fr.axicer.furryattack.FurryAttack;

public class CharacterPartShader extends AbstractShader{
	
	public CharacterPartShader() {
		//TODO recreate shader content
		super("characterpart.vert", "characterpart.geom", "characterpart.frag");
		linkAndValidate();
	}

	public void fillShader() {
		setUniformMat4f("projectionMatrix", FurryAttack.getInstance().projectionMatrix);
	    setUniformMat4f("viewMatrix", FurryAttack.getInstance().viewMatrix);
	    setUniformi("tex", 0);
	}
	
	
}
