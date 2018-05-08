package fr.axicer.furryattack.map;

import org.joml.Vector2f;

import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.util.collision.CollisionBoxM;

/**
 * An obstacle in a {@link AbstractMap}
 * @author Axicer
 *
 */
public class MapObstacle extends CollisionBoxM implements Updateable{
	
	/**
	 * Create an obstacle from a given geometry
	 * @param corners array of {@link Vector2f} creating the geometry
	 */
	public MapObstacle(Vector2f... corners) {
		super(corners);
	}
	
	@Override
	public void render() {
		super.render();
	}
	
	@Override
	public void update() {}
	
	@Override
	public void destroy() {
		super.destroy();
	}
}
