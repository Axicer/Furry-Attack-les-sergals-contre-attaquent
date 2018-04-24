package fr.axicer.furryattack.gui.guis;

import java.util.ArrayList;
import java.util.List;

import fr.axicer.furryattack.gui.elements.GUIComponent;
import fr.axicer.furryattack.render.Destroyable;
import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;

public abstract class GUI implements Renderable, Updateable, Destroyable{
	
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
	
	public abstract void recreate();

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GUI other = (GUI) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
