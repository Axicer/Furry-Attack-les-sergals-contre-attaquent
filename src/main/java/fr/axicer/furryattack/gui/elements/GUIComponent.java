package fr.axicer.furryattack.gui.elements;

import org.joml.Vector3f;

import fr.axicer.furryattack.gui.guis.GUI;
import fr.axicer.furryattack.render.Destroyable;
import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;

/**
 * An abstract component of the GUI
 * @author Axicer
 *
 */
public abstract class GUIComponent implements Renderable, Updateable, Destroyable{
	
	/**
	 * The position of the element
	 */
	protected Vector3f pos;
	/**
	 * The rotation of the element
	 */
	protected float rot;
	/**
	 * The parent's GUI
	 */
	protected GUI gui;
	/**
	 * The alignement (point the component should render)
	 */
	protected GUIAlignement alignement;
	
	/**
	 * An abstract {@link GUIComponent} with no rotation and a position of (0,0,0)
	 */
	public GUIComponent() {
		rot = 0f;
		pos = new Vector3f();
	}
	
	/**
	 * An abstract {@link GUIComponent} with given parameters
	 * @param pos {@link Vector3f} position of the component
	 * @param rot float rotation of the component
	 */
	public GUIComponent(Vector3f pos, float rot) {
		this.pos = pos;
		this.rot = rot;
	}

	/**
	 * Return the component's position
	 * @return {@link Vector3f} position
	 */
	public Vector3f getPosition() {
		return pos;
	}

	/**
	 * Set the component's position
	 * @param pos {@link Vector3f} position
	 */
	public void setPosition(Vector3f pos) {
		this.pos = pos;
	}

	/**
	 * Get the component's rotation
	 * @return float rotation
	 */
	public float getRotation() {
		return rot;
	}

	/**
	 * Set the component's rotation
	 * @param rot float rotation
	 */
	public void setRotation(float rot) {
		this.rot = rot;
	}
	
	/**
	 * Get the component's alignement
	 * @return {@link GUIAlignement} alignement
	 */
	public GUIAlignement getGUIAlignement() {
		return this.alignement;
	}
	/**
	 * Set the {@link GUIAlignement} of this component
	 * @param alignement {@link GUIAlignement} alignement
	 */
	public abstract void setGUIAlignement(GUIAlignement alignement);
}
