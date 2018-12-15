package fr.axicer.furryattack.map.old;

import java.util.ArrayList;
import java.util.List;

import fr.axicer.furryattack.gui.elements.GUIImage;
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
	 * background image
	 */
	protected GUIImage background;
	/**
	 * Name of the map
	 */
	protected String name;
	/**
	 * Gravity on this map
	 */
	protected float mapGravity;
	
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
	 * Returns the map actual gravity applied to each entity
	 * @return float gravity
	 */
	public float getGravity() {
		return this.mapGravity;
	}
	
	/**
	 * Set the gravity to apply on this world 
	 * @param gravity float gravity factor
	 */
	public void setGravity(float gravity) {
		this.mapGravity = gravity;
	}
	
	/**
	 * @return {@link MapType} of the map
	 */
	public abstract MapType getType();
	
	public GUIImage getBackground() {
		return background;
	}

	public void setBackground(GUIImage background) {
		this.background = background;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[map]:\n");
		builder.append("\tname: "+name+"\n");
		builder.append("\tobstacles: "+obstacles+"\n");
		return builder.toString();
	}
}
