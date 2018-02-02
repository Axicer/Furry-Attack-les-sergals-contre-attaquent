package fr.axicer.furryattack.gui;

import java.util.ArrayList;
import java.util.List;

import fr.axicer.furryattack.gui.elements.GUIComponent;
import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;

public abstract class GUI implements Renderable, Updateable{
	
	protected String id;
	protected List<GUIComponent> components;

	public GUI(String id) {
		this.id = id;
		this.components = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<GUIComponent> getComponents() {
		return components;
	}
	
	public abstract void destroy();
}
