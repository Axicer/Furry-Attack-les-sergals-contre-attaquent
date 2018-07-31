package fr.axicer.furryattack.entity;

import org.joml.Vector2f;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.entity.animation.Animation;
import fr.axicer.furryattack.entity.animation.AnimationsType;
import fr.axicer.furryattack.entity.gun.AbstractGun;
import fr.axicer.furryattack.entity.gun.list.BerettaGun;
import fr.axicer.furryattack.map.MapObstacle;
import fr.axicer.furryattack.render.Destroyable;
import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.util.Constants;
import fr.axicer.furryattack.util.collision.CollisionBoxM;

public abstract class Entity extends CollisionBoxM implements Renderable, Updateable, Destroyable{
	
	//entity's race
	protected Species race;
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
	//is the entity reverted (look on the left)
	protected boolean revert;
	//entity's animation
	protected Animation animation;
	// if an entity is shifted
	protected boolean shifted;
	//gun the entity is holding
	protected AbstractGun gun;
	
	//amount of step for each movement
	public static float STEP = 100.0f;
	
	/**
	 * Empty entity constructor
	 */
	public Entity(Species race) {
		this.race = race;
		this.shifted = false;
		this.animation = new Animation(race);
		this.pos = new Vector2f();
		this.acc = new Vector2f();
		this.revert = false;
		this.gun = new BerettaGun(10);
		setBoxBounds();
	}
	
	private void setBoxBounds() {
		//a character is only made of a point at the entity's bottom (centered) not at center
		float height = shifted ? getShiftedHeight() : getHeight();
		updatePos(new Vector2f(pos.x - getWidth()*Constants.WIDTH/2, pos.y),
				new Vector2f(pos.x - getWidth()*Constants.WIDTH/2, pos.y + height*Constants.HEIGHT),
				new Vector2f(pos.x + getWidth()*Constants.WIDTH/2, pos.y + height*Constants.HEIGHT),
				new Vector2f(pos.x + getWidth()*Constants.WIDTH/2, pos.y));
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
	 * @return float shifted height of the entity used both in rendering and collision detection
	 */
	protected abstract float getShiftedHeight();
	
	public Vector2f getArmJunctionPosition() {
		return new Vector2f(pos.x/Constants.WIDTH/*+(revert ? -getWidth() : getWidth())/1.5f*/,
				pos.y/Constants.HEIGHT+(shifted ? getShiftedHeight() : getHeight())/2f);
	}
	
	/**
	 * Move the entity by acc vector
	 * If a collision is detected then the entity is stopped at the last available movement on the corresponding axis
	 */
	public void move() {
		//first is to apply gravity which is actually not done yet
		applyGravityToAccelerationVector();
		
		float height = shifted ? getShiftedHeight() : getHeight();
		
		//amount of movement on X axis depending of the number of step 
		float stepX = acc.x/STEP;
		//var to stop when border is detected
		boolean stopX = false;
		//amout of movement on Y axis to increment depending of the number of steps
		float stepY = acc.y/STEP;
		//variable use to stop incrementing Y axis
		boolean stopY = false;
		//looping for each step
		for(int i = 0 ; i < STEP ; i++) {
			//checking for each obstacle
			for(MapObstacle obstacle : FurryAttack.getInstance().getMapManager().getMap().getObstacles()) {
				//colliding on one border
				boolean posX = false, negX = false;
				boolean posY = false, negY = false;
				//for steps on Y axis
				for(float k = 0f ; k < height ; k+= height/20f) {
					//means "movement on X axis by a step will collide on left"
					posX = obstacle.isInside(pos.x + stepX - getWidth()*Constants.WIDTH/2f, pos.y+k*Constants.HEIGHT);
					//means "movement on X axis by a step will collide on right"
					negX = obstacle.isInside(pos.x + stepX + getWidth()*Constants.WIDTH/2f, pos.y+k*Constants.HEIGHT);
					//if we collide on any side
					if(negX || posX) {
						//should stop incrementing the variable
						stopX = true;
						break;
					}
				}
				for(float j = -getWidth()/2f ; j < getWidth()/2f ; j+=getWidth()/20f) {
					//means "will collide on next step on Y axis at top"
					posY = obstacle.isInside(pos.x+j*Constants.WIDTH , pos.y + stepY + (shifted ? getShiftedHeight() : getHeight())*Constants.HEIGHT);
					//means "will collide on next step on Y axis at bottom"
					negY = obstacle.isInside(pos.x+j*Constants.WIDTH, pos.y + stepY);
					//if a collision is detected on any side
					if(negY || posY) {
						//should stop incrementing
						stopY = true;
						//if it will collide on bottom
						if(negY) {
							//define entity on ground
							setOnGround(true);
						}
						break;
					}
				}
			}
			//if there is no collision on the next step add a step
			if(!stopX)pos.x+=stepX;
			//if there is no collision on next step then increment by a step on Y axis
			if(!stopY)pos.y+=stepY;
			if(stopX && stopY)break;
		}
		//if acceleration is less then 1 pixels cancel
		if(Math.abs(acc.x) < 1f)acc.x = 0f;
		if(Math.abs(acc.y) < 1f)acc.y = 0f;
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
		//set the gun position to the entity shoulder
		gun.getPos().set(getArmJunctionPosition());
		//set the gun reverted
		gun.setReverted(revert);
		//update gun's rotation
		gun.update();
		//change entity orientation depending on the gun orientation
		if(gun.getRot() < 3f*(float)Math.PI/2f && !revert) {
			revert = true;
		}else if(gun.getRot() > 3f*(float)Math.PI/2f && revert) {
			revert = false;
		}
		//set entity's animation type
		if(onGround) {
			if(acc.x == 0) {
				animation.setAnimationType(shifted ? AnimationsType.SHIFT : AnimationsType.STAY);
			}else {
				animation.setAnimationType(AnimationsType.WALK);
			}
		}else {
			animation.setAnimationType(AnimationsType.JUMP);
		}
		setBoxBounds();
		animation.update();
	}
	
	@Override
	public void destroy() {
		animation.destroy();
		gun.destroy();
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
	
	public Animation getAnimation() {
		return this.animation;
	}
	
	public void setShifted(boolean shift) {
		this.shifted = shift;
	}
}
