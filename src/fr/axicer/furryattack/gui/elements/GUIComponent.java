package fr.axicer.furryattack.gui.elements;

import org.joml.Vector3f;

import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;

public abstract class GUIComponent implements Renderable, Updateable{
	
	protected Vector3f pos;
	protected float rot;
	
	public GUIComponent() {
		rot = 0f;
		pos = new Vector3f();
	}
	
	public GUIComponent(Vector3f pos, float rot) {
		this.pos = pos;
		this.rot = rot;
	}

	public Vector3f getPosition() {
		return pos;
	}

	public void setPosition(Vector3f pos) {
		this.pos = pos;
	}

	public float getRotation() {
		return rot;
	}

	public void setRotation(float rot) {
		this.rot = rot;
	}
	
	public abstract void destroy();
}
