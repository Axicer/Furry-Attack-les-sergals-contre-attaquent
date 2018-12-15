package fr.axicer.furryattack.render.shaders;

public class GunShader extends AbstractShader {

	public GunShader() {
		super("gun.vert", "gun.geom", "gun.frag");
		linkAndValidate();
	}
}
