package fr.axicer.furryattack.gui;

import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;

public abstract class GUI implements Renderable, Updateable{
	
	public String id;

	public GUI(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
