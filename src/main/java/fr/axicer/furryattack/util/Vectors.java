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
}
