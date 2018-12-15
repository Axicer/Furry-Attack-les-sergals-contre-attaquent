package fr.axicer.furryattack.render.shaders;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.util.Constants;

public class TileShader extends AbstractShader{

	public TileShader() {
		super("tile.vert", "tile.geom", "tile.frag");
		linkAndValidate();
	}

	public void fillShader(float color_r, float color_g, float color_b, float color_a, float tile_size, Matrix4f model) {
		bind();
		Vector4f color = new Vector4f(color_r,color_g,color_b,color_a);
		setUniformvec4f("uColor", color);
		setUniformf("size", tile_size);
		// projection start from bottom left
		setUniformMat4f("projectionMatrix", new Matrix4f().ortho(0f, Constants.WIDTH, 0f, Constants.HEIGHT, 0.1f, 1000.0f));
		setUniformMat4f("viewMatrix", FurryAttack.getInstance().viewMatrix);
		setUniformMat4f("modelMatrix", model);
		unbind();
	}

}
