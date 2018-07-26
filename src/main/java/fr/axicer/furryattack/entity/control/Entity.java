package fr.axicer.furryattack.entity.control;

import org.joml.Vector2f;

import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.util.collision.CollisionBoxM;

public abstract class Entity extends CollisionBoxM implements Renderable, Updateable{
	
	protected Vector2f pos;
	protected boolean onGround;
	
	public static float STEP = 10000.0f;
	
	public Entity() {}
	
	public void move(float x, float y) {
		float stepX = x/STEP;
		for(int i = 0 ; i < STEP ; i++) {
			pos.x+=stepX;
		}
		float stepY = y/STEP;
		for(int i = 0 ; i < STEP ; i++) {
			pos.y+=stepY;
		}
	}
}
