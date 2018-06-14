package fr.axicer.furryattack.render.shader;

public class FrameDrawerShader extends AbstractShader{

	public FrameDrawerShader() {
		super("frameDrawer/frameDrawer.vs", "frameDrawer/frameDrawer.gs", "frameDrawer/frameDrawer.fs");
		linkAndValidate();
	}

}
