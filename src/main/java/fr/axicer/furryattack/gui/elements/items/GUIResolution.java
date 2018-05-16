package fr.axicer.furryattack.gui.elements.items;

import fr.axicer.furryattack.gui.elements.selector.GUISelectorItem;
import fr.axicer.furryattack.util.Resolution;

/**
 * A resolution-name tuple
 * @author Axicer
 *
 */
public class GUIResolution extends GUISelectorItem<Resolution>{

	/**
	 * Constructor of a {@link GUIResolution}
	 * @param val {@link Resolution} to set
	 * @param name {@link String} name associated
	 */
	public GUIResolution(Resolution val, String name) {
		super(val, name);
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
