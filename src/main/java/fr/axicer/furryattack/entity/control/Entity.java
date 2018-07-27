package fr.axicer.furryattack.entity.control;

import org.joml.Vector2f;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.map.MapObstacle;
import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.util.collision.CollisionBoxM;

public abstract class Entity extends CollisionBoxM implements Renderable, Updateable{
	
	//position of the entity
	protected Vector2f pos;
	//whether an entity is on ground or not
	protected boolean onGround;
	
	//amount of step for each movement
	public static float STEP = 10000.0f;
	
	/**
	 * Empty entity constructor
	 */
	public Entity() {}
	
	/**
	 * @return float width of the entity used both in rendering and collision detection
	 */
	protected abstract float getWidth();
	/**
	 * @return float height of the entity used both in rendering and collision detection
	 */
	protected abstract float getHeight();
	
	/**
	 * Move the entity by an amount of movement on X and Y axis
	 * If a collision is detected then the entity is stopped at the last available movement on the corresponding axis
	 * @param x amount of movement on the X axis
	 * @param y amount of movement on the Y axis
	 */
	public void move(float x, float y) {
		//move on X axis
		//amount of movement on X axis depending of the number of step 
		float stepX = x/STEP;
		//var to stop when border is detected
		boolean stopX = false;
		//looping for each step
		for(int i = 0 ; i < STEP ; i++) {
			//checking for each obstacle
			for(MapObstacle obstacle : FurryAttack.getInstance().getMapManager().getMap().getObstacles()) {
				//means "movement on X axis by a step will collide on left"
				boolean posX = obstacle.isInside(pos.x + stepX - getWidth()/2, pos.y);
				//means "movement on X axis by a step will collide on right"
				boolean negX = obstacle.isInside(pos.x + stepX + getWidth()/2, pos.y);
				//if we collide on any side
				if(negX || posX) {
					//should stop incrementing the variable
					stopX = true;
					//break this loop
					break;
				}
			}
			//if there is no collision on the next step add a step
			if(!stopX)pos.x+=stepX;
			//else stop incrementing on X axis
			else break;
		}
		
		//move on Y axis
		//amout of movement on Y axis to increment depending of the number of steps
		float stepY = y/STEP;
		//variable use to stop incrementing Y axis
		boolean stopY = false;
		//looping for each step
		for(int i = 0 ; i < STEP ; i++) {
			//looping on each obstacle
			for(MapObstacle obstacle : FurryAttack.getInstance().getMapManager().getMap().getObstacles()) {
				//means "will collide on next step on Y axis at top"
				boolean posY = obstacle.isInside(pos.x , pos.y + stepY + getHeight()/2);
				//means "will collide on next step on Y axis at bottom"
				boolean negY = obstacle.isInside(pos.x , pos.y + stepY - getHeight()/2);
				//if a collision is detected on any side
				if(negY || posY) {
					//should stop incrementing
					stopY = true;
					//if it will collide on bottom
					if(negY) {
						//define entity on ground
						onGround = true;
					}
					//stop checking obstacles
					break;
				}
			}
			//if there is no collision on next step then increment by a step on Y axis
			if(!stopY)pos.y+=stepY;
			//else stop incrementing
			else break;
		}
	}
}
