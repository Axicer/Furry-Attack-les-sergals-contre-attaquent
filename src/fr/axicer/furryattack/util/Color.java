package fr.axicer.furryattack.util;

import org.joml.Vector4f;

public class Color extends Vector4f{
	
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
}
