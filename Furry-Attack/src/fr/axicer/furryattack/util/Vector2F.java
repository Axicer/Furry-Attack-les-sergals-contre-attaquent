package fr.axicer.furryattack.util;

public class Vector2F {
	
	protected float x,y;
	
	public Vector2F(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public double length() {
		return Math.sqrt(x*x*+y*y);
	}
	
	public void normalize() {
		double lgt = length();
		x /= lgt;
		y /= lgt;
	}
	
	public Vector2F copy() {
		return new Vector2F(x, y);
	}
}
