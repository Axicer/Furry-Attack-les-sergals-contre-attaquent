package fr.axicer.furryattack.render.shader;

public class CollisionBoxShader extends AbstractShader{

	public CollisionBoxShader() {
		super("collision/collision.vert", null, "collision/collision.frag");
		linkAndValidate();
	}

}
