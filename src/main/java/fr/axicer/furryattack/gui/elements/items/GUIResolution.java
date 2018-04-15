package fr.axicer.furryattack.gui.elements.items;

import fr.axicer.furryattack.gui.elements.selector.GUISelectorItem;
import fr.axicer.furryattack.util.Resolution;

public class GUIResolution extends GUISelectorItem<Resolution>{

	public GUIResolution(Resolution val, String name) {
		super(val, name);
	}
	
	public boolean equals(GUIResolution resolution) {
		return getVal().equals(resolution.getVal());
	}

	@Override
	public String toString() {
		return "GUIResolution [getName()=" + getName() + ", getVal()=" + getVal() + "]";
	}
	
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GUIResolution other = (GUIResolution) obj;
		if (!getName().equals(other.getName()))
			return false;
		if (!getVal().equals(other.getVal()))
			return false;
		return true;
	}
}
