package fr.axicer.furryattack.render.shader;

public class MapFrameDrawerShader extends AbstractShader{

	public MapFrameDrawerShader() {
		super("MapFrameDrawer/mapFrameDrawer.vert", "MapFrameDrawer/mapFrameDrawer.geom", "MapFrameDrawer/mapFrameDrawer.frag");
		linkAndValidate();
	}

}
