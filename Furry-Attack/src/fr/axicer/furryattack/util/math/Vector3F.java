package fr.axicer.furryattack.util.math;

public class Vector3F extends Vector2F{

	public float z;
	
	public Vector3F(float x, float y, float z) {
		super(x, y);
		this.z = z;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
	
	@Override
	public double length() {
		return Math.sqrt(x*x+y*y+z*z);
	}
	
	@Override
	public void normalize() {
		double lgt = length();
		x /= lgt;
		y /= lgt;
		z /= lgt;
	}

	public Vector3F copy() {
		return new Vector3F(x, y, z);
	}
	
	public String toString() {
		return x+","+y+","+z;
	}
}
