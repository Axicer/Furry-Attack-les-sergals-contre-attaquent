package fr.axicer.furryattack.gui.elements;

/**
 * Alignement of a component
 * @author Axicer
 *
 */
public enum GUIAlignement {
	TOP_LEFT,	TOP,	TOP_RIGHT,
	LEFT,		CENTER,	RIGHT,
	BOTTOM_LEFT,BOTTOM,	BOTTOM_RIGHT;
	/**
	 * Empty constructor
	 */
	private GUIAlignement() {}
	
	/**
	 * Get the x offset from center depending of the component width
	 * @param originalComponentWidth int component's width
	 * @return int offset x
	 */
	public int getOffsetXfromCenter(int originalComponentWidth) {
		switch(this) {
		case BOTTOM:
			return 0;
		case CENTER:
			return 0;
		case TOP:
			return 0;
		case BOTTOM_LEFT:
			return originalComponentWidth/2;
		case BOTTOM_RIGHT:
			return -originalComponentWidth/2;
		case LEFT:
			return originalComponentWidth/2;
		case RIGHT:
			return -originalComponentWidth/2;
		case TOP_LEFT:
			return originalComponentWidth/2;
		case TOP_RIGHT:
			return -originalComponentWidth/2;
		default:
			return 0;
		}
	}
	
	/**
	 * Get the y offset from center depending of the component height
	 * @param originalComponentHeight int component's width
	 * @return int offset y
	 */
	public int getOffsetYfromCenter(int originalComponentHeight) {
		switch(this) {
		case BOTTOM:
			return originalComponentHeight/2;
		case BOTTOM_LEFT:
			return originalComponentHeight/2;
		case BOTTOM_RIGHT:
			return originalComponentHeight/2;
		case CENTER:
			return 0;
		case LEFT:
			return 0;
		case RIGHT:
			return 0;
		case TOP:
			return -originalComponentHeight/2;
		case TOP_LEFT:
			return -originalComponentHeight/2;
		case TOP_RIGHT:
			return -originalComponentHeight/2;
		default:
			return 0;
		}
	}
}
