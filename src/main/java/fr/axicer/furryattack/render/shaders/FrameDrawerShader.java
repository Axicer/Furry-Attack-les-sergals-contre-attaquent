package fr.axicer.furryattack.render.shaders;

public class FrameDrawerShader extends AbstractShader{

	public FrameDrawerShader() {
		super("frameDrawer.vert", "frameDrawer.geom", "frameDrawer.frag");
		linkAndValidate();
	}
}
