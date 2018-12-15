package fr.axicer.furryattack.util;

import org.joml.Vector4f;

public class Color extends Vector4f{
	
	public static Color WHITE = new Color(255, 255, 255, 255);
	public static Color BLACK = new Color(0, 0, 0, 255);
	public static Color TRANSPARENT = new Color(0, 0, 0, 0);
	
	/**
	 * Constructor of a Color
	 * @param r red color from 0 to 255
	 * @param g green color from 0 to 255
	 * @param b blue color from 0 to 255
	 * @param a alpha color from 0 to 255
	 */
	public Color(float r, float g, float b, float a) {
		this.x = bound(0, 255f, r)/255f;
		this.y = bound(0, 255f, g)/255f;
		this.z = bound(0, 255f, b)/255f;
		this.w = bound(0, 255f, a)/255f;
	}
	
	private float bound(float low, float high, float val) {
		float r = val > high ? high : val;
		r = val < low ? low : val;
		return r;
	}
	
	/**
	 * Generate a color by an int
	 * @param value the int data as R, G, B, A value with 8 bits for each value
	 * @return {@link Color} class
	 */
	public static Color fromInt(int value) {
		int r = (value >> 24) & 0xFF;
		int g = (value >> 16) & 0xFF;
		int b = (value >> 8) & 0xFF;
		int a = value & 0xFF;
		
		return new Color(r, g, b, a);
	}
	
	/**
	 * Get a  int color from a color class
	 * @param color {@link Color} to get from
	 * @return an int containing all data
	 */
	public static int toInt(Color color) {
		int r = (int) (color.x*255);
		int g = (int) (color.y*255);
		int b = (int) (color.z*255);
		int a = (int) (color.w*255);
		
		return (r << 24) | (g << 16) | (b << 8) | a;
	}
}
