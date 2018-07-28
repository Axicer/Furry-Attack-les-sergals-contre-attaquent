package fr.axicer.furryattack.entity;

import org.joml.Vector2f;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.map.MapObstacle;
import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.util.collision.CollisionBoxM;

public abstract class Entity extends CollisionBoxM implements Renderable, Updateable{
	
	//position of the entity
	protected Vector2f pos;
	//entity's acceleration
	protected Vector2f acc;
	//default gliding movement coeff (higher is more gliding)
	private float glidingCoeff = 0.7f;
	//default air braking movement coeff (higher is less movement)
	private float airBrakingCoeff = 0.999f;
	//whether an entity is on ground or not
	private boolean onGround;
	
	//amount of step for each movement
	public static float STEP = 10000.0f;
	
	/**
	 * Empty entity constructor
	 */
	public Entity() {
		this.pos = new Vector2f();
		this.acc = new Vector2f();
		setBoxBounds();
	}
	
	private void setBoxBounds() {
		updatePos(new Vector2f(pos.x - getWidth()/2, pos.y - getHeight()/2),
				new Vector2f(pos.x - getWidth()/2, pos.y + getHeight()/2),
				new Vector2f(pos.x + getWidth()/2, pos.y + getHeight()/2),
				new Vector2f(pos.x + getWidth()/2, pos.y - getHeight()/2));
	}
	
	/**
	 * @return float width of the entity used both in rendering and collision detection
	 */
	protected abstract float getWidth();
	/**
	 * @return float height of the entity used both in rendering and collision detection
	 */
	protected abstract float getHeight();
	
	/**
	 * Move the entity by acc vector
	 * If a collision is detected then the entity is stopped at the last available movement on the corresponding axis
	 */
	public void move() {
		//first is to apply gravity which is actually not done yet
		applyGravityToAccelerationVector();
		
		//then move on X axis
		//amount of movement on X axis depending of the number of step 
		float stepX = acc.x/STEP;
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
		float stepY = acc.y/STEP;
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
						setOnGround(true);
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
		//aply coef gliding coeff and air braking coeff
		acc.set(acc.x*glidingCoeff*airBrakingCoeff, acc.y*airBrakingCoeff);
	}
	
	/**
	 * Apply gravity on Y axis (depending on the actual map)
	 */
	private void applyGravityToAccelerationVector() {
		//if we're not on ground
		if(!onGround) {
			//subtract gravity from Y acceleration
			acc.set(acc.x, acc.y-FurryAttack.getInstance().getMapManager().getMap().getGravity());
		} else {
			//reset acceleration on Y axis
			acc.set(acc.x, 0);
		}
	}

	/**
	 * Default entity render method which only shows the collision box
	 */
	@Override
	public void render() {
		super.render();
	}
	
	@Override
	public void update() {
		setBoxBounds();
	}
	
	public boolean isOnGround() {
		return onGround;
	}

	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}
	
	public Vector2f getAccelerationVector() {
		return this.acc;
	}
	
	public void setAccelerationVector(Vector2f acc) {
		this.acc = acc;
	}
	
	/**
	 * Set the gliding coefficient to apply to the acceleration vector
	 * @param glidingCoeff float gliding coefficient
	 */
	public void setGlidingCoeff(float glidingCoeff) {
		this.glidingCoeff = glidingCoeff;
	}
	
	/**
	 * Returns the actual gliding coefficient applied to the acceleration vector
	 * @return float gliding coefficient
	 */
	public float getGlidingCoeff() {
		return this.glidingCoeff;
	}
	
	/**
	 * Set the air Braking coefficient to apply to the acceleration vector
	 * @param airBrakingCoeff float gliding coefficient
	 */
	public void setAirBrakingCoeff(float airBrakingCoeff) {
		this.airBrakingCoeff = airBrakingCoeff;
	}
	
	/**
	 * Returns the actual air Braking coefficient applied to the acceleration vector
	 * @return float air Braking coefficient
	 */
	public float getairBrakingCoeff() {
		return this.airBrakingCoeff;
	}
}
