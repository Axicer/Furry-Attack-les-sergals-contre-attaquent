package fr.axicer.furryattack.map;

import java.util.ArrayList;
import java.util.List;

import fr.axicer.furryattack.render.Destroyable;
import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;

/**
 * An asbtract map model
 * @author Axicer
 *
 */
public abstract class AbstractMap implements Renderable,Updateable,Destroyable{
	
	/**
	 * List of all obstacles in the map
	 */
	protected List<MapObstacle> obstacles;
	/**
	 * Name of the map
	 */
	protected String name;
	
	/**
	 * Constructor of an empty abstract map
	 * @param name {@link String} name of the map
	 */
	public AbstractMap(String name) {
		this.obstacles = new ArrayList<>();
		this.name = name;
	}

	/**
	 * Get the list of all obstacles in the map
	 * @return {@link List} of {@link MapObstacle} in the map
	 */
	public List<MapObstacle> getObstacles() {
		return obstacles;
	}
	
	/**
	 * @return {@link MapType} of the map
	 */
	public abstract MapType getType();
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[map]:\n");
		builder.append("\tname: "+name+"\n");
		builder.append("\tobstacles: "+obstacles+"\n");
		return builder.toString();
	}
}
