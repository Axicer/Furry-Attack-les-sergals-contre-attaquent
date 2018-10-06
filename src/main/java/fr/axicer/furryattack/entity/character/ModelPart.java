package fr.axicer.furryattack.entity.character;

/**
 * Enum of all characters common parts
 * @author Axicer
 *
 */
public enum ModelPart {
	//TODO define real width and height
	BODY(0,0f,0f),
	HEAD(1,0f,0f),
	LEFT_LEG(2,0f,0f),
	RIGHT_LEG(3,0f,0f),
	LEFT_ARM(4,0f,0f),
	RIGHT_ARM(5,0f,0f),
	LEFT_HAND(6,0f,0f),
	RIGHT_HAND(7,0f,0f);
	
	//hard coded id used to ensure to match Animation data
	private int id;
	//part size (always the same for each model so hard coded to)
	private float width,height;
	
	/**
	 * {@link ModelPart} constructor
	 * @param id {@link Integer} hard coded ID
	 * @param width {@link Float} hard coded part's width
	 * @param height {@link Float} hard coded part's height 
	 */
	private ModelPart(int id, float width, float height) {
		this.id = id;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * @return {@link ModelPart} ID
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return {@link ModelPart} width
	 */
	public float getWidth() {
		return width;
	}
	
	/**
	 * @return {@link ModelPart} height
	 */
	public float getHeight() {
		return height;
	}
}
