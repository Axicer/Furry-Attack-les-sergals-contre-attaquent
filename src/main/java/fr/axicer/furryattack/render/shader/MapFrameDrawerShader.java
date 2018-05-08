package fr.axicer.furryattack.render.shader;

public class MapFrameDrawerShader extends AbstractShader{

	public MapFrameDrawerShader() {
		super("MapFrameDrawer/mapFrameDrawer.vs", "MapFrameDrawer/mapFrameDrawer.gs", "MapFrameDrawer/mapFrameDrawer.fs");
		linkAndValidate();
	}

}
