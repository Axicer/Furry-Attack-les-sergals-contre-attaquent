package fr.axicer.furryattack.render.shader;

public class FrameDrawerShader extends AbstractShader{

	public FrameDrawerShader() {
		super("frameDrawer/frameDrawer.vert", "frameDrawer/frameDrawer.geom", "frameDrawer/frameDrawer.frag");
		linkAndValidate();
	}

}
