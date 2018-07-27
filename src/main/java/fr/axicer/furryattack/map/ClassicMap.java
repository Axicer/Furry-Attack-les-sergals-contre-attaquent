package fr.axicer.furryattack.map;

/**
 * A simple map with nothing more
 * @author Axicer
 *
 */
public class ClassicMap extends AbstractMap {
	
	/**
	 * Constructor of a classic map
	 * @param name {@link String} name of the map
	 */
	public ClassicMap(String name) {
		super(name);
	}

	@Override
	public void render() {
		background.render();
		for(int i = obstacles.size()-1 ; i >= 0 ; i--) {
			obstacles.get(i).render();
		}
	}

	@Override
	public void update() {
		background.update();
		for(int i = obstacles.size()-1 ; i >= 0 ; i--) {
			obstacles.get(i).update();
		}
	}

	@Override
	public void destroy() {
		background.destroy();
		for(int i = obstacles.size()-1 ; i >= 0 ; i--) {
			obstacles.get(i).destroy();
		}
	}

	@Override
	public MapType getType() {
		return MapType.CLASSIC;
	}
}
