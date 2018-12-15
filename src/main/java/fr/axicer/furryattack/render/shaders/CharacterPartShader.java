package fr.axicer.furryattack.render.shaders;

import org.joml.Matrix4f;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.util.Constants;

public class CharacterPartShader extends AbstractShader{
	
	public CharacterPartShader() {
		//TODO recreate shader content
		super("characterpart.vert", "characterpart.geom", "characterpart.frag");
		linkAndValidate();
	}

	public void fillShader(Matrix4f model) {
		bind();
		setUniformMat4f("projectionMatrix", new Matrix4f().ortho(0f, Constants.WIDTH, 0f, Constants.HEIGHT, 0.1f, 1000.0f));
	    setUniformMat4f("viewMatrix", FurryAttack.getInstance().viewMatrix);
	    setUniformMat4f("modelMatrix", model);
	    setUniformi("tex", 0);
	    unbind();
	}
	
	
}
