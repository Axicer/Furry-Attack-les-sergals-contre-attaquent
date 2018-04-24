package fr.axicer.furryattack.render.shader;

public class GUIFrameDrawerShader extends AbstractShader{

	public GUIFrameDrawerShader() {
		super("GUIFrameDrawer/GUIFrameDrawer.vs", "GUIFrameDrawer/GUIFrameDrawer.gs", "GUIFrameDrawer/GUIFrameDrawer.fs");
		linkAndValidate();
	}

}
