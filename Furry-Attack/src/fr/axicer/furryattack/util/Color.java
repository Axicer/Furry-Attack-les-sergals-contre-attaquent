package fr.axicer.furryattack.util;

import fr.axicer.furryattack.util.math.Vector3F;

public class Color extends Vector3F{

	public float a;
	
	public Color(float r, float g, float b, float a) {
		super(r, g, b);
		this.x = bound(0, 255, r);
		this.y = bound(0, 255, g);
		this.z = bound(0, 255, b);
		this.a = bound(0, 255, a);
	}
	
	private float bound(float low, float high, float val) {
		float r = val > high ? high : val;
		r = val < low ? low : val;
		return r;
	}
	
	public int toShiftedValue() {
		int r = (int)x;
		int g = (int)y;
		int b = (int)z;
		return (r << 24) | (g << 16) | (b << 8) | (int)a;
	}
	public static Color fromShiftedValue(int val) {
		int r = (val >> 24) & 0xFF;
		int g = (val >> 16) & 0xFF;
		int b = (val >> 8) & 0xFF;
		int a = val & 0xFF;
		return new Color(r, g, b, a);
	}
}
