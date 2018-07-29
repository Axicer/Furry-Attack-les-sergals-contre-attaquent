package fr.axicer.furryattack.gui.elements;

import org.joml.Matrix3f;

/**
 * Alignement of a component
 * @author Axicer
 *
 */
public enum GUIAlignement {
	TOP_LEFT(0,0),		TOP(0,1),		TOP_RIGHT(0,2),
	LEFT(1,0),			CENTER(1,1),	RIGHT(1,2),
	BOTTOM_LEFT(2,0),	BOTTOM(2,1),	BOTTOM_RIGHT(2,2);
	
	private static final Matrix3f centerOffsetXmul = new Matrix3f(0.5f, 	0f, 	-0.5f,
																  0.5f, 	0f, 	-0.5f,
																  0.5f, 	0f, 	-0.5f);
	private final static Matrix3f centerOffsetYmul = new Matrix3f(-0.5f, 	-0.5f,	-0.5f,
																  0f, 		0f, 	0f,
																  0.5f, 	0.5f, 	0.5f);
	private static final Matrix3f reverseCenterOffsetXmul = new Matrix3f(-0.5f, 	0f, 	0.5f,
			  															 -0.5f, 	0f, 	0.5f,
			  															 -0.5f, 	0f, 	0.5f);
	private static final Matrix3f reverseCenterOffsetYmul = new Matrix3f(0.5f, 		0.5f,	0.5f,
			  															 0f, 		0f, 	0f,
			  															 -0.5f, 	-0.5f, 	-0.5f);
	/**
	 * matrices position
	 */
	private int x,y;
	
	/**
	 * Empty constructor
	 */
	private GUIAlignement(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Get the x offset from center depending of the component width
	 * @param originalComponentWidth int component's width
	 * @return int offset x
	 */
	public float getOffsetXfromCenter(float originalComponentWidth) {
		return centerOffsetXmul.get(x, y)*originalComponentWidth;
	}
	
	/**
	 * Get the y offset from center depending of the component height
	 * @param originalComponentHeight int component's height
	 * @return int offset y
	 */
	public float getOffsetYfromCenter(float originalComponentHeight) {
		return centerOffsetYmul.get(x, y)*originalComponentHeight;
	}
	
	public float getFrameOffsetX(float width) {
		return reverseCenterOffsetXmul.get(x, y)*width;
	}
	
	public float getFrameOffsetY(float height) {
		return reverseCenterOffsetYmul.get(x, y)*height;
	}
}
