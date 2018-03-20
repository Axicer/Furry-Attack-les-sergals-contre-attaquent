package fr.axicer.furryattack.render.shader;

public class CollisionBoxShader extends AbstractShader{

	public CollisionBoxShader() {
		super("collision/collision.vs", null, "collision/collision.fs");
		linkAndValidate();
	}

}
