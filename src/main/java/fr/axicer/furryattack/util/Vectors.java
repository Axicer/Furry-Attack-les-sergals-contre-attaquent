package fr.axicer.furryattack.util;

import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Simple default vectors to use 
 * @author Axicer
 *
 */
public class Vectors {
	
	public static final Vector2f EMPTY_2F = new Vector2f();
	public static final Vector3f EMPTY_3F = new Vector3f();
	
	public static final Vector2f UP_2F = new Vector2f(0,1);
	public static final Vector2f DOWN_2F = new Vector2f(0,-1);
	public static final Vector2f LEFT_2F = new Vector2f(-1,0);
	public static final Vector2f RIGHT_2F = new Vector2f(1,0);
	
	public static final Vector2f UP_LEFT_2F = new Vector2f(-1,1);
	public static final Vector2f UP_RIGHT_2F = new Vector2f(1,1);
	public static final Vector2f DOWN_LEFT_2F = new Vector2f(-1,-1);
	public static final Vector2f DOWN_RIGHT_2F = new Vector2f(1,-1);
	
	public static final Vector3f UP_3F = new Vector3f(0,1,0);
	public static final Vector3f DOWN_3F = new Vector3f(0,-1,0);
	public static final Vector3f LEFT_3F = new Vector3f(-1,0,0);
	public static final Vector3f RIGHT_3F = new Vector3f(1,0,0);
	public static final Vector3f FRONT_3F = new Vector3f(0,0,1);
	public static final Vector3f BACK_3F = new Vector3f(0,0,-1);
	
	/**
	 * Rotate the current vector by a given angle
	 * @param v {@link Vector2f} to rotate
	 * @param rot {@link Float} angle in radians
	 * @return the given {@link Vector2f} rotated
	 */
	public static Vector2f rotate(Vector2f v, float rot) {
		float x = (float) (v.x*Math.cos(rot)-v.y*Math.sin(rot));
		float y = (float) (v.x*Math.sin(rot)+v.y*Math.cos(rot));
		v.x = x;
		v.y = y;
		return v;
	}
	
	/**
	 * Rotate the current vector by a given angle and get a copy
	 * @param v {@link Vector2f} to rotate
	 * @param rot {@link Float} angle in radians
	 * @return a new {@link Vector2f} corresponding to the given vector rotated
	 */
	public static Vector2f rotateCopy(Vector2f v, float rot) {
		Vector2f copy = new Vector2f(v);
		rotate(copy, rot);
		return copy;
	}
}
