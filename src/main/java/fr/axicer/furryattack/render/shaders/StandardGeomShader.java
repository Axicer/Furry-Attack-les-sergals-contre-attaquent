package fr.axicer.furryattack.render.shaders;

public class StandardGeomShader extends AbstractShader{

	public StandardGeomShader() {
		super("standardGeom.vert", "standardGeom.geom", "standardGeom.frag");
		linkAndValidate();
	}

	@Override
	public void fillShader() {
		// TODO Auto-generated method stub
		
	}
}
