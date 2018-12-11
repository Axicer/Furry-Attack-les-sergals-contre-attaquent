package fr.axicer.furryattack.entity;

import org.joml.Vector2f;

import fr.axicer.furryattack.render.Destroyable;
import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;

public abstract class Entity implements Renderable, Updateable, Destroyable{
	
	//entity's race
	protected Species race;
	//position of the entity
	protected Vector2f pos;
	//entity's acceleration
	protected Vector2f acc;
	//default gliding movement coeff (higher is more gliding)
	private float glidingCoeff = 0.7f;
	//default air braking movement coeff (higher is less movement)
	private float airBrakingCoeff = 0.7f;
	//whether an entity is on ground or not
	protected boolean onGround;
	//is the entity reverted (look on the left)
	protected boolean revert;
	// if an entity is shifted
	protected boolean shifted;
	
	//amount of step for each movement
	public static float STEP = 100.0f;
	
	/**
	 * Empty entity constructor
	 */
	public Entity(Species race) {
		this.race = race;
		this.shifted = false;
		this.pos = new Vector2f();
		this.acc = new Vector2f();
		this.revert = false;
	}
	
	/**
	 * Move the entity by acc vector
	 * If a collision is detected then the entity is stopped at the last available movement on the corresponding axis
	 */
	public void move() {
		//TODO apply gravity when collision will be done
		//first is to apply gravity which is actually not done yet
//		applyGravityToAccelerationVector();
		
		pos.add(acc);
		
		//if acceleration is less then 1 pixels cancel
		if(Math.abs(acc.x) < 1f)acc.x = 0f;
		if(Math.abs(acc.y) < 1f)acc.y = 0f;
		//aply coef gliding coeff and air braking coeff
		acc.set(acc.x*glidingCoeff*airBrakingCoeff, acc.y*airBrakingCoeff);
		
	}
	
	/**
	 * Apply gravity on Y axis (depending on the actual map)
	 */
//	private void applyGravityToAccelerationVector() {
//		//if we're not on ground
//		if(!onGround) {
//			//subtract gravity from Y acceleration
//			acc.set(acc.x, acc.y-FurryAttack.getInstance().getMapManager().getMap().getGravity());
//		} else {
//			//reset acceleration on Y axis
//			acc.set(acc.x, 0);
//		}
//	}

	/**
	 * Default entity render method which only shows the collision box
	 */
	@Override
	public void render() {}
	
	@Override
	public void update() {}
	
	@Override
	public void destroy() {}
	
	public boolean isOnGround() {
		return onGround;
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
	
	public Vector2f getPosition() {
		return this.pos;
	}
	
	public void setGrounded(boolean value) {
		this.onGround = value;
	}
	
	public void setShifted(boolean value) {
		this.shifted = value;
	}
	
	public boolean isReverted() {
		return revert;
	}
	
}
