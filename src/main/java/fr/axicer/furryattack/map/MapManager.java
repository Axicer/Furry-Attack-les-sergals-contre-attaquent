package fr.axicer.furryattack.map;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.generator.maps.MapGenerator;
import fr.axicer.furryattack.util.config.Configuration;

/**
 * Map management class
 * @author Axicer
 *
 */
public class MapManager {
	
	/**
	 * The map actually used
	 */
	private AbstractMap map;
	
	/**
	 * Map manager constructor, create the default map on /maps/default.json
	 */
	public MapManager() {
		setMap(MapGenerator.createMap(new Configuration("/maps/default.json")));
	}
	
	/**
	 * Set the map to use and put it on the map renderer
	 * @param map {@link AbstractMap} to use
	 */
	public void setMap(AbstractMap map) {
		this.map = map;
		FurryAttack.getInstance().getRenderer().getMapRenderer().setMap(map);
	}
	
	/**
	 * get the the actual map used
	 * @return {@link AbstractMap} used
	 */
	public AbstractMap getMap() {
		return this.map;
	}
}
